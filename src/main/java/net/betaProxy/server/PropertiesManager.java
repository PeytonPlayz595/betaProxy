package net.betaProxy.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PropertiesManager {
	
	private File propertiesFile;
	private Map<String, String> propertiesMap = new HashMap<String, String>();
	
	public PropertiesManager(File file) {
		this.propertiesFile = file;
	}
	
	public String getProperty(String key, String defaultKey) {
		String s = propertiesMap.get(key);
		if(s == null) {
			ProxyServer.getLogger().warn("Properties file has no value for '" + key + ".' Using default value '" + defaultKey + ".'");
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
	
	public void loadProperties() throws FileNotFoundException, IOException {
		if(!propertiesFile.exists()) {
			propertiesFile.createNewFile();
		}
		
		try(FileReader fr = new FileReader(propertiesFile); BufferedReader reader = new BufferedReader(fr)) {
			String line = "";
			
			while(true) {
				line = reader.readLine();
				if(line == null) {
					break;
				}
				
				String[] valueSplitter = line.split("=");
				propertiesMap.put(valueSplitter[0], valueSplitter[1]);
			}
			
		} catch(Exception e) {
			throw new IOException("Failed to load properties file", e);
		}
	}

}
