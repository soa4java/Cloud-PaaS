/**
 * 
 */
package com.primeton.paas.console.common.monitor;

/**
 * 服务监控数据 用于页面展示
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class ServiceMonitorInfo {
	
	private boolean enableFlag;

	private String cpuUse;
	private String cpuFree;
	private String memUse;
	private String memFree;
	private String oneload;
	private String ioi;
	private String ioo;
	private String currentTime;

	public String getCpuUse() {
		return cpuUse;
	}

	public void setCpuUse(String cpuUse) {
		this.cpuUse = cpuUse;
	}

	public String getCpuFree() {
		return cpuFree;
	}

	public void setCpuFree(String cpuFree) {
		this.cpuFree = cpuFree;
	}

	public String getMemUse() {
		return memUse;
	}

	public void setMemUse(String memUse) {
		this.memUse = memUse;
	}

	public String getMemFree() {
		return memFree;
	}

	public void setMemFree(String memFree) {
		this.memFree = memFree;
	}

	public String getOneload() {
		return oneload;
	}

	public void setOneload(String oneload) {
		this.oneload = oneload;
	}

	public String getIoi() {
		return ioi;
	}

	public void setIoi(String ioi) {
		this.ioi = ioi;
	}

	public String getIoo() {
		return ioo;
	}

	public void setIoo(String ioo) {
		this.ioo = ioo;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public boolean isEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(boolean enableFlag) {
		this.enableFlag = enableFlag;
	}

	public ServiceMonitorInfo(boolean enableFlag, String cpuUse,
			String cpuFree, String memUse, String memFree, String oneload,
			String ioi, String ioo, String currentTime) {
		super();
		this.cpuUse = cpuUse;
		this.cpuFree = cpuFree;
		this.memUse = memUse;
		this.memFree = memFree;
		this.oneload = oneload;
		this.ioi = ioi;
		this.ioo = ioo;
		this.currentTime = currentTime;
		this.enableFlag = enableFlag;
	}

	public ServiceMonitorInfo() {
		super();
	}

	public String toString() {
		return "ServiceMonitorInfo [enableFlagc=" + enableFlag + ",cpuUse="
				+ cpuUse + ", cpuFree=" + cpuFree + ", memUse=" + memUse
				+ ", memFree=" + memFree + ", oneload=" + oneload + ", ioi="
				+ ioi + ", ioo=" + ioo + ", currentTime=" + currentTime + "]";
	}

}
