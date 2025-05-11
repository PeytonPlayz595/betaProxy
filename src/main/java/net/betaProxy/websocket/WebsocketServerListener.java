package net.betaProxy.websocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebsocketServerListener extends WebSocketServer {

	public final Object startupLock = new Object();
	public volatile boolean startupFailed;
	public volatile boolean started;
	
	public WebsocketServerListener(InetSocketAddress addr) {
		super(addr);
		this.startupFailed = false;
		this.started = false;
		this.setConnectionLostTimeout(15);
		this.setTcpNoDelay(true);
		this.start();
	}
	
	@Override
	public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
		WebsocketNetworkManager mgr = arg0.getAttachment();
		mgr.checkDisconnected();
	}
	
	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		if(!this.started) {
			arg1.printStackTrace();
			this.startupFailed = true;
			synchronized(startupLock) {
				startupLock.notify();
			}
		}
	}
	
	@Override
	public void onMessage(WebSocket arg0, String arg1) {
		arg0.send(WebsocketNetworkManager.generateDisconnectPacket("Received string frames on a binary connection"));
	}
	
	@Override
	public void onMessage(WebSocket arg0, ByteBuffer arg1) {
		WebsocketNetworkManager mgr = arg0.getAttachment();
		if(mgr != null) {
			mgr.addToSendQueue(arg1);
		}
	}
	
	@Override
	public void onOpen(WebSocket arg0, ClientHandshake arg1) {
		try {
			WebsocketNetworkManager mngr = new WebsocketNetworkManager(arg0);
			arg0.setAttachment(mngr);
		} catch (IOException e) {
			arg0.send(WebsocketNetworkManager.generateDisconnectPacket("Connection refused"));
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
