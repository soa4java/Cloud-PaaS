/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.EsbCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * FIXME 不建议这么写.
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class EsbClusterMgrUtil {
	
	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager();

	// private static IServiceQuery serviceQuery = ServiceManagerFactory
	// .getServiceQuery();

	/**
	 * Default. <br>
	 */
	private EsbClusterMgrUtil() {
		super();
	}

	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 */
	public static List<EsbCluster> getClusters(EsbCluster criteria,
			PageCond page) {
		List<EsbCluster> tempClusters = new ArrayList<EsbCluster>();
		List<EsbCluster> returnClusters = new ArrayList<EsbCluster>();
		List<EsbCluster> allClusters = clusterManager.getByType(
				EsbCluster.TYPE, EsbCluster.class);

		for (EsbCluster c : allClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}

		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (EsbCluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + ""); //$NON-NLS-1$
				cluster.getAttributes().put("size", sizeAndStatus[0] + ""); //$NON-NLS-1$
			}
		}
		List<EsbCluster> clusters = new ArrayList<EsbCluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (EsbCluster temp : tempClusters) {
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
	 * @param clusterId
	 * @return
	 */
	public static EsbCluster getCluster(String clusterId) {
		return clusterManager.get(clusterId, EsbCluster.class);
	}

}
