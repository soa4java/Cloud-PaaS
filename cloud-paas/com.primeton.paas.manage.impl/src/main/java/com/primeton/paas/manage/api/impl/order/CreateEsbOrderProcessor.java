/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.manage.api.impl.order;

import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.cluster.EsbCluster;
import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.exception.OrderException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.manager.AbstractOrderProcessor;
import com.primeton.paas.manage.api.impl.util.ServiceUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IEsbConsoleServiceManager;
import com.primeton.paas.manage.api.manager.IEsbSAMServiceManager;
import com.primeton.paas.manage.api.manager.IEsbSSMServiceManager;
import com.primeton.paas.manage.api.manager.IEsbServerServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.service.EsbConsoleService;
import com.primeton.paas.manage.api.service.EsbSAMService;
import com.primeton.paas.manage.api.service.EsbSSMService;
import com.primeton.paas.manage.api.service.EsbServerService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 创建ESB服务订单处理器实现. <br>
 * 
 * 创建一组ESB套件，包括1个Console、1一个SAM、n个Server、n个SSM （1个Server对应1个SSM）。
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CreateEsbOrderProcessor extends AbstractOrderProcessor {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(CreateEsbOrderProcessor.class);
	
	public static final String TYPE = "CreateESB";
	
	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(EsbCluster.TYPE);

	/**
	 * Default. <br>
	 */
	public CreateEsbOrderProcessor() {
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
		OrderItem item = getItemByType(order, EsbCluster.TYPE);
		if (null == item) {
			List<OrderItem> items = order.getItemList();
			if (null == items || items.isEmpty()) {
				order = getOrderManager().getOrderWithItems(order.getOrderId());
				items = order.getItemList();
			}
			item = (null == items || items.isEmpty()) ? null : items.get(0);
		}
		if (null == item) {
			logger.warn("Order [" + order.getOrderType() + ", " + order.getOrderId() + "] none ESB item.");
			performOk(order);
			return;
		}
		item.setItemStatus(OrderItem.ITEM_STATUS_PROCESSING);
		item.setHandleTime(new Date());
		getOrderManager().updateItem(item);
		
		// 解析订单项属性
		int size = getItemValue(item, OrderItemAttr.ATTR_CLUSTER_SIZE, 1); //$NON-NLS-1$
		String displayName = getItemValue(item, OrderItemAttr.ATTR_DISPLAY_NAME, "ESB service"); //$NON-NLS-1$
		String packageId = getItemValue(item, OrderItemAttr.ATTR_HOSTPKG_ID); //$NON-NLS-1$
		// String isStandalone = getItemValue(item, OrderItemAttr.ATTR_IS_STANDALONE, "N"); //$NON-NLS-1$
		String databaseType =  getItemValue(item, OrderItemAttr.ATTR_DATABASE_TYPE, "Oracle"); //$NON-NLS-1$
		String c3p0Url =  getItemValue(item, OrderItemAttr.ATTR_C3P0_URL, ""); //$NON-NLS-1$
		String c3p0UserName =  getItemValue(item, OrderItemAttr.ATTR_C3P0_USER_NAME, ""); //$NON-NLS-1$
		String c3p0Password =  getItemValue(item, OrderItemAttr.ATTR_C3P0_PASSWORD, ""); //$NON-NLS-1$
		int c3p0PoolSize = getItemValue(item, OrderItemAttr.ATTR_C3P0_POOL_SIZE, 100); //$NON-NLS-1$
		int c3p0MaxPoolSize = getItemValue(item, OrderItemAttr.ATTR_C3P0_MAX_POOL_SIZE, 200); //$NON-NLS-1$
		int c3p0MinPoolSize = getItemValue(item, OrderItemAttr.ATTR_C3P0_MIN_POOL_SIZE, 10); //$NON-NLS-1$
		
		if (StringUtil.isEmpty(packageId)) {
			throw new OrderException("Create ESB error, ${packageId} is empty. Please check you order information.");
		}
		
		// 新建ESB集群
		EsbCluster esbCluster = new EsbCluster();
		esbCluster.setMaxSize(size * 2 + 2);
		esbCluster.setMinSize(esbCluster.getMaxSize());
		esbCluster.setOwner(order.getOwner());
		esbCluster.setName(displayName);
		
		EsbCluster cluster = null;
		try {
			cluster = getClusterManager().create(esbCluster, EsbCluster.class);
		} catch (ClusterException e) {
			logger.error(e);
			item.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
			getOrderManager().updateItem(item);
			throw new OrderException(e);
		}
		final String clusterId = null == cluster ? null : cluster.getId();
		if (StringUtil.isEmpty(clusterId)) {
			throw new OrderException("Create ESB cluster " + cluster + " error.");
		}
		
		// 新建ESB Console服务
		{
			EsbConsoleService service = new EsbConsoleService();
			service.setName("ESB Console"); //$NON-NLS-1$
			service.setCreatedBy(order.getHandler());
			service.setCreatedDate(new Date());
			service.setDatabaseType(databaseType);
			service.setHaMode(IService.HA_MODE_NONE);
			service.setJdbcPassword(c3p0Password);
			service.setJdbcUrl(c3p0Url);
			service.setJdbcUser(c3p0UserName);
			service.setOwner(order.getOwner());
			service.setPackageId(packageId);
			service.setStandalone(true); //$NON-NLS-1$
			service.setMaxMemorySize(SystemVariables.getJvmMaxMemorySize(packageId));
			service.setMinMemorySize(SystemVariables.getJvmMinMemorySize(packageId));
			service.setMaxPermSize(SystemVariables.getJvmMaxPermSize(packageId));
			service.setMinPermSize(SystemVariables.getJvmMinPermSize(packageId));
			
			IEsbConsoleServiceManager manager = ServiceManagerFactory.getManager(EsbConsoleService.TYPE);
			try {
				service = manager.add(service, clusterId);
				manager.start(service.getId());
			} catch (Throwable e) {
				logger.error("Process order {0} {1}, create ESB Console service error."
						, new Object[] { order.getOrderType(), order.getOrderId() }, e);
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.info("Begin clean ESB cluster {0}.", new Object[] { clusterId });
					try {
						item.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
						getOrderManager().updateItem(item);
						getClusterManager().destroy(clusterId);
					} catch (Throwable t) {
						logger.error(t);
					}
					throw new OrderException("Create ESB Console service for cluster [" + clusterId + "] error.", e);
				}
			}
		}
		
		// 创建ESB SAM服务
		{
			EsbSAMService service = new EsbSAMService();
			service.setName("ESB SAM");
			service.setCreatedBy(order.getHandler());
			service.setCreatedDate(new Date());
			service.setDatabaseType(databaseType);
			service.setHaMode(IService.HA_MODE_NONE);
			service.setJdbcPassword(c3p0Password);
			service.setJdbcUrl(c3p0Url);
			service.setJdbcUser(c3p0UserName);
			service.setOwner(order.getOwner());
			service.setPackageId(packageId);
			service.setStandalone(true); //$NON-NLS-1$
			service.setMaxMemorySize(SystemVariables.getJvmMaxMemorySize(packageId));
			service.setMinMemorySize(SystemVariables.getJvmMinMemorySize(packageId));
			service.setMaxPermSize(SystemVariables.getJvmMaxPermSize(packageId));
			service.setMinPermSize(SystemVariables.getJvmMinPermSize(packageId));
			
			IEsbSAMServiceManager manager = ServiceManagerFactory.getManager(EsbSAMService.TYPE);
			try {
				service = manager.add(service, clusterId);
				manager.start(service.getId());
			} catch (Throwable e) {
				logger.error("Process order {0} {1}, create ESB SAM service error."
						, new Object[] { order.getOrderType(), order.getOrderId() }, e);
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.info("Begin clean ESB cluster {0}.", new Object[] { clusterId });
					try {
						item.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
						getOrderManager().updateItem(item);
						getClusterManager().destroy(clusterId);
					} catch (Throwable t) {
						logger.error(t);
					}
					throw new OrderException("Create ESB SAM service for cluster [" + clusterId + "] error.", e);
				}
			}
		}
		// 创建ESB Server服务
		List<EsbServerService> servers = null;
		{
			EsbServerService service = new EsbServerService();
			service.setC30p0MaxPoolSize(c3p0MaxPoolSize);
			service.setC30p0MinPoolSize(c3p0MinPoolSize);
			service.setC3p0Password(c3p0Password);
			service.setC3p0PoolSize(c3p0PoolSize);
			service.setC3p0Url(c3p0Url);
			service.setC3p0UserName(c3p0UserName);
			service.setCreatedBy(order.getHandler());
			service.setCreatedDate(new Date());
			service.setDatabaseType(databaseType);
			service.setHaMode(IService.HA_MODE_CLUSTER);
			service.setName("ESB Server"); //$NON-NLS-1$
			service.setOwner(order.getOwner());
			service.setPackageId(packageId);
			service.setStandalone(true); //$NON-NLS-1$
			service.setMaxMemorySize(SystemVariables.getJvmMaxMemorySize(packageId) / 2);
			service.setMinMemorySize(SystemVariables.getJvmMinMemorySize(packageId) / 2);
			service.setMaxPermSize(SystemVariables.getJvmMaxPermSize(packageId) / 2);
			service.setMinPermSize(SystemVariables.getJvmMinPermSize(packageId) / 2);
			
			IEsbServerServiceManager manager = ServiceManagerFactory.getManager(EsbServerService.TYPE);
			try {
				servers = manager.add(service, clusterId, size);
				for (EsbServerService instance : servers) {
					manager.start(instance.getId());
				}
			} catch (Throwable e) {
				logger.error("Process order {0} {1}, create ESB Server service error."
						, new Object[] { order.getOrderType(), order.getOrderId() }, e);
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.info("Begin clean ESB cluster {0}.", new Object[] { clusterId });
					try {
						item.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
						getOrderManager().updateItem(item);
						getClusterManager().destroy(clusterId);
					} catch (Throwable t) {
						logger.error(t);
					}
					throw new OrderException("Create ESB Server service for cluster [" + clusterId + "] error.", e);
				}
			}
		}
		
		// 创建ESB SSM服务, Server (one) - SSM (one)
		{
			EsbSSMService service = new EsbSSMService();
			service.setCreatedBy(order.getHandler());
			service.setCreatedDate(new Date());
			service.setDatabaseType(databaseType);
			service.setHaMode(IService.HA_MODE_CLUSTER);
			service.setJdbcPassword(c3p0Password);
			service.setJdbcUrl(c3p0Url);
			service.setJdbcUser(c3p0UserName);
			service.setName("ESB SSM"); //$NON-NLS-1$
			service.setOwner(order.getOwner());
			service.setPackageId(packageId);
			service.setStandalone(true); //$NON-NLS-1$
			service.setMaxMemorySize(SystemVariables.getJvmMaxMemorySize(packageId) / 2);
			service.setMinMemorySize(SystemVariables.getJvmMinMemorySize(packageId) / 2);
			service.setMaxPermSize(SystemVariables.getJvmMaxPermSize(packageId) / 2);
			service.setMinPermSize(SystemVariables.getJvmMinPermSize(packageId) / 2);
			
			IEsbSSMServiceManager manager = ServiceManagerFactory.getManager(EsbSSMService.TYPE);
			try {
				for (EsbServerService instance : servers) {
					EsbSSMService ess = ServiceUtil.copy(service);
					ess.setIp(instance.getIp());
					try {
						ess = manager.add(ess, clusterId);
						manager.start(ess.getId());
					} catch (Throwable e) {
						logger.error(e);
					}
				}
			} catch (Throwable e) {
				logger.error("Process order {0} {1}, create ESB SSM service error."
						, new Object[] { order.getOrderType(), order.getOrderId() }, e);
				if (SystemVariables.isCleanAfterProcessError()) {
					logger.info("Begin clean ESB cluster {0}.", new Object[] { clusterId });
					try {
						item.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
						getOrderManager().updateItem(item);
						getClusterManager().destroy(clusterId);
					} catch (Throwable t) {
						logger.error(t);
					}
					throw new OrderException("Create ESB SSM service for cluster [" + clusterId + "] error.", e);
				}
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
		// only one item
		process(order);
	}

	/**
	 * 
	 * @return
	 */
	protected static IClusterManager getClusterManager() {
		return clusterManager = null == clusterManager ? ClusterManagerFactory
				.getManager(EsbCluster.TYPE) : clusterManager;
	}

}
