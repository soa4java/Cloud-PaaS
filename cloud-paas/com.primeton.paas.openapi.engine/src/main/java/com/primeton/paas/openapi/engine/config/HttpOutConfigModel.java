/**
 * 
 */
package com.primeton.paas.openapi.engine.config;

import com.primeton.paas.openapi.admin.config.IConfigModel;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HttpOutConfigModel implements IConfigModel {
	
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -6390731946517024200L;
	
	private int connectionTimeout=30;
	
	private int requestTimeout=30;
	
	private String encoding="UTF-8";

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
}
