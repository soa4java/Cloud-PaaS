/**
 * 
 */
package com.primeton.paas.manage.api.app;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class ServiceWarnStrategyItem {
	
	public static final String TYPE_CPU = "CPU";
	public static final String TYPE_MEMORY = "MEMORY";
	public static final String TYPE_LB = "LB";

	public static final String TYPE_IO = "IO";
	public static final String TYPE_NETWORK = "NETWORK";

	private String strategyId;
	private String itemType;
	private String threshold;
	
	public String getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public ServiceWarnStrategyItem() {
		super();
	}

}
