package net.betaProxy.commands;

import net.betaProxy.server.Server;

public class CommandBan extends Command {

	public CommandBan(String name) {
		super(name);
	}

	public void processCommand(String arg, Server server) {
		server.banIP(arg);
	}

	@Override
	public String getCommandDescription() {
		return "bans an ip from the proxy";
	}

}
