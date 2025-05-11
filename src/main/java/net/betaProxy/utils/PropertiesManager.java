package net.betaProxy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import net.betaProxy.main.Main;

public class PropertiesManager {
	
	private File propertiesFile;
	private boolean resetting = false;
	private Map<String, String> propertiesMap = new HashMap<String, String>();
	
	public PropertiesManager(File file) {
		this.propertiesFile = file;
	}
	
	public String getProperty(String key, String defaultKey) {
		String s = propertiesMap.get(key);
		if(s == null) {
			if(!resetting) {
				Main.getLogger().warn("Properties file has no value for '" + key + "'!");
				Main.getLogger().warn("Resetting properties file (restart required)!");
				if(this.propertiesFile.exists()) {
					this.propertiesFile.delete();
				}
				resetting = true;
			}
			return defaultKey;
		}
		return s;
	}
	
	public String getProperty(String value) {
		String s = propertiesMap.get(value);
		if(s == null) {
			throw new RuntimeException(new IOException("Properties file is broken! This is NOT a bug, please try to fix this yourself..."));
		}
		return s;
	}
	
	public void loadProperties() throws IOException {
		checkPropertiesFile();
		
		try(FileReader fr = new FileReader(propertiesFile); BufferedReader reader = new BufferedReader(fr)) {
			String line = "";
			
			while(true) {
				line = reader.readLine();
				if(line == null) {
					break;
				}
				
				String[] s = line.split("=");
				if(s.length == 2) {
					propertiesMap.put(s[0], s[1]);
				}
			}
			
		} catch(Exception e) {
			throw new IOException("Failed to load properties file");
		}
	}
	
	private void checkPropertiesFile() throws IOException {
		File old = new File("betaProxy", propertiesFile.getName());
		if(old.exists()) {
			if(!this.propertiesFile.exists()) {
				this.propertiesFile.createNewFile();
			}
			fixPropertiesFile(old);
			old.delete();
		}
		
		if(!this.propertiesFile.exists()) {
			this.propertiesFile.createNewFile();
			
			try(PrintWriter writer = new PrintWriter(new FileWriter(this.propertiesFile))) {
				writer.println("minecraft_host=0.0.0.0:25565");
				writer.println("minecraft_pvn=8");
				writer.println("websocket_host=0.0.0.0:8080");
			}
		} else {
			fixPropertiesFile(this.propertiesFile);
		}
	}
	
	private void fixPropertiesFile(File file) throws IOException {
		Map<String, String> propertiesMap = new HashMap<String, String>();
		try(FileReader fr = new FileReader(file); BufferedReader reader = new BufferedReader(fr)) {
			String line = "";
			
			while(true) {
				line = reader.readLine();
				if(line == null) {
					break;
				}
				
				String[] s = line.split("=");
				if(s.length == 2) {
					propertiesMap.put(s[0], s[1]);
				}
			}
		}
		
		try(PrintWriter writer = new PrintWriter(new FileWriter(this.propertiesFile))) {
			boolean mcAddr = false;
			boolean mcPvn = false;
			boolean wsAddr = false;
			for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				
				if(key.equals("minecraft_server_address")) {
					mcAddr = true;
					writer.println("minecraft_host=" + value);
				} else if(key.equals("websocket_server_address")) {
					wsAddr = true;
					writer.println("websocket_host=" + value);
				} else if(key.equals("minecraft_server_pvn")) {
					mcPvn = true;
					writer.print("minecraft_pvn=" + value);
				} else {
					writer.println(key + "=" + value);
				}
			}
			
			if(!mcAddr) {
				writer.println("minecraft_host=0.0.0.0:25565");
			}
			if(!mcPvn) {
				writer.println("minecraft_pvn=8");
			}
			if(!wsAddr) {
				writer.println("websocket_host=0.0.0.0:8080");
			}
		}
	}

}
