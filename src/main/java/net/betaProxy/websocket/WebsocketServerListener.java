package net.betaProxy.websocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.BinaryFrame;
import org.java_websocket.framing.DataFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import net.betaProxy.server.Server;
import net.betaProxy.utils.ServerProtocolVersion;

import dev.colbster937.utils.ServerQueryUtils;
import dev.colbster937.utils.WebSocketUtils;

public class WebsocketServerListener extends WebSocketServer {

	public final Object startupLock = new Object();
	public volatile boolean startupFailed;
	public volatile boolean started;

	private Server server;
	private ServerProtocolVersion spv;

	public WebsocketServerListener(InetSocketAddress addr, Server server, ServerProtocolVersion spv) {
		super(addr);
		this.server = server;
		this.spv = spv;
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
		this.server.getConnections().remove(arg0);
	}

	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		if (!this.started) {
			arg1.printStackTrace();
			this.startupFailed = true;
			synchronized (startupLock) {
				startupLock.notify();
			}
		}
	}

	@Override
	public void onMessage(WebSocket arg0, String arg1) {
		if (ServerQueryUtils.accept(arg0, arg1))
			return;
		DataFrame frame = new BinaryFrame();
		frame.setPayload(ByteBuffer
				.wrap(WebsocketNetworkManager.generateDisconnectPacket("Received string frames on a binary connection")));
		frame.setFin(true);
		arg0.sendFrame(frame);
	}

	@Override
	public void onMessage(WebSocket arg0, ByteBuffer arg1) {
		WebsocketNetworkManager mgr = arg0.getAttachment();
		if (mgr != null) {
			mgr.addToSendQueue(arg1);
		}
	}

	@Override
	public void onOpen(WebSocket arg0, ClientHandshake arg1) {
		Map<String, String> userInfo = WebSocketUtils.getProxyUserInfo(arg0, arg1);
		if (this.server.isWhitelistEnabled()) {
			if (!this.server.getWhitelist().contains(userInfo.get("ip"))) {
				DataFrame frame = new BinaryFrame();
				frame.setPayload(ByteBuffer
						.wrap(WebsocketNetworkManager.generateDisconnectPacket("You are not whitelisted on this server")));
				frame.setFin(true);
				arg0.sendFrame(frame);
				WebsocketNetworkManager.LOGGER
						.info("Disconnecting non-whitelisted IP: " + userInfo.get("ip"));
				return;
			}
		}
		if (this.server.getBannedIPs().contains(userInfo.get("ip"))) {
			DataFrame frame = new BinaryFrame();
			frame.setPayload(
					ByteBuffer.wrap(WebsocketNetworkManager.generateDisconnectPacket("You are banned from this server")));
			frame.setFin(true);
			arg0.sendFrame(frame);
			WebsocketNetworkManager.LOGGER.info("Disconnecting banned IP: " + userInfo.get("ip"));
			return;
		}
		try {
			WebsocketNetworkManager mngr = new WebsocketNetworkManager(arg0, this.server, spv, userInfo);
			arg0.setAttachment(mngr);
			this.server.getConnections().add(arg0);
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

		synchronized (startupLock) {
			startupLock.notify();
		}
	}

}
