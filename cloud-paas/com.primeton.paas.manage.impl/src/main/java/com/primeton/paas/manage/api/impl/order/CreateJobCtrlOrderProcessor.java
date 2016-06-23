/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.order;

import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.cluster.JobCtrlCluster;
import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.exception.OrderException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.manager.AbstractOrderProcessor;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IJobCtrlServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.service.JobCtrlService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 订单处理器：创建JobCtrl服务. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CreateJobCtrlOrderProcessor extends AbstractOrderProcessor {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(CreateJobCtrlOrderProcessor.class);
	
	/**
	 * Type of order. <br>
	 */
	public static final String TYPE = "CreateJobCtrl";
	
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager(JobCtrlService.TYPE);
	private static IJobCtrlServiceManager serviceManager = ServiceManagerFactory.getManager(JobCtrlService.TYPE);

	/**
	 * Default. <br>
	 */
	public CreateJobCtrlOrderProcessor() {
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
		OrderItem item = getItemByType(order, JobCtrlService.TYPE);
		if (null == item) {
			List<OrderItem> items = order.getItemList();
			if (null == items || items.isEmpty()) {
				order = getOrderManager().getOrderWithItems(order.getOrderId());
				items = order.getItemList();
			}
			item = (null == items || items.isEmpty()) ? null : items.get(0);
		}
		if (null == item) {
			logger.warn("Order [" + order.getOrderType() + ", " + order.getOrderId() + "] none JobCtrl item.");
			performOk(order);
			return;
		}
		item.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
		item.setHandleTime(new Date());
		getOrderManager().updateItem(item);
		
		// 解析订单项属性
		
		String displayName = getItemValue(item, OrderItemAttr.ATTR_DISPLAY_NAME, "JobCtrl service"); //$NON-NLS-1$
		String packageId = getItemValue(item, OrderItemAttr.ATTR_HOSTPKG_ID); //$NON-NLS-1$
		String isStandalone = getItemValue(item, OrderItemAttr.ATTR_IS_STANDALONE, "N"); //$NON-NLS-1$
		String databaseType =  getItemValue(item, OrderItemAttr.ATTR_DATABASE_TYPE, "Oracle"); //$NON-NLS-1$
		String c3p0Url =  getItemValue(item, OrderItemAttr.ATTR_C3P0_URL, ""); //$NON-NLS-1$
		String c3p0UserName =  getItemValue(item, OrderItemAttr.ATTR_C3P0_USER_NAME, ""); //$NON-NLS-1$
		String c3p0Password =  getItemValue(item, OrderItemAttr.ATTR_C3P0_PASSWORD, ""); //$NON-NLS-1$
		int c3p0PoolSize = getItemValue(item, OrderItemAttr.ATTR_C3P0_POOL_SIZE, 100); //$NON-NLS-1$
		int c3p0MaxPoolSize = getItemValue(item, OrderItemAttr.ATTR_C3P0_MAX_POOL_SIZE, 200); //$NON-NLS-1$
		int c3p0MinPoolSize = getItemValue(item, OrderItemAttr.ATTR_C3P0_MIN_POOL_SIZE, 10); //$NON-NLS-1$
		int schedulerSaveDays = getItemValue(item, OrderItemAttr.ATTR_SCHEDULER_SAVE_DAYS, 7); //$NON-NLS-1$
		String schedulerTimeRound = getItemValue(item, OrderItemAttr.ATTR_SCHEDULER_TIME_ROUND, "8:40-9:50"); //$NON-NLS-1$
		
		if (StringUtil.isEmpty(packageId)) {
			throw new OrderException("Create Gateway error, ${packageId} is empty. Please check you order information.");
		}
		
		JobCtrlCluster cluster = new JobCtrlCluster();
		cluster.setMaxSize(1);
		cluster.setMinSize(1);
		cluster.setName(displayName);
		cluster.setOwner(order.getOwner());
		
		JobCtrlCluster jobCtrlCluster = null;
		try {
			jobCtrlCluster = getClusterManager().create(cluster, JobCtrlCluster.class);
		} catch (ClusterException e) {
			logger.error("Create cluster {0} error.", new Object[] { cluster }, e);
			throw new OrderException(e);
		}
		
		String clusterId = null == jobCtrlCluster ? null : jobCtrlCluster.getId();
		if (StringUtil.isEmpty(clusterId)) {
			throw new OrderException("Create JobCtrl cluster error, " + cluster);
		}
		
		JobCtrlService service = new JobCtrlService();
		service.setCreatedDate(new Date());
		service.setCreatedBy(order.getHandler());
		service.setOwner(order.getOwner());
		service.setName(displayName);
		service.setPackageId(packageId);
		service.setStandalone("Y".equalsIgnoreCase(isStandalone)); //$NON-NLS-1$
		service.setC30p0MaxPoolSize(c3p0MaxPoolSize);
		service.setC30p0MinPoolSize(c3p0MinPoolSize);
		service.setC3p0PoolSize(c3p0PoolSize);
		service.setC3p0Password(c3p0Password);
		service.setC3p0Url(c3p0Url);
		service.setC3p0UserName(c3p0UserName);
		service.setDatabaseType(databaseType);
		service.setHaMode(IService.HA_MODE_NONE);
		service.setName(displayName);
		service.setSchedulerSaveDays(schedulerSaveDays);
		service.setSchedulerTimeRound(schedulerTimeRound);
		service.setMaxMemorySize(SystemVariables.getJvmMaxMemorySize(packageId));
		service.setMinMemorySize(SystemVariables.getJvmMinMemorySize(packageId));
		service.setMaxPermSize(SystemVariables.getJvmMaxPermSize(packageId));
		service.setMinPermSize(SystemVariables.getJvmMinPermSize(packageId));
		
		try {
			getServiceManager().add(service, clusterId);
		} catch (ServiceException e) {
			if (SystemVariables.isCleanAfterProcessError()) {
				logger.info("Begin clean JobCtrl cluster {0}.", new Object[] { clusterId });
				try {
					getClusterManager().destroy(clusterId);
				} catch (Throwable t) {
					logger.error(t);
				}
				throw new OrderException("Create JobCtrl service for cluster [" + clusterId +"] error.", e);
			}
		}
		
		logger.info("Start JobCtrl cluster {0}.", new Object[] { clusterId });
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
		// only one item
		process(order);
	}

	/**
	 * 
	 * @return
	 */
	protected static IClusterManager getClusterManager() {
		return clusterManager = null == clusterManager ? ClusterManagerFactory
				.getManager(JobCtrlService.TYPE) : clusterManager;
	}
	
	/**
	 * 
	 * @return
	 */
	protected static IJobCtrlServiceManager getServiceManager() {
		return serviceManager = null == serviceManager ? (IJobCtrlServiceManager) ServiceManagerFactory
				.getManager(JobCtrlService.TYPE) : serviceManager;
	}

}
