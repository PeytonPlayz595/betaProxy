package net.betaProxy.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.betaProxy.main.Main;

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
				if(s.toLowerCase().startsWith("ban-ip ")) {
					String s1 = s.substring(s.indexOf(" ")).trim();
					Main.banIP(s1);
				} else if(s.toLowerCase().startsWith("pardon-ip ")) {
					String s1 = s.substring(s.indexOf(" ")).trim();
					Main.pardonIP(s1);
				}
			}
		} catch(IOException e) {
			
		}
	}

}
