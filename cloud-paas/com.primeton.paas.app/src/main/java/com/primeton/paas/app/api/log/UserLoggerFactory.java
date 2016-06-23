package com.primeton.paas.app.api.log;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.spi.ILoggerProvider;
import org.gocom.cloud.common.logger.spi.LoggerFactoryPlugin;

/**
 * <pre>
 * ILogger log = UserLoggerFactory.getLogger(ClassA.class);
 * if (log.isErrorEnabled()) {
 *     log.error("....error!");
 * }
 * </pre>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class UserLoggerFactory {

	private static LoggerFactoryPlugin logFactoryPlugin = new LoggerFactoryPlugin();
	
	static {
		try {
			String log4jProviderClass = "org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider"; //$NON-NLS-1$
			@SuppressWarnings("rawtypes")
			Class clazzlog4j = UserLoggerFactory.class.getClassLoader().loadClass(log4jProviderClass);
			setLoggerProvider((ILoggerProvider)clazzlog4j.newInstance());
		} catch (Throwable ignore) {			
		}
	}

	/**
	 * 
	 * @param provider
	 */
	public static void setLoggerProvider(ILoggerProvider provider) {
		synchronized (UserLoggerFactory.class) {
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
