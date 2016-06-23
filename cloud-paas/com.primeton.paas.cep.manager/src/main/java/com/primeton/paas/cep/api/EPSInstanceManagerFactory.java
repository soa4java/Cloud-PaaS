/**
 * 
 */
package com.primeton.paas.cep.api;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EPSInstanceManagerFactory {
	
	private static EPSInstanceManager manager = null;
	
	private EPSInstanceManagerFactory() {
		super();
	}
	
	public static EPSInstanceManager getManager() {
		synchronized (EPSInstanceManagerFactory.class) {
			if (manager == null) {
				ServiceLoader<EPSInstanceManager> loader = ServiceLoader.load(EPSInstanceManager.class);
				if (loader != null) {
					Iterator<EPSInstanceManager> iterator = loader.iterator();
					while (iterator.hasNext()) {
						manager = iterator.next();
						if (manager != null) {
							break;
						}
					}
				}
			}
		}
		return manager;
	}

}
