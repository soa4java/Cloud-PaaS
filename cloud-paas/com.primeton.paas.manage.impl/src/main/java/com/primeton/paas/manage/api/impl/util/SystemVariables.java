/**
 * 
 */
package com.primeton.paas.manage.api.impl.util;

import java.util.Date;

import com.primeton.paas.manage.api.factory.HostTemplateManagerFactory;
import com.primeton.paas.manage.api.factory.VariableManagerFactory;
import com.primeton.paas.manage.api.manager.IHostTemplateManager;
import com.primeton.paas.manage.api.manager.IVariableManager;
import com.primeton.paas.manage.api.model.HostTemplate;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * PAAS 系统环境变量 <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class SystemVariables {
	
	private static IVariableManager variableManager = VariableManagerFactory
			.getManager();
	private static IHostTemplateManager templateManager = HostTemplateManagerFactory
			.getManager();

	public static final String SINGLE_SEPARATOR = "_";
	public static final String DOUBLE_SEPARATOR = "__";
	
	public static final String UNIX_FILE_SEPARATOR = "/";
	
	// App
	public static final String PAAS_DOMAIN_POSTFIX = "web_app_domain_postfix";
	
	// Common
	public static final String PAAS_HOME_PATH = "paas_home";
	public static final String WAIT_FOR_MESSAGE_TIME = "wait_for_message_time";
	
	// Jetty
	public static final String JETTY_STORAGE_PATH = "jetty_storage_path"; //storage/app
	public static final String MySQL_STORAGE_PATH = "mysql_storage_path"; // storage/db
	
	// Memcached
	public static final String MEMCACHED_MAX_CONNECTION_SIZE = "memcached_max_conn_size";
	public static final String MEMCACHED_SLICE_SIZE = "memcached_slice_size";
	
	// HaProxy
	public static final String HAPROXY_HEALTH_URL = "haproxy_health_url";
	public static final String HAPROXY_MAX_CONNECTION_SIZE = "haproxy_max_conn_size";
	public static final String HAPROXY_CONNECTION_TIMEOUT = "haproxy_conn_timeout";
	public static final String HAPROXY_PROTOCAL = "haproxy_protocal";
	
	// haproxy ssl certificate
	public static final String SSL_CERTIFICATE_PATH = "ssl_certificate_path";
	
	// war director
	private static final String PAAS_WAR_ROOT_PATH_KEY = "paas_war_root_path";
	
	// Log Collector director
	private static final String PAAS_LOG_ROOT_PATH_KEY = "paas_log_root_path";
	
	public static final String APP_MONITOR_INTERVAL = "app_monitor_interval";
	
	public static final String SERVICE_MONITOR_INTERVAL = "service_monitor_interval";
	
	public static final String KEEP_LAST_TIME_DATA = "keep_last_time_data"; // unit:m
	
	// HTTP Software Repository
	public static final String HTTP_REPOS_URL = "http_repos_url";
	
	// VM
	public static final String VM_DESTROY_TIMEOUT = "vm_destroy_timeout";
	public static final String VM_UPGRADE_TIMEOUT = "vm_upgrade_timeout";
	
	// Storage
	public static final String STORAGE_DESTROY_TIMEOUT = "storage_destroy_timeout";
	
	/**
	 * if create service exception
	 */
	private static final String CLEAN_AFTER_PROCESS_ERROR = "clean_after_process_error";
	
	private static final String NGINX_DEFAULT_NAME = "nginx_default_name";
	
	private static final String DEFAULT_APPUSER_ROLE = "default_appuser_role";
	
	/**
	 * Enable IAAS VM WebService
	 */
	public static final String IAAS_ENABLE_VM = "iaas_enable_vm";
	
	/**
	 * Enable IAAS Storage WebService
	 */
	public static final String IAAS_ENABLE_STORAGE = "iaas_enable_storage";
	
	/**
	 * Default. <br>
	 */
	private SystemVariables() {
		super();
	}

	/**
	 * 
	 * @param serviceType
	 * @param scriptName
	 * @return
	 */
	public static String getScriptPath(String serviceType, String scriptName) {
		return getBinHome() + UNIX_FILE_SEPARATOR + serviceType
				+ UNIX_FILE_SEPARATOR + "bin" + UNIX_FILE_SEPARATOR + scriptName; //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	protected static IVariableManager getManager() {
		return variableManager = (null == variableManager) ? VariableManagerFactory.getManager() : variableManager;
	}

	/**
	 * 
	 * @return
	 */
	public static IHostTemplateManager getTemplateManager() {
		return templateManager = (null == templateManager) ? HostTemplateManagerFactory.getManager() : templateManager;
	}

	/**
	 * 
	 * @return
	 */
	public static String getDomainPostfix() {
		return getManager().getStringValue(PAAS_DOMAIN_POSTFIX, ".paas.primeton.com");
	}
	
	/**
	 * 
	 * @return PAAS Home
	 */
	public static String getHome() {
		String path = getManager().getStringValue(PAAS_HOME_PATH, "/primeton/paas"); //$NON-NLS-1$
		if (path != null
				&& (path.charAt(path.length() - 1) == '/' || path.charAt(path
						.length() - 1) == '\\')) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getBinHome() {
		return getHome() + "/bin"; //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getTempHome() {
		return getHome() + "/temp"; //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getProgramsHome() {
		return getHome() + "/programs"; //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getWorkspaceHome() {
		return getHome() + "/workspace"; //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getTemplatesHome() {
		return getHome() + "/templates"; //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public static long getMaxWaitMessageTime() {
		return getManager().getLongValue(WAIT_FOR_MESSAGE_TIME, 1000L * 60 * 5); // 5m
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getJettyStoragePath() {
		return getManager().getStringValue(JETTY_STORAGE_PATH, "/storage/app"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getMySQLStoragePath() {
		return getManager().getStringValue(MySQL_STORAGE_PATH, "/storage/db"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getHaproxyHealthUrl(){
		return getManager().getStringValue(HAPROXY_HEALTH_URL, "/"); //$NON-NLS-1$
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static int getHaproxyMaxConnSize(){
		return getManager().getIntValue(HAPROXY_MAX_CONNECTION_SIZE, 50); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public static long getHaproxyConnTimeout(){
		return getManager().getLongValue(HAPROXY_CONNECTION_TIMEOUT, 3600); //$NON-NLS-1$
	}
	

	/**
	 * 
	 * @return
	 */
	public static String getHaproxyProtocal() {
		return getManager().getStringValue(HAPROXY_PROTOCAL, "http"); //$NON-NLS-1$
	}
	

	/**
	 * 
	 * @return
	 */
	public static int getMemcachedMaxConnSize() {
		return getManager().getIntValue(MEMCACHED_MAX_CONNECTION_SIZE, 1024); //$NON-NLS-1$
	}

	/**
	 * 
	 * @return
	 */
	public static int getMemcachedSlice() {
		return getManager().getIntValue(MEMCACHED_SLICE_SIZE, 8); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param packageId
	 * @return
	 */
	public static int getJvmMaxMemorySize(String packageId) {
		int memorySize = getMemorySize(packageId); // GB
		return memorySize < 1 ? 256 : memorySize * 256;
	}

	/**
	 * 
	 * @param packageId
	 * @return
	 */
	public static int getJvmMinMemorySize(String packageId) {
		int memorySize = getMemorySize(packageId); // GB
		return memorySize < 1 ? 128: memorySize * 128;
	}
	
	/**
	 * 
	 * @param packageId
	 * @return
	 */
	public static int getJvmMaxPermSize(String packageId) {
		int memorySize = getMemorySize(packageId); // GB
		return memorySize < 1 ? 128 : memorySize * 128;
	}
	
	/**
	 * 
	 * @param packageId
	 * @return
	 */
	public static int getJvmMinPermSize(String packageId) {
		int memorySize = getMemorySize(packageId); // GB
		return memorySize < 1 ? 64 : memorySize * 64;
	}
	
	/**
	 * GB. <br>
	 * 
	 * @param packageId
	 * @return
	 */
	public static int getMemorySize(String packageId) {
		if (StringUtil.isEmpty(packageId)) {
			return 1; 
		}
		HostTemplate template = getTemplateManager().getTemplate(packageId);
		if (null == template) {
			return 1;
		}
		int memory = 0;
		if (HostTemplate.UNIT_TB.equalsIgnoreCase(template.getUnit())) {
			return template.getMemory() * 1024;
		} else if (HostTemplate.UNIT_GB.equalsIgnoreCase(template.getUnit())) {
			return template.getMemory();
		} else if (HostTemplate.UNIT_MB.equalsIgnoreCase(template.getUnit())) {
			memory = template.getMemory()/1024;
		} else if (HostTemplate.UNIT_KB.equalsIgnoreCase(template.getUnit())) {
			memory = template.getMemory()/1024/1024;
		} else if (HostTemplate.UNIT_BYTE.equalsIgnoreCase(template.getUnit())) {
			memory = template.getMemory()/1024/1024/1024;
		}
		return memory <= 0 ? 1 : memory;
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getIntValue(String key, int defaultValue) {
		return getManager().getIntValue(key, defaultValue);
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static long getLongValue(String key, long defaultValue) {
		return getManager().getLongValue(key, defaultValue);
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getStringValue(String key, String defaultValue) {
		return getManager().getStringValue(key, defaultValue);
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static byte getByteValue(String key, byte defaultValue) {
		return getManager().getByteValue(key, defaultValue);
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Date getDateValue(String key, Date defaultValue) {
		return getManager().getDateValue(key, defaultValue);
	}
	
	/**
	 * 
	 * @param key
	 * @param reg
	 * @param defaultValue
	 * @return
	 */
	public static Date getDateValue(String key, String reg, Date defaultValue) {
		return getManager().getDateValue(key, reg, defaultValue);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static String getLogRootPath(){
		return  getManager().getStringValue(PAAS_LOG_ROOT_PATH_KEY, getWorkspaceHome() + "/application"); 
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getWarRootPath(){
		return getManager().getStringValue(PAAS_WAR_ROOT_PATH_KEY, "");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getHttpRpository() {
		return getManager().getStringValue(HTTP_REPOS_URL, null);
	}
	
	/**
	 * 
	 * @return
	 */
	public static long getAppMonitorInterval() {
		return getLongValue(APP_MONITOR_INTERVAL, 10L);
	}
	
	/**
	 * 
	 * @return
	 */
	public static long getServiceMonitorInterval() {
		return getLongValue(SERVICE_MONITOR_INTERVAL, 60L);
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getKeepLastTimeData() {
		return getIntValue(KEEP_LAST_TIME_DATA, 60);
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isCleanAfterProcessError() {
		return getManager().getBoolValue(CLEAN_AFTER_PROCESS_ERROR, true);
	}

	/**
	 * 
	 * @return
	 */
	public static String getDefaultNginxName() {
		return getManager().getStringValue(NGINX_DEFAULT_NAME, "");
	}
	
	/**
	 * 
	 * @return
	 */
	public static long getVmDestroyTimeout() {
		return getManager().getLongValue(VM_DESTROY_TIMEOUT, 300000);
	}
	
	/**
	 * 
	 * @return
	 */
	public static long getVmUpgradeTimeout() {
		return getManager().getLongValue(VM_UPGRADE_TIMEOUT, 300000);
	}
	
	/**
	 * 
	 * @return
	 */
	public static long getStorageDestroyTimeout() {
		return getManager().getLongValue(STORAGE_DESTROY_TIMEOUT, 300000);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getSslCertificatePath(){
		return getManager().getStringValue(SSL_CERTIFICATE_PATH, "");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDefaultAppUserRole(){
		return getManager().getStringValue(DEFAULT_APPUSER_ROLE, "appuser"); //$NON-NLS-1$
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isIaasEnableVm() {
		return getManager().getBoolValue(IAAS_ENABLE_VM, false);
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isIaasEnableStorage() {
		return getManager().getBoolValue(IAAS_ENABLE_STORAGE, false);
	}
	
}
