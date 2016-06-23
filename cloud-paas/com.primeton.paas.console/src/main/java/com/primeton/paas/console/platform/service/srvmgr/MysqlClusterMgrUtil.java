/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.manage.api.cluster.MySQLCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.MySQLService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Mysql服务管理类（查询）
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class MysqlClusterMgrUtil {

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();

	/**
	 * 查询MySQL服务集群
	 * 
	 * @param criteria
	 *            查询条件（类型、所有者）
	 * @param page
	 *            分页条件
	 * @return 集群列表
	 */
	public static List<MySQLCluster> getMysqlClusters(MySQLCluster criteria,
			PageCond page) {
		List<MySQLCluster> tempClusters = new ArrayList<MySQLCluster>();
		List<MySQLCluster> returnClusters = new ArrayList<MySQLCluster>();

		List<MySQLCluster> allMySQLClusters = clusterManager.getByType(
				MySQLCluster.TYPE, MySQLCluster.class);

		for (MySQLCluster c : allMySQLClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}
		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (MySQLCluster cluster : tempClusters) {
				int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(cluster.getId());
				cluster.getAttributes().put("status", sizeAndStatus[1] + "");
				cluster.getAttributes().put("size", sizeAndStatus[0] + "");
			}
		}
		List<MySQLCluster> clusters = new ArrayList<MySQLCluster>();
		if (criteria.getAttributes().get("status") != null) {
			for (MySQLCluster temp : tempClusters) {
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
	 * 获取MySql集群
	 * 
	 * @param clusterId
	 * @return
	 */
	public static MySQLCluster getCluster(String clusterId) {
		MySQLCluster clt = clusterManager.get(clusterId, MySQLCluster.class);

		List<MySQLService> instList = serviceQuery.getByCluster(clusterId,
				MySQLService.class);
		if (instList != null && !instList.isEmpty() && instList.get(0) != null) {
			MySQLService instance = instList.get(0);
			clt.getAttributes().putAll(instance.getAttributes());
		}

		return clt;
	}

}
