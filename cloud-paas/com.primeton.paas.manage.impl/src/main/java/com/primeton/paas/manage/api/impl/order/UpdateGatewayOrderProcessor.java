/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.order;

import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.OrderException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.manager.AbstractOrderProcessor;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IGatewayServiceManager;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.service.GatewayService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class UpdateGatewayOrderProcessor extends AbstractOrderProcessor {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(UpdateGatewayOrderProcessor.class);
	
	/**
	 * Type of Order. <br>
	 */
	public static final String TYPE = "UpdateGateway";
	
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager(GatewayService.TYPE);
	private static IGatewayServiceManager serviceManager = ServiceManagerFactory.getManager(GatewayService.TYPE);

	/**
	 * Default. <br>
	 */
	public UpdateGatewayOrderProcessor() {
		super();
	}

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
		OrderItem item = getItemByType(order, GatewayService.TYPE);
		if (null == item) {
			List<OrderItem> items = order.getItemList();
			if (null == items || items.isEmpty()) {
				order = getOrderManager().getOrderWithItems(order.getOrderId());
				items = order.getItemList();
			}
			item = (null == items || items.isEmpty()) ? null : items.get(0);
		}
		if (null == item) {
			logger.warn("Order [" + order.getOrderType() + ", " + order.getOrderId() + "] none Gateway item.");
			performOk(order);
			return;
		}
		item.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
		item.setHandleTime(new Date());
		getOrderManager().updateItem(item);
		
		String displayName = getItemValue(item, OrderItemAttr.ATTR_DISPLAY_NAME, "Gateway service"); //$NON-NLS-1$
		int maxConnection = getItemValue(item, OrderItemAttr.ATTR_GATEWAY_MAX_CONNECTION, 300000);
		String preServers = getItemValue(item, OrderItemAttr.ATTR_GATEWAY_PRE_SERVERS, "localhost:80"); //$NON-NLS-1$
		String clusterId = getItemValue(item, OrderItemAttr.ATTR_CLUSTER_ID, null);
		if (StringUtil.isEmpty(clusterId)) {
			throw new OrderException("Update Gateway error, ${clusterId} is empty. Please check you order information.");
		}
		List<GatewayService> services = getServiceQuery().getByCluster(clusterId, GatewayService.class);
		if (null == services || services.isEmpty()) {
			item.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
			item.setFinishTime(new Date());
			getOrderManager().updateItem(item);
			// 触发检查订单状态
			performOk(order);
		}
		// 更新服务配置并重启
		for (GatewayService service : services) {
			service.setName(displayName);
			service.setMaxConnection(maxConnection);
			service.setPreServers(preServers);
			try {
				getServiceManager().update(service, clusterId);
				getServiceManager().restart(service.getId());
			} catch (ServiceException e) {
				logger.error("Update service {0} error.", new Object[] { service }, e);
			}
		}
		// 更新订单项状态
		item.setItemStatus(OrderItem.ITEM_STATUS_SUCCEED);
		item.setFinishTime(new Date());
		getOrderManager().updateItem(item);
		
		// 触发检查订单状态
		performOk(order);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IOrderProcessor#process(com.primeton.paas.manage.api.model.Order, java.lang.String)
	 */
	public void process(Order order, String itemId) throws OrderException {
		if (null == order || StringUtil.isEmpty(itemId)) {
			return;
		}
		// only one item
		process(order);
	}

	/**
	 * 
	 * @return IClusterManager of Gateway
	 */
	protected static IClusterManager getClusterManager() {
		return clusterManager = (null == clusterManager) 
				? ClusterManagerFactory.getManager(GatewayService.TYPE) 
				: clusterManager;
	}

	/**
	 * 
	 * @return
	 */
	protected static IGatewayServiceManager getServiceManager() {
		return serviceManager = (null == serviceManager) 
				? (IGatewayServiceManager)ServiceManagerFactory.getManager(GatewayService.TYPE) 
				: serviceManager;
	}

}
