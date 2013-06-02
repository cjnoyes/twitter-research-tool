package com.cjnoyesw.twitter.research.searcher;

import twitter4j.*;
import com.cjnoyesw.twitter.research.*;


public class UserHelper {

	public UserHelper(AbstractSearcher searcher) {
		this.searcher = searcher;
	}
	
	long [] getUserIds(String [] screennames) throws Exception {
		long [] userIds = null;
		TwitterFactory factory = new TwitterFactory(ConfigurationBuilderHelper.getBuilder(searcher.getConfiguration()).build());
		Twitter twitter = factory.getInstance();
		ResponseList<User> list = twitter.lookupUsers(screennames);
		if (list == null) {
			return userIds;
		}
		userIds = new long[list.size()];
		int index = 0;
		for (User user: list) {
			userIds[index++]=user.getId();
		}
		return userIds;
	}
	
	AbstractSearcher searcher;
	
}
