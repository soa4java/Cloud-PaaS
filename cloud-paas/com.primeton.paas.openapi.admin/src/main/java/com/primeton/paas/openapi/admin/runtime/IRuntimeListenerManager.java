/**
 * 
 */
package com.primeton.paas.openapi.admin.runtime;

import com.primeton.paas.openapi.admin.runtime.impl.RuntimeListenerManagerImpl;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IRuntimeListenerManager {

	public static IRuntimeListenerManager INSTANCE = new RuntimeListenerManagerImpl();
	
	/**
	 * 
	 */
	public void startListener();
	
	/**
	 * 
	 */
	public void stopListener();

}
