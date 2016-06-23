/**
 * 
 */
package com.primeton.paas.manage.spi.factory;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.spi.resource.IStoragePoolConfig;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class StoragePoolConfigFactory {

	private static IStoragePoolConfig manager;
	
	private StoragePoolConfigFactory() {}
	
	/**
	 * 
	 * @return ISharedStoragePoolConfigManager
	 */
	public static IStoragePoolConfig getManager() {
		synchronized (StoragePoolConfigFactory.class) {
			if(manager == null) {
				ServiceExtensionLoader<IStoragePoolConfig> loader = ServiceExtensionLoader.load(IStoragePoolConfig.class);
				Iterator<IStoragePoolConfig> iterator = loader.iterator();
				while (iterator.hasNext()) {
					return manager = iterator.next();
				}
			}
		}
		return manager;
	}
	
}
