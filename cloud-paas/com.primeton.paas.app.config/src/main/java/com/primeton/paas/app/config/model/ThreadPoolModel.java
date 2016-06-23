/**
 * 
 */
package com.primeton.paas.app.config.model;

import com.primeton.paas.app.config.IConfigModel;

/**
 * /Cloud/Cesium/Config/Global/threadPool. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ThreadPoolModel implements IConfigModel {

	private static final long serialVersionUID = -264683093907158761L;

	private String poolName;
	
	private int minPoolSize;
	
	private int maxPoolSize;
	
	private int shrinkTime;

	public ThreadPoolModel() {
		super();
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

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

	public int getShrinkTime() {
		return shrinkTime;
	}

	public void setShrinkTime(int shrinkTime) {
		this.shrinkTime = shrinkTime;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "ThreadPoolModel(poolName="+poolName+",minPoolSize="+minPoolSize+",maxPoolSize="+maxPoolSize+",shrinkTime="+shrinkTime+")";
	}

}
