package net.betaProxy.server;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.betaProxy.config.AccessibleProxyConfig;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.BinaryFrame;
import org.java_websocket.framing.DataFrame;

import net.betaProxy.commands.CommandThread;
import net.betaProxy.utils.LoggerRedirector;
import net.betaProxy.utils.ServerProtocolVersion;
import net.betaProxy.websocket.WebsocketNetworkManager;
import net.betaProxy.websocket.WebsocketServerListener;
import net.lax1dude.log4j.LogManager;
import net.lax1dude.log4j.Logger;

public class Server {

    private final File ipBanFile = new File("banned-ips.txt");
    private final File whiteListFile = new File("banned-ips.txt");
    private boolean whiteListEnabled = false;

    private WebsocketServerListener wsNetManager;
    private InetSocketAddress mcAddress;

    private Set<String> bannedIPs = new HashSet<String>();
    private Set<String> whitelistedIPs = new HashSet<String>();
    private Set<WebSocket> connections = new HashSet<WebSocket>();

    private boolean autoDetectPvn = false;
    private int timeout = 0;
    private final String defaultIPTCP1;
    private final String defaultIPWSS1;
    private int pvn = 0;
    private int timeout1 = 15;
    private Boolean whiteList;
    private Logger LOGGER;
    File configFile = new File("servers.yml");
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private String name = "";

    public Server(String namer, String defaultIPTCP, String defaultIPWSS, int pvn, int timeout, boolean whitelist) {
        name = namer;
        defaultIPTCP1 = defaultIPTCP;
        defaultIPWSS1 = defaultIPWSS;
        timeout1 = timeout;
        this.pvn = pvn;
        whiteListEnabled = whitelist;
        LOGGER =  LogManager.getLogger("(SERVER: " + name+ ")" + " Beta Proxy");
        startServer();
    }


    public void startServer() {
        System.setOut(new LoggerRedirector("STDOUT", false, System.out));
        System.setErr(new LoggerRedirector("STDERR", true, System.err));

        LOGGER.info("Loading configurations...");
        loadBannedList();
        loadWhiteList();

        CommandThread cmdThread = new CommandThread(this);
        cmdThread.setDaemon(true);
        cmdThread.start();

        String wsAddr =  defaultIPWSS1;
        String mcAddr =  defaultIPTCP1;
        timeout = timeout1;
        autoDetectPvn = AccessibleProxyConfig.exp_pvnAutoDetect;

        if(timeout < 5 || timeout > 60) {
            throw new RuntimeException("Timeout value is invalid. It must be between 5-60 seconds");
        }

            ServerProtocolVersion protocolVersion = new ServerProtocolVersion(autoDetectPvn ? -1 : Integer.valueOf(pvn));

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

        if(!protocolVersion.isAutoDetectPVN()) {
            LOGGER.info("Starting TCP -> WebSocket proxy for Minecraft server version(s) " + protocolVersion.getSupportedVersionNames());
        } else {
            LOGGER.info("Starting TCP -> WebSocket proxy (client pvn set to autodect)");
        }
        LOGGER.info("Forwarding TCP connection tcp:/" + inetVanillaAddress.toString() + " to ws:/" + inetWebsocketAddress.toString());

        wsNetManager = new WebsocketServerListener(inetWebsocketAddress, this, protocolVersion);
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

    public InetSocketAddress getMinecraftSocketAddress() {
        return mcAddress;
    }

    public Logger getLogger() {
        return LOGGER;
    }

    public void banIP(String ip) {
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

    public void pardonIP(String ip) {
        if(bannedIPs.contains(ip.toLowerCase())) {
            bannedIPs.remove(ip.toLowerCase());
        }
        saveBannedList();
        LOGGER.info("Pardoned IP: " + ip.toLowerCase());
    }

    public void whitelistIP(String ip) {
        LOGGER.info("Adding ip '" + ip + "' to the whitelist");
        whitelistedIPs.add(ip.toLowerCase());
        saveWhiteList();
    }

    public void removeIPFromWhitelist(String ip) {
        LOGGER.info("Removing ip '" + ip + "' from whitelist");
        if(whitelistedIPs.contains(ip.toLowerCase())) {
            whitelistedIPs.remove(ip.toLowerCase());
        }
        saveWhiteList();
    }

    private void loadBannedList() {
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

    private void loadWhiteList() {
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

    private void saveBannedList() {
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

    private void saveWhiteList() {
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

    public boolean isWhitelistEnabled() {
        return whiteListEnabled;
    }

    public int getTimeout() {
        return timeout;
    }

    public Set<WebSocket> getConnections() {
        return this.connections;
    }

    public Set<String> getBannedIPs() {
        return this.bannedIPs;
    }

    public Set<String> getWhitelist() {
        return this.whitelistedIPs;
    }

}