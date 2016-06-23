/**
 * 
 */
package com.primeton.paas.manage.api.app;

/**
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class StrategyItem {
	
	public static final String TYPE_CPU = "CPU";
	public static final String TYPE_MEMORY = "MEMORY";
	public static final String TYPE_LB = "LB";

	public static final String TYPE_IO = "IO";
	public static final String TYPE_NETWORK = "NETWORK";

	private String strategyType;
	private String strategyName;
	
	private String itemType;
	private String threshold;
	
	/**
	 * 
	 */
	public StrategyItem() {
		super();
	}
	
	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public String getStrategyType() {
		return strategyType;
	}

	public void setStrategyType(String strategyType) {
		this.strategyType = strategyType;
	}

	/**
	 * @return the type
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @param type the type to set
	 */
	public void setItemType(String type) {
		this.itemType = type;
	}

	/**
	 * @return the threshold
	 */
	public String getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer(StrategyItem.class.getSimpleName())
			.append("{type:").append(itemType)
			.append(", threshold:").append(threshold)
			.append("}").toString();
	}
	
}
