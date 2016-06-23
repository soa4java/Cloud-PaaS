/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.manage.api.config;

import java.util.List;

import com.primeton.paas.app.config.eos.DasModel;
import com.primeton.paas.app.config.eos.DataSourceModel;
import com.primeton.paas.app.config.eos.HttpAccessModel;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IEosAppConfigManager {
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	List<DataSourceModel> getDataSources(String appName);
	
	/**
	 * 
	 * @param appName
	 * @param dsName
	 * @return
	 */
	DataSourceModel getDataSource(String appName, String dsName);
	
	/**
	 * 
	 * @param appName
	 * @param dsName
	 */
	void deleteDataSource(String appName, String dsName);
	
	/**
	 * 
	 * @param appName
	 * @param ds
	 */
	void saveDataSource(String appName, DataSourceModel ds);
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	DasModel getDasModel(String appName);
	
	/**
	 * 
	 * @param appName
	 * @param das
	 */
	void saveDasModel(String appName, DasModel das);
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	HttpAccessModel getHttpAccessModel(String appName);
	
	/**
	 * 
	 * @param appName
	 * @param model
	 */
	void saveHttpAccessModel(String appName, HttpAccessModel model);
	
}
