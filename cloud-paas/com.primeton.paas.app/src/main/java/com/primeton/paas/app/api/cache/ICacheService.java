/**
 * 
 */
package com.primeton.paas.app.api.cache;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface ICacheService {
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	boolean contains(String key);
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	<V extends Serializable> V get(String key);
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	boolean put(String key, Serializable value);
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	boolean put(String key, Serializable value, int expireTime);
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	boolean put(String key, Serializable value, Date expireTime);
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	boolean remove(String key);
	
	/**
	 * 
	 * @return
	 */
	boolean clear();
	
	/**
	 * 
	 * @return
	 */
	boolean isShutdown();
	
	/**
	 * 
	 */
	void shutdown();
	
}