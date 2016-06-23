/**
 * 
 */
package com.primeton.paas.console.app.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.config.model.DataSourceModel;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.cluster.MySQLCluster;
import com.primeton.paas.manage.api.config.AppConfigManagerFactory;
import com.primeton.paas.manage.api.config.DataSourceConfig;
import com.primeton.paas.manage.api.config.IDataSourceConfigManager;
import com.primeton.paas.manage.api.exception.ConfigureException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.MySQLService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 应用数据源辅助类
 *
 * @author liming(li-ming@primeton.com)
 */
public class DataSourceUtil {

	private static ILogger logger = LoggerFactory.getLogger(DataSourceUtil.class);

	private static IDataSourceConfigManager dsConfigManager = AppConfigManagerFactory
			.getDataSourceConfigManager();

	private static IClusterManager mysqlManager = ClusterManagerFactory
			.getManager(MySQLService.TYPE);

	private static IServiceQuery srvQueryMgr = ServiceManagerFactory
			.getServiceQuery();
	
	/**
	 * 查询应用数据源配置 <br>
	 * 
	 * @param appName 应用名称
	 * @return 数据源列表
	 */
	public static List<DataSourceModel> queryDataSources(String appName) {
		List<DataSourceModel> dsList = new ArrayList<DataSourceModel>();
		if (StringUtil.isEmpty(appName)) {
			return dsList;
		}
		DataSourceConfig dsConfig = dsConfigManager.getDataSourceConfig(appName);
		DataSourceModel[] dsmodels = dsConfig==null ? null : dsConfig.getDataSourceModels();
		if (dsmodels == null || dsmodels.length < 1) {
			return dsList;
		}
		for (int i = 0; i < dsmodels.length; i++) {
			DataSourceModel model = dsmodels[i];
			String clusterId = model.getDataSourceId(); //mysql clusterId
			ICluster c =  mysqlManager.get(clusterId);
			if (c != null) {
				model.setDbServiceName(c.getName());
			}
		}
		return Arrays.asList(dsmodels);
	}
	
	/**
	 * 查询数据源配置信息 <br>
	 * @param appName  应用名称
	 * @param dataSourceName  数据源名称
	 * @return 数据源
	 */
	public static DataSourceModel queryDataSource(String appName, String dataSourceName) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(dataSourceName)) {
			return null;
		}
		DataSourceModel datasource = dsConfigManager.getDataSourceModel(appName, dataSourceName);
		return datasource;
	}
	
	/**
	 * 添加数据源 <br>
	 * @param appName  应用名称
	 * @param model  数据源配置
	 * @return  添加成功则返回true, 否则返回false
	 */
	public static boolean addAppDataSource(String appName, DataSourceModel model) {
		if (StringUtil.isEmpty(appName) || model == null || StringUtil.isEmpty(model.getDataSourceId())) {
			return false;
		}
		try {
			dsConfigManager.addDataSource(appName, model);
		} catch (ConfigureException e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 修改数据源 <br>
	 * @param appName  应用名称
	 * @param model  数据源
	 * @return  修改成功则返回true, 否则返回false
	 */
	public static boolean updateDataSource(String appName, DataSourceModel model) {
		if (appName == null || appName.length() == 0 || model == null) {
			return false;
		}
		try {
			dsConfigManager.updateDataSource(appName, model);
		} catch (ConfigureException e) {
			logger.error(e);
			return false;
		}
		return true;
	}
	
	/**
	 * 删除数据源 <br>
	 * @param appName  应用名称
	 * @param dataSourceName  数据源名称
	 * @return  删除成功则返回true, 否则返回false
	 */
	public static boolean removeDataSource(String appName, String dataSourceName) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(dataSourceName)) {
			return false;
		}
		try {
			dsConfigManager.removeDataSource(appName, dataSourceName);
		} catch (ConfigureException e) {
			logger.error(e);
			return false;
		}
		return true;
	}
	
	/**
	 * 检验数据源是否存在 <br>
	 * @param appName  应用名称
	 * @param dataSourceName  数据源名称
	 * @return  存在则返回true, 否则返回false
	 */
	public static boolean isExistDataSource(String appName, String dataSourceName) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(dataSourceName)) {
			return false;
		}
		DataSourceModel model = queryDataSource(appName,dataSourceName);
		return null != model;
	}
	
	/**
	 * Query my open db service <br>
	 * @param userID  云平台租户ID
	 * @return
	 */
	public static List<MySQLCluster> queryOpenDBService(String userID) {
		List<MySQLCluster> mysqlList = new ArrayList<MySQLCluster>();
		if (StringUtil.isEmpty(userID)) {
			return mysqlList;
		}
		try {
			List<MySQLCluster> clusters = mysqlManager.getByOwner(userID, MySQLCluster.TYPE, null, MySQLCluster.class);
			if (clusters != null && !clusters.isEmpty()) {
				for(MySQLCluster mysql : clusters){
					String clusterId = mysql.getId();
					List<MySQLService> services = srvQueryMgr.getByCluster(clusterId,MySQLService.class);
					if (services == null) {
						continue;
					}
					for (MySQLService s : services) {
						if (s.getState() == IService.STATE_RUNNING) {
							mysqlList.add(mysql);
							continue;
						}
					}
				}
			}
			return mysqlList;
		} catch (Exception e) {
			logger.error(e);
			return mysqlList;
		}
	}
	
	/**
	 * Query my db service <br>
	 * 
	 * @param userID  云平台租户ID
	 * @return
	 */
	public static List<MySQLCluster> queryAllDBService(String userID) {
		List<MySQLCluster> mysqlList = new ArrayList<MySQLCluster>();
		if (StringUtil.isEmpty(userID)) {
			return mysqlList;
		}
		try {
			List<MySQLCluster> clusters = mysqlManager.getByOwner(userID, MySQLCluster.TYPE, null, MySQLCluster.class);
			if (clusters != null && !clusters.isEmpty()) {
				for (MySQLCluster mysql : clusters) {
					String clusterId = mysql.getId();
					List<MySQLService> services = srvQueryMgr.getByCluster(clusterId,MySQLService.class);
					if (services == null) {
						continue;
					}
					mysqlList.add(mysql);
				}
			}
			return mysqlList;
		} catch (Exception e) {
			logger.error(e);
			return mysqlList;
		}
	}
	
}