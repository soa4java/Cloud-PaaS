/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.Collection;
import java.util.Map;

import com.primeton.paas.manage.api.monitor.ServiceMetaData;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface ServiceMonitorDao {
	
	boolean add(Map<String, Object> metaData);
	
	boolean add(ServiceMetaData metaData);
	
	ServiceMetaData get(String clusterId);
	
	int delete(long begin, long end);
	
	<T extends Collection<Map<String, Object>>> boolean addBatch(T datas);

}
