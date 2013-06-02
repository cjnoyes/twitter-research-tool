package com.cjnoyesw.twitter.research.searcher;

import java.util.TimerTask;

public class StreamTimerTask extends TimerTask {

	public StreamTimerTask() {
		
	}
	
	public StreamTimerTask(StreamStatusAdapter adapter) {
		this.adapter = adapter;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (adapter != null) {
			adapter.closeStream();	
		}
	}

	
	
	public StreamStatusAdapter getAdapter() {
		return adapter;
	}



	public void setAdapter(StreamStatusAdapter adapter) {
		this.adapter = adapter;
	}



	private StreamStatusAdapter adapter;
	
}
