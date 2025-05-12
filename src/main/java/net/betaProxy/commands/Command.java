package net.betaProxy.commands;

public abstract class Command {
	
	public String name;
	public boolean hasArgs;
	
	public Command(String name) {
		this.name = name;
		hasArgs = true;
	}
	
	public abstract void processCommand(String arg);
	
	public abstract String getCommandDescription();

}
