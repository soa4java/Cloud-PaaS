/**
 * 
 */
package com.primeton.paas.app.config.handler;

import com.primeton.paas.app.config.AbstractConfigurationHandler;
import com.primeton.paas.app.config.ConfigModelManager;
import com.primeton.paas.app.config.Configuration;
import com.primeton.paas.app.config.C3P0ConfigNames;
import com.primeton.paas.app.config.Configuration.Group;
import com.primeton.paas.app.config.Configuration.Module;
import com.primeton.paas.app.config.model.SimulatorDataSourceModel;
import com.primeton.paas.app.config.model.SimulatorDataSourceSetModel;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SimulatorDataSourceConfigHandler extends AbstractConfigurationHandler<SimulatorDataSourceSetModel> {

	/**
	 * 
	 * @param configs
	 */
	public SimulatorDataSourceConfigHandler(Configuration[] configs) {
		super(configs);
	}

	/**
	 * 
	 * @param fileLoactions
	 */
	public SimulatorDataSourceConfigHandler(String[] fileLoactions) {
		super(fileLoactions);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.config.AbstractConfigurationHandler#toModel(com.primeton.paas.app.config.Configuration.Module[])
	 */
	public SimulatorDataSourceSetModel toModel(Module[] modules) {
		Module module = new Module();
		for (Module md : modules) {
			module.mergeModule(md);
		}

		SimulatorDataSourceSetModel modelSet = new SimulatorDataSourceSetModel();
		for (String groupName : module.groupNames()) {
			Group group = module.getGroup(groupName);
			SimulatorDataSourceModel model = new SimulatorDataSourceModel();

			model.setDataSourceName(groupName);
			model.setDriverClass(group.getConfigValue(C3P0ConfigNames.DRIVER_CLASS));
			model.setJdbcUrl(group.getConfigValue(C3P0ConfigNames.JDBC_URL));
			model.setUser(group.getConfigValue(C3P0ConfigNames.USER));
			model.setPassword(group.getConfigValue(C3P0ConfigNames.PASSWORD));
			model.setInitialPoolSize(group.getConfigValue(C3P0ConfigNames.INITIAL_POOL_SIZE));
			model.setMinPoolSize(group.getConfigValue(C3P0ConfigNames.MIN_POOL_SIZE));
			model.setMaxPoolSize(group.getConfigValue(C3P0ConfigNames.MAX_POOL_SIZE));
			model.setIdleConnectionTestPeriod(group.getConfigValue(C3P0ConfigNames.IDLE_CONNECTION_TEST_PERIOD));
			model.setAcquireRetryAttempts(group.getConfigValue(C3P0ConfigNames.ACQUIRE_RETRY_ATTEMPTS));
			model.setAcquireRetryDelay(group.getConfigValue(C3P0ConfigNames.ACQUIRE_RETRY_DELAY));
			model.setAcquireIncrement(group.getConfigValue(C3P0ConfigNames.ACQUIRE_INCREMENT));
			model.setCheckoutTimeout(group.getConfigValue(C3P0ConfigNames.CHECKOUT_TIMEOUT));
			model.setTestSQL(group.getConfigValue(C3P0ConfigNames.PREFERRED_TEST_QUERY));

			modelSet.addSimulatorDataSourceModel(model);
		}

		return modelSet;
	}
	
	public void toModule(SimulatorDataSourceSetModel modelSet, Module[] modules) {
	}
	
	public void toLoad(SimulatorDataSourceSetModel modelSet) {
		ConfigModelManager.setSimulatorDataSourceSetModel(modelSet);
	}

	public void toDeleteItems(SimulatorDataSourceSetModel modelSet, String[] itemIds) {
	}

	public void toUnload(SimulatorDataSourceSetModel modelSet) {
	}

}
