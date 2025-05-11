package net.betaProxy.main;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import net.betaProxy.utils.LoggerRedirector;
import net.betaProxy.utils.PropertiesManager;
import net.betaProxy.utils.SupportedProtocolVersionInfo;
import net.betaProxy.websocket.WebsocketServerListener;
import net.lax1dude.log4j.LogManager;
import net.lax1dude.log4j.Logger;

public class Main {
	
	private static Logger LOGGER = LogManager.getLogger("Beta Proxy");
	private static PropertiesManager propertiesManager;
	private static final File dataDir = new File("config");
	
	private static WebsocketServerListener wsNetManager;
	private static InetSocketAddress mcAddress;
	
	public static void main(String[] args) {
		System.setOut(new LoggerRedirector("STDOUT", false, System.out));
		System.setErr(new LoggerRedirector("STDERR", true, System.err));
		
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
		
		String wsAddr = propertiesManager.getProperty("websocket_host", "0.0.0.0:8080");
		String mcAddr = propertiesManager.getProperty("minecraft_host", "0.0.0.0:25565");
		String pvnS = propertiesManager.getProperty("minecraft_pvn", "8");
		
		int pvn;
		try {
			pvn = Integer.parseInt(pvnS);
		} catch(Exception e) {
			throw new RuntimeException("Invalid value for server protocol version: '" + pvnS + "'");
		}
		SupportedProtocolVersionInfo.setPNVVersion(pvn);
		
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
				throw new RuntimeException("ERROR: websocket host '" + wsAddr + "' is invalid", ex);
			}
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
				throw new RuntimeException("ERROR: minecraft host '" + mcAddr + "' is invalid", ex);
			}
		}
		
		LOGGER.info("Starting TCP -> WebSocket proxy for Minecraft server version(s) " + SupportedProtocolVersionInfo.getSupportedVersionNames());
		LOGGER.info("Forwarding TCP connection tcp:/" + inetVanillaAddress.toString() + " to ws:/" + inetWebsocketAddress.toString());
		
		wsNetManager = new WebsocketServerListener(inetWebsocketAddress);
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
