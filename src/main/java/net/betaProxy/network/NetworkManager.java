package net.betaProxy.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.BinaryFrame;
import org.java_websocket.framing.DataFrame;

import net.betaProxy.server.ProxyServer;

public class NetworkManager {
	
	public Socket socket;
	private DataInputStream socketInputStream;
	private DataOutputStream socketOutputStream;
	
	private WebSocket webSocket;
	
	public NetworkManager(WebSocket webSocket) throws IOException {
		this.webSocket = webSocket;
		InetSocketAddress addr = ProxyServer.getMinecraftAddress();
		socket = new Socket(addr.getHostString(), addr.getPort());
		socket.setTrafficClass(24);
		this.socketInputStream = new DataInputStream(socket.getInputStream());
		this.socketOutputStream = new DataOutputStream(socket.getOutputStream());
		new NetworkReaderThread().start();
	}
	
	public void addToSendQueue(ByteBuffer pkt) {
		if(isConnectionOpen()) {
			try {
				byte[] data = new byte[pkt.remaining()];
				pkt.get(data);
				socketOutputStream.write(data);
				socketOutputStream.flush();
			} catch (IOException e) {
				networkShutdown(false);
			}
		}
	}
	
	private void networkShutdown(boolean isError) {
		if(isConnectionOpen()) {
			try {
				this.socket.close();
			} catch (IOException e1) {
			}
		}
		if(this.webSocket.isOpen()) {
			this.webSocket.send(generateDisconnectPacket(isError ? "Internal proxy error" : "Socket closed"));
			this.webSocket.close();
		}
		if(isError) {
			ProxyServer.getLogger().error("An internal error has occured!");
		} else {
			ProxyServer.getLogger().error("Socket closed!");
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
	
	
	public void readPacket() {
		if(this.isConnectionOpen()) {
			try {
				byte[] packet = PacketDefragmenter.defragment(this.socketInputStream);
				if(packet != null && packet.length > 0) {
					DataFrame frame = new BinaryFrame();
					frame.setPayload(ByteBuffer.wrap(packet));
					frame.setFin(true);
					this.webSocket.sendFrame(frame);
				}
			} catch(IOException e) {
				this.networkShutdown(false);
			}
		}
	}
	
	public boolean isConnectionOpen() {
		return !this.socket.isClosed();
	}
	
	private class NetworkReaderThread extends Thread {
		public void run() {
			while(true) {
				try {
					NetworkManager.this.readPacket();
				} catch (Exception e) {
					NetworkManager.this.networkShutdown(true);
					ProxyServer.getLogger().error(e);
				}
			}
		}
	}
}
