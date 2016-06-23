/**
 * 
 */
package com.primeton.paas.app.cache;

import java.io.Serializable;
import java.util.Date;

import net.rubyeye.xmemcached.MemcachedClient;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.app.api.cache.ICacheService;

/**
 * 
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 *
 */
public class MemCachedServiceImpl implements ICacheService {
	
	private static ILogger logger = LoggerFactory.getLogger(MemCachedServiceImpl.class);
	
	private static final int EXPIRE_TIME = 360; // seconds
	
	private MemcachedClient memcachedClient;

	/**
	 * @param memcachedClient
	 */
	public MemCachedServiceImpl(MemcachedClient memcachedClient) {
		super();
		this.memcachedClient = memcachedClient;
		if (memcachedClient == null || memcachedClient.isShutdown()) {
			throw new IllegalArgumentException("memcachedClient is null or has shutdown.");
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#contains(java.lang.String)
	 */
	public boolean contains(String key) {
		return get(key) != null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#get(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <V extends Serializable> V get(String key) {
		if (key == null)
			return null;
		try {
			return (V)memcachedClient.get(key);
		} catch (Throwable t) {
			logger.error(t);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#put(java.lang.String, java.io.Serializable)
	 */
	public boolean put(String key, Serializable value) {
		return put(key, value, EXPIRE_TIME);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#put(java.lang.String, java.io.Serializable, int)
	 */
	public boolean put(String key, Serializable value, int expireTime) {
		if (key == null) {
			return false;
		}
		expireTime = expireTime > 0 ? expireTime : EXPIRE_TIME;
		try {
			return memcachedClient.set(key, expireTime, value);
		} catch (Throwable t) {
			logger.error(t);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#put(java.lang.String, java.io.Serializable, java.util.Date)
	 */
	public boolean put(String key, Serializable value, Date expireTime) {
		int time = EXPIRE_TIME;
		if (expireTime != null) {
			time = (int)((expireTime.getTime() - System.currentTimeMillis())/1000);
		}
		return put(key, value, time);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#remove(java.lang.String)
	 */
	public boolean remove(String key) {
		if (key == null)
			return false;
		try {
			return memcachedClient.delete(key);
		} catch (Throwable t) {
			logger.error(t);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#clear()
	 */
	public boolean clear() {
		try {
			memcachedClient.flushAll();
			return true;
		} catch (Throwable t) {
			logger.error(t);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#isShutdown()
	 */
	public boolean isShutdown() {
		return memcachedClient.isShutdown();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#shutdown()
	 */
	public void shutdown() {
		try {
			if (!memcachedClient.isShutdown()) {
				memcachedClient.shutdown();
			}
		} catch (Throwable t) {
			logger.error(t);
		}
	}

}