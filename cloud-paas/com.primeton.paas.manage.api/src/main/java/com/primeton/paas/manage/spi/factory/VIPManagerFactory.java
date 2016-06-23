/**
 * 
 */
package com.primeton.paas.manage.spi.factory;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.spi.resource.IVIPManager;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VIPManagerFactory {

	private static IVIPManager manager = null;
	
	private VIPManagerFactory() {}
	
	public static IVIPManager getManager() {
		synchronized (VIPManagerFactory.class) {
			if( manager == null) {
				ServiceExtensionLoader<IVIPManager> loader = ServiceExtensionLoader.load(IVIPManager.class);
				Iterator<IVIPManager> iterator = loader.iterator();
				while (iterator.hasNext()) {
					return manager = iterator.next();
				}
			}
		}
		return manager;
	}
	
}
