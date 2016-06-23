/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.platform.service.audit.OrderManagerUtil;
import com.primeton.paas.manage.api.cluster.KeepalivedCluster;
import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterFactory;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.OrderManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.KeepalivedService;
import com.primeton.paas.manage.api.service.NginxService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 集群管理类（集群的查询、启动、停止、删除）
 * 
 * @author liyanping(liyp@primeton.com)
 * 
 */
public class ClusterMangerUtil {

	private static ILogger logger = LoggerFactory.getLogger(ClusterMangerUtil.class);

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager();

	private static IServiceQuery srvQueryMgr = ServiceManagerFactory.getServiceQuery();

	private static IOrderManager orderManager = OrderManagerFactory
			.getManager();

	/**
	 * 指定条件查询集群列表<br>
	 * 
	 * @param criteria
	 *            (IP/owner/type)
	 * @param page
	 * @return
	 */
	public static ICluster[] getClusters(MyCluster criteria, PageCond page) {
		String type = criteria.getType();
		String owner = criteria.getOwner();

		List<ICluster> clusterList = new ArrayList<ICluster>();

		if (!StringUtil.isEmpty(type)) {
			ICluster[] clusters = clusterManager.getByType(type);
			if (clusters != null && clusters.length > 1) {
				List<ICluster> list1 = Arrays.asList(clusters);

				if (list1 == null || list1.isEmpty()) {
					page.setCount(0);
					return new ICluster[0];
				}
				clusterList.addAll(list1);
			}
		}

		if (!StringUtil.isEmpty(owner)) {
			ICluster[] clusters = clusterManager.getByOwner(owner, null);
			if (clusters != null && clusters.length > 1) {
				List<ICluster> list2 = Arrays.asList(clusters);

				if (list2 == null || list2.isEmpty()) {
					page.setCount(0);
					return new ICluster[0];
				} else if (clusterList == null || clusterList.isEmpty()) {

					clusterList.addAll(list2);
				} else {

					clusterList.retainAll(list2);
				}
			}
		}

		page.setCount(clusterList.size());
		int end = page.getBegin() + page.getLength() - 1;
		if (page.getBegin() + page.getLength() > page.getCount()) {
			end = page.getCount() - 1;
		}

		List<ICluster> retList = new ArrayList<ICluster>();
		for (int i = page.getBegin(); i <= end; i++) {
			retList.add(clusterList.get(i));
		}

		return retList.toArray(new ICluster[retList.size()]);
	}

	/**
	 * 获取所有的集群类型<br>
	 * 
	 * @return
	 */
	public static String[] getClusterType() {
		return clusterManager.getTypes();
	}

	/**
	 * 获取集群信息<br>
	 * 
	 * @param clusterId
	 * @return
	 */
	public static ICluster getCluster(String clusterId) {
		return clusterManager.get(clusterId);
	}

