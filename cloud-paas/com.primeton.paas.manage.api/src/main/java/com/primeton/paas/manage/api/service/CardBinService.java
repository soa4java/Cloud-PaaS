/**
 * 
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * Marked @Deprecated by ZhongWen.Li
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Deprecated
public class CardBinService extends AbstractService {
	
	private static final long serialVersionUID = -3331155234083408469L;

	public static final String TYPE = "CardBin";
	
	private static final String MIN_MEMORY_SIZE = "minMemory";
	private static final String MAX_MEMORY_SIZE = "maxMemory";
	private static final String MIN_PERM_SIZE = "minPermSize";
	private static final String MAX_PERM_SIZE = "maxPermSize";
	private static final String JDBC_DRIVER = "jdbcDriver";
	private static final String JDBC_URL = "jdbcUrl";
	private static final String JDBC_USER = "jdbcUser";
	private static final String JDBC_PASSWORD = "jdbcPassword";
	private static final String JDBC_MIN_POOL_SIZE = "jdbcMinPoolSize";
	private static final String JDBC_MAX_POOL_SIZE = "jdbcMaxPoolSize";
	private static final String WS_PORT = "wsPort";
	
	private static final String SYNC_REMOTE_HOST = "remoteIp";
	private static final String SYNC_REMOTE_USER = "remoteUser";
	private static final String SYNC_REMOTE_PWD = "remotePwd";
	private static final String SYNC_HDFS_FILE_URL = "hdfsFileUrl";
	private static final String SYNC_REMOTE_FILE_PATH = "remoteFilePath";
	private static final String SYNC_TMP_FILE_PATH = "tempFilePath";
	private static final String SYNC_DEST_FILE_PATH = "destFilePath";
	private static final String SYNC_DAY_OF_MONTH = "syncDay";
	private static final String SYNC_HOUR_OF_DAY = "syncHour";
	private static final String SYNC_IS_ENABLE = "isSync";
	

	/**
	 * Default constructor <br>
	 */
	public CardBinService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}

	/**
	 * @return the minMemorySize
	 */
	public int getMinMemorySize() {
		return getValue(MIN_MEMORY_SIZE, -1);
	}


	/**
	 * @param minMemorySize the minMemorySize to set
	 */
	public void setMinMemorySize(int minMemorySize) {
		setValue(MIN_MEMORY_SIZE, minMemorySize);
	}


	/**
	 * @return the maxMemorySize
	 */
	public int getMaxMemorySize() {
		return getValue(MAX_MEMORY_SIZE, -1);
	}


	/**
	 * @param maxMemorySize the maxMemorySize to set
	 */
	public void setMaxMemorySize(int maxMemorySize) {
		setValue(MAX_MEMORY_SIZE, maxMemorySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMinPermMemorySize() {
		return getValue(MIN_PERM_SIZE, -1);
	}
	
	/**
	 * 
	 * @param minPermMemorySize
	 */
	public void setMinPermMemorySize(int minPermMemorySize) {
		setValue(MIN_PERM_SIZE, minPermMemorySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxPermMemorySize() {
		return getValue(MAX_PERM_SIZE, -1);
	}
	
	/**
	 * 
	 * @param maxPermMemorySize
	 */
	public void setMaxPermMemorySize(int maxPermMemorySize) {
		setValue(MAX_PERM_SIZE, maxPermMemorySize);
	}

	/**
	 * @return
	 */
	public String getJdbcDriver() {
		return getValue(JDBC_DRIVER);
	}

	/**
	 * @param jdbcDriver
	 */
	public void setJdbcDriver(String jdbcDriver) {
		setValue(JDBC_DRIVER, jdbcDriver);
	}

	/**
	 * @return
	 */
	public int getJdbcMaxPoolSize() {
		return getValue(JDBC_MAX_POOL_SIZE,10);
	}

	/**
	 * @param jdbcMaxPoolSize
	 */
	public void setJdbcMaxPoolSize(int jdbcMaxPoolSize) {
		setValue(JDBC_MAX_POOL_SIZE, jdbcMaxPoolSize);
	}

	/**
	 * @return
	 */
	public int getJdbcMinPoolSize() {
		return getValue(JDBC_MIN_POOL_SIZE, 5);
	}

	/**
	 * @param jdbcMinPoolSize
	 */
	public void setJdbcMinPoolSize(int jdbcMinPoolSize) {
		setValue(JDBC_MIN_POOL_SIZE, jdbcMinPoolSize);
	}

	/**
	 * @return
	 */
	public String getJdbcPassword() {
		return getValue(JDBC_PASSWORD);
	}

	/**
	 * @param jdbcPassword
	 */
	public void setJdbcPassword(String jdbcPassword) {
		setValue(JDBC_PASSWORD, jdbcPassword);
	}

	/**
	 * @return
	 */
	public String getJdbcUrl() {
		return getValue(JDBC_URL);
	}

	/**
	 * @param jdbcUrl
	 */
	public void setJdbcUrl(String jdbcUrl) {
		setValue(JDBC_URL, jdbcUrl);
	}

	/**
	 * @return
	 */
	public String getJdbcUser() {
		return getValue(JDBC_USER);
	}

	/**
	 * @param jdbcUser
	 */
	public void setJdbcUser(String jdbcUser) {
		setValue(JDBC_USER, jdbcUser);
	}
	
	/**
	 * @return
	 */
	public int getWsPort() {
		return getValue(WS_PORT, 8011);
	}

	/**
	 * @param wsPort
	 */
	public void setWsPort(int wsPort) {
		setValue(WS_PORT, wsPort);
	}
	
	/**
	 * @return
	 */
	public String getRemoteIp() {
		return getValue(SYNC_REMOTE_HOST);
	}
	/**
	 * @param remoteIp
	 */
	public void setRemoteIp(String remoteIp) {
		setValue(SYNC_REMOTE_HOST, remoteIp);
	}
	
	/**
	 * @return
	 */
	public String getRemoteUser() {
		return getValue(SYNC_REMOTE_USER);
	}
	/**
	 * @param remoteUser
	 */
	public void setRemoteUser(String remoteUser) {
		setValue(SYNC_REMOTE_USER, remoteUser);
	}
	
	
	/**
	 * @return
	 */
	public String getRemotePwd() {
		return getValue(SYNC_REMOTE_PWD);
	}
	/**
	 * @param remotePwd
	 */
	public void setRemotePwd(String remotePwd) {
		setValue(SYNC_REMOTE_PWD, remotePwd);
	}
	
	
	/**
	 * @return
	 */
	public String getHdfsFileUrl() {
		return getValue(SYNC_HDFS_FILE_URL);
	}
	
	public void setHdfsFileUrl(String hdfsFileUrl) {
		setValue(SYNC_HDFS_FILE_URL, hdfsFileUrl);
	}
	
	/**
	 * @return
	 */
	public String getRemoteFilePath() {
		return getValue(SYNC_REMOTE_FILE_PATH);
	}
	/**
	 * @param remoteFilePath
	 */
	public void setRemoteFilePath(String remoteFilePath) {
		setValue(SYNC_REMOTE_FILE_PATH, remoteFilePath);
	}
	
	/**
	 * @return
	 */
	public String getTempFilePath() {
		return getValue(SYNC_TMP_FILE_PATH);
	}
	/**
	 * @param tempFilePath
	 */
	public void setTempFilePath(String tempFilePath) {
		setValue(SYNC_TMP_FILE_PATH, tempFilePath);
	}
	
	/**
	 * @return
	 */
	public String getDestFilePath() {
		return getValue(SYNC_DEST_FILE_PATH);
	}
	/**
	 * @param tempFilePath
	 */
	public void setDestFilePath(String destFilePath) {
		setValue(SYNC_DEST_FILE_PATH, destFilePath);
	}
	
	/**
	 * @return
	 */
	public int getSyncDay() {
		return getValue(SYNC_DAY_OF_MONTH, -1);
	}
	/**
	 * @param syncDay
	 */
	public void setSyncDay(int syncDay){
		setValue(SYNC_DAY_OF_MONTH, syncDay);
	}
	
	/**
	 * @return
	 */
	public int getSyncHour() {
		return getValue(SYNC_HOUR_OF_DAY, -1);
	}
	/**
	 * @param syncHour
	 */
	public void setSyncHour(int syncHour){
		setValue(SYNC_HOUR_OF_DAY, syncHour);
	}

	/**
	 * @return
	 */
	public String getSyncIsEnable() {
		return getValue(SYNC_IS_ENABLE, "N");
	}
	/**
	 * @param isEnableSync
	 */
	public void setSyncIsEnable(String isSync) {
		setValue(SYNC_IS_ENABLE, isSync);
	}
	
}
