/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.app.StretchStrategy;
import com.primeton.paas.manage.api.app.StretchStrategyException;
import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.cluster.MemcachedCluster;
import com.primeton.paas.manage.api.cluster.MySQLCluster;
import com.primeton.paas.manage.api.cluster.NginxCluster;
import com.primeton.paas.manage.api.cluster.SVNRepositoryCluster;
import com.primeton.paas.manage.api.cluster.TomcatCluster;
import com.primeton.paas.manage.api.cluster.WarCluster;
import com.primeton.paas.manage.api.exception.ConfigureException;
import com.primeton.paas.manage.api.exception.WebAppException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.OrderManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.impl.util.ServiceOpenUtil;
import com.primeton.paas.manage.api.impl.util.ServiceRemoveUtil;
import com.primeton.paas.manage.api.impl.util.StretchStrategyConfUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 订单处理. <br>
 * 
 * FIXME 需要重构 (Comment by ZhongWen.Li)
 * 
 * @author YanPing.Li (mailto:liyp@primeton.com)
 *
 */
public final class OrderHandler {

	private static ILogger logger = ManageLoggerFactory.getLogger(OrderHandler.class);
	
	private static IOrderManager orderManager = OrderManagerFactory.getManager();
	
	private static IWebAppManager appManager = WebAppManagerFactory.getManager(); 
	
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager(IClusterManager.DEFAULT_TYPE);
	
	/**
	 * 
	 * @param order
	 */
	public static void createApp(Order order) {
		if (order == null || order.getItemList() == null
				|| order.getItemList().size() < 0) {
			return;
		}
		OrderItem appItem = getItemByType(order, OrderItem.ITEM_TYPE_APP);
		String appName = getItemStringValue(appItem, OrderItemAttr.ATTR_APP_NAME, "");
		if (appItem != null
				&& appItem.getItemStatus() != OrderItem.ITEM_STATUS_SUCCEED
				&& appItem.getItemStatus() != OrderItem.ITEM_STATUS_PROCESSING) {
			try {
				ServiceOpenUtil.createInnerApplication(order);
			} catch (Exception e) {
				appItem.setFinishTime(new Date());
				appItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
				orderManager.updateItem(appItem);
				throw new RuntimeException("Save application ["+ appName +"] error.", e);
			}
		}
		
		// create session
		String sessionMemId = null;
		OrderItem sessionItem = getItemByType(order, OrderItem.ITEM_TYPE_SESSION);
		if (sessionItem != null
				&& sessionItem.getItemStatus() != OrderItem.ITEM_STATUS_SUCCEED
				&& sessionItem.getItemStatus() != OrderItem.ITEM_STATUS_PROCESSING) {
			try {
				sessionMemId = ServiceOpenUtil.createMemcached(order, OrderItem.ITEM_TYPE_SESSION, appName);
			} catch (Exception e) {
				logger.error("Create session service failed.{appName:"+ appName +"}. error:" + e);
				sessionItem.setFinishTime(new Date());
				sessionItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
				orderManager.updateItem(sessionItem); //update item handle endtime
				throw new RuntimeException("Create session service failed.{appName:"+ appName +"}."+ e.getMessage());
			}
		}
		
		// create jetty
		OrderItem jettyItem = getItemByType(order, OrderItem.ITEM_TYPE_JETTY);
		int jettyItemStatus = jettyItem == null ? OrderItem.ITEM_STATUS_INIT : jettyItem.getItemStatus();
		if (jettyItem != null
				&& jettyItemStatus != OrderItem.ITEM_STATUS_SUCCEED
				&& jettyItemStatus != OrderItem.ITEM_STATUS_PROCESSING){
			ServiceOpenUtil.JettyCreateThread jetty = new ServiceOpenUtil.JettyCreateThread(
					order, appName, sessionMemId);
			Thread t_jetty = new Thread(jetty);
			t_jetty.start(); 
		}
		
		// create tomcat
		OrderItem tomcatItem = getItemByType(order, OrderItem.ITEM_TYPE_TOMCAT);
		int tomcatItemStatus = tomcatItem == null ? OrderItem.ITEM_STATUS_INIT : tomcatItem.getItemStatus();
		if (tomcatItem != null
				&& tomcatItemStatus != OrderItem.ITEM_STATUS_SUCCEED
				&& tomcatItemStatus != OrderItem.ITEM_STATUS_PROCESSING) {
			ServiceOpenUtil.TomcatCreateThread tomcat = new ServiceOpenUtil.TomcatCreateThread(
					order, appName, sessionMemId);
			Thread t_tomcat = new Thread(tomcat);
			t_tomcat.start();
		}
		
		//create haproxy
		OrderItem haproxyItem = getItemByType(order, OrderItem.ITEM_TYPE_HAPROXY);
		if (haproxyItem != null && haproxyItem.getItemStatus() != OrderItem.ITEM_STATUS_SUCCEED 
				&& haproxyItem.getItemStatus() != OrderItem.ITEM_STATUS_PROCESSING
				&& (jettyItemStatus == OrderItem.ITEM_STATUS_SUCCEED || tomcatItemStatus == OrderItem.ITEM_STATUS_SUCCEED) ) {
			ServiceOpenUtil.HaproxyCreateThread haproxy = new ServiceOpenUtil.HaproxyCreateThread(
					order, appName);
			Thread t_haproxy = new Thread(haproxy);
			t_haproxy.start();
		}
		
		//create mysql-vm
		OrderItem mysqlItem = getItemByType(order, OrderItem.ITEM_TYPE_MYSQL);
		if(mysqlItem!=null && mysqlItem.getItemStatus() != OrderItem.ITEM_STATUS_SUCCEED
				&& mysqlItem.getItemStatus() != OrderItem.ITEM_STATUS_PROCESSING){
			ServiceOpenUtil.MysqlCreateThread mysql = new ServiceOpenUtil.MysqlCreateThread(order,appName);
			Thread t_mysql = new Thread(mysql);
			t_mysql.start();
		}
		
		//create memcached
		OrderItem memcachedItem = getItemByType(order, OrderItem.ITEM_TYPE_MEMCACHED);
		if(memcachedItem!=null && memcachedItem.getItemStatus() != OrderItem.ITEM_STATUS_SUCCEED
				&& memcachedItem.getItemStatus() != OrderItem.ITEM_STATUS_PROCESSING){
			//create memcached-cluster ; bind memcached-cluster with app
			ServiceOpenUtil.MemcachedCreateThread memcached = new ServiceOpenUtil.MemcachedCreateThread(order,appName);
			Thread t_memcached = new Thread(memcached);
			t_memcached.start();
		}
		
		//create war-cluster ; bind war-cluster with app
		OrderItem warItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_WAR);
		if (warItem != null && warItem.getItemStatus() != OrderItem.ITEM_STATUS_SUCCEED
				&& warItem.getItemStatus() != OrderItem.ITEM_STATUS_PROCESSING) {
			ServiceOpenUtil.WarCreateThread war = new ServiceOpenUtil.WarCreateThread(order,appName);
			Thread t_war = new Thread(war);
			t_war.start();
		}
		
