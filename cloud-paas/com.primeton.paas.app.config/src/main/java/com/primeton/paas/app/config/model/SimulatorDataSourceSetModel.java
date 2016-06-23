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
public class SimulatorDataSourceSetModel implements IConfigModel {
	
	private static final long serialVersionUID = 5531718679832047802L;
	
	private Map<String, SimulatorDataSourceModel> dataSourceSet = new HashMap<String, SimulatorDataSourceModel>();

	/**
	 * 
	 * @return
	 */
	public Map<String, SimulatorDataSourceModel> getDataSourceSet() {
		return dataSourceSet;
	}

	public void setDataSourceSet(Map<String, SimulatorDataSourceModel> dataSourceSet) {
		if(dataSourceSet == null) {
			this.dataSourceSet.clear();
		} else {
			this.dataSourceSet = dataSourceSet;
		}
	}
	
	/**
	 * 
	 * @param model
	 */
	public void addSimulatorDataSourceModel(SimulatorDataSourceModel model) {
		if (model != null && model.getDataSourceName() != null
				&& model.getDataSourceName().trim().length() > 0) {
			this.dataSourceSet.put(model.getDataSourceName(), model);
		}
	}
	
	/**
	 * 
	 * @param dataSourceName
	 */
	public void removeSimulatorDataSourceModel(String dataSourceName) {
		if (dataSourceName != null && dataSourceName.trim().length() > 0) {
			this.dataSourceSet.remove(dataSourceName);
		}
	}
	
}
