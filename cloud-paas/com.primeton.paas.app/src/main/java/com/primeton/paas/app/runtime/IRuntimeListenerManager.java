/**
 * 
 */
package com.primeton.paas.app.runtime;

import com.primeton.paas.app.runtime.impl.RuntimeListenerManagerImpl;

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
