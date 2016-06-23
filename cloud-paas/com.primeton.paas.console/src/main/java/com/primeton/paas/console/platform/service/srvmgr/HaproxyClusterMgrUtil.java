/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.cluster.NginxCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.HaProxyService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Haproxy集群管理类(查询)
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class HaproxyClusterMgrUtil {

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static IWebAppManager appManager = WebAppManagerFactory.getManager();

	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();

	/**
	 * 查询haproxy服务集群
	 * 
	 * @param criteria
	 *            查询条件（类型、所有者）
	 * @param page
	 *            分页条件
	 * @return 集群列表
	 */
	public static List<HaProxyCluster> getHaproxyClusters(
			HaProxyCluster criteria, PageCond page) {
		List<HaProxyCluster> tempClusters = new ArrayList<HaProxyCluster>();
		List<HaProxyCluster> returnClusters = new ArrayList<HaProxyCluster>();
		List<HaProxyCluster> allHaProxyClusters = clusterManager.getByType(
				HaProxyCluster.TYPE, HaProxyCluster.class);

		for (HaProxyCluster c : allHaProxyClusters) {
			if (StringUtil.contain(c.getOwner(), criteria.getOwner())
					&& StringUtil.contain(c.getName(), criteria.getName())
					&& StringUtil.contain(c.getId(), criteria.getId())) {
				tempClusters.add(c);
			}
		}

		if (tempClusters != null && !tempClusters.isEmpty()) {
			for (HaProxyCluster cluster : tempClusters) {
				int state = ClusterMangerUtil.getClusterState(cluster.getId());
				cluster.getAttributes().put("status", state + ""); //$NON-NLS-1$
			}
		}
		List<HaProxyCluster> clusters = new ArrayList<HaProxyCluster>();
		if (criteria.getAttributes().get("status") != null) { //$NON-NLS-1$
			for (HaProxyCluster temp : tempClusters) {
				if (temp.getAttributes()
						.get("status").equals(criteria.getAttributes().get("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
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
			returnClusters.add(getCluster(clusters.get(i).getId()));
		}
		return returnClusters;
	}

	/**
	 * 获取Haproxy可关联的服务类型
	 * 
	 * @return
	 */
	public static MyService[] getRelServiceTypes() {
		List<MyService> list = new ArrayList<MyService>();

		String types = SystemVariable.getHaproxyRelSrvType();
		if (StringUtil.isEmpty(types)) {
			return new MyService[0];
		}
		String[] typeArr = types.split(",");
		if (typeArr != null && typeArr.length > 0) {
			for (String t : typeArr) {

				MyService _srv = new MyService(t);
				list.add(_srv);
			}
		}
		return list.toArray(new MyService[list.size()]);
	}

	/**
	 * 根据服务类型，查询Haproxy可关联的集群
	 * 
	 * @param type
	 * @return
	 */
	public static ICluster[] getAllRelServiceByType(String type) {
		if (StringUtil.isEmpty(type)) {
			return new ICluster[0];
		}
		ICluster[] clusters = clusterManager.getByType(type);
		return clusters == null || clusters.length < 1 ? new ICluster[0]
				: clusters;
	}

	/**
	 * 获取Haproxy可关联的nginx服务
	 * 
	 * @return
	 */
	public static List<NginxCluster> getAllNginxClusters() {
		List<NginxCluster> nginxList = clusterManager.getByType(
				NginxCluster.TYPE, NginxCluster.class);

		List<NginxCluster> retList = new ArrayList<NginxCluster>();
		if (nginxList != null && !nginxList.isEmpty()) {
			for (NginxCluster clt : nginxList) {
				if (clt != null
						&& clt.getName().equals(
								NginxCluster.DEFAULT_CLUSTER_NAME)) {
					retList.add(clt);
				}
			}
		}

		return retList;
	}

	/**
	 * 获取haproxy集群信息<br>
	 * 
	 * @param clusterId
	 *            集群标识
	 * @return
	 */
	public static HaProxyCluster getCluster(String clusterId) {
		HaProxyCluster clt = clusterManager
				.get(clusterId, HaProxyCluster.class);

		int state = ClusterMangerUtil.getClusterState(clt.getId());
		clt.getAttributes().put("status", state + "");

		List<HaProxyService> instList = serviceQuery.getByCluster(clusterId,
				HaProxyService.class);
		if (instList == null || instList.isEmpty() || instList.get(0) == null) {
			return clt;
		}

		// access url
		HaProxyService inst = instList.get(0);
		String ip = inst.getIp();
		int port = inst.getPort();

		clt.getAttributes().put("url", "http://" + ip + ":" + port);

		// instance size
		clt.getAttributes().put("size", instList.size() + "");

		// members info
		if (StringUtil.isEmpty(clt.getMembers())) {
			// 已关联服务
			String[] relCltIds = clusterManager
					.getRelationClustersId(clusterId);
			StringBuffer relBuffer = new StringBuffer();
			if (relCltIds != null && relCltIds.length > 0) {
				for (int i = 0; i < relCltIds.length; i++) {
					String id = relCltIds[i];

					IService[] srvs = serviceQuery.getByCluster(id);
					if (srvs != null && srvs.length > 0) {
						for (IService _srv : srvs) {
							relBuffer.append(_srv.getIp() + ":"
									+ _srv.getPort() + " ;");
						}
					}
				}

				if (relBuffer.length() > 1) {
					relBuffer.deleteCharAt(relBuffer.length() - 1);
				}
			}
			clt.getAttributes().put("members", relBuffer.toString());

		} else {
			clt.getAttributes().put("members", clt.getMembers());
		}

		// bound applicaiton name
		if (clt != null) {
			String[] appNames = appManager.getRelationApp(clusterId);
			StringBuffer namesBuffer = new StringBuffer();

			if (appNames != null) {
				for (String appName : appNames) {
					namesBuffer.append(appName + ",");
				}

				if (namesBuffer.length() > 1) {
					namesBuffer.deleteCharAt(namesBuffer.length() - 1);
				}

				clt.getAttributes().put("apps", namesBuffer.toString());

			} else {
				clt.getAttributes().put("apps", "");
			}
		}

		clt.getAttributes().put("balance", inst.getBalance());
		clt.getAttributes().put("healthUrl", inst.getHealthUrl());
		clt.getAttributes().put("connTimeout",
				String.valueOf(inst.getConnTimeout()));
		clt.getAttributes().put("maxConnectionSize",
				String.valueOf(inst.getMaxConnectionSize()));
		clt.getAttributes().put("sessionTimeout",
				String.valueOf(inst.getSessionTimeout()));
		clt.getAttributes().put("urlHealthCheckInterval",
				String.valueOf(inst.getUrlHealthCheckInterval()));
		clt.getAttributes().put("protocal", inst.getProtocal());
		return clt;
	}

}