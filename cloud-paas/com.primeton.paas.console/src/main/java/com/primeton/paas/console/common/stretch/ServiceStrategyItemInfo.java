package com.primeton.paas.console.common.stretch;

/**
 * 服务报警策略项
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class ServiceStrategyItemInfo {
	
	public static final String TYPE_CPU = "CPU"; //
	public static final String TYPE_MEMORY = "MEMORY";//内存
	public static final String TYPE_LB = "LB"; //负载

	public static final String TYPE_IO = "IO"; //TODO
	public static final String TYPE_NETWORK = "NETWORK"; //TODO
	
	private String enableFlag;
	private long continuedTime;	// 持续时间
	private long ignoreTime;	// 伸缩后忽略时间
	private String alarmType;   //报警方式
	private String alarmAddress;//报警地址  邮箱或者手机号
	
	private String cpuThreshold; //cpu 
	private String memThreshold; //memory
//	private String netThreshold; //network
//	private String ioThreshold; //io 
	private String lbThreshold; //load balance
	public ServiceStrategyItemInfo() {
		super();
	}
	public String getEnableFlag() {
		return enableFlag;
	}
	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}
	public long getContinuedTime() {
		return continuedTime;
	}
	public void setContinuedTime(long continuedTime) {
		this.continuedTime = continuedTime;
	}
	public long getIgnoreTime() {
		return ignoreTime;
	}
	public void setIgnoreTime(long ignoreTime) {
		this.ignoreTime = ignoreTime;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public String getCpuThreshold() {
		return cpuThreshold;
	}
	public void setCpuThreshold(String cpuThreshold) {
		this.cpuThreshold = cpuThreshold;
	}
	public String getMemThreshold() {
		return memThreshold;
	}
	public void setMemThreshold(String memThreshold) {
		this.memThreshold = memThreshold;
	}
	public String getLbThreshold() {
		return lbThreshold;
	}
	public void setLbThreshold(String lbThreshold) {
		this.lbThreshold = lbThreshold;
	}
	
	public String getAlarmAddress() {
		return alarmAddress;
	}
	public void setAlarmAddress(String alarmAddress) {
		this.alarmAddress = alarmAddress;
	}
	@Override
	public String toString() {
		return "ServiceStrategyItemInfo [enableFlag=" + enableFlag
				+ ", continuedTime=" + continuedTime + ", ignoreTime="
				+ ignoreTime + ", alarmType=" + alarmType + ", alarmAddress="
				+ alarmAddress + ", cpuThreshold=" + cpuThreshold
				+ ", memThreshold=" + memThreshold + ", lbThreshold="
				+ lbThreshold + "]";
	}
	
}
