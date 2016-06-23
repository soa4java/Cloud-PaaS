/**
 * 
 */
package com.primeton.paas.app.runtime.startup;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.gocom.cloud.cesium.common.api.exception.TimeoutException;
import org.gocom.cloud.cesium.manage.runtime.api.client.InstanceManager;
import org.gocom.cloud.cesium.manage.runtime.api.client.InstanceManagerFactory;
import org.gocom.cloud.cesium.manage.runtime.api.model.RuntimeInstance;
import org.gocom.cloud.cesium.manage.runtime.api.naming.NamingUtil;
import org.gocom.cloud.cesium.model.api.Cluster;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.AppConstants;
import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.api.jdbc.DataSourceFactory;
import com.primeton.paas.app.config.C3P0ConfigNames;
import com.primeton.paas.app.config.ConfigModelManager;
import com.primeton.paas.app.config.model.DataSourceModel;
import com.primeton.paas.app.config.model.IServiceConstants;
import com.primeton.paas.app.config.model.SimulatorDataSourceModel;
import com.primeton.paas.app.config.model.SimulatorDataSourceSetModel;
import com.primeton.paas.app.runtime.IRuntimeListener;
import com.primeton.paas.app.runtime.RuntimeEvent;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DataSourceStartupListener implements IRuntimeListener, C3P0ConfigNames {
	
	private static ILogger logger = SystemLoggerFactory.getLogger(DataSourceStartupListener.class);

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.runtime.IRuntimeListener#start(com.primeton.paas.app.runtime.RuntimeEvent)
	 */
	public void start(RuntimeEvent envent) {
		ServerContext serverContext = ServerContext.getInstance();
		int mode = serverContext.getRunMode();
		// Development Mode
		if (AppConstants.RUN_MODE_SIMULATOR == mode) {
			initSimulatorDataSource();
			return;
		}
		DataSourceModel[] models = ConfigModelManager.getDataSourceModels();
		String appName = ConfigModelManager.getAppName();
		if (models == null || models.length == 0) {
			logger.warn("DataSource module not found for app {" + appName + "}.");
			return;
		}
		InstanceManager instanceManager = null;
		try {
			instanceManager = InstanceManagerFactory.createInstanceManager();
		} catch (TimeoutException e) {
			logger.error("Init DataSource error, Connect to zookeeper timeout, " + e);
			return;
		}
		for (DataSourceModel model : models) {
			if (model != null) {
				// JDBC URL
				String clusterId = model.getDataSourceId();
				if (clusterId == null || clusterId.trim().length() == 0 || "-1".equals(clusterId)) {
					continue;
				}
				Cluster cluster = NamingUtil.getCluster(IServiceConstants.SERVICE_MYSQL, clusterId);
				if (cluster == null) {
					continue;
				}
				Map<String, String> attrs = cluster.getAttributes();
				if (attrs == null || attrs.size() == 0) {
					continue;
				}
				final String schemaName = attrs.get("schema"); //$NON-NLS-1$
				final String jdbcDriver = attrs.get("jdbcDriver"); //$NON-NLS-1$
				final String jdbcUser = attrs.get("user"); //$NON-NLS-1$
				final String jdbcPassword = attrs.get("password"); //$NON-NLS-1$
				
				RuntimeInstance[] instances = instanceManager.getInstances(IServiceConstants.SERVICE_MYSQL, clusterId);
				if (instances == null || instances.length == 0) {
					continue;
				}
				RuntimeInstance instance = instances[0];
				final String ip = instance.getIp();
				final int port = instance.getPort();
				
				final String jdbcUrl = "jdbc:mysql://" + ip + ":" + port + "/" + schemaName;  //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
				
				Properties properties = new Properties();
				properties.setProperty(ACQUIRE_INCREMENT, String.valueOf(model.getAcquireIncrement()));
				properties.setProperty(ACQUIRE_RETRY_ATTEMPTS, String.valueOf(model.getAcquireRetryAttempts()));
				properties.setProperty(ACQUIRE_RETRY_DELAY, String.valueOf(model.getAcquireRetryDelay()));
				properties.setProperty(CHECKOUT_TIMEOUT, String.valueOf(model.getCheckoutTimeout()));
				properties.setProperty(IDLE_CONNECTION_TEST_PERIOD, String.valueOf(model.getIdleConnectionTestPeriod()));
				properties.setProperty(INITIAL_POOL_SIZE, String.valueOf(model.getInitialPoolSize()));
				properties.setProperty(MAX_POOL_SIZE, String.valueOf(model.getMaxPoolSize()));
				properties.setProperty(MIN_POOL_SIZE, String.valueOf(model.getMinPoolSize()));
				properties.setProperty(DRIVER_CLASS, jdbcDriver);
				properties.setProperty(JDBC_URL, jdbcUrl);
				properties.setProperty(USER, jdbcUser);
				properties.setProperty(PASSWORD, jdbcPassword);
				
				if(logger.isInfoEnabled()) {
					logger.info("Will create datesource { URL:" + jdbcUrl + " }");
				}
				DataSource dataSource = DataSourceFactory.createDataSource(properties);
				if (dataSource != null) {
					DataSourceFactory.addDataSource(model.getDataSourceName(), dataSource);
				}
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("Init DataSource finish.");
		}

	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.runtime.IRuntimeListener#stop(com.primeton.paas.app.runtime.RuntimeEvent)
	 */
	public void stop(RuntimeEvent envent) {
		DataSourceFactory.clear();
	}
	
	/**
	 * init. <br>
	 */
	private void initSimulatorDataSource() {
		SimulatorDataSourceSetModel dataSourceSet = ConfigModelManager.getSimulatorDataSourceSetModel();
		if (dataSourceSet == null || dataSourceSet.getDataSourceSet() == null
				|| dataSourceSet.getDataSourceSet().isEmpty()) {
			if (logger.isWarnEnabled()) {
				logger.warn("DataSource config model not found.");
			}
			return;
		}
		Map<String, SimulatorDataSourceModel> models = dataSourceSet.getDataSourceSet();
		for (String name : models.keySet()) {
			SimulatorDataSourceModel model = models.get(name);
			if(model == null) {
				continue;
			}
			Properties properties = new Properties();
			properties.setProperty(ACQUIRE_INCREMENT, model.getAcquireIncrement());
			properties.setProperty(ACQUIRE_RETRY_ATTEMPTS, model.getAcquireRetryAttempts());
			properties.setProperty(ACQUIRE_RETRY_DELAY, model.getAcquireRetryDelay());
			properties.setProperty(CHECKOUT_TIMEOUT, model.getCheckoutTimeout());
			properties.setProperty(IDLE_CONNECTION_TEST_PERIOD, model.getIdleConnectionTestPeriod());
			properties.setProperty(INITIAL_POOL_SIZE, model.getInitialPoolSize());
			properties.setProperty(MAX_POOL_SIZE, model.getMaxPoolSize());
			properties.setProperty(MIN_POOL_SIZE, model.getMinPoolSize());
			properties.setProperty(DRIVER_CLASS, model.getDriverClass());
			properties.setProperty(JDBC_URL, model.getJdbcUrl());
			properties.setProperty(USER, model.getUser());
			properties.setProperty(PASSWORD, model.getPassword());
			
			logger.info("Will create datesource {0} {1}", new Object[] { name, model.getJdbcUrl() });
			DataSource dataSource = DataSourceFactory.createDataSource(properties);
			if (dataSource != null) {
				DataSourceFactory.addDataSource(model.getDataSourceName(), dataSource);
			}
			logger.info("Init DataSource finish.");
		}
	}

}
