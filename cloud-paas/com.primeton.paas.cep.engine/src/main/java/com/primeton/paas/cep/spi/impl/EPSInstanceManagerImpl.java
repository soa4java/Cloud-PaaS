/**
 * 
 */
package com.primeton.paas.cep.spi.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.primeton.paas.cep.model.EPSInstance;
import com.primeton.paas.cep.model.EventListener;
import com.primeton.paas.cep.model.EventType;
import com.primeton.paas.cep.spi.EPSException;
import com.primeton.paas.cep.spi.EPSInstanceManager;
import com.primeton.paas.cep.spi.EventTypeException;
import com.primeton.paas.cep.spi.EventTypeManager;
import com.primeton.paas.cep.spi.EventTypeManagerFactory;
import com.primeton.paas.cep.util.EventListenerUtil;
import com.primeton.paas.cep.util.PathUtil;
import com.primeton.paas.cep.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class EPSInstanceManagerImpl implements EPSInstanceManager {

	private EventTypeManager eventTypeManager;
	// id[EPSInstnceID] - name[UUID]
	private Map<String, String> statementNames = new ConcurrentHashMap<String, String>();
	
	private static ILogger logger = LoggerFactory.getLogger(EPSInstanceManagerImpl.class);
	
	/**
	 * 
	 */
	public EPSInstanceManagerImpl() {
		super();
		eventTypeManager = EventTypeManagerFactory.getManager();
	}

	/**
	 * 
	 * @param eventTypeManager
	 */
	public EPSInstanceManagerImpl(EventTypeManager eventTypeManager) {
		super();
		this.eventTypeManager = eventTypeManager;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#update(com.primeton.paas.cep.model.EPSInstance)
	 */
	public void update(EPSInstance instance) throws EPSException {
		if (instance == null || StringUtil.isEmpty(instance.getId())) {
			logger.warn("Parameter ${epsInstance} is null or field ${id} is blank.");
			return;
		}
		/*
		EPSInstance epsInstance = get(instance.getId());
		if (epsInstance != null) {
			epsInstance.setStatus(instance.getStatus());
			long time = instance.getStartTime();
			time = time > 0 ? time : System.currentTimeMillis();
			epsInstance.setStartTime(time);
			try {
				PathUtil.setEPS(epsInstance);
			} catch (Throwable t) {
				throw new EPSException(t);
			}
			logger.info("Update " + instance);
		}*/
		PathUtil.setEPS(instance);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#start(java.lang.String)
	 */
	public void start(String id) throws EPSException {
		EPSInstance instance = get(id);
		if (instance == null) {
			throw new EPSException("EPSInstance {" + id + "} not found.");
		}
		
		if (EPSInstance.DISABLE.equals(instance.getEnable())) {
			throw new EPSException("EPSInstance {" + id + "} has been disabled, not allow to start.");
		}
		
		/*
		if (EPSInstance.STATUS_RUNNING.equals(instance.getStatus())) {
			// throw new EPSException("EPSInstance {" + id + "} has already started.");
			stop(id);
		}
		*/
		if (isRunning(id)) {
			logger.warn("EPSInstance {" + id + "} has already started, then execute stop action first.");
			stop(id);
		}
		/*
		EventType eventType = eventTypeManager.get(instance.getEventName());
		if (eventType == null) {
			eventType = EventTypeUtil.load(instance.getEventName());
			try {
				eventTypeManager.register(eventType);
			} catch (EventTypeException e) {
				throw new EPSException(e);
			}
		}
		*/
		try {
			eventTypeManager.registerAllEvent();
		} catch (EventTypeException e) {
			throw new EPSException(e);
		}
		
		// eps
		if (StringUtil.isEmpty(instance.getEps())) {
			
			throw new EPSException("EPSInstance {" + id + "} eps statement is null or blank.");
		}
		
		// Find listener
		List<String> listeners = instance.listListeners();
		List<EventListener> eventListeners = new ArrayList<EventListener>();
		for (String listenerId : listeners) {
			EventListener listener = EventListenerUtil.load(listenerId);
			if (listener != null) {
				eventListeners.add(listener);
			}
		}
		if (eventListeners.isEmpty()) {
			throw new EPSException("EPSInstance {" + id + "} listeners not found.");
		}
		
		// Register
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(eventTypeManager.getConfiguration());
		EPStatement statement = epService.getEPAdministrator().createEPL(instance.getEps());
		String name = statement.getName();	// UUID
		
		for (EventListener listener : eventListeners) {
			statement.addListener(listener);
			logger.info("Register listener [" + listener + "] on EPS statement [" + instance.getEps() + "] success.");
		}
		
		// Update status
		/*
		instance.setStatus(EPSInstance.STATUS_RUNNING);
		instance.setStartTime(System.currentTimeMillis());
		update(instance);
		*/
		
		// buffer
		statementNames.put(id, name);
		logger.info("Add instance [" + id + "] EPStatement name [" + name + "] relationship to buffer(key-value).");
		logger.info("Start " + instance + " success.");
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#stop(java.lang.String)
	 */
	public void stop(String id) throws EPSException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		String name = statementNames.remove(id);
		if (StringUtil.isEmpty(name)) {
			return;
		}
		// Unregister
		try {
			EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(eventTypeManager.getConfiguration());
			EPStatement statement = epService.getEPAdministrator().getStatement(name);
			if (statement != null) {
				statement.removeAllListeners();
				statement.destroy();
				logger.info("Stop EPSInstance [" + id + "] success.");
			}
			
			/*
			EPSInstance instance = get(id);
			if (instance != null) {
				instance.setStatus(EPSInstance.STATUS_STOPED);
				//instance.setEnable(EPSInstance.DISABLE);
				update(instance);
			}*/
		} catch (Throwable t) {
			throw new EPSException(t);
		}	

	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#restart(java.lang.String)
	 */
	public void restart(String id) throws EPSException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		if (isRunning(id)) {
			stop(id);
		}
		start(id);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#isRunning(java.lang.String)
	 */
	public boolean isRunning(String id) {
		return StringUtil.isEmpty(id) ? false : statementNames.containsKey(id);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#get(java.lang.String)
	 */
	public EPSInstance get(String id) {
		return PathUtil.getEPS(id);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#getAll()
	 */
	public List<EPSInstance> getAll() {
		return PathUtil.getEPSs();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#sendEvent(java.util.Map, java.lang.String)
	 */
	public void sendEvent(Map<String, Object> event, String eventName)
			throws EPSException {
		if (event == null || event.isEmpty() || StringUtil.isEmpty(eventName)) {
			return;
		}
		try {
			EventType eventType = eventTypeManager.get(eventName);
			if (eventType == null) {
				//eventTypeManager.register(EventTypeUtil.load(eventName));
				throw new EPSException("Unkonwn event type,ignore it!");
			}
			
			EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(eventTypeManager.getConfiguration());
			EPRuntime epRuntime = epService.getEPRuntime();
			epRuntime.sendEvent(event, eventName);
			
			logger.info("Throw event {eventName:" + eventName + ", event:" + event + "} to engine success.");
		} catch (Throwable t) {
			throw new EPSException(t);
		}
	}

}
