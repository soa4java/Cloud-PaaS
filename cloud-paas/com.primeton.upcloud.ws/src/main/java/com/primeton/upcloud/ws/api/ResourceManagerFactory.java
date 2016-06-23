/**
 * 
 */
package com.primeton.upcloud.ws.api;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ResourceManagerFactory {

	private ResourceManagerFactory() {
		super();
	}
	
	/**
	 * 
	 * @return
	 */
	public static ResourceManager getManager() {
		ServiceLoader<ResourceManager> loader = ServiceLoader.load(ResourceManager.class);
		Iterator<ResourceManager> iterator = loader.iterator();
		while (iterator.hasNext()) {
			ResourceManager manager = iterator.next();
			if (manager != null) {
				return manager;
			}
		}
		throw new RuntimeException(ResourceManager.class.getName() + " implements class not found.");
	}
	
}
