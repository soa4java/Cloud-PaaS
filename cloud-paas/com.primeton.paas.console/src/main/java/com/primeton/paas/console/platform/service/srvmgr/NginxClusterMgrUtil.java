/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.KeepalivedCluster;
import com.primeton.paas.manage.api.cluster.NginxCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.KeepalivedService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Nginx服务集群管理类
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class NginxClusterMgrUtil {

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();

	/**
	 * 查询Nginx集群列表
	 * 
	 * @param criteria
	 *            查询条件（类型、所有者）
	 * @param page
	 *            分页条件
	 * @return 集群列表
	 */
	public static List<NginxCluster> getNginxClusters(NginxCluster criteria,
			PageCond page) {
		List<NginxCluster> tempClusters = new ArrayList<NginxCluster>();
		List<NginxCluster> returnClusters = new ArrayList<NginxCluster>();

		List<NginxCluster> allNginxClusters = clusterManager.getByType(
				NginxCluster.TYPE, NginxCluster.class);

		for (NginxCluster c : allNginxClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}
		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (NginxCluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + ""); //$NON-NLS-1$
				cluster.getAttributes().put("size", sizeAndStatus[0] + ""); //$NON-NLS-1$
			}
		}
		List<NginxCluster> clusters = new ArrayList<NginxCluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (NginxCluster temp : tempClusters) {
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
			clusters.get(i).getAttributes()
					.put("vip", getRelKeepalivedVip(clusters.get(i).getId())); //$NON-NLS-1$
			returnClusters.add(clusters.get(i));
		}

		return returnClusters;
	}

	/**
	 * 获取nginx集群信息<br>
	 * 
	 * @param clusterId
	 *            集群标识
	 * @return
	 */
	public static NginxCluster getCluster(String clusterId) {
		NginxCluster c = clusterManager.get(clusterId, NginxCluster.class);
		return c;
	}

	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	private static String getRelKeepalivedVip(String clusterId) {
		String vip = "";
		String[] clusterIds = clusterManager.getRelationClustersId(clusterId,
				KeepalivedCluster.TYPE);
		if (clusterIds != null && clusterIds.length >= 1) {
			List<KeepalivedService> srvs = serviceQuery.getByCluster(
					clusterIds[0], KeepalivedService.class);
			if (!srvs.isEmpty()) {
				vip = srvs.get(0).getVirtualIpAddress();
			}
		}
		return vip;
	}

}