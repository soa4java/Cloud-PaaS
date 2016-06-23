package com.primeton.paas.mail.server.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.mail.server.config.Constants;
import com.primeton.paas.mail.server.util.C3P0DataSourceFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConnectionFactory {
	
	private static DataSource datasource = null;
	private static final String IDLE_TEST_PERIOD = "3600";
	
	/**
	 * 
	 */
	public synchronized static void initDataSource() {
		if (datasource != null) {
			System.out.println("DataSource is already init!");
			return;
		}
		Properties properties = new Properties();
		properties.setProperty(C3P0DataSourceFactory.DRIVER_CLASS, SystemProperties.getProperty(Constants.SYSTEM_JDBC_DRIVER_KEY, "com.mysql.jdbc.Driver"));
		properties.setProperty(C3P0DataSourceFactory.JDBC_URL, SystemProperties.getProperty(Constants.SYSTEM_JDBC_URL_KEY, "jdbc:mysql://192.168.100.201:3306/upaas"));
		properties.setProperty(C3P0DataSourceFactory.USER, SystemProperties.getProperty(Constants.SYSTEM_JDBC_USER_KEY, "root"));
		properties.setProperty(C3P0DataSourceFactory.PASSWORD, SystemProperties.getProperty(Constants.SYSTEM_JDBC_PASSWORD_KEY, "000000"));
		properties.setProperty(C3P0DataSourceFactory.MIN_POOL_SIZE, SystemProperties.getProperty(Constants.SYSTEM_JDBC_MIN_POOL_SIZE_KEY, "5"));
		properties.setProperty(C3P0DataSourceFactory.MAX_POOL_SIZE, SystemProperties.getProperty(Constants.SYSTEM_JDBC_MAX_POOL_SIZE_KEY, "10"));
		properties.setProperty(C3P0DataSourceFactory.IDLE_CONNECTION_TEST_PERIOD, IDLE_TEST_PERIOD);
		datasource = C3P0DataSourceFactory.createDataSource(properties);
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
