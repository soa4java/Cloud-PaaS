/**
 * 
 */
package com.primeton.paas.manage.api.monitor;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class MonitorDataManagerFactory {
	
	private MonitorDataManagerFactory() {
		super();
	}
	
	private static IMonitorDataManager manager;
	
	/**
	 * 
	 * @return
	 */
	public static synchronized IMonitorDataManager getManager() {
		if (null == manager) {
			ServiceExtensionLoader<IMonitorDataManager> loader = ServiceExtensionLoader
					.load(IMonitorDataManager.class);
			Iterator<IMonitorDataManager> iterator = loader.iterator();
			while (iterator.hasNext()) {
				return manager = iterator.next();
			}
		}
		return manager;
	}

}
