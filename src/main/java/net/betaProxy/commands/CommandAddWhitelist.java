package net.betaProxy.commands;

import net.betaProxy.server.Server;

public class CommandAddWhitelist extends Command {

	public CommandAddWhitelist(String name) {
		super(name);
	}

	@Override
	public void processCommand(String arg, Server server) {
		server.whitelistIP(arg);
	}

	@Override
	public String getCommandDescription() {
		return "adds an ip address to the proxies whitelist";
	}

}
