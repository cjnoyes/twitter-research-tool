package com.cjnoyesw.twitter.research.searcher;


import java.util.Date;

import twitter4j.*;

public class LocationTrendSearcher extends AbstractSearcher {

	public LocationTrendSearcher(String subtype) {
		// TODO Auto-generated constructor stub
		this.subtype = subtype;
	}

	protected void writeTrends(Trends trends, Location loc) throws Exception {
    	Date date = trends.getTrendAt();
    	for (Trend trend: trends.getTrends()) {
    		dataLogger.writeField(date);
    		dataLogger.writeField(loc.getCountryCode());
    		dataLogger.writeField(loc.getCountryName());
    		dataLogger.writeField(loc.getName());
    		dataLogger.writeField(loc.getPlaceCode());
    		dataLogger.writeField(loc.getPlaceName());
    		dataLogger.writeField(loc.getURL());
    		dataLogger.writeField(loc.getWoeid());
    		dataLogger.writeField(trend.getName());
    		dataLogger.writeField(trend.getQuery());
    		dataLogger.endLine();
    		}
	}
	
	protected void processWoeid(Location loc) throws Exception {
		Trends trends = twitter.getLocationTrends(loc.getWoeid());
		if (trends != null) {
			writeTrends(trends, loc);
		}
	}
	
	@Override
	public void search() throws Exception {
		// TODO Auto-generated method stub
		 TwitterFactory factory = new TwitterFactory(builder.build());
		 try {
		    dataLogger.open();
		    twitter = factory.getInstance();
		 }
		 catch (Exception e) {
			if (dataLogger != null) {
		       	dataLogger.close();
		    }
	       	logger.error(e.getMessage(),e);
			Exception ex = new Exception(e.getMessage(),e);
			throw ex;
	     }
	   
		 if (subtype.toLowerCase().equals("geo")) {
        	searchGeo();
        	return;
         }
         try {
        	ResponseList<Location> list = twitter.getAvailableTrends();
        	if (list != null) {
        		for (Location loc: list) {
        			processWoeid(loc);
        		}
        	}
         }
         catch (Exception e) {
         	logger.error(e.getMessage(),e);
 			Exception ex = new Exception(e.getMessage(),e);
 			throw ex;
         }
         finally {
         	if (dataLogger != null) {
         		dataLogger.close();
         	}
         }
	}

	protected void searchGeo() throws Exception {
		String val = configuration.getProperty("location_latitude", "");
		double latitude = 0;
	    try {
			latitude = Double.parseDouble(val);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			logger.warn("defaulting to 0.0 latitude");
		}
	    val = configuration.getProperty("location_longitude", "");
		double longitude = 0;
	    try {
			longitude = Double.parseDouble(val);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			logger.warn("defaulting to 0.0 longitude");
		}
	    GeoLocation geo = new GeoLocation(latitude,longitude);
	    try {
        	ResponseList<Location> list = twitter.getAvailableTrends(geo);
        	if (list != null) {
        		for (Location loc: list) {
        			processWoeid(loc);
        		}
        	}
         }
         catch (Exception e) {
         	logger.error(e.getMessage(),e);
 			Exception ex = new Exception(e.getMessage(),e);
 			throw ex;
         }
         finally {
         	if (dataLogger != null) {
         		dataLogger.close();
         	}
         }
	    
	}
	
	
	private Twitter twitter;
	private String subtype;
}
