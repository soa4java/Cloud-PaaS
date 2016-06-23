/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.order;

import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.exception.OrderException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.impl.manager.AbstractOrderProcessor;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 订单处理器 : 销毁服务集群以及服务实例. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultDestroyServiceOrderProcessor extends AbstractOrderProcessor {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultDestroyServiceOrderProcessor.class);
	
	/**
	 * Type of order processor. <br>
	 */
	public static final String TYPE = "DefaultDestroyService";
	
	private static IWebAppManager webAppManager = WebAppManagerFactory.getManager();
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IOrderProcessor#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IOrderProcessor#process(com.primeton.paas.manage.api.model.Order)
	 */
	public void process(Order order) throws OrderException {
		if (null == order) {
			return;
		}
		List<OrderItem> items = order.getItemList();
		if (null == items || items.isEmpty()) {
			order = getOrderManager().getOrderWithItems(order.getOrderId());
			items = order.getItemList();
		}
		if (null == items || items.isEmpty()) {
			logger.warn("Order [" + order.getOrderType() + ", " + order.getOrderId() + "] none item.");
		}
		OrderItem item = items.get(0);
		if (null == item) {
			return;
		}
		item.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
		item.setHandleTime(new Date());
		getOrderManager().updateItem(item);
		
		String serviceId = getItemValue(item, OrderItemAttr.ATTR_SERVICE_ID);
		String serviceType = getItemValue(item, OrderItemAttr.ATTR_SERVICE_TYPE);
		if (StringUtil.isEmpty(serviceType)) {
			throw new OrderException("OrderItem [" + item.getItemId() + "] service type property ${type} is empty.");
		}
		if (StringUtil.isNotEmpty(serviceId)) {
			try {
				ServiceManagerFactory.getManager(serviceType).destroy(serviceId);
				item.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			} catch (Throwable t) {
				logger.error("Process OrderItem [" + item.getItemId() + "] error.", t);
				item.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
			}
			item.setFinishTime(new Date());
			getOrderManager().updateItem(item);
			performOk(order);
			return;
		}
		
		String clusterId = getItemValue(item, OrderItemAttr.ATTR_CLUSTER_ID);
		if (StringUtil.isNotEmpty(clusterId)) {
			IClusterManager clusterManager = ClusterManagerFactory.getManager(serviceType);
			try {
				String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
				if (relClusterIds != null && relClusterIds.length > 0) {
					for (String id : relClusterIds) {
						clusterManager.unbindCluster(clusterId, id);
					}
				}
				// remove relations with application
				String[] apps = getWebAppManager().getRelationApp(clusterId);
				if (apps != null && apps.length > 0) {
					for (String name : apps) {
						getWebAppManager().unbind(name, clusterId);
					}
				}
				try {
					logger.info("Begin destroy " + serviceType + " cluster [" + clusterId + "].");
					clusterManager.destroy(clusterId);
					logger.info("End destroy " + serviceType + " cluster [" + clusterId + "].");
				} catch (ClusterException e) {
					logger.error("Destroy " + serviceType + " cluster [" + clusterId + "] error.\n" + e);
				}
				clusterManager.destroy(clusterId);
				item.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			} catch (Throwable t) {
				logger.error("Process OrderItem [" + item.getItemId() + "] error.", t);
				item.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
			}
			item.setFinishTime(new Date());
			getOrderManager().updateItem(item);
			performOk(order);
		} else {
			// Nothing to do
			item.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			item.setFinishTime(new Date());
			getOrderManager().updateItem(item);
			performOk(order);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IOrderProcessor#process(com.primeton.paas.manage.api.model.Order, java.lang.String)
	 */
	public void process(Order order, String itemId) throws OrderException {
		// Only one item
		process(order);
	}

	/**
	 * 
	 * @return
	 */
	protected static IWebAppManager getWebAppManager() {
		return webAppManager = (null == webAppManager)
				? WebAppManagerFactory.getManager()
				: webAppManager;
	}

}
