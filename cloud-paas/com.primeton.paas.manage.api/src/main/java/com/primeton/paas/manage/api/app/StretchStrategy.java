/**
 * 
 */
package com.primeton.paas.manage.api.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class StretchStrategy {
	
	public static final String STRATEGY_INCREASE = "INCREASE";
	public static final String STRATEGY_DECREASE = "DECREASE";
	
	public static final String GLOBAL_STRATEGY = "global_strategy";
	
	private String appName;
	
	private String strategyName = GLOBAL_STRATEGY;
	private boolean isEnable = false;
	
	private String strategyType;
	private int stretchSize;	
	private long continuedTime;	
	private long ignoreTime;
	private List<StrategyItem> strategyItems = new ArrayList<StrategyItem>();
	
	public StretchStrategy() {
		super();
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the strategyName
	 */
	public String getStrategyName() {
		return strategyName;
	}

	/**
	 * @param strategyName the strategyName to set
	 */
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	/**
	 * @return
	 */
	public boolean getIsEnable() {
		return isEnable;
	}

	/**
	 * @param this isEnable
	 */
	public void setIsEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	/**
	 * @return the strategy
	 */
	public String getStrategyType() {
		return strategyType;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategyType(String strategy) {
		this.strategyType = strategy;
	}

	/**
	 * @return the stretchSize
	 */
	public int getStretchSize() {
		return stretchSize;
	}

	/**
	 * @param stretchSize the stretchSize to set
	 */
	public void setStretchSize(int stretchSize) {
		this.stretchSize = stretchSize;
	}

	/**
	 * @return the continuedTime
	 */
	public long getContinuedTime() {
		return continuedTime;
	}

	/**
	 * @param continuedTime the continuedTime to set
	 */
	public void setContinuedTime(long continuedTime) {
		this.continuedTime = continuedTime;
	}

	/**
	 * @return the ignoreTime
	 */
	public long getIgnoreTime() {
		return ignoreTime;
	}

	/**
	 * @param ignoreTime the ignoreTime to set
	 */
	public void setIgnoreTime(long ignoreTime) {
		this.ignoreTime = ignoreTime;
	}

	/**
	 * @return the strategyItems
	 */
	public List<StrategyItem> getStrategyItems() {
		return strategyItems;
	}

	/**
	 * @param strategyItems the strategyItems to set
	 */
	public void setStrategyItems(List<StrategyItem> strategyItems) {
		if (strategyItems == null || strategyItems.isEmpty()) {
			this.strategyItems.clear();
		} else {
			this.strategyItems = strategyItems;
		}
	}
	
	/**
	 * 
	 * @param strategyItem
	 */
	public void addStrategyItem(StrategyItem strategyItem) {
		if (strategyItem != null && !this.strategyItems.contains(strategyItem)) {
			this.strategyItems.add(strategyItem);
		}
	}
	
	/**
	 * 
	 * @param strategyItems
	 */
	public <T extends Collection<StrategyItem>> void addStrategyItems(T strategyItems) {
		if (strategyItems == null || strategyItems.isEmpty()) {
			return;
		}
		for (StrategyItem strategyItem : strategyItems) {
			addStrategyItem(strategyItem);
		}
	}
	
	/**
	 * 
	 * @param strategyItem
	 */
	public void removeStrategyItem(StrategyItem strategyItem) {
		if (strategyItem != null || this.strategyItems.contains(strategyItem)) {
			this.strategyItems.remove(strategyItem);
		}
	}
	
}
