package net.betaProxy.commands;

import net.betaProxy.server.Server;

public abstract class Command {
	
	public String name;
	public boolean hasArgs;
	
	public Command(String name) {
		this.name = name;
		hasArgs = true;
	}
	
	public abstract void processCommand(String arg, Server server);
	
	public abstract String getCommandDescription();

}
