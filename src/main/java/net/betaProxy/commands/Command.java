package net.betaProxy.commands;

public abstract class Command {
	
	public String name;
	
	public Command(String name) {
		this.name = name;
	}
	
	public abstract void processCommand(String arg);

}
