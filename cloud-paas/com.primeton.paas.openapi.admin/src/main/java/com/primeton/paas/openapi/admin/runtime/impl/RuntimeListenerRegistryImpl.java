/**
 * 
 */
package com.primeton.paas.openapi.admin.runtime.impl;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.openapi.admin.runtime.IRuntimeListener;
import com.primeton.paas.openapi.admin.runtime.IRuntimeListenerRegistry;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RuntimeListenerRegistryImpl implements IRuntimeListenerRegistry {

	private List<Integer> priorityList = new ArrayList<Integer>();
	
	private List<IRuntimeListener> listeners = new ArrayList<IRuntimeListener>();
	
	public List<IRuntimeListener> getRuntimeListeners() {
		return listeners;
	}

	public void registerRuntimeListener(IRuntimeListener listener, int order) {
		if (!isExit(listener)) {
			boolean inserted = false;
			for (int i = 0; i < priorityList.size(); i++) {
				int sortData = priorityList.get(i);
				if (sortData>order){
					priorityList.add(i, order);
					listeners.add(i,listener);
					inserted = true;
					break;
				}
			}
			if (!inserted){
				priorityList.add(order);
				listeners.add(listener);
			}
		}
	}

	public void registerRuntimeListener(IRuntimeListener listener) {
		registerRuntimeListener(listener, 1000);
	}

	private boolean isExit(IRuntimeListener listener) {
		String name = listener.getClass().getCanonicalName();
		for (IRuntimeListener l : listeners) {
			if (l.getClass().getCanonicalName().equals(name)) {
				return true;
			}
		}
		return false;
	}

}
