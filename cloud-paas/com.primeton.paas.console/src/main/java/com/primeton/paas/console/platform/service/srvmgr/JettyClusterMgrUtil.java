/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Jetty集群管理类
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class JettyClusterMgrUtil {

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();

	/**
	 * 查询Jetty集群
	 * 
	 * @param criteria
	 *            查询条件（类型、所有者）
	 * @param page
	 *            分页条件
	 * @return 集群列表
	 */
	public static List<JettyCluster> getJettyClusters(JettyCluster criteria,
			PageCond page) {
		List<JettyCluster> tempClusters = new ArrayList<JettyCluster>();
		List<JettyCluster> returnClusters = new ArrayList<JettyCluster>();
		List<JettyCluster> allJettyClusters = clusterManager.getByType(
				JettyCluster.TYPE, JettyCluster.class);

		for (JettyCluster c : allJettyClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}

		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (JettyCluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + ""); //$NON-NLS-1$
				cluster.getAttributes().put("size", sizeAndStatus[0] + ""); //$NON-NLS-1$
			}
		}
		List<JettyCluster> clusters = new ArrayList<JettyCluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (JettyCluster temp : tempClusters) {
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
	 * @param clusterId
	 * @return
	 */
	public static JettyCluster getCluster(String clusterId) {
		JettyCluster clt = clusterManager.get(clusterId, JettyCluster.class);

		List<JettyService> instList = serviceQuery.getByCluster(clusterId,
				JettyService.class);
		if (instList != null && !instList.isEmpty() && instList.get(0) != null) {
			JettyService instance = instList.get(0);
			clt.getAttributes().putAll(instance.getAttributes());
		}
		return clt;
	}

}
