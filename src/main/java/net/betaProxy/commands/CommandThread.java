package net.betaProxy.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandThread extends Thread {
	
	public CommandThread() {
		
	}
	
	@Override
	public void run() {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			String s = null;
			
			while(true) {
				s = reader.readLine();
				if(s == null) {
					break;
				}
				
				Command cmd = CommandsList.getCommand(s);
				if(cmd != null) {
					if(cmd.hasArgs) {
						cmd.processCommand(s.substring(s.indexOf(" ")).trim());
					} else {
						cmd.processCommand(null);
					}
				}
			}
		} catch(IOException e) {
			
		}
	}

}
