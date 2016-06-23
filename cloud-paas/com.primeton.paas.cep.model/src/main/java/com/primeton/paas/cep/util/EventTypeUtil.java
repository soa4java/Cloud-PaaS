/**
 * 
 */
package com.primeton.paas.cep.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import com.primeton.paas.cep.model.EventType;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EventTypeUtil {
	
	/**
	 * Load all implements <br>
	 * 
	 * @return EventType List
	 */
	public static List<EventType> load() {
		List<EventType> eventTypes = new ArrayList<EventType>();
		ServiceLoader<EventType> loader = ServiceLoader.load(EventType.class);
		if (loader != null) {
			Iterator<EventType> iterator = loader.iterator();
			while (iterator.hasNext()) {
				EventType eventType = iterator.next();
				eventTypes.add(eventType);
			}
		}
		return eventTypes;
	}
	
	/**
	 * Load implement by eventName <br>
	 * 
	 * @param eventName
	 * @return EventType
	 */
	public static EventType load(String eventName) {
		if (StringUtil.isEmpty(eventName)) {
			return null;
		}
		List<EventType> eventTypes = load();
		for (EventType eventType : eventTypes) {
			if (eventType != null && eventName.equals(eventType.getName())) {
				return eventType;
			}
		}
		return null;
	}

}
