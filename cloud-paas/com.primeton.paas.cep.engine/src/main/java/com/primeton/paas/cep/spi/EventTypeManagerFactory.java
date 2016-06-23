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
public class EventTypeManagerFactory {
	
	private static EventTypeManager manager = null;

	private EventTypeManagerFactory() {
		super();
	}
	
	public static EventTypeManager getManager() {
		synchronized (EventTypeManagerFactory.class) {
			if (manager == null) {
				ServiceLoader<EventTypeManager> loader = ServiceLoader.load(EventTypeManager.class);
				if (loader != null) {
					Iterator<EventTypeManager> iterator = loader.iterator();
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
