/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.MailCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.MailService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MailClusterMgrUtil {
	
	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static IServiceQuery serviceQuery = ServiceManagerFactory
			.getServiceQuery();

	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 */
	public static List<MailCluster> getMailClusters(MailCluster criteria,
			PageCond page) {
		List<MailCluster> tempClusters = new ArrayList<MailCluster>();
		List<MailCluster> returnClusters = new ArrayList<MailCluster>();
		List<MailCluster> allMailClusters = clusterManager.getByType(
				MailCluster.TYPE, page, MailCluster.class);
		for (MailCluster mc : allMailClusters) {
			if (StringUtil.contain(mc.getOwner(), criteria.getOwner())
					&& StringUtil.contain(mc.getName(), criteria.getName())
					&& StringUtil.contain(mc.getId(), criteria.getId())) {
				tempClusters.add(mc);
			}
		}
		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (MailCluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + ""); //$NON-NLS-1$
				cluster.getAttributes().put("size", sizeAndStatus[0] + ""); //$NON-NLS-1$
			}
		}
		List<MailCluster> clusters = new ArrayList<MailCluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (MailCluster temp : tempClusters) {
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
	 * Query mail cluster detail info
	 * 
	 * @param clusterId
	 * @return
	 */
	public static MailCluster getClusterDetail(String clusterId) {
		MailCluster cluster = clusterManager.get(clusterId, MailCluster.class);
		List<MailService> instList = serviceQuery.getByCluster(clusterId,
				MailService.class);
		if (instList == null || instList.isEmpty()) {
			return cluster;
		}
		MailService inst = instList.get(0);
		if (inst != null && inst.getAttributes() != null) {
			cluster.getAttributes().putAll(inst.getAttributes());
		}
		return cluster;
	}

}
