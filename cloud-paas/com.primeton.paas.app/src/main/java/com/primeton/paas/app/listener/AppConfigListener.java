/**
 * 
 */
package com.primeton.paas.app.listener;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gocom.cloud.cesium.config.api.client.ConfigListener;
import org.gocom.cloud.cesium.config.api.model.ConfigItem;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.AppConstants;
import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.api.log.IUserLogModify;
import com.primeton.paas.app.config.ConfigModelManager;
import com.primeton.paas.app.config.ZkConstants;
import com.primeton.paas.app.config.model.DataSourceModel;
import com.primeton.paas.app.config.model.ServiceSourceModel;
import com.primeton.paas.app.config.model.UserLogModel;
import com.primeton.paas.app.config.model.VariableModel;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;
import com.primeton.paas.app.spi.log.UserLogModifyFactory;
import com.primeton.paas.app.startup.Server;
import com.primeton.paas.app.util.AppUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class AppConfigListener implements ConfigListener {
	
	private static ILogger logger = SystemLoggerFactory.getLogger(AppConfigListener.class);

	/**
	 * 
	 */
	public void addConfigItem(String moduleName, ConfigItem configItem) {
		if (ZkConstants.CONFIG_MODULE_DATA_SOURCE.equals(moduleName)) {
			DataSourceModel model = (DataSourceModel) configItem.getValue();
			ConfigModelManager.addDataSourceModel(model);
			if (logger.isInfoEnabled()) logger.info("Add DataSource config " + configItem.getKey() + "=" + model);
			
		} else if (ZkConstants.CONFIG_MODULE_USER_LOG.equals(moduleName)) {
			UserLogModel model = (UserLogModel) configItem.getValue();
			
			ConfigModelManager.setUserLogModel(model);
			
			String configPath = ServerContext.getInstance().getConfigDirPath();
			final String userLogConfig = configPath + File.separator + AppConstants.LOG_CONFIG_FILE_NAME_USER;
			try {
				AppUtil.updateUserLoggerLevel(model.getLogLevel(null), userLogConfig);
				
				// init default user.log
				Server.getInstance().initLogger(false, true);
				
				// init other userLogs
				List<IUserLogModify> list = UserLogModifyFactory.getModifies();
				logger.info(list);
				Map<String,String> userlogs = model.getUserLogs();
				for (IUserLogModify userLog : list) {
					Set<String> keySet = userlogs.keySet();
					Iterator<String> it = keySet.iterator();
					while(it.hasNext()){ 
						String type = it.next();
					   userLog.doModifyLevel(type, userlogs.get(type));
					}
				}
			} catch (Throwable t) {
				if(logger.isErrorEnabled()) {
					logger.error(t);
				}
			}
			if (logger.isInfoEnabled()) logger.info("Add UserLog config " + model);
			
		} else if(ZkConstants.CONFIG_MODULE_SERVICE_SOURCE.equals(moduleName)) {
			ServiceSourceModel model = (ServiceSourceModel) configItem.getValue();
			ConfigModelManager.addServiceModel(model);
			if (logger.isInfoEnabled()) logger.info("Add ServiceSource config " + model);
			
		} else if(ZkConstants.CONFIG_MODULE_APP_VARIABLE.equals(moduleName)) {
			VariableModel model = (VariableModel) configItem.getValue();
			ConfigModelManager.addVariableModel(model);
		}
	}

	/**
	 * 
	 */
	public void deleteConfigItem(String moduleName, String configKey) {
		if (ZkConstants.CONFIG_MODULE_DATA_SOURCE.equals(moduleName)) {
			ConfigModelManager.removeDataSourceModel(configKey);
			if (logger.isInfoEnabled()) logger.info("Delete DataSource config " + configKey);
		} else if (ZkConstants.CONFIG_MODULE_USER_LOG.equals(moduleName)) {
			ConfigModelManager.setUserLogModel(null);
			if (logger.isInfoEnabled()) logger.info("Delete UserLog config " + configKey);
		} else if(ZkConstants.CONFIG_MODULE_SERVICE_SOURCE.equals(moduleName)) {
			ConfigModelManager.removeServiceModel(configKey);
			if (logger.isInfoEnabled()) logger.info("Delete ServiceSource config " + configKey);
		} else if(ZkConstants.CONFIG_MODULE_APP_VARIABLE.equals(moduleName)) {
			ConfigModelManager.removeVariableModel(configKey);
			if (logger.isInfoEnabled()) logger.info("Delete Variable config " + configKey);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gocom.cloud.cesium.config.api.client.ConfigListener#updateConfigItem(java.lang.String, org.gocom.cloud.cesium.config.api.model.ConfigItem)
	 */
	public void updateConfigItem(String moduleName, ConfigItem configItem) {
		if (ZkConstants.CONFIG_MODULE_DATA_SOURCE.equals(moduleName)) {
			DataSourceModel model = (DataSourceModel) configItem.getValue();
			ConfigModelManager.addDataSourceModel(model);
			if (logger.isInfoEnabled()) logger.info("Update DataSource config " + configItem.getKey() + "=" + model);
		} else if (ZkConstants.CONFIG_MODULE_USER_LOG.equals(moduleName)) {
			UserLogModel model = (UserLogModel) configItem.getValue();
			ConfigModelManager.setUserLogModel(model);
			
			String configPath = ServerContext.getInstance().getConfigDirPath();
			final String userLogConfig = configPath + File.separator + AppConstants.LOG_CONFIG_FILE_NAME_USER;
			if (model != null) {
				try {
					AppUtil.updateUserLoggerLevel(model.getLogLevel(UserLogModel.DEFAULT_USERLOG_TYPE), userLogConfig);  
					if(logger.isInfoEnabled()) {
						logger.info("Update local file {" + userLogConfig + "}, User Logger level change to {" + model.getLogLevel(UserLogModel.DEFAULT_USERLOG_TYPE) + "}");
					}
					// init default user.log
					Server.getInstance().initLogger(false, true);
					
					// init other userLogs
					List<IUserLogModify> list = UserLogModifyFactory.getModifies();
					logger.info("Had load "+list.size()+" IUserLogModify implements.Do modify.");
					Map<String,String> userlogs = model.getUserLogs();
					for (IUserLogModify userLog : list) {
						Set<String> keySet = userlogs.keySet();
						Iterator<String> it = keySet.iterator();
						while (it.hasNext()) {
						   String type = it.next();
						   userLog.doModifyLevel(type, userlogs.get(type));
						}
					}
					
				} catch (Throwable t) {
					if(logger.isErrorEnabled()) {
						logger.error(t);
					}
				}
			}
			if (logger.isInfoEnabled()) logger.info("Update UserLog config " + model);
		} else if (ZkConstants.CONFIG_MODULE_SERVICE_SOURCE.equals(moduleName)) {
			ServiceSourceModel model = (ServiceSourceModel) configItem.getValue();
			ConfigModelManager.addServiceModel(model);
			if (logger.isInfoEnabled()) logger.info("Update ServiceSource config " + model);
		} else if (ZkConstants.CONFIG_MODULE_APP_VARIABLE.equals(moduleName)) {
			VariableModel model = (VariableModel) configItem.getValue();
			ConfigModelManager.addVariableModel(model);
			if (logger.isInfoEnabled()) logger.info("Update Variable config " + model);
		}
	}
	
}
