/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.primeton.paas.manage.api.monitor.MetaData;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface HostMonitorDao {

	boolean add(Map<String, Object> data);
	
	boolean add(MetaData metadata);
	
	List<MetaData> get(String ip, long begin, long end);
	
	void delete(long begin, long end);
	
	<T extends Collection<Map<String, Object>>> boolean addBatch(T datas);
	
	MetaData getLatestData(String ip);
	
}
