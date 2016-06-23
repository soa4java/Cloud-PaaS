/**
 * 
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MailService  extends AbstractService {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1720064832825590199L;

	public static final String TYPE = "Mail";
	
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
	
	private static final String MAX_MAILWORKER_NUM = "maxMailWorkerNum"; //default 10
	
	/**
	 * Default constructor <br>
	 */
	public MailService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxMailWorkerNum() {
		return getValue(MAX_MAILWORKER_NUM, 10);
	}
	
	/**
	 * 
	 * @param basePath
	 */
	public void setMaxMailWorkerNum(int maxMailWorkerNum) {
		setValue(MAX_MAILWORKER_NUM, maxMailWorkerNum);
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
	
}
