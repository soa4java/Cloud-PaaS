/**
 * 
 */
package com.primeton.paas.app.config;

/**
 * ZooKeeper上应用配置模块常量. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface ZkConstants {
	
	/**
	 * 默认应用类型. <br>
	 */
	public final static String APP_TYPE_DEFAULT = "default";
	
	/**
	 * 配置模块:线程池. <br>
	 */
	public final static String CONFIG_MODULE_THREAD_POOL = "threadPool";
	
	/**
	 * 配置模块:数据源. <br>
	 */
	public final static String CONFIG_MODULE_DATA_SOURCE = "dataSource";
	
	/**
	 * 配置模块:应用绑定的服务(可以调用的服务). <br>
	 */
	public final static String CONFIG_MODULE_SERVICE_SOURCE = "serviceSource";
	
	/**
	 * 配置模块:应用变量. <br>
	 */
	public final static String CONFIG_MODULE_APP_VARIABLE = "variable";
	
	/**
	 * 配置模块:系统日志. <br>
	 */
	public final static String CONFIG_MODULE_SYSTEM_LOG = "systemLog";
	
	/**
	 * 配置模块:用户日志. <br>
	 */
	public final static String CONFIG_MODULE_USER_LOG = "userLog";
	
	/**
	 * 配置模块:EOS数据源. <br>
	 */
	public final static String CONFIG_MODULE_EOS_DATASOURCE = "eosDataSource";
	
	/**
	 * 配置模块:EOS DAS. <br>
	 */
	public final static String CONFIG_MODULE_EOS_DAS = "eosDAS";
	
	/**
	 * 配置模块:EOS HTTP Access. <br>
	 */
	public final static String CONFIG_MODULE_EOS_HTTP_ACCESS = "eosHttpAccess";
	
	/**
	 * 默认配置项名称. <br>
	 */
	public final static String CONFIG_ITEM_DEFAULT = "default";
	
}
