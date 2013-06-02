package com.cjnoyesw.twitter.research;

import java.util.Properties;
import java.io.FileReader;


public class Configuration {

	public Configuration() {
		filename="twitter-research.properties";
		properties = new Properties();
	}
	
	public Configuration(String filename) {
		this.filename  = filename;
		properties = new Properties();
	}
	
	public void load() throws Exception {
		FileReader reader = null;
		try {
			reader = new FileReader(filename);
			properties.load(reader);
		}
		catch (Exception e) {
			throw new Exception("Problem loading configuration: " + e.getMessage());
		}
		finally {
			if (reader != null) {
				try {
				   reader.close();
				}
				catch (Exception e) {
					
				}
			}
		}
	}
	
	public String getProperty(String key, String deflt) {
	   return properties.getProperty(key, deflt);
	}
	
	public String getProperty(String key) {
		return getProperty(key,"");
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

    

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}



	private Properties properties;
	private String filename;
}
