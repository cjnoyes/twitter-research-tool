package com.cjnoyesw.twitter.research;

import com.cjnoyesw.twitter.research.searcher.*;

public class SearcherFactory {

	public static Searcher getInstance(String type) {
		return getInstance(type,null);
	}
	
	
    public static Searcher getInstance(String type, String subType) {
    	if (type.toLowerCase().equals("trend")) {
    		return new TrendSearcher(subType);
    	}
    	else if (type.toLowerCase().equals("sample-stream")) {
    		return new SampleStreamSearcher(subType);
    	}
    	else if (type.toLowerCase().equals("local-trend")) {
    		return new LocationTrendSearcher(subType);
    	}
    	else if (type.toLowerCase().equals("filter-stream")) {
    		return new QueryStreamSearcher(subType);
    	}
    	else if (type.toLowerCase().equals("search-query")) {
    		return new QuerySearcher(subType);
    	}
    	return null;
	}
}
