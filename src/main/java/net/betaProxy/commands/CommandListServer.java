package net.betaProxy.commands;

import net.betaProxy.server.Server;
import net.betaProxy.server.ServerManager;

public class CommandListServer extends Command{
    public CommandListServer(String name) {
        super(name);
        hasArgs = false;
    }

    public void processCommand(String arg, Server server) {
        server.listOtherServers();
    }

    @Override
    public String getCommandDescription() {
        return "lists servers connected";
    }

}
