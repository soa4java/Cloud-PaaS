/**
 * 
 */
package com.primeton.paas.common.impl.http;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ObjectRegister {
	
	//<key, Object>
	private ConcurrentHashMap<Object, Object> objectMap = new ConcurrentHashMap<Object, Object>();
	
	public ObjectRegister() {
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(Object key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null!");
		}
		return (T)objectMap.get(key);
	}
	
	public void register(Object key, Object obj) {
		if (key == null) {
			throw new IllegalArgumentException("key is null!");
		}
		if (obj == null) {
			throw new IllegalArgumentException("Object is null!");
		}
		objectMap.put(key, obj);
	}
	
	public void unregister(Object key) {
		if (key == null) {
			return;
		}
		objectMap.remove(key);
	}
	
	public Object[] list() {		
		return objectMap.keySet().toArray(new Object[0]);
	}

	public boolean exists(Object key) {
		if (key == null) {
			return false;
		}
		return objectMap.get(key) != null;
	}
	
	public void clear() {
		objectMap.clear();
	}
	
}
