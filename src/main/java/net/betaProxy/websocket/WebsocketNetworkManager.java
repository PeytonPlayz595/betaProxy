package net.betaProxy.websocket;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.framing.BinaryFrame;
import org.java_websocket.framing.DataFrame;

import net.betaProxy.server.Server;
import net.betaProxy.utils.ClientServerProtocolMatcher;
import net.betaProxy.utils.ProtocolAwarePacketReader;
import net.betaProxy.utils.ServerProtocolVersion;
import net.lax1dude.log4j.LogManager;
import net.lax1dude.log4j.Logger;

import dev.colbster937.utils.ProxyProtocol;

public class WebsocketNetworkManager {

	public Socket socket;
	private DataInputStream socketInputStream;
	private DataOutputStream socketOutputStream;
	private Thread readerThread = null;

	private WebSocket webSocket;
	private volatile boolean running;
	private boolean isShuttingDown = false;
	private boolean isQuerySocket = false;
	private boolean hasSentProxy = false;

	private Map<String, String> userInfo;

	private long socketLastRead = System.currentTimeMillis();
	private long webSocketLastRead = System.currentTimeMillis();

	public static Logger LOGGER = LogManager.getLogger("NetworkManager");
	private String ip;

	private ClientServerProtocolMatcher protocolMatcher;
	public ProtocolAwarePacketReader packetReader;
	private ServerProtocolVersion spv;
	private Server server;

	public WebsocketNetworkManager(WebSocket webSocket, Server server, ServerProtocolVersion spv, Map<String, String> userInfo) throws IOException {
		this.webSocket = webSocket;
		this.server = server;
		this.socket = new Socket();
		this.socket.setSoTimeout(3000);
		this.socket.setTcpNoDelay(true);
		this.socket.setTrafficClass(24);
		this.socket.connect(server.getMinecraftSocketAddress());
		this.socketInputStream = new DataInputStream(socket.getInputStream());
		this.socketOutputStream = new DataOutputStream(socket.getOutputStream());
		this.running = true;
		final String s = Thread.currentThread().getName();
		this.ip = webSocket.getRemoteSocketAddress().getHostString();
		this.spv = spv;
		this.userInfo = userInfo;
		this.protocolMatcher = new ClientServerProtocolMatcher(this.spv);
		this.packetReader = new ProtocolAwarePacketReader(server, this.spv);
		this.readerThread = new Thread(() -> {
			Thread.currentThread().setName(s);
			while (running) {
				try {
					checkDisconnected();
					readPacket();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		this.readerThread.start();
		LOGGER.info(webSocket.getRemoteSocketAddress().toString() + " connected!");
	}

	public void setQuerySocket() {
		this.isQuerySocket = true;
	}

	public void addToSendQueue(ByteBuffer pkt) {
		if (this.isConnectionOpen()) {
			byte[] data = new byte[pkt.remaining()];
			pkt.get(data);
			if (!protocolMatcher.hasMatched) {
				protocolMatcher.attemptMatch(pkt);
			}
			if (protocolMatcher.isError) {
				if (!protocolMatcher.hasSent) {
					byte[] error;
					if (protocolMatcher.outdatedClient) {
						error = generateDisconnectPacket("Outdated client");
					} else if (protocolMatcher.outdatedServer) {
						error = generateDisconnectPacket("Outdated server");
					} else {
						error = generateDisconnectPacket("Internal protocol error");
					}

					if (!this.isQuerySocket) {
						try {
							this.socketOutputStream.write(error);
							this.socketOutputStream.flush();
						} catch (Exception e) {
						}
						try {
							DataFrame frame = new BinaryFrame();
							frame.setPayload(ByteBuffer.wrap(error));
							frame.setFin(true);
							this.webSocket.sendFrame(frame);
						} catch (Exception e) {
						}
					}
					protocolMatcher.hasSent = true;
				}
				return; // Wait for proxy to time out connection
			}
			try {
				if (this.server.isIPForwardingEnabled() && !this.hasSentProxy && !this.isQuerySocket) {
					InetSocketAddress mcAddress = this.server.getMinecraftSocketAddress();
					this.addToSendQueue(
							ByteBuffer.wrap(ProxyProtocol.buildProxyHeader(userInfo.get("ip"), Integer.parseInt(userInfo.get("port")),
									mcAddress.getHostString(), mcAddress.getPort())));
				}
				this.socketOutputStream.write(data);
				this.socketOutputStream.flush();
				this.webSocketLastRead = System.currentTimeMillis();
			} catch (Exception e) {
			}
		}
	}

	void checkDisconnected() {
		long currentTime = System.currentTimeMillis();

		if (this.isShuttingDown || !this.running) {
			return;
		}

		boolean disconnected = !this.isConnectionOpen() || !this.isWebSocketOpen();
		if (currentTime >= (socketLastRead + server.getTimeout() * 1000)
				|| currentTime >= (webSocketLastRead + server.getTimeout() * 1000) || disconnected) {
			if (this.isConnectionOpen()) {
				if (!isQuerySocket)
					this.addToSendQueue(
							ByteBuffer.wrap(generateDisconnectPacket(disconnected ? "Connection closed" : "Timed out")));
				try {
					this.socket.close();
				} catch (IOException e) {
				}
			}
			LOGGER.info(ip + " disconnected!");
			if (this.isWebSocketOpen()) {
				if (!isQuerySocket) {
					try {
						DataFrame frame = new BinaryFrame();
						frame.setPayload(
								ByteBuffer.wrap(generateDisconnectPacket(disconnected ? "Connection closed" : "Timed out")));
						frame.setFin(true);
						this.webSocket.sendFrame(frame);
					} catch (Exception e) {
					}
				}
				try {
					this.webSocket.close();
				} catch (Exception e) {
				}
			}
			this.running = false;
		}
	}

	private static byte[] disconnect = new byte[] { -1, 0, 12, 68, 105, 115, 99, 111, 110, 110, 101, 99, 116, 101, 100 };

	public static byte[] generateDisconnectPacket(String reason) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
			dos.write(255);
			dos.writeUTF(reason);
			return baos.toByteArray();
		} catch (Exception e) {
			return disconnect;
		}
	}

	public void readPacket() {
		if (this.running && this.isConnectionOpen() && !this.isQuerySocket) {
			try {
				byte[] packet = packetReader.defragment(this.socketInputStream);
				if (packet != null && packet.length > 0 && isWebSocketOpen()) {
					try {
						DataFrame frame = new BinaryFrame();
						frame.setPayload(ByteBuffer.wrap(packet));
						frame.setFin(true);
						this.webSocket.sendFrame(frame);
					} catch (Exception e) {
					}
					this.socketLastRead = System.currentTimeMillis();
				}
			} catch (Exception e) {
			}
		}
	}

	public DataInputStream getSocketInputStream() {
		return this.socketInputStream;
	}

	public DataOutputStream getSocketOutputStream() {
		return this.socketOutputStream;
	}

	private boolean isWebSocketOpen() {
		return this.webSocket != null && this.webSocket.getReadyState() == ReadyState.OPEN && this.webSocket.isOpen()
				&& !this.webSocket.isClosing() && !this.webSocket.isClosed();
	}

	public boolean isConnectionOpen() {
		return this.socket != null && !this.socket.isClosed() && !this.socket.isInputShutdown()
				&& !this.socket.isOutputShutdown();
	}
}
