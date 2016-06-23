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
import com.primeton.paas.manage.api.app.IStrategyQueryManager;
import com.primeton.paas.manage.api.app.StretchStrategy;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.factory.StretchStrategyManagerFactory;
import com.primeton.paas.manage.api.factory.TaskManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.impl.task.SrvAutoStretchTask;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.ITaskManager;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.TomcatService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-11-15
 *
 */
public class AppStretchEventListener implements MessageListener {
	
	private static final String MSG_TYPE_KEY = "MSG_TYPE";
	private static final String MSG_TYPE_LOWER = "LOWER_APP_MONITOR";
	private static final String MSG_TYPE_UPPER = "UPPER_APP_MONITOR";
	
	private static final String ACTION_KEY = "EVENT_ACTION";
	private static final String ACTION_DESCREASE = "DECREASE";
	private static final String ACTION_INCREASE = "INCREASE";
	
	private static final String APP_NAME = "appName";
	
	private static ILogger logger = ManageLoggerFactory.getLogger(AppStretchEventListener.class);
	
	private static IWebAppManager webAppManager = WebAppManagerFactory.getManager();;
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager(IClusterManager.DEFAULT_TYPE);;
	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();
	private static IStrategyQueryManager queryManager = StretchStrategyManagerFactory.getQueryManager();
	private static ITaskManager taskManager = TaskManagerFactory.getManager();
	
	private static Map<String, String> flags = new ConcurrentHashMap<String, String>();

	/**
	 * Default. <br>
	 */
	public AppStretchEventListener() {
		super();
	}

	/* (non-Javadoc)
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
		String action = header.get(ACTION_KEY);
		String appName = header.get(APP_NAME);
		
		if (StringUtil.isNotEmpty(msgType, action)) {
			if ((MSG_TYPE_LOWER.equals(msgType) && ACTION_DESCREASE.equals(action))
					|| (MSG_TYPE_UPPER.equals(msgType) && ACTION_INCREASE.equals(action))) {
				String flag = flags.get(appName);
				if (flag == null) {
					Thread thread = new Thread(new AppStretchThread(appName, action));
					thread.setDaemon(true);
					thread.setName(AppStretchThread.class.getSimpleName());
					thread.start();
				}
			} else {
				return;
			}
		}
		
	}

	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	private static class AppStretchThread implements Runnable {
		
		private String appName;
		private String action;
		
		/**
		 * @param appName
		 * @param action
		 */
		public AppStretchThread(String appName, String action) {
			this.appName = appName;
			this.action = action;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				String flag = flags.get(appName);
				if (flag == null) {
					logger.info("WebApp [" + appName + "] do action [" + action + "] start and lock.");
					flags.put(appName, action);
				} else {
					logger.info("WebApp [" + appName + "] doing action [" + action + "] and already locked, donothing and return.");
					return;
				}
				
				WebApp webApp = webAppManager.get(appName);
				if (webApp == null) {
					flags.remove(appName);
					logger.error("WebApp [" + appName + "] is not exist.");
					return;
				}
				
				StretchStrategy strategy = queryManager.get(appName, action);
				if (strategy == null) {
					logger.error("WebApp [" + appName + "] stretch strategy [" + action + "]  is null.");
					return;
				}
				
				if (! strategy.getIsEnable()) {
					logger.error("WebApp [" + appName + "] stretch strategy [" + action + "] disable.");
					return;
				}
				
				String type = JettyService.TYPE;
				ICluster cluster = clusterManager.getByApp(appName, type);
				if (null == cluster) {
					type = TomcatService.TYPE;
					cluster = clusterManager.getByApp(appName, type);
				}
				if (cluster == null) {
					logger.error("WebApp [" + appName + "] Jetty and Tomcat Cluster is null.");
					return;
				}
				logger.info("WebApp [" + appName + "] "+type+" Cluster [" + cluster.getId() + "] do action [" + action + "].");
				
				int size = strategy.getStretchSize();
				IService[] instances = serviceQuery.getByCluster(cluster.getId());
				int now = instances == null ? 0 : instances.length;
				
				if (StretchStrategy.STRATEGY_INCREASE.equals(action)) {
					int max = cluster.getMaxSize();
					int left = cluster.getMaxSize() - now;
					if (left <= 0) {
						logger.warn("WebApp [" + appName + "] server instance is [" + now + "], equal max instances size [" + max + "], cannot increase.");
						flags.remove(appName);
						return;
					}
					size = size <= left ? size : left;
					if (size < 1) {
						flags.remove(appName);
						return;
					}
				} else {
					int min = cluster.getMinSize();
					int left = now - min;
					if (left <= 0) {
						flags.remove(appName);
						logger.warn("WebApp [" + appName + "] server instance is [" + now + "], equal min instances size [" + min + "], cannot decrease.");
						return;
					}
					size = size <= left ? size : left;
					if (size < 1) {
						flags.remove(appName);
						return;
					}
				}
				
				long timeout = strategy.getIgnoreTime() * 60L * 1000L;
				
				SrvAutoStretchTask task = new SrvAutoStretchTask(cluster.getId(), action, size, type, timeout);
				
				String id = taskManager.add(task, timeout, "system");
				logger.info("WebApp [" + appName + "] do action [" + action + "] create SrvAutoStretchTask [" + id + "].");
				
				long begin = System.currentTimeMillis();
				while (true) {
					long diff = System.currentTimeMillis() - begin;
					if (diff > timeout) {
						logger.info("WebApp [" + appName + "] do action [" + action + "] event listener hibernate stop and wake up.");
						break;
					}
					logger.info("WebApp [" + appName + "] do action [" + action + "] event listener hibernate elapsed time [" + diff/1000L + "] sec.");
					ThreadUtil.sleep(60 * 1000L);
				}
				flags.remove(appName);
				logger.info("WebApp [" + appName + "] do action [" + action + "] end and unlock.");
			} catch (Exception e) {
				logger.error("WebApp [" + appName + "] do action [" + action + "] exception", e);
			}
		}
		
	}
	
	
}
