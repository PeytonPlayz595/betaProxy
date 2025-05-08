package net.betaProxy.network;

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

import net.betaProxy.log4j.LogManager;
import net.betaProxy.log4j.Logger;
import net.betaProxy.server.ProxyServer;

public class NetworkManager {
	
	public Socket socket;
	private DataInputStream socketInputStream;
	private DataOutputStream socketOutputStream;
	private Thread readerThread = null;
	
	private WebSocket webSocket;
	private volatile boolean running;
	private boolean isShuttingDown = false;
	public boolean terminated = false;
	
	public int socketLastRead = 0;
	public int webSocketLastRead = 0;
	
	private Logger LOGGER = LogManager.getLogger("NetworkManager");
	
	public NetworkManager(WebSocket webSocket) throws IOException {
		this.webSocket = webSocket;
		InetSocketAddress addr = ProxyServer.getMinecraftAddress();
		socket = new Socket(addr.getHostString(), addr.getPort());
		socket.setTrafficClass(24);
		this.socketInputStream = new DataInputStream(socket.getInputStream());
		this.socketOutputStream = new DataOutputStream(socket.getOutputStream());
		this.running = true;
		final String s = Thread.currentThread().getName();
		this.readerThread = new Thread(() -> {
			Thread.currentThread().setName(s);
		    while(true && running) {
		    	try {
		    		checkDisconnected();
					readPacket();
				} catch (Exception e) {
					LOGGER.error(e);
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
				this.webSocketLastRead = 0;
			} catch (IOException e) {
			}
		}
	}
	
	void checkDisconnected() {
		++this.socketLastRead;
		++this.webSocketLastRead;
		
		if(this.isShuttingDown || !this.running) {
			return;
		}
		
		boolean disconnected = !this.socket.isConnected() || !this.webSocket.isOpen();
		if(this.socketLastRead == 1200 || this.webSocketLastRead == 1200 || disconnected) {
			if(this.isConnectionOpen()) {
				this.addToSendQueue(ByteBuffer.wrap(this.generateDisconnectPacket(disconnected ? "Connected closed" : "Connection timed out")));
				try {
					this.socket.close();
				} catch(IOException e) {
				}
			}
			if(this.webSocket.isOpen()) {
				LOGGER.info(this.webSocket.getRemoteSocketAddress().toString() + " disconnected!");
				DataFrame frame = new BinaryFrame();
				frame.setPayload(ByteBuffer.wrap(this.generateDisconnectPacket(disconnected ? "Connected closed" :"Connection timed out")));
				frame.setFin(true);
				try {
					this.webSocket.sendFrame(frame);
				} catch(Exception e) {
				}
				this.webSocket.close();
			}
			this.running = false;
			this.terminated = true;
		}
	}
	
	public byte[] generateDisconnectPacket(String reason) {
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
			dos.write(255);
			dos.writeUTF(reason);
			return baos.toByteArray();
		} catch(Exception e) {
			return new byte[1];
		}
	}
	
	private boolean isWebSocketOpen() {
		return this.webSocket.getReadyState() == ReadyState.OPEN;
	}
	
	public void readPacket() {
		if(this.running && this.isConnectionOpen()) {
			try {
				byte[] packet = PacketDefragmenter.defragment(this.socketInputStream);
				if(packet != null && packet.length > 0 && isWebSocketOpen()) {
					DataFrame frame = new BinaryFrame();
					frame.setPayload(ByteBuffer.wrap(packet));
					frame.setFin(true);
					try {
					this.webSocket.sendFrame(frame);
					} catch(Exception e) {
					}
					this.socketLastRead = 0;
				}
			} catch(IOException e) {
			}
		}
	}
	
	public boolean isConnectionOpen() {
		return !this.socket.isClosed();
	}
}
