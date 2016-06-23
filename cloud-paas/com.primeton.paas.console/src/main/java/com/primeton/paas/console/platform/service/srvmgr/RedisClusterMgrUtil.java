/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.RedisCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.RedisService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * FIXME 不建议这么写.
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RedisClusterMgrUtil {
	
	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static IServiceQuery serviceQuery = ServiceManagerFactory
			.getServiceQuery();

	private RedisClusterMgrUtil() {
		super();
	}

	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 */
	public static List<RedisCluster> getRedisClusters(RedisCluster criteria,
			PageCond page) {
		List<RedisCluster> tempClusters = new ArrayList<RedisCluster>();
		List<RedisCluster> returnClusters = new ArrayList<RedisCluster>();
		List<RedisCluster> allClusters = clusterManager.getByType(
				RedisCluster.TYPE, RedisCluster.class);

		for (RedisCluster c : allClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}

		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (RedisCluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + ""); //$NON-NLS-1$
				cluster.getAttributes().put("size", sizeAndStatus[0] + ""); //$NON-NLS-1$
			}
		}
		List<RedisCluster> clusters = new ArrayList<RedisCluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (RedisCluster temp : tempClusters) {
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
	public static RedisCluster getCluster(String clusterId) {
		RedisCluster cluster = clusterManager.get(clusterId, RedisCluster.class);

		List<RedisService> instList = serviceQuery.getByCluster(clusterId,
				RedisService.class);
		if (instList != null && !instList.isEmpty() && instList.get(0) != null) {
			RedisService instance = instList.get(0);
			cluster.getAttributes().putAll(instance.getAttributes());
		}
		return cluster;
	}
	
}
