/**
 * 
 */
package com.primeton.paas.manage.spi.factory;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.spi.resource.IVmManager;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VmManagerFactory {
	
	private VmManagerFactory() {
		super();
	}
	
	private static IVmManager manager = null;

	public static IVmManager getManager() {
		synchronized (VmManagerFactory.class) {
			if(manager == null) {
				ServiceExtensionLoader<IVmManager> loader = ServiceExtensionLoader.load(IVmManager.class);
				Iterator<IVmManager> iterator = loader.iterator();
				while(iterator.hasNext()) {
					return manager = iterator.next();
				}
			}
		}
		return manager;
	}
	
}
