/**
 * 
 */
package com.primeton.paas.app.spi.log;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.spi.ILoggerProvider;
import org.gocom.cloud.common.logger.spi.LoggerFactoryPlugin;

/**
 * <pre>
 * ILogger log = SystemLoggerFactory.getLogger(ClassA.class);
 * if (log.isErrorEnabled()) {
 *     log.error("....error!");
 * }
 * </pre>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SystemLoggerFactory {

	private static LoggerFactoryPlugin logFactoryPlugin = new LoggerFactoryPlugin();
	
	static {
		try {
			String log4jProviderClass = "org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider"; //$NON-NLS-1$
			@SuppressWarnings("rawtypes")
			Class clazzlog4j = SystemLoggerFactory.class.getClassLoader().loadClass(log4jProviderClass);
			setLoggerProvider((ILoggerProvider)clazzlog4j.newInstance());
		} catch (Throwable ignore) {			
		}
	}

	/**
	 * 
	 * @param provider
	 */
	public static void setLoggerProvider(ILoggerProvider provider) {
		synchronized (SystemLoggerFactory.class) {
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
