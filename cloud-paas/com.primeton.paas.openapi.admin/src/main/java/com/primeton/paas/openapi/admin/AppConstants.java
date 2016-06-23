/**
 * 
 */
package com.primeton.paas.openapi.admin;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class AppConstants {
	
	public static final String SYS_ENCODING = "UTF-8";
	
	public static final String CONTENT_TYPE = "text/html;charset=UTF-8";

	//	public final static String SERVER_MBEAN_DOMAIN_NAME="com.primeton.paas.;
	
	public final static String LOG_DIR_NAME = "logs";
	
	public static final String CONFIG_FILE_NAME_SYSTEM = "system-config.xml";
	
	public static final String CONFIG_FILE_NAME_USER = "user-config.xml";
	
	public static final String CONFIG_FILE_NAME_MSG_REDUCE= "msgreduce-config.xml";
	
	public static final String CONFIG_FILE_NAME_SERVICE_GRADE = "servicegrade-config.xml";
	
	public static final String LOG_CONFIG_FILE_NAME_SYSTEM = "log4j-system.xml";
	
	public static final String LOG_CONFIG_FILE_NAME_USER = "log4j-user.xml";
	
	public final static String DEFAULT_GROUP_NAME = "default";
	
	public static final String CONFIG_FILE_NAME_ZKCLIENT = "zkConfig.xml";
	
	public static final String ENV_INST_ID = "paas.instId";
	
	public static final String ENV_SRV_TYPE = "paas.srvType";
	
	public static final String ENV_APP_NAME = "paas.appName";

	public static final String ENV_IP = "paas.ip";

	public static final String ENV_HTTP_PORT = "paas.httpPort";

	public static final String ENV_CLUSTER_NAME = "paas.clusterName";

	public static final String ENV_INST_WORK_DIR = "paas.instWorkDir";
	
	public final static String ENV_LOG_DIR = "paas.logDir";
	
	public final static String ENV_JETTY_HOME = "jetty.home";
	
	public final static String ENV_FILE_ROOT_DIR = "paas.fileRootDir";
	
	public final static String SERVICE_TYPE_JETTY = "Jetty";
	
	public final static String SERVICE_TYPE_MYSQL = "MySQL";
	
	public final static String SERVICE_TYPE_MEMCACHED = "Memcached";
	
	public static final String ENV_RUN_MODE = "paas.runMode";

	public static final String RUN_MODE_DEVELOP = "develop";

	public static final String RUN_MODE_PRODUCT = "product";
	
	public static final int RUN_MODE_SIMULATOR = 1;

	public static final int RUN_MODE_CLOUD = 2;
	
	public static String CUST_ID = "custId";
	
	public static String BIZ_CODE = "bizCode";
	
	public static String BIZ_PARAMS = "bizParams";
	
}
