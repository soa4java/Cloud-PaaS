/**
 * 
 */
package com.primeton.paas.cep.spi;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.cep.engine.ServerContext;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-11-11
 *
 */
public class StartupListenerManager {
	
	private static boolean isStarted;
	private static boolean isLoaded;
	
	private static Map<String, StartupListener> listeners = new ConcurrentHashMap<String, StartupListener>();
	
	private static ILogger logger = LoggerFactory.getLogger(StartupListenerManager.class);
	
	private StartupListenerManager() {
		super();
	}
	
	/**
	 * 
	 */
	private synchronized static void load() {
		if (isLoaded) {
			logger.warn(StartupListener.class.getSimpleName() + " is already loaded.");
			return;
		}
		ServiceLoader<StartupListener> loader = ServiceLoader.load(StartupListener.class);
		if (loader != null) {
			Iterator<StartupListener> iterator = loader.iterator();
			while (iterator.hasNext()) {
				StartupListener listener = iterator.next();
				if (listener != null) {
					listeners.put(listener.getId(), listener);
				}
			}
		}
		isLoaded = true;
		logger.info("Load " + StartupListener.class.getSimpleName() + " implements success.");
	}
	
	/**
	 * 
	 */
	public synchronized static void start() {
		if (isStarted) {
			logger.warn(StartupListener.class.getSimpleName() + " is already started.");
			return;
		}
		load();
		
		long begin = System.currentTimeMillis();
		logger.info("Begin start " + StartupListener.class.getSimpleName() + ".");
		for (StartupListener listener : listeners.values()) {
			try {
				listener.start(ServerContext.getContext());
			} catch (Exception e) {
				logger.error(e);
			}
		}
		long end = System.currentTimeMillis();
		logger.info("End start " + StartupListener.class.getSimpleName() + ", time spent " + (end-begin)/1000L + " seconds.");
		isStarted = true;
	}
	
	/**
	 * 
	 */
	public synchronized static void stop() {
		if (!isStarted) {
			logger.warn(StartupListener.class.getSimpleName() + " is already stopped.");
			return;
		}
		
		long begin = System.currentTimeMillis();
		logger.info("Begin stop " + StartupListener.class.getSimpleName() + ".");
		for (StartupListener listener : listeners.values()) {
			try {
				listener.stop(ServerContext.getContext());
			} catch (Exception e) {
				logger.error(e);
			}
		}
		long end = System.currentTimeMillis();
		logger.info("End stop " + StartupListener.class.getSimpleName() + ", time spent " + (end-begin)/1000L + " seconds.");
		isStarted = false;
	}
	
	/**
	 * 
	 */
	public synchronized static void restart() {
		if (isStarted) {
			stop();
		}
		isLoaded = false;
		listeners.clear();
		start();
	}
	
	/**
	 * 
	 * @param id
	 */
	public static void start(String id) {
		if (listeners.containsKey(id)) {
			try {
				listeners.get(id).start(ServerContext.getContext());
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
	
	/**
	 * 
	 * @param id
	 */
	public static void stop(String id) {
		if (listeners.containsKey(id)) {
			try {
				listeners.get(id).stop(ServerContext.getContext());
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
	
	/**
	 * 
	 * @param id
	 */
	public static void restart(String id) {
		if (listeners.containsKey(id)) {
			try {
				listeners.get(id).restart(ServerContext.getContext());
				logger.info("Restart " + listeners.get(id) + " success.");
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

}
