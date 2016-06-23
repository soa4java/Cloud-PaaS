/**
 * 
 */
package com.primeton.paas.cep.api;

import java.util.List;

import com.primeton.paas.cep.api.EPSException;
import com.primeton.paas.cep.model.EPSInstance;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface EPSInstanceManager {
	
	/**
	 * 
	 * @throws EPSInitException
	 */
	void init() throws EPSInitException;
	
	/**
	 * 
	 * @param instance
	 * @return
	 * @throws EPSException
	 */
	EPSInstance register(EPSInstance instance) throws EPSException;
	
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
	void enable(String id) throws EPSException;
	
	/**
	 * 
	 * @param id
	 * @throws EPSException
	 */
	void disable(String id) throws EPSException;
	
	/**
	 * 
	 * @param id
	 * @throws EPSException
	 */
	void unregister(String id) throws EPSException;
	
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
	
}
