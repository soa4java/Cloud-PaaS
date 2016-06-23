package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * 
 * @author liyanping(liyp@primeton.com)
 */
public class OpenAPIService extends AbstractService {

	private static final long serialVersionUID = -1327167993699989507L;

	public static final String TYPE = "OpenAPI";

	private static final String MIN_MEMORY_SIZE = "minMemory";
	private static final String MAX_MEMORY_SIZE = "maxMemory";
	private static final String MIN_PERM_SIZE = "minPermSize";
	private static final String MAX_PERM_SIZE = "maxPermSize";
	
	
	/**
	 * Default constructor <br>
	 */
	public OpenAPIService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	/**
	 * @return the minMemorySize
	 */
	public int getMinMemorySize() {
		return getValue(MIN_MEMORY_SIZE, -1);
	}


	/**
	 * @param minMemorySize the minMemorySize to set
	 */
	public void setMinMemorySize(int minMemorySize) {
		setValue(MIN_MEMORY_SIZE, minMemorySize);
	}


	/**
	 * @return the maxMemorySize
	 */
	public int getMaxMemorySize() {
		return getValue(MAX_MEMORY_SIZE, -1);
	}


	/**
	 * @param maxMemorySize the maxMemorySize to set
	 */
	public void setMaxMemorySize(int maxMemorySize) {
		setValue(MAX_MEMORY_SIZE, maxMemorySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMinPermMemorySize() {
		return getValue(MIN_PERM_SIZE, -1);
	}
	
	/**
	 * 
	 * @param minPermMemorySize
	 */
	public void setMinPermMemorySize(int minPermMemorySize) {
		setValue(MIN_PERM_SIZE, minPermMemorySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxPermMemorySize() {
		return getValue(MAX_PERM_SIZE, -1);
	}
	
	/**
	 * 
	 * @param maxPermMemorySize
	 */
	public void setMaxPermMemorySize(int maxPermMemorySize) {
		setValue(MAX_PERM_SIZE, maxPermMemorySize);
	}
	
}
