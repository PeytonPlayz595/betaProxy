package net.betaProxy.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.betaProxy.server.Server;

public class CommandThread extends Thread {
	
	private Server server;
	
	public CommandThread(Server server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		server.getLogger().info("Do switch-server <server> to switch to a different websocket server in the proxy");
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			String s = null;
			
			while(true) {
				s = reader.readLine();
				if(s == null) {
					break;
				}
				
				Command cmd = CommandsList.getCommand(s);
				if(cmd != null) {
					if(cmd.hasArgs) {
						cmd.processCommand(s.substring(s.indexOf(" ")).trim(), server);
					} else {
						cmd.processCommand(null, server);
					}
				} else {
					server.getLogger().info("Unknown command, use command 'help' for more info");
				}
			}
		} catch(IOException e) {
			
		}
	}
	public void switchServer(Server server){
		this.server = server;
	}
}