		//create svnRepo-cluster ; bind svnRepo-cluster with app
		OrderItem repoItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_SVN_REPO);
		if (repoItem != null && repoItem.getItemStatus() != OrderItem.ITEM_STATUS_SUCCEED
				&& repoItem.getItemStatus() != OrderItem.ITEM_STATUS_PROCESSING) {
			ServiceOpenUtil.SvnRepoCreateThread svnRepo = new ServiceOpenUtil.SvnRepoCreateThread(order,appName);
			Thread t_svnRepo = new Thread(svnRepo);
			t_svnRepo.start();
		}
		
		//save strategy
		ServiceOpenUtil.AppStrategyThread strategyThread = new ServiceOpenUtil.AppStrategyThread(appName);
		Thread t_strategy = new Thread(strategyThread);
		t_strategy.setDaemon(true);
		t_strategy.start();
		
	}
	
	/**
	 * 
	 * @param orderId
	 * @param itemId
	 * @return
	 */
	public static boolean srvCreateNotifyListeners(String orderId, String itemId) {
		Order order = orderManager.getOrderWithItems(orderId);
		List<OrderItem> items = order.getItemList();
		int cout = items.size();
		int succeed = 0;
		int failed = 0;
		OrderItem c_item = orderManager.getOrderItem(itemId);
		if (c_item.getItemStatus() == OrderItem.ITEM_STATUS_FAILED) {
			order.setOrderStatus(Order.ORDER_STATUS_FAILED);
			order.setFinishTime(new Date());
			order.setNotes("Order process error!");
			orderManager.update(order);
		}
		for (OrderItem item : items) {
			if (item == null) {
				continue;
			}
			if (item.getItemStatus() == OrderItem.ITEM_STATUS_SUCCEED) {
				succeed += 1;
			} else if (item.getItemStatus() == OrderItem.ITEM_STATUS_FAILED) {
				failed += 1;
			}
		}
		
		if (succeed + failed != cout) {
			return false;
		}
		
		//update order status  && update order handle finishTime
		if (succeed == cout) {
			logger.info("all the items have been handled.");
			
			order.setOrderStatus(Order.ORDER_STATUS_SUCCEED);
			order.setFinishTime(new Date());
			order.setNotes("Order handle successed!");
			orderManager.update(order);
		} else {
			order.setOrderStatus(Order.ORDER_STATUS_FAILED);
			order.setFinishTime(new Date());
			order.setNotes("Order handle failed!");
			orderManager.update(order);
		}
		return true;
	}

	/**
	 * 
	 * @param orderId
	 * @param itemId
	 * @param appName
	 * @return
	 */
	public static boolean notifyListeners(String orderId, String itemId,
			String appName) {
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(itemId)
				|| StringUtil.isEmpty(appName)) {
			return false;
		}
 		Order order = orderManager.getOrderWithItems(orderId);
		List<OrderItem> items = order.getItemList();
		int cout = items.size();
		int succeed = 0;
		int failed = 0;
		
		OrderItem c_item = orderManager.getOrderItem(itemId);
		if (c_item.getItemStatus() == OrderItem.ITEM_STATUS_FAILED) {
			order.setOrderStatus(Order.ORDER_STATUS_FAILED);
			order.setFinishTime(new Date());
			order.setNotes("Order handle failed!");
			orderManager.update(order);
		}
		
		// jetty||tomcat --> haproxy
		if (c_item != null
				&& (OrderItem.ITEM_TYPE_JETTY.equals(c_item.getItemType()) || OrderItem.ITEM_TYPE_TOMCAT
						.equals(c_item.getItemType()))
				&& c_item.getItemStatus() == OrderItem.ITEM_STATUS_SUCCEED) {
			// create haproxy ; bind jetty||tomcat with haproxy; bind haproxy
			// with nginx
			ServiceOpenUtil.HaproxyCreateThread haproxy = new ServiceOpenUtil.HaproxyCreateThread(
					order, appName);
			Thread t_haproxy = new Thread(haproxy);
			
			OrderItem haproxyItem = getItemByType(order, OrderItem.ITEM_TYPE_HAPROXY);
			try {
				t_haproxy.start(); 
			} catch (Exception e) {
				if (haproxyItem != null) {
					haproxyItem.setFinishTime(new Date());
					haproxyItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
					orderManager.updateItem(haproxyItem); //update item handle endtime
				}
				logger.error("create haproxy cluster failed.{appName:" + appName +"}." + e.getMessage());
			}
		}
		
		// haproxy --> nginx
		if (c_item != null
				&& c_item.getItemType().equals(OrderItem.ITEM_TYPE_HAPROXY)
				&& c_item.getItemStatus() == OrderItem.ITEM_STATUS_SUCCEED) {
 			ServiceOpenUtil.NginxRestartThread nginx = new ServiceOpenUtil.NginxRestartThread(order, appName);
 			Thread t_nginx = new Thread(nginx);
 			
 			OrderItem nginxItem = getItemByType(order, OrderItem.ITEM_TYPE_NGINX);
 			if (nginxItem != null) {
 				try {
 					t_nginx.start();
 				} catch (Exception e) {
 					if (nginxItem != null) {
 						nginxItem.setFinishTime(new Date());
 						nginxItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
 						orderManager.updateItem(nginxItem); // update item handle endtime & handle status
 					}
 					logger.error("Restart nginx cluster failed,{appName = " + appName + "}." + e.getMessage());
 				}
 			}
		}
		for (OrderItem item : items) {
			if (item == null) {
				continue;
			}
			if (item.getItemStatus() == OrderItem.ITEM_STATUS_SUCCEED) {
				succeed += 1;
			} else if (item.getItemStatus() == OrderItem.ITEM_STATUS_FAILED) {
				failed += 1;
			}
		}
		if (succeed + failed != cout) {
			return false;
		}
		
		if (succeed == cout) {
			logger.info("all the items have been handled.");
			//update application's status
			if (order.getOrderType().equals(Order.ORDER_TYPE_CREATE_APP)) {
				try {
					logger.info("Init application config .");
					appManager.initAppConfig(appName);
					
					logger.info("Update App Status to 'Open'.");
					WebApp app = appManager.get(appName);
					app.setState(WebApp.STATE_OPEND);//opened
					appManager.update(app);
				} catch (WebAppException e) {
					logger.error("Update application  '"+ appName + "' status failed:" + e.getMessage());
				} catch (ConfigureException e) {
					logger.error("Initial application  '"+ appName + "' config failed:" + e.getMessage());
				}
			}

			order.setOrderStatus(Order.ORDER_STATUS_SUCCEED);
			order.setFinishTime(new Date());
			order.setNotes("Order handle successed!");
			orderManager.update(order);
		} else {
			order.setOrderStatus(Order.ORDER_STATUS_FAILED);
			order.setFinishTime(new Date());
			order.setNotes("Order handle error!");
			orderManager.update(order);
		}
		return true;
	}
	
	/**
	 * 
	 * @param appItem
	 * @param appName
	 */
	public static void removeApp(OrderItem appItem , String appName) {
		if (StringUtil.isEmpty(appName)) {
			logger.info("Remove app cancelled. appName is null");
			return;
		}
		if (appItem != null) {
			appItem.setHandleTime(new Date());
			appItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
			orderManager.updateItem(appItem);
		}	
		//remove inner application 
		doRemoveApp(appItem, appName);
	}
	
	
	/**
	 * remove innter application <br>
	 * 
	 * @param appItem
	 * @param appName
	 */
	public static void doRemoveApp(OrderItem appItem, String appName) {
		// unbind relation between haproxy-cluster and app
		ICluster haproxyCluster = clusterManager.getByApp(appName,
				HaProxyCluster.TYPE);
		String haproxyClusterId = haproxyCluster == null ? null
				: haproxyCluster.getId();

		// unbind relation between jetty-cluster||tomcat-cluster and app
		ICluster jettyCluster = clusterManager.getByApp(appName,
				JettyCluster.TYPE);
		String jettyClusterId = jettyCluster == null ? null : jettyCluster
				.getId();

		ICluster tomcatCluster = clusterManager.getByApp(appName,
				TomcatCluster.TYPE);
		String tomcatClusterId = tomcatCluster == null ? null : tomcatCluster
				.getId();

		// unbind jetty-cluster||tomcat-cluster and haproxy
		if (!StringUtil.isEmpty(haproxyClusterId)) {
			if (!StringUtil.isEmpty(tomcatClusterId)) {
				// unbind tomcat-cluster and haproxy-cluster
				ServiceRemoveUtil.unbindClusterRel(haproxyClusterId,
						tomcatClusterId);
			} else {
				// unbind relation between jetty-cluster and haproxy-cluster
				ServiceRemoveUtil.unbindClusterRel(haproxyClusterId,
						jettyClusterId);
			}
			ServiceRemoveUtil.removeHaproxyCluster(haproxyClusterId, appName);
		}
		if (!StringUtil.isEmpty(tomcatClusterId)) {
			// remove tomcat-cluster
			ServiceRemoveUtil.removeTomcatCluster(tomcatClusterId, appName);
		}
		if (!StringUtil.isEmpty(jettyClusterId)) {
			// remove jetty-cluster
			ServiceRemoveUtil.removeJettyCluster(jettyClusterId, appName);
		}
		// unbind relation between mysql-cluster and app
		ICluster mysqlCluster = clusterManager.getByApp(appName,
				MySQLCluster.TYPE);
		String mysqlClusterId = mysqlCluster == null ? null : mysqlCluster
				.getId();
		if (!StringUtil.isEmpty(mysqlClusterId)) {
			// remove cluster & services
			ServiceRemoveUtil.removeMysqlCluster(mysqlClusterId, appName);
		}
		// unbind relation between svnrepository-cluster and app
		ICluster svnRepoCluster = clusterManager.getByApp(appName,
				SVNRepositoryCluster.TYPE);
		String svnRepoId = svnRepoCluster == null ? null : svnRepoCluster
				.getId();
		if (!StringUtil.isEmpty(svnRepoId)) {
			ServiceRemoveUtil.removeSvnRepoCluster(svnRepoId, appName);
		}

		// unbind relation between memcached-cluster and app
		ICluster[] memcachedClusters = clusterManager.getByType(appName,
				MemcachedCluster.TYPE);
		for (ICluster memcachedCluster : memcachedClusters) {
			String memcacheId = memcachedCluster == null ? null
					: memcachedCluster.getId();
			if (!StringUtil.isEmpty(memcacheId)) {
				ServiceRemoveUtil.removeMemcachedCluster(memcacheId, appName);
			}
		}
		// unbind relation between war-cluster and app
		ICluster warCluster = clusterManager.getByApp(appName, WarCluster.TYPE);
		String warId = warCluster == null ? null : warCluster.getId();
		if (!StringUtil.isEmpty(warId)) {
			ServiceRemoveUtil.removeWarCluster(warId, appName);
		}
		// remove app
		ServiceRemoveUtil.removeInnerApplication(appName);
		// remove app collect log director
		ServiceRemoveUtil.removeAppResource(appName);
		if (appItem != null) {
			appItem.setFinishTime(new Date());
			appItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			orderManager.updateItem(appItem);
		}
		// restart nginx
		ServiceOpenUtil.restartNginx(OrderHandler.getNginxClusterId());
	}
	
	/**
	 * 
	 * @param order
	 */
	public static void createService(Order order) {
		if (order == null || order.getItemList() == null || order.getItemList().isEmpty()) {
			return;
		}
		List<OrderItem> itemList = order.getItemList();
		for (OrderItem item : itemList) {
			if (item == null) {
				continue;
			}
			int status = item.getItemStatus();
			if (status == OrderItem.ITEM_STATUS_SUCCEED) {
				continue;
			}
			try {
				doCreateService(order, item, null);
			} catch (Exception e) {
				item.setFinishTime(new Date());
				item.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
				orderManager.updateItem(item);
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 
	 * @param order
	 * @param item
	 * @param appName
	 */
	@SuppressWarnings("deprecation")
	public static void doCreateService(Order order, OrderItem item,
			String appName) {
		if (order == null || item == null) {
			return;
		}
		// FIXME Comment by ZhongWen.Li
		// 不要这么写
		String itemType = item.getItemType();
		if (OrderItem.ITEM_TYPE_MYSQL.equals(itemType)) {
			ServiceOpenUtil.createMySQL(order, appName);
		} else if (OrderItem.ITEM_TYPE_JETTY.equals(itemType)) {
			ServiceOpenUtil.createJetty(order, appName);
		} else if(OrderItem.ITEM_TYPE_TOMCAT.equals(itemType)){
			ServiceOpenUtil.createTomcat(order, appName);
		} else if (OrderItem.ITEM_TYPE_MEMCACHED.equals(itemType)) {
			ServiceOpenUtil.createMemcached(order, OrderItem.ITEM_TYPE_MEMCACHED, appName);
		} else if (OrderItem.ITEM_TYPE_HAPROXY.equals(itemType)) {
			ServiceOpenUtil.createInnerHaproxy(order, appName);
		} else if (OrderItem.ITEM_TYPE_SVN_REPO.equals(itemType)) {
			ServiceOpenUtil.createSVNRepository(order, appName);
		} else if (OrderItem.ITEM_TYPE_OPENAPI.equals(itemType)) {
			ServiceOpenUtil.createOpenAPI(order);
		} else if (OrderItem.ITEM_TYPE_NGINX.equals(itemType)) {
			if (Order.ORDER_TYPE_CREATE_SRV.equals(order.getOrderType()) && order.getItemList().size() == 1) {
				ServiceOpenUtil.createNginx(order);
			} else {
				ServiceOpenUtil.NginxRestartThread nginx = new ServiceOpenUtil.NginxRestartThread(order,appName);
				Thread t_nginx = new Thread(nginx);
				OrderItem nginxItem = getItemByType(order, OrderItem.ITEM_TYPE_NGINX);
				if (nginxItem != null && nginxItem.getItemStatus() != OrderItem.ITEM_STATUS_SUCCEED) {
					try {
						t_nginx.start();
					} catch (Exception e) {
						if (nginxItem != null) {
							nginxItem.setFinishTime(new Date());
							nginxItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
							orderManager.updateItem(nginxItem);//update item handle endtime & handle status
						}
						logger.error("Restart nginx cluster failed,{appName = " + appName + "}." + e.getMessage());
					}
				}
			} 
		} else if (OrderItem.ITEM_TYPE_SVN.equals(itemType)) {
			ServiceOpenUtil.createSVN(order);
		} else if(OrderItem.ITEM_TYPE_CEPENGINE.equals(itemType)){
			ServiceOpenUtil.createCEPEngine(order);
		} else if (OrderItem.ITEM_TYPE_SMS.equals(itemType)) {
			ServiceOpenUtil.SmsCreateThread sms = new ServiceOpenUtil.SmsCreateThread(order);
			Thread t_sms = new Thread(sms);
			t_sms.start(); 
		} else if (OrderItem.ITEM_TYPE_COLLECTOR.equals(itemType)) {
			ServiceOpenUtil.createCollector(order);
		} else if (OrderItem.ITEM_TYPE_CARDBIN.equals(itemType)) {
			ServiceOpenUtil.createCardbin(order);
		} else if (OrderItem.ITEM_TYPE_MAIL.equals(itemType)) {
			ServiceOpenUtil.createMail(order);
		} else if(OrderItem.ITEM_TYPE_WAR.equals(itemType)) {
			ServiceOpenUtil.createWar(order, appName);
		} else if (OrderItem.ITEM_TYPE_SESSION.equals(itemType)) {
			ServiceOpenUtil.createMemcached(order, OrderItem.ITEM_TYPE_SESSION, appName);
		} else {
			throw new RuntimeException("Can not process item type [" + item.getItemType() + "].");
		}
	}

	/**
	 * 
	 * @param order
	 */
	public static void removeService(Order order) {
		if (order == null || order.getItemList() == null
				|| order.getItemList().isEmpty()) {
			throw new RuntimeException(
					"Service Remove cancelled: can not find orderItem list.");
		}
		List<OrderItem> itemList = order.getItemList();
		for (OrderItem item : itemList) {
			if (item == null) {
				continue;
			}
			try {
				doRemoveService(item);
			} catch (Exception e) {
				item.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
				item.setFinishTime(new Date());
				orderManager.updateItem(item);
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * @param item
	 */
	@SuppressWarnings("deprecation")
	public static void doRemoveService(OrderItem item) {
		if (item == null) {
			return;
		}
		String itemType = item.getItemType();
		String clusterId = getItemStringValue(item, OrderItemAttr.ATTR_CLUSTER_ID, null);
		if (StringUtil.isEmpty(clusterId)) {
			throw new RuntimeException("Service Remove cancelled : can not find cluster.");
		}
		// update item handle beginTime
		item.setHandleTime(new Date());
		item.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
		orderManager.updateItem(item);
		
		// FIXME Comment by ZhongWen.Li
		// 不要这么写,以后需要重构这块代码
		if (OrderItem.ITEM_TYPE_MYSQL.equals(itemType)) {
			ServiceRemoveUtil.removeMysqlCluster(clusterId, null);
		} else if (OrderItem.ITEM_TYPE_JETTY.equals(itemType)) {
			ServiceRemoveUtil.removeJettyCluster(clusterId, null);
		} else if(OrderItem.ITEM_TYPE_TOMCAT.equals(itemType)){
			ServiceRemoveUtil.removeTomcatCluster(clusterId, null);
		} else if (OrderItem.ITEM_TYPE_MEMCACHED.equals(itemType)) {
			ServiceRemoveUtil.removeMemcachedCluster(clusterId, null);
		} else if (OrderItem.ITEM_TYPE_HAPROXY.equals(itemType)) {
			ServiceRemoveUtil.removeHaproxyCluster(clusterId, null);
		} else if (OrderItem.ITEM_TYPE_OPENAPI.equals(itemType)) {
			ServiceRemoveUtil.removeOpenAPICluster(clusterId);
		} else if (OrderItem.ITEM_TYPE_NGINX.equals(itemType)) {
			ServiceRemoveUtil.removeNginx(clusterId);
		} else if (OrderItem.ITEM_TYPE_SVN.equals(itemType)) {
			ServiceRemoveUtil.removeSVN(clusterId);
		} else if (OrderItem.ITEM_TYPE_SVN_REPO.equals(itemType)) {
			ServiceRemoveUtil.removeSvnRepoCluster(clusterId, null);
		}  else if (OrderItem.ITEM_TYPE_KEEPALIVED.equals(itemType)) {
			ServiceRemoveUtil.removeKeepalivedCluster(clusterId);
		} else if(OrderItem.ITEM_TYPE_CEPENGINE.equals(itemType)){
			ServiceRemoveUtil.removeCEPEngineCluster(clusterId);
		} else if (OrderItem.ITEM_TYPE_SMS.equals(itemType)){
			ServiceRemoveUtil.removeSmsCluster(clusterId);
		} else if (OrderItem.ITEM_TYPE_COLLECTOR.equals(itemType)){
			ServiceRemoveUtil.removeCollectorCluster(clusterId);
		} else if (OrderItem.ITEM_TYPE_CARDBIN.equals(itemType)) {
			ServiceRemoveUtil.removeCardBinCluster(clusterId);
		} else if (OrderItem.ITEM_TYPE_MAIL.equals(itemType)) {
			ServiceRemoveUtil.removeMailCluster(clusterId);
		} else if (OrderItem.ITEM_TYPE_SESSION.equals(itemType)) {
			ServiceRemoveUtil.removeMemcachedCluster(clusterId, null);
		} else if (OrderItem.ITEM_TYPE_REDIS.equals(itemType)) {
			ServiceRemoveUtil.removeRedisCluster(clusterId);
		} else if (OrderItem.ITEM_TYPE_JOBCTRL.equals(itemType)) {
			ServiceRemoveUtil.removeJobCtrlCluster(clusterId);
		} else if (OrderItem.ITEM_TYPE_ESB.equals(itemType)) {
			ServiceRemoveUtil.removeEsbCluster(clusterId);
		} else if (OrderItem.ITEM_TYPE_MSGQUEUE.equals(itemType)) {
			ServiceRemoveUtil.removeMsgQueueCluster(clusterId);
		} else {
			logger.warn("Nothing to do for process order item {0}.", new Object[] { itemType });
		} 	
		item.setFinishTime(new Date());
		item.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED); 
		orderManager.updateItem(item);
	}

	/**
	 * 
	 * @param order
	 */
	public static void stretchStrategy(Order order) {
		if (order == null || order.getItemList()==null || order.getItemList().size()<0) {
			return;
		}
		OrderItem stretchTypeItem = getItemByType(order, OrderItem.ITEM_TYPE_STRETCH_STRATEGY_NAME);
		String appName = getItemStringValue(stretchTypeItem, OrderItemAttr.ATTR_APP_NAME, "");
		String strategyName = getItemStringValue(stretchTypeItem, OrderItemAttr.ATTR_STRETCH_STRATEGY_NAME,"");
		
		if (StringUtil.isNotEmpty(strategyName) && strategyName.equals(StretchStrategy.GLOBAL_STRATEGY)) {
			stretchTypeItem.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING); 
			stretchTypeItem.setHandleTime(new Date());
			orderManager.updateItem(stretchTypeItem);
			try {
				StretchStrategyConfUtil.setGlobalStrategy(appName,stretchTypeItem);
			} catch (StretchStrategyException e) {
				logger.info("Save application to global strategy failed.{appName:"+appName + ",strategyName="+strategyName+"}. error msg:" + e.getMessage());
				stretchTypeItem.setItemStatus(OrderItem.ITEM_STATUS_FAILED); 
				stretchTypeItem.setFinishTime(new Date());
				orderManager.updateItem(stretchTypeItem);
				throw new RuntimeException(e.getMessage());
			}
			stretchTypeItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED); 
			stretchTypeItem.setFinishTime(new Date());
			orderManager.updateItem(stretchTypeItem);
			return ;
		} 
		
		List<OrderItem> itemList = order.getItemList();
		for (OrderItem item : itemList) {
			String itemType = item.getItemType();
			int itemStatus = item.getItemStatus();
			if (itemStatus == OrderItem.ITEM_STATUS_SUCCEED ) {
				continue;
			}
			if (OrderItem.ITEM_TYPE_STRETCH_STRATEGY_NAME.equals(itemType)) {
				stretchTypeItem.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED); 
				stretchTypeItem.setHandleTime(new Date());
				stretchTypeItem.setFinishTime(new Date());
				orderManager.updateItem(stretchTypeItem);
			} else if (OrderItem.ITEM_TYPE_STRETCH_INC_STRATEGY.equals(itemType)) {
				ServiceOpenUtil.handleIncStretch(item, appName, strategyName);
			} else if (OrderItem.ITEM_TYPE_STRETCH_DEC_STRATEGY.equals(itemType)) {
				ServiceOpenUtil.handleDecStretch(item, appName, strategyName);
			}
		}
	}
	
	/**
	 * 
	 * @param order
	 * @param itemType
	 * @return
	 */
	public static OrderItem getItemByType(Order order,String itemType) {
		if (order == null || itemType == null || itemType.trim().length() < 1
				|| order.getItemList() == null || order.getItemList().isEmpty()) {
			return null;
		}
		List<OrderItem> itemList = order.getItemList();
		for (OrderItem item : itemList) {
			String curType = item.getItemType();
			if (itemType.equals(curType)){
				return item;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param order
	 * @param itemAttrName
	 * @param defaultValue
	 * @return
	 */
	public static String getItemStringValue(OrderItem orderitem,
			String itemAttrName, String defaultValue) {
		if (orderitem == null || itemAttrName == null
				|| itemAttrName.trim().length() < 1) {
			return defaultValue;
		}
		List<OrderItemAttr> attrList = orderitem.getAttrList();
		if (attrList == null || attrList.isEmpty()) {
			return defaultValue;
		}
		for (OrderItemAttr attr : attrList) {
			String attrName = attr.getAttrName();
			String attrValue = attr.getAttrValue();
			if (itemAttrName.equals(attrName)) {
				return attrValue;
			}
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param order
	 * @param itemAttrName
	 * @param defaultValue
	 * @return
	 */
	public static int getItemIntValue(OrderItem orderitem, String itemAttrName,
			int defaultValue) {
		if (orderitem == null || itemAttrName == null
				|| itemAttrName.trim().length() < 1) {
			return defaultValue;
		}
		List<OrderItemAttr> attrList = orderitem.getAttrList();
		if (attrList == null || attrList.isEmpty()) {
			return defaultValue;
		}
		try {
			for (OrderItemAttr attr : attrList) {
				String attrName = attr.getAttrName();
				String attrValue = attr.getAttrValue();
				if (itemAttrName.equals(attrName)) {
					return Integer.parseInt(attrValue);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @return nginxClusterId
	 */
	public static String getNginxClusterId() {
		ICluster[] clusters = clusterManager.getByType(NginxCluster.TYPE);
		if (clusters != null && clusters.length > 0 && clusters[0] != null) {
			String defaultName = SystemVariables.getDefaultNginxName();
			if (StringUtil.isEmpty(defaultName)) {
				defaultName = NginxCluster.DEFAULT_CLUSTER_NAME;
			}
			for (ICluster clt : clusters) {
				String nginxClusterId = clt.getId();
				String name = clt.getName();
				if (defaultName.equals(name)) {
					return nginxClusterId;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param order
	 */
	public static void modifyService(Order order) {
		if (order == null || order.getItemList() == null || order.getItemList().isEmpty()) {
			return;
		}
		List<OrderItem> itemList = order.getItemList();
		for (OrderItem item : itemList) {
			if (item == null) {
				continue;
			}
			int status = item.getItemStatus();
			if (status == OrderItem.ITEM_STATUS_SUCCEED) {
				continue;
			}
			String instId = OrderHandler.getItemStringValue(item, OrderItemAttr.ATTR_SERVICE_ID, null);
			if (StringUtil.isEmpty(instId)) {
				continue;
			}
			String clusterId =  OrderHandler.getItemStringValue(item, OrderItemAttr.ATTR_CLUSTER_ID, null);
			if (StringUtil.isEmpty(clusterId)) {
				continue;
			}
			try {
				doModifyService(order, item);
			} catch (Exception e) {
				item.setFinishTime(new Date());
				item.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
				orderManager.updateItem(item);
				throw new RuntimeException(e);
			}
		}
	}
	
	
	/**
	 * @param order
	 * @param item
	 */
	public static void doModifyService(Order order, OrderItem item) {
		if (order == null || item == null) {
			return;
		}
		//	String itemType = item.getItemType();
		//	TODO
	}
	
}