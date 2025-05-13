package net.betaProxy.commands;

import net.betaProxy.server.Server;

public class CommandPardon extends Command {

	public CommandPardon(String name) {
		super(name);
	}

	@Override
	public void processCommand(String arg, Server server) {
		server.pardonIP(arg);
	}

	@Override
	public String getCommandDescription() {
		return "pardons (unbans) an ip from the proxy";
	}

}
