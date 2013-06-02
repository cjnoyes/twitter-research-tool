/**
 * 
 */
package com.cjnoyesw.twitter.research;

import java.util.Map;
import java.util.HashMap;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Christopher J. Noyes
 *
 */
public class Tool {

	public Tool() {
		args = new HashMap<String,String>();
		confBuilder = new ConfigurationBuilder();
	}
	
	public Tool(Map<String,String> args) {
		this.args = args;
		confBuilder = new ConfigurationBuilder();
	}
	
	public Tool(String config) {
		args = new HashMap<String,String>();
		confBuilder = new ConfigurationBuilder();
        args.put(Constants.CONFIG_FILE, config);
	}
	
	@SuppressWarnings("static-access")
	public void execute() throws Exception {
		Searcher searcher = SearcherFactory.getInstance(searchType, searchSubtype);
		if (searcher == null) {
			throw new Exception("Invalid Search Type: " + searchType);
		}
		searcher.setConfiguration(configuration);
		searcher.setConfigurationBuilder(confBuilder);
		DataLogger data = new DataLogger(workdir,delimType);
		data.setType(searchType + "-" + searchSubtype);
		searcher.setDataLogger(data);
		searcher.search();
		if (!searcher.isCompleted()) {
			while (!searcher.isCompleted()) {
				Thread.currentThread().sleep(1000);
			}
			data.close();
		}
	}
	
	
	public void put(String key, String value) throws Exception {
		if (setup_start) {
			throw new Exception("Cannot add properties after setup() called");
		}
		args.put(key,value);
	}
	
	public void setup() throws Exception {
		setup_start = true;
		String config = (String)args.get("config");
		if (config != null) {	
			configuration = new Configuration(config);
			configuration.load();
		}
		else {
			configuration = new Configuration();
			try {
				configuration.load();
			}
			catch (Exception e) {
			}
		}
		for (String key: args.keySet()) {
			Object value = args.get(key);
			if (value != null) {
				configuration.getProperties().put(key, value);
			}
		}
		
		basedir = configuration.getProperty("basedir");
	    if (basedir.equals(".")) {
	    	File base = new File(basedir);
	    	basedir = base.getCanonicalPath();
	    }
		File file = new File(basedir);
		if (!file.exists()) {
			file.mkdirs();
		}
		if (!file.canWrite()) {
			logger.error("Can't Write in " + basedir);
		}
		project = configuration.getProperty("project");
		if (project.length() > 0) {
		   file = new File(file,project);
		   if (!file.exists()) {
			   file.mkdirs();
		   }
		   if (!file.canWrite()) {
			   logger.error("Can't Write in " + file.getCanonicalPath());
		   }
		}
		workdir = file.getAbsolutePath();
		delimType = configuration.getProperty("delim_type","tab");
		searchType = configuration.getProperty("search_type");
		if ("".equals(searchType)) {
			logger.error("Error No search_type parameter Provided");
		}
		searchSubtype = configuration.getProperty("search_subtype");
		if ("".equals(searchSubtype)) {
			logger.error("Error No search_subtype parameter Provided");
		}
		confBuilder = ConfigurationBuilderHelper.getBuilder(configuration);
	}
	
	
	public static void info() {
		System.out.println("No arguments.");
		logger.error("No Arguments");
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger = LoggerFactory.getLogger(Tool.class);
		if (args.length == 0) {
			info();
			return;
		}
		// TODO Auto-generated method stub
        HashMap<String,String> map = new HashMap<String,String>();
        for (int i = 0; i < args.length-1; i+=2) {
        	String key = args[i];
        	String value = args[i+1];
        	System.out.println("Arg " + key + "=" + value);
        	logger.info("Arg " + key + "=" + value);
        	map.put(key, value);
        }
        Tool tool = new Tool(map);
        try {
        	tool.setup();
        	tool.execute();
        }
        catch (Exception e) {
        	logger.error(e.getMessage(),e);
        	System.out.println("Error: " + e.getMessage());
        }
	}
    
	private static Logger logger;
	private Map<String,String> args;
	private Configuration configuration;
	private ConfigurationBuilder confBuilder;
	private String basedir;
	private String project;
	private String searchType;
	private String searchSubtype;
	private String workdir;
	private String delimType;
	private boolean setup_start = false;
}
