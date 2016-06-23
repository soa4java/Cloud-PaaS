package com.primeton.paas.console.common.stretch;


public class StrategyConfigInfo {

	private String appName; //应用名称
	
	private String strategyName;//伸缩策略名称
	
//	private char isGlobal; //是否全局
	
	private StretchItem incStrategy;
	
	private StretchItem decStrategy;
	
	public StrategyConfigInfo(){
		super();
		incStrategy = new StretchItem();
		decStrategy = new StretchItem();
	}
	
	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	
	public StretchItem getDecStrategy() {
		return decStrategy;
	}

	public void setDecStrategy(StretchItem decStrategy) {
		this.decStrategy = decStrategy;
	}

	public StretchItem getIncStrategy() {
		return incStrategy;
	}

	public void setIncStrategy(StretchItem incStrategy) {
		this.incStrategy = incStrategy;
	}

//	public char getIsGlobal() {
//		return isGlobal;
//	}
//	public void setIsGlobal(char isGlobal) {
//		this.isGlobal = isGlobal;
//	}
	
	public static class StretchItem {
		private String enableFlag;
		private String strategyType;		// 策略：伸|缩
		private long continuedTime;	// 持续时间
		private long ignoreTime;	// 伸缩后忽略时间
		private int stretchSize;	// 伸缩幅度
		
		private String cpuThreshold; //cpu 
		private String memThreshold; //memory
//		private String netThreshold; //network
//		private String ioThreshold; //io 
		private String lbThreshold; //load balance
	
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
		public String getCpuThreshold() {
			return cpuThreshold;
		}
		public void setCpuThreshold(String cpuThreshold) {
			this.cpuThreshold = cpuThreshold;
		}
	
		public String getEnableFlag() {
			return enableFlag;
		}
		public void setEnableFlag(String enableFlag) {
			this.enableFlag = enableFlag;
		}
		public String getMemThreshold() {
			return memThreshold;
		}
		public void setMemThreshold(String memThreshold) {
			this.memThreshold = memThreshold;
		}
//		public String getIoThreshold() {
//			return ioThreshold;
//		}
//		public void setIoThreshold(String ioThreshold) {
//			this.ioThreshold = ioThreshold;
//		}
//		public String getNetThreshold() {
//			return netThreshold;
//		}
//		public void setNetThreshold(String netThreshold) {
//			this.netThreshold = netThreshold;
//		}
		public String getLbThreshold() {
			return lbThreshold;
		}
		public void setLbThreshold(String lbThreshold) {
			this.lbThreshold = lbThreshold;
		}
		public String getStrategyType() {
			return strategyType;
		}
		public void setStrategyType(String strategy) {
			this.strategyType = strategy;
		}
		public int getStretchSize() {
			return stretchSize;
		}
		public void setStretchSize(int stretchSize) {
			this.stretchSize = stretchSize;
		}
	}
}
