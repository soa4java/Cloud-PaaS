/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import java.util.HashMap;
import java.util.Map;

import com.primeton.paas.cesium.agent.MonitorMessage;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.monitor.EventListener;
import com.primeton.paas.manage.api.service.HaProxyService;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.MemcachedService;
import com.primeton.paas.manage.api.service.MySQLService;
import com.primeton.paas.manage.api.service.NginxService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class ServiceMonitorListener implements EventListener, Constants {
	
	public static final String UNIQKEY = "serviceMonitorEvent";
	
	private IServiceQuery query;
	private IClusterManager clusterManager;
	
	/**
	 * 
	 */
	public ServiceMonitorListener() {
		super();
		query = ServiceManagerFactory.getServiceQuery();
		clusterManager = ClusterManagerFactory.getManager(IClusterManager.DEFAULT_TYPE);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.monitor.EventListener#handle(java.util.Map)
	 */
	public Map<String, Object> handle(Map<String, Object> event) {
		if (event == null) {
			return null;
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(UNIQKEY, UNIQKEY);
		
		String ip = (String)event.get(MonitorMessage.IP);
		
		if (StringUtil.isNotEmpty(ip)) {
			IService[] instances = query.getByIp(ip);
			if (instances != null && instances.length > 0) {
				for (IService instance : instances) {
					if(instance==null||IService.MODE_LOGIC.equals(instance.getMode())){
						continue;
					}
					// Jetty||HaProxy||Memcached||MySQL||UPJAS ||Nginx
					String type = instance.getType();
					if (JettyService.TYPE.equals(type)
							|| HaProxyService.TYPE.equals(type)
							|| MemcachedService.TYPE.equals(type)
							|| MySQLService.TYPE.equals(type)
							|| NginxService.TYPE.equals(type)) {
						String instanceId = instance.getId();
						String clusterId = clusterManager.getClusterId(instanceId);
							Map<String,Object> memEvent = new HashMap<String, Object>();
							memEvent.putAll(event);
							//	memEvent.put(INSTANCE_ID, instanceId);
							memEvent.put(CLUSTER_ID, clusterId);
							memEvent.put(EVENT_NAME, EVENT_SERVICEMONITOR);
							result.put(instanceId, memEvent);
					}
				}
			}
		}
		return result;
	}
	
}
