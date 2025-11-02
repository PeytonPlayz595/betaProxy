package net.betaProxy.commands;

import dev.colbster937.utils.ServerQueryUtils;

import net.betaProxy.server.Server;

public class CommandConfirmCode extends Command {

	public CommandConfirmCode(String name) {
		super(name);
	}

	public void processCommand(String arg, Server server) {
		ServerQueryUtils.setServerListConfirmCode(arg);
		server.getLogger().info("Serverlist confirmation code has been set to " + arg);
	}

	@Override
	public String getCommandDescription() {
		return "sets the serverlist confirmation code";
	}

}
