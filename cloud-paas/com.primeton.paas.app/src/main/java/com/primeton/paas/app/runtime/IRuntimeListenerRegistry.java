/**
 * 
 */
package com.primeton.paas.app.runtime;

import java.util.List;

import com.primeton.paas.app.runtime.impl.RuntimeListenerRegistryImpl;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IRuntimeListenerRegistry {
	
	public static IRuntimeListenerRegistry INSTANCE = new RuntimeListenerRegistryImpl();

	/**
	 * 
	 * @param listener
	 */
	public void registerRuntimeListener(IRuntimeListener listener);

	/**
	 * 
	 * @param listener
	 * @param order
	 */
	public void registerRuntimeListener(IRuntimeListener listener, int order);

	/**
	 * 
	 * @return
	 */
	public List<IRuntimeListener> getRuntimeListeners();
	
}