package com.cjnoyesw.twitter.research;

import twitter4j.conf.ConfigurationBuilder;

public class ConfigurationBuilderHelper {
      
	public static ConfigurationBuilder getBuilder(Configuration conf) {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		String debug = conf.getProperty("debug","false");
		if ("true".equals(debug)) {
			builder.setDebugEnabled(true);
		}
		else {
			builder.setDebugEnabled(false);
		}
		String val = conf.getProperty("consumer_key");
		if (val.length() > 0) {
			builder.setOAuthConsumerKey(val);
		}
		val = conf.getProperty("consumer_secret");
		if (val.length() > 0) {
			builder.setOAuthConsumerSecret(val);
		}
		val = conf.getProperty("access_token");
		if (val.length() > 0) {
			builder.setOAuthAccessToken(val);
		}
		val = conf.getProperty("access_token_secret");
		if (val.length() > 0) {
			builder.setOAuthAccessTokenSecret(val);
		}
		return builder;
	}
	
}
