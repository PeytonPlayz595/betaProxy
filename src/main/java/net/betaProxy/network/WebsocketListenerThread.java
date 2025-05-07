package net.betaProxy.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import net.betaProxy.server.ProxyServer;

public class WebsocketListenerThread extends WebSocketServer {

	public final Object startupLock = new Object();
	public volatile boolean startupFailed;
	public volatile boolean started;
	
	public WebsocketListenerThread(InetSocketAddress addr) {
		super(addr);
		this.startupFailed = false;
		this.started = false;
		this.setConnectionLostTimeout(15);
		this.setTcpNoDelay(true);
		this.start();
	}
	
	@Override
	//Not being called!?
	public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
		NetworkManager mgr = arg0.getAttachment();
		try {
			mgr.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ProxyServer.getLogger().info(arg0.getRemoteSocketAddress().toString() + " disconnected!");
	}
	
	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		if(!this.started) {
			this.startupFailed = true;
			synchronized(startupLock) {
				startupLock.notify();
			}
		}
	}
	
	@Override
	public void onMessage(WebSocket arg0, String arg1) {
		ProxyServer.getLogger().warn("Recieved string frames on a binary connection!");
		arg0.send(new byte[] { -1, 0, 55, 82, 101, 99, 105, 101, 118, 101, 100, 32, 115, 116, 114, 105, 110, 103, 32, 102, 114, 97, 109, 101, 115, 32, 111, 110, 32, 98, 105, 110, 97, 114, 121, 32, 99, 111, 110, 110, 101, 99, 116, 105, 111, 110, 32, 40, 102, 117, 99, 107, 32, 111, 102, 102, 41, 33 });
		arg0.close();
	}
	
	@Override
	public void onMessage(WebSocket arg0, ByteBuffer arg1) {
		NetworkManager mgr = arg0.getAttachment();
		if(mgr != null) {
			mgr.addToSendQueue(arg1);
		}
	}
	
	@Override
	public void onOpen(WebSocket arg0, ClientHandshake arg1) {
		try {
			NetworkManager mngr = new NetworkManager(arg0);
			arg0.setAttachment(mngr);
			ProxyServer.getLogger().info(arg0.getRemoteSocketAddress().toString() + " connected!");
		} catch (IOException e) {
			ProxyServer.getLogger().warn("Cannot connect to minecraft server (Is the server down?)");
			arg0.send(new byte[] { -1, 0, 42, 73, 110, 116, 101, 114, 110, 97, 108, 32, 112, 114, 111, 120, 121, 32, 101, 114, 114, 111, 114, 32, 40, 73, 115, 32, 116, 104, 101, 32, 115, 101, 114, 118, 101, 114, 32, 100, 111, 119, 110, 63, 41 });
			arg0.close();
			e.printStackTrace();
		}
	}
	
	@Override
	public void onStart() {
		this.started = true;
		
		synchronized(startupLock) {
			startupLock.notify();
		}
	}
	
}
