/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.cluster.KeepalivedCluster;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IKeepalivedServiceManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.KeepalivedService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * keepalived服务管理
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class KeepalivedMgrUtil {
	
	private static ILogger logger = LoggerFactory.getLogger(KeepalivedMgrUtil.class);

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static IServiceQuery srvQueryMgr = ServiceManagerFactory
			.getServiceQuery();

	private static IKeepalivedServiceManager keepalivedServiceManager = ServiceManagerFactory
			.getManager(KeepalivedService.TYPE);

	private static String CLUSTER_STATUS = "status";

	/**
	 * 
	 * @param page
	 * @return
	 */
	public static List<KeepalivedCluster> getClusters(PageCond page) {
		List<KeepalivedCluster> list = clusterManager.getByType(
				KeepalivedCluster.TYPE, page, KeepalivedCluster.class);
		if (list == null) {
			return new ArrayList<KeepalivedCluster>();
		}
		for (KeepalivedCluster cluster : list) {
			int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
			cluster.getAttributes().put(CLUSTER_STATUS, sizeAndStatus[1] + "");
			cluster.getAttributes().put("size", sizeAndStatus[0] + "");
		}
		return list;
	}

	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 */
	public static List<KeepalivedCluster> getClusters(
			KeepalivedCluster criteria, PageCond page) {
		List<KeepalivedCluster> tempClusters = new ArrayList<KeepalivedCluster>();
		List<KeepalivedCluster> returnClusters = new ArrayList<KeepalivedCluster>();

		List<KeepalivedCluster> allKeepalivedClusters = clusterManager
				.getByType(KeepalivedCluster.TYPE, KeepalivedCluster.class);

		for (KeepalivedCluster c : allKeepalivedClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}
		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (KeepalivedCluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + ""); //$NON-NLS-1$
				cluster.getAttributes().put("size", sizeAndStatus[0] + ""); //$NON-NLS-1$
			}
		}
		List<KeepalivedCluster> clusters = new ArrayList<KeepalivedCluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (KeepalivedCluster temp : tempClusters) {
				if (temp.getAttributes().get("status") //$NON-NLS-1$
						.equals(criteria.getAttributes().get("status"))) { //$NON-NLS-1$
					clusters.add(temp);
				}
			}
		} else {
			clusters.addAll(tempClusters);
		}

		page.setCount(clusters.size());
		int end = page.getBegin() + page.getLength() - 1;
		if (page.getBegin() + page.getLength() > page.getCount()) {
			end = page.getCount() - 1;
		}

		for (int i = page.getBegin(); i <= end; i++) {
			returnClusters.add(clusters.get(i));
		}
		return returnClusters;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static KeepalivedCluster getCluster(String id) {
		return clusterManager.get(id, KeepalivedCluster.class);
	}

	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public static List<KeepalivedService> getInstances(String clusterId) {
		return srvQueryMgr.getByCluster(clusterId, KeepalivedService.class);
	}

	/**
	 * 
	 * @param instanceId
	 * @return
	 */
	public static boolean start(String instanceId) {
		try {
			keepalivedServiceManager.start(instanceId);
			return true;
		} catch (ServiceException e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 
	 * @param instanceId
	 * @return
	 */
	public static boolean stop(String instanceId) {
		try {
			keepalivedServiceManager.stop(instanceId);
			return true;
		} catch (ServiceException e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 
	 * @param instanceId
	 * @return
	 */
	public static boolean destroy(String instanceId) {
		try {
			keepalivedServiceManager.destroy(instanceId);
			return true;
		} catch (ServiceException e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static KeepalivedService getInstance(String id) {
		return srvQueryMgr.get(id, KeepalivedService.class);
	}

	/**
	 * 
	 * @param clusterId
	 * @param instance
	 * @return
	 */
	public static boolean update(String clusterId, KeepalivedService instance) {
		if (StringUtil.isEmpty(clusterId) || instance == null
				|| StringUtil.isEmpty(instance.getId())) {
			return false;
		}
		KeepalivedService service = getInstance(instance.getId());
		if (service == null) {
			return false;
		}
		instance.setIp(service.getIp());
		instance.setName(service.getName());
		instance.setOwner(service.getOwner());
		instance.setParentId(service.getParentId());
		instance.setPid(service.getPid());
		instance.setPort(service.getPort());
		instance.setState(service.getState());
		instance.setCreatedBy(service.getCreatedBy());
		instance.setStartDate(service.getStartDate());
		instance.setCreatedDate(service.getCreatedDate());

		try {
			keepalivedServiceManager.update(instance, clusterId);
			return true;
		} catch (ServiceException e) {
			logger.error(e);
		}
		return false;
	}

}
