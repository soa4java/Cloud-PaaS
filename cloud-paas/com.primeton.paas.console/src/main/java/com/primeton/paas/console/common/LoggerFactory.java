/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.console.common;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.spi.LoggerFactoryPlugin;
import org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider;

import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class LoggerFactory {

	private static LoggerFactoryPlugin plugin = new LoggerFactoryPlugin();

	static {
		try {
			plugin.setLoggerProvider(new Log4jLoggerProvider());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Default. <br>
	 */
	private LoggerFactory() {
		super();
	}
	
	/**
	 * 
	 * @param file
	 */
	public static void init(String file) {
		if (StringUtil.isEmpty(file)) {
			return;
		}
		refresh();
		try {
			plugin.setLoggerProvider(new Log4jLoggerProvider(file));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static ILogger getLogger(Class<?> clazz) {
		return plugin.getLogger(clazz);
	}
	
	/**
	 * refresh
	 */
	public static void refresh() {
		plugin.refresh();
	}
	
	/**
	 * destroy
	 */
	public static void destroy() {
		plugin.destroy();
	}
	
}
