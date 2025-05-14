package net.betaProxy.main;

import net.betaProxy.server.Server;

import java.io.File;

public class Main {
	private final File whiteListFile = new File("server-forwarding.txt");

	public static void main(String[] args) {

		new Server();
	}

}
