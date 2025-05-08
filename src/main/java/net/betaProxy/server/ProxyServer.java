package net.betaProxy.server;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import net.betaProxy.log4j.LogManager;
import net.betaProxy.log4j.Logger;
import net.betaProxy.network.NetworkManager;
import net.betaProxy.network.WebsocketListenerThread;

public class ProxyServer {
	
	private static Logger LOGGER = LogManager.getLogger("Beta Proxy");
	private static PropertiesManager propertiesManager;
	private static final File dataDir = new File("betaProxy");
	
	private static WebsocketListenerThread wsNetManager;
	private static InetSocketAddress mcAddress;
	
	public static void main(String[] args) {
		System.setOut(new LoggerOutputStream("STDERR", true, System.err));
		System.setErr(new LoggerOutputStream("STDOUT", false, System.out));
		
		LOGGER.info("Loading server properties...");
		if(!dataDir.exists()) {
			dataDir.mkdirs();
		}
		propertiesManager = new PropertiesManager(new File(dataDir, "server_properties.txt"));
		
		try {
			propertiesManager.loadProperties();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		String wsAddr = propertiesManager.getProperty("websocket_server_address", "0.0.0.0:8080");
		String mcAddr = propertiesManager.getProperty("minecraft_server_address", "0.0.0.0:25565");
		
		InetSocketAddress inetWebsocketAddress = null;
		if (wsAddr.length() > 0 && !wsAddr.equalsIgnoreCase("null")) {
			String addr = wsAddr;
			int port = 25565;
			int cp = wsAddr.lastIndexOf(':');
			if(cp != -1) {
				addr = wsAddr.substring(0, cp);
				port = Integer.parseInt(wsAddr.substring(cp + 1));
			}
			
			try {
				inetWebsocketAddress = new InetSocketAddress(InetAddress.getByName(addr), port);
			}catch(UnknownHostException ex) {
				throw new RuntimeException("ERROR: websocket_server_address '" + wsAddr + "' is invalid", ex);
			}
		}
		
		if(inetWebsocketAddress == null) {
			
		}
		
		InetSocketAddress inetVanillaAddress = null;
		if (mcAddr.length() > 0 && !mcAddr.equalsIgnoreCase("null")) {
			String addr = mcAddr;
			int port = 25565;
			int cp = mcAddr.lastIndexOf(':');
			if(cp != -1) {
				addr = mcAddr.substring(0, cp);
				port = Integer.parseInt(mcAddr.substring(cp + 1));
			}
			try {
				inetVanillaAddress = new InetSocketAddress(InetAddress.getByName(addr), port);
			}catch(UnknownHostException ex) {
				throw new RuntimeException("ERROR: minecraft_server_address '" + mcAddr + "' is invalid", ex);
			}
		}
		
		LOGGER.info("Starting TCP -> WebSocket proxy for Minecraft server version Beta 1.1_02");
		LOGGER.info("Forwarding TCP connection tcp:/" + inetVanillaAddress.toString() + " to ws:/" + inetWebsocketAddress.toString());
		
		wsNetManager = new WebsocketListenerThread(inetWebsocketAddress);
		synchronized(wsNetManager.startupLock) {
			try {
				wsNetManager.startupLock.wait(5000l);
			} catch (InterruptedException e) {
				;
			}
		}
		if(wsNetManager.startupFailed || !wsNetManager.started) {
			throw new RuntimeException("ERROR: Could not start websocket server on " + inetWebsocketAddress.toString());
		}
		
		mcAddress = inetVanillaAddress;
	}
	
	public static InetSocketAddress getMinecraftAddress() {
		return mcAddress;
	}
	
	public static Logger getLogger() {
		return LOGGER;
	}

}
