package net.betaProxy.commands;

import java.util.Arrays;
import java.util.List;

public class CommandsList {
	
	private static final Command[] commands = new Command[] { 
			new CommandBan("ban-ip "), 
			new CommandPardon("pardon-ip "),
			new CommandHelp("help"),
	};
	
	public static Command getCommand(String cmd) {
		for(int i = 0; i < commands.length; ++i) {
			Command command = commands[i];
			if(command.hasArgs) {
				if(cmd.toLowerCase().startsWith(command.name)) {
					return command;
				}
			} else {
				if(cmd.toLowerCase().equals(command.name)) {
					return command;
				}
			}
		}
		return null;
	}
	
	public static List<Command> getCommandList() {
		return Arrays.asList(commands);
	}

}
