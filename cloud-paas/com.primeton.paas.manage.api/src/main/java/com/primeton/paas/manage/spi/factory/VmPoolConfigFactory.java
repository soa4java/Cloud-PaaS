/**
 * 
 */
package com.primeton.paas.manage.spi.factory;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.spi.resource.IVmPoolConfig;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VmPoolConfigFactory {

	private VmPoolConfigFactory() {
		super();
	}
	
	private static IVmPoolConfig manager = null;
	
	public static IVmPoolConfig getManager() {
		synchronized (VmPoolConfigFactory.class) {
			if (manager == null) {
				ServiceExtensionLoader<IVmPoolConfig> loader = ServiceExtensionLoader.load(IVmPoolConfig.class);
				Iterator<IVmPoolConfig> iterator = loader.iterator();
				while (iterator.hasNext()) {
					return manager = iterator.next();
				}
			}
		}
		return manager;
	}
	
}
