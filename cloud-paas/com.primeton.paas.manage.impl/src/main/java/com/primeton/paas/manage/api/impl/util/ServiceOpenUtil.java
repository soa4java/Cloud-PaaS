/**
 * 
 */
package com.primeton.paas.manage.api.impl.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.app.IStretchStrategyManager;
import com.primeton.paas.manage.api.app.StretchStrategy;
import com.primeton.paas.manage.api.app.StretchStrategyException;
import com.primeton.paas.manage.api.cluster.CEPEngineCluster;
import com.primeton.paas.manage.api.cluster.CardBinCluster;
import com.primeton.paas.manage.api.cluster.CollectorCluster;
import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.cluster.KeepalivedCluster;
import com.primeton.paas.manage.api.cluster.MailCluster;
import com.primeton.paas.manage.api.cluster.MemcachedCluster;
import com.primeton.paas.manage.api.cluster.MySQLCluster;
import com.primeton.paas.manage.api.cluster.NginxCluster;
import com.primeton.paas.manage.api.cluster.OpenAPICluster;
import com.primeton.paas.manage.api.cluster.SVNCluster;
import com.primeton.paas.manage.api.cluster.SVNRepositoryCluster;
import com.primeton.paas.manage.api.cluster.SmsCluster;
import com.primeton.paas.manage.api.cluster.TomcatCluster;
import com.primeton.paas.manage.api.cluster.WarCluster;
import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.exception.WebAppException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.HostAssignManagerFactory;
import com.primeton.paas.manage.api.factory.HostManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.OrderManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.factory.StretchStrategyManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.impl.manager.OrderHandler;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IHostAssignManager;
import com.primeton.paas.manage.api.manager.IHostManager;
import com.primeton.paas.manage.api.manager.IJettyServiceManager;
import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.manager.IServiceManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.ITomcatServiceManager;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.service.CEPEngineService;
import com.primeton.paas.manage.api.service.CardBinService;
import com.primeton.paas.manage.api.service.CollectorService;
import com.primeton.paas.manage.api.service.HaProxyService;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.KeepalivedService;
import com.primeton.paas.manage.api.service.MailService;
import com.primeton.paas.manage.api.service.MemcachedService;
import com.primeton.paas.manage.api.service.MySQLService;
import com.primeton.paas.manage.api.service.NginxService;
import com.primeton.paas.manage.api.service.OpenAPIService;
import com.primeton.paas.manage.api.service.SVNRepositoryService;
import com.primeton.paas.manage.api.service.SVNService;
import com.primeton.paas.manage.api.service.SmsService;
import com.primeton.paas.manage.api.service.TomcatService;
import com.primeton.paas.manage.api.util.ExceptionUtil;
import com.primeton.paas.manage.api.util.RuntimeExec;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.exception.AddressException;
import com.primeton.paas.manage.spi.factory.VIPManagerFactory;
import com.primeton.paas.manage.spi.resource.IVIPManager;

/**
 * 
 * @author YanPing.Li (mailto:liyp@primeton.com)
 *
 */
@SuppressWarnings("deprecation")
public final class ServiceOpenUtil {

	private static ILogger logger = ManageLoggerFactory.getLogger(ServiceOpenUtil.class);
	
	private static IOrderManager orderManager = OrderManagerFactory.getManager();
	
	private static IHostAssignManager hostAssignManager = HostAssignManagerFactory.getManager();
	
	private static IWebAppManager appManager = WebAppManagerFactory.getManager(); 
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager(IClusterManager.DEFAULT_TYPE);
	private static IClusterManager jettyClusterManager = ClusterManagerFactory.getManager(JettyCluster.TYPE);
	private static IClusterManager mysqlClusterManager = ClusterManagerFactory.getManager(MySQLCluster.TYPE);
	private static IClusterManager svnRepoClusterManager = ClusterManagerFactory.getManager(SVNRepositoryCluster.TYPE);
	private static IClusterManager warClusterManager = ClusterManagerFactory.getManager(WarCluster.TYPE);
	private static IClusterManager memcachedClusterManager = ClusterManagerFactory.getManager(MemcachedCluster.TYPE);
	private static IClusterManager haproxyClusterManager =  ClusterManagerFactory.getManager(HaProxyCluster.TYPE);
	private static IClusterManager nginxClusterManager =  ClusterManagerFactory.getManager(NginxCluster.TYPE);
	private static IClusterManager openapiClusterManager = ClusterManagerFactory.getManager(OpenAPICluster.TYPE);
	private static IClusterManager keepalivedClusterManager = ClusterManagerFactory.getManager(KeepalivedCluster.TYPE);
	private static IClusterManager svnClusterManager = ClusterManagerFactory.getManager(SVNCluster.TYPE);
	private static IClusterManager cepEngineClusterManager = ClusterManagerFactory.getManager(CEPEngineCluster.TYPE);
	private static IClusterManager cardbinClusterManager = ClusterManagerFactory.getManager(CardBinCluster.TYPE);
	private static IClusterManager mailClusterManager = ClusterManagerFactory.getManager(MailCluster.TYPE);
	private static IClusterManager smsClusterManager = ClusterManagerFactory.getManager(SmsCluster.TYPE);
	private static IClusterManager collectorClusterManager =  ClusterManagerFactory.getManager(CollectorCluster.TYPE);
	private static IClusterManager tomcatClusterManager =  ClusterManagerFactory.getManager(TomcatService.TYPE);
	
	private static IVIPManager vipManager = VIPManagerFactory.getManager();
	private static IStretchStrategyManager stretchMgr = StretchStrategyManagerFactory.getManager();
	
	private static IHostManager hostManager = HostManagerFactory.getHostManager();
	
	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();
	
	/**
	 * Default. <br>
	 */
	private ServiceOpenUtil() {
		super();
	}

	/**
	 * 应用创建任务处理. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class ApplicationCreateThread implements Runnable {
		
		private Order order;
		private String appName;
		
		/**
		 * 
		 * @param order
		 * @param appName
		 */
		public ApplicationCreateThread(Order order, String appName) {
			this.order = order;
			this.appName = appName;
		}
		
		public void run() {
			long begin = System.currentTimeMillis();
		
			OrderItem appItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_APP);
			
			// save appliation & cep rule
			String jettyClusterId = null;
			try {
				createInnerApplication(order);
			} catch (Exception e) {
				logger.info("Save application error.{appName:"+ appName +"}. error:"+ e.getMessage());
				
				String itemId = appItem.getItemId();
				appItem.setFinishTime(new Date());
				appItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
				orderManager.updateItem(appItem); //update item handle endtime
				
				OrderHandler.notifyListeners(order.getOrderId(), itemId, appName);
			}
			long end = System.currentTimeMillis();
			logger.info("End save application.{appName:"+ appName +",clusterId:"+ jettyClusterId +"}.Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
		
	}
	
