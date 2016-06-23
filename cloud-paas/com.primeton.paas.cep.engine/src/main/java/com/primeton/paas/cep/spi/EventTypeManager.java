/**
 * 
 */
package com.primeton.paas.cep.spi;

import java.util.Collection;
import java.util.List;

import com.espertech.esper.client.Configuration;
import com.primeton.paas.cep.model.EventType;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface EventTypeManager {
	
	/**
	 * 
	 * @param eventType
	 * @throws EventTypeException
	 */
	void register(EventType eventType) throws EventTypeException;
	
	/**
	 * 
	 * @throws EventTypeException
	 */
	void registerAllEvent() throws EventTypeException;
	
	/**
	 * 
	 * @param eventType
	 * @param override
	 * @throws EventTypeException
	 */
	void register(EventType eventType, boolean override) throws EventTypeException;
	
	/**
	 * 
	 * @param eventTypes
	 * @throws EventTypeException
	 */
	<T extends Collection<EventType>> void register(T eventTypes) throws EventTypeException;
	
	/**
	 * 
	 * @param eventTypes
	 * @param override
	 * @throws EventTypeException
	 */
	<T extends Collection<EventType>> void register(T eventTypes, boolean override) throws EventTypeException;
	
	/**
	 * 
	 * @param eventName
	 * @throws EventTypeException
	 */
	void unregister(String eventName) throws EventTypeException;
	
	/**
	 * 
	 * @throws EventTypeException
	 */
	void unregister() throws EventTypeException;
	
	/**
	 * 
	 * @param eventName
	 * @param force
	 * @throws EventTypeException
	 */
	void unregister(String eventName, boolean force) throws EventTypeException;
	
	/**
	 * 
	 * @param force
	 * @throws EventTypeException
	 */
	void unregister(boolean force) throws EventTypeException;
	
	/**
	 * 
	 * @param eventName
	 * @return
	 */
	EventType get(String eventName);
	
	/**
	 * 
	 * @return
	 */
	List<EventType> getAll();
	
	/**
	 * 
	 * @return
	 */
	Configuration getConfiguration();

}
