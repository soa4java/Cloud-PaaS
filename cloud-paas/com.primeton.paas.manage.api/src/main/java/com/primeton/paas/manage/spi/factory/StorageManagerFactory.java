/**
 * 
 */
package com.primeton.paas.manage.spi.factory;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.spi.resource.IStorageManager;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class StorageManagerFactory {

	private static IStorageManager manager = null;
	
	private StorageManagerFactory() {}
	
	/**
	 * 
	 * @return
	 */
	public static IStorageManager getManager() {
		synchronized (StorageManagerFactory.class) {
			if(manager == null) {
				ServiceExtensionLoader<IStorageManager> loader = ServiceExtensionLoader.load(IStorageManager.class);
				Iterator<IStorageManager> iterator = loader.iterator();
				while(iterator.hasNext()) {
					return manager = iterator.next();
				}
			}
		}
		return manager;
	}
	
}
