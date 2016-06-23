/**
 * 
 */
package com.primeton.paas.app.config.handler;

import com.primeton.paas.app.config.AbstractConfigurationHandler;
import com.primeton.paas.app.config.ConfigModelManager;
import com.primeton.paas.app.config.Configuration;
import com.primeton.paas.app.config.Configuration.Group;
import com.primeton.paas.app.config.Configuration.Module;
import com.primeton.paas.app.config.model.SimulatorVariableSetModel;
import com.primeton.paas.app.config.model.VariableModel;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SimulatorVariableConfigHandler extends AbstractConfigurationHandler<SimulatorVariableSetModel> {

	public static final String NAME = "name";
	public static final String VALUE = "value";
	public static final String VALUE_TYPE = "valueType";
	public static final String DESC = "desc";
	
	/**
	 * 
	 * @param configs
	 */
	public SimulatorVariableConfigHandler(Configuration[] configs) {
		super(configs);
	}

	/**
	 * 
	 * @param fileLoactions
	 */
	public SimulatorVariableConfigHandler(String[] fileLoactions) {
		super(fileLoactions);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.config.AbstractConfigurationHandler#toModel(com.primeton.paas.app.config.Configuration.Module[])
	 */
	public SimulatorVariableSetModel toModel(Module[] modules) {
		Module module = new Module();
		for (Module md : modules) {
			module.mergeModule(md);
		}

		SimulatorVariableSetModel modelSet = new SimulatorVariableSetModel();
		for (String groupName : module.groupNames()) {
			Group group = module.getGroup(groupName);
			VariableModel model = new VariableModel();

			model.setName(groupName);
			model.setValue(group.getConfigValue(VALUE));
			model.setValueType(group.getConfigValue(VALUE_TYPE));
			model.setDesc(group.getConfigValue(DESC));

			modelSet.getVariableSet().put(groupName, model);
		}

		return modelSet;
	}
	
	public void toModule(SimulatorVariableSetModel modelSet, Module[] modules) {
	}
	
	public void toLoad(SimulatorVariableSetModel modelSet) {
		ConfigModelManager.setSimulatorVariableSetModel(modelSet);
	}

	public void toDeleteItems(SimulatorVariableSetModel modelSet, String[] itemIds) {
	}

	public void toUnload(SimulatorVariableSetModel modelSet) {
	}

}
