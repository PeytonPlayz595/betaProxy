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
					cmd.processCommand(s.substring(s.indexOf(" ")).trim());
				}
			}
		} catch(IOException e) {
			
		}
	}

}
