package net.betaProxy.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ServerManager {

    public static class ServerEntry {
        public String name;
        public String minecraftPort;
        public String proxyPort;
        public int pvn;
        public int timeout;
        public boolean whitelistEnabled;
    }

    public static class ServerConfig {
        @JsonProperty("servers")
        public List<ServerEntry> servers;
    }

    public ServerManager() {
    }

    public static void initServers() {
        File configFile = new File("servers.yml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        if (!configFile.exists()) {
            generateDefaultConfig(configFile, mapper);
        }

        try {
            ServerConfig config = mapper.readValue(configFile, ServerConfig.class);

            if (config.servers != null) {
                for (ServerEntry entry : config.servers) {
                    new Server(entry.name, entry.minecraftPort, entry.proxyPort, entry.pvn, entry.timeout, entry.whitelistEnabled);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateDefaultConfig(File file, ObjectMapper mapper) {
        ServerConfig defaultConfig = new ServerConfig();
        defaultConfig.servers = List.of(
                createEntry("default", "25565", "8081", 8, 15, false)
        );

        try {
            mapper.writeValue(file, defaultConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ServerEntry createEntry(String name, String mcPort, String proxyPort, int pvn, int timeout, boolean whitelistEnabled) {
        ServerEntry entry = new ServerEntry();
        entry.name = name;
        entry.minecraftPort = mcPort;
        entry.proxyPort = proxyPort;
        entry.pvn = pvn;
        entry.timeout = timeout;
        entry.whitelistEnabled = whitelistEnabled;
        return entry;
    }
}
