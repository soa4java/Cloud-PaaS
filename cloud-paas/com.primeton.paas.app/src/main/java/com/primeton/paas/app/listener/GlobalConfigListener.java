/**
 * 
 */
package com.primeton.paas.app.listener;

import org.gocom.cloud.cesium.config.api.client.ConfigListener;
import org.gocom.cloud.cesium.config.api.model.ConfigItem;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.config.ZkConstants;
import com.primeton.paas.app.config.ConfigModelManager;
import com.primeton.paas.app.config.model.SystemLogModel;
import com.primeton.paas.app.config.model.ThreadPoolModel;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class GlobalConfigListener implements ConfigListener {
	
	private static ILogger log = SystemLoggerFactory.getLogger(GlobalConfigListener.class);

	/*
	 * (non-Javadoc)
	 * @see org.gocom.cloud.cesium.config.api.client.ConfigListener#addConfigItem(java.lang.String, org.gocom.cloud.cesium.config.api.model.ConfigItem)
	 */
	public void addConfigItem(String moduleName, ConfigItem configItem) {
		if (ZkConstants.CONFIG_MODULE_THREAD_POOL.equals(moduleName)) {
			ThreadPoolModel model = (ThreadPoolModel) configItem.getValue();
			ConfigModelManager.setThreadPoolModel(model);
			if (log.isInfoEnabled()) log.info("Add ThreadPool config " + model);
		} else if (ZkConstants.CONFIG_MODULE_SYSTEM_LOG.equals(moduleName)) {
			SystemLogModel model = (SystemLogModel) configItem.getValue();
			ConfigModelManager.setSystemLogModel(model);
			if (log.isInfoEnabled()) log.info("Add SystemLog config " + model);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gocom.cloud.cesium.config.api.client.ConfigListener#deleteConfigItem(java.lang.String, java.lang.String)
	 */
	public void deleteConfigItem(String moduleName, String configKey) {
		if (ZkConstants.CONFIG_MODULE_THREAD_POOL.equals(moduleName)) {
			ConfigModelManager.setThreadPoolModel(null);
			if (log.isInfoEnabled()) log.info("Delete ThreadPool config " + configKey);
		} else if (ZkConstants.CONFIG_MODULE_SYSTEM_LOG.equals(moduleName)) {
			ConfigModelManager.setSystemLogModel(null);
			log.info("Delete SystemLog config " + configKey);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gocom.cloud.cesium.config.api.client.ConfigListener#updateConfigItem(java.lang.String, org.gocom.cloud.cesium.config.api.model.ConfigItem)
	 */
	public void updateConfigItem(String moduleName, ConfigItem configItem) {
		if (ZkConstants.CONFIG_MODULE_THREAD_POOL.equals(moduleName)) {
			ThreadPoolModel model = (ThreadPoolModel) configItem.getValue();
			ConfigModelManager.setThreadPoolModel(model);
			log.info("Update ThreadPool config " + model);
		} else if (ZkConstants.CONFIG_MODULE_SYSTEM_LOG.equals(moduleName)) {
			SystemLogModel model = (SystemLogModel) configItem.getValue();
			ConfigModelManager.setSystemLogModel(model);
			log.info("Update SystemLog config " + model);
		}
	}
	
}
