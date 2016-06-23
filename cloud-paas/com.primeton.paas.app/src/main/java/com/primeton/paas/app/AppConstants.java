/**
 * 
 */
package com.primeton.paas.app;

import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.app.config.model.IServiceConstants;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface AppConstants extends IServiceConstants {
	
	public static final String SYS_ENCODING = "UTF-8";

	public final static String LOG_DIR_NAME = "logs";
	
	public static final String CONFIG_FILE_NAME_SYSTEM = "system-config.xml";
	
	public static final String CONFIG_FILE_NAME_USER = "user-config.xml";
	
	public static final String CONFIG_FILE_NAME_SIMULATOR = "simulator-config.xml";
	
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

	public static final String ENV_WORK_DIR = "paas.workDir";
	
	public final static String ENV_LOG_DIR = "paas.logDir";
	
	public final static String ENV_SERVER_HOME = "server.home";
	
	public final static String ENV_FILE_ROOT_DIR = "paas.fileRootDir";
	
	/**
	 * {@link SystemProperties#RUN_MODE_PRODUCT}
	 * {@link SystemProperties#RUN_MODE_DEVELOP}
	 * {@link SystemProperties#RUN_MODE_SIMULATOR}
	 */
	public static final String ENV_RUN_MODE = "paas.runMode";

	/**
	 * RUN MODE : Development. <br>
	 */
	public static final String RUN_MODE_DEVELOP = "develop";

	/**
	 * RUN MODE : Product. <br>
	 */
	public static final String RUN_MODE_PRODUCT = "product";
	
	/**
	 * RUN MODE : Simulator. <br>
	 */
	public static final int RUN_MODE_SIMULATOR = 1;

	/**
	 * RUN MODE : Cloud. <br>
	 */
	public static final int RUN_MODE_CLOUD = 2;
	
	
}
