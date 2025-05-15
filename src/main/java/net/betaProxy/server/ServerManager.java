package net.betaProxy.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.betaProxy.config.AccessibleProxyConfig;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ServerManager {

    public static class ServerEntry {
        public String name;
        public String minecraftIP;
        public String proxyIP;
        public int pvn;
        public int timeout;
        public boolean whitelistEnabled;
    }

    public static class ServerConfig {
        @JsonProperty("servers")
        public List<ServerEntry> servers;
    }
    public static class ProxyConfig {
        @JsonProperty("PVN Auto Detect (Experiment)")
        public boolean exp_usePVNAutoDetect;
    }


    public ServerManager() {
    }

    public static void initServers() {
        File serverConfigFile = new File("servers.yml");
        File proxyConfigFile = new File("config.yml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        if (!serverConfigFile.exists()) {
            generateDefaultConfig(serverConfigFile, mapper);
        }
        if(!proxyConfigFile.exists()){
            generateDefaultProxyConfig(proxyConfigFile, mapper);
        }

        try {
            ServerConfig config = mapper.readValue(serverConfigFile, ServerConfig.class);
            ProxyConfig pconfig = mapper.readValue(proxyConfigFile, ProxyConfig.class);
            AccessibleProxyConfig.exp_pvnAutoDetect = pconfig.exp_usePVNAutoDetect;
            if (config.servers != null) {
                for (ServerEntry entry : config.servers) {
                    new Server(entry.name, entry.minecraftIP, entry.proxyIP, entry.pvn, entry.timeout, entry.whitelistEnabled);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void generateDefaultConfig(File file, ObjectMapper mapper) {
        ServerConfig defaultConfig = new ServerConfig();
        defaultConfig.servers = List.of(
                createEntry("default", "0.0.0.0:25565", "0.0.0.0:8081", 8, 15, false)
        );

        try {
            mapper.writeValue(file, defaultConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void generateDefaultProxyConfig(File file, ObjectMapper mapper) {
        ProxyConfig defaultConfig = new ProxyConfig();
        defaultConfig.exp_usePVNAutoDetect = false;

        try {
            mapper.writeValue(file, defaultConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ServerEntry createEntry(String name, String minecraftIP, String proxyIP, int pvn, int timeout, boolean whitelistEnabled) {
        ServerEntry entry = new ServerEntry();
        entry.name = name;
        entry.minecraftIP = minecraftIP;
        entry.proxyIP = proxyIP;
        entry.pvn = pvn;
        entry.timeout = timeout;
        entry.whitelistEnabled = whitelistEnabled;
        return entry;
    }
}
