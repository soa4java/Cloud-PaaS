/**
 * 
 */
package com.primeton.paas.console.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.factory.HostTemplateManagerFactory;
import com.primeton.paas.manage.api.factory.VariableManagerFactory;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IHostTemplateManager;
import com.primeton.paas.manage.api.manager.IVariableManager;
import com.primeton.paas.manage.api.model.HostTemplate;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.TomcatService;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SystemVariable {
	
	private static IVariableManager varManager = VariableManagerFactory
			.getManager();

	private static IHostTemplateManager templateManager = HostTemplateManagerFactory
			.getManager();
	
	private static final String PAAS_WAR_TEMP_ROOT_PATH = "paas_war_temp_root_path";
	
	// cpu-monitor & memory-monitor
	private static final String DISPALY_MONITOR_DATA_INTERVAL = "dispaly_monitor_data_interval";
	
	private static final String DISPLAY_MONITOR_DATA_LENGTH = "dispaly_monitor_data_length";
	
	public static final String NAS_STORAGE_SIZE_KEY = "nas_storage_size";
	
	public static final String APP_CACHE_SIZE_KEY = "app_cache_size";
	
	public static final String APP_CACHE_MAXCONN_SIZE_KEY = "app_cache_maxConnectionSize";
	
	private static final String APP_SERVER_CLUSTER_MAX_NUM_KEY = "app_server_cluster_max_num";
	
	private static final String MYSQL_CHARACTERSETS = "mysql_characterSets";//mysql字符集种类
	
	// haproxy ssl certificate
	public static final String SSL_CERTIFICATE_TEMP_PATH = "ssl_certificate_temp_path";
	
	public static final String SSL_CERTIFICATE_PATH = "ssl_certificate_path";
	
	private static final String SERVER_CLUSTER_MAX_NUM_KEY = "_server_cluster_max_num";

	private static final String MAX_MAILWORKER_NUM_KEY = "maxMailWorkerNum"; // default
	
	/**
	 * Haproxy服务创建
	 */
	private static final String HAPROXY_REL_SERVICE_TYPE = "haproxy_rel_srvType"; // 可关联服务类型定义
	private static final String HAPROXY_POLICY_CONFIG = "haproxy_policy"; // haproxy
	
	/**
	 * Nginx 服务创建
	 */
	public static final String NGINX_WORKER_PROCESSES = "workerProcesses";// Nginx工作线程个数
	public static final String NGINX_WORKER_CONNECTIONS = "workerConnections"; // Nginx工作线程连接池大小
	public static final String NGINX_KEEPALIVE_TIMEOUT = "keepaliveTimeout";// keepalive长连接超时时间

	/***
	 * 伸缩
	 */
	private static final String SERVICE_STRETCH_TIMEOUT = "service_stretch_timeout";// 服务伸缩超时时间
	
	private static final String PHP_MY_ADMIN_URL_KEY = "phpmyadmin_url";
	
	public static final String MONITOR_REFRESHT_TIME_KEY = "monitor_refresh_times";
	
	public static final String MONITOR_TIME_LENGTHS_KEY = "monitor_time_lengths"; 
	
	public static final String CEP_ONEMINUTESAGO_KEY = "monitor_time_ago";
	
	public static final String CEP_MONITORINTERVAL_KEY = "app_monitor_interval";
	
	public static final String STRATEGY_DURATION_KEY = "strategy_duration_opts"; // 持续时间 （分钟）
	
	public static final String CPU_USAGE_KEY = "cpu_usage_opts"; // CPU ：使用率（%）

	public static final String MEM_USAGE_KEY = "memory_usage_opts"; // Memory ：使用率（%）

	public static final String NET_TRAFFIC_KEY = "network_traffic_opts"; // Network ：网络流量 （MB/s）

	public static final String IO_RATE_KEY = "io_rate_opts"; // IO ：读写速率（MB/s）

	public static final String LOAD_BALANCE_KEY = "lb_balance_opts"; // 负载

	public static final String STRETCH_SCALE_KEY = "stretch_scale_opts"; // 伸缩幅度（台）

	public static final String IGNORE_TIME_KEY = "ignore_time_opts"; // 伸缩后的休眠时间

	public static final String ALL_OPTS_KEY = "strategy_opts"; // 一次获取所有配置 
	
	// 资源下限大小配置
	public static final String VM_POOL_MIN_SIZE = "vm_pool_min_size";
	// 资源上限大小配置
	public static final String VM_POOL_MAX_SIZE = "vm_pool_max_size";
	// 增长步长大小配置
	public static final String VM_POOL_INCREASE_SIZE = "vm_pool_increase_size";
	// 缩减步长大小配置
	public static final String VM_POOL_DECREASE_SIZE = "vm_pool_decrease_size";
	// 轮询时间间隔配置,单位:秒
	public static final String VM_POOL_TIME_INTERVAL = "vm_pool_time_interval";
	// 虚拟机操作超时时间(默认10分钟)
	public static final String VM_OPERATE_TIMEOUT = "vm_operate_timeout";
	
	public static final String NAS_STORAGE_SIZE = "nas_storage_size";
	// 资源下限大小配置
	public static final String STORAGE_POOL_MIN_SIZE = "storage_pool_min_size";
	// 资源上限大小配置
	public static final String STORAGE_POOL_MAX_SIZE = "storage_pool_max_size";
	// 增长步长大小配置
	public static final String STORAGE_POOL_INCREASE_SIZE = "storage_pool_increase_size";
	// 缩减步长大小配置
	public static final String STORAGE_POOL_DECREASE_SIZE = "storage_pool_decrease_size";
	// 轮询时间间隔配置,单位:秒
	public static final String STORAGE_POOL_TIME_INTERVAL = "storage_pool_time_interval";
	//
	public static final String STORAGE_OPERATE_TIMEOUT = "storage_operate_timeout";
	
	/**
	 * Hadoop 管理门户地址. <br>
	 */
	private static final String HADOOP_MANAGEMENT_PROTAL = "hadoop_management_protal";
	
	/**
	 * Hadoop 管理门户用户. <br>
	 */
	private static final String HADOOP_MANAGEMENT_USER = "hadoop_management_user";
	
	/**
	 * Hadoop 管理门户用户密码. <br>
	 */
	private static final String HADOOP_MANAGEMENT_PASSWORD = "hadoop_management_password";
	
	/**
	 * key
	 */
	public static final String CONSOLE_TYPE_KEY = "CONSOLE_TYPE";
	
	/**
	 * platform
	 */
	public static final String CONSOLE_TYPE_PLATFORM = "platform";
	
	/**
	 * application
	 */
	public static final String CONSOLE_TYPE_APPLICATION = "app";
	
	/**
	 * 
	 * @return
	 */
	public static String getConsoleType() {
		return System.getProperty(CONSOLE_TYPE_KEY, CONSOLE_TYPE_PLATFORM);
	}
	
	/**
	 * 获取应用war保存目录 <br>
	 * 
	 * @return
	 */
	public static String getWarTempRootPath(){
		return varManager.getStringValue(PAAS_WAR_TEMP_ROOT_PATH, SystemVariables.getTempHome() + "/upload/"); //$NON-NLS-1$
	}
	
	/**
	 * Default 20  <br>
	 * 
	 * @return
	 */
	public static int getMonitorDataLength() {
		return varManager.getIntValue(DISPLAY_MONITOR_DATA_LENGTH, 20);
	}
	
	/**
	 * Default 20 minutes <br>
	 * @return
	 */
	public static long getMonitorTimeInterval() {
		return varManager.getLongValue(DISPALY_MONITOR_DATA_INTERVAL,  20L * 60L * 1000L);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Integer[] getAppServerSizes() {
		int max = getAppServerClusterMaxNum();
		List<Integer> nums = new ArrayList<Integer>();
		for (int i = 1; i <= max; i++) {
			nums.add(i);
		}
		return nums.toArray(new Integer[nums.size()]);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getAppServerSizesJSON() {
		return array2JSON(getAppServerSizes());
	}
	
	/**
	 * 
	 * @return
	 */
	public static Integer[] getCacheSizes() {
		return getCacheSizeArray();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getCacheSizesJSON() {
		return array2JSON(getCacheSizeArray());
	}
	
	/**
	 * 
	 * @return
	 */
	public static Integer[] getCacheMaxConnSizes() {
		return getCacheMaxConnSizeArray();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getCacheMaxConnSizesJSON() {
		return array2JSON(getCacheMaxConnSizes());
	}
	
	/**
	 * 
	 * @return
	 */
	public static Integer[] getNasStorageSizes() {
		return getNasStorageSizeArray();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getNasStorageSizesJSON() {
		return array2JSON(getNasStorageSizeArray());
	}
	
	/**
	 * 
	 * @return
	 */
	public static String[] getServerTypes() {
		String[] serverTypes = new String[2];
		serverTypes[1] = TomcatService.TYPE;
		serverTypes[0] = JettyService.TYPE;
		return serverTypes;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getServerTypesJSON() {
		return array2JSON(getServerTypes());
	}
	
	/**
	 * 获取Haproxy支持的 SSL认证类型 <br>
	 * 
	 * @return
	 */
	public static String[] getSslProtocalTypes(){
		String[] protocals = new String[5];
		protocals[0] = HaProxyCluster.PROTOCOL_HTTP;
		protocals[1] = HaProxyCluster.PROTOCOL_HTTPS;
		protocals[2] = HaProxyCluster.PROTOCOL_HTTP_HTTPS;
		protocals[3] = HaProxyCluster.PROTOCOL_MUTUAL_HTTPS;//双向ssl https认证
		protocals[4] = HaProxyCluster.PROTOCOL_MUTUAL_HTTP_HTTPS;//双向ssl http+https 认证
		return protocals;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getSslProtocalTypesJSON() {
		String[] protocals = getSslProtocalTypes();
		List<Object> list = new ArrayList<Object>();
		for (String protocal : protocals) {
			Map<String, String> map = new HashMap<String, String>();
			String id = protocal;
			String text = protocal;
			if (HaProxyCluster.PROTOCOL_HTTPS.equalsIgnoreCase(protocal)
					|| HaProxyCluster.PROTOCOL_HTTP_HTTPS.equalsIgnoreCase(protocal)) {
				text = text + " (单向)";
			}
			if (HaProxyCluster.PROTOCOL_MUTUAL_HTTPS.equalsIgnoreCase(protocal)
					|| HaProxyCluster.PROTOCOL_MUTUAL_HTTP_HTTPS.equalsIgnoreCase(protocal)) {
				text = text + " (双向)";
			}
			map.put("id", id); //$NON-NLS-1$
			map.put("text", text); //$NON-NLS-1$
			list.add(map);
		}
		return JSONArray.fromObject(list).toString();
	}
	
	/**
	 * 获取缓存大小配置<br>
	 * 
	 * @return
	 */
	public static List<Integer> getCacheSize() {
		List<Integer> cacheSize = new ArrayList<Integer>();
		String[] sizes = varManager.getStringValue(APP_CACHE_SIZE_KEY, "8,16,24,32,64,128,256,512,1024,2048,4096").split(",");
		for (String str : sizes) {
			try {
				Integer size = Integer.parseInt(str);
				cacheSize.add(size);
			} catch (NumberFormatException e) {
				// ignore
			}
		}
		return cacheSize;
	}
	
	/**
	 * 获取缓存最大连接数配置<br>
	 * 
	 * @return
	 */
	public static List<Integer> getCacheMaxConnSize(){
		List<Integer> cacheSize = new ArrayList<Integer>();
		String[] sizes = varManager.getStringValue(APP_CACHE_MAXCONN_SIZE_KEY, "50,100,150,200,250,300,400,500,600,700,800,900,1000,1500,2000").split(",");
		for (String str : sizes) {
			try {
				Integer size = Integer.parseInt(str);
				cacheSize.add(size);
			} catch (NumberFormatException e) {
				// ignore
			}
		}
		return cacheSize;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Integer[] getCacheSizeArray() {
		List<Integer> cacheSize = getCacheSize();
		Integer[] array = new Integer[cacheSize.size()];
		for (int i=0; i<cacheSize.size(); i++) {
			array[i] = cacheSize.get(i);
		}
		return array;
	}
	
	public static Integer[] getCacheMaxConnSizeArray(){
		List<Integer> cacheMaxConnSize = getCacheMaxConnSize();
		Integer[] array = new Integer[cacheMaxConnSize.size()];
		for (int i=0; i<cacheMaxConnSize.size(); i++) {
			array[i] = cacheMaxConnSize.get(i);
		}
		return array;
	}
	
	/**
	 * 获取存储大小配置<br>
	 * 
	 * @return
	 */
	public static List<Integer> getNasStorageSize() {
		List<Integer> nasStorageSize = new ArrayList<Integer>();
		String[] sizes = varManager.getStringValue(NAS_STORAGE_SIZE_KEY,
				"2,4,8,16,32,64,128,256").split(",");
		for (String str : sizes) {
			try {
				Integer size = Integer.parseInt(str);
				nasStorageSize.add(size);
			} catch (NumberFormatException e) {
				// ignore
			}
		}
		return nasStorageSize;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Integer[] getNasStorageSizeArray() {
		List<Integer> nasStorageSize = getNasStorageSize();
		if (nasStorageSize == null || nasStorageSize.isEmpty()) {
			return new Integer[0];
		}
		Integer[] array = new Integer[nasStorageSize.size()];
		for (int i=0; i<nasStorageSize.size(); i++) {
			array[i] = nasStorageSize.get(i);
		}
		return array;
	}

	/**
	 * 获取应用服务器集群最大数限制 <br>
	 * 
	 * @return
	 */
	public static int getAppServerClusterMaxNum() {
		return varManager.getIntValue(APP_SERVER_CLUSTER_MAX_NUM_KEY, 10); //$NON-NLS-1$
	}

	/**
	 * 
	 * @return
	 */
	public static String[] getMySQLCharacterSets() {
		return varManager.getStringValue(MYSQL_CHARACTERSETS, "").split(",");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getMySQLCharacterSetsJSON() {
		return array2JSON(getMySQLCharacterSets());
	}

	/**
	 * 
	 * @return
	 */
	public static String getSslCertificatePath() {
		return varManager.getStringValue(SSL_CERTIFICATE_PATH, "");
	}

	/**
	 * 
	 * @return
	 */
	public static String getSslCertificateTempPath() {
		return varManager.getStringValue(SSL_CERTIFICATE_TEMP_PATH, "");
	}

	/**
	 * 获取所有主机套餐 <br>
	 * 
	 * @return
	 */
	public static List<HostTemplate> getHostTemplates() {
		return templateManager.getTemplates(null);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getHostTemplatesJSON() {
		List<HostTemplate> templates = templateManager.getTemplates(null);
		templates = null == templates ? new ArrayList<HostTemplate>()
				: templates;
		List<Object> list = new ArrayList<Object>();
		for (HostTemplate template : templates) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", template.getTemplateId()); //$NON-NLS-1$
			map.put("text", //$NON-NLS-1$
					template.getTemplateName() + " (" + template.getCpu() + "C/"
							+ template.getMemory() + template.getUnit() + ")");
			list.add(map);
		}
		return JSONArray.fromObject(list).toString();
	}

	/**
	 * 获取服务实例最大数<br>
	 * 
	 * @param type
	 * @return
	 */
	public static Integer[] getServiceInstNum(String type) {
		int max = loadMaxServiceInstNum(type);
		List<Integer> nums = new ArrayList<Integer>();
		for (int i = 1; i <= max; i++) {
			nums.add(i);
		}
		return nums.toArray(new Integer[nums.size()]);
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static String getServiceInstNumJSON(String type) {
		return array2JSON(getServiceInstNum(type));
	}

	/**
	 * @param type
	 * @return
	 */
	public static int loadMaxServiceInstNum(String type) {
		return varManager.getIntValue(type.toLowerCase()
				+ SERVER_CLUSTER_MAX_NUM_KEY, 3);
	}

	/**
	 * @return
	 */
	public static String[] getHaproxyPolicy() {
		return varManager.getStringValue(HAPROXY_POLICY_CONFIG,
				"roundrobin,static-rr,source,leastconn,uri,uri-param")
				.split(",");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getHaproxyPolicyJSON() {
		return array2JSON(getHaproxyPolicy());
	}
	
	/**
	 * 
	 * @return
	 */
	public static String[] getHaproxyProtocols() {
		return new String[] {"http", "tcp"}; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getHaproxyProtocolsJSON() {
		return array2JSON(getHaproxyProtocols());
	}

	/**
	 * @return
	 */
	public static String getHaproxyRelSrvType() {
		String types = "Jetty,Tomcat,OpenAPI,SMS";
		return varManager.getStringValue(HAPROXY_REL_SERVICE_TYPE, types);
	}

	/**
	 * @return
	 */
	public static Integer[] getMaxMailWorkerNums() {
		int max = varManager.getIntValue(MAX_MAILWORKER_NUM_KEY, 10);
		List<Integer> nums = new ArrayList<Integer>();
		for (int i = 1; i <= max; i++) {
			nums.add(i);
		}
		return nums.toArray(new Integer[nums.size()]);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getMaxMailWorkerNumsJSON() {
		return array2JSON(getMaxMailWorkerNums());
	}

	/**
	 * 
	 * @return
	 */
	public static long getServiceStretchTimeout() {
		return varManager.getLongValue(SERVICE_STRETCH_TIMEOUT, 12000L);
	}

	/**
	 * 
	 * @return
	 */
	public static Integer[] getNginxProcesses() {
		int max = varManager.getIntValue(NGINX_WORKER_PROCESSES, 10);
		List<Integer> nums = new ArrayList<Integer>();
		for (int i = 1; i <= max; i++) {
			nums.add(i);
		}
		return nums.toArray(new Integer[nums.size()]);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getNginxProcessesJSON() {
		return array2JSON(getNginxProcesses());
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getPhpMyAdminURL() {
		return varManager.getStringValue(PHP_MY_ADMIN_URL_KEY, null);
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getMaxRedisSize() {
		return 20;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getMaxRedisSizeJSON() {
		return array2JSON(getRedisSizeArray());
	}
	
	/**
	 * 
	 * @return
	 */
	public static Integer[] getRedisSizeArray() {
		int maxSize = getMaxRedisSize();
		maxSize = maxSize >= 1 ? maxSize : 5;
		Integer[] array = new Integer[maxSize];
		for (int i=1; i<=maxSize; i++) {
			array[i-1] = i;
		}
		return array;
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getMaxGatewaySize() {
		return 5;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Integer[] getGatewaySizeArray() {
		int maxSize = getMaxGatewaySize();
		maxSize = maxSize >= 1 ? maxSize : 5;
		Integer[] array = new Integer[maxSize];
		for (int i=1; i<=maxSize; i++) {
			array[i-1] = i;
		}
		return array;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getMaxGatewaySizeJSON() {
		return array2JSON(getGatewaySizeArray());
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getMaxGatewayConnection() {
		return 900000;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Integer[] getMaxGatewayConnectionArray() {
		int maxSize = getMaxGatewayConnection();
		maxSize = maxSize >= 1 ? maxSize : 50000;
		List<Integer> list = new ArrayList<Integer>();
		int begin = 100000;
		while (begin < maxSize) {
			list.add(begin);
			begin = begin + 100000;
		}
		return list.toArray(new Integer[list.size()]);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getMaxGatewayConnectionJSON() {
		return array2JSON(getMaxGatewayConnectionArray());
	}
	
	/**
	 * 获取常量｛minSize｝<br>
	 * 
	 * @return
	 */
	public static String[] getHostPoolMinSize() {
		String minSize = varManager.getStringValue(SystemVariable.VM_POOL_MIN_SIZE,
				"1,2,3,4,5");
		String[] vars = null;
		if (minSize != null) {
			vars = minSize.split(",");
		}
		return vars;
	}

	/**
	 * 获取常量｛maxSize｝<br>
	 * 
	 * @return
	 */

	public static String[] getHostPoolMaxSize() {
		String maxSize = varManager.getStringValue(SystemVariable.VM_POOL_MAX_SIZE,
				"1,2,3,4,5");
		String[] vars = null;
		if (maxSize != null) {
			vars = maxSize.split(",");
		}
		return vars;
	}

	/**
	 * 获取常量｛increaseSize｝<br>
	 * 
	 * @return
	 */
	public static String[] getHostPoolIncreaseSize() {
		return varManager.getStringValue(VM_POOL_INCREASE_SIZE, "1,2,3,4,5")
				.split(",");
	}

	/**
	 * 获取常量｛decreaseSize｝<br>
	 * 
	 * @return
	 */
	public static String[] getHostPoolDecreaseSize() {
		return varManager.getStringValue(VM_POOL_DECREASE_SIZE, "1,2,3,4,5")
				.split(",");
	}

	/**
	 * 获取常量｛timeInterval｝<br>
	 * 
	 * @return
	 */
	public static String[] getHostPoolTimeInterval() {
		return varManager.getStringValue(VM_POOL_TIME_INTERVAL,
				"10,20,30,40,50").split(",");
	}
	
	/**
	 * 获取常量｛appStorageSize｝,获取应用服务器可供选择的存储大小<br>
	 * 
	 * @return
	 */
	public static String[] getAppStorageSizes() {
		String minSize = varManager.getStringValue(SystemVariable.NAS_STORAGE_SIZE,
				"2,4,8,16,32,64,128,256,512,1024,2048");
		String[] vars = null;
		if (minSize != null) {
			vars = minSize.split(",");
		}
		return vars;
	}

	/**
	 * 获取常量｛minSize｝<br>
	 * 
	 * @return
	 */
	public static String[] getStoragePoolMinSize() {
		String minSize = varManager.getStringValue(SystemVariable.STORAGE_POOL_MIN_SIZE,
				"1,2,3,4,5");
		String[] vars = null;
		if (minSize != null) {
			vars = minSize.split(",");
		}
		return vars;
	}

	/**
	 * 获取常量｛maxSize｝<br>
	 * 
	 * @return
	 */

	public static String[] getStoragePoolMaxSize() {
		String maxSize = varManager.getStringValue(SystemVariable.STORAGE_POOL_MAX_SIZE,
				"1,2,3,4,5");
		String[] vars = null;
		if (maxSize != null) {
			vars = maxSize.split(",");
		}
		return vars;
	}

	/**
	 * 获取常量｛increaseSize｝<br>
	 * 
	 * @return
	 */
	public static String[] getStoragePoolIncreaseSize() {
		String increaseSize = varManager.getStringValue(
				SystemVariable.STORAGE_POOL_INCREASE_SIZE, "1,2,3,4,5");
		String[] vars = null;
		if (increaseSize != null) {
			vars = increaseSize.split(",");
		}
		return vars;
	}

	/**
	 * 获取常量｛decreaseSize｝<br>
	 * 
	 * @return
	 */
	public static String[] getStoragePoolDecreaseSize() {
		String decreaseSize = varManager.getStringValue(
				SystemVariable.STORAGE_POOL_DECREASE_SIZE, "1,2,3,4,5");
		String[] vars = null;
		if (decreaseSize != null) {
			vars = decreaseSize.split(",");
		}
		return vars;
	}

	/**
	 * 获取常量｛timeInterval｝<br>
	 * 
	 * @return
	 */
	public static String[] getStoragePoolTimeInterval() {
		String timeInterval = varManager.getStringValue(
				SystemVariable.STORAGE_POOL_TIME_INTERVAL, "10,20,30,40,50");
		String[] vars = null;
		if (timeInterval != null) {
			vars = timeInterval.split(",");
		}
		return vars;
	}
	
	/**
	 * 
	 * @param array
	 * @return
	 */
	public static String array2JSON(Object[] array) {
		if (null == array) {
			return "{}";
		}
		List<Object> list = new ArrayList<Object>();
		for (Object value : array) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", value); //$NON-NLS-1$
			map.put("text", value); //$NON-NLS-1$
			list.add(map);
		}
		return JSONArray.fromObject(list).toString();
	}
	
	/**
	 * 
	 * @param collection
	 * @return
	 */
	public static String collection2JSON(Collection<Object> collection) {
		if (null == collection) {
			return "{}";
		}
		List<Object> list = new ArrayList<Object>();
		for (Object value : collection) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", value); //$NON-NLS-1$
			map.put("text", value); //$NON-NLS-1$
			list.add(map);
		}
		return JSONArray.fromObject(list).toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getHadoopManagementProtal() {
		return varManager.getStringValue(HADOOP_MANAGEMENT_PROTAL, "http://192.168.0.21:7180"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getHadoopManagementUser() {
		return varManager.getStringValue(HADOOP_MANAGEMENT_USER, "admin"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getHadoopManagementPassword() {
		return varManager.getStringValue(HADOOP_MANAGEMENT_PASSWORD, "admin"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public static String[] getDatabaseTypes() {
		return new String[] {"Oracle", "DB2", "MySQL", "SQLServer"};
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDatabaseTypesJSON() {
		String[] databaseTypes = getDatabaseTypes();
		databaseTypes = null == databaseTypes || databaseTypes.length == 0 ? new String[] {
				"Oracle", "DB2", "MySql", "SQLServer" }
				: databaseTypes;
		List<Object> list = new ArrayList<Object>();
		for (String type : databaseTypes) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", type);
			map.put("text", type);
			list.add(map);
		}
		return JSONArray.fromObject(list).toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public static Integer[] getEsbServerSizes() {
		List<Integer> sizes = new ArrayList<Integer>(10);
		for (int i=1; i<=10; i++) {
			sizes.add(i);
		}
		return sizes.toArray(new Integer[sizes.size()]);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getEsbServerSizesJSON() {
		return array2JSON(getEsbServerSizes());
	}
	
}
