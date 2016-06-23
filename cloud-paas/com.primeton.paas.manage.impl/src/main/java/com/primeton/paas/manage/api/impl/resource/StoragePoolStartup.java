/**
 * 
 */
package com.primeton.paas.manage.api.impl.resource;

import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.spi.factory.StoragePoolConfigFactory;
import com.primeton.paas.manage.spi.model.StoragePoolConfig;
import com.primeton.paas.manage.spi.resource.IStoragePoolConfig;
import com.primeton.paas.manage.spi.resource.IStoragePoolStartup;

/**
 * 
 * @author liming(li-ming@primeton.com)
 *
 */
public class StoragePoolStartup implements IStoragePoolStartup {

	private static ILogger logger = ManageLoggerFactory
			.getLogger(StoragePoolStartup.class);

	private boolean isStarted = false;

	private static IStoragePoolConfig configManager = StoragePoolConfigFactory.getManager();

	/**
	 * Default. <br>
	 */
	public StoragePoolStartup() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.primeton.paas.manage.spi.resource.ISharedStoragePoolManager#start()
	 */
	public void start() {
		if (!SystemVariables.isIaasEnableStorage()) {
			logger.warn("Can not start storage resource monitor, IAAS-Storage API has been disabled.");
			return;
		}
		if (this.isStarted) {
			return;
		}

		List<StoragePoolConfig> configs = configManager.getAllEnabled();
		if (configs == null || configs.isEmpty()) {
			return;
		}

		for (StoragePoolConfig sPoolConfig : configs) {
			StorageResourceMonitor monitor = new StorageResourceMonitor(
					sPoolConfig.getId(), sPoolConfig);
			StorageResourceMonitorManager.register(monitor);
		}

		this.isStarted = true;
		if (logger.isInfoEnabled()) {
			logger.info("Start SharedStorage POOL success.");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStoragePoolStartup#stop()
	 */
	public void stop() {
		if (!this.isStarted) {
			return;
		}
		StorageResourceMonitorManager.unregister();
		this.isStarted = true;
		logger.info("Close storage resource monitor.");
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStoragePoolStartup#restart()
	 */
	public void restart() {
		stop();
		start();
	}

}
