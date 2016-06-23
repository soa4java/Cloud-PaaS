/**
 * 
 */
package com.primeton.paas.manage.spi.factory;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.spi.resource.IVmPoolStartup;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VmPoolManagerFactory {

	private static IVmPoolStartup manager = null;
	
	public VmPoolManagerFactory() {}
	
	public static IVmPoolStartup getManager() {
		synchronized (VmManagerFactory.class) {
			if(manager == null) {
				ServiceExtensionLoader<IVmPoolStartup> loader = ServiceExtensionLoader.load(IVmPoolStartup.class);
				Iterator<IVmPoolStartup> iterator = loader.iterator();
				while(iterator.hasNext()) {
					return manager = iterator.next();
				}
			}
		}
		return manager;
	}
	
}
