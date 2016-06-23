/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.manage.api.impl.config;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.common.api.exception.TimeoutException;
import org.gocom.cloud.cesium.config.api.client.ConfigManager;
import org.gocom.cloud.cesium.config.api.client.ConfigManagerFactory;
import org.gocom.cloud.cesium.config.api.model.ConfigItem;
import org.gocom.cloud.cesium.config.api.model.ConfigModule;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.config.ZkConstants;
import com.primeton.paas.app.config.eos.DasModel;
import com.primeton.paas.app.config.eos.DataSourceModel;
import com.primeton.paas.app.config.eos.HttpAccessModel;
import com.primeton.paas.exception.api.PaasRuntimeException;
import com.primeton.paas.manage.api.config.IEosAppConfigManager;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class EosAppConfigManagerImpl implements IEosAppConfigManager {
	
	private static ConfigManager configManager;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(EosAppConfigManagerImpl.class);

	/**
	 * Default. <br>
	 */
	public EosAppConfigManagerImpl() {
		super();
	}
	
	/**
	 * 
	 * @return
	 */
	protected static ConfigManager getConfigManager() {
		if (null == configManager) {
			try {
				configManager = ConfigManagerFactory.createConfigManager();
			} catch (TimeoutException e) {
				throw new PaasRuntimeException("Connect to zookeeper timeout", e); //$NON-NLS-1$
			}
		}
		return configManager;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IEosAppConfigManager#getDataSources(java.lang.String)
	 */
	public List<DataSourceModel> getDataSources(String appName) {
		List<DataSourceModel> models = new ArrayList<DataSourceModel>();
		if (StringUtil.isEmpty(appName)) {
			return models;
		}
		try {
			ConfigItem[] items = getConfigManager().getAppConfigItems(appName, ZkConstants.CONFIG_MODULE_EOS_DATASOURCE);
			if (items != null && items.length > 0) {
				for (ConfigItem item : items) {
					Object obj = null == item ? null : item.getValue();
					if (null != obj && obj instanceof DataSourceModel) {
						models.add((DataSourceModel)obj);
					}
				}
			}
		} catch (Throwable e) {
			logger.error(e);
		}
		return models;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IEosAppConfigManager#getDataSource(java.lang.String, java.lang.String)
	 */
	public DataSourceModel getDataSource(String appName, String dsName) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(dsName)) {
			return null;
		}
		try {
			ConfigItem configItem = getConfigManager().getAppConfigItem(
					appName, ZkConstants.CONFIG_MODULE_EOS_DATASOURCE,
					ZkConstants.CONFIG_ITEM_DEFAULT);
			Object obj = null == configItem ? null : configItem.getValue();
			return null != obj && obj instanceof DataSourceModel ? (DataSourceModel) obj
					: null;
		} catch (Throwable e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IEosAppConfigManager#deleteDataSource(java.lang.String, java.lang.String)
	 */
	public void deleteDataSource(String appName, String dsName) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(dsName)) {
			return;
		}
		if (!existsDataSourceModule(appName)) {
			return;
		}
		getConfigManager().removeAppConfigItem(appName, ZkConstants.CONFIG_MODULE_EOS_DATASOURCE, dsName);
		logger.info("Delete app {0} data source {1} configuration success.", new Object[] { appName, dsName });
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IEosAppConfigManager#saveDataSource(java.lang.String, com.primeton.paas.app.config.eos.DataSourceModel)
	 */
	public void saveDataSource(String appName, DataSourceModel ds) {
		if (StringUtil.isEmpty(appName) || null == ds || StringUtil.isEmpty(ds.getName())) {
			return;
		}
		ConfigItem configItem = new ConfigItem(ds.getName());
		configItem.setValue(ds);
		getConfigManager().setAppConfigItem(appName, ZkConstants.CONFIG_MODULE_EOS_DATASOURCE, configItem);
		logger.info("Save app {0} data source {1} configuration success.", new Object[] { appName, ds });
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	private boolean existsDataSourceModule(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return false;
		}
		ConfigModule[] configModules = getConfigManager().getAppConfigModules(appName);
		ConfigModule configModule = null;
		if (configModules != null && configModules.length > 0) {
			for (ConfigModule module : configModules) {
				if (module != null
						&& ZkConstants.CONFIG_MODULE_EOS_DATASOURCE
								.equals(module.getModuleName())) {
					configModule = module;
					break;
				}
			}
		}
		return configModule != null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IEosAppConfigManager#getDasModel(java.lang.String)
	 */
	public DasModel getDasModel(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		try {
			ConfigItem configItem = getConfigManager().getAppConfigItem(
					appName, ZkConstants.CONFIG_MODULE_EOS_DAS,
					ZkConstants.CONFIG_ITEM_DEFAULT);
			Object obj = null == configItem ? null : configItem.getValue();
			return null != obj && obj instanceof DasModel ? (DasModel) obj
					: null;
		} catch (Throwable e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IEosAppConfigManager#saveDasModel(java.lang.String, com.primeton.paas.app.config.eos.DasModel)
	 */
	public void saveDasModel(String appName, DasModel das) {
		if (StringUtil.isEmpty(appName) || null == das) {
			return;
		}
		ConfigItem configItem = new ConfigItem(ZkConstants.CONFIG_ITEM_DEFAULT);
		configItem.setValue(das);
		getConfigManager().setAppConfigItem(appName, ZkConstants.CONFIG_MODULE_EOS_DAS, configItem);
		logger.info("Save app {0} DAS {1} configuration success.", new Object[] { appName, das });
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IEosAppConfigManager#getHttpAccessModel(java.lang.String)
	 */
	public HttpAccessModel getHttpAccessModel(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		try {
			ConfigItem configItem = getConfigManager().getAppConfigItem(
					appName, ZkConstants.CONFIG_MODULE_EOS_HTTP_ACCESS,
					ZkConstants.CONFIG_ITEM_DEFAULT);
			Object obj = null == configItem ? null : configItem.getValue();
			return null != obj && obj instanceof HttpAccessModel ? (HttpAccessModel) obj
					: null;
		} catch (Throwable e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IEosAppConfigManager#saveHttpAccessModel(java.lang.String, com.primeton.paas.app.config.eos.HttpAccessModel)
	 */
	public void saveHttpAccessModel(String appName, HttpAccessModel model) {
		if (StringUtil.isEmpty(appName) || null == model) {
			return;
		}
		ConfigItem configItem = new ConfigItem(ZkConstants.CONFIG_ITEM_DEFAULT);
		configItem.setValue(model);
		getConfigManager().setAppConfigItem(appName, ZkConstants.CONFIG_MODULE_EOS_HTTP_ACCESS, configItem);
		logger.info("Save app {0} HTTP Access {1} configuration success.", new Object[] { appName, model });
	}

}
