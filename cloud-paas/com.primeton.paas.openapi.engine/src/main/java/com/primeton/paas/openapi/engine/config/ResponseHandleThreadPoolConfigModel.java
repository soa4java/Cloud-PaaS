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
public class ResponseHandleThreadPoolConfigModel implements IConfigModel {
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -4613206732640535034L;

	private int minPoolSize = 20;

	private int maxPoolSize = 200;

	private int queueSize = 10000;

	private int aliveTimeout = 60;

	public int getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public int getAliveTimeout() {
		return aliveTimeout;
	}

	public void setAliveTimeout(int aliveTimeout) {
		this.aliveTimeout = aliveTimeout;
	}
	
}
