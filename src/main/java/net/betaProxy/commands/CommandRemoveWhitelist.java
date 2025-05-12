package net.betaProxy.commands;

import net.betaProxy.main.Main;

public class CommandRemoveWhitelist extends Command {

	public CommandRemoveWhitelist(String name) {
		super(name);
	}

	@Override
	public void processCommand(String arg) {
		Main.removeIPFromWhitelist(arg);
	}

	@Override
	public String getCommandDescription() {
		return "removes an ip address from the proxies whitelist";
	}

}
