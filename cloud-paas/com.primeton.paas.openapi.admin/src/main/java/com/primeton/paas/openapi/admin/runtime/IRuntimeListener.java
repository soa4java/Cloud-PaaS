/**
 * 
 */
package com.primeton.paas.openapi.admin.runtime;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IRuntimeListener {

	/**
	 * 
	 * @param envent
	 */
	public void start(RuntimeEvent envent);
	
	/**
	 * 
	 * @param envent
	 */
	public void stop(RuntimeEvent envent);
	
}