	/**
	 * 应用伸缩任务处理. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class AppStrategyThread implements Runnable {
		
		private String appName;
		
		public AppStrategyThread(String appName) {
			this.appName = appName;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			logger.info("Begin save application's strategy.{appName="+appName+"}.");

			try {
				// save default telescopic strategy - inc
				StretchStrategy incStrategy = new StretchStrategy();
				incStrategy.setAppName(appName);
				incStrategy.setStrategyName(StretchStrategy.GLOBAL_STRATEGY);
				incStrategy.setStrategyType(StretchStrategy.STRATEGY_INCREASE);
				logger.info("save application's increase strategy[global].{appName="+appName + "}.");
				stretchMgr.save(incStrategy);
			} catch (Exception e) {
				logger.error("save application's increase strategy {appName:"+appName + ",strategyName:"+StretchStrategy.GLOBAL_STRATEGY+"} error.error:" + e);
			}
			try {
				// save default telescopic strategy - dec
				StretchStrategy decStrategy = new StretchStrategy();
				decStrategy.setAppName(appName);
				decStrategy.setStrategyName(StretchStrategy.GLOBAL_STRATEGY);
				decStrategy.setStrategyType(StretchStrategy.STRATEGY_DECREASE);
				logger.info("save application decrease strategy [global].{appName="+appName + "}.");
				stretchMgr.save(decStrategy);
			} catch (Exception e) {
				logger.error("save application's decrease strategy {appName:"+appName + ",strategyName:"+StretchStrategy.GLOBAL_STRATEGY+"} error.error:" + e);
			}
			
			long end = System.currentTimeMillis();
			logger.info("End save application's strategy.{appName="+appName+"}.Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
	}
	
	/**
	 * Jetty服务创建任务处理. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class JettyCreateThread implements Runnable {
		
		private Order order;
		private String appName;
		private String sid; // 共享Session存储的Memcached服务集群标识
		
		/**
		 * 
		 * @param order
		 * @param appName
		 * @param sid
		 */
		public JettyCreateThread(Order order, String appName, String sid) {
			this.order = order;
			this.appName = appName;
			this.sid = sid;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			
			//create jetty-vm & jetty cluster & jetty service
			String jettyClusterId = null;
			try {
				jettyClusterId = createJetty(order, appName);
				if (null != sid && null != jettyClusterId) {
					bindClusters(jettyClusterId, sid);
				}
			} catch (Exception e) {
				logger.error("Create jetty error.(appName:"+ appName +"}. error:"+ e.getMessage());
				OrderItem jettyItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_JETTY);
				String itemId = jettyItem.getItemId();
				jettyItem.setFinishTime(new Date());
				jettyItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
				orderManager.updateItem(jettyItem); //update item handle endtime
				
				OrderHandler.notifyListeners(order.getOrderId(), itemId,
						appName);
			}
			long end = System.currentTimeMillis();
			logger.info("End create jetty.(appName:"+ appName +";clusterId:"+ jettyClusterId +").Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
		
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class TomcatCreateThread implements Runnable {
		
		private Order order;
		private String appName;
		private String sid; // 共享Session的Memcached集群标识
		
		/**
		 * 
		 * @param order
		 * @param appName
		 */
		public TomcatCreateThread(Order order, String appName, String sid) {
			this.order = order;
			this.appName = appName;
			this.sid = sid;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			String clusterId = null;
			try {
				clusterId = createTomcat(order, appName);
				if (null != sid && null != clusterId) {
					bindClusters(clusterId, sid);
				}
			} catch (Exception e) {
				logger.error("Create tomcat error.{appName:"+ appName +"}. error message:"+ e.getMessage());
				OrderItem item = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_TOMCAT);
				String itemId = item.getItemId();
				
				item.setFinishTime(new Date());
				item.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
				orderManager.updateItem(item); // update item handle end time
				
				OrderHandler.notifyListeners(order.getOrderId(), itemId,
						appName);
			}
			long end = System.currentTimeMillis();
			logger.info("Finish create tomcat.(appName:"+ appName +"; clusterId:"+ clusterId +"). Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
		
	}
	
	/**
	 * HaProxy服务创建任务处理. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class HaproxyCreateThread implements Runnable {
		
		private Order order;
		private String appName;
		
		/**
		 * 
		 * @param order
		 * @param appName
		 */
		public HaproxyCreateThread(Order order, String appName) {
			this.order = order;
			this.appName = appName;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			logger.info("Begin create haproxy { appName:"+ appName +" }.");
			OrderItem haproxyItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_HAPROXY);
			// create haproxy-cluster;bind haproxy-cluster with app
			String haproxyClusterId = null;
			try {
				haproxyClusterId = createInnerHaproxy(order,appName);
			} catch (Exception e) {
				logger.error("Create haproxy error.{appName:"+appName+"}. error:\n" + ExceptionUtil.getCauseMessage(e));
				// update orderItem status
				if (haproxyItem != null) {
					haproxyItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
					haproxyItem.setFinishTime(new Date());
					orderManager.updateItem(haproxyItem);
					
					OrderHandler.notifyListeners(order.getOrderId(),
							haproxyItem.getItemId(), appName);
				}
			}
			long end = System.currentTimeMillis();
			logger.info("End create haproxy.(appName:"+ appName +";clusterId:"+ haproxyClusterId +").Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
		
	}
	
	/**
	 * Nginx重启任务处理. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class NginxRestartThread implements Runnable {
		
		private Order order;
		private String appName;
		
		/**
		 * 
		 * @param order
		 * @param appName
		 */
		public NginxRestartThread(Order order, String appName){
			this.order = order;
			this.appName = appName;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			OrderItem nginxItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_NGINX);
			OrderItem appItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_APP);
			String isEnableDomain = OrderHandler.getItemStringValue(appItem, OrderItemAttr.ATTR_APP_IS_ENABLE_DOMAIN, "Y"); 
			String nginxClusterId = OrderHandler.getItemStringValue(nginxItem, OrderItemAttr.ATTR_CLUSTER_ID, "");
			String itemId = nginxItem == null ? "" : nginxItem.getItemId();
			// update item handle beginTime // begin
			if (nginxItem != null) {
				nginxItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
				nginxItem.setHandleTime(new Date());
				orderManager.updateItem(nginxItem);
			}
			if (appItem == null || !isEnableDomain.equals("N")) {
				// restart nginx ;
				// get nginxClusterId ; bind nginx-cluster with haproxy-cluster
				if (StringUtil.isEmpty(nginxClusterId)) {
					nginxClusterId = OrderHandler.getNginxClusterId(); 
				}
				//* restart nginx
				logger.info("Begin Restart nginx cluster {clusterId:" + nginxClusterId +"}.");
				restartNginx(nginxClusterId);
			}
			// update orderItem status //end
			if (nginxItem != null) {
				nginxItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
				nginxItem.setFinishTime(new Date());
				orderManager.updateItem(nginxItem);
			}
			OrderHandler.notifyListeners(order.getOrderId(), itemId, appName);
		}
		
	}
	
	/**
	 * 创建mysql服务. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class MysqlCreateThread implements Runnable {
		
		private Order order;
		private String appName;
		
		/**
		 * 
		 * @param order
		 * @param appName
		 */
		public MysqlCreateThread(Order order, String appName) {
			this.order = order;
			this.appName = appName;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			OrderItem mysqlItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_MYSQL);
			if (mysqlItem == null) {
				return ;
			}
			long begin = System.currentTimeMillis();
			logger.info("Begin create mysql { appName:"+ appName +" }.");
			
			String itemId = mysqlItem.getItemId();
			String mysqlClusterId = null;
			try {
				mysqlClusterId = createMySQL(order, appName);
			} catch (Exception e) {
				logger.error("create mysql error {appName:" +appName + "} error:" + e);
				mysqlItem.setFinishTime(new Date());
				mysqlItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
				orderManager.updateItem(mysqlItem); //update item handle endtime
				OrderHandler.notifyListeners(order.getOrderId(),itemId,appName);
			}
			long end = System.currentTimeMillis();
			logger.info("End create mysql.{appName:"+ appName +",clusterId:"+ mysqlClusterId +"}.Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
		
	}
	
	/**
	 * 创建应用WAR版本管理. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class WarCreateThread implements Runnable {

		private Order order;
		private String appName;
		
		/**
		 * 
		 * @param order
		 * @param appName
		 */
		public WarCreateThread(Order order, String appName) {
			this.order = order;
			this.appName = appName;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			logger.info("Begin create war. {appName:" + appName + "}.");
			
			//create war-cluster
			String warClusterId = null;
			try {
				warClusterId = createWar(order, appName);
			} catch (Exception e) {
				logger.error("create war error {appName:" +appName + "}.error:" + e);
				// update item handle beginTime
				OrderItem warItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_WAR);
				if (warItem != null) {
					warItem.setFinishTime(new Date());
					warItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
					orderManager.updateItem(warItem); // update item handle endtime
					
					OrderHandler.notifyListeners(order.getOrderId(),
							warItem.getItemId(), appName);
				}
			}
			long end = System.currentTimeMillis();
			logger.info("End create war.{appName:"+ appName +",clusterId:"+ warClusterId +"}.Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
	}
	
	/**
	 * 创建SVN资源库服务. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class SvnRepoCreateThread implements Runnable {
		
		private Order order;
		private String appName;
		
		/**
		 * 
		 * @param order
		 * @param appName
		 */
		public SvnRepoCreateThread(Order order, String appName){
			this.order = order;
			this.appName = appName;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			logger.info("Begin create svnRepo. {appName:"+appName+"}.");
			
			String svnRepoId = null;			
			
			try {
				svnRepoId = createSVNRepository(order, appName);
			} catch (Exception e) {
				logger.error("create svnRepository cluster error.{appName:" + appName +"}.error:" + e);
				
				OrderItem repoItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_SVN_REPO);
				String repoItemId = repoItem == null ? "" :repoItem.getItemId(); 
				
				if (repoItem != null) {
					repoItem.setFinishTime(new Date());
					repoItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
					orderManager.updateItem(repoItem); //update item handle endtime
					OrderHandler.notifyListeners(order.getOrderId(),repoItemId,appName);
				}
			}
			
			long end = System.currentTimeMillis();
			logger.info("End create svnRepo.{appName:"+ appName +",clusterId:"+ svnRepoId +"}.Time Spent : " + (end - begin) / 1000 + " seconds.");
			
		}
	}

	/**
	 * 创建缓存服务. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class MemcachedCreateThread implements Runnable {
		
		private Order order;
		private String appName;
		
		/**
		 * 
		 * @param order
		 * @param appName
		 */
		public MemcachedCreateThread(Order order, String appName) {
			this.order = order;
			this.appName = appName;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			logger.info("begin create memcached cluster.{appName:" + appName +"}.");
			
			String cachedClusterId = null;
			try {
				cachedClusterId = createMemcached(order,
						OrderItem.ITEM_TYPE_MEMCACHED, appName);
			} catch (Exception e) {
				logger.error("create memcached cluster error.{appName:" + appName +"}.error:" + e);
				OrderItem memcachedItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_MEMCACHED);
				if (memcachedItem != null) {
					memcachedItem.setFinishTime(new Date());
					memcachedItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
					orderManager.updateItem(memcachedItem); //update item handle endtime
					OrderHandler.notifyListeners(order.getOrderId(),memcachedItem.getItemId(),appName);
				}
			}
			
			long end = System.currentTimeMillis();
			logger.info("End create memcached {appName:"+ appName +",clusterId:"+ cachedClusterId +"}.Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class OpenAPICreateThread implements Runnable {

		private Order order;
		
		/**
		 * 
		 * @param order
		 * @param appName
		 */
		public OpenAPICreateThread(Order order, String appName) {
			this.order = order;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			logger.info("begin create openapi cluster.");
			
			String cachedClusterId = null;
			try {
				cachedClusterId = createOpenAPI(order);
			} catch (Exception e) {
				logger.error("create openapi cluster error.error:" + e);
				
				OrderItem openapiItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_OPENAPI);
				if (openapiItem != null) {
					openapiItem.setFinishTime(new Date());
					openapiItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
					orderManager.updateItem(openapiItem); //update item handle endtime
					
					OrderHandler.notifyListeners(order.getOrderId(),
							openapiItem.getItemId(), null);
				}
			}
			
			long end = System.currentTimeMillis();
			logger.info("End create memcached {clusterId:"+ cachedClusterId +"}.Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
		
	}
	
	/**
	 * CEP引擎创建. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class CEPEngineCreateThread implements Runnable {
		
		private Order order;
		
		/**
		 * 
		 * @param order
		 * @param appName
		 */
		public CEPEngineCreateThread(Order order, String appName) {
			this.order = order;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			logger.info("Begin create CEP cluster.");
			
			String cepClusterId = null;
			try {
				cepClusterId = createCEPEngine(order);
			} catch (Exception e) {
				logger.error("Create CEP cluster error." + e);
				
				OrderItem cepItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_CEPENGINE);
				if (cepItem != null) {
					cepItem.setFinishTime(new Date());
					cepItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
					orderManager.updateItem(cepItem); //update item handle endtime
					OrderHandler.notifyListeners(order.getOrderId(),
							cepItem.getItemId(), null);
				}
			}
			
			long end = System.currentTimeMillis();
			logger.info("End create CEP {clusterId:"+ cepClusterId +"}.Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
		
	}
	
	/**
	 * Marked @Deprecated by ZhongWen.Li
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	@Deprecated
	public static class SmsCreateThread implements Runnable {
		
		private Order order;
		
		/**
		 * 
		 * @param order
		 */
		public SmsCreateThread(Order order) {
			this.order = order;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			logger.info("begin create sms cluster.");

			String smsClusterId = null;
			try {
				smsClusterId = createSms(order);
			} catch (Exception e) {
				logger.error("create sms cluster error.error:" + e);
				
				OrderItem smsItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_SMS);
				if (smsItem != null) {
					smsItem.setFinishTime(new Date());
					smsItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
					orderManager.updateItem(smsItem); 
					OrderHandler.notifyListeners(order.getOrderId(),
							smsItem.getItemId(), null);
				}
			}
			long end = System.currentTimeMillis();
			logger.info("End create sms {clusterId:"+ smsClusterId +"}.Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
	}
	
	/**
	 * Marked @Deprecated by ZhongWen.Li
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	@Deprecated
	public static class CardbinCreateThread implements Runnable {
		
		private Order order;
		
		/**
		 * 
		 * @param order
		 */
		public CardbinCreateThread(Order order) {
			this.order = order;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long begin = System.currentTimeMillis();
			logger.info("begin create cardbin cluster.");
			
			String cardbinClusterId = null;
			try {
				cardbinClusterId = createCardbin(order);
			} catch (Exception e) {
				logger.error("create cardbin cluster error.error:" + e);
				OrderItem cardbinItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_CARDBIN);
				if(cardbinItem!=null){
					cardbinItem.setFinishTime(new Date());
					cardbinItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
					orderManager.updateItem(cardbinItem); 
					OrderHandler.notifyListeners(order.getOrderId(),
							cardbinItem.getItemId(), null);
				}
			}
			long end = System.currentTimeMillis();
			logger.info("End create cardbin {clusterId:"+ cardbinClusterId +"}.Time Spent : " + (end - begin) / 1000 + " seconds.");
		}
	}
	
	/**
	 * create Jetty-cluster & Jetty-service
	 * 
	 * @param order
	 * @param appName
	 * @return
	 */
	public static String createJetty(Order order, String appName) {
		
		String owner = order.getOwner();
		String handler = order.getHandler();
		OrderItem sessionItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_SESSION);
		String sessionCltId = OrderHandler.getItemStringValue(sessionItem, OrderItemAttr.ATTR_CLUSTER_ID, "");
		
		//update item handle beginTime
		if (sessionItem != null && StringUtil.isEmpty(sessionCltId)) {
			logger.error("Session service create is not complete");
			throw new RuntimeException("Session service create is not complete");
		}
		
		OrderItem jettyItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_JETTY);
		//update item handle beginTime
		if (jettyItem != null) {
			jettyItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			jettyItem.setHandleTime(new Date());
			orderManager.updateItem(jettyItem);
		}
		
		String displayName = OrderHandler.getItemStringValue(jettyItem, OrderItemAttr.ATTR_DISPLAY_NAME,"");
		String packageId = OrderHandler.getItemStringValue(jettyItem, OrderItemAttr.ATTR_HOSTPKG_ID, "");
		int minSize = OrderHandler.getItemIntValue(jettyItem, OrderItemAttr.ATTR_JETTY_MIN_SIZE, 1);
		int maxSize = OrderHandler.getItemIntValue(jettyItem, OrderItemAttr.ATTR_JETTY_MAX_SIZE, 1);
		int storageSize = OrderHandler.getItemIntValue(jettyItem, OrderItemAttr.ATTR_JETTY_STORAGE_SIZE, 1);
		String storagePath = SystemVariables.getJettyStoragePath();
		String isStandalone = OrderHandler.getItemStringValue(jettyItem, OrderItemAttr.ATTR_IS_STANDALONE, "Y");
		
		// create jetty cluster
		logger.info("Begin Create Jetty Cluster '" + displayName + "'.");
		JettyCluster cluster = new JettyCluster();
		cluster.setOwner(owner);
		cluster.setMinSize(minSize);
		cluster.setMaxSize(maxSize);
		cluster.setName(displayName);
		
		ICluster jettyCluster = null ;
		try {
			jettyCluster = jettyClusterManager.create(cluster);
		} catch (ClusterException e1) {
			logger.error("Create Jetty Cluster '" + displayName + "' error. error:" + e1);
		}
		
		if (jettyCluster == null) {
			throw new RuntimeException("Create Jetty Cluster '" + displayName + "' Exception.");
		}
		
		String jettyClusterId = jettyCluster.getId();
		logger.info("End Create Jetty Cluster.{clusterId = " + jettyClusterId + ",name="+ displayName+"}.");
		
		//bind jettycluster with app
		if (!StringUtil.isEmpty(appName)) {
			logger.info("Bind relationship between Jetty & Applicaiton {clusterId:" + jettyClusterId + ",appName="+ appName+"}.");
			appManager.bind(appName,jettyClusterId);
		}
		//bind jetty-cluster with sessin-cluster
		logger.info("Begin bind relationship between Jetty & Session {jettyCltId:" + jettyClusterId + ",sessinCltId="+ sessionCltId+"}.");
		bindClusters(jettyClusterId, sessionCltId);
		logger.info("Success bind relationship between Jetty & Session {jettyCltId:" + jettyClusterId + ",sessinCltId="+ sessionCltId+"}.");
		//create jetty service
		long begin = System.currentTimeMillis();
		logger.info("Begin Create Jetty Service '" + displayName + "'.");
		
		JettyService jettyService = new JettyService();
		jettyService.setName(displayName);
		jettyService.setAppName(appName);
		jettyService.setOwner(owner);
		jettyService.setEnableSession(sessionItem != null);
		jettyService.setStorageSize(storageSize);
		jettyService.setStoragePath(storagePath);
		jettyService.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$
		jettyService.setPackageId(packageId);
		jettyService.setMaxPermMemorySize(SystemVariables.getJvmMaxPermSize(packageId));
		jettyService.setMinPermMemorySize(SystemVariables.getJvmMinPermSize(packageId));
		jettyService.setMaxMemorySize(SystemVariables.getJvmMaxMemorySize(packageId));
		jettyService.setMinMemorySize(SystemVariables.getJvmMinMemorySize(packageId));
		
		jettyService.setCreatedBy(handler);
		jettyService.setCreatedDate(new Date());
		IJettyServiceManager serviceManager = ServiceManagerFactory.getManager(JettyService.TYPE);
		
		List<JettyService> list = null;
		try {
			list = serviceManager.add(jettyService, jettyClusterId, cluster.getMinSize());
		} catch (Exception e) {
			logger.error(e);
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("Begin remove jetty cluster.{clusterId:"+ jettyClusterId +"}.");
				ServiceRemoveUtil.removeJettyCluster(jettyClusterId, appName);
			}
			throw new RuntimeException("create jetty service error. {clusterId:"+ jettyClusterId +"}." +e.getMessage());
		}
		if (list == null || list.isEmpty()) {
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("create jetty service error. create instance return null.{clusterId:"+ jettyClusterId +"}.");
				ServiceRemoveUtil.removeJettyCluster(jettyClusterId, appName);
			}
			
			throw new RuntimeException("create jetty service error");
		}
		
		long end = System.currentTimeMillis();
		logger.info("End Create Jetty Service. Time Spend " +  (end-begin)/1000 +" seconds.");
		
		//* start jetty
		try {
			begin = System.currentTimeMillis();
			logger.info("Begin Start jetty cluster {clusterId:" + jettyClusterId +").");
			jettyClusterManager.start(jettyClusterId);
			end = System.currentTimeMillis();
			logger.info("End Start jetty cluster {clusterId:" + jettyClusterId +"}. Time Spend " +  (end-begin)/1000 +" seconds.");
		} catch (ServiceException e1) {
			logger.error("Start jetty cluster error. {clusterId:" + jettyClusterId +"}. error:" + e1);
		}
		
		// update orderItem status
		if (jettyItem != null) {
			jettyItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			jettyItem.setFinishTime(new Date());
			orderManager.updateItem(jettyItem);
		}
		
		String itemId = jettyItem.getItemId();
		OrderHandler.notifyListeners(order.getOrderId(),itemId,appName);
		return jettyClusterId;
	}
	
	/**
	 * 
	 * @param order
	 * @param appName
	 * @return
	 */
	public static String createTomcat(Order order, String appName) {
		if (null == order || StringUtil.isEmpty(appName)) {
			return null;
		}
		String owner = order.getOwner();
		String handler = order.getHandler();
		OrderItem item = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_TOMCAT);
		
		OrderItem sessionItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_SESSION);
		
		// update item handle beginTime
		if (item != null) {
			item.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			item.setHandleTime(new Date());
			orderManager.updateItem(item);
		}
		String displayName = OrderHandler.getItemStringValue(item,
				OrderItemAttr.ATTR_DISPLAY_NAME, ""); //$NON-NLS-1$
		String packageId = OrderHandler.getItemStringValue(item,
				OrderItemAttr.ATTR_HOSTPKG_ID, ""); //$NON-NLS-1$
		int minSize = OrderHandler.getItemIntValue(item,
				OrderItemAttr.ATTR_JETTY_MIN_SIZE, 1); //$NON-NLS-1$
		int maxSize = OrderHandler.getItemIntValue(item,
				OrderItemAttr.ATTR_JETTY_MAX_SIZE, 1); //$NON-NLS-1$
		String isStandalone = OrderHandler.getItemStringValue(item,
				OrderItemAttr.ATTR_IS_STANDALONE, "Y"); //$NON-NLS-1$
		
		// JVM Memory Settings
		int minPermSize = OrderHandler.getItemIntValue(item, OrderItemAttr.ATTR_JVM_MIN_PERM_SIZE, 64); //$NON-NLS-1$
		int maxPermSize = OrderHandler.getItemIntValue(item, OrderItemAttr.ATTR_JVM_MAX_PERM_SIZE, 128); //$NON-NLS-1$
		int minMemSize = OrderHandler.getItemIntValue(item, OrderItemAttr.ATTR_JVM_MIN_MEM_SIZE, 128); //$NON-NLS-1$
		int maxMemSize = OrderHandler.getItemIntValue(item, OrderItemAttr.ATTR_JVM_MAX_MEM_SIZE, 256); //$NON-NLS-1$

		logger.info("Begin Create Tomcat Cluster '" + displayName + "'.");
		TomcatCluster cluster = new TomcatCluster();
		cluster.setOwner(owner);
		cluster.setMinSize(minSize);
		cluster.setMaxSize(maxSize);
		cluster.setName(displayName);
		
		ICluster tCluster = null ;
		try {
			tCluster = tomcatClusterManager.create(cluster);
		} catch (ClusterException e1) {
			logger.error("Create Tomcat Cluster '" + displayName + "' error.\n" + e1);
		}
		if (tCluster == null) {
			throw new RuntimeException("Create Tomcat Cluster '" + displayName + "' error.");
		}
		String clusterId = tCluster.getId();
		logger.info("End Create Tomcat Cluster.{clusterId = " + clusterId + ", name="+ displayName+"}.");
		
		//	bind tomcat cluster with app
		if (!StringUtil.isEmpty(appName)) {
			logger.info("Bind relationship between Tomcat & Applicaiton {clusterId:" + clusterId + ", appName="+ appName+"}.");
			appManager.bind(appName, clusterId);
		}
		
		//	create tomcat service
		long begin = System.currentTimeMillis();
		logger.info("Begin Create Tomcat Service '" + displayName + "'.");
		
		TomcatService service = new TomcatService();
		service.setName(displayName);
		service.setAppName(appName);
		service.setOwner(owner);
		service.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$ 
		service.setPackageId(packageId);
		service.setEnableSession(sessionItem != null);
		// jvm config 
		if (service.isStandalone()) {
			service.setMaxPermSize(SystemVariables.getJvmMaxPermSize(packageId));
			service.setMinPermSize(SystemVariables.getJvmMinPermSize(packageId));
			service.setMaxMemorySize(SystemVariables.getJvmMaxMemorySize(packageId));
			service.setMinMemorySize(SystemVariables.getJvmMinMemorySize(packageId));
		} else {
			service.setMaxPermSize(maxPermSize);
			service.setMinPermSize(minPermSize);
			service.setMaxMemorySize(maxMemSize);
			service.setMinMemorySize(minMemSize);
		}
		
		service.setCreatedBy(handler);
		service.setCreatedDate(new Date());
		ITomcatServiceManager serviceManager = ServiceManagerFactory.getManager(TomcatService.TYPE);
		
		List<TomcatService> list = null;
		try {
			list = serviceManager.add(service, clusterId, cluster.getMinSize());
		} catch (Exception e) {
			logger.error(e);
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("Begin remove tomcat cluster.{clusterId:"+ clusterId +"}.");
				ServiceRemoveUtil.removeTomcatCluster(clusterId, appName);
			}
			throw new RuntimeException("Create tomcat service error. {clusterId:"+ clusterId +"}." + e.getMessage());
		}
		if (list == null || list.isEmpty()) {
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("Create tomcat service error, create tomcat instance return null.{clusterId:"+ clusterId +"}.");
				ServiceRemoveUtil.removeTomcatCluster(clusterId, appName);
			}
			throw new RuntimeException("Create tomcat service error.");
		}
		long end = System.currentTimeMillis();
		logger.info("End Create Tomcat Service. Time Spend " +  (end-begin)/1000 +" seconds.");
		
		// start tomcat
		try {
			
			begin = System.currentTimeMillis();
			logger.info("Begin Start tomcat cluster {clusterId:" + clusterId +").");
			tomcatClusterManager.start(clusterId);
			end = System.currentTimeMillis();
			logger.info("End Start tomcat cluster {clusterId:" + clusterId +"}. Time Spend " +  (end-begin)/1000 +" seconds.");
		} catch (ServiceException e1) {
			logger.error("Start tomcat cluster error. {clusterId:" + clusterId +"}. error:" + e1);
		}
		// update orderItem status
		if (item != null) {
			item.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			item.setFinishTime(new Date());
			orderManager.updateItem(item);
		}
		String itemId = item.getItemId();
		OrderHandler.notifyListeners(order.getOrderId(), itemId, appName);
		return clusterId;
	}
	
	
	/**
	 * create Mysql-cluster & Mysql-service
	 * 
	 * @param order
	 * @param appName
	 * @param vmOrderId
	 * @return
	 */
	public static String createMySQL(Order order, String appName) {
		String owner = order.getOwner();
		String handler = order.getHandler();
		OrderItem mysqlItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_MYSQL);
		
		// update item handle beginTime
		if (mysqlItem != null) {
			mysqlItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			mysqlItem.setHandleTime(new Date());
			orderManager.updateItem(mysqlItem);
		}
		
		String packageId = OrderHandler.getItemStringValue(mysqlItem, OrderItemAttr.ATTR_HOSTPKG_ID, "");
		String displayName = OrderHandler.getItemStringValue(mysqlItem, OrderItemAttr.ATTR_DISPLAY_NAME, "");
		String userName = OrderHandler.getItemStringValue(mysqlItem, OrderItemAttr.ATTR_MYSQL_USER_NAME, "");
		String password = OrderHandler.getItemStringValue(mysqlItem, OrderItemAttr.ATTR_MYSQL_PASSWORD, "");
		String characterSet = OrderHandler.getItemStringValue(mysqlItem, OrderItemAttr.ATTR_MYSQL_CHARACTERSET, "utf-8");
		String isBackUp = OrderHandler.getItemStringValue(mysqlItem, OrderItemAttr.ATTR_IS_BACKUP, "");
		int storageSize = OrderHandler.getItemIntValue(mysqlItem, OrderItemAttr.ATTR_MYSQL_STORAGE_SIZE, 2);
		String storagePath = SystemVariables.getMySQLStoragePath();
		String isStandalone = OrderHandler.getItemStringValue(mysqlItem, OrderItemAttr.ATTR_IS_STANDALONE, "Y");
		
		String mysqlClusterId = null;
		MySQLCluster cluster = new MySQLCluster();
		cluster.setOwner(owner);
		cluster.setName(displayName);
		cluster.setJdbcDriver("com.mysql.jdbc.Driver"); //$NON-NLS-1$
		cluster.setSchema("paas"); //$NON-NLS-1$
		cluster.setUser(userName);
		cluster.setPassword(password);
		cluster.setMinSize(1);
		cluster.setMaxSize("Y".equalsIgnoreCase(isBackUp) ? 2 : 1); //$NON-NLS-1$
		
		ICluster mysqlCluster = null;
		logger.info("Begin Create Mysql Cluster '" + displayName + "'.");
		try {
			mysqlCluster = mysqlClusterManager.create(cluster);
		} catch (ClusterException e1) {
			logger.error("Create mysql cluster error.{appName:"+ appName +"}. error:" + e1);
		}
		
		if( mysqlCluster == null){
			logger.info("Create mysql cluster error, Begin remove Mysql-VM.");
			throw new RuntimeException("Create mysql cluster error.");
		}
		
		mysqlClusterId = mysqlCluster.getId();
		logger.info("End Create Mysql Cluster {clusterId =" + mysqlClusterId + ",name=" + displayName+"}.");
		
		
		if (!StringUtil.isEmpty(appName)) {
			logger.info("Bind relationship between MySQL & Applicaiton {clusterId:" + mysqlClusterId + ",appName="+ appName+"}.");
			appManager.bind(appName, mysqlClusterId);
		}
		
		MySQLService mysqlService = new MySQLService();
		// mysqlService.setCaseSensitive(false);//caseSensitive
		// mysqlService.setCharset("utf-8") ;//charset
		mysqlService.setStandalone("Y".equals(isStandalone)); //$NON-NLS-1$
		mysqlService.setName(displayName);
		mysqlService.setSchema("paas"); //$NON-NLS-1$
		mysqlService.setOwner(owner);
		mysqlService.setUser(userName);
		mysqlService.setPassword(password); // BUG
		mysqlService.setCharacterSet(characterSet);
		mysqlService.setStorageSize(storageSize);
		mysqlService.setStoragePath(storagePath);
		mysqlService.setCreatedDate(new Date());
		mysqlService.setCreatedBy(handler);
		mysqlService.setStoragePath(SystemVariables.getMySQLStoragePath());
		mysqlService.setPackageId(packageId);
		
		//	create service instance 
		IServiceManager serviceManager = ServiceManagerFactory.getManager(MySQLService.TYPE);
		MySQLService instance = null;
		
		long begin = System.currentTimeMillis();
		logger.info("Begin create MySQL Service {clusterId=" + mysqlClusterId
				+ ",service name :" + displayName + "}.");
		try {
			instance = serviceManager.add(mysqlService, mysqlClusterId);
		} catch (ServiceException e) {
			logger.error("create mysql service error.error:" + e);
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("Begin remove mysql cluster.{clusterId:"+ mysqlClusterId +"}.");
				ServiceRemoveUtil.removeMysqlCluster(mysqlClusterId, appName);
			}
			throw new RuntimeException("create mysql service error."+e.getMessage());
		}
		if (instance == null) {
			logger.error("create mysql service error.{clusterId:"+ mysqlClusterId +"}");
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("create mysql service return null,Begin remove mysql cluster {clusterId:"+ mysqlClusterId +"}.");
				ServiceRemoveUtil.removeMysqlCluster(mysqlClusterId, appName);
			}
			throw new RuntimeException("create mysql service error");
		}
		long end = System.currentTimeMillis();
		logger.info("End Create Mysql Service. {" + instance.toString() + "}. Time Spend " +  (end-begin)/1000 +" seconds.");
		//* start mysql
		try {
			begin = System.currentTimeMillis();
			logger.info("Begin Start mysql cluster {clusterId:" + mysqlClusterId +"}.");
			mysqlClusterManager.start(mysqlClusterId);
			end = System.currentTimeMillis();
			logger.info("End Start mysql cluster {clusterId:" + mysqlClusterId +"}. Time Spend " +  (end-begin)/1000 +" seconds.");
		} catch (ServiceException e) {
			logger.error("Start mysql error.{clusterId:" + mysqlClusterId +"}. error:" + e);
		}
		// update orderItem status
		if (mysqlItem != null) {
			mysqlItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			mysqlItem.setFinishTime(new Date());
			orderManager.updateItem(mysqlItem);
		}
		
		String itemId = mysqlItem.getItemId();
		OrderHandler.notifyListeners(order.getOrderId(), itemId, appName);
		return mysqlClusterId;
	}
	
	/**
	 * create application  <br>
	 * 
	 * @param order
	 * @throws StretchStrategyException
	 * @throws WebAppException 
	 */
	public static void createInnerApplication(Order order)
			throws WebAppException {
		if (order == null) {
			return ;
		}
		OrderItem appItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_APP);
		String itemId = appItem.getItemId();
		
		// update item handle beginTime
		if (appItem != null) {
			appItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			appItem.setHandleTime(new Date());
			orderManager.updateItem(appItem);
		}
		
		String appName = OrderHandler.getItemStringValue(appItem, OrderItemAttr.ATTR_APP_NAME, "");
		String domain = OrderHandler.getItemStringValue(appItem, OrderItemAttr.ATTR_APP_DOMAIN, "");
		String displayName = OrderHandler.getItemStringValue(appItem, OrderItemAttr.ATTR_DISPLAY_NAME, "");
		String desc = OrderHandler.getItemStringValue(appItem, OrderItemAttr.ATTR_APP_DESC, "");
		
		logger.info("Begin save application '" + appName + "'.");
		// save application info 
		WebApp app = new WebApp();
		app.setName(appName);
		app.setDesc(desc);
		app.setDisplayName(displayName);
		app.setSecondaryDomain(domain);
		app.setType(WebApp.TYPE_FREE); //type
		app.setState(WebApp.STATE_WAIT_OPEN);//app state
		app.setOwner(order.getOwner());
		appManager.add(app);
		logger.info("End save application '" + appName + "'.");
		// update orderItem status
		if (appItem != null) {
			appItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			appItem.setFinishTime(new Date());
			orderManager.updateItem(appItem);
		}
		OrderHandler.notifyListeners(order.getOrderId(), itemId, appName);
	}
	
	/**
	 * create svnRepository-cluster
	 * 
	 * @param order
	 * @param appName
	 * @return
	 */
	public static String createSVNRepository(Order order, String appName) {
		if (null == order || StringUtil.isEmpty(appName)) {
			return null;
		}
		String owner = order.getOwner();
		String handler = order.getHandler();
		String displayName = "svnRepository-" + appName;
		// update item handle beginTime
		OrderItem svnRepoItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_SVN_REPO);
		if (svnRepoItem != null) {
			svnRepoItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			svnRepoItem.setHandleTime(new Date());
			orderManager.updateItem(svnRepoItem);
		}
		
		SVNRepositoryCluster cluster = new SVNRepositoryCluster();
		cluster.setName(displayName);
		cluster.setOwner(owner);
		cluster.setMinSize(1);
		cluster.setMaxSize(1);

		logger.info("Begin Create SvnRepository Cluster '" + displayName + "'.");
		ICluster svnRepoCluster = null;
		try {
			svnRepoCluster = svnRepoClusterManager.create(cluster);
		} catch (ClusterException e) {
			logger.error("Create SvnRepository cluster error {appName:" + appName +"}.error:" + e);
			throw new RuntimeException("Create SvnRepository cluster error {appName:" + appName +"}.");
		}
		if (svnRepoCluster == null) {
			return null;
		}
		String svnRepoClusterId = svnRepoCluster.getId();
		logger.info("End Create SvnRepository cluster {appName:" + appName + " ,clusterId:" + svnRepoClusterId + "}.");
		if (!StringUtil.isEmpty(appName)) {
			//bind svnRepo-clsuter with app
			logger.info("Bind relationship between SVNRepository & Applicaiton {clusterId:" + svnRepoClusterId + ",appName="+ appName+"}.");
			appManager.bind(appName, svnRepoClusterId);
		}
		
		SVNRepositoryService svnRepoService = new SVNRepositoryService();
		svnRepoService.setName(displayName);
		svnRepoService.setRepoName(appName);
		
		final String user = owner + "_" + appName;
		svnRepoService.setUser(user);	//lyp_sample
		svnRepoService.setPassword(user); //lyp_sample
		svnRepoService.setOwner(owner);
		svnRepoService.setCreatedBy(handler);
		svnRepoService.setCreatedDate(new Date());
		
		logger.info("Begin Create SvnRepository Service '" + displayName + "'.");
		IServiceManager serviceManager = ServiceManagerFactory.getManager(SVNRepositoryService.TYPE);
		try {
			serviceManager.add(svnRepoService, svnRepoClusterId);
		} catch (ServiceException e) {
			logger.error("Create SvnRepository Service error {appName:" + appName +"}.error:" + e);
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("Begin remove svnRepository.{clusterId"+ svnRepoClusterId+ ",appName:"+appName+"}.");
				ServiceRemoveUtil.removeSvnRepoCluster(svnRepoClusterId, appName);
			}
			throw new RuntimeException("Create SvnRepository Service error {appName:" + appName +"}.");
		}
		// update orderItem status
		if (svnRepoItem != null) {
			svnRepoItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			svnRepoItem.setFinishTime(new Date());
			orderManager.updateItem(svnRepoItem);
		}
		String itemId = svnRepoItem == null ? "" :svnRepoItem.getItemId();
		OrderHandler.notifyListeners(order.getOrderId(), itemId, appName);
		return svnRepoClusterId;
	}

	/**
	 * create war-cluster
	 * 
	 * @param order
	 * @param appName
	 * @return
	 * @throws ClusterException
	 * @throws ServiceException
	 */
	public static String createWar(Order order, String appName) {
		String owner = order.getOwner();
		String displayName = "war-" + appName;
		
		// update item handle beginTime
		OrderItem warItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_WAR);
		if (warItem != null) {
			warItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			warItem.setHandleTime(new Date());
			orderManager.updateItem(warItem);
		}
		
		WarCluster cluster = new WarCluster();
		cluster.setCurrentWarVersion("1.0.0"); //$NON-NLS-1$
		cluster.setMaxSize(1);
		cluster.setMinSize(1);
		cluster.setName(displayName);
		cluster.setOwner(owner);
		
		logger.info("End Create war cluster {appName:" + appName + " ,warName:" + displayName + "}.");
		ICluster warCluster = null;
		try {
			warCluster = warClusterManager.create(cluster);
		} catch (ClusterException e) {
			logger.error("Create war cluster error {appName:" + appName + "}.error:" + e);
		}
		
		if (warCluster == null) {
			throw new RuntimeException("Create war cluster error {appName:" + appName + "}.");
		}
		
		String warClusterId = warCluster.getId();
		logger.info("End Create war cluster {appName:" + appName + " ,warClusterId:" + warClusterId + "}.");
		
		if (!StringUtil.isEmpty(appName)) {
			//bind war-cluster with app
			logger.info("Bind relationship between War & Applicaiton {clusterId:" + warClusterId + ",appName="+ appName+"}.");
			appManager.bind(appName, warClusterId);
		}
		
		// update orderItem status
		if (warItem != null) {
			warItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			warItem.setFinishTime(new Date());
			orderManager.updateItem(warItem);
		}
		
		String itemId = warItem==null?"":warItem.getItemId();
		OrderHandler.notifyListeners(order.getOrderId(), itemId, appName);
		
		return warClusterId;
	}

	/**
	 * create memcached-cluster
	 * 
	 * @param order
	 * @param appName
	 * @return
	 * @throws ClusterException
	 * @throws ServiceException
	 */
	public static String createMemcached(Order order, String itemType,
			String appName) {
		if (null == order) {
			return null;
		}
		String owner = order.getOwner();
		String handler = order.getHandler();
		
		OrderItem memItem = OrderHandler.getItemByType(order, itemType);
		int memcachedSize = OrderHandler.getItemIntValue(memItem, OrderItemAttr.ATTR_MEMCACHED_SIZE, 128);
		// update item handle beginTime
		if (memItem != null) {
			memItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			memItem.setHandleTime(new Date());
			orderManager.updateItem(memItem);
		}
		
		String displayName = OrderHandler.getItemStringValue(memItem, OrderItemAttr.ATTR_DISPLAY_NAME, "");
		String packageId = OrderHandler.getItemStringValue(memItem, OrderItemAttr.ATTR_HOSTPKG_ID, "");//host package
		int minNum = OrderHandler.getItemIntValue(memItem, "minNum", 1); //$NON-NLS-1$
		String isStandalone = OrderHandler.getItemStringValue(memItem, OrderItemAttr.ATTR_IS_STANDALONE, "N"); //$NON-NLS-1$
		int sliceSize = SystemVariables.getMemcachedSlice();
		int srvCnt = sliceSize == 0 ? 1 : memcachedSize / sliceSize ;
		int maxNum = OrderHandler.getItemIntValue(memItem, "maxNum", srvCnt); //$NON-NLS-1$
		
		int maxConnsize = OrderHandler.getItemIntValue(memItem, OrderItemAttr.ATTR_MEMCACHED_MAX_CONN_SIZE, 
				SystemVariables.getMemcachedMaxConnSize());
		
		MemcachedCluster cluster = new MemcachedCluster();
		cluster.setName(displayName);
		cluster.setOwner(owner);
		cluster.setMinSize(minNum);
		cluster.setMaxSize(maxNum);
		cluster.setMemcachedSize(memcachedSize);
		
		logger.info("Begin Create Memcached Cluster '" + displayName + "'.");
		ICluster memcacheCluster = null;
		try {
			memcacheCluster = memcachedClusterManager.create(cluster);
		} catch (ClusterException e) {
			logger.error("Create memcached cluster error.{appName:"+appName +",cluster-name:"+displayName+"}.error:" + e);
		}
		
		String cachedClusterId = memcacheCluster == null ? null:memcacheCluster.getId();
		if (cachedClusterId == null) {
			throw new RuntimeException("Create memcached cluster error.{appName:"+appName +",cluster-name:"+displayName+"}.");
		}
		logger.info("End Create Memcached Cluster.{clusterId = " + cachedClusterId + ",name="+ displayName+"}.");
		
		if (itemType.equals(OrderItem.ITEM_TYPE_SESSION)) {
			logger.info("Begin update clusterId to memItemAttrs.");
			OrderItemAttr attr = new OrderItemAttr();
			attr.setAttrName(OrderItemAttr.ATTR_CLUSTER_ID);
			attr.setAttrValue(cachedClusterId);
			attr.setItemId(memItem.getItemId());
			List<OrderItemAttr> attrList = new ArrayList<OrderItemAttr>();
			attrList.add(attr);
			memItem.setAttrList(attrList);
			orderManager.updateItemAttrs(memItem);
			logger.info("End update clusterId to memItemAttrs.");
			
			//bind memcached-cluster with app
			logger.info("Bind relationship between Session & Applicaiton {clusterId:" + cachedClusterId + ",appName="+ appName+"}.");
			appManager.bind(appName, cachedClusterId);
			
		} else if (!StringUtil.isEmpty(appName)) {
			//bind memcached-cluster with app
			logger.info("Bind relationship between Memcached & Applicaiton {clusterId:" + cachedClusterId + ",appName="+ appName+"}.");
			appManager.bind(appName, cachedClusterId);
		}
		
		MemcachedService cachedService = null;
		String[] hostIps = null;
		long timeout = hostAssignManager.getInstallTimeout(MemcachedService.TYPE);
		try {
			logger.info("Apply Memcached hosts :{hostNum="+srvCnt+",timeout="+timeout+"}.");
			
			hostIps = hostAssignManager.apply(packageId, MemcachedService.TYPE, "Y".equals(isStandalone), srvCnt, timeout); //$NON-NLS-1$
		} catch (Exception e) {
			logger.error("Apply Memcached hosts error.{hostNum="+srvCnt+"}.error:" + e);
		}
		if (hostIps == null || hostIps.length < srvCnt) {
			logger.error("Can not find enougy memcached host.{clusterId="+ cachedClusterId +",num="+srvCnt+"}.");
			
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.info("Begin remove memcached cluster. {clusterId:"+ cachedClusterId + "}.");
				ServiceRemoveUtil.removeMemcachedCluster(cachedClusterId,appName);
			}
			throw new RuntimeException("Create memcache cancelled, can not find enough host resources.");
		}
		
		for (int i = 1; i <= srvCnt ; i++) {
			cachedService = new MemcachedService();
			cachedService.setName(displayName + "-" + i); //$NON-NLS-1$
			cachedService.setMemorySize(sliceSize);
			cachedService.setMaxConnectionSize(maxConnsize);
			cachedService.setPackageId(packageId);
			cachedService.setOwner(owner);
			cachedService.setCreatedBy(handler);
			cachedService.setCreatedDate(new Date());
			cachedService.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$
			cachedService.setIp(hostIps[i-1]); 
			
			logger.info("Begin Create Memcached Service '" + displayName + "'.");
			IServiceManager serviceManager = ServiceManagerFactory.getManager(MemcachedService.TYPE);
			try {
				serviceManager.add(cachedService, cachedClusterId);
			} catch (ServiceException e) {
				logger.error("Create memcached service error.{appName:"+appName +"}.error:" + e);
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.info("Begin remove memcached cluster. {clusterId:"+ cachedClusterId + ",appName:"+appName+ "}.");
					ServiceRemoveUtil.removeMemcachedCluster(cachedClusterId, appName);
				}
				throw new RuntimeException("Create memcached service error.{clusterId:"+ cachedClusterId + ",appName:"+appName+ "}.");
			}
		}
		
		//start memcached
		if (!StringUtil.isEmpty(cachedClusterId)) {
			logger.info("Begin Start memcached {appName:"+appName +",clusterId:" + cachedClusterId +"}.");
			try {
				memcachedClusterManager.start(cachedClusterId);
			} catch (ServiceException e) {
				logger.error("Start memcached error {appName:"+appName +",clusterId:" + cachedClusterId +"}.error:" + e);
			}
		}
	
		// update orderItem status
		if (memItem != null) {
			memItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			memItem.setFinishTime(new Date());
			orderManager.updateItem(memItem);
		}
		
		String itemId = memItem == null ? "" : memItem.getItemId();
		OrderHandler.notifyListeners(order.getOrderId(), itemId, appName);
		return cachedClusterId;
	}
	
	/**
	 * 
	 * @param pId
	 * @param cId
	 * @return
	 */
	public static boolean bindClusters(String pId, String cId) {
		if (StringUtil.isEmpty(pId) || StringUtil.isEmpty(cId)) {
			return false;
		}
		try {
			clusterManager.bindCluster(pId, cId);
		} catch (ClusterException e) {
			logger.error("bind cluster error { pId:" + pId + ",cId:" + cId +"}.error:" + e);
			return false;
		}
		return true;
	}

	/**
	 * @param order
	 * @return
	 */
	public static String createOpenAPI(Order order) {
		if (order == null ) {
			return null;
		}
		String owner = order.getOwner();
		String handler = order.getHandler();
		
		OrderItem openapiItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_OPENAPI);
		String itemId = openapiItem == null ? "" : openapiItem.getItemId();

		// update item handle beginTime
		if (openapiItem != null) {
			openapiItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			openapiItem.setHandleTime(new Date());
			orderManager.updateItem(openapiItem);
		}
		
		String displayName = OrderHandler.getItemStringValue(openapiItem, OrderItemAttr.ATTR_DISPLAY_NAME,"");
		int maxSize = OrderHandler.getItemIntValue(openapiItem, OrderItemAttr.ATTR_CLUSTER_MAX_SIZE, 1);//max size
		
		logger.info("Begin create OpenAPI Cluster '" + displayName + "'.");
		OpenAPICluster cluster = new OpenAPICluster();
		cluster.setMinSize(1);
		cluster.setMaxSize(maxSize);
		cluster.setName(displayName);
		cluster.setOwner(owner);
		
		
		ICluster openapiCluster = null;
		try {
			openapiCluster = openapiClusterManager.create(cluster);
		} catch (ClusterException e) {
			logger.error("Create openapi cluster error.{cluster-name:"+displayName+"}.error:" + e);
		}
		
		String openapiClusterId = openapiCluster == null ? null:openapiCluster.getId();
		if (openapiClusterId == null) {
			throw new RuntimeException("Create openapi cluster error.{cluster-name:"+displayName+"}.");
		}
		logger.info("End Create openapi Cluster.{clusterId = " + openapiClusterId + ",name="+ displayName+"}.");
		
		// OpenAPI 
		String packageId = OrderHandler.getItemStringValue(openapiItem, OrderItemAttr.ATTR_HOSTPKG_ID, "");
		String isStandalone = OrderHandler.getItemStringValue(openapiItem, OrderItemAttr.ATTR_IS_STANDALONE, "Y"); //$NON-NLS-1$
		
		OpenAPIService openapiService = new OpenAPIService();
		openapiService.setName(displayName);
		openapiService.setPackageId(packageId);//packageId
		openapiService.setOwner(owner);
		openapiService.setCreatedBy(handler);
		openapiService.setCreatedDate(new Date());
		openapiService.setStandalone("Y".equals(isStandalone));
		
		/*
		int maxMemorySize = OrderHandler.getItemIntValue(openapiItem, OrderItemAttr.ATTR_MAX_MEMORY,0);
		int minMemorySize = OrderHandler.getItemIntValue(openapiItem, OrderItemAttr.ATTR_MIN_MEMORY, 0);
		int maxPermMemorySize = OrderHandler.getItemIntValue(openapiItem, OrderItemAttr.ATTR_MAX_PERM_MEMORY, 0);
		int minPermMemorySize = OrderHandler.getItemIntValue(openapiItem, OrderItemAttr.ATTR_MIN_PERM_MEMORY, 0);
		openapiService.setMaxMemorySize(maxMemorySize);
		openapiService.setMinMemorySize(minMemorySize);
		openapiService.setMaxPermMemorySize(maxPermMemorySize);
		openapiService.setMinPermMemorySize(minPermMemorySize);
		*/
		openapiService.setMaxPermMemorySize(SystemVariables.getJvmMaxPermSize(packageId));
		openapiService.setMinPermMemorySize(SystemVariables.getJvmMinPermSize(packageId));
		openapiService.setMaxMemorySize(SystemVariables.getJvmMaxMemorySize(packageId));
		openapiService.setMinMemorySize(SystemVariables.getJvmMinMemorySize(packageId));
		
		logger.info("Begin Create openapi Service '" + displayName + "'.");
		IServiceManager serviceManager = ServiceManagerFactory.getManager(OpenAPIService.TYPE);
		OpenAPIService instance = null;
		try {
			instance = serviceManager.add(openapiService, openapiClusterId);
		} catch (ServiceException e) {
			logger.error("Create openapi service error.{clusterId:"+ openapiClusterId + "}.error:" + e);
		}
		
		if (instance == null) {
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("Begin remove openapi cluster. {clusterId: "+ openapiClusterId+"}.");
				ServiceRemoveUtil.removeOpenAPICluster(openapiClusterId);
			}
			throw new RuntimeException("Create openapi service error.{clusterId:"+ openapiClusterId + "}.");
		}
		
		logger.info("End create openapi service : " + instance.toString());
		
		//start openapi
		if (!StringUtil.isEmpty(openapiClusterId)) {
			logger.info("Begin Start openapi {clusterId:" + openapiClusterId +"}.");
			try {
				openapiClusterManager.start(openapiClusterId);
			} catch (ServiceException e) {
				logger.error("Start openapi error {clusterId:" + openapiClusterId +"}.error:" + e);
			}
		}
	
		// update orderItem status
		if (openapiItem != null) {
			openapiItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			openapiItem.setFinishTime(new Date());
			orderManager.updateItem(openapiItem);
		}
		
		OrderHandler.notifyListeners(order.getOrderId(), itemId, null);
		return openapiClusterId;
	}

	/**
	 * 
	 * @param order
	 * @param appName
	 * @return
	 */
	public static String createInnerHaproxy(Order order, String appName) {
		String owner = order.getOwner();
		String handler = order.getHandler();
		
		OrderItem haproxyItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_HAPROXY);
		// update item handle beginTime
		if (haproxyItem != null) {
			haproxyItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			haproxyItem.setHandleTime(new Date());
			orderManager.updateItem(haproxyItem);
		}
		
		// String appName = OrderHandler.getItemStringValue(haproxyItem, OrderItemAttr.ATTR_APP_NAME, "");  
		String packageId = OrderHandler.getItemStringValue(haproxyItem, OrderItemAttr.ATTR_HOSTPKG_ID, ""); //$NON-NLS-1$
		String isBackup = OrderHandler.getItemStringValue(haproxyItem, OrderItemAttr.ATTR_IS_BACKUP, "N"); //$NON-NLS-1$
		String isStandalone = OrderHandler.getItemStringValue(haproxyItem, OrderItemAttr.ATTR_IS_STANDALONE, "N"); //$NON-NLS-1$
		int minSize = OrderHandler.getItemIntValue(haproxyItem, OrderItemAttr.ATTR_CLUSTER_MAX_SIZE, 1);//
		String displayName = OrderHandler.getItemStringValue(haproxyItem, OrderItemAttr.ATTR_DISPLAY_NAME, "");
		
		if (StringUtil.isEmpty(displayName)) {
			displayName = "haproxy-" + appName; //$NON-NLS-1$
		}
		OrderItem appItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_APP);
		String domain = OrderHandler.getItemStringValue(appItem, OrderItemAttr.ATTR_APP_DOMAIN, ""); 
		String isEnableDomain = OrderHandler.getItemStringValue(appItem, OrderItemAttr.ATTR_APP_IS_ENABLE_DOMAIN, "Y"); //$NON-NLS-1$
		
		OrderItem nginxItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_NGINX);
		if (StringUtil.isEmpty(domain) && nginxItem != null) {
			domain = OrderHandler.getItemStringValue(nginxItem, OrderItemAttr.ATTR_APP_DOMAIN, "");
		}
		
		String nginxClusterId = OrderHandler.getItemStringValue(nginxItem, OrderItemAttr.ATTR_CLUSTER_ID, "");
		// get nginxClusterId ; bind nginx-cluster with haproxy-cluster
		if (StringUtil.isEmpty(nginxClusterId)) {
			nginxClusterId = OrderHandler.getNginxClusterId();
		}
		
		String sslProtocalType = OrderHandler.getItemStringValue(haproxyItem, OrderItemAttr.ATTR_HAPROXY_PROTOCOL_TYPE, HaProxyCluster.PROTOCOL_HTTP);
		String sslCertificateZipFile = OrderHandler.getItemStringValue(haproxyItem, OrderItemAttr.ATTR_HAPROXY_SSL_CERTIFICATE_PATH, "");
		String sslCertificateBasePath = SystemVariables.getSslCertificatePath();
		List<NginxService> nginxList = serviceQuery.getByCluster(nginxClusterId, NginxService.class);
		if (nginxList != null && nginxList.get(0) != null) {
			NginxService nginxSrv = nginxList.get(0);
			String _sslCertificatePath = nginxSrv.getSslCertificatePath();
			if (!StringUtil.isEmpty(_sslCertificatePath)) {
				sslCertificateBasePath = _sslCertificatePath;
			}
		}
		
		String sslCertificatePath = sslCertificateBasePath + File.separator + domain;
		String sslCertificate = "";
		String sslCertificateKey = "";
		String sslCaCertificate = "";
	
		if (sslProtocalType != null && !sslProtocalType.equals(HaProxyCluster.PROTOCOL_HTTP)	
				&& StringUtil.isNotEmpty(sslCertificateZipFile)){
			
			File _sslDir = new File(sslCertificatePath);
			if(!_sslDir.exists() || !_sslDir.isDirectory()) {
				_sslDir.mkdir();
			}
			
			//unzip ssl.zip -d /primeton/paas/temp/test.primeton.com
			String cmd = "unzip " + sslCertificateZipFile + " -d " + _sslDir; //$NON-NLS-1$
			RuntimeExec exec = new RuntimeExec();
			try {
				logger.error("Execute shell command : [" + cmd + "] ");
				exec.execute(cmd, 1000L*20); //20s
			} catch (Exception e) {
				logger.error("Execute shell command : [" + cmd + "] error, error message:\n" + e.getMessage());
			}
			
			File[] files = _sslDir.listFiles();
			if (files == null || files.length < 1) {
				throw new RuntimeException("Can not Find ssl certificate files in '" + sslCertificatePath + "'.{ tmpPath=' " + sslCertificateZipFile + "' }.");
			}
			
			File sslTmpFile = new File(sslCertificateZipFile);
			if(sslTmpFile.exists()) {
				logger.info("Remove temp ssl certificate zip file {appName:"+ appName + ",sslTempPath:" + sslTmpFile + "}.");
				ServiceRemoveUtil.delFile(sslTmpFile);
			}
			
			//.server.crt  &  .server.key & .client.crt| .ca.crt  
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				String _fname = file.getName();
				String _fpath = file.getPath();//
				if (StringUtil.isEmpty(_fname)) {
					continue;
				}
				//if (_fname.endsWith("server.crt")) {
				if(_fname.indexOf("server.crt") >= 0){
					sslCertificate = _fpath;
				//} else if (_fname.endsWith("ca.crt") || _fname.endsWith("client.crt")) {
				} else if (_fname.indexOf("ca.crt") >= 0 || _fname.indexOf("client.crt") >= 0 ){
					sslCaCertificate = _fpath;
				//} else if (_fname.endsWith("server.key")) {
				} else if (_fname.indexOf("server.key") >= 0) {
					sslCertificateKey = _fpath;
				} else {
					continue;
				}
			}
		}
		
		//haproxy rel  service (Jetty | OpenAPI | SMS | Cardbin)
		String relClusterId = OrderHandler.getItemStringValue(haproxyItem, OrderItemAttr.ATTR_HAPROXY_REL_SRV_ID, "");
		if (StringUtil.isEmpty(relClusterId) && StringUtil.isNotEmpty(appName)) {
			//Bind with application (jetty||upjas)
			//get jetty cluster
			ICluster jetty = clusterManager.getByApp(appName, JettyCluster.TYPE);
			ICluster tomcat = clusterManager.getByApp(appName, TomcatCluster.TYPE);
			if (jetty != null) {
				relClusterId = jetty.getId();
			}
			if (tomcat != null) {
				relClusterId = tomcat.getId();
			}
			if (jetty == null && tomcat == null) {
				throw new RuntimeException("Can not get jetty or tomcat cluster by appName. {appName:" + appName +"}.");
			}
		}
		if (StringUtil.isEmpty(relClusterId)) {
			throw new RuntimeException("Create Haproxy cancelled,Can not get relation clusterId.");
		} 
		
		//create haproxy cluster
		HaProxyCluster cluster = new HaProxyCluster();
		cluster.setOwner(owner);
		cluster.setName(displayName);
		cluster.setMinSize(minSize);
		cluster.setMaxSize("Y".equalsIgnoreCase(isBackup) ? 2 : 1); //$NON-NLS-1$
		cluster.setDomain(domain);
		cluster.setIsEnableDomain(isEnableDomain);
		cluster.setProtocolType(sslProtocalType);
		cluster.setSslCertificate(sslCertificate);
		cluster.setSslCertificateKey(sslCertificateKey);
		if (HaProxyCluster.PROTOCOL_MUTUAL_HTTPS.equals(sslProtocalType) 
				|| HaProxyCluster.PROTOCOL_MUTUAL_HTTP_HTTPS.equals(sslProtocalType)) {
			cluster.setCaSslCertificate(sslCaCertificate);
		}
		
		ICluster haproxyCluster = null;
		logger.info("Begin create inner haproxy cluster.{name=" + displayName + "}.");
		try {
			haproxyCluster = haproxyClusterManager.create(cluster);
		} catch (ClusterException e) {
			logger.error("Create inner haproxy cluster error.{appName:"+ appName+"}.error:" + e);
			
			throw new RuntimeException(e);
		}
		
		String haproxyClusterId = haproxyCluster.getId();
		logger.info("End create inner haproxy cluster.{clusterId = " + haproxyClusterId + ",name="+ displayName+"}.");
		
		//bind haproxy-cluster with app
		if (!StringUtil.isEmpty(appName)) {
			logger.info("Bind relationship between inner haproxy & applicaiton {clusterId:" + haproxyClusterId + ",appName="+ appName+"}.");
			appManager.bind(appName, haproxyClusterId);
		}
		
		if (!StringUtil.isEmpty(relClusterId)) {
			//bind haproxy-cluster with relation-cluster
			bindClusters(haproxyClusterId, relClusterId);
		}
		
		String balance = OrderHandler.getItemStringValue(haproxyItem, OrderItemAttr.ATTR_HAPROXY_BALANCE, HaProxyService.BALANCE_ROUNDROBIN);
		long connTimeout = Long.parseLong(OrderHandler.getItemStringValue(haproxyItem, OrderItemAttr.ATTR_HAPROXY_CONN_TIMEOUT, "0"));
		if (connTimeout <= 0) {
			connTimeout = SystemVariables.getHaproxyConnTimeout();
		}
		String healthCheckUri = OrderHandler.getItemStringValue(haproxyItem, OrderItemAttr.ATTR_HAPROXY_HEALTHCHECKURI, SystemVariables.getHaproxyHealthUrl());
		String protocal = OrderHandler.getItemStringValue(haproxyItem, OrderItemAttr.ATTR_HAPROXY_PROTOCOL, SystemVariables.getHaproxyProtocal());
		int maxConnection = OrderHandler.getItemIntValue(haproxyItem, OrderItemAttr.ATTR_HAPROXY_MAX_CONNNECTION, SystemVariables.getHaproxyMaxConnSize());
		
		HaProxyService service = new HaProxyService();
		service.setName(displayName);
		service.setHealthUrl(healthCheckUri); //healthUrl
		service.setMaxConnectionSize(maxConnection); //maxConnectionSize 
		
		if (StringUtil.isNotEmpty(balance)) {
			service.setBalance(balance);
		}
		if (StringUtil.isNotEmpty(protocal)) {
			service.setProtocal(protocal);
		}
		
		service.setConnTimeout(connTimeout);
		service.setPackageId(packageId);
		service.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$
		service.setCreatedBy(handler);
		service.setOwner(owner);
		service.setCreatedDate(new Date());
		service.setState(IService.STATE_NOT_RUNNING);
		
		long timeout = hostAssignManager.getInstallTimeout(HaProxyService.TYPE);
		int num = "Y".equalsIgnoreCase(isBackup) ? 2 : 1 ; //$NON-NLS-1$
		String[] hostIps = null;
		try {
			logger.info("Apply haproxy type host.{packageId:"+packageId +",num:"+num +",isStandalone" + isStandalone +"}.");
			hostIps = hostAssignManager.apply(packageId, HaProxyService.TYPE, "Y".equalsIgnoreCase(isStandalone), num, timeout); //$NON-NLS-1$
		} catch (Throwable e) {
			logger.error(e);
		}
		if (hostIps == null || hostIps.length < num) {
			throw new RuntimeException("Create haproxy service error, not enough hosts.{packageId:"+packageId +",num:"+num +"}.");
		}
		// create master
		HaProxyService _master = null;
		HaProxyService master = ServiceUtil.copy(service);
		master.setHaMode(IService.HA_MODE_MASTER);
		master.setIp(hostIps[0]);
		logger.info("Begin create haproxy master service.{clusterId = " + haproxyClusterId + ",hostIp:"+ hostIps[0] + "}.");
		
		IServiceManager haproxySrvManager = ServiceManagerFactory.getManager(HaProxyService.TYPE);
		try {
			_master = haproxySrvManager.add(master, haproxyClusterId);
		} catch (ServiceException e) {
			logger.error("Create haproxy master service error.{clusterId = " + haproxyClusterId +"}.error:" + e);
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("Begin remove haproxy.{clusterId:"+ haproxyClusterId + "}.");
				ServiceRemoveUtil.removeHaproxyCluster(haproxyClusterId, null);
				// release host
				try {
					logger.info("release host [ip:"+hostIps[0]+"].");
					hostManager.release(hostIps[0]);
				} catch (HostException eHost) {
					if(logger.isErrorEnabled()) {
						logger.error("Release host [ip:" + hostIps[0] + "] error." + eHost.getMessage());
					}
				}
			}
			throw new RuntimeException("Create haproxy master service error.{clusterId:" + haproxyClusterId +"}.");
		}
		if (_master == null) {
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.info("Create haproxy instance error, result is null.Begin remove haproxy.{clusterId:"+ haproxyClusterId + "}.");
				ServiceRemoveUtil.removeHaproxyCluster(haproxyClusterId, null);
				// release host
				try {
					logger.info("release host [ip:"+hostIps[0]+"].");
					hostManager.release(hostIps[0]);
				} catch (HostException eHost) {
					if(logger.isErrorEnabled()) {
						logger.error("Release host [ip:" + hostIps[0] + "] error." + eHost.getMessage());
					}
				}
			}
			
			throw new RuntimeException("Create haproxy instance error, result is null.{appName:"+ appName+"}.");
		}
		logger.info("End create haproxy master service :" +  _master.toString());
		
		if ("Y".equalsIgnoreCase(isBackup)) { //$NON-NLS-1$
			// create slave
			HaProxyService _slave = null;
			HaProxyService slave = ServiceUtil.copy(service);
			slave.setHaMode(IService.HA_MODE_SLAVE);
			slave.setIp(hostIps[1]);
			// create slaver haproxy service
			logger.info("Begin create haproxy slaver service.{clusterId" + haproxyClusterId + ",hostIp:"+ hostIps[1] + "}.");
			try {
				_slave = haproxySrvManager.add(slave,haproxyClusterId);
			} catch (ServiceException e) {
				logger.error("Create haproxy slaver service error.{clusterId:" + haproxyClusterId +"}.error:" + e);
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.error("Begin remove haprxy.{clusterId:"+ haproxyClusterId + "}.");
					ServiceRemoveUtil.removeHaproxyCluster(haproxyClusterId, null);
					// release host
					try {
						logger.info("release host [ip:"+hostIps[1]+"].");
						hostManager.release(hostIps[1]);
					} catch (HostException eHost) {
						logger.error("Release host [ip:" + hostIps[1] + "] error." + eHost.getMessage());
					}
				}
				throw new RuntimeException("Create haproxy slaver service error.{clusterId:" + haproxyClusterId +"}.");
			}
			
			if (_slave == null) {
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.info("Create haproxy instance error, result is null.Begin remove haprxy.{clusterId:"+ haproxyClusterId + "}.");
					ServiceRemoveUtil.removeHaproxyCluster(haproxyClusterId, null);
					// release host
					try {
						logger.info("release host [ip:"+hostIps[1]+"].");
						hostManager.release(hostIps[1]);
					} catch (HostException eHost) {
						if(logger.isErrorEnabled()) {
							logger.error("Release host [ip:" + hostIps[1] + "] error." + eHost.getMessage());
						}
					}
				}
				throw new RuntimeException("Create haproxy instance error, result is null.{appName:"+ appName+"}.");
			}
			logger.info("End create haproxy slaver service :" +  _slave.toString());
		}
		
		if (!StringUtil.isEmpty(nginxClusterId)) {
			ServiceOpenUtil.bindClusters(nginxClusterId, haproxyClusterId);
		}
		// update orderItem status
		if (haproxyItem != null) {
			haproxyItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			haproxyItem.setFinishTime(new Date());
			orderManager.updateItem(haproxyItem);
		}
		// start haproxy
		logger.info("Begin Start haproxy cluster '" + haproxyClusterId + "'.");
		try {
			haproxyClusterManager.start(haproxyClusterId);
		} catch (ServiceException e1) {
			logger.error("Start haproxy error.{clusterId:" + haproxyClusterId +"}. error:" + e1);
		}
		
		OrderHandler.notifyListeners(order.getOrderId(), haproxyItem.getItemId(), appName);
		return haproxyClusterId;
	}
	
	/**
	 * restart nginx cluter
	 * 
	 * @param clusterId
	 * @return
	 */
	public static boolean restartNginx(String clusterId) {
		try {
			logger.info("Restart Nginx Cluster '" + clusterId + ".");
			nginxClusterManager.restart(clusterId);
			return true;
		} catch (ServiceException e) {
			logger.error("Restart Nginx cluster error.error:" + e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	public static String createNginx(Order order) {
		if (null == order) {
			return null;
		}
		String owner = order.getOwner();
		String handler = order.getHandler();
		OrderItem nginxItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_NGINX);
		String itemId = nginxItem == null ? "" : nginxItem.getItemId();
		// update item handle beginTime
		if (nginxItem != null) {
			nginxItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			nginxItem.setHandleTime(new Date());
			orderManager.updateItem(nginxItem);
		}
		String displayName = OrderHandler.getItemStringValue(nginxItem, OrderItemAttr.ATTR_DISPLAY_NAME, "");
		String packageId = OrderHandler.getItemStringValue(nginxItem, OrderItemAttr.ATTR_HOSTPKG_ID, "");
		String isBackup = OrderHandler.getItemStringValue(nginxItem, OrderItemAttr.ATTR_IS_BACKUP, "N"); //$NON-NLS-1$
		int minSize = OrderHandler.getItemIntValue(nginxItem, OrderItemAttr.ATTR_CLUSTER_MIN_SIZE, 1);
		String isStandalone = OrderHandler.getItemStringValue(nginxItem, OrderItemAttr.ATTR_IS_STANDALONE, "Y"); //$NON-NLS-1$
		
		//create nginx cluster
		NginxCluster cluster = new NginxCluster();
		cluster.setOwner(owner);
		cluster.setName(displayName);
		cluster.setMinSize(minSize);
		cluster.setMaxSize("Y".equalsIgnoreCase(isBackup) ? 2 : 1); //$NON-NLS-1$
		
		ICluster nginxCluster = null;
		logger.info("Begin create nginx cluster.{name =" + displayName + "}.");
		try {
			nginxCluster = nginxClusterManager.create(cluster);
		} catch (ClusterException e) {
			logger.error("Create nginx cluster error.{name ="+displayName+"}.error:" + e);
			throw new RuntimeException(e);
		}
		
		String nginxClusterId = nginxCluster.getId();
		logger.info("End create nginx cluster.{clusterId = " + nginxClusterId + ",name="+ displayName+"}.");
		
		String[] hostIps = null;
		long timeout = hostAssignManager.getInstallTimeout(NginxService.TYPE);
		int num = "Y".equalsIgnoreCase(isBackup) ? 2 : 1; //$NON-NLS-1$
		
		if ("Y".equalsIgnoreCase(isBackup)) { //$NON-NLS-1$
			try {
				hostIps = hostAssignManager.applyMS(packageId, NginxService.TYPE, timeout);
			} catch (Exception e) {
				logger.error("Apply nginx host error. error:" + e);
			}
		} else {
			try {
				hostIps = hostAssignManager.apply(packageId, NginxService.TYPE, "Y".equals(isStandalone), 1, timeout);
			} catch (Exception e) {
				logger.error("Apply nginx host error.error:" + e);
			}
		}
		
		if (hostIps == null || hostIps.length < num) {
			logger.error("Create nginx services error, can not find enougy host.{clusterId="+nginxClusterId+",packageId= " + packageId +",num="+num +"}.");
			
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("Begin remove nginx cluster.{clusterId="+nginxClusterId+"}.");
				ServiceRemoveUtil.removeNginx(nginxClusterId);
			}
			throw new RuntimeException("Didn't apply enough to host resources. Can not create nginx service.");
		}
		
		int workerProcesses = OrderHandler.getItemIntValue(nginxItem, OrderItemAttr.ATTR_NGINX_WORKER_PROCESSES, -1);
		int workerConnections = OrderHandler.getItemIntValue(nginxItem, OrderItemAttr.ATTR_NGINX_WORKER_CONNECTIONS,-1);
		int keepaliveTimeout = OrderHandler.getItemIntValue(nginxItem, OrderItemAttr.ATTR_NGINX_KEEPALIVE_TIMEOUT, -1);
		//	int typesHashMaxSize = OrderHandler.getItemIntValue(nginxItem, OrderItemAttr.ATTR_NGINX_TYPES_HASH_MAX_SIZE, -1);
		int clientMaxBodySize = OrderHandler.getItemIntValue(nginxItem, OrderItemAttr.ATTR_NGINX_CLIENT_MAX_BODY_SIZE, -1);
		String allowAccessHosts = OrderHandler.getItemStringValue(nginxItem, OrderItemAttr.ATTR_NGINX_ALLOW_ACCESS_HOSTS, "");
		
		String sslCertificatePath = OrderHandler.getItemStringValue(nginxItem, OrderItemAttr.ATTR_NGINX_SSL_CERTIFICATE_PATH, "");
		
		NginxService service = new NginxService();
		service.setCreatedDate(new Date());
		service.setCreatedBy(handler);
		service.setOwner(owner);
		service.setPackageId(packageId);
		service.setState(IService.STATE_NOT_RUNNING);
		service.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$
		service.setSslCertificatePath(sslCertificatePath);
		service.setWorkerProcesses(workerProcesses > 0 ? workerProcesses : 1);
		service.setWorkerConnections(workerConnections > 0 ? workerConnections : 1024);
		service.setKeepaliveTimeout(keepaliveTimeout > 0 ? keepaliveTimeout : 65);
		if (StringUtil.isNotEmpty(allowAccessHosts) ) {
			service.setAllowAccessHosts(allowAccessHosts);
		}
		service.setClientMaxBodySize(clientMaxBodySize > 0 ? clientMaxBodySize : 128); // MB
		
		//create master 
		NginxService _master = null;
		NginxService master = ServiceUtil.copy(service);
		master.setName(displayName + "-master"); //$NON-NLS-1$
		master.setIp(hostIps[0]);
		master.setHaMode(IService.HA_MODE_MASTER);
		
		logger.info("Begin create master nginx service.{clusterId = " + nginxClusterId + ", ip = "+ hostIps[0] + "}.");
		IServiceManager nginxSrvManger = ServiceManagerFactory.getManager(NginxService.TYPE);
		try {
			_master = nginxSrvManger.add(master, nginxClusterId);
		} catch (ServiceException e) {
			logger.error("Create master nginx service error.{clusterId = " + nginxClusterId +"}.error:" + e);
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("Begin remove nginx cluster .{clusterId:"+ nginxClusterId + "}.");
				ServiceRemoveUtil.removeNginx(nginxClusterId);
			}
			throw new RuntimeException("Create haproxy master service error.{clusterId:" + nginxClusterId +"}.");
		}
		
		KeepalivedCluster keepalived_cluster = new KeepalivedCluster();
		keepalived_cluster.setMaxSize("Y".equalsIgnoreCase(isBackup) ? 2 : 1); //$NON-NLS-1$
		keepalived_cluster.setMinSize(1);
		keepalived_cluster.setOwner(owner);
		keepalived_cluster.setName(displayName + "-" + KeepalivedService.TYPE); //$NON-NLS-1$
		
		ICluster _keepalived = null;
		logger.info("Begin create keepalived cluster {name=" + displayName + "}.");
		try {
			_keepalived = keepalivedClusterManager.create(keepalived_cluster);
		} catch (ClusterException e1) {
			logger.error("create keepalived cluster error.{name=" + displayName + "}.error:" + e1);
		}

		if(_keepalived == null){
			if (SystemVariables.isCleanAfterProcessError()) {
				ServiceRemoveUtil.removeHaproxyCluster(nginxClusterId, null);
			}
			orderManager.updateItemStatus(itemId, OrderItem.ITEM_STATUS_FAILED);
			OrderHandler.srvCreateNotifyListeners(order.getOrderId(), itemId);
			throw new RuntimeException("create keepalived cluster return null.{name=" + displayName + "}.");
		}

		String keepalivedClusterId = _keepalived.getId();
		logger.info("End create keepalived cluster.{clusterId = " + keepalivedClusterId + ",name="+ displayName+"}.");
		
		logger.info("Bind nginx cluster '"+nginxClusterId + "' with keepalived cluster '" + keepalivedClusterId +"' .");
		bindClusters(nginxClusterId, keepalivedClusterId);
		
		String vip = null;
		try {
			vip = vipManager.apply();
		} catch (AddressException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		if (vip == null) {
			logger.error("Can not create keepalived instance, apply vip resource failured.");
			
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.info("Begin remove nginx cluster. {clusterId: "+ nginxClusterId+"}.");
				ServiceRemoveUtil.removeNginx(nginxClusterId);
				logger.info("Begin remove keppalived cluster. {clusterId: "+ keepalivedClusterId+"}.");
				ServiceRemoveUtil.removeKeepalivedCluster(keepalivedClusterId);
			}
			orderManager.updateItemStatus(itemId, OrderItem.ITEM_STATUS_FAILED);
			OrderHandler.srvCreateNotifyListeners(order.getOrderId(), itemId);
			throw new RuntimeException("Can not create keepalived instance, apply vip resource failured.");
		}
		
		KeepalivedService ks = new KeepalivedService();
		ks.setVirtualIpAddress(vip);
		ks.setAdvertInt(1);
		ks.setAuthPass("1111"); //$NON-NLS-1$
		ks.setAuthType("PASS"); //$NON-NLS-1$
		ks.setCreatedBy(service.getCreatedBy());
		ks.setCreatedDate(new Date());
		ks.setInterface("eth0"); //$NON-NLS-1$
		ks.setNotificationEmail("root@localhost"); //$NON-NLS-1$
		ks.setNotificationEmailFrom("localhost"); //$NON-NLS-1$
		ks.setOwner(service.getOwner());
		ks.setPackageId(service.getPackageId());
		ks.setSmtpConnectTimeout(30);
		ks.setSmtpServer("localhost"); //$NON-NLS-1$
		ks.setStandalone("Y".equals(isStandalone)); //$NON-NLS-1$
		ks.setVirtualRouterId("51"); //$NON-NLS-1$
		ks.setVrrpScriptInterval(2);
		ks.setVrrpScriptWeight(2);
		
		KeepalivedService ksm = ServiceUtil.copy(ks); 
		String masterIp = _master.getIp();
		ksm.setHaMode(IService.HA_MODE_MASTER);
		ksm.setIp(masterIp);
		ksm.setMcastSrcIp(_master.getIp());
		ksm.setName("master"); //$NON-NLS-1$
		ksm.setParentId(_master.getId());
		ksm.setPriority(200);
		ksm.setRouterId(_master.getId());
		ksm.setVrrpState(KeepalivedService.VRRP_STATE_MASTER);
		String script = SystemVariables.getBinHome() + "/" + NginxService.TYPE  //$NON-NLS-1$
				+ "/bin/monitor_" + _master.getId() + ".sh"; //$NON-NLS-1$ //$NON-NLS-2$ 
		ksm.setVrrpScriptPath(script);
		IServiceManager keepalivedServiceManager = ServiceManagerFactory.getManager(KeepalivedService.TYPE);
		
		String type = KeepalivedService.TYPE;
		
		Host host = hostManager.get(masterIp);
		if (host != null && !host.getType().contains(type)) {
			try {
				long begin = System.currentTimeMillis();
				logger.info("Begin install service " + type + " on host '" + hostIps[0] + "'.");
				keepalivedServiceManager.install(masterIp, timeout);
				long end = System.currentTimeMillis();
				logger.info("End install service " + type + " on host '" + masterIp + "'. Time spents " + (end-begin)/1000L + " seconds.");
				
				host.addType(type);
				hostManager.update(host);
			} catch (Exception e) {
				logger.error("Install keepalived service on host '" + masterIp + "' error. error:" + e);
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.info("Begin remove nginx cluster.{clusterId="+nginxClusterId+"}.");
					ServiceRemoveUtil.removeNginx(nginxClusterId);
					ServiceRemoveUtil.removeKeepalivedCluster(keepalivedClusterId);
				}
				throw new RuntimeException("Apply nginx host error, install keepalived service on host '" + masterIp + "' error.");
			}
		}
		
		try {
			keepalivedServiceManager.add(ksm, keepalivedClusterId);
		} catch (ServiceException e) {
			logger.error("Create master keepalived service error.error:" + e);
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.info("Begin remove nginx cluster. {clusterId: "+ nginxClusterId+"}.");
				ServiceRemoveUtil.removeNginx(nginxClusterId);
				logger.info("Begin remove keppalived cluster. {clusterId: "+ keepalivedClusterId+"}.");
				ServiceRemoveUtil.removeKeepalivedCluster(keepalivedClusterId);
			}
			throw new RuntimeException(e);
		}
		
		//create slaver
		if ("Y".equalsIgnoreCase(isBackup) && StringUtil.isNotEmpty(hostIps[1])) { //$NON-NLS-1$
			NginxService slave = ServiceUtil.copy(service);
			NginxService _slave = null;
			slave.setIp(hostIps[1]);
			slave.setName(displayName + "-master");
			slave.setHaMode(IService.HA_MODE_SLAVE);
			logger.info("Begin create nginx slaver service.{clusterId=" + nginxClusterId + "}.");
			try {
				_slave = nginxSrvManger.add(slave,nginxClusterId);
			} catch (ServiceException e) {
				logger.error("Create nginx slaver service error.{clusterId:" + nginxClusterId +"}.error:" + e);
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.error("Begin remove nginx.{clusterId:"+ nginxClusterId + "}.");
					ServiceRemoveUtil.removeNginx(nginxClusterId);
				}
				throw new RuntimeException("Create nginx slaver service error.{clusterId:" + nginxClusterId +"}.");
			}
		
			KeepalivedService kss = ServiceUtil.copy(ks);
			kss.setHaMode(IService.HA_MODE_SLAVE);
			kss.setIp(_slave.getIp());
			kss.setMcastSrcIp(_slave.getIp());
			kss.setName("slave"); //$NON-NLS-1$
			kss.setParentId(_slave.getId());
			kss.setPriority(100);
			kss.setRouterId(_slave.getId());
			kss.setVrrpState(KeepalivedService.VRRP_STATE_BACKUP);
			String _script = SystemVariables.getBinHome() + "/" + NginxService.TYPE //$NON-NLS-1$ 
					+ "/bin/monitor_" + _slave.getId() + ".sh"; //$NON-NLS-1$ //$NON-NLS-2$ 
			kss.setVrrpScriptPath(_script);
			
			try {
				keepalivedServiceManager.add(kss, keepalivedClusterId);
			} catch (ServiceException e) {
				logger.error("Create slave keepalived service error.error:" + e);
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.error("Begin remove nginx cluster. {clusterId: "+ nginxClusterId+"}.");
					ServiceRemoveUtil.removeNginx(nginxClusterId);
					logger.error("Begin remove keppalived cluster. {clusterId: "+ keepalivedClusterId+"}.");
					ServiceRemoveUtil.removeKeepalivedCluster(keepalivedClusterId);
				}
				throw new RuntimeException(e);
			}
		}
		// start nginx   
		logger.info("Begin start nginx cluster '" + nginxClusterId + "'.");
		try {
			nginxClusterManager.start(nginxClusterId);
		} catch (ServiceException e1) {
			logger.error("Start nginx error.{clusterId:" + nginxClusterId +"}. error:" + e1);
		}
		// start keepalived
		logger.info("Begin Start Keepalived cluster '" + keepalivedClusterId + "'.");
		try {
			keepalivedClusterManager.start(keepalivedClusterId);
		} catch (ServiceException e1) {
			logger.error("Start Keepalived error.{clusterId:" + keepalivedClusterId +"} error:" + e1);
		}
		// update orderItem status
		if (nginxItem != null) {
			nginxItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			nginxItem.setFinishTime(new Date());
			orderManager.updateItem(nginxItem);
		}
		
		OrderHandler.srvCreateNotifyListeners(order.getOrderId(), itemId);
		return nginxClusterId;
	}
	
	/**
	 * create svn 
	 * @param order
	 * @return
	 */
	public static String createSVN(Order order) {
		if (null == order) {
			return null;
		}
		String owner = order.getOwner();
		String handler = order.getHandler();
		
		OrderItem svnItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_SVN);
		String itemId = svnItem == null ? "" : svnItem.getItemId();
		
		// update item handle beginTime
		if (svnItem != null) {
			svnItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			svnItem.setHandleTime(new Date());
			orderManager.updateItem(svnItem);
		}
		
		String displayName = OrderHandler.getItemStringValue(svnItem, OrderItemAttr.ATTR_DISPLAY_NAME, ""); //$NON-NLS-1$
		String packageId = OrderHandler.getItemStringValue(svnItem, OrderItemAttr.ATTR_HOSTPKG_ID, ""); //$NON-NLS-1$
		//	String isBackup = OrderHandler.getItemStringValue(svnItem, OrderItemAttr.ATTR_IS_BACKUP, "N");
		String repoRoot = OrderHandler.getItemStringValue(svnItem, OrderItemAttr.ATTR_SVN_REPO_ROOT, "repos"); //$NON-NLS-1$
		int maxSize = OrderHandler.getItemIntValue(svnItem, OrderItemAttr.ATTR_CLUSTER_MAX_SIZE, 1);//
		String isStandalone = OrderHandler.getItemStringValue(svnItem, OrderItemAttr.ATTR_IS_STANDALONE, "Y"); //$NON-NLS-1$
		
		// create svn cluster
		SVNCluster cluster = new SVNCluster();
		cluster.setOwner(owner);
		cluster.setName(displayName);
		cluster.setMinSize(1);
		cluster.setMaxSize(maxSize);
		
		ICluster svnCluster = null;
		logger.info("Begin create svn cluster.{name ="+displayName+"}.");
		try {
			svnCluster = svnClusterManager.create(cluster);
		} catch (ClusterException e) {
			logger.error("Create svn cluster error.{name ="+displayName+"}.error:" + e);
			throw new RuntimeException(e);
		}
		
		
		String svnClusterId = svnCluster.getId();
		logger.info("End create svn cluster.{clusterId = " + svnClusterId + ",name="+ displayName+"}.");
		
		SVNService service = new SVNService();
		service.setCreatedDate(new Date());
		service.setCreatedBy(handler);
		service.setOwner(owner);
		service.setName(displayName);
		service.setPackageId(packageId);
		service.setRepoRoot(repoRoot);
		service.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$
		service.setState(IService.STATE_NOT_RUNNING);
		
		SVNService _master = null;
		
		IServiceManager svnSrvManger = ServiceManagerFactory.getManager(SVNService.TYPE);
		try {
			_master = svnSrvManger.add(service, svnClusterId);
		} catch (ServiceException e) {
			logger.error("Create svn service error.{clusterId = " + svnClusterId +"}.error:" + e);
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("Begin remove svn cluster. {clusterId: "+ svnClusterId+"}.");
				ServiceRemoveUtil.removeSVN(svnClusterId);
			}
			throw new RuntimeException("Create svn service error.{clusterId:" + svnClusterId +"}." + e.getMessage());
		}
		
		if (_master == null) {
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.info("Begin remove svn cluster. {clusterId: "+ svnClusterId+"}.");
				ServiceRemoveUtil.removeSVN(svnClusterId);
			}
			throw new RuntimeException("Create svn service error. return null.{clusterId:" + svnClusterId +"}.");
		}
		
		logger.info("End create svn service success: " + _master.toString());
		
		//* start svn   
		logger.info("Begin start svn cluster '" + svnClusterId + "'.");
		try {
			svnClusterManager.start(svnClusterId);
		} catch (ServiceException e1) {
			logger.error("Start svn error.{clusterId:" + svnClusterId +"}.error:" + e1);
		}
		
		// update orderItem status
		if (svnItem != null) {
			svnItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			svnItem.setFinishTime(new Date());
			orderManager.updateItem(svnItem);
		}
		
		OrderHandler.srvCreateNotifyListeners(order.getOrderId(), itemId);
		return svnClusterId;
	}

	/**
	 * create cepAnalysis
	 * @param order
	 * @return
	 */
	public static String createCEPEngine(Order order) {
		if (null == order) {
			return null;
		}
		String owner = order.getOwner();
		String handler = order.getHandler();
		
		OrderItem cepItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_CEPENGINE);
		String itemId = cepItem == null ? "" : cepItem.getItemId();

		// update item handle beginTime
		if (cepItem != null) {
			cepItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			cepItem.setHandleTime(new Date());
			orderManager.updateItem(cepItem);
		}
		
		String displayName = OrderHandler.getItemStringValue(cepItem, OrderItemAttr.ATTR_DISPLAY_NAME, ""); //$NON-NLS-1$
		String packageId = OrderHandler.getItemStringValue(cepItem, OrderItemAttr.ATTR_HOSTPKG_ID, ""); //$NON-NLS-1$
		String isBackup = OrderHandler.getItemStringValue(cepItem, OrderItemAttr.ATTR_IS_BACKUP, "N"); //$NON-NLS-1$
		int minMemory = OrderHandler.getItemIntValue(cepItem,OrderItemAttr.ATTR_CEP_MIN_MEMORY, 128); //$NON-NLS-1$
		int maxMemory = OrderHandler.getItemIntValue(cepItem,OrderItemAttr.ATTR_CEP_MAX_MEMORY, 256); //$NON-NLS-1$
		int maxPermSize = OrderHandler.getItemIntValue(cepItem,OrderItemAttr.ATTR_CEP_MAX_PERM_SIZE, 64); //$NON-NLS-1$
		String groupName = OrderHandler.getItemStringValue(cepItem,OrderItemAttr.ATTR_CEP_GROUP_NAME, "default"); //$NON-NLS-1$
		String mqServer = OrderHandler.getItemStringValue(cepItem, OrderItemAttr.ATTR_CEP_MQ_SERVER, "default"); //$NON-NLS-1$
		String isStandalone = OrderHandler.getItemStringValue(cepItem, OrderItemAttr.ATTR_IS_STANDALONE, "Y"); //$NON-NLS-1$
		
		String mqDests = OrderHandler.getItemStringValue(cepItem, OrderItemAttr.ATTR_CEP_MQ_DESTS, "default"); //$NON-NLS-1$
		String mqTypes = OrderHandler.getItemStringValue(cepItem, OrderItemAttr.ATTR_CEP_MQ_TYPES, "T");//T | Q //$NON-NLS-1$
		Map<String, String> dests = new HashMap<String, String>();
		if (StringUtil.isNotEmpty(mqDests, mqTypes)) {
			String[] names = mqDests.split(",");
			String[] types = mqTypes.split(",");
			if (names.length == types.length) {
				for (int i = 0; i < types.length; i++) {
					String name = names[i];
					String type = types[i];
					if (StringUtil.isNotEmpty(name)) {
						type = StringUtil.isNotEmpty(type) ? type : CEPEngineService.MQ_TYPE_EXCHANGE;
						dests.put(name, type);
					}
				}
			}
		} else {
			dests.put("default", CEPEngineService.MQ_TYPE_EXCHANGE); //$NON-NLS-1$
		}
		//create cep cluster
		CEPEngineCluster cluster = new CEPEngineCluster();
		cluster.setOwner(owner);
		cluster.setName(displayName);
		cluster.setMinSize(1);
		cluster.setMaxSize("Y".equalsIgnoreCase(isBackup) ? 2 : 1); //$NON-NLS-1$
		
		ICluster cepCluster = null;
		logger.info("Begin create cepEngine cluster.{name ="+displayName+"}.");
		try {
			cepCluster = cepEngineClusterManager.create(cluster);
		} catch (ClusterException e) {
			logger.error("Create cepEngine cluster error.{name ="+displayName+"}.error:" + e);
			
			throw new RuntimeException(e);
		}
		
		
		String cepClusterId = cepCluster.getId();
		logger.info("End create cepEngine cluster.{clusterId = " + cepClusterId + ",name="+ displayName+"}.");
		
		CEPEngineService cepService = new CEPEngineService();
		cepService.setCreatedDate(new Date());
		cepService.setCreatedBy(handler);
		cepService.setOwner(owner);
		cepService.setPackageId(packageId);
		cepService.setState(IService.STATE_NOT_RUNNING);
		cepService.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$
		
		cepService.setMinMemory(minMemory);
		cepService.setMaxMemory(maxMemory);
		cepService.setMaxPermSize(maxPermSize);
		cepService.setGroupName(groupName);
		cepService.setMQServer(mqServer);
		cepService.setMQDests(dests);
		
		logger.info("Begin Create cepEngine Service '" + displayName + "'.");
		IServiceManager serviceManager = ServiceManagerFactory.getManager(CEPEngineService.TYPE);

		if ("Y".equalsIgnoreCase(isBackup)) { //$NON-NLS-1$
			CEPEngineService _master = null;
			CEPEngineService master = ServiceUtil.copy(cepService);
			master.setName(displayName + "-master");
		
			try {
				_master = serviceManager.add(master, cepClusterId);
			} catch (ServiceException e) {
				logger.error("Create cepEngineService-master service error.{clusterId:"+ cepClusterId + "}.error:" + e);
			}
			if (_master == null) {
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.error("Begin remove cepEngineService cluster. {clusterId:"+ cepClusterId + "}.");
					ServiceRemoveUtil.removeCEPEngineCluster(cepClusterId);
				}
				throw new RuntimeException("Create cepEngineService service error.{clusterId:"+ cepClusterId + "}.");
			}
			
			logger.info("End create cepEngineService master-service : " + _master.toString());
		
			CEPEngineService _slaver = null;
			CEPEngineService slaver = ServiceUtil.copy(cepService);
			slaver.setName(displayName + "-slaver"); //$NON-NLS-1$
			try {
				_slaver = serviceManager.add(slaver, cepClusterId);
			} catch (ServiceException e) {
				logger.error("Create cepEngineService-slaver service error.{clusterId:"+ cepClusterId + "}.error:" + e);
			}
		
			if (_slaver == null) {
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.info("Begin remove cepEngineService cluster. {clusterId:"+ cepClusterId + "}.");
					ServiceRemoveUtil.removeCEPEngineCluster(cepClusterId);
				}
				throw new RuntimeException("Create cepEngineService service error.{clusterId:"+ cepClusterId + "}.");
			}
			logger.info("End create cepEngineService slaver-service : " + _slaver.toString());
		} else {
			CEPEngineService cepEngine = null;
			CEPEngineService cepEngineService = ServiceUtil.copy(cepService);
			cepEngineService.setName(displayName);
		
			try {
				cepEngine = serviceManager.add(cepEngineService, cepClusterId);
			} catch (ServiceException e) {
				logger.error("Create cepEngineService service error.{clusterId:"+ cepClusterId + "}.error:" + e);
			}
		
			if (cepEngine == null) {
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.info("Begin remove cepEngineService cluster. {clusterId:"+ cepClusterId + "}.");
					ServiceRemoveUtil.removeCEPEngineCluster(cepClusterId);
				}
				throw new RuntimeException("Create cepEngineService service error.{clusterId:"+ cepClusterId + "}.");
			}
			
			logger.info("End create cepEngineService master-service : " + cepEngine.toString());
		}
		
		// start CEPEngine
		if (!StringUtil.isEmpty(cepClusterId)) {
			logger.info("Begin Start cepEngine {clusterId:" + cepClusterId +"}.");
			try {
				cepEngineClusterManager.start(cepClusterId);
			} catch (ServiceException e) {
				logger.error("Start cepEngine error {clusterId:" + cepClusterId +"}.error:" + e);
			}
		}
		
		// update orderItem status
		if (cepItem != null) {
			cepItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			cepItem.setFinishTime(new Date());
			orderManager.updateItem(cepItem);
		}
		
		OrderHandler.notifyListeners(order.getOrderId(), itemId, null);
		return cepClusterId;
	}
	
	/**
	 * Marked @Deprecated by ZhongWen.Li
	 * Create Cardbin Cluster & Service
	 * 
	 * @param order
	 * @return
	 */
	@Deprecated
	public static String createCardbin(Order order) {
		if (order == null ) {
			return null;
		}
		
		String owner = order.getOwner();
		String handler = order.getHandler();
		OrderItem cardbinItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_CARDBIN);
		String itemId = cardbinItem == null ? "" : cardbinItem.getItemId();
		// update item handle beginTime
		if (cardbinItem != null) {
			cardbinItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			cardbinItem.setHandleTime(new Date());
			orderManager.updateItem(cardbinItem);
		}
		String displayName = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_DISPLAY_NAME, ""); //$NON-NLS-1$
		int maxSize = OrderHandler.getItemIntValue(cardbinItem, OrderItemAttr.ATTR_CLUSTER_MAX_SIZE, 1);//max size //$NON-NLS-1$
		String isStandalone = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_IS_STANDALONE, "Y"); //$NON-NLS-1$
		
		logger.info("Begin create cardbin cluster '" + displayName + "'.");
		CardBinCluster cluster = new CardBinCluster();
		cluster.setMinSize(1);
		cluster.setMaxSize(maxSize);
		cluster.setName(displayName);
		cluster.setOwner(owner);

		ICluster cardbinCluster = null;
		try {
			cardbinCluster = cardbinClusterManager.create(cluster);
		} catch (ClusterException e) {
			logger.error("Create cardbin cluster error.{cluster-name:"+displayName+"}.error:" + e);
		}
		String cardbinClusterId = cardbinCluster == null ? null:cardbinCluster.getId();
		if (cardbinClusterId == null) {
			throw new RuntimeException(
					"Create cardbin cluster error.{cluster-name:"
							+ displayName + "}.");
		}
		logger.info("End create cardbin Cluster.{clusterId = " + cardbinClusterId + ",name="+ displayName+"}.");
		
		String packageId = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_HOSTPKG_ID, ""); //$NON-NLS-1$

		String jdbcUrl = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_JDBC_URL, null);
		String jdbcDriver = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_JDBC_DRIVER, "com.mysql.jdbc.Driver"); //$NON-NLS-1$
		String jdbcUser = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_JDBC_USER, "root"); //$NON-NLS-1$
		String jdbcPassword = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_JDBC_PASSWORD, null);
		int jdbcMinPoolSize = OrderHandler.getItemIntValue(cardbinItem, OrderItemAttr.ATTR_JDBC_MIN_POOL_SIZE, 5);
		int jdbcMaxPoolSize = OrderHandler.getItemIntValue(cardbinItem, OrderItemAttr.ATTR_JDBC_MAX_POOL_SIZE, 100);
		
		String tempFilePath = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_SYNC_TMP_FILE_PATH, ""); //$NON-NLS-1$
		String destFilePath = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_SYNC_DEST_FILE_PATH, ""); //$NON-NLS-1$
		String isSync = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_SYNC_IS_ENABLE, "N"); //$NON-NLS-1$
		
		CardBinService cardbinInst = new CardBinService();
		cardbinInst.setName(displayName);
		cardbinInst.setPackageId(packageId); 
		cardbinInst.setOwner(owner);
		cardbinInst.setCreatedBy(handler);
		cardbinInst.setCreatedDate(new Date());
		cardbinInst.setStandalone("Y".equals(isStandalone));
		
		//synchronize cardbin data
		cardbinInst.setSyncIsEnable(isSync);
		cardbinInst.setTempFilePath(tempFilePath);
		cardbinInst.setDestFilePath(destFilePath);
		
		if (!StringUtil.isEmpty(isSync) && "Y".equalsIgnoreCase(isSync.toUpperCase())) { //$NON-NLS-1$
			//	String remoteIp = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_SYNC_REMOTE_HOST, "");
			//	String remoteUser = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_SYNC_REMOTE_USER, "");
			//	String remotePwd = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_SYNC_REMOTE_PWD, "");
			int syncDay = OrderHandler.getItemIntValue(cardbinItem, OrderItemAttr.ATTR_SYNC_DAY_OF_MONTH, -1);
			int syncHour = OrderHandler.getItemIntValue(cardbinItem, OrderItemAttr.ATTR_SYNC_HOUR_OF_DAY, -1);
			String hdfsFileUrl = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_SYNC_HDFS_FILE_URL, "");
			String remoteFilePath = OrderHandler.getItemStringValue(cardbinItem, OrderItemAttr.ATTR_SYNC_REMOTE_FILE_PATH, "");
			//	cardbinInst.setRemoteIp(remoteIp);
			//	cardbinInst.setRemoteUser(remoteUser);
			//	cardbinInst.setRemotePwd(remotePwd);
			cardbinInst.setSyncDay(syncDay);
			cardbinInst.setSyncHour(syncHour);
			cardbinInst.setHdfsFileUrl(hdfsFileUrl);
			cardbinInst.setRemoteFilePath(remoteFilePath);
		} 		
		//jdbc config
		cardbinInst.setJdbcUrl(jdbcUrl);
		cardbinInst.setJdbcDriver(jdbcDriver);
		cardbinInst.setJdbcUser(jdbcUser);
		cardbinInst.setJdbcPassword(jdbcPassword);
		cardbinInst.setJdbcMaxPoolSize(jdbcMaxPoolSize);
		cardbinInst.setJdbcMinPoolSize(jdbcMinPoolSize);
		
		//ws port
		cardbinInst.setWsPort(0);
		
		cardbinInst.setMaxPermMemorySize(SystemVariables.getJvmMaxPermSize(packageId));
		cardbinInst.setMinPermMemorySize(SystemVariables.getJvmMinPermSize(packageId));
		cardbinInst.setMaxMemorySize(SystemVariables.getJvmMaxMemorySize(packageId));
		cardbinInst.setMinMemorySize(SystemVariables.getJvmMinMemorySize(packageId));
		
		logger.info("Begin Create cardbin Service '" + displayName + "'.");
		IServiceManager serviceManager = ServiceManagerFactory.getManager(CardBinService.TYPE);
		CardBinService instance = null;
		try {
			instance = serviceManager.add(cardbinInst, cardbinClusterId);
		} catch (ServiceException e) {
			throw new RuntimeException("Create cardbin service error.{clusterId:"+ cardbinClusterId + "}." + e.getMessage());
		}
		if (instance == null) {
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.info("Begin remove cardbin cluster. {clusterId:"+ cardbinClusterId + "}.");
				ServiceRemoveUtil.removeCardBinCluster(cardbinClusterId);
			}
			throw new RuntimeException("Create cardbin service error.{clusterId:"+ cardbinClusterId + "}.");
		}
		logger.info("End create cardbin service : " + instance.toString());
		// start cardbin
		if (!StringUtil.isEmpty(cardbinClusterId)) {
			logger.info("Begin Start cardbin {clusterId:" + cardbinClusterId +"}.");
			try {
				cardbinClusterManager.start(cardbinClusterId);
			} catch (ServiceException e) {
				logger.error("Start cardbin error {clusterId:" + cardbinClusterId +"}.error:" + e);
			}		
		}
		// update orderItem status
		if (cardbinItem != null) {
			cardbinItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			cardbinItem.setFinishTime(new Date());
			orderManager.updateItem(cardbinItem);
		}
		OrderHandler.notifyListeners(order.getOrderId(), itemId, null);
		return cardbinClusterId;
	}
	
	/**
	 * Marked @Deprecated by ZhongWen.Li. <br>
	 * 
	 * Create SMS Cluster & Service
	 * 
	 * @param order
	 * @return
	 */
	@Deprecated
	public static String createSms(Order order) {
		if (order == null ) {
			return null;
		}
		String owner = order.getOwner();
		String handler = order.getHandler();
		OrderItem smsItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_SMS);
		String itemId = smsItem == null ? "" : smsItem.getItemId();

		// update item handle beginTime
		if (smsItem != null) {
			smsItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			smsItem.setHandleTime(new Date());
			orderManager.updateItem(smsItem);
		}
		String displayName = OrderHandler.getItemStringValue(smsItem, OrderItemAttr.ATTR_DISPLAY_NAME, ""); //$NON-NLS-1$
		int maxSize = OrderHandler.getItemIntValue(smsItem, OrderItemAttr.ATTR_CLUSTER_MAX_SIZE, 1); //max size //$NON-NLS-1$
		logger.info("Begin create sms cluster '" + displayName + "'.");
		SmsCluster cluster = new SmsCluster();
		cluster.setMinSize(1);
		cluster.setMaxSize(maxSize);
		cluster.setName(displayName);
		cluster.setOwner(owner);

		ICluster smsCluster = null;
		try {
			smsCluster = smsClusterManager.create(cluster);
		} catch (ClusterException e) {
			logger.error("Create sms cluster error.{cluster-name:"+displayName+"}.error:" + e);
		}
		String smsClusterId = smsCluster == null ? null:smsCluster.getId();
		if (smsClusterId == null) {
			throw new RuntimeException("Create sms cluster error.{cluster-name:"+displayName+"}.");
		}
		logger.info("End create sms Cluster.{clusterId = " + smsClusterId + ",name="+ displayName+"}.");
		
		String packageId = OrderHandler.getItemStringValue(smsItem, OrderItemAttr.ATTR_HOSTPKG_ID, ""); //$NON-NLS-1$
		String isStandalone = OrderHandler.getItemStringValue(smsItem, OrderItemAttr.ATTR_IS_STANDALONE, "Y"); //$NON-NLS-1$
		
		SmsService smsInst = new SmsService();
		smsInst.setName(displayName);
		/*
		int maxMemorySize = OrderHandler.getItemIntValue(smsItem, OrderItemAttr.ATTR_MAX_MEMORY,0);
		int minMemorySize = OrderHandler.getItemIntValue(smsItem, OrderItemAttr.ATTR_MIN_MEMORY, 0);
		int maxPermMemorySize = OrderHandler.getItemIntValue(smsItem, OrderItemAttr.ATTR_MAX_PERM_MEMORY, 0);
		int minPermMemorySize = OrderHandler.getItemIntValue(smsItem, OrderItemAttr.ATTR_MIN_PERM_MEMORY, 0);
		smsInst.setMinMemorySize(minMemorySize);
		smsInst.setMaxMemorySize(maxMemorySize);
		smsInst.setMinPermMemorySize(minPermMemorySize);
		smsInst.setMaxPermMemorySize(maxPermMemorySize);
		*/
		smsInst.setMaxPermMemorySize(SystemVariables.getJvmMaxPermSize(packageId));
		smsInst.setMinPermMemorySize(SystemVariables.getJvmMinPermSize(packageId));
		smsInst.setMaxMemorySize(SystemVariables.getJvmMaxMemorySize(packageId));
		smsInst.setMinMemorySize(SystemVariables.getJvmMinMemorySize(packageId));
		
		smsInst.setPackageId(packageId);//packageId
		smsInst.setStandalone("Y".equals(isStandalone)); //$NON-NLS-1$
		
		smsInst.setOwner(owner);
		smsInst.setCreatedBy(handler);
		smsInst.setCreatedDate(new Date());
		
		logger.info("Begin Create sms Service {clusterId=" + smsClusterId + ",displayName="+displayName+"}.");
		IServiceManager serviceManager = ServiceManagerFactory.getManager(SmsService.TYPE);
		SmsService instance = null;
		try {
			instance = serviceManager.add(smsInst, smsClusterId);
		} catch (ServiceException e) {
			logger.error("Create sms service error.{clusterId:"+ smsClusterId + "}.error message:\n" + e);
		}
		if (instance == null) {
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.info("Begin remove sms cluster. {clusterId: "+ smsClusterId+"}.");
				ServiceRemoveUtil.removeSmsCluster(smsClusterId);
			}
			throw new RuntimeException("Create sms service error.{clusterId:"+ smsClusterId + "}.");
		}
		logger.info("End create sms service : " + instance.toString());
		// start sms
		if (!StringUtil.isEmpty(smsClusterId)) {
			logger.info("Begin Start sms {clusterId:" + smsClusterId +"}.");
			try {
				smsClusterManager.start(smsClusterId);
			} catch (ServiceException e) {
				logger.error("Start sms error {clusterId:" + smsClusterId +"}. error��" + e);
			}
		}
		// update orderItem status
		if (smsItem != null) {
			smsItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			smsItem.setFinishTime(new Date());
			orderManager.updateItem(smsItem);
		}
		OrderHandler.notifyListeners(order.getOrderId(), itemId, null);
		return smsClusterId;
	}
	
	/**
	 * Create Collector Cluster & Service
	 * 
	 * @param order
	 * @return
	 */
	public static String createCollector(Order order) {
		if (order == null ) {
			return null;
		}
		String owner = order.getOwner();
		String handler = order.getHandler();
		OrderItem collectorItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_COLLECTOR);
		String itemId = collectorItem == null ? "" : collectorItem.getItemId();
		// update item handle beginTime
		if (collectorItem != null) {
			collectorItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			collectorItem.setHandleTime(new Date());
			orderManager.updateItem(collectorItem);
		}
		String displayName = OrderHandler.getItemStringValue(collectorItem, OrderItemAttr.ATTR_DISPLAY_NAME, ""); //$NON-NLS-1$
		String packageId = OrderHandler.getItemStringValue(collectorItem, OrderItemAttr.ATTR_HOSTPKG_ID, ""); //$NON-NLS-1$
		String isBackup = OrderHandler.getItemStringValue(collectorItem, OrderItemAttr.ATTR_IS_BACKUP, "N"); //$NON-NLS-1$
		int minMemory = OrderHandler.getItemIntValue(collectorItem, OrderItemAttr.ATTR_COLLECTOR_MIN_MEMORY, 128); //$NON-NLS-1$
		int maxMemory = OrderHandler.getItemIntValue(collectorItem, OrderItemAttr.ATTR_COLLECTOR_MAX_MEMORY, 256); //$NON-NLS-1$
		int maxPermSize = OrderHandler.getItemIntValue(collectorItem, OrderItemAttr.ATTR_COLLECTOR_MAX_PERM_SIZE, 64); //$NON-NLS-1$
		String groupName = OrderHandler.getItemStringValue(collectorItem, OrderItemAttr.ATTR_COLLECTOR_GROUP_NAME, "default"); //$NON-NLS-1$
		String mqServer = OrderHandler.getItemStringValue(collectorItem, OrderItemAttr.ATTR_COLLECTOR_MQ_SERVER, "default"); //$NON-NLS-1$
		String isStandalone = OrderHandler.getItemStringValue(collectorItem, OrderItemAttr.ATTR_IS_STANDALONE, "Y"); //$NON-NLS-1$
		
		String mqDests = OrderHandler.getItemStringValue(collectorItem, OrderItemAttr.ATTR_COLLECTOR_MQ_DESTS, "default"); //$NON-NLS-1$
		String mqTypes = OrderHandler.getItemStringValue(collectorItem, OrderItemAttr.ATTR_COLLECTOR_MQ_TYPES,CollectorService.MQ_TYPE_EXCHANGE);//T | Q
		Map<String, String> dests = new HashMap<String, String>();
		if (StringUtil.isNotEmpty(mqDests, mqTypes)) {
			String[] names = mqDests.split(",");
			String[] types = mqTypes.split(",");
			if (names.length == types.length) {
				for (int i = 0; i < types.length; i++) {
					String name = names[i];
					String type = types[i];
					if (StringUtil.isNotEmpty(name)) {
						type = StringUtil.isNotEmpty(type) ? type : CollectorService.MQ_TYPE_EXCHANGE;
						dests.put(name, type);
					}
				}
			}
		} else {
			dests.put("default", CollectorService.MQ_TYPE_EXCHANGE); //$NON-NLS-1$
		}
		
		String logRoot = OrderHandler.getItemStringValue(collectorItem, OrderItemAttr.ATTR_COLLECTOR_LOG_ROOT, SystemVariables.getWorkspaceHome() + "/application");
		int appenderBuffer = OrderHandler.getItemIntValue(collectorItem, OrderItemAttr.ATTR_COLLECTOR_APPENDER_BUFFER, 1) * 1024 * 1024; // MB -> B
		
		logger.info("Begin create collector cluster '" + displayName + "'.");
		CollectorCluster cluster = new CollectorCluster();
		cluster.setOwner(owner);
		cluster.setName(displayName);
		cluster.setMinSize(1);
		cluster.setMaxSize("Y".equalsIgnoreCase(isBackup) ? 2 : 1); //$NON-NLS-1$

		ICluster collectorCluster = null;
		try {
			collectorCluster = collectorClusterManager.create(cluster);
		} catch (ClusterException e) {
			logger.error("Create collector cluster error.{cluster-name:"+displayName+"}. error:"  + e);
		}
		String clusterId = collectorCluster == null ? null:collectorCluster.getId();
		if (clusterId == null) {
			throw new RuntimeException(
					"Create collector cluster error.{cluster-name:"
							+ displayName + "}.");
		}
		logger.info("End create collector Cluster.{clusterId=" + clusterId + ",name="+displayName+"}.");
		
		CollectorService collectorService = new CollectorService();
		collectorService.setCreatedDate(new Date());
		collectorService.setCreatedBy(handler);
		collectorService.setOwner(owner);
		collectorService.setPackageId(packageId);
		collectorService.setState(IService.STATE_NOT_RUNNING);
		collectorService.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$
		
		collectorService.setMinMemorySize(minMemory);
		collectorService.setMaxMemorySize(maxMemory);
		collectorService.setMaxPermMemorySize(maxPermSize);
		collectorService.setGroupName(groupName);
		collectorService.setMQServer(mqServer);
		collectorService.setMQDests(dests);
		collectorService.setLogRoot(logRoot);
		collectorService.setAppenderBuffer(appenderBuffer);
		
		logger.info("Begin Create collector Service {clusterId=" + clusterId + ",name="+displayName+"}.");
		IServiceManager serviceManager = ServiceManagerFactory.getManager(CollectorService.TYPE);
		
		if ("Y".equalsIgnoreCase(isBackup)) { //$NON-NLS-1$
			CollectorService _master = null;
			CollectorService master = ServiceUtil.copy(collectorService);
			master.setName(displayName + "-master");
			try {
				_master = serviceManager.add(master, clusterId);
			} catch (ServiceException e) {
				logger.error("Create collectorService service error.{clusterId:"+ clusterId + "}. error:" + e);
			}
			if (_master == null) {
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.error("Begin remove CollectorService cluster. {clusterId:"+ clusterId + "}.");
					ServiceRemoveUtil.removeCollectorCluster(clusterId);
				}
				throw new RuntimeException("Create CollectorService service error.{clusterId:"+ clusterId + "}.");
			}
			logger.info("End create CollectorService master-service : " + _master.toString());
		
			CollectorService _slaver = null;
			CollectorService slaver = ServiceUtil.copy(collectorService);
			slaver.setName(displayName + "-slaver");
			try {
				_slaver = serviceManager.add(slaver, clusterId);
			} catch (ServiceException e) {
				logger.error("Create CollectorService service error.{clusterId:"+ clusterId + "}. error:" + e );
			}
			if (_slaver == null) {
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.info("Begin remove CollectorService cluster. {clusterId:"+ clusterId + "}.");
					ServiceRemoveUtil.removeCollectorCluster(clusterId);
				}
				throw new RuntimeException("Create CollectorService service error.{clusterId:"+ clusterId + "}.");
			}
			logger.info("End create cepEngineService slaver-service : " + _slaver.toString());
			
		} else {
			CollectorService collectorServiceInst = null;
			CollectorService collService = ServiceUtil.copy(collectorService);
			collService.setName(displayName);
			try {
				collectorServiceInst = serviceManager.add(collService, clusterId);
			} catch (ServiceException e) {
				logger.error("Create collector service error.{clusterId:"+ clusterId + "}. error:" + e);
			}
			if (collectorServiceInst == null) {
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.info("Begin remove collector cluster. {clusterId:"+ clusterId + "}.");
					ServiceRemoveUtil.removeCollectorCluster(clusterId);
				}
				throw new RuntimeException("Create collector service error.{clusterId:"+ clusterId + "}.");
			}
			logger.info("End create collector service : " + collectorServiceInst.toString());
		}
		// start collector
		if (!StringUtil.isEmpty(clusterId)) {
			logger.info("Begin start collector. {clusterId:" + clusterId +"}.");
			try {
				collectorClusterManager.start(clusterId);
			} catch (ServiceException e) {
				logger.error("Start collector error {clusterId:" + clusterId +"}. error:" + e);
			}
		}
		// update orderItem status
		if (collectorItem != null) {
			collectorItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			collectorItem.setFinishTime(new Date());
			orderManager.updateItem(collectorItem);
		}
		OrderHandler.notifyListeners(order.getOrderId(), itemId, null);
		return clusterId;
	}
	
	/**
	 * Create Mail Cluster & Service
	 * 
	 * @param order
	 * @return
	 */
	public static String createMail(Order order) {
		if (order == null ) {
			return null;
		}
		String owner = order.getOwner();
		String handler = order.getHandler();
		OrderItem mailItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_MAIL);
		String itemId = mailItem == null ? "" : mailItem.getItemId();
		// update item handle beginTime
		if (mailItem != null) {
			mailItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			mailItem.setHandleTime(new Date());
			orderManager.updateItem(mailItem);
		}
		String displayName = OrderHandler.getItemStringValue(mailItem, OrderItemAttr.ATTR_DISPLAY_NAME, "Email Service");
		int maxSize = OrderHandler.getItemIntValue(mailItem, OrderItemAttr.ATTR_CLUSTER_MAX_SIZE, 1); 
		logger.info("Begin create mail cluster '" + displayName + "'.");
		MailCluster cluster = new MailCluster();
		cluster.setMinSize(1);
		cluster.setMaxSize(maxSize);
		cluster.setName(displayName);
		cluster.setOwner(owner);

		ICluster mailCluster = null;
		try {
			mailCluster = mailClusterManager.create(cluster);
		} catch (ClusterException e) {
			throw new RuntimeException("Create mail cluster error.{cluster-name:"+displayName+"}."  + e.getMessage());
		}
		
		String mailClusterId = mailCluster == null ? null:mailCluster.getId();
		if(mailClusterId == null) {
			throw new RuntimeException("Create mail cluster error.{cluster-name:"+displayName+"}.");
		}
		logger.info("End create mail Cluster.{clusterId = " + mailClusterId + ",name="+ displayName+"}.");
		
		String packageId = OrderHandler.getItemStringValue(mailItem, OrderItemAttr.ATTR_HOSTPKG_ID, ""); //$NON-NLS-1$
		int maxMailWorker = OrderHandler.getItemIntValue(mailItem, OrderItemAttr.ATTR_MAIL_MAX_MAILWORKER_NUM, 10);
		String jdbcUrl = OrderHandler.getItemStringValue(mailItem, OrderItemAttr.ATTR_JDBC_URL, null);
		String jdbcDriver = OrderHandler.getItemStringValue(mailItem, OrderItemAttr.ATTR_JDBC_DRIVER, "com.mysql.jdbc.Driver"); //$NON-NLS-1$
		String jdbcUser = OrderHandler.getItemStringValue(mailItem, OrderItemAttr.ATTR_JDBC_USER, "root"); //$NON-NLS-1$
		String jdbcPassword = OrderHandler.getItemStringValue(mailItem, OrderItemAttr.ATTR_JDBC_PASSWORD, null);
		int jdbcMinPoolSize = OrderHandler.getItemIntValue(mailItem, OrderItemAttr.ATTR_JDBC_MIN_POOL_SIZE, 5); //$NON-NLS-1$
		int jdbcMaxPoolSize = OrderHandler.getItemIntValue(mailItem, OrderItemAttr.ATTR_JDBC_MAX_POOL_SIZE, 100); //$NON-NLS-1$
		String isStandalone = OrderHandler.getItemStringValue(mailItem, OrderItemAttr.ATTR_IS_STANDALONE, "Y"); //$NON-NLS-1$
		
		MailService mailInst = new MailService();
		mailInst.setName(displayName);
		mailInst.setMaxMailWorkerNum(maxMailWorker); //max mail worker num
		mailInst.setPackageId(packageId);//packageId
		mailInst.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$

		//Jdbc config
		mailInst.setJdbcUrl(jdbcUrl);
		mailInst.setJdbcDriver(jdbcDriver);
		mailInst.setJdbcUser(jdbcUser);
		mailInst.setJdbcPassword(jdbcPassword);
		mailInst.setJdbcMaxPoolSize(jdbcMaxPoolSize);
		mailInst.setJdbcMinPoolSize(jdbcMinPoolSize);
		mailInst.setMaxPermMemorySize(SystemVariables.getJvmMaxPermSize(packageId));
		mailInst.setMinPermMemorySize(SystemVariables.getJvmMinPermSize(packageId));
		mailInst.setMaxMemorySize(SystemVariables.getJvmMaxMemorySize(packageId));
		mailInst.setMinMemorySize(SystemVariables.getJvmMinMemorySize(packageId));
		
		mailInst.setOwner(owner);
		mailInst.setCreatedBy(handler);
		mailInst.setCreatedDate(new Date());
		
		logger.info("Begin Create mail Service {clusterId=" + mailClusterId + ",displayName="+displayName+"}.");
		IServiceManager serviceManager = ServiceManagerFactory.getManager(MailService.TYPE);
		MailService instance = null;
		try {
			instance = serviceManager.add(mailInst, mailClusterId);
		} catch (ServiceException e) {
			logger.error("Create mail service error.{cluster :" + mailClusterId + "}.error:" + e);
		}
		if (instance == null) {
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.error("Begin remove mail cluster. {clusterId:"+ mailClusterId + "}.");
				ServiceRemoveUtil.removeMailCluster(mailClusterId);
			}
			throw new RuntimeException("Create mail service return null.{clusterId:"+ mailClusterId + "}.");
		}
		
		logger.info("End create mail service : " + instance.toString());
		// start mail
		if (!StringUtil.isEmpty(mailClusterId)) {
			logger.info("Begin Start mail {clusterId:" + mailClusterId +"}.");
			try {
				mailClusterManager.start(mailClusterId);
			} catch (ServiceException e) {
				logger.error("Start mail error {clusterId:" + mailClusterId +"}.error:" + e);
			}
		}
		// update orderItem status
		if (mailItem != null) {
			mailItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			mailItem.setFinishTime(new Date());
			orderManager.updateItem(mailItem);
		}
		OrderHandler.notifyListeners(order.getOrderId(), itemId, null);
		return mailClusterId;
	}
	
	/**
	 * 
	 * @param incItem
	 * @param appName
	 * @param strategyName
	 */
	public static void handleIncStretch(OrderItem incItem, String appName,
			String strategyName) {
		if (incItem == null || incItem.getItemStatus() == OrderItem.ITEM_STATUS_SUCCEED) {
			logger.warn("Save inc stretch strategy cancel, incItem is null or has hanled. {appName:" +  appName + ", strategyName:" + strategyName+"}.");
		}
		//increase strategy
		
		//update item handle beginTime
		incItem.setHandleTime(new Date());
		incItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
		orderManager.updateItem(incItem);
		try {
			StretchStrategyConfUtil.setIncStrategy(appName,incItem);
		} catch (StretchStrategyException e) {
			logger.error("Save application increase strategy error.{appName:"+appName + ",strategyName="+strategyName+"}. error:" + e);
			incItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED); //error
			incItem.setFinishTime(new Date());
			orderManager.updateItem(incItem);
			throw new RuntimeException(e.getMessage());
		}
		incItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED); //success
		incItem.setFinishTime(new Date());
		orderManager.updateItem(incItem);
	}

	/**
	 * 
	 * @param decItem
	 * @param appName
	 * @param strategyName
	 */
	public static void handleDecStretch(OrderItem decItem, String appName,
			String strategyName) {
		if (decItem == null || decItem.getItemStatus() == OrderItem.ITEM_STATUS_SUCCEED) {
			logger.warn("Save dec stretch strategy cancel, decItem is null or has hanled. {appName:" +  appName + ",strategyName:"+strategyName+"}.");
		}
		// decrease strategy
		// update item handle beginTime
		decItem.setHandleTime(new Date());
		decItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
		orderManager.updateItem(decItem);
		try {
			StretchStrategyConfUtil.setDecStrategy(appName, decItem);
		} catch (StretchStrategyException e) {
			logger.error("Save application decrease strategy error.{appName:"+appName + ",strategyName="+strategyName+"}. error:" + e);
			decItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED); //error
			decItem.setFinishTime(new Date());
			orderManager.updateItem(decItem);
			throw new RuntimeException(e.getMessage());
		}
		decItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED); //success
		decItem.setFinishTime(new Date());
		orderManager.updateItem(decItem);
	}

}
