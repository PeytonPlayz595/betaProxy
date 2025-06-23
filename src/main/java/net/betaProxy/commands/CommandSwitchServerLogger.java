package net.betaProxy.commands;

import net.betaProxy.server.Server;
import net.betaProxy.server.ServerManager;

public class CommandSwitchServerLogger extends Command{
    public CommandSwitchServerLogger(String name) {
        super(name);
    }

    public void processCommand(String arg, Server server) {
        server.switchServerCommand(arg);

    }

    @Override
    public String getCommandDescription() {
        return " switches to a different server logger Usage switch-server <server>";
    }

}
