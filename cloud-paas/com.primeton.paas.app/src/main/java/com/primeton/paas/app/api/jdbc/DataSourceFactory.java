/**
 * 
 */
package com.primeton.paas.app.api.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.gocom.cloud.common.logger.api.ILogger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PoolConfig;
import com.primeton.paas.app.config.C3P0ConfigNames;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DataSourceFactory implements C3P0ConfigNames {

	private static ILogger logger = SystemLoggerFactory.getLogger(DataSourceFactory.class);
	
	private static ConcurrentHashMap<String, DataSource> dataSourceCache = new ConcurrentHashMap<String, DataSource>();
	
	private DataSourceFactory() {}
	
	/**
	 * 
	 * @param properties
	 * @return
	 */
	public static DataSource createDataSource(Properties properties) {
		if (properties == null) {
			logger.warn("Can not create DataSource, properties is null.");
			return null;
		}
		
		try {
			PoolConfig poolConfig = new PoolConfig(properties);
			ComboPooledDataSource dataSource = new ComboPooledDataSource();
			dataSource.setDriverClass(properties.getProperty(DRIVER_CLASS));
			dataSource.setJdbcUrl(properties.getProperty(JDBC_URL));
			dataSource.setUser(properties.getProperty(USER));
			dataSource.setPassword(properties.getProperty(PASSWORD));
			dataSource.setAcquireIncrement(poolConfig.getAcquireIncrement());
			dataSource.setAcquireRetryAttempts(poolConfig.getAcquireRetryAttempts());
			dataSource.setAcquireRetryDelay(poolConfig.getAcquireRetryDelay());
			dataSource.setCheckoutTimeout(poolConfig.getCheckoutTimeout());
			dataSource.setConnectionTesterClassName(poolConfig.getConnectionTesterClassName());
			dataSource.setFactoryClassLocation(poolConfig.getFactoryClassLocation());
			dataSource.setIdleConnectionTestPeriod(poolConfig.getIdleConnectionTestPeriod());
			dataSource.setInitialPoolSize(Integer.parseInt(properties.getProperty(INITIAL_POOL_SIZE, "5")));
			dataSource.setMaxIdleTime(poolConfig.getMaxIdleTime());
			dataSource.setMaxPoolSize(Integer.parseInt(properties.getProperty(MAX_POOL_SIZE, "10")));
			dataSource.setMaxStatements(poolConfig.getMaxStatements());
			dataSource.setMaxStatementsPerConnection(poolConfig.getMaxStatementsPerConnection());
			dataSource.setMinPoolSize(Integer.parseInt(properties.getProperty(MIN_POOL_SIZE, "5")));
			dataSource.setNumHelperThreads(poolConfig.getNumHelperThreads());
			dataSource.setPreferredTestQuery(poolConfig.getPreferredTestQuery());
			dataSource.setPropertyCycle(poolConfig.getPropertyCycle());
			dataSource.setAutoCommitOnClose(poolConfig.isAutoCommitOnClose());
			dataSource.setBreakAfterAcquireFailure(poolConfig.isBreakAfterAcquireFailure());
			dataSource.setForceIgnoreUnresolvedTransactions(poolConfig.isForceIgnoreUnresolvedTransactions());
			dataSource.setTestConnectionOnCheckin(poolConfig.isTestConnectionOnCheckin());
			dataSource.setTestConnectionOnCheckout(poolConfig.isTestConnectionOnCheckout());
			dataSource.setUsesTraditionalReflectiveProxies(poolConfig.isUsesTraditionalReflectiveProxies());
			
			logger.info("Create DataSource {0} success.", new Object[] { dataSource });
			return dataSource;
		} catch (Throwable t) {
			logger.error(t);
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param dataSource
	 */
	public static void destroyDataSource(DataSource dataSource) {
		if (dataSource == null) {
			return;
		}
		try {
			DataSources.destroy(dataSource);
			logger.info("Destory DataSource {0} success.", new Object[] { dataSource });
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/**
	 * 
	 * @param name
	 * @param dataSource
	 * @param override
	 */
	public static void addDataSource(String name, DataSource dataSource, boolean override) {
		if (name == null || name.trim().length() == 0) {
			logger.warn("Can not add DataSource {0}, dataSource name is empty.", 
					new Object[] { dataSource });
			return;
		}
		if (dataSource == null) {
			return;
		}
		// Test JDBC Connection
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			logger.warn("Can not add DataSource {0} {1}, can not get java.sql.Connection.", 
					new Object[] { name, dataSource }, e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
		
		// Test Connection
		DataSource existed = dataSourceCache.putIfAbsent(name, dataSource);
		if (existed == null) {
			logger.info("Add DataSource {0} {1} success.", new Object[] { name, dataSource});
			return;
		} else if (override) {
			dataSourceCache.remove(name);
			DataSourceFactory.destroyDataSource(existed);
			dataSourceCache.put(name, dataSource);
			logger.info("DataSource {0}, {1} be replaced with {2}.",
					new Object[] { name, existed, dataSource });
			return;
		}
		logger.warn("DataSource {0} {1} is already exists.", new Object[] { name, existed });
	}
	
	/**
	 * 
	 * @param name
	 * @param dataSource
	 */
	public static void addDataSource(String name, DataSource dataSource) {
		addDataSource(name, dataSource, false);
	}
	
	/**
	 * 
	 * @param name
	 */
	public static void removeDataSource(String name) {
		if(name == null || name.trim().length() == 0) {
			return;
		}
		DataSource existed = dataSourceCache.remove(name);
		if (existed != null) {
			DataSourceFactory.destroyDataSource(existed);
		}
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static DataSource getDataSource(String name) {
		if (name == null || name.trim().length() == 0) {
			return null;
		}
		return dataSourceCache.get(name);
	}
	
	/**
	 * 
	 * @return
	 */
	public static DataSource[] getDataSources() {
		return dataSourceCache.values().toArray(new DataSource[dataSourceCache.size()]);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String[] getDataSourceNames() {
		return dataSourceCache.keySet().toArray(new String[dataSourceCache.size()]);
	}
	
	/**
	 * clear. <br>
	 */
	public static void clear() {
		for (String name : dataSourceCache.keySet()) {
			removeDataSource(name);
		}
	}
	
}
