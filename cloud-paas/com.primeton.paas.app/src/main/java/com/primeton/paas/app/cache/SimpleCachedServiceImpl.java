/**
 * 
 */
package com.primeton.paas.app.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.primeton.paas.app.api.cache.ICacheService;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SimpleCachedServiceImpl implements ICacheService {
	
	private ConcurrentHashMap<String, Serializable> map = new ConcurrentHashMap<String, Serializable>();
	
	private Timer timer = new Timer(true);
	
	private TimerTask task = null;
	
	private boolean isShutdown = false;
	
	/**
	 * 
	 */
	public SimpleCachedServiceImpl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#contains(java.lang.String)
	 */
	public boolean contains(String key) {
		if (key == null) {
			return false;
		}
		return map.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#get(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <V extends Serializable> V get(String key) {
		if (key == null) {
			return null;
		}
		return (V)map.get(key);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#put(java.lang.String, java.io.Serializable)
	 */
	public boolean put(String key, Serializable value) {
		if (key == null) {
			throw new IllegalArgumentException("key is null!");
		}
		if (value == null) {
			throw new IllegalArgumentException("value is null!");
		}
		map.put(key, value);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#put(java.lang.String, java.io.Serializable, int)
	 */
	public boolean put(final String key, Serializable value, int expireTime) {
		if (key == null) {
			throw new IllegalArgumentException("key is null!");
		}
		if (value == null) {
			throw new IllegalArgumentException("value is null!");
		}

		if (task != null && map.containsKey(key)) {
			task.cancel();
			timer.purge();
		}
		task = new TimerTask() {
			/*
			 * (non-Javadoc)
			 * @see java.util.TimerTask#run()
			 */
			public void run() {
				if (map != null) {
					map.remove(key);
				}
			}			
		};	
		timer.schedule(task, expireTime*1000);

		map.put(key, value);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#put(java.lang.String, java.io.Serializable, java.util.Date)
	 */
	public boolean put(final String key, Serializable value, Date expireTime) {
		if (key == null) {
			throw new IllegalArgumentException("key is null!");
		}
		if (value == null) {
			throw new IllegalArgumentException("value is null!");
		}
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (map != null) {
					map.remove(key);
				}
			}			
		}, expireTime);
		map.put(key, value);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#remove(java.lang.String)
	 */
	public boolean remove(String key) {
		if (key == null) {
			return true;
		}
		map.remove(key);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#clear()
	 */
	public boolean clear() {
		map.clear();
		return true;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#isShutdown()
	 */
	public boolean isShutdown() {
		return isShutdown;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.api.cache.ICacheService#shutdown()
	 */
	public void shutdown() {
		if (isShutdown)
			return;
		if (timer != null) {
			timer.cancel();
		}
		map.clear();
		isShutdown = true;
	}

}