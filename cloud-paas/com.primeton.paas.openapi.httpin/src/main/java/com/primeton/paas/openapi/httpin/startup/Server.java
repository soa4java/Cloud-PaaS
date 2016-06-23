/*8
 * 
 */
package com.primeton.paas.openapi.httpin.startup;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider;

import com.primeton.paas.openapi.admin.AppConstants;
import com.primeton.paas.openapi.admin.ServerContext;
import com.primeton.paas.openapi.admin.runtime.RuntimeManager;

/**
 * 
 * @author tenghao(mailto:tenghao@primeton.com)
 *
 */
public class Server implements IServer {

	private static Server instance = new Server();

	private static ILogger logger = null;

	private Server() {}

	/**
	 * 
	 * @return Server Instance
	 */
	public static Server getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.openapi.httpin.startup.IServer#start()
	 */
	public void start() {
		initLogger(true);
		
		prepareServerDir();
		
		initLogger(false);
		
		initZkClientFactory();
		
		RuntimeManager.start();

		ServerContext.getInstance().setStarted(true);
		
		logger.info("UPaaS App [" + ServerContext.getInstance().getAppName() + "] Instance [" + ServerContext.getInstance().getInstId() + "] started.");
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.openapi.httpin.startup.IServer#stop()
	 */
	public void stop() {
		destroyZkClientFactory();
		
		RuntimeManager.stop();

		ServerContext.getInstance().setStarted(false);

		if (logger.isInfoEnabled())
			logger.info("UPaaS App [" + ServerContext.getInstance().getAppName() + "] Instance [" + ServerContext.getInstance().getInstId() + "] stopped.");

		clearLogger();
	}

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
	
	private void initZkClientFactory() {
		long start = System.currentTimeMillis();
		
		String zkConfigFilePath = ServerContext.getInstance().getConfigDirPath() + File.separator + AppConstants.CONFIG_FILE_NAME_ZKCLIENT;
		ZkClientFactory.init(new File(zkConfigFilePath));
		
		logger.info("Init ZkClientFactory from config file " + zkConfigFilePath);

		long end = System.currentTimeMillis();
		logger.debug("The ZkClientFactory initial end, in " + (end - start) + " ms.");
	}
	
	private void destroyZkClientFactory() {
		ZkClientFactory.destroy();
		logger.info("Stop StartupService");
	}

	private void initLogger(boolean isFirst) {
		String logDir = null;
		if (ServerContext.getInstance().hasExternalDir()) {
			logDir = new File(ServerContext.getInstance().getConfigDirPath()).getParentFile().getAbsolutePath() + File.separator + AppConstants.LOG_DIR_NAME;
		} else {
			String jettyHome = ServerContext.getInstance().getJettyHome();
			if (jettyHome != null)
				logDir = jettyHome + File.separator + AppConstants.LOG_DIR_NAME;
			else
				logDir = "." + File.separator + AppConstants.LOG_DIR_NAME;
		}
		System.setProperty(AppConstants.ENV_LOG_DIR, logDir);
		String logConfigDirPath = null;
		if (isFirst) {
			logConfigDirPath = ServerContext.getInstance().getWarRealPath() + File.separator + "WEB-INF" + File.separator + "config" + File.separator;
		} else {
			logConfigDirPath = ServerContext.getInstance().getConfigDirPath() + File.separator;
		}
		LoggerFactory.setLoggerProvider(new Log4jLoggerProvider(logConfigDirPath + AppConstants.LOG_CONFIG_FILE_NAME_SYSTEM)); // ϵͳ��־��ʼ��
		LoggerFactory.refresh();
		logger = LoggerFactory.getLogger(Server.class);
	}

	private void clearLogger() {
		LoggerFactory.destroy();
	}
	
}
