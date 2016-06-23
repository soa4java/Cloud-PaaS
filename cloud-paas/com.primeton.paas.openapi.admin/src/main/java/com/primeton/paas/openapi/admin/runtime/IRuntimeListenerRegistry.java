/**
 * 
 */
package com.primeton.paas.openapi.admin.runtime;

import java.util.List;

import com.primeton.paas.openapi.admin.runtime.impl.RuntimeListenerRegistryImpl;

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

	public void registerRuntimeListener(IRuntimeListener listener,int order);
	
	/**
	 * 
	 * @return
	 */
	public List<IRuntimeListener> getRuntimeListeners();
	
}