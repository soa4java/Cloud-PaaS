/**
 * 
 */
package com.primeton.paas.cardbin.server.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.cardbin.spi.CardBinConstants;
import com.primeton.paas.cardbin.spi.CardBinRuntimeException;
import com.primeton.paas.cardbin.util.C3P0DataSourceFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConnectionFactory {
	
	private static DataSource datasource = null;
	
	private static final String IDLE_TEST_PERIOD = "3600";
	
	public synchronized static void initDataSource() {
		if (datasource != null) {
			throw new CardBinRuntimeException("DataSource is already init!");
		}
		synchronized(ConnectionFactory.class) {
			if (datasource == null) {
				Properties properties = new Properties();
				properties.setProperty(C3P0DataSourceFactory.DRIVER_CLASS, SystemProperties.getProperty(CardBinConstants.SYSTEM_JDBC_DRIVER_KEY, "com.mysql.jdbc.Driver"));
				properties.setProperty(C3P0DataSourceFactory.JDBC_URL, SystemProperties.getProperty(CardBinConstants.SYSTEM_JDBC_URL_KEY, "jdbc:mysql://127.0.0.1:3306/upaas"));
				properties.setProperty(C3P0DataSourceFactory.USER, SystemProperties.getProperty(CardBinConstants.SYSTEM_JDBC_USER_KEY, "root"));
				properties.setProperty(C3P0DataSourceFactory.PASSWORD, SystemProperties.getProperty(CardBinConstants.SYSTEM_JDBC_PASSWORD_KEY, "1234"));
				properties.setProperty(C3P0DataSourceFactory.MIN_POOL_SIZE, SystemProperties.getProperty(CardBinConstants.SYSTEM_JDBC_MIN_POOL_SIZE_KEY, "5"));
				properties.setProperty(C3P0DataSourceFactory.MAX_POOL_SIZE, SystemProperties.getProperty(CardBinConstants.SYSTEM_JDBC_MAX_POOL_SIZE_KEY, "10"));
				properties.setProperty(C3P0DataSourceFactory.IDLE_CONNECTION_TEST_PERIOD, IDLE_TEST_PERIOD);
				datasource = C3P0DataSourceFactory.createDataSource(properties);
			}
		}
	}
	
	/**
	 * 
	 */
	public static void destroyDataSource() {
		if (datasource == null) {
			return;
		}
		C3P0DataSourceFactory.destroyDataSource(datasource);
		datasource = null;
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		if (datasource == null) {
			initDataSource();
		}
		return null == datasource ? null : datasource.getConnection();
	}
	
}
