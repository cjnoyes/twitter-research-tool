package com.cjnoyesw.twitter.research.searcher;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;

import twitter4j.*;

public class SampleStreamSearcher extends AbstractSearcher {

	public SampleStreamSearcher( String subtype ) {
		
	}
	
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		try {
			statusStream.close();
			stream.shutdown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}
		super.stop();
	}

	@Override
	public void search() throws Exception {
		// TODO Auto-generated method stub
        String count = configuration.getProperty("stream_count","0");
        int streamCount = 0;
	    try {
			streamCount = Integer.parseInt(count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			logger.warn("Output will not be length limited");
		}
	    String mins = configuration.getProperty("stream_minutes","0");
        int streamMinutes = 0;
	    try {
			streamMinutes = Integer.parseInt(mins);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			logger.warn("Stream will not be time limited");
		}
	    Calendar cal = GregorianCalendar.getInstance();
	    if (streamMinutes > 0) {
	    	cal.add(Calendar.MINUTE, streamMinutes);
	    }
	    TwitterStreamFactory factory = new TwitterStreamFactory(builder.build());
	    try {
	        stream = factory.getInstance();
	        dataLogger.open();
	        StreamStatusAdapter adapter = new StreamStatusAdapter();
	        adapter.setDataLogger(dataLogger);
	        if (streamCount > 0) {
	        	adapter.setMaxItems(streamCount);
	        }
	        if (streamMinutes > 0) {
	        	StreamTimerTask task = new StreamTimerTask(adapter);
	        	timer = new Timer();
	        	timer.schedule(task, cal.getTime());
	        }
	        adapter.setSearcher(this);
	        stream.addConnectionLifeCycleListener(adapter);
	        stream.addListener(adapter);
	        stream.sample();
	        statusStream = stream.getSampleStream();
	        adapter.setStream(statusStream);
	        completed = false;
	    }
	    catch (Exception e) {
	    	completed = true;
        	logger.error(e.getMessage(),e);
			Exception ex = new Exception(e.getMessage(),e);
			throw ex;
        }
	}
	
	
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}


	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return completed;
	}

	private boolean completed;
	private Timer timer;
	private TwitterStream stream;
    private StatusStream statusStream;
}
