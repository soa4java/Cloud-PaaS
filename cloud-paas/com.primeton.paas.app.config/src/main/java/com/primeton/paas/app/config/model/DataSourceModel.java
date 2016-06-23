/**
 * 
 */
package com.primeton.paas.app.config.model;

import com.primeton.paas.app.config.IConfigModel;

/**
 * /Cloud/Cesium/Config/App/appName/dataSource. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DataSourceModel implements IConfigModel {

	private static final long serialVersionUID = -4923632433647415327L;
	
	private String dataSourceName;
	
	private String dataSourceId;
	
	private String dbServiceName;
	
	private String dataSourceDesc;
	
	private int initialPoolSize;
	
	private int minPoolSize;
	
	private int maxPoolSize;
	
	private int acquireRetryAttempts;
	
	private int acquireRetryDelay;
	
	private int acquireIncrement;
	
	private int checkoutTimeout;
	
	private int idleConnectionTestPeriod;
	
	private String testSQL;

	/**
	 * Default. <br>
	 */
	public DataSourceModel() {
		super();
	}

	/**
	 * 
	 * @param dataSourceName
	 */
	public DataSourceModel(String dataSourceName) {
		super();
		this.dataSourceName = dataSourceName;
	}

	/**
	 * @return the dataSourceName
	 */
	public String getDataSourceName() {
		return dataSourceName;
	}

	/**
	 * @param dataSourceName the dataSourceName to set
	 */
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	/**
	 * @return the dataSourceId
	 */
	public String getDataSourceId() {
		return dataSourceId;
	}

	/**
	 * @param dataSourceId the dataSourceId to set
	 */
	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	/**
	 * @return the dbServiceName
	 */
	public String getDbServiceName() {
		return dbServiceName;
	}

	/**
	 * @param dbServiceName the dbServiceName to set
	 */
	public void setDbServiceName(String dbServiceName) {
		this.dbServiceName = dbServiceName;
	}

	/**
	 * @return the dataSourceDesc
	 */
	public String getDataSourceDesc() {
		return dataSourceDesc;
	}

	/**
	 * @param dataSourceDesc the dataSourceDesc to set
	 */
	public void setDataSourceDesc(String dataSourceDesc) {
		this.dataSourceDesc = dataSourceDesc;
	}

	/**
	 * @return the initialPoolSize
	 */
	public int getInitialPoolSize() {
		return initialPoolSize;
	}

	/**
	 * @param initialPoolSize the initialPoolSize to set
	 */
	public void setInitialPoolSize(int initialPoolSize) {
		this.initialPoolSize = initialPoolSize;
	}

	/**
	 * @return the minPoolSize
	 */
	public int getMinPoolSize() {
		return minPoolSize;
	}

	/**
	 * @param minPoolSize the minPoolSize to set
	 */
	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	/**
	 * @return the maxPoolSize
	 */
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	/**
	 * @param maxPoolSize the maxPoolSize to set
	 */
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	/**
	 * @return the acquireRetryAttempts
	 */
	public int getAcquireRetryAttempts() {
		return acquireRetryAttempts;
	}

	/**
	 * @param acquireRetryAttempts the acquireRetryAttempts to set
	 */
	public void setAcquireRetryAttempts(int acquireRetryAttempts) {
		this.acquireRetryAttempts = acquireRetryAttempts;
	}

	/**
	 * @return the acquireRetryDelay
	 */
	public int getAcquireRetryDelay() {
		return acquireRetryDelay;
	}

	/**
	 * @param acquireRetryDelay the acquireRetryDelay to set
	 */
	public void setAcquireRetryDelay(int acquireRetryDelay) {
		this.acquireRetryDelay = acquireRetryDelay;
	}

	/**
	 * @return the acquireIncrement
	 */
	public int getAcquireIncrement() {
		return acquireIncrement;
	}

	/**
	 * @param acquireIncrement the acquireIncrement to set
	 */
	public void setAcquireIncrement(int acquireIncrement) {
		this.acquireIncrement = acquireIncrement;
	}

	/**
	 * @return the checkoutTimeout
	 */
	public int getCheckoutTimeout() {
		return checkoutTimeout;
	}

	/**
	 * @param checkoutTimeout the checkoutTimeout to set
	 */
	public void setCheckoutTimeout(int checkoutTimeout) {
		this.checkoutTimeout = checkoutTimeout;
	}

	/**
	 * @return the idleConnectionTestPeriod
	 */
	public int getIdleConnectionTestPeriod() {
		return idleConnectionTestPeriod;
	}

	/**
	 * @param idleConnectionTestPeriod the idleConnectionTestPeriod to set
	 */
	public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
		this.idleConnectionTestPeriod = idleConnectionTestPeriod;
	}

	/**
	 * @return the testSQL
	 */
	public String getTestSQL() {
		return testSQL;
	}

	/**
	 * @param testSQL the testSQL to set
	 */
	public void setTestSQL(String testSQL) {
		this.testSQL = testSQL;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "DataSourceModel [dataSourceName=" + dataSourceName
				+ ", dataSourceId=" + dataSourceId + ", dbServiceName="
				+ dbServiceName + ", dataSourceDesc=" + dataSourceDesc
				+ ", initialPoolSize=" + initialPoolSize + ", minPoolSize="
				+ minPoolSize + ", maxPoolSize=" + maxPoolSize
				+ ", acquireRetryAttempts=" + acquireRetryAttempts
				+ ", acquireRetryDelay=" + acquireRetryDelay
				+ ", acquireIncrement=" + acquireIncrement
				+ ", checkoutTimeout=" + checkoutTimeout
				+ ", idleConnectionTestPeriod=" + idleConnectionTestPeriod
				+ ", testSQL=" + testSQL + "]";
	}
	
}
