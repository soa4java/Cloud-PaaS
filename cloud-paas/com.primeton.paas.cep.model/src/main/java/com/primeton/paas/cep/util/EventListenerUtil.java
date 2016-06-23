/**
 * 
 */
package com.primeton.paas.cep.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import com.primeton.paas.cep.model.EventListener;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EventListenerUtil {
	
	/**
	 * Load all implements <br>
	 * 
	 * @return EventListener List
	 */
	public static List<EventListener> load() {
		List<EventListener> listeners = new ArrayList<EventListener>();
		ServiceLoader<EventListener> loader = ServiceLoader.load(EventListener.class);
		if (loader != null) {
			Iterator<EventListener> iterator = loader.iterator();
			while (iterator.hasNext()) {
				EventListener EventListener = iterator.next();
				listeners.add(EventListener);
			}
		}
		return listeners;
	}
	
	/**
	 * Load implement by id <br>
	 * 
	 * @param id
	 * @return EventListener
	 */
	public static EventListener load(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		List<EventListener> listeners = load();
		for (EventListener listener : listeners) {
			if (listener != null && id.equals(listener.getId())) {
				return listener;
			}
		}
		return null;
	}
	
}
