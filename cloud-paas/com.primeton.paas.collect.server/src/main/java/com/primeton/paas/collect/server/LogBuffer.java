/**
 * 
 */
package com.primeton.paas.collect.server;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.collect.common.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class LogBuffer implements Constants {
	
	private static ILogger logger = LoggerFactory.getLogger(LogBuffer.class);
	
	// org.apache.log4j.Logger
	// For write application's user and system logs
	private static Logger log = null;
	
	private static BlockingQueue<String> cache = new LinkedBlockingQueue<String>();
	
	private static WriteThread writeThread = new WriteThread();
	private static boolean isStarted = false;
	
	/**
	 * 
	 */
	private static void init() {
		ServerContext context = ServerContext.getContext();
		String log4j = context.getServerHome() + File.separator + "conf" + File.separator + "log4j-app.properties"; //$NON-NLS-1$
		logger.info("Init log4j environment from file [" + log4j + "], for write application's user and system logs.");
		PropertyConfigurator.configure(log4j);
		
		log = Logger.getLogger(LogBuffer.class);
	}
	
	/**
	 * 
	 */
	public static synchronized void start() {
		if (isStarted) {
			return;
		}
		
		init();
		
		writeThread.setDaemon(true);
		writeThread.setName(WriteThread.class.getSimpleName());
		writeThread.start();
		isStarted = true;
		
		logger.info("LogBuffer start success.");
	}
	
	/**
	 */
	public static void stop() {
		stop(30000L);
	}
	
	/**
	 * 
	 * @param timeout
	 */
	public static synchronized void stop(long timeout) {
		long begin = System.currentTimeMillis();
		long end = begin;
		while (true) {
			end = System.currentTimeMillis();
			if (end - begin > timeout) {
				break;
			}
			if (cache.isEmpty()) {
				break;
			}
			logger.info("LogBuffer size [" + cache.size() + "]. Waiting for consumers deal.");
			ThreadUtil.sleep(50L);
		}
		writeThread.close();
		logger.info("LogBuffer stop success.");
	}
	
	/**
	 * 
	 * @param log
	 */
	public static void cache(String log) {
		if (StringUtil.isNotEmpty(log)) {
			try {
				cache.put(log);
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static Logger getLogger() {
		if (log == null) {
			init();
		}
		return log;
	}

	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class WriteThread extends Thread {
		
		private static ILogger logger = LoggerFactory.getLogger(WriteThread.class);
		
		private boolean flag = true;
		private boolean isRunning = false;

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			isRunning = true;
			logger.info(WriteThread.class.getSimpleName() + " startup success.");
			
			while (flag) {
				//	logger.info("LogBuffer size [" + cache.size() + "].");
				if (cache.isEmpty()) {
					ThreadUtil.sleep(100L);
					continue;
				}
				logger.info("LogBuffer size [" + cache.size() + "].");
				
				StringBuffer message = new StringBuffer();
				/*
				for (int i=0; i<100; i++) {
					if(cache.isEmpty()) {
						break;
					}
					String content = null;
					try {
						content = cache.take();
					} catch (Throwable e) {
						logger.error(e);
					}
					
					if (content != null) {
						message.append(content).append("\n");
					}
					if(logger.isWarnEnabled()){
						logger.warn("times :"+i);
					}
				}
			
				 Call log4j Appender
				String content = message.toString();
				*/
				String content = null;
				try {
					content = cache.take();
				} catch (Throwable e) {
					logger.error(e);
				}
				message.append(content).append("\n");
				content = message.toString();
				
				if (content.length() > 0) {
					getLogger().info(content);
				}
			}
			isRunning = false;
		}
		
		/**
		 * 
		 */
		public void close() {
			this.flag = false;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isRunning() {
			return isRunning;
		}
		
	}

}
