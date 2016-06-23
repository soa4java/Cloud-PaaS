/**
 * 
 */
package com.primeton.paas.manage.api.config;

import com.primeton.paas.app.config.model.VariableModel;
import com.primeton.paas.manage.api.exception.ConfigureException;

/**
 * 应用变量配置管理器. <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IVariableConfigManager {

	/**
	 * 
	 * @param appName
	 * @return
	 */
	VariableConfig getVariableConfig(String appName);
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	VariableModel[] getAllVariableModels(String appName);
	
	/**
	 * 
	 * @param appName
	 * @param varName
	 * @return
	 */
	VariableModel getVariableModel(String appName, String varName);
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	String[] getVariableNames(String appName);
	
	/**
	 * 
	 * @param appName
	 * @param var
	 * @throws ConfigureException
	 */
	void addVariableModel(String appName, VariableModel var) throws ConfigureException;
	
	/**
	 * 
	 * @param appName
	 * @param var
	 * @throws ConfigureException
	 */
	void updateVariableModel(String appName, VariableModel var) throws ConfigureException;
	
	/**
	 * 
	 * @param appName
	 * @param varName
	 * @throws ConfigureException
	 */
	void removeVariableModel(String appName, String varName) throws ConfigureException;
	
}
