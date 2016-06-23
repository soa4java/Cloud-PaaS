/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.MemcachedCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.MemcachedService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Memcached集群管理类
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class MemcachedClusterMgrUtil {

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static IWebAppManager appManager = WebAppManagerFactory.getManager();

	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();

	/**
	 * 查询Memcahced服务集群
	 * 
	 * @param criteria 查询条件（类型、所有者）
	 * @param page 分页条件
	 * @return 集群列表
	 */
	public static List<MemcachedCluster> getMemcachedClusters(
			MemcachedCluster criteria, PageCond page) {
		List<MemcachedCluster> tempClusters = new ArrayList<MemcachedCluster>();
		List<MemcachedCluster> returnClusters = new ArrayList<MemcachedCluster>();
		List<MemcachedCluster> allMemcachedClusters = clusterManager.getByType(
				MemcachedCluster.TYPE, MemcachedCluster.class);

		for (MemcachedCluster c : allMemcachedClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}
		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (MemcachedCluster cluster : tempClusters) {
				int state = ClusterMangerUtil.getClusterState(cluster.getId());
				cluster.getAttributes().put("status", state + ""); //$NON-NLS-1$
				int size = ClusterMangerUtil.getClusterSize(cluster.getId());
				cluster.getAttributes().put("size", size + ""); //$NON-NLS-1$
			}
		}
		List<MemcachedCluster> clusters = new ArrayList<MemcachedCluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (MemcachedCluster temp : tempClusters) {
				if (temp.getAttributes()
						.get("status").equals(criteria.getAttributes().get("status"))) { //$NON-NLS-1$  //$NON-NLS-2$
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
	 * @param clusterId
	 * @return
	 */
	public static MemcachedCluster getCluster(String clusterId) {
		MemcachedCluster clt = clusterManager.get(clusterId,
				MemcachedCluster.class);
		if (clt == null) {
			return clt;
		}
		// 已绑定的应用
		String[] appNames = appManager.getRelationApp(clusterId);
		if (appNames != null) {
			//
			StringBuffer urlsBuffer = new StringBuffer();
			for (String appName : appNames) {
				urlsBuffer.append(appName + ",");
			}
			if (urlsBuffer.length() > 1) {
				urlsBuffer.deleteCharAt(urlsBuffer.length() - 1);
			}
			clt.getAttributes().put("apps", urlsBuffer.toString()); //$NON-NLS-1$

		} else {
			clt.getAttributes().put("apps", ""); //$NON-NLS-1$
		}
		// status
		int state = ClusterMangerUtil.getClusterState(clt.getId());
		clt.getAttributes().put("status", state + ""); //$NON-NLS-1$
		// size
		List<MemcachedService> instList = serviceQuery.getByCluster(clusterId,
				MemcachedService.class);
		if (instList == null || instList.isEmpty()) {
			clt.getAttributes().put("size", "0");

			return clt;
		}

		clt.getAttributes().put("size", instList.size() + ""); //$NON-NLS-1$

		MemcachedService inst = instList.get(0);
		if (inst == null || inst.getAttributes() == null) {
			return clt;
		}
		clt.getAttributes().put("ip", inst.getIp()); //$NON-NLS-1$
		clt.getAttributes().put("port", inst.getPort() + ""); //$NON-NLS-1$
		clt.getAttributes()
				.put("maxConnectionSize", String.valueOf(inst.getMaxConnectionSize())); //$NON-NLS-1$

		if (clt.getMemcachedSize() < 0) {
			clt.setMemcachedSize(inst.getMemorySize() * instList.size());
		}
		return clt;
	}

}