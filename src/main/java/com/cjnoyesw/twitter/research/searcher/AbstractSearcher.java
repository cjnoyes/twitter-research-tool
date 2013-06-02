package com.cjnoyesw.twitter.research.searcher;

import twitter4j.conf.ConfigurationBuilder;

import twitter4j.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cjnoyesw.twitter.research.*;

public abstract class AbstractSearcher implements Searcher {

	public AbstractSearcher() {
		logger = LoggerFactory.getLogger("AbstractSearcher");
	}
	
	
	public void setDataLogger(DataLogger logger) {
		// TODO Auto-generated method stub
		dataLogger = logger;
	}

	public void setConfiguration(Configuration cnf) {
		// TODO Auto-generated method stub
		configuration = cnf;
	}

	public void setConfigurationBuilder(ConfigurationBuilder bld) {
		// TODO Auto-generated method stub
		builder = bld;
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

	public boolean isCompleted() {
		return true;
	}
	
	public void setCompleted(boolean flg) {
		
	}
	
	
	
	
	public ConfigurationBuilder getBuilder() {
		return builder;
	}


	public void setBuilder(ConfigurationBuilder builder) {
		this.builder = builder;
	}


	public Logger getLogger() {
		return logger;
	}


	public void setLogger(Logger logger) {
		this.logger = logger;
	}


	public DataLogger getDataLogger() {
		return dataLogger;
	}


	public Configuration getConfiguration() {
		return configuration;
	}




	protected DataLogger dataLogger;
	protected Configuration configuration;
	protected ConfigurationBuilder builder;
	protected Logger logger;
	
	
}
