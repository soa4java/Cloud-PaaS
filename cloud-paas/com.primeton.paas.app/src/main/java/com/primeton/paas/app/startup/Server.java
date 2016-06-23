/**
 * 
 */
package com.primeton.paas.app.startup;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;
import org.gocom.cloud.cesium.zkclient.api.ZkConfig;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider;

import com.primeton.paas.app.AppConstants;
import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.api.log.UserLoggerFactory;
import com.primeton.paas.app.config.ConfigModelManager;
import com.primeton.paas.app.runtime.RuntimeManager;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class Server implements IServer {

	private static Server instance = null;

	private static ILogger logger = null;

	private Server() {}

	/**
	 * Single Instance <br>
	 * 
	 * @return Server Instance
	 */
	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.startup.IServer#start()
	 */
	public void start() {
		// WEB-INF/config/log4j-user.xml
		// WEB-INF/config/log4j-system.xml
		initLogger(true, true, true);
		
		prepareServerDir();
		
		initLogger(false, true, true);
		
		if (AppConstants.RUN_MODE_CLOUD == ServerContext.getInstance().getRunMode()) {
			initZkClientFactory();
		}
		
		RuntimeManager.start();

		ServerContext.getInstance().setStarted(true);
		
		logger.info("PaaS App [" + ServerContext.getInstance().getAppName() + "] Instance [" + ServerContext.getInstance().getInstId() + "] started.");
	}

	/**
	 * ֹͣServer <br>
	 */
	public void stop() {
		if (AppConstants.RUN_MODE_CLOUD == ServerContext.getInstance().getRunMode()) {
			destroyZkClientFactory();
		}

		RuntimeManager.stop();

		ServerContext.getInstance().setStarted(false);
		logger.info("PaaS App [" + ServerContext.getInstance().getAppName() + "] Instance [" + ServerContext.getInstance().getInstId() + "] stopped.");

		clearLogger();
	}

	/**
	 * 
	 */
	private void prepareServerDir() {
		File configDir = new File(ServerContext.getInstance().getConfigDirPath());
		File defaultConfigDir = new File(ServerContext.getInstance().getDefaultConfigDirPath());

		if (!configDir.equals(defaultConfigDir)) {
			if (!configDir.exists() || configDir.list().length == 0) {
				try {
					FileUtils.copyDirectoryToDirectory(defaultConfigDir, configDir.getParentFile());
				} catch (IOException e) {
					if (logger.isErrorEnabled())
						logger.error("Failed to copy config files dir " + defaultConfigDir.getAbsolutePath() + " to external config dir " + configDir.getParentFile(), e);
				}
			} else {
				for (File file : defaultConfigDir.listFiles()) {
					if (!new File(configDir, file.getName()).exists()) {
						try {
							FileUtils.copyFileToDirectory(file, configDir);
						} catch (IOException e) {
							if (logger.isErrorEnabled())
								logger.error("Failed to copy config file " + file.getAbsolutePath() + " to dir " + configDir, e);
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	private void initZkClientFactory() {
		long start = System.currentTimeMillis();
		String zkConfigFilePath = ServerContext.getInstance().getConfigDirPath() + File.separator + AppConstants.CONFIG_FILE_NAME_ZKCLIENT;
		ZkClientFactory.init(new File(zkConfigFilePath));
		ZkConfig zkConfig = ZkClientFactory.getZkConfig();

		ConfigModelManager.setZkConfig(zkConfig);
		if (logger.isInfoEnabled())
			logger.info("Init ZkClientFactory from config file " + zkConfigFilePath);

		if (logger.isInfoEnabled())
			logger.info("Read local configuration");

		ConfigModelManager.reloadConfigFromZK();

		long end = System.currentTimeMillis();
		if (logger.isDebugEnabled())
			logger.debug("The ZkClientFactory initial end, in " + (end - start) + " ms.");
	}

	/**
	 * 
	 */
	private void destroyZkClientFactory() {
		ConfigModelManager.destroy();
		logger.info("Close configListenerManager and unregister all config listeners");

		ZkClientFactory.destroy();
		logger.info("Stop StartupService");
	}
	
	/**
	 * 
	 * @param initSystemLog
	 * @param initUserLog
	 */
	public void initLogger(boolean initSystemLog, boolean initUserLog) {
		initLogger(false, initSystemLog, initUserLog);
	}

	/**
	 * 
	 * @param isFirst
	 * @param initSystemLog
	 * @param initUserLog
	 */
	private void initLogger(boolean isFirst, boolean initSystemLog, boolean initUserLog) {
		String logDir = null;
		if (ServerContext.getInstance().hasExternalDir()) {
			logDir = new File(ServerContext.getInstance().getConfigDirPath()).getParentFile().getAbsolutePath() + File.separator + AppConstants.LOG_DIR_NAME;
		} else {
			String serverHome = ServerContext.getInstance().getServerHome();
			if (serverHome != null)
				logDir = serverHome + File.separator + AppConstants.LOG_DIR_NAME;
			else
				logDir = "." + File.separator + AppConstants.LOG_DIR_NAME;
		}
		
		//	SystemProperties.setProperty(AppConstants.ENV_LOG_DIR, logDir);
		System.setProperty(AppConstants.ENV_LOG_DIR, logDir);

		String logConfigDirPath = null;
		if (isFirst) {
			logConfigDirPath = ServerContext.getInstance().getWarRealPath() + File.separator + "WEB-INF" + File.separator + "config" + File.separator;
		} else {
			logConfigDirPath = ServerContext.getInstance().getConfigDirPath() + File.separator;
		}

		if (initSystemLog) {
			SystemLoggerFactory.setLoggerProvider(new Log4jLoggerProvider(logConfigDirPath + AppConstants.LOG_CONFIG_FILE_NAME_SYSTEM));
			SystemLoggerFactory.refresh();
			LoggerFactory.setLoggerProvider(new Log4jLoggerProvider(logConfigDirPath + AppConstants.LOG_CONFIG_FILE_NAME_SYSTEM));
			LoggerFactory.refresh();
		}
		if (initUserLog) {
			UserLoggerFactory.setLoggerProvider(new Log4jLoggerProvider(logConfigDirPath + AppConstants.LOG_CONFIG_FILE_NAME_USER));
			UserLoggerFactory.refresh();
		}
		
		logger = SystemLoggerFactory.getLogger(Server.class);
	}
	

	/**
	 * Destory <br>
	 */
	private void clearLogger() {
		SystemLoggerFactory.destroy();
		UserLoggerFactory.destroy();
	}
	
}
