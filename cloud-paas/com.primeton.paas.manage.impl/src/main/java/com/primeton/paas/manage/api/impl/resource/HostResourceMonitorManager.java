/**
 * 
 */
package com.primeton.paas.manage.api.impl.resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class HostResourceMonitorManager {

	private static Map<String, HostResourceMonitor> threads = new ConcurrentHashMap<String, HostResourceMonitor>();
	
	private static ILogger logger = ManageLoggerFactory.getLogger(HostResourceMonitorManager.class);
	
	/**
	 * 
	 * @param monitor
	 */
	public static synchronized void register(HostResourceMonitor monitor) {
		if (monitor == null || StringUtil.isEmpty(monitor.getId())) {
			return;
		}
		if (threads.containsKey(monitor.getId())) {
			HostResourceMonitor obj = threads.remove(monitor.getId());
			if (obj != null && obj.isRunning()) {
				obj.close();
			}
		}
		threads.put(monitor.getId(), monitor);
		if (!monitor.isRunning()) {
			Thread thread = new Thread(monitor);
			thread.setName("HostResourceMonitor[" + monitor.getId() + "]");
			thread.setDaemon(true);
			thread.start();
		}
		if (logger.isInfoEnabled()) {
			logger.info("Register " + monitor + " success.");
		}
	}
	
	/**
	 * 
	 * @param id
	 */
	public static synchronized void unregister(String id) {
		if (StringUtil.isNotEmpty(id) && threads.containsKey(id)) {
			HostResourceMonitor monitor = threads.remove(id);
			if (monitor != null && monitor.isRunning()) {
				monitor.close();
				/*
				long begin = System.currentTimeMillis();
				long end = 0;
				long timeout = 60000L;
				
				while (true) {
					end = System.currentTimeMillis();
					if (end - begin > timeout) {
						break;
					}
					if (!monitor.isRunning()) {
						break;
					}
				}
				*/
			}
			if (logger.isInfoEnabled()) {
				logger.info("Unregister " + monitor + " success.");
			}
		}
	}
	
	/**
	 * 
	 */
	public static synchronized void unregister() {
		for (HostResourceMonitor monitor : threads.values()) {
			if (monitor.isRunning()) {
				monitor.close();
			}
		}
	}
	
}
