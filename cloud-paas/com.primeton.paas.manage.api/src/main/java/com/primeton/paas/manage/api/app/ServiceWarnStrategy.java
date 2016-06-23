/**
 * 
 */
package com.primeton.paas.manage.api.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class ServiceWarnStrategy {
	
	public static final String MAIL_ALARM = "MAIL";
	public static final String SMS_ALARM = "SMS";
	public static final String SMS_MAIL_ALARM = "SMS,MAIL";
	
	public static final String GLOBAL_STRATEGY_ID = "global_strategy";
	
	private String strategyId = GLOBAL_STRATEGY_ID;
	private String clusterId;
	
	private boolean isEnable = false;
	
	private long continuedTime;	
	private long ignoreTime;	
	private String alarmType;	
	
	private String alarmAddress;
	
	private List<ServiceWarnStrategyItem> serviceWarnStrategyItems = new ArrayList<ServiceWarnStrategyItem>();	// ������

	public ServiceWarnStrategy() {
		super();
	}

	public String getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
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
	
	public String getAlarmAddress() {
		return alarmAddress;
	}

	public void setAlarmAddress(String alarmAddress) {
		this.alarmAddress = alarmAddress;
	}

	/**
	 * @return the strategyItems
	 */
	public List<ServiceWarnStrategyItem> getServiceWarnStrategyItems() {
		return serviceWarnStrategyItems;
	}

	/**
	 * @param strategyItems the strategyItems to set
	 */
	public void setStrategyItems(List<ServiceWarnStrategyItem> strategyItems) {
		if (strategyItems == null || strategyItems.isEmpty()) {
			this.serviceWarnStrategyItems.clear();
		} else {
			this.serviceWarnStrategyItems = strategyItems;
		}
	}
	
	/**
	 * 
	 * @param strategyItem
	 */
	public void addStrategyItem(ServiceWarnStrategyItem strategyItem) {
		if (strategyItem != null
				&& !this.serviceWarnStrategyItems.contains(strategyItem)) {
			this.serviceWarnStrategyItems.add(strategyItem);
		}
	}
	
	/**
	 * 
	 * @param strategyItems
	 */
	public <T extends Collection<ServiceWarnStrategyItem>> void addStrategyItems(T serviceWarnStrategyItems) {
		if (serviceWarnStrategyItems == null || serviceWarnStrategyItems.isEmpty()) {
			return;
		}
		for (ServiceWarnStrategyItem strategyItem : serviceWarnStrategyItems) {
			addStrategyItem(strategyItem);
		}
	}
	
	/**
	 * 
	 * @param strategyItem
	 */
	public void removeStrategyItem(ServiceWarnStrategyItem strategyItem) {
		if (strategyItem != null || this.serviceWarnStrategyItems.contains(strategyItem)) {
			this.serviceWarnStrategyItems.remove(strategyItem);
		}
	}

	public String toString() {
		return "ServiceWarnStrategy [strategyId=" + strategyId + ", clusterId=" + clusterId + ", isEnable="
				+ isEnable + ", continuedTime=" + continuedTime
				+ ", ignoreTime=" + ignoreTime + ", alarmType=" + alarmType
				+ ", alarmAddress=" + alarmAddress
				+ ", serviceWarnStrategyItems=" + serviceWarnStrategyItems
				+ "]";
	}

}
