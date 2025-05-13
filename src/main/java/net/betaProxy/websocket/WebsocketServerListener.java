package net.betaProxy.websocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.BinaryFrame;
import org.java_websocket.framing.DataFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import net.betaProxy.server.Server;

public class WebsocketServerListener extends WebSocketServer {

	public final Object startupLock = new Object();
	public volatile boolean startupFailed;
	public volatile boolean started;
	
	private Server server;
	
	public WebsocketServerListener(InetSocketAddress addr, Server server) {
		super(addr);
		this.server = server;
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
		server.getConnections().remove(arg0);
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
		DataFrame frame = new BinaryFrame();
		frame.setPayload(ByteBuffer.wrap(WebsocketNetworkManager.generateDisconnectPacket("Received string frames on a binary connection")));
		frame.setFin(true);
		arg0.sendFrame(frame);
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
		if(server.isWhitelistEnabled()) {
			if(!server.getWhitelist().contains(arg0.getRemoteSocketAddress().getHostString())) {
				DataFrame frame = new BinaryFrame();
				frame.setPayload(ByteBuffer.wrap(WebsocketNetworkManager.generateDisconnectPacket("You are not whitelisted on this server")));
				frame.setFin(true);
				arg0.sendFrame(frame);
				WebsocketNetworkManager.LOGGER.info("Disconnecting non-whitelisted IP: " + arg0.getRemoteSocketAddress().getHostString());
				return;
			}
		}
		if(server.getBannedIPs().contains(arg0.getRemoteSocketAddress().getHostString())) {
			DataFrame frame = new BinaryFrame();
			frame.setPayload(ByteBuffer.wrap(WebsocketNetworkManager.generateDisconnectPacket("You are banned from this server")));
			frame.setFin(true);
			arg0.sendFrame(frame);
			WebsocketNetworkManager.LOGGER.info("Disconnecting banned IP: " + arg0.getRemoteSocketAddress().getHostString());
			return;
		}
		try {
			WebsocketNetworkManager mngr = new WebsocketNetworkManager(arg0, server);
			arg0.setAttachment(mngr);
			server.getConnections().add(arg0);
		} catch (IOException e) {
			DataFrame frame = new BinaryFrame();
			frame.setPayload(ByteBuffer.wrap(WebsocketNetworkManager.generateDisconnectPacket("Connection refused")));
			frame.setFin(true);
			arg0.sendFrame(frame);
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
