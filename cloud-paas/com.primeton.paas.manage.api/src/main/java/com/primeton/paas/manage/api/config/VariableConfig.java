/**
 * 
 */
package com.primeton.paas.manage.api.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.primeton.paas.app.config.model.VariableModel;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VariableConfig {

	private String appName;
	private Map<String, VariableModel> variableModels = new HashMap<String, VariableModel>();
	
	public VariableConfig() {
		super();
	}

	/**
	 * 
	 * @param appName
	 */
	public VariableConfig(String appName) {
		super();
		this.appName = appName;
	}

	/**
	 * 
	 * @param appName
	 * @param variableModels
	 */
	public VariableConfig(String appName, List<VariableModel> variableModels) {
		super();
		this.appName = appName;
		if(variableModels != null && variableModels.size() > 0) {
			for (VariableModel model : variableModels) {
				this.variableModels.put(model.getName(), model);
			}
		}
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the variableModels
	 */
	public VariableModel[] getVariableModels() {
		return this.variableModels.values().toArray(new VariableModel[0]);
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getVariableModelNames() {
		return this.variableModels.keySet().toArray(new String[0]);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public VariableModel getVariableModel(String name) {
		return StringUtil.isEmpty(name) ? null : this.variableModels.get(name);
	}

	/**
	 * @param variableModels the variableModels to set
	 */
	public void setVariableModels(List<VariableModel> variableModels) {
		this.variableModels.clear();
		if(variableModels != null) {
			for (VariableModel model : variableModels) {
				this.variableModels.put(model.getName(), model);
			}
		}
	}

	/**
	 * 
	 * @param model
	 */
	public void addVariableModel(VariableModel model) {
		if(model != null) {
			this.variableModels.put(model.getName(), model);
		}
	}
	
	/**
	 * ɾ����� <br>
	 * 
	 * @param name �������
	 */
	public void removeVariableModel(String name) {
		if(StringUtil.isEmpty(name)) {
			return;
		}
		this.variableModels.remove(name);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[").append(super.toString()).append("] ")
			.append("appName:" + this.appName)
			.append(", variableModels" + this.variableModels);
		
		return stringBuffer.toString();
	}
	
		
}