/**
 * 
 */
package com.primeton.paas.manage.api.config;

import com.primeton.paas.app.config.model.DataSourceModel;
import com.primeton.paas.manage.api.exception.ConfigureException;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IDataSourceConfigManager {

	/**
	 * 
	 * @param appName
	 * @return
	 */
	DataSourceConfig getDataSourceConfig(String appName);
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	DataSourceModel[] getAllDataSourceModels(String appName);
		
	/**
	 * 
	 * @param appName
	 * @param dataSourceName
	 * @return
	 */
	DataSourceModel getDataSourceModel(String appName, String dataSourceName);
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	String[] getDataSourceNames(String appName);
	
	/**
	 * 
	 * @param appName
	 * @param model
	 * @throws ConfigureException
	 */
	void updateDataSource(String appName, DataSourceModel model) throws ConfigureException;
	
	/**
	 * 
	 * @param appName
	 * @param model
	 * @throws ConfigureException
	 */
	void addDataSource(String appName, DataSourceModel model) throws ConfigureException;
	
	/**
	 * 
	 * @param appName
	 * @param dataSourceName
	 * @throws ConfigureException
	 */
	void removeDataSource(String appName, String dataSourceName) throws ConfigureException;
	
}
