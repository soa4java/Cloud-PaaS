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
public class StoragePoolFactory {

	private static IStoragePoolStartup startup = null;
	
	private StoragePoolFactory() {
		super();
	}
	
	/**
	 * 
	 * @return
	 */
	public static IStoragePoolStartup getStartup() {
		synchronized (StoragePoolFactory.class) {
			if (startup == null) {
				ServiceExtensionLoader<IStoragePoolStartup> loader = ServiceExtensionLoader.load(IStoragePoolStartup.class);
				Iterator<IStoragePoolStartup> iterator = loader.iterator();
				while (iterator.hasNext()) {
					return startup = iterator.next();
				}
			}
		}
		return startup;
	}
	
}
