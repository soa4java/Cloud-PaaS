/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.mqclient.api.Message;
import org.gocom.cloud.cesium.mqclient.api.MessageListener;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.cep.model.EventMessage;
import com.primeton.paas.manage.api.app.IServiceWarnStrategyManager;
import com.primeton.paas.manage.api.app.ServiceWarnStrategy;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceWarnStrategyManagerFactory;
import com.primeton.paas.manage.api.factory.TaskManagerFactory;
import com.primeton.paas.manage.api.impl.task.ServiceMonitorAlarmTask;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.ITaskManager;
import com.primeton.paas.manage.api.model.ICluster;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class ServiceUpperEventListener implements MessageListener{

	private static final String MSG_TYPE_KEY = "MSG_TYPE";
	private static final String MSG_TYPE_UPPER = "UPPER_SERVICE_MONITOR";
	
	private static final String CLUSTER_ID = "clusterId";
	
	private static ILogger logger = ManageLoggerFactory.getLogger(ServiceUpperEventListener.class);
	
	private static Map<String, String> flags = new ConcurrentHashMap<String, String>();
	
	private static IServiceWarnStrategyManager serviceWarnManager = ServiceWarnStrategyManagerFactory.getManager();
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager(IClusterManager.DEFAULT_TYPE);
	private static ITaskManager taskManager = TaskManagerFactory.getManager();
	
	/*
	 * (non-Javadoc)
	 * @see org.gocom.cloud.cesium.mqclient.api.MessageListener#handleMessage(org.gocom.cloud.cesium.mqclient.api.Message)
	 */
	public void handleMessage(Message<?> message) {
		if (message == null || !EventMessage.class.getName().equals(message.getClass().getName())) {
			return;
		}
		EventMessage msg = (EventMessage)message;
		if (logger.isDebugEnabled()) {
			logger.debug(message + " come in.");
		}
		Map<String, String> header = msg.getHeaders();
		Map<String, Object> body = msg.getBody();
		if (header == null || header.isEmpty()
				|| body == null || body.isEmpty()) {
			return;
		}
		
		String msgType = header.get(MSG_TYPE_KEY);
		String clusterId = header.get(CLUSTER_ID);
		
		if ((MSG_TYPE_UPPER.equals(msgType))) {
			String flag = flags.get(clusterId);
			if (flag == null) {
				Thread thread = new Thread(new ServiceAlarmThread(clusterId));
				thread.setDaemon(true);
				thread.setName(ServiceAlarmThread.class.getSimpleName());
				thread.start();
			}
		} else {
			return;
		}
	}

	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class ServiceAlarmThread implements Runnable {
		
		private String clusterId;
		
		public ServiceAlarmThread(String clusterId) {
			this.clusterId = clusterId;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				String flag = flags.get(clusterId);
				if (flag == null) {
					logger.info("Service clusterId: [" + clusterId + "] do alarm start and lock.");
					flags.put(clusterId, clusterId);
				} else {
					logger.info("Service clusterId: [" + clusterId + "] doing alarm and already locked, donothing and return.");
					return;
				}
				
				ServiceWarnStrategy strategy = serviceWarnManager.get(clusterId);
				if (strategy == null) {
					flags.remove(clusterId);
					logger.error("Service clusterId: [" + clusterId + "]'s strategy is null.");
					return;
				}
				if (!strategy.isEnable()) {
					logger.error("Service clusterId: [" + clusterId + "] alarm strategy is disabled.");
					return;
				}
				
				ICluster cluster = clusterManager.get(clusterId);
				if (cluster == null) {
					if (cluster == null) {
						logger.error("Service clusterId: [" + clusterId + "] Cluster is null.");
						return;
					}
				}
				logger.info("Service clusterId: [" + clusterId + "] Cluster [" + cluster.getId() + "] do [" + strategy.getAlarmType() + "] alarm.");
				
				long timeout = strategy.getIgnoreTime() * 60L * 1000L;
				
				ServiceMonitorAlarmTask task = new ServiceMonitorAlarmTask(strategy,timeout);
				
				String id = taskManager.add(task, timeout, "system");
				logger.info("Service clusterId: [" + clusterId + "] do [" + strategy.getAlarmType() + "] alarm create ServiceMonitorAlarmTask [" + id + "].");
				
				long begin = System.currentTimeMillis();
				int count = 0;
				while (true) {
					long diff = System.currentTimeMillis() - begin;
					if (diff > timeout) {
						logger.info("Service clusterId: [" + clusterId + "] do [" + strategy.getAlarmType() + "] alarm event listener hibernate stop and wake up.");
						break;
					}
					
					if (count > 0 && count % 5 == 4) {
						logger.info("Service clusterId: [" + clusterId + "] do [" + strategy.getAlarmType() + "] alarm event listener hibernate elapsed time [" + diff/1000L + "] sec.");
					}
					count++;
					ThreadUtil.sleep(60 * 1000L);
				}
				flags.remove(clusterId);
				logger.info("Service clusterId: [" + clusterId + "]  do [" + strategy.getAlarmType() + "] alarm end and unlock.");
			} catch (Exception e) {
				logger.info("Service clusterId: [" + clusterId + "]  do exception.",e);
			}
		}
		
	}
	
	
}
