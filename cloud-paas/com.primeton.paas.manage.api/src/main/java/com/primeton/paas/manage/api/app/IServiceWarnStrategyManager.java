/**
 * 
 */
package com.primeton.paas.manage.api.app;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public interface IServiceWarnStrategyManager {
	
	/**
	 * @param clusterId
	 * @return
	 */
	ServiceWarnStrategy get(String clusterId);
	
	/**
	 * @param strategy
	 */
	void save(ServiceWarnStrategy strategy);
	
	/**
	 * @param clusterId
	 */
	void remove(String clusterId);
	
	/**
	 * @return
	 */
	ServiceWarnStrategy getGlobalStrategy();
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	boolean queryAppServiceMonitor(String appName);
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	boolean queryServiceMonitor(String clusterId);
	
}
