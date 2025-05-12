package net.betaProxy.websocket;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.framing.BinaryFrame;
import org.java_websocket.framing.DataFrame;

import net.betaProxy.main.Main;
import net.betaProxy.utils.ProtocolAwarePacketReader;
import net.lax1dude.log4j.LogManager;
import net.lax1dude.log4j.Logger;

public class WebsocketNetworkManager {
	
	public Socket socket;
	private DataInputStream socketInputStream;
	private DataOutputStream socketOutputStream;
	private Thread readerThread = null;
	
	private WebSocket webSocket;
	private volatile boolean running;
	private boolean isShuttingDown = false;
	
	private long socketLastRead = System.currentTimeMillis();
	private long webSocketLastRead = System.currentTimeMillis();
	
	public static Logger LOGGER = LogManager.getLogger("NetworkManager");
	private String ip;
	
	public WebsocketNetworkManager(WebSocket webSocket) throws IOException {
		this.webSocket = webSocket;
		InetSocketAddress addr = Main.getMinecraftAddress();
		socket = new Socket(addr.getHostString(), addr.getPort());
		socket.setTrafficClass(24);
		this.socketInputStream = new DataInputStream(socket.getInputStream());
		this.socketOutputStream = new DataOutputStream(socket.getOutputStream());
		this.running = true;
		final String s = Thread.currentThread().getName();
		ip = webSocket.getRemoteSocketAddress().getHostString();
		this.readerThread = new Thread(() -> {
			Thread.currentThread().setName(s);
		    while(running) {
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
	
	public void addToSendQueue(ByteBuffer pkt) {
		if(isConnectionOpen()) {
			try {
				byte[] data = new byte[pkt.remaining()];
				pkt.get(data);
				socketOutputStream.write(data);
				socketOutputStream.flush();
				this.webSocketLastRead = System.currentTimeMillis();
			} catch (Exception e) {
			}
		}
	}
	
	void checkDisconnected() {
		long currentTime = System.currentTimeMillis();
		
		if(this.isShuttingDown || !this.running) {
			return;
		}
		
		boolean disconnected = !this.isConnectionOpen() || !this.isWebSocketOpen();
		if(currentTime >= (socketLastRead + 2*1000) || currentTime >= (webSocketLastRead + 2*1000) || disconnected) {
			if(this.isConnectionOpen()) {
				this.addToSendQueue(ByteBuffer.wrap(generateDisconnectPacket(disconnected ? "Connected closed" : "Timed out")));
				try {
					this.socket.close();
				} catch(IOException e) {
				}
			}
			LOGGER.info(ip + " disconnected!");
			if(isWebSocketOpen()) {
				try {
					DataFrame frame = new BinaryFrame();
					frame.setPayload(ByteBuffer.wrap(generateDisconnectPacket(disconnected ? "Connected closed" : "Timed out")));
					frame.setFin(true);
					this.webSocket.sendFrame(frame);
				} catch(Exception e) {
				}
				try {
					this.webSocket.close();
				} catch(Exception e) {
				}
			}
			this.running = false;
		}
	}
	
	private static byte[] disconnect = new byte[] { -1, 0, 12, 68, 105, 115, 99, 111, 110, 110, 101, 99, 116, 101, 100 };
	public static byte[] generateDisconnectPacket(String reason) {
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
			dos.write(255);
			dos.writeUTF(reason);
			return baos.toByteArray();
		} catch(Exception e) {
			return disconnect;
		}
	}
	
	public void readPacket() {
		if(this.running && this.isConnectionOpen()) {
			try {
				byte[] packet = ProtocolAwarePacketReader.defragment(this.socketInputStream);
				if(packet != null && packet.length > 0 && isWebSocketOpen()) {
					try {
						DataFrame frame = new BinaryFrame();
						frame.setPayload(ByteBuffer.wrap(packet));
						frame.setFin(true);
						this.webSocket.sendFrame(frame);
					} catch(Exception e) {
					}
					this.socketLastRead = System.currentTimeMillis();
				}
			} catch(Exception e) {
			}
		}
	}
	
	private boolean isWebSocketOpen() {
		return this.webSocket != null && this.webSocket.getReadyState() == ReadyState.OPEN && this.webSocket.isOpen() && !this.webSocket.isClosing() && !this.webSocket.isClosed();
	}
	
	public boolean isConnectionOpen() {
		return this.socket != null && !this.socket.isClosed() && !this.socket.isInputShutdown() && !this.socket.isOutputShutdown();
	}
}
