package com.cjnoyesw.twitter.research;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataLogger {

	public DataLogger() {
		delim="tab";
		workdir=".";
		logger=LoggerFactory.getLogger(DataLogger.class);
	}
	
	public DataLogger(String workdir, String delim) {
		this.workdir = workdir;
		this.delim = delim;
		logger=LoggerFactory.getLogger(DataLogger.class);
	}
	
	
	public void close() {
		if (!open) {
			return;
		}
		logger.info("Closing " + outputfile);
		if (writer != null) {
			try {
				writer.close();
				open = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(),e);
			}
		}
	}
	
	public void open() throws Exception {
		String name = type + '-';
		dateFormat = SimpleDateFormat.getDateTimeInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-dd");
		name += df.format(new Date()) + (delim.equals("csv")?".csv":".txt");
		File file = new File(workdir,name);
		String os = System.getProperty("os.name");
		if (os.contains("win")) {
			endLine = "\r\n";
		}
		else {
			endLine = "\n";
		}
		outputfile = file.getAbsolutePath();
		logger.info("Will write to " + outputfile);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			writer = new OutputStreamWriter(fos,"UTF-8");
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			Exception ex = new Exception(e.getMessage(),e);
			throw ex;
		}
		open=true;
		currField = 0;
	}
	
	public void endLine() throws Exception {
		if (!open) {
			return;
		} 
		try {
	    	 writer.append(endLine);
	    	 currField = 0;
	     }
	     catch (Exception e) {
				logger.error(e.getMessage(),e);
				Exception ex = new Exception(e.getMessage(),e);
				throw ex;
			}
	}
	
	public void writeField(String field) throws Exception {
		if (!open) {
			return;
		}
		if (field==null) {
			field = "none";
		}
		field = field.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t","");
		if (field.startsWith("\"")) {
			field = field.substring(1);
		}
		if (field.endsWith("\"")) {
			field = field.substring(0,field.length()-1);
		}
		try {
			 if (currField != 0 && delim.equals("tab")) {
				 writer.append("\t");
			 }
			 else if (currField != 0) {
				 writer.append(",");
			 }
	    	 if (delim.equals("csv")) {
	    		 writer.append("\"");
	    	 }
	    	 writer.append(field);
	    	 if (delim.equals("csv")) {
	    		 writer.append("\"");
	    	 }
	    	 currField++;
	     }
	     catch (Exception e) {
				logger.error(e.getMessage(),e);
				Exception ex = new Exception(e.getMessage(),e);
				throw ex;
			}
	}
	
	public void writeField(Date field) throws Exception {
		if (!open) {
			return;
		}
		try {
			 if (currField != 0 && delim.equals("tab")) {
				 writer.append("\t");
			 }
			 else if (currField != 0) {
				 writer.append(",");
			 }
	    	 if (delim.equals("csv")) {
	    		 writer.append("\"");
	    	 }
	    	 writer.append(dateFormat.format(field));
	    	 if (delim.equals("csv")) {
	    		 writer.append("\"");
	    	 }
	    	 currField++;
	     }
	     catch (Exception e) {
				logger.error(e.getMessage(),e);
				Exception ex = new Exception(e.getMessage(),e);
				throw ex;
			}
	}
	
	public void writeField(int field) throws Exception {
		if (!open) {
			return;
		}
		try {
			 if (currField != 0 && delim.equals("tab")) {
				 writer.append("\t");
			 }
			 else if (currField != 0) {
				 writer.append(",");
			 }
	    	 writer.append(field+"");
	    	 currField++;
	     }
	     catch (Exception e) {
				logger.error(e.getMessage(),e);
				Exception ex = new Exception(e.getMessage(),e);
				throw ex;
			}
	}
	
	public void writeField(long field) throws Exception {
		if (!open) {
			return;
		}
		try {
			 if (currField != 0 && delim.equals("tab")) {
				 writer.append("\t");
			 }
			 else if (currField != 0) {
				 writer.append(",");
			 }
	    	 writer.append(field+"");
	    	 currField++;
	     }
	     catch (Exception e) {
				logger.error(e.getMessage(),e);
				Exception ex = new Exception(e.getMessage(),e);
				throw ex;
			}
	}
	
	public void writeField(double field) throws Exception {
		if (!open) {
			return;
		}
		try {
			 if (currField != 0 && delim.equals("tab")) {
				 writer.append("\t");
			 }
			 else if (currField != 0) {
				 writer.append(",");
			 }
	    	 writer.append(field+"");
	    	 currField++;
	     }
	     catch (Exception e) {
				logger.error(e.getMessage(),e);
				Exception ex = new Exception(e.getMessage(),e);
				throw ex;
			}
	}
	
	public void writeField(boolean field) throws Exception {
		if (!open) {
			return;
		}
		try {
			 if (currField != 0 && delim.equals("tab")) {
				 writer.append("\t");
			 }
			 else if (currField != 0) {
				 writer.append(",");
			 }
	    	 writer.append(field?"true":"false");
	    	 currField++;
	     }
	     catch (Exception e) {
				logger.error(e.getMessage(),e);
				Exception ex = new Exception(e.getMessage(),e);
				throw ex;
			}
	}
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDelim() {
		return delim;
	}

	public void setDelim(String delim) {
		this.delim = delim;
	}

	public String getWorkdir() {
		return workdir;
	}

	public void setWorkdir(String workdir) {
		this.workdir = workdir;
	}

	private boolean open;
	private int currField;
	private String endLine;
	private Logger logger;
	private String type;
	private OutputStreamWriter writer;
	private String delim;
	private String workdir;
	private String outputfile;
	private DateFormat dateFormat;
	
}
