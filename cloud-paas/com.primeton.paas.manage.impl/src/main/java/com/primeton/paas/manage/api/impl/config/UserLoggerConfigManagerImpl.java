/**
 * 
 */
package com.primeton.paas.manage.api.impl.config;

import java.util.Map;

import org.gocom.cloud.cesium.common.api.exception.TimeoutException;
import org.gocom.cloud.cesium.config.api.client.ConfigManager;
import org.gocom.cloud.cesium.config.api.client.ConfigManagerFactory;
import org.gocom.cloud.cesium.config.api.execption.ConfigModuleNotFoundException;
import org.gocom.cloud.cesium.config.api.model.ConfigItem;
import org.gocom.cloud.cesium.config.api.model.ConfigModule;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.config.ZkConstants;
import com.primeton.paas.app.config.model.UserLogModel;
import com.primeton.paas.exception.api.PaasRuntimeException;
import com.primeton.paas.manage.api.config.IUserLoggerConfigManager;
import com.primeton.paas.manage.api.exception.ConfigureException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class UserLoggerConfigManagerImpl implements IUserLoggerConfigManager {

	private ConfigManager configManager = null;
	private static ILogger logger = ManageLoggerFactory.getLogger(UserLoggerConfigManagerImpl.class);
	
	/**
	 * Default. <br>
	 */
	public UserLoggerConfigManagerImpl() {
		super();
		try {
			this.configManager = ConfigManagerFactory.createConfigManager();
		} catch (TimeoutException e) {
			logger.error(e);
			throw new PaasRuntimeException("Connect to zookeeper timeout", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IUserLoggerConfigManager#getUserLogModel(java.lang.String)
	 */
	public UserLogModel getUserLogModel(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		try {
			ConfigItem[] configItems = configManager.getAppConfigItems(appName, ZkConstants.CONFIG_MODULE_USER_LOG);
			if (configItems != null && configItems.length > 0) {
				for (ConfigItem configItem : configItems) {
					if (configItem != null && configItem.getValue() != null
							&& configItem.getValue() instanceof UserLogModel) {
						return (UserLogModel) configItem.getValue();
					}
				}
			}
		} catch (ConfigModuleNotFoundException e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IUserLoggerConfigManager#updateUserLogModel(java.lang.String, com.primeton.paas.app.config.model.UserLogModel)
	 */
	public void updateUserLogModel(String appName, UserLogModel model) throws ConfigureException {
		if (StringUtil.isEmpty(appName)
				|| model == null
				|| StringUtil.isEmpty(model
						.getLogLevel(UserLogModel.DEFAULT_USERLOG_TYPE))) {
			return;
		}
		if (!existsUserModule(appName)) {
			throw new ConfigureException("App UserLog Module {.../" + appName + "/userLog} not found in zookeeper.");
		}
		ConfigItem configItem = new ConfigItem(ZkConstants.APP_TYPE_DEFAULT);
		configItem.setValue(model);
		configManager.setAppConfigItem(appName, ZkConstants.CONFIG_MODULE_USER_LOG, configItem);
		if (logger.isInfoEnabled()) {
			logger.info("Update [" + appName + "] UserLog {" + model + "} success.");
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IUserLoggerConfigManager#updateUserLogModel(java.lang.String, java.lang.String)
	 */
	public void updateUserLogModel(String appName, String level) throws ConfigureException {
		if(StringUtil.isEmpty(appName) || StringUtil.isEmpty(level)) {
			return;
		}
		UserLogModel model = getUserLogModel(appName);
		//	UserLogModel model = new UserLogModel();
		//	model.setLogLevel(level);
		model.setLogLevel(UserLogModel.DEFAULT_USERLOG_TYPE, level);
		updateUserLogModel(appName, model);
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	private boolean existsUserModule(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return false;
		}
		ConfigModule[] configModules = configManager.getAppConfigModules(appName);
		ConfigModule configModule = null;
		if (configModules != null && configModules.length > 0) {
			for (ConfigModule module : configModules) {
				if (module != null
						&& ZkConstants.CONFIG_MODULE_USER_LOG
								.equals(module.getModuleName())) {
					configModule = module;
					break;
				}
			}
		}
		return configModule != null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IUserLoggerConfigManager#updateUserLogsModel(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void updateUserLogsModel(String appName, String type, String level)
			throws ConfigureException {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(level)) {
			return;
		}
		UserLogModel model = getUserLogModel(appName);
		if (model.getLogLevel(type) == null) {
			throw new ConfigureException("App UserLog Module {.../" + appName + "/  " + type + "userLog} not found in zookeeper.");
		}
		model.setLogLevel(type, level);
		updateUserLogModel(appName, model);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IUserLoggerConfigManager#addUserLogModel(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addUserLogModel(String appName, String type, String level) throws ConfigureException {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(type)) {
			throw new ConfigureException(
					"Add UserLog Error.Cause error params!");
		}
		UserLogModel model = getUserLogModel(appName);
		if (model == null) {
			throw new ConfigureException("App UserLog Module {.../" + appName + "/  " + type + "userLog} not found in zookeeper.");
		}
		model.setLogLevel(type, level);
		updateUserLogModel(appName, model);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IUserLoggerConfigManager#addUserLogModel(java.lang.String, com.primeton.paas.app.config.model.UserLogModel)
	 */
	public void addUserLogModel(String appName, UserLogModel model) throws ConfigureException {
		if (StringUtil.isEmpty(appName) || model == null) {
			return;
		}
		UserLogModel orlModel = getUserLogModel(appName);
		if (orlModel == null) {
			throw new ConfigureException("App UserLog Module {.../"
					+ appName + "/ userLog} not found in zookeeper.");
		}
		orlModel.getUserLogs().putAll(model.getUserLogs());
		updateUserLogModel(appName, orlModel);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IUserLoggerConfigManager#deleteUserLogModel(java.lang.String, java.lang.String)
	 */
	public void deleteUserLogModel(String appName, String type) throws ConfigureException {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(type)) {
			return;
		}
		UserLogModel model = getUserLogModel(appName);
		if (model.getLogLevel(type) == null) {
			throw new ConfigureException("App UserLog Module {.../" + appName + "/ userLog} not found in zookeeper.");
		}
		model.getUserLogs().remove(type);
		updateUserLogModel(appName, model);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.config.IUserLoggerConfigManager#deleteUserLogModel(java.lang.String, com.primeton.paas.app.config.model.UserLogModel)
	 */
	public void deleteUserLogModel(String appName, UserLogModel model) throws ConfigureException {
		if (StringUtil.isEmpty(appName) || model == null) {
			return;
		}
		UserLogModel orlModel = getUserLogModel(appName);
		if (orlModel == null) {
			throw new ConfigureException("App UserLog Module {.../"
					+ appName + "/ userLog} not found in zookeeper.");
		}
		Map<String,String> deletes = model.getUserLogs();
		for (String type : deletes.keySet()) {
			if (UserLogModel.DEFAULT_USERLOG_TYPE.equalsIgnoreCase(type)) {
				continue;
			}
			orlModel.getUserLogs().remove(type);
		}
		updateUserLogModel(appName, orlModel);
	}
	
}
