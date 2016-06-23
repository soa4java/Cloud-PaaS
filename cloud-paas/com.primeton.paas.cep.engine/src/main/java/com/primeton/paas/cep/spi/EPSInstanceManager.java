/**
 * 
 */
package com.primeton.paas.cep.spi;

import java.util.List;
import java.util.Map;

import com.primeton.paas.cep.model.EPSInstance;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface EPSInstanceManager {
	
//	void add(EPSInstance instance) throws EPSException;
	
	/**
	 * 
	 * @param instance
	 * @throws EPSException
	 */
	void update(EPSInstance instance) throws EPSException;
	
	/**
	 * 
	 * @param id
	 * @throws EPSException
	 */
	void start(String id) throws EPSException;
	
	/**
	 * 
	 * @param id
	 * @throws EPSException
	 */
	void stop(String id) throws EPSException;
	
	/**
	 * 
	 * @param id
	 * @throws EPSException
	 */
	void restart(String id) throws EPSException;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	boolean isRunning(String id);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	EPSInstance get(String id);
	
	/**
	 * 
	 * @return
	 */
	List<EPSInstance> getAll();
	
	/**
	 * 
	 * @param event
	 * @param eventName
	 * @throws EPSException
	 */
	void sendEvent(Map<String, Object> event, String eventName) throws EPSException;

}
