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
public class EsbServerService extends AbstractService {

	/**
	 * serialVersionUID. <br>
	 */
	private static final long serialVersionUID = -5433107750264516459L;

	/**
	 * EsbServer. <br>
	 */
	public static final String TYPE = "EsbServer";
	
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

	private static final String MIN_MEMORY_SIZE = "minMemory";
	
	private static final String MAX_MEMORY_SIZE = "maxMemory";
	
	private static final String MIN_PERM_SIZE = "minPermSize";
	
	private static final String MAX_PERM_SIZE = "maxPermSize";
	
	private static final String ADMIN_PORT = "adminPort";
	
	private static final String JMX_PORT = "jmxPort";
	
	private static final String MANAGE_PORT = "managePort";
	
	private static final String DATABASE_TYPE = "databaseType";
	
	private static final String C3P0_URL = "c3p0Url";
	
	private static final String C3P0_USER_NAME = "c3p0UserName";
	
	private static final String C3P0_PASSWORD = "c3p0Password";
	
	private static final String C3P0_POOL_SIZE = "c3p0PoolSize";
	
	private static final String C3P0_MAX_POOL_SIZE = "c3p0MaxPoolSize";
	
	private static final String C3P0_MIN_POOL_SIZE = "c3p0MinPoolSize";
	
	/**
	 * Default. <br>
	 */
	public EsbServerService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
		setAdminPort(-1); //$NON-NLS-1$
		setJmxPort(-1); //$NON-NLS-1$
		setManagePort(-1); //$NON-NLS-1$
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
	public int getAdminPort() {
		return getValue(ADMIN_PORT, 6200); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param adminPort
	 */
	public void setAdminPort(int adminPort) {
		setValue(ADMIN_PORT, adminPort);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getJmxPort() {
		return getValue(JMX_PORT, 9999); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param jmxPort
	 */
	public void setJmxPort(int jmxPort) {
		setValue(JMX_PORT, jmxPort);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getManagePort() {
		return getValue(MANAGE_PORT, 1099); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param managePort
	 */
	public void setManagePort(int managePort) {
		setValue(MANAGE_PORT, managePort);
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
	public String getC3p0Url() {
		return getValue(C3P0_URL);
	}
	
	/**
	 * 
	 * @param c3p0Url
	 */
	public void setC3p0Url(String c3p0Url) {
		setValue(C3P0_URL, c3p0Url);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getC3p0UserName() {
		return getValue(C3P0_USER_NAME);
	}

	/**
	 * 
	 * @param c3p0UserName
	 */
	public void setC3p0UserName(String c3p0UserName) {
		setValue(C3P0_USER_NAME, c3p0UserName);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getC3p0Password() {
		return getValue(C3P0_PASSWORD);
	}
	
	/**
	 * 
	 * @param c3p0Password
	 */
	public void setC3p0Password(String c3p0Password) {
		setValue(C3P0_PASSWORD, c3p0Password);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getC3p0PoolSize() {
		return getValue(C3P0_POOL_SIZE, 100); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param c3p0PoolSize
	 */
	public void setC3p0PoolSize(int c3p0PoolSize) {
		setValue(C3P0_POOL_SIZE, c3p0PoolSize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getC3p0MaxPoolSize() {
		return getValue(C3P0_MAX_POOL_SIZE, 100); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param c3p0MaxPoolSize
	 */
	public void setC30p0MaxPoolSize(int c3p0MaxPoolSize) {
		setValue(C3P0_MAX_POOL_SIZE, c3p0MaxPoolSize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getC3p0MinPoolSize() {
		return getValue(C3P0_MIN_POOL_SIZE, 100);
	}
	
	/**
	 * 
	 * @param c3p0MinPoolSize
	 */
	public void setC30p0MinPoolSize(int c3p0MinPoolSize) {
		setValue(C3P0_MIN_POOL_SIZE, c3p0MinPoolSize);
	}
	
}
