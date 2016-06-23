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
 * @author liming(mailto:li-ming@primeton.com)
 *
 */
public class StorageResourceMonitorManager {

	private static Map<String, StorageResourceMonitor> threads = new ConcurrentHashMap<String, StorageResourceMonitor>();
	
	private static ILogger logger = ManageLoggerFactory.getLogger(StorageResourceMonitorManager.class);
	
	/**
	 * 
	 * @param monitor
	 */
	public static synchronized void register(StorageResourceMonitor monitor) {
		if (StringUtil.isEmpty(monitor.getId())) {
			return;
		}
		if (threads.containsKey(monitor.getId())) {
			StorageResourceMonitor obj = threads.remove(monitor.getId());
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
		logger.info("Register " + monitor + " success.");
	}
	
	/**
	 * 
	 * @param id
	 */
	public static synchronized void unregister(String id) {
		if (StringUtil.isNotEmpty(id) && threads.containsKey(id)) {
			StorageResourceMonitor monitor = threads.remove(id);
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
	 * unregister
	 */
	public static synchronized void unregister() {
		for (StorageResourceMonitor monitor : threads.values()) {
			if (monitor.isRunning()) {
				monitor.close();
			}
		}
	}
	
}
