/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import java.util.Map;

import com.primeton.paas.cesium.agent.MonitorMessage;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.monitor.EventListener;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.TomcatService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class AppMonitorListener implements EventListener, Constants {
	
	private IServiceQuery query;
	
	public AppMonitorListener() {
		super();
		query = ServiceManagerFactory.getServiceQuery();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.monitor.EventListener#handle(java.util.Map)
	 */
	public Map<String, Object> handle(Map<String, Object> event) {
		if (event == null) {
			return null;
		}
		
		String ip = (String)event.get(MonitorMessage.IP);
		if (StringUtil.isNotEmpty(ip)) {
			IService[] instances = query.getByIp(ip);
			if (instances != null && instances.length > 0) {
				for (IService instance : instances) {
					if(instance==null){
						return null;
					}
					if (JettyService.TYPE.equals(instance.getType())) {
						String appName = ((JettyService)instance).getAppName();
						event.put(APP_NAME, appName);
						event.put(EVENT_NAME, EVENT_APPMONITOR);
						return event;
					}
					if (TomcatService.TYPE.equals(instance.getType())) {
						String appName = ((TomcatService) instance).getAppName();
						event.put(APP_NAME, appName);
						event.put(EVENT_NAME, EVENT_APPMONITOR);
						return event;
					}
				}
			}
		}
		return null;
	}

}
