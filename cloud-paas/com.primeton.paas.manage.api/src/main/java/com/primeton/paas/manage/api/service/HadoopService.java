/**
 *
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HadoopService extends AbstractService {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1391364476972039809L;
	
	public static final String TYPE = "Hadoop";
	
	private static final String MIN_MEMORY_SIZE = "minMemory";
	
	private static final String MAX_MEMORY_SIZE = "maxMemory";
	
	private static final String MIN_PERM_SIZE = "minPermSize";
	
	private static final String MAX_PERM_SIZE = "maxPermSize";	

	/**
	 * Default. <br>
	 */
	public HadoopService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMinMemorySize() {
		return getValue(MIN_MEMORY_SIZE, 128);
	}
	
	/**
	 * 
	 * @param minMemorySize
	 */
	public void setMinMemorySize(int minMemorySize) {
		setValue(MIN_MEMORY_SIZE, minMemorySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxMemorySize() {
		return getValue(MAX_MEMORY_SIZE, 256);
	}
	
	/**
	 * 
	 * @param maxMemorySize
	 */
	public void setMaxMemorySize(int maxMemorySize) {
		setValue(MAX_MEMORY_SIZE, maxMemorySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMinPermSize() {
		return getValue(MIN_PERM_SIZE, 64);
	}
	
	/**
	 * 
	 * @param minPermSize
	 */
	public void setMinPermSize(int minPermSize) {
		setValue(MIN_PERM_SIZE, minPermSize);
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxPermSize() {
		return getValue(MAX_PERM_SIZE, 64);
	}
	
	/**
	 * 
	 * @param maxPermSize
	 */
	public void setMaxPermSize(int maxPermSize) {
		setValue(MAX_PERM_SIZE, maxPermSize);
	}

}
