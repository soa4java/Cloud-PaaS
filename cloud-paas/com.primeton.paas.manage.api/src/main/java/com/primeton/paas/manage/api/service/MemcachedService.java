/**
 * 
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class MemcachedService extends AbstractService {

	private static final long serialVersionUID = 797987169838150996L;

	public static final String TYPE = "Memcached";

	/**
	 * Default. <br>
	 */
	public MemcachedService() {
		super();
		setType(TYPE);
		setMode(MODE_PHYSICAL);
	}
	
	public static final String MEMORY_SIZE = "memorySize";
	public static final String MAX_CONNECTION_SIZE = "maxConnectionSize";
	
	/**
	 * 
	 * @return
	 */
	public int getMemorySize() {
		return getValue(MEMORY_SIZE, -1);
	}
	
	/**
	 * 
	 * @param sliceSize
	 */
	public void setMemorySize(int sliceSize) {
		setValue(MEMORY_SIZE, sliceSize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxConnectionSize() {
		return getValue(MAX_CONNECTION_SIZE, 0);
	}
	
	/**
	 * 
	 * @param maxConnectionSize
	 */
	public void setMaxConnectionSize(int maxConnectionSize) {
		setValue(MAX_CONNECTION_SIZE, String.valueOf(maxConnectionSize));
	}
	
}
