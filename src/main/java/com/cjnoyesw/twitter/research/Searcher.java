package com.cjnoyesw.twitter.research;

import twitter4j.conf.ConfigurationBuilder;

public interface Searcher {
      void search() throws Exception;
      void setDataLogger(DataLogger logger);
      void setConfiguration(Configuration cnf);
      void setConfigurationBuilder(ConfigurationBuilder bld);
      void stop();
      boolean isCompleted();
      void setCompleted(boolean flg);
}
