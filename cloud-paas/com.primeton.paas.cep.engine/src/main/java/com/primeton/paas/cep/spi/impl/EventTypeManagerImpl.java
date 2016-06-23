/**
 * 
 */
package com.primeton.paas.cep.spi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.espertech.esper.client.Configuration;
import com.primeton.paas.cep.model.EventType;
import com.primeton.paas.cep.spi.EventTypeException;
import com.primeton.paas.cep.spi.EventTypeManager;
import com.primeton.paas.cep.util.EventTypeUtil;
import com.primeton.paas.cep.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EventTypeManagerImpl implements EventTypeManager {
	
	private Map<String, EventType> eventTypes = new ConcurrentHashMap<String, EventType>();
	private Configuration configuration = new Configuration();
	
	private static ILogger logger = LoggerFactory.getLogger(EventTypeManagerImpl.class);

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EventTypeManager#register(com.primeton.paas.cep.model.EventType)
	 */
	public void register(EventType eventType) throws EventTypeException {
		register(eventType, false);
	}

	public void registerAllEvent() throws EventTypeException {
		List<EventType> types = EventTypeUtil.load();
		for(EventType type:types){
			configuration.addEventType(type.getName(), type.getType());
			eventTypes.put(type.getName(), type);
			logger.info("Register EventType " + type + " success.");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EventTypeManager#register(com.primeton.paas.cep.model.EventType, boolean)
	 */
	public synchronized void register(EventType eventType, boolean override)
			throws EventTypeException {
		if (eventType == null || StringUtil.isEmpty(eventType.getName())
				|| eventType.getType() == null || eventType.getType().isEmpty()) {
			throw new EventTypeException("Parameter ${eventType} is null or field ${eventName} is blank or field ${type} is empty.");
		}
		if (eventTypes.containsKey(eventType.getName())) {
			if (override) {
				try {
					unregister(eventType.getName(), true);
				} catch (EventTypeException e) {
					logger.error(e);
				}
			} else {
				throw new EventTypeException("EventType {" + eventType.getName() + "} has already registered.");
			}
		}
		eventTypes.put(eventType.getName(), eventType);
		configuration.addEventType(eventType.getName(), eventType.getType()); // Esper
		logger.info("Register EventType " + eventType + " success.");
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EventTypeManager#register(java.util.Collection)
	 */
	public <T extends Collection<EventType>> void register(T eventTypes)
			throws EventTypeException {
		register(eventTypes, false);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EventTypeManager#register(java.util.Collection, boolean)
	 */
	public <T extends Collection<EventType>> void register(T eventTypes,
			boolean override) throws EventTypeException {
		if (eventTypes != null) {
			for (EventType type : eventTypes) {
				register(type, override);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EventTypeManager#unregister(java.lang.String)
	 */
	public void unregister(String eventName) throws EventTypeException {
		unregister(eventName, false);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EventTypeManager#unregister(java.lang.String, boolean)
	 */
	public synchronized void unregister(String eventName, boolean force)
			throws EventTypeException {
		if (StringUtil.isEmpty(eventName)) {
			return;
		}
		if (eventTypes.containsKey(eventName)) {
			eventTypes.remove(eventName);
			boolean isSuccess = configuration.removeEventType(eventName, force); // Esper
			if (!isSuccess) {
				throw new EventTypeException("Unregister EventType {" + eventName + "} exception.");
			}
			logger.info("Unregister EventType {" + eventName + "} success.");
		}
		
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EventTypeManager#unregister()
	 */
	public void unregister() throws EventTypeException {
		unregister(false);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EventTypeManager#unregister(boolean)
	 */
	public void unregister(boolean force) throws EventTypeException {
		List<EventTypeException> exceptions = new ArrayList<EventTypeException>();
		for (String eventName : eventTypes.keySet()) {
			try {
				unregister(eventName, force);
			} catch (EventTypeException e) {
				logger.error(e);
				exceptions.add(e);
			}
		}
		if (!exceptions.isEmpty()) {
			throw exceptions.get(0);
		}
		logger.info((force ? "Force" : "") + " unregister all EventType success.");
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EventTypeManager#get(java.lang.String)
	 */
	public EventType get(String eventName) {
		return eventTypes.get(eventName);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EventTypeManager#getAll()
	 */
	public List<EventType> getAll() {
		List<EventType> types = new ArrayList<EventType>();
		for (EventType type : eventTypes.values()) {
			types.add(type);
		}
		return types;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EventTypeManager#getConfiguration()
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

}
