/**
 * 
 */
package com.primeton.paas.console.common.monitor;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class LoadAverageInfo {
	
	private String loadAverages;
	private String times;
	
	public LoadAverageInfo() {
		super();
	}
	
	public LoadAverageInfo(String loadAverages, String times) {
		super();
		this.loadAverages = loadAverages;
		this.times = times;
	}

	public String getLoadAverages() {
		return loadAverages;
	}
	
	public void setLoadAverages(String loadAverages) {
		this.loadAverages = loadAverages;
	}
	
	public String getTimes() {
		return times;
	}
	
	public void setTimes(String times) {
		this.times = times;
	}

	public String toString() {
		return "LoadAverageInfo [loadAverages=" + loadAverages + ", times="
				+ times + "]";
	}
	
}
