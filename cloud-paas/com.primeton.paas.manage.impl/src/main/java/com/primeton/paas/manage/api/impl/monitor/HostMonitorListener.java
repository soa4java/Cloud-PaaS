/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import java.util.Map;

import com.primeton.paas.cesium.agent.MonitorMessage;
import com.primeton.paas.manage.api.monitor.EventListener;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class HostMonitorListener implements EventListener {

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.monitor.EventListener#handle(java.util.Map)
	 */
	public Map<String, Object> handle(Map<String, Object> event) {
		if (event == null || event.isEmpty()) {
			return null;
		}
		String ip = (String)event.get(MonitorMessage.IP);
		if (StringUtil.isNotEmpty(ip)) {
			// Throw it to buffer
			HostDataBuffer.put(event);
		}
		return null;
	}

}
