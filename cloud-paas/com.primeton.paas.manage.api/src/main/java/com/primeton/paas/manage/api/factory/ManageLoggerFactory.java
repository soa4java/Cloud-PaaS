package com.primeton.paas.manage.api.factory;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.spi.ILoggerProvider;
import org.gocom.cloud.common.logger.spi.LoggerFactoryPlugin;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ManageLoggerFactory {

	private static LoggerFactoryPlugin logFactoryPlugin = new LoggerFactoryPlugin();
	
	static {
		try {
			String log4jProviderClass = "org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider";
			@SuppressWarnings("rawtypes")
			Class clazzlog4j = ManageLoggerFactory.class.getClassLoader().loadClass(log4jProviderClass);
			setLoggerProvider((ILoggerProvider)clazzlog4j.newInstance());
		} catch (Throwable ignore) {			
		}
	}

	/**
	 * 
	 * @param provider
	 */
	public static void setLoggerProvider(ILoggerProvider provider) {
		synchronized (ManageLoggerFactory.class) {
			logFactoryPlugin.setLoggerProvider(provider);
		}
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ILogger getLogger(Class clazz) {
		return logFactoryPlugin.getLogger(clazz);
	}

	/**
	 * 
	 * @param loggerName
	 * @return
	 */
	public static ILogger getLogger(String loggerName) {
		return logFactoryPlugin.getLogger(loggerName);
	}
	
	/**
	 * 
	 */
	public static void refresh() {
		logFactoryPlugin.refresh();
	}
	
	/**
	 * 
	 */
	public static void destroy() {
		logFactoryPlugin.destroy();
	}
	
}