	/**
	 * 获取集群的状态<br>
	 * 策略：集群中只要有一个实例是运行状态，集群状态为运行
	 * 
	 * @param clusterId
	 *            集群标识
	 * @return
	 */
	public static int getClusterState(String clusterId) {
		IService[] services = srvQueryMgr.getByCluster(clusterId);
		if (services != null && services.length > 0) {
			for (IService s : services) {
				if (s.getState() == IService.STATE_RUNNING) {
					return IService.STATE_RUNNING;
				}
			}
		}
		return IService.STATE_NOT_RUNNING;
	}

	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public static int getClusterSize(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return 0;
		}
		IService[] services = srvQueryMgr.getByCluster(clusterId);
		return null == services ? 0 : services.length;
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public static int[] getClusterSizeAndStatus(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return new int[] {0, IService.STATE_NOT_RUNNING};
		}
		IService[] services = srvQueryMgr.getByCluster(clusterId);
		boolean isRunning = false;
		int size = 0;
		if (services != null && services.length > 0) {
			size = services.length;
			for (IService s : services) {
				if (s.getState() == IService.STATE_RUNNING) {
					isRunning = true;
					break;
				}
			}
		}
		return new int[] {size, isRunning ? IService.STATE_RUNNING : IService.STATE_NOT_RUNNING};
	}

	/**
	 * 移除集群<br>
	 * 
	 * @param clusterId
	 * @param type
	 * @return
	 */
	public static String removeCluster(String clusterId, String type) {
		if (StringUtil.isEmpty(clusterId) || StringUtil.isEmpty(type)) {
			return "Illegal argument clusterId is null or empty!";
		}
		ICluster cluster = clusterManager.get(clusterId);
		if (cluster == null) {
			return "Cluster [" + clusterId + "] not exists.";
		}

		String owner = DataContextManager.current().getMUODataContext()
				.getUserObject().getUserId();

		Order order = new Order();
		order.setSubmitTime(new Date());// submitTime
		order.setOrderStatus(Order.ORDER_STATUS_SUBMITED);// /orderStatus
		order.setOrderType(Order.ORDER_TYPE_DELETE_SRV);// orderType
		order.setOwner(owner);
		order.setHandler(owner);

		List<OrderItem> itemList = new ArrayList<OrderItem>();
		OrderItem item = new OrderItem();
		item.setItemType(cluster.getType());

		List<OrderItemAttr> attrList = new ArrayList<OrderItemAttr>();
		OrderItemAttr attr = new OrderItemAttr();
		attr.setAttrName(OrderItemAttr.ATTR_CLUSTER_ID); // clusterId
		attr.setAttrValue(clusterId);
		attrList.add(attr);

		if (!StringUtil.isEmpty(cluster.getName())) {
			attr = new OrderItemAttr();
			attr.setAttrName(OrderItemAttr.ATTR_DISPLAY_NAME); // displayName
			attr.setAttrValue(cluster.getName());
			attrList.add(attr);
		}
		item.setAttrList(attrList);
		itemList.add(item);
		order.setItemList(itemList);

		Order newOrder = null;
		try {
			newOrder = orderManager.add(order);
		} catch (Exception e) {
			logger.error(e);
			return "Save order error, " + e.getMessage();
		}
		String orderId = newOrder == null ? null : newOrder.getOrderId();
		if (orderId == null) {
			return "Save order error!";
		}
		// handle order
		OrderManagerUtil.asynHandleOrder(orderId);
		return "Submit order [" + orderId + "] success.";
	}

	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public static boolean startCluster(String clusterId) {
		return startCluster(clusterId, null);
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param type
	 * @return
	 */
	public static boolean startCluster(String clusterId, String type) {
		if (StringUtil.isEmpty(clusterId)) {
			return false;
		}
		if (StringUtil.isEmpty(type)) {
			ICluster cluster = clusterManager.get(clusterId);
			if (null == cluster) {
				return false;
			}
			type = cluster.getType();
		}
		// Nginx
		if (NginxService.TYPE.equals(type)) {
			return startNginxCluster(clusterId);
		}
		IClusterManager manager = ClusterManagerFactory.getManager(type);
		manager = null == manager ? clusterManager : manager;
		try {
			manager.start(clusterId);
			return true;
		} catch (ServiceException e) {
			logger.error("Start {0} cluster {1} error.", new Object[] { type, clusterId}, e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public static boolean restartCluster(String clusterId) {
		return restartCluster(clusterId, null);
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param type
	 * @return
	 */
	public static boolean restartCluster(String clusterId, String type) {
		if (StringUtil.isEmpty(clusterId)) {
			return false;
		}
		if (StringUtil.isEmpty(type)) {
			ICluster cluster = clusterManager.get(clusterId);
			if (null == cluster) {
				return false;
			}
			type = cluster.getType();
		}
		IClusterManager manager = ClusterManagerFactory.getManager(type);
		manager = null == manager ? clusterManager : manager;
		try {
			manager.restart(clusterId);
			return true;
		} catch (ServiceException e) {
			logger.error("Restart {0} cluster {1} error.", new Object[] { type, clusterId}, e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public static boolean stopCluster(String clusterId) {
		return stopCluster(clusterId, null);
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param type
	 * @return
	 */
	public static boolean stopCluster(String clusterId, String type) {
		if (StringUtil.isEmpty(clusterId)) {
			return false;
		}
		if (StringUtil.isEmpty(type)) {
			ICluster cluster = clusterManager.get(clusterId);
			if (null == cluster) {
				return false;
			}
			type = cluster.getType();
		}
		IClusterManager manager = ClusterManagerFactory.getManager(type);
		manager = null == manager ? clusterManager : manager;
		try {
			manager.stop(clusterId);
			return true;
		} catch (ServiceException e) {
			logger.error("Shut down {0} cluster {1} error.", new Object[] { type, clusterId}, e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public static boolean destroyCluster(String clusterId) {
		return destroyCluster(clusterId, null);
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param type
	 * @return
	 */
	public static boolean destroyCluster(String clusterId, String type) {
		if (StringUtil.isEmpty(clusterId)) {
			return false;
		}
		if (StringUtil.isEmpty(type)) {
			ICluster cluster = clusterManager.get(clusterId);
			if (null == cluster) {
				return false;
			}
			type = cluster.getType();
		}
		IClusterManager manager = ClusterManagerFactory.getManager(type);
		manager = null == manager ? clusterManager : manager;
		try {
			manager.destroy(clusterId);
			return true;
		} catch (ClusterException e) {
			logger.error("Destroy {0} cluster {1} error.", new Object[] { type, clusterId}, e);
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	private static boolean startNginxCluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return false;
		}
		IClusterManager manager = ClusterManagerFactory.getManager(NginxService.TYPE);
		manager = null == manager ? clusterManager : manager;
		try {
			manager.start(clusterId);
		} catch (Exception e) {
			logger.error("Start nginx cluster {1} error.", new Object[] { clusterId}, e);
			return false;
		}
		// 查询关联的keepalived集群
		String[] relCltIds = clusterManager.getRelationClustersId(clusterId, KeepalivedCluster.TYPE);
		if (relCltIds != null && relCltIds.length > 0) {
			IClusterManager kmanager = ClusterManagerFactory.getManager(KeepalivedService.TYPE);
			kmanager = null == kmanager ? clusterManager : kmanager;
			try {
				for (int i = 0; i < relCltIds.length; i++) {
					String Id = relCltIds[i];
					kmanager.start(Id);
				}
			} catch (Exception e) {
				logger.error("Start Keepalived cluster {1} error.", new Object[] { clusterId}, e);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param user
	 * @param pageCond
	 * @param clazz
	 * @return
	 */
	public static <T extends ICluster> List<T> getClustersByUser(String user,
			PageCond pageCond, Class<T> clazz) {
		if (StringUtil.isEmpty(user)) {
			return new ArrayList<T>();
		}
		
		List<T> list = clusterManager.getByOwner(user,
				ClusterFactory.getType(clazz), pageCond, clazz);

		for (ICluster cluster : list) {
			int[] sizeAndStatus = ClusterMangerUtil
					.getClusterSizeAndStatus(cluster.getId());
			cluster.getAttributes().put(
					"state", String.valueOf(sizeAndStatus[1])); //$NON-NLS-1$
			cluster.getAttributes().put(
					"size", String.valueOf(sizeAndStatus[0])); //$NON-NLS-1$
		}
		return list == null ? new ArrayList<T>() : list;
	}

}
