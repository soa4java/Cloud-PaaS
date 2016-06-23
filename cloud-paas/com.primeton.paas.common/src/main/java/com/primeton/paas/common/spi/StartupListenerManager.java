/**
 * 
 */
package com.primeton.paas.common.spi;

import java.util.ArrayList;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class StartupListenerManager {
	
	private static ILogger log = LoggerFactory.getLogger(StartupListenerManager.class);
	
	private static ArrayList<IStartupListener> list = new ArrayList<IStartupListener>();
	
	private static Object lock = new Object();
	
	static {
		try {
			ServiceExtensionLoader<IStartupListener> loader = ServiceExtensionLoader.load(IStartupListener.class);
			for (IStartupListener listener : loader.getExtensions()) {
				register(listener);
			}
			
		} catch (Throwable t) {
			log.error(t.getMessage());
		}
	}
	
	/**
	 * 
	 * @param listener
	 */
	public static void register(IStartupListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener is null!");
		}
		synchronized(lock) {
			list.add(listener);
		}
	}
	
	/**
	 * 
	 * @param index
	 * @param listener
	 */
	public static void register(int index, IStartupListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener is null!");
		}
		if (index < 0 || index >= list.size()) {
			throw new IllegalArgumentException("index'" + index + "' is illegal!");
		}
		synchronized(lock) {
			list.add(index, listener);
		}
	}
	
	/**
	 * 
	 * @param listener
	 */
	public static void unregister(IStartupListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener is null!");
		}
		synchronized(lock) {
			list.remove(listener);
		}
	}
	
	/**
	 * 
	 * @param index
	 */
	public static void unregister(int index) {
		if (index < 0 || index >= list.size()) {
			throw new IllegalArgumentException("index'" + index + "' is illegal!");
		}
		synchronized(lock) {
			list.remove(index);
		}
	}
	
	/**
	 * 
	 */
	public static void startListener() {
		synchronized(lock) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == null) {
					continue;
				}
				try {
					list.get(i).start();
				} catch (Throwable t) {
					log.warn(t);
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public static void stopListener() {
		synchronized(lock) {
			for (int i = list.size() - 1; i >= 0; i--) {
				if (list.get(i) == null) {
					continue;
				}
				try {
					list.get(i).stop();
				} catch (Throwable t) {
					log.warn(t);
				}
			}
		}
	}
	
}
