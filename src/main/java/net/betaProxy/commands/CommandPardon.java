package net.betaProxy.commands;

import net.betaProxy.main.Main;

public class CommandPardon extends Command {

	public CommandPardon(String name) {
		super(name);
	}

	@Override
	public void processCommand(String arg) {
		Main.pardonIP(arg);
	}

	@Override
	public String getCommandDescription() {
		return "pardons (unbans) an ip from the proxy";
	}

}
