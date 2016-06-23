/**
 * 
 */
package com.primeton.paas.collect.common;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-12-17
 *
 */
public class LogMetaData {
	
	public static final String TYPE_APP = "user";
	public static final String TYPE_SYSTEM = "system";
	
	private String appName;
	private String type = TYPE_APP;
	private String instance;
	private long time = System.currentTimeMillis();
	private String content;
	
	/**
	 * 
	 */
	public LogMetaData() {
		super();
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the instance
	 */
	public String getInstance() {
		return instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public void setInstance(String instance) {
		this.instance = instance;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "LogMetaData [appName=" + appName + ", type=" + type
				+ ", instance=" + instance + ", time=" + time + ", content="
				+ content + "]";
	}
	
}
