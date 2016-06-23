/**
 * 
 */
package com.primeton.paas.console.common.monitor;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CPUInfo {
	
	private String uses; // 使用率
	private String times; // 时刻
	
	public CPUInfo() {
		super();
	}

	public CPUInfo(String uses, String times) {
		super();
		this.uses = uses;
		this.times = times;
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
		return "CPUInfo [uses=" + uses + ", times=" + times + "]";
	}
	
}
