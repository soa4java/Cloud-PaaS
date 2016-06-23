/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.order;

import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.OrderException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.manager.AbstractOrderProcessor;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
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
public class SingleDestroyServiceOrderProcessor extends AbstractOrderProcessor {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(SingleDestroyServiceOrderProcessor.class);
	
	/**
	 * Type of order processor. <br>
	 */
	public static final String TYPE = "SingleDestroyService";
	
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
		if (StringUtil.isNotEmpty(serviceId)) {
			if (StringUtil.isEmpty(serviceType)) {
				IService service = getServiceQuery().get(serviceId);
				serviceType = (null == service) ? null : service.getType();
			}
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
			if (StringUtil.isEmpty(serviceType)) {
				ICluster cluster = getClusterManager().get(clusterId);
				serviceType = null == cluster ? null : cluster.getType();
			}
			try {
				ClusterManagerFactory.getManager(serviceType).destroy(clusterId);
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

}
