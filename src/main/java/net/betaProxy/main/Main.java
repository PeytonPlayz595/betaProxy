package net.betaProxy.main;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.betaProxy.server.Server;
import net.betaProxy.server.ServerManager;

import java.io.File;

public class Main {
	public static ServerManager serverManager = new ServerManager();
	public static void main(String[] args) {
		ServerManager.initServers();
	}

}
