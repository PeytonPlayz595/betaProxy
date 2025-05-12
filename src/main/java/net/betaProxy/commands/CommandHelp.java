package net.betaProxy.commands;

import java.util.List;

import net.betaProxy.main.Main;

public class CommandHelp extends Command {

	public CommandHelp(String name) {
		super(name);
		hasArgs = false;
	}

	@Override
	public void processCommand(String arg) {
		List<Command> cmds = CommandsList.getCommandList();
		Main.getLogger().info("List of commands:");
		for(int i = 0; i < cmds.size(); ++i) {
			Command cmd = cmds.get(i);
			Main.getLogger().info("- " + (cmd.hasArgs ? cmd.name.replace(" ", ": ") : cmd.name + ": ") + cmd.getCommandDescription());
		}
	}

	@Override
	public String getCommandDescription() {
		return "returns a list of available commands";
	}

}
