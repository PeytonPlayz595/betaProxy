package net.betaProxy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.betaProxy.utils.jdk_fix.Properties;
import net.lax1dude.log4j.LogManager;
import net.lax1dude.log4j.Logger;

public class PropertiesManager {
	
	public static Logger logger = LogManager.getLogger("Beta Proxy");
	
	private Properties properties = new Properties();
	private File propertiesFile;
	
	public PropertiesManager(File file) {
		this.propertiesFile = file;
		if(file.exists()) {
			try {
				this.properties.load(new FileInputStream(file));
			} catch(Exception e) {
				logger.warn("Failed to load " + file.getName(), file);
				this.createDefaultProperties();
			}
		} else {
			logger.warn(file.getName() + " does not exist");
			this.createDefaultProperties();
		}
	}
	
	private void createDefaultProperties() {
		logger.info("Generating new properties file");
		this.saveProperties();
	}
	
	private void saveProperties() {
		try {
			this.properties.store(new FileOutputStream(this.propertiesFile), "Beta Proxy server config");
		} catch (Exception var2) {
			logger.info("Failed to save " + this.propertiesFile.getName(), var2);
			this.createDefaultProperties();
		}
	}
	
	public String getProperty(String key, String defaultValue) {
		if(!this.properties.containsKey(key)) {
			this.properties.setProperty(key, defaultValue);
			this.saveProperties();
		}

		return this.properties.getProperty(key, defaultValue);
	}
	
	public int getProperty(String key, int defaultValue) {
		try {
			return Integer.parseInt(this.getProperty(key, "" + defaultValue));
		} catch (Exception var4) {
			this.properties.setProperty(key, "" + defaultValue);
			return defaultValue;
		}
	}
	
	public boolean getProperty(String key, boolean defaultValue) {
		try {
			return Boolean.parseBoolean(this.getProperty(key, "" + defaultValue));
		} catch (Exception var4) {
			this.properties.setProperty(key, "" + defaultValue);
			return defaultValue;
		}
	}


}
