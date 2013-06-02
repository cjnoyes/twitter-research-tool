package com.cjnoyesw.twitter.research.searcher;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.text.SimpleDateFormat;

import com.cjnoyesw.twitter.research.Searcher;

import twitter4j.*;

public class TrendSearcher extends AbstractSearcher implements Searcher {

	public TrendSearcher(String subtype) {
		this.subtype = subtype;
	}
	
	
	protected void searchRange(String start) throws Exception {
		String count = configuration.getProperty("trend_count", "30");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
	    try {
			startDate =df.parse(start);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			Exception ex = new Exception("Can't parse date: " + start,e);
			throw ex;
		}
	    int trendCount = 30;
	    try {
			trendCount = Integer.parseInt(count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			logger.warn("defaulting to 30 requests");
		}
	    Calendar cal = GregorianCalendar.getInstance();
	    cal.setTime(startDate);
	    TwitterFactory factory = new TwitterFactory(builder.build());
	    try {
	       dataLogger.open();
	       Twitter twitter = factory.getInstance();
           ResponseList<Trends> list = null;
	       for (int i = 0; i < trendCount; i++) {
	    	   if (subtype.toLowerCase().equals("daily")) {
	        		list = twitter.getDailyTrends(cal.getTime(),false);
	        		cal.add(Calendar.DATE, 1);
	        	}
	        	else if (subtype.toLowerCase().equals("weekly")) {
	        		list = twitter.getWeeklyTrends();
	        		cal.add(Calendar.DATE, 7);
	        	}
	        	else {
	        		logger.error("Invalid subtype " + subtype);
	        		return;
	        	}
	        	if (list == null) {
	        	   logger.warn("No Respones returned");
	        	   return;
	        	}
	        	
	        	for (Trends trends: list) {
	        		Date date = trends.getTrendAt();
	        		for (Trend trend: trends.getTrends()) {
	        			dataLogger.writeField(date);
	        			dataLogger.writeField(trend.getQuery());
	        		    dataLogger.endLine();
	        		}
	        	}
	        	if (list != null && list.getRateLimitStatus().getRemainingHits() ==0  ) {
	        		logger.warn("About to be cut off due to rate limit, you may try again after " + SimpleDateFormat.getDateTimeInstance().format(list.getRateLimitStatus().getResetTime()));
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
	
	
	public void search() throws Exception {
		// TODO Auto-generated method stub
		String start = configuration.getProperty("trend_start", "");
		if (start.length() > 0) {
		   searchRange(start);
		   return;
		}
        TwitterFactory factory = new TwitterFactory(builder.build());
        try {
        	dataLogger.open();
        	Twitter twitter = factory.getInstance();
        	ResponseList<Trends> list = null;
        	if (subtype.toLowerCase().equals("daily")) {
        		list = twitter.getDailyTrends();
        	}
        	else if (subtype.toLowerCase().equals("weekly")) {
        		list = twitter.getWeeklyTrends();
        	}
        	else {
        		logger.error("Invalid subtype " + subtype);
        	}
        	if (list == null) {
        	   logger.warn("No Respones returned");
        	}
            
        	for (Trends trends: list) {
        		Date date = trends.getTrendAt();
        		for (Trend trend: trends.getTrends()) {
        			dataLogger.writeField(date);
        			dataLogger.writeField(trend.getQuery());
        		    dataLogger.endLine();
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


	
	private String subtype;
}
