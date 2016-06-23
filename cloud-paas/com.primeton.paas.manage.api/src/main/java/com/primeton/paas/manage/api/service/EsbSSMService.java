/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class EsbSSMService extends AbstractService {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1454983397531535674L;
	
	public static final String TYPE = "EsbSSM";
	
	private static final String MIN_MEMORY_SIZE = "minMemory";
	
	private static final String MAX_MEMORY_SIZE = "maxMemory";
	
	private static final String MIN_PERM_SIZE = "minPermSize";
	
	private static final String MAX_PERM_SIZE = "maxPermSize";
	
	private static final String DATABASE_TYPE = "databaseType";
	
	private static final String JDBC_URL = "jdbcUrl";
	
	private static final String JDBC_USER = "jdbcUser";
	
	private static final String JDBC_PASSWORD = "jdbcPassword";
	
	/**
	 * Oracle
	 */
	public static final String DATABASE_ORACLE = "Oracle";
	
	/**
	 * MySQL
	 */
	public static final String DATABASE_MySQL = "MySql";
	
	/**
	 * DB2
	 */
	public static final String DATABASE_DB2 = "DB2";

	/**
	 * Default. <br>
	 */
	public EsbSSMService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMinMemorySize() {
		return getValue(MIN_MEMORY_SIZE, 128); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param minMemorySize
	 */
	public void setMinMemorySize(int minMemorySize) {
		setValue(MIN_MEMORY_SIZE, minMemorySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxMemorySize() {
		return getValue(MAX_MEMORY_SIZE, 256); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param maxMemorySize
	 */
	public void setMaxMemorySize(int maxMemorySize) {
		setValue(MAX_MEMORY_SIZE, maxMemorySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMinPermSize() {
		return getValue(MIN_PERM_SIZE, 64); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param minPermSize
	 */
	public void setMinPermSize(int minPermSize) {
		setValue(MIN_PERM_SIZE, minPermSize);
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxPermSize() {
		return getValue(MAX_PERM_SIZE, 64); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param maxPermSize
	 */
	public void setMaxPermSize(int maxPermSize) {
		setValue(MAX_PERM_SIZE, maxPermSize);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDatabaseType() {
		return getValue(DATABASE_TYPE);
	}
	
	/**
	 * 
	 * @param databaseType
	 */
	public void setDatabaseType(String databaseType) {
		setValue(DATABASE_TYPE, databaseType);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getJdbcUrl() {
		return getValue(JDBC_URL);
	}
	
	/**
	 * 
	 * @param url
	 */
	public void setJdbcUrl(String url) {
		setValue(JDBC_URL, url);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getJdbcUser() {
		return getValue(JDBC_USER);
	}
	
	/**
	 * 
	 * @param user
	 */
	public void setJdbcUser(String user) {
		setValue(JDBC_USER, user);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getJdbcPassword() {
		return getValue(JDBC_PASSWORD);
	}
	
	/**
	 * 
	 * @param password
	 */
	public void setJdbcPassword(String password) {
		setValue(JDBC_PASSWORD, password);
	}

}
