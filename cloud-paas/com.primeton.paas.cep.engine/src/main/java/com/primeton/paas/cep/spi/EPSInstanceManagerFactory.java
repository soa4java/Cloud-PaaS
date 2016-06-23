/**
 * 
 */
package com.primeton.paas.cep.spi;

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
	
	public synchronized static EPSInstanceManager getManager() {
		if (null == manager) {
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
		return manager;
	}

}
