/**
 * 
 */
package com.primeton.paas.console.common.monitor;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MemoryInfo {
	
	private String uses; // 使用率
	private String times; // 时刻

	public MemoryInfo() {
		super();
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getUses() {
		return uses;
	}

	public void setUses(String uses) {
		this.uses = uses;
	}

	public String toString() {
		return "MemoryInfo [uses=" + uses + ", times=" + times + "]";
	}
	
}
