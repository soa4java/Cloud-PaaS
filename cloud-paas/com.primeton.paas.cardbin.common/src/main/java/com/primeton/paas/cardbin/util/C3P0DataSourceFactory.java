/**
 * 
 */
package com.primeton.paas.cardbin.util;

import java.util.Properties;

import javax.sql.DataSource;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PoolConfig;

/**
 * C3P0DataSourceFactory <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@SuppressWarnings("deprecation")
public final class C3P0DataSourceFactory implements IC3P0DataSourceConfigNames {
	private static final ILogger logger = LoggerFactory.getLogger(C3P0DataSourceFactory.class);

	private C3P0DataSourceFactory() {

	}
	
	/**
	 * 
	 * @param properties
	 * @return
	 */
	public static DataSource createDataSource(Properties properties) {
		if (properties==null) {
			throw new NullPointerException("The properties is null.");
		}
		try {
			PoolConfig poolConfig = new PoolConfig(properties);
			ComboPooledDataSource ds = new ComboPooledDataSource();
			ds.setDriverClass(properties.getProperty(DRIVER_CLASS));
			ds.setJdbcUrl(properties.getProperty(JDBC_URL));
			ds.setUser(properties.getProperty(USER));
			ds.setPassword(properties.getProperty(PASSWORD));
			ds.setAcquireIncrement(poolConfig.getAcquireIncrement());
			ds.setAcquireRetryAttempts(poolConfig.getAcquireRetryAttempts());
			ds.setAcquireRetryDelay(poolConfig.getAcquireRetryDelay());
			ds.setCheckoutTimeout(poolConfig.getCheckoutTimeout());
			ds.setConnectionTesterClassName(poolConfig.getConnectionTesterClassName());
			ds.setFactoryClassLocation(poolConfig.getFactoryClassLocation());
			ds.setIdleConnectionTestPeriod(poolConfig.getIdleConnectionTestPeriod());
			ds.setInitialPoolSize(poolConfig.getInitialPoolSize());
			ds.setMaxIdleTime(poolConfig.getMaxIdleTime());
			ds.setMaxPoolSize(poolConfig.getMaxPoolSize());
			ds.setMaxStatements(poolConfig.getMaxStatements());
			ds.setMaxStatementsPerConnection(poolConfig.getMaxStatementsPerConnection());
			ds.setMinPoolSize(poolConfig.getMinPoolSize());
			ds.setNumHelperThreads(poolConfig.getNumHelperThreads());
			ds.setPreferredTestQuery(poolConfig.getPreferredTestQuery());
			ds.setPropertyCycle(poolConfig.getPropertyCycle());
			ds.setAutoCommitOnClose(poolConfig.isAutoCommitOnClose());
			ds.setBreakAfterAcquireFailure(poolConfig.isBreakAfterAcquireFailure());
			ds.setForceIgnoreUnresolvedTransactions(poolConfig.isForceIgnoreUnresolvedTransactions());
			ds.setTestConnectionOnCheckin(poolConfig.isTestConnectionOnCheckin());
			ds.setTestConnectionOnCheckout(poolConfig.isTestConnectionOnCheckout());
			ds.setUsesTraditionalReflectiveProxies(poolConfig.isUsesTraditionalReflectiveProxies());
			return ds;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * 
	 * @param datasource
	 */
	public static void destroyDataSource(DataSource datasource) {
		if (datasource == null) {
			return;
		}
		try {
			DataSources.destroy(datasource);
		} catch (Throwable e) {
			logger.error(e);
		}
	}
	
}
