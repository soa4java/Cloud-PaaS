/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.primeton.paas.manage.api.monitor.AppMetaData;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface AppMonitorDao {
	
	/**
	 * 
	 * @param metaData
	 * @return
	 */
	boolean add(Map<String, Object> metaData);
	
	/**
	 * 
	 * @param metaData
	 * @return
	 */
	boolean add(AppMetaData metaData);
	
	/**
	 * 
	 * @param appName
	 * @param begin
	 * @param end
	 * @return
	 */
	List<AppMetaData> get(String appName, long begin, long end);
	
	/**
	 * 
	 * @param begin
	 * @param end
	 */
	void delete(long begin, long end);
	
	/**
	 * 
	 * @param datas
	 * @return
	 */
	<T extends Collection<Map<String, Object>>> boolean addBatch(T datas);

}
