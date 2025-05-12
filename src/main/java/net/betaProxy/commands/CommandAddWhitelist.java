package net.betaProxy.commands;

import net.betaProxy.main.Main;

public class CommandAddWhitelist extends Command {

	public CommandAddWhitelist(String name) {
		super(name);
	}

	@Override
	public void processCommand(String arg) {
		Main.whitelistIP(arg);
	}

	@Override
	public String getCommandDescription() {
		return "adds an ip address to the proxies whitelist";
	}

}
