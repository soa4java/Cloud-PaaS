/**
 * 
 */
package com.primeton.paas.app.config.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.primeton.paas.app.config.IConfigModel;

/**
 * /Cloud/Cesium/Config/App/${appName}/userLog. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class UserLogModel implements IConfigModel {
	
	private static final long serialVersionUID = -7817250058366622684L;
	
	public static final String LEVEL_DEBUG = "DEBUG";

	public static final String LEVEL_INFO = "INFO";

	public static final String LEVEL_WARN = "WARN";

	public static final String LEVEL_ERROR = "ERROR";

	public static final String LEVEL_FATAL = "FATAL";
	
	public static final String DEFAULT_USERLOG_TYPE = "user";
	
	private static List<String> levels = new ArrayList<String>();
	
	static {
		levels.add(LEVEL_DEBUG);
		levels.add(LEVEL_INFO);
		levels.add(LEVEL_WARN);
		levels.add(LEVEL_ERROR);
		levels.add(LEVEL_FATAL);
	}

	private Map<String,String> userLogs = new HashMap<String, String>();

	public UserLogModel() {
		super();
	}
	
	/**
	 * 
	 * @param userLogs
	 */
	public UserLogModel(Map<String, String> userLogs) {
		if (null == userLogs) {
			this.userLogs = new HashMap<String, String>();
		} else {
			this.userLogs = userLogs;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> getUserLogs() {
		return userLogs;
	}

	/**
	 * 
	 * @param userLogs
	 */
	public void setUserLogs(Map<String, String> userLogs) {
		if (null == userLogs) {
			this.userLogs = new HashMap<String, String>();
		} else {
			this.userLogs = userLogs;
		}
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public String getLogLevel(String type) {
		return null == type ? null : userLogs.get(type);
	}
	
	/**
	 * 
	 * @param type
	 * @param level
	 */
	public void setLogLevel(String type, String level) {
		if (type != null) {
			if (level != null && levels.contains(level)) {
				userLogs.put(type, level);
			} else {
				userLogs.put(type, LEVEL_INFO);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return UserLogModel.class.getSimpleName() + "[ " + userLogs + " ]";
	}
	
}