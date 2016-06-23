/**
 * 
 */
package com.primeton.paas.manage.spi.resource;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VmPoolFactory {

	private static IVmPoolStartup startup = null;
	
	private VmPoolFactory() {
		super();
	}
	
	/**
	 * 
	 * @return
	 */
	public static IVmPoolStartup getStartup() {
		synchronized (VmPoolFactory.class) {
			if (startup == null) {
				ServiceExtensionLoader<IVmPoolStartup> loader = ServiceExtensionLoader.load(IVmPoolStartup.class);
				Iterator<IVmPoolStartup> iterator = loader.iterator();
				while (iterator.hasNext()) {
					return startup = iterator.next();
				}
			}
		}
		return startup;
	}
	
}
