/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.manage.api.impl.order;

import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.cluster.MsgQueueCluster;
import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.exception.OrderException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.manager.AbstractOrderProcessor;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IMsgQueueServiceManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.service.MsgQueueService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SingleCreateMsgQueueOrderProcessor extends AbstractOrderProcessor {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(SingleCreateMsgQueueOrderProcessor.class);
	
	/**
	 * Type of Order. <br>
	 */
	public static final String TYPE = "SingleCreateMsgQueue";
	
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager(MsgQueueService.TYPE);
	private static IMsgQueueServiceManager serviceManager = ServiceManagerFactory.getManager(MsgQueueService.TYPE);

	/**
	 * Default. <br>
	 */
	public SingleCreateMsgQueueOrderProcessor() {
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
		OrderItem item = getItemByType(order, MsgQueueService.TYPE);
		if (null == item) {
			List<OrderItem> items = order.getItemList();
			if (null == items || items.isEmpty()) {
				order = getOrderManager().getOrderWithItems(order.getOrderId());
				items = order.getItemList();
			}
			item = (null == items || items.isEmpty()) ? null : items.get(0);
		}
		if (null == item) {
			logger.warn("Order [" + order.getOrderType() + ", " + order.getOrderId() + "] none MsgQueue item.");
			performOk(order);
			return;
		}
		item.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
		item.setHandleTime(new Date());
		getOrderManager().updateItem(item);

		// 获取表单数据
		String displayName = getItemValue(item, OrderItemAttr.ATTR_DISPLAY_NAME, "MsgQueue service"); //$NON-NLS-1$
		String packageId = getItemValue(item, OrderItemAttr.ATTR_HOSTPKG_ID); //$NON-NLS-1$
		String isStandalone = getItemValue(item, OrderItemAttr.ATTR_IS_STANDALONE, "N"); //$NON-NLS-1$
		String user = getItemValue(item, OrderItemAttr.ATTR_MSG_QUEUE_USER, "paas"); //$NON-NLS-1$
		String password = getItemValue(item, OrderItemAttr.ATTR_MSG_QUEUE_PASSWORD, "paas"); //$NON-NLS-1$
		final String vhost = "/";
		
		if (StringUtil.isEmpty(packageId)) {
			throw new OrderException("Create MsgQueue error, packageId is empty. Please check you order information.");
		}
		
		// 创建集群
		MsgQueueCluster cluster = new MsgQueueCluster();
		cluster.setOwner(order.getOwner());
		cluster.setName(displayName);
		cluster.setMinSize(1);
		cluster.setMaxSize(1);
		
		logger.info("Begin create cluster {0}.", new Object[] { cluster });
		ICluster mqcluster = null;
		try {
			mqcluster = getClusterManager().create(cluster);
		} catch (ClusterException e) {
			logger.error("Create {0} error.", new Object[] {cluster}, e);
			throw new OrderException(e);
		}
		logger.info("End create cluster {0}.", new Object[] { cluster });
		
		// 创建实例
		MsgQueueService service = new MsgQueueService();
		service.setCreatedDate(new Date());
		service.setCreatedBy(order.getHandler());
		service.setOwner(order.getOwner());
		service.setName(displayName);
		service.setPackageId(packageId);
		service.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$
		service.setHaMode(IService.HA_MODE_NONE);
		service.setUser(user);
		service.setPassword(password);
		service.setVhost(vhost);
		
		try {
			getServiceManager().add(service, mqcluster.getId());
		} catch (ServiceException e) {
			logger.error("Create {0} at cluster {1} error.", new Object[] {service, mqcluster.getId()}, e);
			throw new OrderException(e);
		}
		
		logger.info("Start MsgQueue cluster {0}.", new Object[] { mqcluster.getId() });
		try {
			getClusterManager().start(mqcluster.getId());
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
	 * @return
	 */
	protected static IClusterManager getClusterManager() {
		return clusterManager = null == clusterManager ? ClusterManagerFactory
				.getManager(MsgQueueService.TYPE) : clusterManager;
	}
	
	/**
	 * 
	 * @return
	 */
	protected static IMsgQueueServiceManager getServiceManager() {
		return serviceManager = null == serviceManager ? (IMsgQueueServiceManager) ServiceManagerFactory
				.getManager(MsgQueueService.TYPE) : serviceManager;
	}

}
