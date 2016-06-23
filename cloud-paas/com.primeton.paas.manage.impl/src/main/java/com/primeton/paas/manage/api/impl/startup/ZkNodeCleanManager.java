/**
 *
 */
package com.primeton.paas.manage.api.impl.startup;

import java.util.List;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.manage.runtime.impl.utils.RuntimeZkPathUtil;
import org.gocom.cloud.cesium.zkclient.api.ZkClient;
import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;
import org.gocom.cloud.cesium.zkclient.api.util.ZkPathUtil;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 定期清理zookeeper上无用的服务集群节点. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public final class ZkNodeCleanManager {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(ZkNodeCleanManager.class);
	
	private static boolean isStartup = false;
	
	private static ZkNodeCleanTask task = null;

	/**
	 * Startup. <br>
	 */
	public static void startup() {
		if (isStartup) {
			logger.warn("Zookeeper node clean task has been startup.");
			return;
		}
		task = new ZkNodeCleanTask();
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.setName("Zookeeper_Node_Clean_Task"); //$NON-NLS-1$
		t.start();
		logger.info(task + " startup.");
	}
	
	/**
	 * Shutdown. <br>
	 */
	public static void shutdown() {
		if (!isStartup) {
			return;
		}
		if (null != task) {
			task.close();
			task = null;
		}
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	private static class ZkNodeCleanTask implements Runnable {
		
		private boolean shutdownSigal = false;
		
		private IClusterManager clusterManager;

		public ZkNodeCleanTask() {
			super();
			clusterManager = ClusterManagerFactory.getManager(IClusterManager.DEFAULT_TYPE);
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			while (true) {
				if (shutdownSigal) {
					break;
				}
				
				// Clean not exists cluster node
				clean();
				
				logger.info("Zookeeper node clean task, Wait 30m clean again.");
				
				ThreadUtil.sleep(1000L * 60 * 30); // 30m
			}
		}
		
		/**
		 * Clean. <br>
		 */
		protected void clean() {
			try {
				ZkClient zkClient = ZkClientFactory.getZkClient();
				if (null == zkClient) {
					return;
				}
				List<String> serviceTypes = ZkPathUtil.getChildren(zkClient, RuntimeZkPathUtil.SERVICE_ROOT_PATH); // Nginx/MySQL/HaProxy/etc
				if (null == serviceTypes || serviceTypes.isEmpty()) {
					return;
				}
				for (String type : serviceTypes) {
					try {
						String typePath = RuntimeZkPathUtil.SERVICE_ROOT_PATH
								+ RuntimeZkPathUtil.PATH_DELIMITER + type
								+ RuntimeZkPathUtil.PATH_DELIMITER
								+ RuntimeZkPathUtil.CLUSTER_ROOT_PATH;
						List<String> clusters = ZkPathUtil.getChildren(zkClient, typePath);
						if (null == clusters || clusters.isEmpty()) {
							continue;
						}
						for (String id : clusters) {
							if (!StringUtil.isEmpty(id)) {
								if (null == clusterManager.get(id)) {
									try {
										String clusterPath = typePath + RuntimeZkPathUtil.PATH_DELIMITER + id;
										zkClient.delete(clusterPath);
										logger.info("Clean zookeeper node " + clusterPath);
									} catch (Throwable t) {
										logger.error(t.getMessage());
									}
								}
							}
						}
					} catch (Throwable t) {
						logger.error(t.getMessage());
					}
				}
			} catch (Throwable t) {
				logger.error(t.getMessage());
			}
		}
		
		public void close() {
			shutdownSigal = true;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "ZkNodeCleanTask [" + super.toString() + "]";
		}
		
	}
	
}
