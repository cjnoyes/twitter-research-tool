package com.cjnoyesw.twitter.research.searcher;

import twitter4j.*;

public class QuerySearcher extends AbstractSearcher {

	public QuerySearcher(String subtype) {
		this.subtype = subtype;
	}
	
	
	protected int page(Query query) throws Exception {
	   int lines = 0;
	   QueryResult result = twitter.search(query);
	  
	   for (Tweet tweet: result.getTweets()) {
		   dataLogger.writeField(tweet.getCreatedAt());
		   dataLogger.writeField(tweet.getFromUser());
		   dataLogger.writeField(tweet.getFromUserId());
		   dataLogger.writeField(tweet.getId());
		   dataLogger.writeField(tweet.getIsoLanguageCode());
		   dataLogger.writeField(tweet.getLocation());
		   dataLogger.writeField(tweet.getText());
		   dataLogger.writeField(tweet.getToUser());
		   dataLogger.writeField(tweet.getSource());
		   dataLogger.writeField(tweet.getInReplyToStatusId());
		   dataLogger.endLine();
		   lines++;
	   }
	   
	   return lines;
	}
	
	
	@Override
	public void search() throws Exception {
		// TODO Auto-generated method stub
		String since = configuration.getProperty("query_since", "");
		String until = configuration.getProperty("query_until", "");
		String lang = configuration.getProperty("query_lang", "");
		String text = configuration.getProperty("query_text", "");
		String sinceid = configuration.getProperty("query_sinceid", "0");
		int count = -1;
		
		Query query = new Query();
		query.setPage(1);
		query.setRpp(100);
		if (text != null && text.length() > 0) {
			query.setQuery(text);
		}
		if (since != null && since.length() > 0) {
			query.setSince(since);
		}
		if (until != null && until.length() > 0) {
			query.setUntil(until);
		}
		if (lang != null && lang.length() > 0) {
			query.setSince(lang);
		}
		long id = 0;
	    try {
			id = Long.parseLong(sinceid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}
	    if (id > 0l) {
	    	query.setSinceId(id);
	    }
		TwitterFactory factory = new TwitterFactory(builder.build());
	    try {
	       dataLogger.open();
	       twitter = factory.getInstance();
	       do {
	    	  page(query);
	    	  query.setPage(query.getPage()+1);
	       } while (count > 0);
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
