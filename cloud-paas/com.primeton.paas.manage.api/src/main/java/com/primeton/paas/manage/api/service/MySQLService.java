/**
 * 
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class MySQLService extends AbstractService {

	private static final long serialVersionUID = 7302840033070596421L;

	public static final String TYPE = "MySQL";
	
	public static final String SCHEMAL = "schema";
	public static final String USER = "user";
	public static final String PASSWORD = "password";
	public static final String CHARACTERSET = "characterSet";
	public static final String STORAGE_PATH = "storagePath";
	public static final String STORAGE_SIZE = "storageSize";
	public static final String JDBC_DRIVER = "jdbcDriver";
	
	/**
	 * 
	 */
	public MySQLService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}

	public String getSchema() {
		return getValue(SCHEMAL, "upaas");
	}
	
	public void setSchema(String schema) {
		setValue(SCHEMAL, (schema == null || schema.trim().length() == 0) ? "upaas" : schema);
	}
	
	public String getUser() {
		return getValue(USER, "upaas");
	}
	
	public void setUser(String user) {
		setValue(USER, (user == null || user.trim().length() == 0) ? "upaas" : user);
	}
	
	public String getPassword() {
		return getValue(PASSWORD, "000000");
	}
	
	public void setPassword(String password) {
		setValue(PASSWORD, (password == null || password.trim().length() == 0) ? "000000" : password);
	}
	
	public String getCharacterSet() {
		return getValue(CHARACTERSET, "utf-8");
	}
	
	public void setCharacterSet(String characterSet) {
		setValue(CHARACTERSET, (characterSet == null || characterSet.trim().length() == 0) ? "utf-8" : characterSet);
	}
	
	public String getStoragePath() {
		return getValue(STORAGE_PATH, "/storage/db");
	}
	
	/**
	 * 
	 * @return
	 */
	public int getStorageSize() {
		return (int)getValue(STORAGE_SIZE, 0);
	}
	
	/**
	 * 
	 * @param storageSize
	 */
	public void setStorageSize(int storageSize) {
		setValue(STORAGE_SIZE, storageSize);
	}
	
	
	public void setStoragePath(String storagePath) {
		setValue(STORAGE_PATH, (storagePath == null || storagePath.trim().length() == 0) ? "/storage/db" : storagePath);
	}
	
	public String getBasePath() {
		return getStoragePath() + "/mysql"; //$NON-NLS-1$
	}
	
	public String getDataPath() {
		return getBasePath() + "/data"; //$NON-NLS-1$
	}
	
	public String getJdbcDriver() {
		return getValue(JDBC_DRIVER, "com.mysql.jdbc.Driver"); //$NON-NLS-1$
	}
	
	public void setJdbcDriver(String jdbcDriver) {
		setValue(JDBC_DRIVER, (jdbcDriver == null || jdbcDriver.trim().length() == 0) ? "com.mysql.jdbc.Driver" : jdbcDriver); //$NON-NLS-1$
	}
	
}
