/**
 * 
 */
package com.primeton.paas.app.config.model;

import java.util.HashMap;
import java.util.Map;

import com.primeton.paas.app.config.IConfigModel;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SimulatorVariableSetModel implements IConfigModel {
	
	private static final long serialVersionUID = 5531718679832047802L;
	
	private Map<String, VariableModel> variableSet = new HashMap<String, VariableModel>();

	/**
	 * 
	 * @return
	 */
	public Map<String, VariableModel> getVariableSet() {
		return variableSet;
	}

	/**
	 * 
	 * @param variableSet
	 */
	public void setVariableSet(Map<String, VariableModel> variableSet) {
		this.variableSet = variableSet;
	}
	
}
