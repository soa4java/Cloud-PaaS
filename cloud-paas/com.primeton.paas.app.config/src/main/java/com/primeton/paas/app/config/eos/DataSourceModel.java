/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.app.config.eos;

import com.primeton.paas.app.config.IConfigModel;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DataSourceModel implements IConfigModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1029231293011528444L;

	private String name;
	private String dbType;
	private String driverType;
	private String driverClass;
	private String jdbcUrl;
	private String username;
	private String password;
	private int initSize;
	private int minSize;
	private int maxSize;
	private String isolationLevel;
	private int retryTimes;
	private String testSQL;
	
	/**
	 * Default. <br>
	 */
	public DataSourceModel() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDriverType() {
		return driverType;
	}

	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getInitSize() {
		return initSize;
	}

	public void setInitSize(int initSize) {
		this.initSize = initSize;
	}

	public int getMinSize() {
		return minSize;
	}

	public void setMinSize(int minSize) {
		this.minSize = minSize;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public String getIsolationLevel() {
		return isolationLevel;
	}

	public void setIsolationLevel(String isolationLevel) {
		this.isolationLevel = isolationLevel;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public String getTestSQL() {
		return testSQL;
	}

	public void setTestSQL(String testSQL) {
		this.testSQL = testSQL;
	}

	public String toString() {
		return "DataSourceModel [name=" + name + ", dbType=" + dbType
				+ ", driverType=" + driverType + ", driverClass=" + driverClass
				+ ", jdbcUrl=" + jdbcUrl + ", username=" + username
				+ ", password=" + password + ", initSize=" + initSize
				+ ", minSize=" + minSize + ", maxSize=" + maxSize
				+ ", isolationLevel=" + isolationLevel + ", retryTimes="
				+ retryTimes + ", testSQL=" + testSQL + "]";
	}
	
}
