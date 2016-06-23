package com.primeton.paas.manage.api.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.primeton.paas.app.config.model.DataSourceModel;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DataSourceConfig {

	private String appName;
	Map<String, DataSourceModel> dataSourceModels = new HashMap<String, DataSourceModel>();
	
	public DataSourceConfig() {
		super();
	}

	/**
	 * 
	 * @param appName
	 */
	public DataSourceConfig(String appName) {
		super();
		this.appName = appName;
	}

	/**
	 * 
	 * @param appName
	 * @param models
	 */
	public DataSourceConfig(String appName, List<DataSourceModel> models) {
		super();
		this.appName = appName;
		if(models != null && models.size() > 0) {
			for (DataSourceModel model : models) {
				this.dataSourceModels.put(model.getDataSourceName(), model);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getAppName() {
		return appName;
	}
	
	/**
	 * 
	 * @param appName
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	/**
	 * 
	 * @return
	 */
	public DataSourceModel[] getDataSourceModels() {
		return this.dataSourceModels.values().toArray(new DataSourceModel[0]);
	}
	
	/**
	 * 
	 * @param models
	 */
	public void setDataSourceModels(List<DataSourceModel> models) {
		this.dataSourceModels.clear();
		if(models != null && models.size() > 0) {
			for (DataSourceModel model : models) {
				this.dataSourceModels.put(model.getDataSourceName(), model);
			}
		}
	}
	
	/**
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public DataSourceModel getDataSourceModel(String dataSourceName) {
		return StringUtil.isEmpty(dataSourceName) ? null : this.dataSourceModels.get(dataSourceName);
	}
	
	/**
	 * 
	 * @param model
	 */
	public void addDataSourceModel(DataSourceModel model) {
		if(model != null) {
			this.dataSourceModels.put(model.getDataSourceName(), model);
		}
	}
	
	/**
	 * 
	 * @param dataSourceName
	 */
	public void removeDataSourceModel(String dataSourceName) {
		if(StringUtil.isEmpty(dataSourceName)) {
			return;
		}
		this.dataSourceModels.remove(dataSourceName);
	}
	
}
