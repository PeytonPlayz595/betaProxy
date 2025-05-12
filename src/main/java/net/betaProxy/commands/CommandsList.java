package net.betaProxy.commands;

public class CommandsList {
	
	private static final Command[] commands = new Command[] { new CommandBan("ban-ip "), new CommandPardon("pardon-ip ") };
	
	public static Command getCommand(String cmd) {
		for(int i = 0; i < commands.length; ++i) {
			Command command = commands[i];
			if(cmd.toLowerCase().startsWith(command.name)) {
				return command;
			}
		}
		return null;
	}

}
