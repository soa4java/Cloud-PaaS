/**
 * 
 */
package com.primeton.paas.openapi.httpin.config;

import com.primeton.paas.openapi.admin.config.IConfigModel;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HttpInConfigModel implements IConfigModel {
	
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -3095023048101165211L;
	
	private long timeout=-1;

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
}
