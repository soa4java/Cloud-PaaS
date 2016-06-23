/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.order;

import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.cluster.GatewayCluster;
import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.exception.OrderException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.manager.AbstractOrderProcessor;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IGatewayServiceManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.service.GatewayService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 订单处理器 : 创建简单的Gateway服务集群及其服务实例. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CreateGatewayOrderProcessor extends AbstractOrderProcessor {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(CreateGatewayOrderProcessor.class);
	
	/**
	 * Type of Order. <br>
	 */
	public static final String TYPE = "CreateGateway";
	
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager(GatewayService.TYPE);
	private static IGatewayServiceManager serviceManager = ServiceManagerFactory.getManager(GatewayService.TYPE);

	/**
	 * Default. <br>
	 */
	public CreateGatewayOrderProcessor() {
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
		String packageId = getItemValue(item, OrderItemAttr.ATTR_HOSTPKG_ID); //$NON-NLS-1$
		int clusterSize = getItemValue(item, OrderItemAttr.ATTR_CLUSTER_SIZE, 1); //$NON-NLS-1$
		clusterSize = clusterSize < 1 ? 1 : clusterSize;
		String isStandalone = getItemValue(item, OrderItemAttr.ATTR_IS_STANDALONE, "N"); //$NON-NLS-1$
		int maxConnection = getItemValue(item, OrderItemAttr.ATTR_GATEWAY_MAX_CONNECTION, 300000);
		String preServers = getItemValue(item, OrderItemAttr.ATTR_GATEWAY_PRE_SERVERS, "localhost:80"); //$NON-NLS-1$
		
		if (StringUtil.isEmpty(packageId)) {
			throw new OrderException("Create Gateway error, ${packageId} is empty. Please check you order information.");
		}
		
		GatewayCluster cluster = new GatewayCluster();
		cluster.setOwner(order.getOwner());
		cluster.setName(displayName);
		cluster.setMinSize(clusterSize);
		cluster.setMaxSize(clusterSize);
		
		ICluster gatewayCluster = null;
		logger.info("Begin create Gateway cluster [" + displayName + "].");
		try {
			gatewayCluster = getClusterManager().create(cluster);
		} catch (ClusterException e) {
			logger.error("Create Gateway cluster [" + displayName + "] error", e);
			throw new OrderException(e);
		}
		
		String clusterId = gatewayCluster.getId();
		logger.info("End create Gateway cluster [" + clusterId + "].");
		
		GatewayService service = new GatewayService();
		service.setCreatedDate(new Date());
		service.setCreatedBy(order.getHandler());
		service.setOwner(order.getOwner());
		service.setName(displayName);
		service.setPackageId(packageId);
		service.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$
		service.setMaxConnection(maxConnection);
		service.setPreServers(preServers);
		
		List<GatewayService> services = null;
		try {
			services = getServiceManager().add(service, clusterId, clusterSize);
		} catch (ServiceException e) {
			logger.error("Create Gateway service {0} in cluster {1} error.", new Object[] { service, clusterId }, e);
		}
		
		if (null == services || services.isEmpty()) {
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.info("Begin remove Gateway cluster [" + clusterId + "].");
				try {
					getClusterManager().destroy(clusterId);
				} catch (Throwable t) {
					logger.error(t);
				}
			}
			throw new OrderException("Create Gateway service error [" + clusterId + "].");
		}
		
		logger.info("Begin start Gateway cluster [" + clusterId + "].");
		try {
			getClusterManager().start(clusterId);
		} catch (ServiceException e) {
			logger.error(e);
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
