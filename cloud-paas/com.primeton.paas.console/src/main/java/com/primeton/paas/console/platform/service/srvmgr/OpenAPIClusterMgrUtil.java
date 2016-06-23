/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.OpenAPICluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * OpenAPI集群管理
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class OpenAPIClusterMgrUtil {

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);
	/**
	 * 查询OpenAPI服务集群
	 * 
	 * @param criteria
	 *            查询条件（类型、所有者）
	 * @param page
	 *            分页条件
	 * @return 集群列表
	 */
	public static List<OpenAPICluster> getOpenAPIClusters(
			OpenAPICluster criteria, PageCond page) {
		List<OpenAPICluster> tempClusters = new ArrayList<OpenAPICluster>();
		List<OpenAPICluster> returnClusters = new ArrayList<OpenAPICluster>();
		List<OpenAPICluster> allOpenAPIClusters = clusterManager.getByType(
				OpenAPICluster.TYPE, OpenAPICluster.class);
		for (OpenAPICluster c : allOpenAPIClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}
		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (OpenAPICluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + ""); //$NON-NLS-1$
				cluster.getAttributes().put("size", sizeAndStatus[0] + ""); //$NON-NLS-1$
			}
		}
		List<OpenAPICluster> clusters = new ArrayList<OpenAPICluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (OpenAPICluster temp : tempClusters) {
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
	public static OpenAPICluster getCluster(String clusterId) {
		OpenAPICluster c = clusterManager.get(clusterId, OpenAPICluster.class);
		return c;
	}

}
