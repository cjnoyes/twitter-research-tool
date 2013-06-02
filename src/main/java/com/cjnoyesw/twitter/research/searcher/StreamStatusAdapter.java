/**
 * 
 */
package com.cjnoyesw.twitter.research.searcher;

import java.io.IOException;

import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.ConnectionLifeCycleListener;
import twitter4j.StatusStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cjnoyesw.twitter.research.DataLogger;

/**
 * @author Christopher J. Noyes
 *
 */
public class StreamStatusAdapter extends StatusAdapter implements ConnectionLifeCycleListener {

	/**
	 * 
	 */
	public StreamStatusAdapter() {
		// TODO Auto-generated constructor stub
		logger = LoggerFactory.getLogger(StreamStatusAdapter.class);
		maxItems=0;
	}

	@Override
	public void onException(Exception ex) {
		// TODO Auto-generated method stub
		logger.error(ex.getMessage(),ex);
		super.onException(ex);
	}

	@Override
	public void onStatus(Status status) {
		// TODO Auto-generated method stub
		super.onStatus(status);
		if (maxItems == 0 || counter < maxItems) {
		try {
		   dataLogger.writeField(status.getCreatedAt());
		   dataLogger.writeField(status.getId());
		   dataLogger.writeField(status.getSource());
		   dataLogger.writeField(status.getText());
		   dataLogger.writeField(status.getRetweetCount());
		   if (status.getRetweetCount() > 0) {
			  dataLogger.writeField(status.getRetweetedStatus().getSource());
			  dataLogger.writeField(status.getRetweetedStatus().getText());
			  dataLogger.writeField(status.getRetweetedStatus().getUser().getScreenName());
		   }
		   else {
			   dataLogger.writeField("-");
			   dataLogger.writeField("-");
			   dataLogger.writeField("-");
		   }
		   dataLogger.writeField(status.getInReplyToScreenName() != null?status.getInReplyToScreenName():"-");
		   dataLogger.writeField(status.getUser().getScreenName());
		   dataLogger.writeField(status.getInReplyToStatusId());
		   dataLogger.writeField(status.isFavorited());
		   dataLogger.writeField(status.isRetweet());
		   dataLogger.writeField(status.isRetweetedByMe());
		   dataLogger.writeField(status.getGeoLocation()!= null?status.getGeoLocation().getLatitude():0.0);
		   dataLogger.writeField(status.getGeoLocation()!= null?status.getGeoLocation().getLongitude():0.0);
		   dataLogger.writeField(status.getPlace() != null?status.getPlace().getName():"-");
		   dataLogger.endLine();
		   counter++;
		   super.onStatus(status);
		}
		catch (Exception e) {
		   logger.error(e.getMessage(),e);
		   
		}
		}
		else {
			try {
				searcher.setCompleted(true);
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(),e);
			}
		}
	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		// TODO Auto-generated method stub
		logger.warn("Limited Statuses: " + numberOfLimitedStatuses);
		super.onTrackLimitationNotice(numberOfLimitedStatuses);
	}
	
	
	public DataLogger getDataLogger() {
		return dataLogger;
	}

	public void setDataLogger(DataLogger dataLogger) {
		this.dataLogger = dataLogger;
	}

    
	@Override
	public void onCleanUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub
	
	}
	
	public StatusStream getStream() {
		return stream;
	}

	public void setStream(StatusStream stream) {
		this.stream = stream;
	}

	public int getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

    public void closeStream() {
    	try {
    		searcher.setCompleted(true);
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}
    }

    
    
	public AbstractSearcher getSearcher() {
		return searcher;
	}

	public void setSearcher(AbstractSearcher searcher) {
		this.searcher = searcher;
	}



	private StatusStream stream;
	private int counter;
    private int maxItems;
	private DataLogger dataLogger;
    private Logger logger;
	private AbstractSearcher searcher;
}
