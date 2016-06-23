/**
 * 
 */
package com.primeton.paas.manage.api.impl.startup;

import java.io.File;
import java.util.Map;

import org.gocom.cloud.cesium.common.api.exception.TimeoutException;
import org.gocom.cloud.cesium.common.api.manage.variable.VariableManager;
import org.gocom.cloud.cesium.common.api.model.Variable;
import org.gocom.cloud.cesium.common.api.util.JsonSerializerUtil;
import org.gocom.cloud.cesium.config.template.api.ConfigTemplateManager;
import org.gocom.cloud.cesium.manage.runtime.api.client.InstanceManager;
import org.gocom.cloud.cesium.manage.runtime.api.model.MQServer;
import org.gocom.cloud.cesium.manage.runtime.api.naming.NamingUtil;
import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider;

import com.primeton.paas.common.spi.StartupListenerManager;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.CesiumFactory;
import com.primeton.paas.manage.api.startup.IManageStartup;
import com.primeton.paas.manage.api.util.StringUtil;

/** 
 * PAAS Management startup. <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultManageStartup implements IManageStartup {
	
	public static final String ZK_CONFIG = "zkConfig.xml";
	
	public static final String LOG4J_MANAGE = "log4j-manage.xml";
	
	public static final String LOG4J_CESIUM = "log4j-cesium.xml";
	
	public static final String RUNTIME_VAR_MQSERVER = "mqServer";
	
	private ILogger logger = null;
	
	private static String TYPE = System.getProperty("CONSOLE_TYPE", "platform"); //$NON-NLS-1$ //$NON-NLS-2$
	
	private String configRootPath;
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.bootstrap.IStartup#start(java.lang.String)
	 */
	public void start(String configPath) {
		if (StringUtil.isEmpty(configPath)) {
			throw new IllegalArgumentException("Config path is null or blank.");
		}
		File home = new File(configPath);
		if (home == null || !home.exists() || home.isFile()) {
			throw new IllegalArgumentException("Config path [" + configPath + "] not exists or it is not a directory.");
		}
		configRootPath = home.getAbsolutePath();
		
		// init log4j
		String log4jManage = configRootPath + File.separator + LOG4J_MANAGE;
		String log4jCesium = configRootPath + File.separator + LOG4J_CESIUM;
		ManageLoggerFactory.setLoggerProvider(new Log4jLoggerProvider(log4jManage));
		LoggerFactory.setLoggerProvider(new Log4jLoggerProvider(log4jCesium));
		logger = ManageLoggerFactory.getLogger(this.getClass());
		logger.info("PAAS Manage Service Startup.");
		logger.info("PAAS Manage Log initialized.");
		
		// init zookeeper
		final String zkCfgPath = configRootPath + File.separator + ZK_CONFIG;
		File zkCfg = new File(zkCfgPath);
		if (zkCfg.exists() && zkCfg.isFile()) {
			ZkClientFactory.init(zkCfg);
			logger.info("Init ZkClientFactory from config file " + zkCfgPath);
		} else {
			throw new IllegalArgumentException("Zookeeper config file [" + zkCfgPath + "] not found.");
		}
		
		if ("platform".equalsIgnoreCase(TYPE)) { //$NON-NLS-1$
			// Init zookeeper Application configuration templates.
			initAppConfigTemplate();
			
			// Table cld_variable TYPE = 'RUNNING'
			NamingUtil.initRuntimeVariablesToZK();
			
			// init rabbitmq address
			initMQServer();
			
			// zookeeper node clean
			ZkNodeCleanManager.startup();
		}
		
		// User define listeners
		StartupListenerManager.startListener();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.bootstrap.IStartup#stop()
	 */
	public void stop() {
		if ("platform".equalsIgnoreCase(TYPE)) { //$NON-NLS-1$
			ZkNodeCleanManager.shutdown();
		}
		
		StartupListenerManager.stopListener();
		
		ZkClientFactory.destroy();
	}
	
	/**
	 * 从Zookeeper上获取rabbitmq-server服务地址信息. <br>
	 */
	@SuppressWarnings("unchecked")
	private void initMQServer() {
		MQServer server = new MQServer();
		Map<String, String> args = null;
		
		VariableManager variableManager = CesiumFactory.getVariableManager();
		try {
			Variable var = variableManager.getVariable(RUNTIME_VAR_MQSERVER);
			if (var != null && var.getValue() != null && var.getValue().trim().length() > 0) {
				Object obj = JsonSerializerUtil.deserialize(var.getValue());
				if (obj != null && obj instanceof Map) {
					args = (Map<String, String>)obj;
				} else if (obj != null && obj instanceof MQServer) {
					args = ((MQServer)obj).getAttrs();
				}
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("Variable [mqServer] not found in table [cld_variable].");
				}
			}
		} catch (Throwable e) {
			logger.error(e);
			Variable var = NamingUtil.lookupVariable(RUNTIME_VAR_MQSERVER);
			if (var == null || var.getValue() == null
					|| var.getValue().trim().length() == 0) {
				logger.warn("Variable [/Cloud/Cesium/Variables/mqServer] not found in zookeeper.");
			} else {
				try {
					Object obj = JsonSerializerUtil.deserialize(var.getValue());
					if (obj != null && obj instanceof Map) {
						args = (Map<String, String>)obj;
					} else if (obj != null && obj instanceof MQServer) {
						args = ((MQServer)obj).getAttrs();
					}
				} catch (Throwable t) {
					logger.error(t);
				}
			}
		}
		if (args == null || args.isEmpty()) {
			logger.warn("Init MQServer error, mqServer not found in database table and zookeeper, Please add variable[mqServer] to table[cld_variable] first.");
			return;
		}
		server.addAttrs(args);
		
		// /Cloud/Cesium/MQServer
		InstanceManager instanceManager = CesiumFactory.getInstanceManager();
		instanceManager.setMQServer(server);
	}
	
	/**
	 * 初始化应用配置模板. <br>
	 */
	private void initAppConfigTemplate() {
		ConfigTemplateManager manager = null;
		try {
			manager = CesiumFactory.getConfigTemplateManager();
			// initAppConfigDB(manager);
			manager.initGlobalConfigFromTemplate();
		} catch (TimeoutException e) {
			logger.error("Init Zookeeper Global Config From DB Config Template Timeout.");
		}
	}
	
	/*
	private void initAppConfigDB(ConfigTemplateManager manager) {
		manager.clearAllConfigTemplates();
		
		ConfigItem threadPoolConfigItem = new ConfigItem(ZkConfigConstants.CONFIG_ITEM_DEFAULT);
		ThreadPoolModel threadPoolModel = new ThreadPoolModel();
		threadPoolModel.setPoolName("threadPool1");
		threadPoolModel.setMinPoolSize(10);
		threadPoolModel.setMaxPoolSize(200);
		threadPoolModel.setShrinkTime(2000);
		threadPoolConfigItem.setValue(threadPoolModel);
		manager.saveGlobalConfigItem(ZkConfigConstants.CONFIG_MODULE_THREAD_POOL, threadPoolConfigItem);
		
		ConfigItem systemLogConfigItem = new ConfigItem(ZkConfigConstants.CONFIG_ITEM_DEFAULT);
		SystemLogModel systemLogModel = new SystemLogModel();
		systemLogModel.setLogLevel("INFO");
		systemLogConfigItem.setValue(systemLogModel);
		manager.saveGlobalConfigItem(ZkConfigConstants.CONFIG_MODULE_SYSTEM_LOG, systemLogConfigItem);
		
		ConfigItem userLogConfigItem = new ConfigItem(ZkConfigConstants.CONFIG_ITEM_DEFAULT);
		UserLogModel userLogModel = new UserLogModel();
		userLogModel.setLogLevel("INFO");
		userLogConfigItem.setValue(userLogModel);
		manager.saveAppConfigItem(ZkConfigConstants.APP_TYPE_DEFAULT, ZkConfigConstants.CONFIG_MODULE_USER_LOG, userLogConfigItem);
		
		ConfigItem dataSourceConfigItem = new ConfigItem(ZkConfigConstants.CONFIG_ITEM_DEFAULT);
		DataSourceModel dataSourceModel = new DataSourceModel();
		PoolConfig poolConfig = new PoolConfig();
		dataSourceModel.setAcquireIncrement(poolConfig.getAcquireIncrement() + "");
		dataSourceModel.setAcquireRetryAttempts(poolConfig.getAcquireRetryAttempts() + "");
		dataSourceModel.setAcquireRetryDelay(poolConfig.getAcquireRetryDelay() + "");
		dataSourceModel.setCheckoutTimeout(poolConfig.getCheckoutTimeout() + "");
		dataSourceModel.setDataSourceDesc("template");
		dataSourceModel.setDataSourceId("-1");
		dataSourceModel.setDataSourceName(ZkConfigConstants.CONFIG_ITEM_DEFAULT);
		dataSourceModel.setDbServiceName("MySQL");
		dataSourceModel.setIdleConnectionTestPeriod(1000L * 60 * 60 + "");
		dataSourceModel.setInitialPoolSize("5");
		dataSourceModel.setMaxPoolSize("10");
		dataSourceModel.setMinPoolSize("5");
		dataSourceConfigItem.setValue(dataSourceModel);
		manager.saveAppConfigItem(ZkConfigConstants.APP_TYPE_DEFAULT, ZkConfigConstants.CONFIG_MODULE_DATA_SOURCE, dataSourceConfigItem);
		
		ConfigItem variableConfigItem = new ConfigItem(ZkConfigConstants.CONFIG_ITEM_DEFAULT);
		VariableModel variableModel = new VariableModel();
		variableModel.setName(ZkConfigConstants.CONFIG_ITEM_DEFAULT);
		variableModel.setValue("value");
		variableModel.setValueType(VariableModel.DATA_TYPE_STRING);
		variableModel.setDesc("template");
		variableConfigItem.setValue(variableModel);
		manager.saveAppConfigItem(ZkConfigConstants.APP_TYPE_DEFAULT, ZkConfigConstants.CONFIG_MODULE_APP_VARIABLE, variableConfigItem);
	}
	*/

}
