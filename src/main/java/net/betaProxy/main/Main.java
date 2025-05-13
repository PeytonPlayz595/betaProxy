package net.betaProxy.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.BinaryFrame;
import org.java_websocket.framing.DataFrame;

import net.betaProxy.commands.CommandThread;
import net.betaProxy.utils.LoggerRedirector;
import net.betaProxy.utils.PropertiesManager;
import net.betaProxy.utils.SupportedProtocolVersionInfo;
import net.betaProxy.websocket.WebsocketNetworkManager;
import net.betaProxy.websocket.WebsocketServerListener;
import net.lax1dude.log4j.LogManager;
import net.lax1dude.log4j.Logger;

public class Main {
	
	private static Logger LOGGER = LogManager.getLogger("Beta Proxy");
	private static PropertiesManager propertiesManager;
	private static final File ipBanFile = new File("banned-ips.txt");
	private static final File whiteListFile = new File("banned-ips.txt");
	private static boolean whiteListEnabled = false;
	
	private static WebsocketServerListener wsNetManager;
	private static InetSocketAddress mcAddress;
	
	public static Set<String> bannedIPs = new HashSet<String>();
	public static Set<String> whitelistedIPs = new HashSet<String>();
	public static Set<WebSocket> connections = new HashSet<WebSocket>();
	
	private static int timeout = 0;
	
	public static void main(String[] args) {
		System.setOut(new LoggerRedirector("STDOUT", false, System.out));
		System.setErr(new LoggerRedirector("STDERR", true, System.err));
		
		LOGGER.info("Loading configurations...");
		loadBannedList();
		loadWhiteList();
		propertiesManager = new PropertiesManager(new File("server.properties"));
		
		CommandThread cmdThread = new CommandThread();
		cmdThread.setDaemon(true);
		cmdThread.start();
		
		String wsAddr = propertiesManager.getProperty("websocket_host", "0.0.0.0:8080");
		String mcAddr = propertiesManager.getProperty("minecraft_host", "0.0.0.0:25565");
		int pvn = propertiesManager.getProperty("minecraft_pvn", 8);
		whiteListEnabled = propertiesManager.getProperty("whitelist_enabled", false);
		timeout = propertiesManager.getProperty("timeout", 15);
		
		if(timeout < 5 || timeout > 60) {
			throw new RuntimeException("Timeout value is invalid. It must be between 5-60 seconds");
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
	
	public static void banIP(String ip) {
		bannedIPs.add(ip.toLowerCase());
		LOGGER.info("Banning IP: " + ip.toLowerCase());
		saveBannedList();
		
		Iterator<WebSocket> iterator = connections.iterator();
		while(iterator.hasNext()) {
			WebSocket socket = iterator.next();
			if(socket.getRemoteSocketAddress().getHostString().equals(ip.toLowerCase())) {
				try {
					DataFrame frame = new BinaryFrame();
					frame.setPayload(ByteBuffer.wrap(WebsocketNetworkManager.generateDisconnectPacket("You were banned")));
					frame.setFin(true);
					socket.sendFrame(frame);
				} catch(Exception e) {
				}
			}
		}
	}
	
	public static void pardonIP(String ip) {
		if(bannedIPs.contains(ip.toLowerCase())) {
			bannedIPs.remove(ip.toLowerCase());
		}
		saveBannedList();
		LOGGER.info("Pardoned IP: " + ip.toLowerCase());
	}
	
	public static void whitelistIP(String ip) {
		Main.getLogger().info("Adding ip '" + ip + "' to the whitelist");
		whitelistedIPs.add(ip.toLowerCase());
		saveWhiteList();
	}
	
	public static void removeIPFromWhitelist(String ip) {
		Main.getLogger().info("Removing ip '" + ip + "' from whitelist");
		if(whitelistedIPs.contains(ip.toLowerCase())) {
			whitelistedIPs.remove(ip.toLowerCase());
		}
		saveWhiteList();
	}
	
	private static void loadBannedList() {
		try {
			if(!ipBanFile.exists()) {
				ipBanFile.createNewFile();
			}
			
			bannedIPs.clear();
			BufferedReader var1 = new BufferedReader(new FileReader(ipBanFile));
			String var2 = "";

			while(true) {
				var2 = var1.readLine();
				if(var2 == null) {
					var1.close();
					break;
				}

				bannedIPs.add(var2.trim().toLowerCase());
			}
		} catch (Exception var3) {
			LOGGER.warn("Failed to load ip ban list: " + var3);
		}
	}
	
	private static void loadWhiteList() {
		try {
			if(!whiteListFile.exists()) {
				whiteListFile.createNewFile();
			}
			
			whitelistedIPs.clear();
			BufferedReader var1 = new BufferedReader(new FileReader(whiteListFile));
			String var2 = "";

			while(true) {
				var2 = var1.readLine();
				if(var2 == null) {
					var1.close();
					break;
				}

				whitelistedIPs.add(var2.trim().toLowerCase());
			}
		} catch (Exception var3) {
			LOGGER.warn("Failed to load ip white list: " + var3);
		}
	}
	
	private static void saveBannedList() {
		try {
			PrintWriter var1 = new PrintWriter(new FileWriter(ipBanFile, false));
			Iterator<String> var2 = bannedIPs.iterator();

			while(var2.hasNext()) {
				String var3 = var2.next();
				var1.println(var3);
			}

			var1.close();
		} catch (Exception var4) {
			LOGGER.warn("Failed to save ip ban list: " + var4);
		}

	}
	
	private static void saveWhiteList() {
		try {
			PrintWriter var1 = new PrintWriter(new FileWriter(whiteListFile, false));
			Iterator<String> var2 = whitelistedIPs.iterator();

			while(var2.hasNext()) {
				String var3 = var2.next();
				var1.println(var3);
			}

			var1.close();
		} catch (Exception var4) {
			LOGGER.warn("Failed to save ip white list: " + var4);
		}

	}
	
	public static boolean isWhitelistEnabled() {
		return whiteListEnabled;
	}
	
	public static int getTimeout() {
		return timeout;
	}

}
