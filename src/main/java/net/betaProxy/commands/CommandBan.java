package net.betaProxy.commands;

import net.betaProxy.main.Main;

public class CommandBan extends Command {

	public CommandBan(String name) {
		super(name);
	}

	public void processCommand(String arg) {
		Main.banIP(arg);
	}

	@Override
	public String getCommandDescription() {
		return "bans an ip from the proxy";
	}

}
