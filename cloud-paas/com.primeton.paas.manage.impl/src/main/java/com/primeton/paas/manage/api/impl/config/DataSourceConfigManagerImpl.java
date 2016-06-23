/**
 * 
 */
package com.primeton.paas.manage.api.impl.config;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.common.api.exception.TimeoutException;
import org.gocom.cloud.cesium.config.api.client.ConfigManager;
import org.gocom.cloud.cesium.config.api.client.ConfigManagerFactory;
import org.gocom.cloud.cesium.config.api.execption.ConfigItemNotFoundException;
import org.gocom.cloud.cesium.config.api.execption.ConfigModuleNotFoundException;
import org.gocom.cloud.cesium.config.api.model.ConfigItem;
import org.gocom.cloud.cesium.config.api.model.ConfigModule;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.config.ZkConstants;
import com.primeton.paas.app.config.model.DataSourceModel;
import com.primeton.paas.exception.api.PaasRuntimeException;
import com.primeton.paas.manage.api.config.DataSourceConfig;
import com.primeton.paas.manage.api.config.IDataSourceConfigManager;
import com.primeton.paas.manage.api.exception.ConfigureException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DataSourceConfigManagerImpl implements IDataSourceConfigManager {
	
	private ConfigManager configManager = null;
	private static ILogger logger = ManageLoggerFactory.getLogger(DataSourceConfigManagerImpl.class);
	
	/**
	 * Default. <br>
	 */
	public DataSourceConfigManagerImpl() {
		super();
		try {
			this.configManager = ConfigManagerFactory.createConfigManager();
		} catch (TimeoutException e) {
			throw new PaasRuntimeException("Connect to zookeeper timeout", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IDataSourceConfigManager#getDataSourceConfig(java.lang.String)
	 */
	public DataSourceConfig getDataSourceConfig(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		DataSourceConfig dataSourceConfig = new DataSourceConfig(appName);
		try {
			ConfigItem[] items = configManager.getAppConfigItems(appName, ZkConstants.CONFIG_MODULE_DATA_SOURCE);
			if (items != null && items.length > 0) {
				List<DataSourceModel> models = new ArrayList<DataSourceModel>();
				for (ConfigItem item : items) {
					if (item != null) {
						Object obj = item.getValue();
						if (obj instanceof DataSourceModel) {
							models.add((DataSourceModel)obj);
						}
					}
				}
				dataSourceConfig.setDataSourceModels(models);
			}
		} catch (ConfigModuleNotFoundException e) {
			logger.error(e);
		}
		return dataSourceConfig;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IDataSourceConfigManager#getAllDataSourceModels(java.lang.String)
	 */
	public DataSourceModel[] getAllDataSourceModels(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return new DataSourceModel[0];
		}
		DataSourceConfig dataSourceConfig = getDataSourceConfig(appName);
		return dataSourceConfig == null ? new DataSourceModel[0] : dataSourceConfig.getDataSourceModels();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IDataSourceConfigManager#getDataSourceModel(java.lang.String, java.lang.String)
	 */
	public DataSourceModel getDataSourceModel(String appName, String dataSourceName) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(dataSourceName)) {
			return null;
		}
		try {
			ConfigItem configItem = configManager.getAppConfigItem(appName, ZkConstants.CONFIG_MODULE_DATA_SOURCE, dataSourceName);
			if (configItem != null) {
				Object model = configItem.getValue();
				if (model != null && model instanceof DataSourceModel) {
					return (DataSourceModel)model;
				}
			}
		} catch (ConfigItemNotFoundException e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IDataSourceConfigManager#getDataSourceNames(java.lang.String)
	 */
	public String[] getDataSourceNames(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return new String[0];
		}
		List<String> names = new ArrayList<String>();
		DataSourceConfig dataSourceConfig = getDataSourceConfig(appName);
		if (dataSourceConfig != null) {
			DataSourceModel[] models = dataSourceConfig.getDataSourceModels();
			if (models != null && models.length > 0) {
				for (DataSourceModel model : models) {
					if (model != null && model.getDataSourceName() != null) {
						names.add(model.getDataSourceName());
					}
				}
			}
		}
		return names.toArray(new String[names.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IDataSourceConfigManager#updateDataSource(java.lang.String, com.primeton.paas.app.config.model.DataSourceModel)
	 */
	public void updateDataSource(String appName, DataSourceModel model) throws ConfigureException {
		if (StringUtil.isEmpty(appName) || model == null) {
			return;
		}
		if (!existsDataSourceModule(appName)) {
			throw new ConfigureException("App DataSource Module {.../"
					+ appName + "/dataSource} not found in zookeeper.");
		}
		ConfigItem configItem = new ConfigItem(model.getDataSourceName());
		configItem.setValue(model);
		configManager.setAppConfigItem(appName, ZkConstants.CONFIG_MODULE_DATA_SOURCE, configItem);
		logger.info("Update DataSource {" + model + "} success.");
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IDataSourceConfigManager#addDataSource(java.lang.String, com.primeton.paas.app.config.model.DataSourceModel)
	 */
	public void addDataSource(String appName, DataSourceModel model) throws ConfigureException {
		if (StringUtil.isEmpty(appName) || model == null
				|| StringUtil.isEmpty(model.getDataSourceName())) {
			return;
		}
		DataSourceModel existed = getDataSourceModel(appName, model.getDataSourceName());
		if (existed != null) {
			throw new ConfigureException("DataSource {"
					+ model.getDataSourceName() + "} already exists.");
		}
		ConfigItem configItem = new ConfigItem(model.getDataSourceName());
		configItem.setValue(model);
		configManager.setAppConfigItem(appName, ZkConstants.CONFIG_MODULE_DATA_SOURCE, configItem);
		logger.info("Add DataSource {" + model + "} success.");
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IDataSourceConfigManager#removeDataSource(java.lang.String, java.lang.String)
	 */
	public void removeDataSource(String appName, String dataSourceName) throws ConfigureException {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(dataSourceName)) {
			return;
		}
		if (!existsDataSourceModule(appName)) {
			throw new ConfigureException("App DataSource Module {.../"
					+ appName + "/dataSource} not found in zookeeper.");
		}
		configManager.removeAppConfigItem(appName, ZkConstants.CONFIG_MODULE_DATA_SOURCE, dataSourceName);
		logger.info("Delete DataSource {" + dataSourceName + "} success.");
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
		ConfigModule[] configModules = configManager.getAppConfigModules(appName);
		ConfigModule configModule = null;
		if (configModules != null && configModules.length > 0) {
			for (ConfigModule module : configModules) {
				if (module != null
						&& ZkConstants.CONFIG_MODULE_DATA_SOURCE
								.equals(module.getModuleName())) {
					configModule = module;
					break;
				}
			}
		}
		return configModule != null;
	}

}
