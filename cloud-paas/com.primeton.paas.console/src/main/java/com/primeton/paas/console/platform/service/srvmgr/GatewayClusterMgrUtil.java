/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.GatewayCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.GatewayService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * FIXME 不建议这么写.
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class GatewayClusterMgrUtil {
	
	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager();

	private static IServiceQuery serviceQuery = ServiceManagerFactory
			.getServiceQuery();

	private GatewayClusterMgrUtil() {
		super();
	}

	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 */
	public static List<GatewayCluster> getGatewayClusters(
			GatewayCluster criteria, PageCond page) {
		List<GatewayCluster> list = new ArrayList<GatewayCluster>();
		List<GatewayCluster> result = new ArrayList<GatewayCluster>();
		List<GatewayCluster> all = clusterManager.getByType(
				GatewayCluster.TYPE, GatewayCluster.class);

		for (GatewayCluster cluster : all) {
			if (StringUtil.contain(cluster.getOwner(), criteria.getOwner())
					&& StringUtil
							.contain(cluster.getName(), criteria.getName())
					&& StringUtil.contain(cluster.getId(), criteria.getId())) {
				list.add(cluster);
			}
		}

		if (list != null && !list.isEmpty()) {
			for (GatewayCluster cluster : list) {
				int[] sizeAndStatus = ClusterMangerUtil
						.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + ""); //$NON-NLS-1$
				cluster.getAttributes().put("size", sizeAndStatus[0] + ""); //$NON-NLS-1$
			}
		}
		List<GatewayCluster> clusters = new ArrayList<GatewayCluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (GatewayCluster cluster : list) {
				if (cluster.getAttributes().get("status") //$NON-NLS-1$
						.equals(criteria.getAttributes().get("status"))) { //$NON-NLS-1$
					clusters.add(cluster);
				}
			}
		} else {
			clusters.addAll(list);
		}

		page.setCount(clusters.size());
		int end = page.getBegin() + page.getLength() - 1;
		if (page.getBegin() + page.getLength() > page.getCount()) {
			end = page.getCount() - 1;
		}

		for (int i = page.getBegin(); i <= end; i++) {
			result.add(clusters.get(i));
		}
		return result;
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public static GatewayCluster getCluster(String clusterId) {
		GatewayCluster cluster = clusterManager.get(clusterId,
				GatewayCluster.class);
		List<GatewayService> instList = serviceQuery.getByCluster(clusterId,
				GatewayService.class);
		if (instList != null && !instList.isEmpty() && instList.get(0) != null) {
			GatewayService instance = instList.get(0);
			cluster.getAttributes().putAll(instance.getAttributes());
		}
		return cluster;
	}
	
}
