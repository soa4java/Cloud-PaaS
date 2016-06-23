/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.JobCtrlCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.JobCtrlService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * FIXME 不建议这么写.
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class JobCtrlClusterMgrUtil {
	
	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager();

	private static IServiceQuery serviceQuery = ServiceManagerFactory
			.getServiceQuery();

	/**
	 * Default. <br>
	 */
	private JobCtrlClusterMgrUtil() {
		super();
	}

	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 */
	public static List<JobCtrlCluster> getClusters(JobCtrlCluster criteria,
			PageCond page) {
		List<JobCtrlCluster> tempClusters = new ArrayList<JobCtrlCluster>();
		List<JobCtrlCluster> returnClusters = new ArrayList<JobCtrlCluster>();
		List<JobCtrlCluster> allClusters = clusterManager.getByType(
				JobCtrlCluster.TYPE, JobCtrlCluster.class);

		for (JobCtrlCluster c : allClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}

		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (JobCtrlCluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + ""); //$NON-NLS-1$
				cluster.getAttributes().put("size", sizeAndStatus[0] + ""); //$NON-NLS-1$
			}
		}
		List<JobCtrlCluster> clusters = new ArrayList<JobCtrlCluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (JobCtrlCluster temp : tempClusters) {
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
	public static JobCtrlCluster getCluster(String clusterId) {
		JobCtrlCluster cluster = clusterManager.get(clusterId, JobCtrlCluster.class);

		List<JobCtrlService> instList = serviceQuery.getByCluster(clusterId,
				JobCtrlService.class);
		if (instList != null && !instList.isEmpty() && instList.get(0) != null) {
			JobCtrlService instance = instList.get(0);
			cluster.getAttributes().putAll(instance.getAttributes());
		}
		return cluster;
	}

}
