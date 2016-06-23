/**
 * 
 */
package com.primeton.paas.manage.api.monitor;

import java.util.List;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IMonitorDataManager {

	/**
	 * 
	 * @param ip
	 * @param begin
	 * @param end
	 * @return
	 */
	List<MetaData> getByIp(String ip, long begin, long end);
	
	/**
	 * 
	 * @param appName
	 * @param begin
	 * @param end
	 * @return
	 */
	List<AppMetaData> getByApp(String appName, long begin, long end);
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	MetaData getLatestDataByIp(String ip);
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	ServiceMetaData getByClusterId(String clusterId);
}
