/**
 * 
 */
package com.primeton.paas.manage.spi.factory;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.spi.resource.IStoragePoolManager;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class StoragePoolManagerFactory {

	private static IStoragePoolManager manager;
	
	private StoragePoolManagerFactory() {}
	
	/**
	 * 
	 * @return ISharedStoragePoolManager
	 */
	public static IStoragePoolManager getManager() {
		synchronized (StoragePoolManagerFactory.class) {
			if (manager == null) {
				ServiceExtensionLoader<IStoragePoolManager> loader = ServiceExtensionLoader.load(IStoragePoolManager.class);
				Iterator<IStoragePoolManager> iterator = loader.iterator();
				while(iterator.hasNext()) {
					return manager = iterator.next();
				}
			}
		}
		return manager;
	}
}
