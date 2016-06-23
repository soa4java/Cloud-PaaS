/**
 * 
 */
package com.primeton.paas.app.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.gocom.cloud.cesium.config.api.client.ConfigListener;
import org.gocom.cloud.cesium.config.api.client.ConfigListenerManager;
import org.gocom.cloud.cesium.config.api.client.ConfigManager;
import org.gocom.cloud.cesium.config.api.model.ConfigItem;
import org.gocom.cloud.cesium.config.api.model.ConfigModule;
import org.gocom.cloud.cesium.zkclient.api.ZkConfig;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.config.model.DataSourceModel;
import com.primeton.paas.app.config.model.ServiceSourceModel;
import com.primeton.paas.app.config.model.SimulatorDataSourceSetModel;
import com.primeton.paas.app.config.model.SimulatorVariableSetModel;
import com.primeton.paas.app.config.model.SystemLogModel;
import com.primeton.paas.app.config.model.ThreadPoolModel;
import com.primeton.paas.app.config.model.UserLogModel;
import com.primeton.paas.app.config.model.VariableModel;
import com.primeton.paas.app.listener.AppConfigListener;
import com.primeton.paas.app.listener.GlobalConfigListener;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;
import com.primeton.paas.app.util.AppUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConfigModelManager {

	private static ILogger logger = SystemLoggerFactory.getLogger(ConfigModelManager.class);

	private static ConfigManager configManager = null;
	
	private static ConfigListenerManager configListenerManager = null;

	private static ConfigListener globalListener;

	private static ConfigListener appListener;

	private static ZkConfig zkConfig;

	private static String appName;

	private static Map<String, DataSourceModel> dataSourceModels = new ConcurrentHashMap<String, DataSourceModel>();
	private static Map<String, ServiceSourceModel> serviceSourceModels = new ConcurrentHashMap<String, ServiceSourceModel>();
	private static Map<String, VariableModel> variableModels = new ConcurrentHashMap<String, VariableModel>();

	private static ThreadPoolModel threadPoolModel;
	private static UserLogModel userLogModel;
	private static SystemLogModel systemLogModel;
	
	private static SimulatorDataSourceSetModel simulatorDataSourceSetModel;
	private static SimulatorVariableSetModel simulatorVariableSetModel ;
	
	static {
		appName = ServerContext.getInstance().getAppName();
		if (appName == null) {
			logger.error("Environment variable {cesium.appName} not found. please check jvm params.");
		}
	}
	
	public static ZkConfig getZkConfig() {
		return zkConfig;
	}

	public static void setZkConfig(ZkConfig zkConfig) {
		ConfigModelManager.zkConfig = zkConfig;
	}

	public static String getAppName() {
		return appName;
	}

	public static void setAppName(String appName) {
		ConfigModelManager.appName = appName;
	}

	/**
	 * 
	 * @return
	 */
	public static String[] getDataSourceNames() {
		return dataSourceModels.keySet().toArray(new String[0]);
	}

	/**
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public static DataSourceModel getDataSourceModel(String dataSourceName) {
		if(dataSourceName == null || dataSourceName.trim().length() == 0) {
			return null;
		}
		return dataSourceModels.get(dataSourceName);
	}

	/**
	 * 
	 * @return
	 */
	public static DataSourceModel[] getDataSourceModels() {
		return dataSourceModels.values().toArray(new DataSourceModel[0]);
	}

	/**
	 * 
	 * @param model
	 */
	public static void addDataSourceModel(DataSourceModel model) {
		if (model == null || model.getDataSourceName() == null
				|| model.getDataSourceName().trim().length() == 0) {
			return;
		}
		dataSourceModels.put(model.getDataSourceName(), model);
	}

	/**
	 * 
	 * @param dataSourceName
	 */
	public static void removeDataSourceModel(String dataSourceName) {
		if (dataSourceName == null || dataSourceName.trim().length() == 0) {
			return;
		}
		dataSourceModels.remove(dataSourceName);
	}

	/**
	 * 
	 * @return
	 */
	public static ThreadPoolModel getThreadPoolModel() {
		return threadPoolModel;
	}

	/**
	 * 
	 * @param threadPoolModel
	 */
	public static void setThreadPoolModel(ThreadPoolModel threadPoolModel) {
		ConfigModelManager.threadPoolModel = threadPoolModel;
	}

	/**
	 * 
	 * @return
	 */
	public static UserLogModel getUserLogModel() {
		return userLogModel;
	}

	/**
	 * 
	 * @param userLogModel
	 */
	public static void setUserLogModel(UserLogModel userLogModel) {
		ConfigModelManager.userLogModel = userLogModel;
	}

	/**
	 * 
	 * @return
	 */
	public static SystemLogModel getSystemLogModel() {
		return systemLogModel;
	}

	/**
	 * 
	 * @param systemLogModel
	 */
	public static void setSystemLogModel(SystemLogModel systemLogModel) {
		ConfigModelManager.systemLogModel = systemLogModel;
	}
	
	/**
	 * 
	 * @param model
	 */
	public static void addServiceModel(ServiceSourceModel model) {
		if (model == null || model.getClusterId() == null
				|| model.getClusterId().trim().length() == 0)
			return;
		serviceSourceModels.put(model.getClusterId(), model);
	}
	
	/**
	 * 
	 * @param clusterId
	 */
	public static void removeServiceModel(String clusterId) {
		if (clusterId == null || clusterId.trim().length() == 0)
			return;
		serviceSourceModels.remove(clusterId);
	}
	
	/**
	 * 
	 * @return
	 */
	public static ServiceSourceModel[] getServiceSourceModels() {
		return serviceSourceModels.values().toArray(new ServiceSourceModel[serviceSourceModels.size()]);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String[] getServiceSourceNames() {
		return serviceSourceModels.keySet().toArray(new String[serviceSourceModels.size()]);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static ServiceSourceModel getServiceSourceModel(String name) {
		if (name == null || name.trim().length() == 0) {
			return null;
		}
		return serviceSourceModels.get(name);
	}
	
	/**
	 * 
	 * @param model
	 */
	public static void addVariableModel(VariableModel model) {
		if (model != null) {
			variableModels.put(model.getName(), model);
		}
	}
	
	/**
	 * 
	 * @param name
	 */
	public static void removeVariableModel(String name) {
		if (name == null || name.trim().length() == 0) {
			return;
		}
		variableModels.remove(name);
	}
	
	/**
	 * 
	 * @return
	 */
	public static VariableModel[] getVariableModels() {
		return variableModels.values().toArray(new VariableModel[0]);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static VariableModel getVariableModel(String name) {
		if (name == null || name.trim().length() == 0) {
			return null;
		}
		return variableModels.get(name);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String[] getVariableModelNames() {
		return variableModels.keySet().toArray(new String[variableModels.size()]);
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static ServiceSourceModel[] getServiceSourceModels(String type) {
		if (type == null || type.trim().length() == 0) {
			return null;
		}
		List<ServiceSourceModel> models = new ArrayList<ServiceSourceModel>();
		for (ServiceSourceModel model : serviceSourceModels.values()) {
			if (model != null && type.equals(model.getType())) {
				models.add(model);
			}
		}
		return models.toArray(new ServiceSourceModel[0]);
	}

	/**
	 * 
	 * @param configManager
	 */
	private static void reloadGlobalConfig(ConfigManager configManager) {
		ConfigModule[] modules = configManager.getGlobalConfigModules();
		if (modules == null || modules.length == 0) {
			logger.warn("Global ConfigModules not found, reload failured.");
			return;
		}
		
		for (ConfigModule module : modules) {
			String moduleName = module.getModuleName();
			if (ZkConstants.CONFIG_MODULE_THREAD_POOL.equals(moduleName)) {
				ConfigItem configItem = module.getConfigItem(ZkConstants.CONFIG_ITEM_DEFAULT);
				ThreadPoolModel model = (ThreadPoolModel) configItem.getValue();
				ConfigModelManager.setThreadPoolModel(model);
				logger.info("Fetch ThreadPool config " + model + " from zookeeper.");
			} else if (ZkConstants.CONFIG_MODULE_SYSTEM_LOG.equals(moduleName)) {
				ConfigItem configItem = module.getConfigItem(ZkConstants.CONFIG_ITEM_DEFAULT);
				SystemLogModel model = (SystemLogModel) configItem.getValue();
				ConfigModelManager.setSystemLogModel(model);
				logger.info("Fetch SystemLog config " + model + " from zookeeper.");
			}
		}
		
		logger.info("Load global config from zookeeper.");
	}
	
	/**
	 * 
	 * @param configManager
	 */
	private static void reloadAppConfig(ConfigManager configManager) {
		if (appName == null || appName.trim().length() == 0
				|| configManager == null) {
			return;
		}
		ConfigModule[] modules = configManager.getAppConfigModules(appName);
		if (modules == null || modules.length == 0) {
			logger.warn("App {" + appName + "} ConfigModules not found, reload failured.");
		}
		for (ConfigModule module : modules) {
			if (module == null) {
				continue;
			}
			String moduleName = module.getModuleName();
			if (moduleName == null || moduleName.trim().length() == 0) {
				continue;
			}
			if (ZkConstants.CONFIG_MODULE_DATA_SOURCE.equals(moduleName)) {
				ConfigItem[] configItems = module.getConfigItems();
				for (ConfigItem item : configItems) {
					if(item == null) {
						continue;
					}
					DataSourceModel model = (DataSourceModel) item.getValue();
					ConfigModelManager.addDataSourceModel(model);
					logger.info("Fetch DataSource config " + model + " from zooKeeper.");
				}
			} else if (ZkConstants.CONFIG_MODULE_USER_LOG.equals(moduleName)) {
				ConfigItem configItem = module.getConfigItem(ZkConstants.APP_TYPE_DEFAULT);
				if(configItem != null) {
					UserLogModel model = (UserLogModel) configItem.getValue();
					ConfigModelManager.setUserLogModel(model);
					logger.info("Fetch UserLog config " + model + " from zookeeper.");
				} else {
					Map<String,String> userlogs = new HashMap<String, String>();
					userlogs.put(UserLogModel.DEFAULT_USERLOG_TYPE, UserLogModel.LEVEL_INFO);
					UserLogModel defaultModel = new UserLogModel(userlogs);
					ConfigModelManager.setUserLogModel(defaultModel);
					logger.info("UserLog config not found in zookeeper, so use default {" + userLogModel + "}.");
				}
				
			} else if (ZkConstants.CONFIG_MODULE_SERVICE_SOURCE.equals(moduleName)) {
				ConfigItem[] configItems = module.getConfigItems();
				for (ConfigItem item : configItems) {
					if(item == null) {
						continue;
					}
					ServiceSourceModel model = (ServiceSourceModel)item.getValue();
					addServiceModel(model);
					logger.info("Fetch ServiceSource config " + model + " from zooKeeper.");
				}
			} else if(ZkConstants.CONFIG_MODULE_APP_VARIABLE.equals(moduleName)) {
				ConfigItem[] configItems = module.getConfigItems();
				for (ConfigItem item : configItems) {
					if(item == null) {
						continue;
					}
					VariableModel model = (VariableModel)item.getValue();
					addVariableModel(model);
					logger.info("Fetch Variable config " + model + " from zooKeeper.");
				}
			}
		}
		
		logger.info("Finish Load app config from zookeeper.");
	}
	

	/**
	 * 
	 */
	public static void reloadConfigFromZK() {
		configManager = AppUtil.getConfigManager(zkConfig);
		
		reloadGlobalConfig(configManager);
		
		reloadAppConfig(configManager);

		configListenerManager = AppUtil.getConfigListenerManager(zkConfig);
		
		globalListener = new GlobalConfigListener();
		configListenerManager.registerGlobalConfigListener(globalListener);
		logger.info("Subscribe global config listener to zooKeeper");
		
		appListener = new AppConfigListener();
		configListenerManager.registerAppConfigListener(appName, appListener);
		logger.info("Subscribe app config listener to zooKeeper");
	}

	/**
	 * 
	 */
	public static void destroy() {
		configManager.clear();
		
		configListenerManager.unregisterGlobalConfigListener(globalListener);
		configListenerManager.unregisterAppConfigListener(appName, globalListener);
		configListenerManager.clear();
		
		zkConfig = null;
		appName = null;

		dataSourceModels.clear();
		serviceSourceModels.clear();
		variableModels.clear();
		
		threadPoolModel = null;
		systemLogModel = null;
		userLogModel = null;
	}

	public static SimulatorDataSourceSetModel getSimulatorDataSourceSetModel() {
		return simulatorDataSourceSetModel;
	}

	public static void setSimulatorDataSourceSetModel(SimulatorDataSourceSetModel simulatorDataSourceSetModel) {
		ConfigModelManager.simulatorDataSourceSetModel = simulatorDataSourceSetModel;
	}

	public static SimulatorVariableSetModel getSimulatorVariableSetModel() {
		return simulatorVariableSetModel;
	}

	public static void setSimulatorVariableSetModel(SimulatorVariableSetModel simulatorVariableSetModel) {
		ConfigModelManager.simulatorVariableSetModel = simulatorVariableSetModel;
	}
	
}
