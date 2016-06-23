/**
 * 
 */
package com.primeton.paas.app.api.var;

import java.util.Map;

import com.primeton.paas.app.AppConstants;
import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.config.ConfigModelManager;
import com.primeton.paas.app.config.model.VariableModel;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public final class VariableCache {

	private VariableCache() {}
	
	/**
	 * 
	 * @return
	 */
	public static VariableModel[] getVariables() {
		if (AppConstants.RUN_MODE_CLOUD == ServerContext.getInstance().getRunMode()) {
			return ConfigModelManager.getVariableModels();
		} else {
			Map<String, VariableModel> variableSet = ConfigModelManager.getSimulatorVariableSetModel().getVariableSet();
			return variableSet.values().toArray(new VariableModel[0]);
		}
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static VariableModel getVariable(String name) {
		if (AppConstants.RUN_MODE_CLOUD == ServerContext.getInstance().getRunMode()) {
			return ConfigModelManager.getVariableModel(name);
		} else {
			Map<String, VariableModel> variableSet = ConfigModelManager.getSimulatorVariableSetModel().getVariableSet();
			return variableSet.get(name);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static String[] getVariableNames() {
		if (AppConstants.RUN_MODE_CLOUD == ServerContext.getInstance().getRunMode()) {
			return ConfigModelManager.getVariableModelNames();
		} else {
			Map<String, VariableModel> variableSet = ConfigModelManager.getSimulatorVariableSetModel().getVariableSet();
			return variableSet.keySet().toArray(new String[0]);
		}
	}
	
}
