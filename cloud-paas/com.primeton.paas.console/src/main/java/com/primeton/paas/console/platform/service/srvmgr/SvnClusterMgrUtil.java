/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.SVNCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 资源库(SVN)服务集群管理类
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class SvnClusterMgrUtil {

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	/**
	 * 根据查询条件查询svnrepository服务集群列表<br>
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 */
	public static List<SVNCluster> getSvnClusters(SVNCluster criteria,
			PageCond page) {
		List<SVNCluster> tempClusters = new ArrayList<SVNCluster>();
		List<SVNCluster> returnClusters = new ArrayList<SVNCluster>();

		List<SVNCluster> allSVNClusters = clusterManager.getByType(
				SVNCluster.TYPE, SVNCluster.class);

		for (SVNCluster c : allSVNClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}
		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (SVNCluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + "");
				cluster.getAttributes().put("size", sizeAndStatus[0] + "");
			}
		}
		List<SVNCluster> clusters = new ArrayList<SVNCluster>();
		if (criteria.getAttributes().get("status") != null) {
			for (SVNCluster temp : tempClusters) {
				if (temp.getAttributes().get("status")
						.equals(criteria.getAttributes().get("status"))) {
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
	 * 获取SVNRepository集群<br>
	 * 
	 * @param clusterId
	 *            集群标识
	 * @return
	 */
	public static SVNCluster getCluster(String clusterId) {
		return StringUtil.isEmpty(clusterId) ? null : clusterManager.get(
				clusterId, SVNCluster.class);
	}

}
