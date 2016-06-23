/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.CEPEngineCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * cep集群管理
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class CEPEngineClusterMgrUtil {

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	/**
	 * 查询CEPAnalysis集群
	 * 
	 * @param criteria
	 *            查询条件
	 * @param page
	 *            分页条件
	 * @return 集群列表
	 */
	public static List<CEPEngineCluster> getCEPAnalysisCluster(
			CEPEngineCluster criteria, PageCond page) {

		List<CEPEngineCluster> tempClusters = new ArrayList<CEPEngineCluster>();
		List<CEPEngineCluster> returnClusters = new ArrayList<CEPEngineCluster>();

		List<CEPEngineCluster> allCEPEngineClusters = clusterManager.getByType(
				CEPEngineCluster.TYPE, CEPEngineCluster.class);

		for (CEPEngineCluster c : allCEPEngineClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}
		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (CEPEngineCluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + "");
				cluster.getAttributes().put("size", sizeAndStatus[0] + "");
			}
		}
		List<CEPEngineCluster> clusters = new ArrayList<CEPEngineCluster>();
		if (criteria.getAttributes().get("status") != null) {
			for (CEPEngineCluster temp : tempClusters) {
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
	 * @param clusterId
	 * @return
	 */
	public static CEPEngineCluster getCluster(String clusterId) {
		return clusterManager.get(clusterId, CEPEngineCluster.class);
	}

}