/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.CardBinCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.CardBinService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 卡Bin集群管理类
 *
 * @author liuyi(liu-yi@primeton.com)
 */
@Deprecated
public class CardBinClusterMgrUtil {

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static IServiceQuery serviceQuery = ServiceManagerFactory
			.getServiceQuery();

	/**
	 * 查询卡Bin服务集群
	 * 
	 * @param criteria 查询条件（类型、所有者）
	 * @param page 分页条件
	 * @return 集群列表
	 * @return
	 */
	@Deprecated
	public static List<CardBinCluster> getCardBinClusters(
			CardBinCluster criteria, PageCond page) {
		List<CardBinCluster> tempClusters = new ArrayList<CardBinCluster>();
		List<CardBinCluster> returnClusters = new ArrayList<CardBinCluster>();

		List<CardBinCluster> allCardBinClusters = clusterManager.getByType(
				CardBinCluster.TYPE, CardBinCluster.class);

		for (CardBinCluster c : allCardBinClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}
		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (CardBinCluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + ""); //$NON-NLS-1$
				cluster.getAttributes().put("size", sizeAndStatus[0] + ""); //$NON-NLS-1$
			}
		}
		List<CardBinCluster> clusters = new ArrayList<CardBinCluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (CardBinCluster temp : tempClusters) {
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
	@Deprecated
	public static CardBinCluster getClusterDetail(String clusterId) {
		CardBinCluster cluster = clusterManager.get(clusterId,
				CardBinCluster.class);
		List<CardBinService> instList = serviceQuery.getByCluster(clusterId,
				CardBinService.class);
		if (instList == null || instList.isEmpty()) {
			return cluster;
		}
		CardBinService inst = instList.get(0);
		if (inst != null && inst.getAttributes() != null) {
			cluster.getAttributes().putAll(inst.getAttributes());
		}
		return cluster;
	}

}
