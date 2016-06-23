/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.api.manager.IHostAssignManager;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class HostAssignManagerFactory {

	private static IHostAssignManager manager = null;
	
	private HostAssignManagerFactory() {}
	
	/**
	 * 
	 * @return
	 */
	public static IHostAssignManager getManager() {
		synchronized (HostAssignManagerFactory.class) {
			if (manager == null) {
				ServiceExtensionLoader<IHostAssignManager> loader = ServiceExtensionLoader.load(IHostAssignManager.class);
				Iterator<IHostAssignManager> iterator = loader.iterator();
				if (iterator != null) {
					while (iterator.hasNext()) {
						manager = iterator.next();
						if (manager != null) {
							break;
						}
					}
				}
			}
		}
		return manager;
	}
	
}
