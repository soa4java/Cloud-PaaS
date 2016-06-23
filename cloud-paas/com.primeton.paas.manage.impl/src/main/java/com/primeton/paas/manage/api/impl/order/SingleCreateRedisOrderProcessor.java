/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.order;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.cluster.RedisCluster;
import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.exception.OrderException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.manager.AbstractOrderProcessor;
import com.primeton.paas.manage.api.impl.util.ServiceRemoveUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IRedisServiceManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.service.RedisService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 订单处理器 : 创建简单的Redis服务集群及其服务实例. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SingleCreateRedisOrderProcessor extends AbstractOrderProcessor {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(SingleCreateRedisOrderProcessor.class);
	
	/**
	 * Type of Order. <br>
	 */
	public static final String TYPE = "SingleCreateRedis";
	
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager(RedisService.TYPE);
	private static IRedisServiceManager serviceManager = ServiceManagerFactory.getManager(RedisService.TYPE);

	/**
	 * Default. <br>
	 */
	public SingleCreateRedisOrderProcessor() {
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
		OrderItem item = getItemByType(order, RedisService.TYPE);
		if (null == item) {
			List<OrderItem> items = order.getItemList();
			if (null == items || items.isEmpty()) {
				order = getOrderManager().getOrderWithItems(order.getOrderId());
				items = order.getItemList();
			}
			item = (null == items || items.isEmpty()) ? null : items.get(0);
		}
		if (null == item) {
			logger.warn("Order [" + order.getOrderType() + ", " + order.getOrderId() + "] none redis item.");
			performOk(order);
			return;
		}
		item.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
		item.setHandleTime(new Date());
		getOrderManager().updateItem(item);
		
		String displayName = getItemValue(item, OrderItemAttr.ATTR_DISPLAY_NAME, "redis service"); //$NON-NLS-1$
		String packageId = getItemValue(item, OrderItemAttr.ATTR_HOSTPKG_ID); //$NON-NLS-1$
		int clusterSize = getItemValue(item, OrderItemAttr.ATTR_CLUSTER_SIZE, 1); //$NON-NLS-1$
		clusterSize = clusterSize < 1 ? 1 : clusterSize;
		String isStandalone = getItemValue(item, OrderItemAttr.ATTR_IS_STANDALONE, "N"); //$NON-NLS-1$
		String aliasName = getItemValue(item, OrderItemAttr.ATTR_REDIS_ALIAS_NAME, UUID.randomUUID().toString()); //$NON-NLS-1$
		
		if (StringUtil.isEmpty(packageId)) {
			throw new OrderException("Create redis error, packageId is empty. Please check you order information.");
		}
		
		RedisCluster cluster = new RedisCluster();
		cluster.setOwner(order.getOwner());
		cluster.setName(displayName);
		cluster.setMinSize(clusterSize);
		cluster.setMaxSize(clusterSize);
		cluster.setAliasName(aliasName);
		
		ICluster redisCluster = null;
		logger.info("Begin create redis cluster [" + displayName + "].");
		try {
			redisCluster = getClusterManager().create(cluster);
		} catch (ClusterException e) {
			logger.error("Create redis cluster [" + displayName + "] error", e);
			throw new OrderException(e);
		}
		
		String redisClusterId = redisCluster.getId();
		logger.info("End create redis cluster [" + redisClusterId + "].");
		
		RedisService service = new RedisService();
		service.setCreatedDate(new Date());
		service.setCreatedBy(order.getHandler());
		service.setOwner(order.getOwner());
		service.setName(displayName);
		service.setPackageId(packageId);
		service.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$
		service.setState(IService.STATE_NOT_RUNNING);
		
		List<RedisService> services = null;
		try {
			services = getServiceManager().add(service, redisClusterId, clusterSize);
		} catch (ServiceException e) {
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.info("Begin clean redis cluster [" + redisClusterId + "].");
				ServiceRemoveUtil.removeRedisCluster(redisClusterId);
			}
			throw new OrderException("Create redis service for cluster [" + redisClusterId +"] error.", e);
		}
		
		if (null == services || services.isEmpty()) {
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.info("Begin remove redis cluster [" + redisClusterId + "].");
				ServiceRemoveUtil.removeRedisCluster(redisClusterId);
			}
			throw new OrderException("Create redis service error [" + redisClusterId + "].");
		}
		
		logger.info("Begin start redis cluster [" + redisClusterId + "].");
		try {
			getClusterManager().start(redisClusterId);
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
	 * @return IClusterManager of Redis
	 */
	protected static IClusterManager getClusterManager() {
		return clusterManager = (null == clusterManager) 
				? ClusterManagerFactory.getManager(RedisService.TYPE) 
				: clusterManager;
	}

	/**
	 * 
	 * @return
	 */
	protected static IRedisServiceManager getServiceManager() {
		return serviceManager = (null == serviceManager) 
				? (IRedisServiceManager)ServiceManagerFactory.getManager(RedisService.TYPE) 
				: serviceManager;
	}

}
