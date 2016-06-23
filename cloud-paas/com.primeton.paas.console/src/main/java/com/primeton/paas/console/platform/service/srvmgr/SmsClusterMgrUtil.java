/**
 *
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.SmsCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author liuyi(liu-yi@primeton.com)
 *
 */
@Deprecated
public class SmsClusterMgrUtil {

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	/**
	 * 查询短信服务集群
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 */
	public static List<SmsCluster> getSmsClusters(SmsCluster criteria,
			PageCond page) {
		List<SmsCluster> tempClusters = new ArrayList<SmsCluster>();
		List<SmsCluster> returnClusters = new ArrayList<SmsCluster>();

		List<SmsCluster> allSmsClusters = clusterManager.getByType(
				SmsCluster.TYPE, SmsCluster.class);

		for (SmsCluster c : allSmsClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}
		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (SmsCluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + ""); //$NON-NLS-1$
				cluster.getAttributes().put("size", sizeAndStatus[0] + ""); //$NON-NLS-1$
			}
		}
		List<SmsCluster> clusters = new ArrayList<SmsCluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (SmsCluster temp : tempClusters) {
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
	public static SmsCluster getCluster(String clusterId) {
		SmsCluster c = clusterManager.get(clusterId, SmsCluster.class);
		return c;
	}

}
