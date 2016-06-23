/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.io.Serializable;

/**
 * 订单项属性. <br>
 * 
 * @author YanPing.Li (mailto:liyp@primeton.com)
 *
 */
public class OrderItemAttr implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8886437098386652711L;
	
	private String id ;
	private String itemId;
	private String attrName;
	private String attrValue;
	private String description;
	
	
	// Define by Yanping.Li
	/** orderItem attributes */
	
	/** service common */
	public static final String ATTR_SERVICE_ID = "serviceId";
	public static final String ATTR_APP_NAME = "appName";
	public static final String ATTR_CLUSTER_ID = "clusterId";
	public static final String ATTR_DISPLAY_NAME = "displayName";
	public static final String ATTR_SERVICE_TYPE = "type"; 
	public static final String ATTR_HOSTPKG_ID = "hostPkgId";
	public static final String ATTR_HOST_TYPE = "hostType";
	public static final String ATTR_IS_BACKUP = "isBackUp";
	public static final String ATTR_SERVICE_SCPOE = "scope";
	public static final String ATTR_IS_STANDALONE = "isStandalone";
	
	public static final String ATTR_MIN_MEMORY = "minMemorySize";
	public static final String ATTR_MAX_MEMORY = "maxMemorySize";
	public static final String ATTR_MIN_PERM_MEMORY = "minPermMemorySize";
	public static final String ATTR_MAX_PERM_MEMORY = "maxPermMemorySize";
	public static final String ATTR_JDBC_DRIVER = "jdbcDriver"; 
	public static final String ATTR_JDBC_URL = "jdbcUrl";
	public static final String ATTR_JDBC_USER = "jdbcUser";
	public static final String ATTR_JDBC_PASSWORD = "jdbcPassword";
	public static final String ATTR_JDBC_MIN_POOL_SIZE = "jdbcMinPoolSize";
	public static final String ATTR_JDBC_MAX_POOL_SIZE = "jdbcMaxPoolSize";
	
	/** cluster common */
	public static final String ATTR_CLUSTER_MAX_SIZE = "maxNum";
	public static final String ATTR_CLUSTER_MIN_SIZE = "minNum";
	public static final String ATTR_CLUSTER_SIZE = "size";
	
	public static final String ATTR_DATABASE_TYPE = "databaseType";
	
	// c3p0
	public static final String ATTR_C3P0_URL = "c3p0Url";
	public static final String ATTR_C3P0_USER_NAME = "c3p0UserName";
	public static final String ATTR_C3P0_PASSWORD = "c3p0Password";
	public static final String ATTR_C3P0_POOL_SIZE = "c3p0PoolSize";
	public static final String ATTR_C3P0_MAX_POOL_SIZE = "c3p0MaxPoolSize";
	public static final String ATTR_C3P0_MIN_POOL_SIZE = "c3p0MinPoolSize";
	
	// scheduler
	public static final String ATTR_SCHEDULER_SAVE_DAYS = "schedulerSaveDays";
	public static final String ATTR_SCHEDULER_TIME_ROUND = "schedulerTimeRound";
	
	// app
	public static final String ATTR_APP_DOMAIN = "domain";
	public static final String ATTR_APP_IS_ENABLE_DOMAIN = "isEnableDomain"; 
	public static final String ATTR_APP_DESC = "desc";
	
	/** app container :Jetty | Tomcat */
	public static final String ATTR_APP_SERVER_TYPE = "serverType";
	public static final String ATTR_JETTY_MIN_SIZE = "minSize";
	public static final String ATTR_JETTY_MAX_SIZE = "maxSize";
	public static final String ATTR_JETTY_STORAGE_SIZE = "storageSize";
	public static final String ATTR_JETTY_STORAGE_PATH = "storagePath";
	public static final String ATTR_JETTY_VM_ARGS = "vmArgs";
	
	public static final String ATTR_JVM_MIN_PERM_SIZE = "minPermSize";
	public static final String ATTR_JVM_MAX_PERM_SIZE = "maxPermSize";
	public static final String ATTR_JVM_MIN_MEM_SIZE = "minMemSize";
	public static final String ATTR_JVM_MAX_MEM_SIZE = "maxMemSize";
	
	/** svn */	
	public static final String ATTR_SVN_REPO_ROOT = "repoRoot";	
	public static final String ATTR_SVN_REPO_NAME = "svnRepoName";
	public static final String ATTR_SVN_REPO_USER_NAME = "svnUserName";
	public static final String ATTR_SVN_REPO_PASSWORD = "svnPassword";

	/** haproxy */
	public static final String ATTR_HAPROXY_DOMAIN = "domain";
	public static final String ATTR_HAPROXY_HEALTHCHECKURI= "checkUri";
	public static final String ATTR_HAPROXY_BALANCE = "balance";
	public static final String ATTR_HAPROXY_MAX_CONNNECTION = "maxConnectionSize";
	public static final String ATTR_HAPROXY_CONN_TIMEOUT = "connTimeOut";
	public static final String ATTR_HAPROXY_PROTOCOL = "protocol";
	public static final String ATTR_HAPROXY_MEMBER = "member";
	public static final String ATTR_HAPROXY_REL_SRV_TYPE = "relSrvType";
	public static final String ATTR_HAPROXY_REL_SRV_ID = "relServiceId";
	
	public static final String ATTR_HAPROXY_PROTOCOL_TYPE = "protocolType";
	public static final String ATTR_HAPROXY_SSL_CERTIFICATE_PATH = "sslCertificatePath";
	
	/** memcached */
	public static final String ATTR_MEMCACHED_SIZE = "cacheSize";
	public static final String ATTR_MEMCACHED_MAX_CONN_SIZE = "maxConnectionSize";
	
	/** mysql */
	public static final String ATTR_MYSQL_NAME = "serviceName";
	public static final String ATTR_MYSQL_USER_NAME = "userName";
	public static final String ATTR_MYSQL_PASSWORD = "password";
	public static final String ATTR_MYSQL_STORAGE_SIZE = "storageSize";
	public static final String ATTR_MYSQL_STORAGE_PATH = "storagePath";
	public static final String ATTR_MYSQL_CHARACTERSET = "characterSet";
	
	/** cardbin */
	public static final String ATTR_DATA_FILE_PATH = "dataFilePath";
	
	public static final String ATTR_SYNC_REMOTE_HOST = "remoteIp";
	public static final String ATTR_SYNC_REMOTE_USER = "remoteUser";
	public static final String ATTR_SYNC_REMOTE_PWD = "remotePwd";
	public static final String ATTR_SYNC_HDFS_FILE_URL = "hdfsFileUrl";
	public static final String ATTR_SYNC_REMOTE_FILE_PATH = "remoteFilePath";
	public static final String ATTR_SYNC_TMP_FILE_PATH = "tempFilePath";
	public static final String ATTR_SYNC_DEST_FILE_PATH = "destFilePath";
	public static final String ATTR_SYNC_DAY_OF_MONTH = "syncDay"; 
	public static final String ATTR_SYNC_HOUR_OF_DAY = "syncHour"; 
	public static final String ATTR_SYNC_IS_ENABLE = "isSync";
	
	/** mail */
	public static final String ATTR_MAIL_MAX_MAILWORKER_NUM = "maxMailWorkerNum";
	
	/** nginx */
	public static final String ATTR_NGINX_WORKER_PROCESSES = "workerProcesses";
	public static final String ATTR_NGINX_WORKER_CONNECTIONS = "workerConnections";
	public static final String ATTR_NGINX_KEEPALIVE_TIMEOUT = "keepaliveTimeout";
	public static final String ATTR_NGINX_TYPES_HASH_MAX_SIZE = "typesHashMaxSize";
	public static final String ATTR_NGINX_ALLOW_ACCESS_HOSTS = "allowAccessHosts";
	public static final String ATTR_NGINX_CLIENT_MAX_BODY_SIZE = "clientMaxBodySize";
	public static final String ATTR_NGINX_SSL_CERTIFICATE_PATH = "sslCertificatePath"; 
	
	/** NetProxy */
	public static final String ATTR_NETPROXY_WORKER_PROCESSES = "workerProcesses";
	public static final String ATTR_NETPROXY_WORKER_CONNECTIONS = "workerConnections";
	public static final String ATTR_NETPROXY_KEEPALIVE_TIMEOUT = "keepaliveTimeout";
	public static final String ATTR_NETPROXY_TYPES_HASH_MAX_SIZE = "typesHashMaxSize";
	public static final String ATTR_NETPROXY_ALLOW_ACCESS_HOSTS = "allowAccessHosts";
	public static final String ATTR_NETPROXY_CLIENT_MAX_BODY_SIZE = "clientMaxBodySize";
	public static final String ATTR_NETPROXY_SSL_CERTIFICATE_PATH = "sslCertificatePath";  
			
	/** cep */
	public static final String ATTR_CEP_MIN_MEMORY = "minMemory";		
	public static final String ATTR_CEP_MAX_MEMORY = "maxMemory";		
	public static final String ATTR_CEP_MAX_PERM_SIZE = "maxPermSize";	
	public static final String ATTR_CEP_GROUP_NAME = "groupName";		
	public static final String ATTR_CEP_MQ_SERVER = "mqServer";			
	public static final String ATTR_CEP_MQ_DESTS = "mqDests";			
	public static final String ATTR_CEP_MQ_TYPES = "mqTypes";			//T:Topic/Exchange | Q:Queue
	
	/** collector */
	public static final String ATTR_COLLECTOR_MIN_MEMORY = "minMemory";		
	public static final String ATTR_COLLECTOR_MAX_MEMORY = "maxMemory";		
	public static final String ATTR_COLLECTOR_MAX_PERM_SIZE = "maxPermSize";	
	public static final String ATTR_COLLECTOR_GROUP_NAME = "groupName";		
	public static final String ATTR_COLLECTOR_MQ_SERVER = "mqServer";			
	public static final String ATTR_COLLECTOR_MQ_DESTS = "mqDests";			
	public static final String ATTR_COLLECTOR_MQ_TYPES = "mqTypes";			//T:Topic/Exchange | Q:Queue
	public static final String ATTR_COLLECTOR_LOG_ROOT = "logRoot";			
	public static final String ATTR_COLLECTOR_APPENDER_BUFFER = "appenderBuffer";
	
	/** app stretch strategy */
	public static final String ATTR_STRETCH_ISENABLE = "isEnable" ;
	public static final String ATTR_STRETCH_STRATEGY_NAME = "strategyName";
	public static final String ATTR_STRETCH_SIZE = "stretchSize";
	public static final String ATTR_STRETCH_STRATEGY_TYPE = "strategyType";
	public static final String ATTR_STRETCH_STRATEGY_CONTINUED_TIME = "continuedTime";
	public static final String ATTR_STRETCH_IGNORE_TIME = "ignoreTime";
	public static final String ATTR_STRETCH_CPU_THRESHOLD = "cpuThreshold";
	public static final String ATTR_STRETCH_MEMORY_THRESHOLD = "memoryThreshold";
	public static final String ATTR_STRETCH_LB_THRESHOLD = "lbThreshold";
	
	// gateway
	public static final String ATTR_GATEWAY_MAX_CONNECTION = "maxConnection";
	public static final String ATTR_GATEWAY_PRE_SERVERS = "preServers";
	
	// redis
	public static final String ATTR_REDIS_ALIAS_NAME = "aliasName";
	
	// MsgQueue
	public static final String ATTR_MSG_QUEUE_USER = "user";
	public static final String ATTR_MSG_QUEUE_PASSWORD = "password";
	
	/**
	 * 
	 */
	public OrderItemAttr() {
		super();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the attrName
	 */
	public String getAttrName() {
		return attrName;
	}

	/**
	 * @param attrName the attrName to set
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	/**
	 * @return the attrValue
	 */
	public String getAttrValue() {
		return attrValue;
	}

	/**
	 * @param attrValue the attrValue to set
	 */
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "OrderItemAttr [id=" + id + ", itemId=" + itemId + ", attrName="
				+ attrName + ", attrValue=" + attrValue + ", description="
				+ description + "]";
	}
	
}
