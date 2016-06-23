/**
 * 
 */
package com.primeton.paas.app.config.model;

import com.primeton.paas.app.config.IConfigModel;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SimulatorDataSourceModel implements IConfigModel {

	private static final long serialVersionUID = -4923632433647415327L;
	
	private String dataSourceName;
	
	private String driverClass;
	
	private String jdbcUrl;
	
	private String user;
	
	private String password;
	
	private String initialPoolSize;
	
	private String minPoolSize;
	
	private String maxPoolSize;
	
	private String acquireRetryAttempts;
	
	private String acquireRetryDelay;
	
	private String acquireIncrement;
	
	private String checkoutTimeout;
	
	private String idleConnectionTestPeriod;
	
	private String testSQL;

	/**
	 * Default. <br>
	 */
	public SimulatorDataSourceModel() {
		super();
	}

	public String getAcquireIncrement() {
		return acquireIncrement;
	}

	public void setAcquireIncrement(String acquireIncrement) {
		this.acquireIncrement = acquireIncrement;
	}

	public String getAcquireRetryAttempts() {
		return acquireRetryAttempts;
	}

	public void setAcquireRetryAttempts(String acquireRetryAttempts) {
		this.acquireRetryAttempts = acquireRetryAttempts;
	}

	public String getAcquireRetryDelay() {
		return acquireRetryDelay;
	}

	public void setAcquireRetryDelay(String acquireRetryDelay) {
		this.acquireRetryDelay = acquireRetryDelay;
	}

	public String getCheckoutTimeout() {
		return checkoutTimeout;
	}

	public void setCheckoutTimeout(String checkoutTimeout) {
		this.checkoutTimeout = checkoutTimeout;
	}

	public String getIdleConnectionTestPeriod() {
		return idleConnectionTestPeriod;
	}

	public void setIdleConnectionTestPeriod(String idleConnectionTestPeriod) {
		this.idleConnectionTestPeriod = idleConnectionTestPeriod;
	}

	public String getInitialPoolSize() {
		return initialPoolSize;
	}

	public void setInitialPoolSize(String initialPoolSize) {
		this.initialPoolSize = initialPoolSize;
	}

	public String getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(String maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public String getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(String minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public String getTestSQL() {
		return testSQL;
	}

	public void setTestSQL(String testSQL) {
		this.testSQL = testSQL;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "SimulatorDataSourceModel (dataSourceName="+dataSourceName+",jdbcUrl="+jdbcUrl+",user="+user+",password="+password+")";
	}
	
}
