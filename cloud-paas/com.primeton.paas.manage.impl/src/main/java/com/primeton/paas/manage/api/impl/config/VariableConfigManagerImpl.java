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
import com.primeton.paas.app.config.model.VariableModel;
import com.primeton.paas.exception.api.PaasRuntimeException;
import com.primeton.paas.manage.api.config.IVariableConfigManager;
import com.primeton.paas.manage.api.config.VariableConfig;
import com.primeton.paas.manage.api.exception.ConfigureException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VariableConfigManagerImpl implements IVariableConfigManager {
	
	private ConfigManager configManager = null;
	private static ILogger logger = ManageLoggerFactory.getLogger(VariableConfigManagerImpl.class);
	
	/**
	 * Default. <br>
	 */
	public VariableConfigManagerImpl() {
		super();
		try {
			this.configManager = ConfigManagerFactory.createConfigManager();
		} catch (TimeoutException e) {
			logger.error(e);
			throw new PaasRuntimeException("Connect to zookeeper timeout", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IVariableConfigManager#getVariableConfig(java.lang.String)
	 */
	public VariableConfig getVariableConfig(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		VariableConfig variableConfig = new VariableConfig(appName);
		try {
			ConfigItem[] items = configManager.getAppConfigItems(appName, ZkConstants.CONFIG_MODULE_APP_VARIABLE);
			if (items != null && items.length > 0) {
				List<VariableModel> variableModels = new ArrayList<VariableModel>();
				for (ConfigItem item : items) {
					if (item != null) {
						Object obj = item.getValue();
						if (obj instanceof VariableModel) {
							variableModels.add((VariableModel) obj);
						}
					}
				}
				variableConfig.setVariableModels(variableModels);
			}
		} catch (ConfigModuleNotFoundException e) {
			logger.error(e);
		}
		return variableConfig;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IVariableConfigManager#getVariableModel(java.lang.String, java.lang.String)
	 */
	public VariableModel getVariableModel(String appName, String varName) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(varName)) {
			return null;
		}
		try {
			ConfigItem configItem = configManager.getAppConfigItem(appName, ZkConstants.CONFIG_MODULE_APP_VARIABLE, varName);
			if (configItem != null) {
				Object model = configItem.getValue();
				if (model != null && model instanceof VariableModel) {
					return (VariableModel) model;
				}
			}
		} catch (ConfigItemNotFoundException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IVariableConfigManager#getVariableNames(java.lang.String)
	 */
	public String[] getVariableNames(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return new String[0];
		}
		List<String> names = new ArrayList<String>();
		VariableConfig variableConfig = getVariableConfig(appName);
		
		if (variableConfig != null) {
			VariableModel[] models = variableConfig.getVariableModels();
			if (models != null && models.length > 0) {
				for (VariableModel model : models) {
					if (model != null && model.getName() != null) {
						names.add(model.getName());
					}
				}
			}
		}
		return names.toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IVariableConfigManager#getAllVariableModels(java.lang.String)
	 */
	public VariableModel[] getAllVariableModels(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return new VariableModel[0];
		}
		VariableConfig variableConfig = getVariableConfig(appName);
		return variableConfig == null ? new VariableModel[0] : variableConfig.getVariableModels();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IVariableConfigManager#updateVariableModel(java.lang.String, com.primeton.paas.app.config.model.VariableModel)
	 */
	public void updateVariableModel(String appName, VariableModel var) throws ConfigureException {
		if (StringUtil.isEmpty(appName) || var == null) {
			return;
		}
		if (!existsVariableModule(appName)) {
			throw new ConfigureException("App Variable Module {.../" + appName + "/variable} not found in zookeeper.");
		}
		ConfigItem configItem = new ConfigItem(var.getName());
		configItem.setValue(var);
		configManager.setAppConfigItem(appName, ZkConstants.CONFIG_MODULE_APP_VARIABLE, configItem);
		logger.info("Update variable {" + var + "} success.");
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IVariableConfigManager#removeVariableModel(java.lang.String, java.lang.String)
	 */
	public void removeVariableModel(String appName, String varName) throws ConfigureException {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(varName)) {
			return;
		}
		if (!existsVariableModule(appName)) {
			throw new ConfigureException("App Variable Module {.../" + appName + "/variable} not found in zookeeper.");
		}
		configManager.removeAppConfigItem(appName, ZkConstants.CONFIG_MODULE_APP_VARIABLE, varName);
		logger.info("Delete variable {" + varName + "} success.");
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	private boolean existsVariableModule(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return false;
		}
		ConfigModule[] configModules = configManager.getAppConfigModules(appName);
		ConfigModule configModule = null;
		if (configModules != null && configModules.length > 0) {
			for (ConfigModule module : configModules) {
				if (module != null
						&& ZkConstants.CONFIG_MODULE_APP_VARIABLE
								.equals(module.getModuleName())) {
					configModule = module;
					break;
				}
			}
		}
		return configModule != null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IVariableConfigManager#addVariableModel(java.lang.String, com.primeton.paas.app.config.model.VariableModel)
	 */
	public void addVariableModel(String appName, VariableModel var) throws ConfigureException {
		if (StringUtil.isEmpty(appName) || var == null
				|| StringUtil.isEmpty(var.getName())) {
			return;
		}
		if (!existsVariableModule(appName)) {
			throw new ConfigureException("App Variable Module {.../" + appName + "/variable} not found in zookeeper.");
		}
		VariableModel model = null;
		try {
			model = getVariableModel(appName, var.getName());
		} catch (Throwable t) {
			// ignore
		}
		if (model != null) {
			throw new ConfigureException("Variable {" + var.getName() + "} already exists.");
		}
		ConfigItem configItem = new ConfigItem(var.getName());
		configItem.setValue(var);
		configManager.setAppConfigItem(appName, ZkConstants.CONFIG_MODULE_APP_VARIABLE, configItem);
		logger.info("Add variable {" + var + "} success.");
	}
	
}
