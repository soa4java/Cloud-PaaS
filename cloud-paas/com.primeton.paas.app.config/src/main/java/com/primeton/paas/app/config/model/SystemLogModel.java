/**
 * 
 */
package com.primeton.paas.app.config.model;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.app.config.IConfigModel;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SystemLogModel implements IConfigModel {
	
	private static final long serialVersionUID = -4941127322487811054L;

	public static final String LEVEL_DEBUG = "DEBUG";

	public static final String LEVEL_INFO = "INFO";

	public static final String LEVEL_WARN = "WARN";

	public static final String LEVEL_ERROR = "ERROR";

	public static final String LEVEL_FATAL = "FATAL";
	
	private static List<String> levels = new ArrayList<String>();
	
	static {
		levels.add(LEVEL_DEBUG);
		levels.add(LEVEL_INFO);
		levels.add(LEVEL_WARN);
		levels.add(LEVEL_ERROR);
		levels.add(LEVEL_FATAL);
	}

	private String logLevel;

	public SystemLogModel() {
		super();
	}

	/**
	 * 
	 * @param logLevel
	 */
	public SystemLogModel(String logLevel) {
		super();
		if (logLevel != null && levels.contains(logLevel)) {
			this.logLevel = logLevel;
		} else {
			this.logLevel = LEVEL_INFO;
		}
	}

	/**
	 * @return the logLevel
	 */
	public String getLogLevel() {
		return logLevel;
	}


	/**
	 * @param logLevel the logLevel to set
	 */
	public void setLogLevel(String logLevel) {
		if (logLevel != null && levels.contains(logLevel)) {
			this.logLevel = logLevel;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return SystemLogModel.class.getSimpleName() + " (logLevel = " + logLevel + ")";
	}
	
}
