/**
 * 
 */
package com.primeton.paas.console.common.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.primeton.paas.manage.api.cluster.MemcachedCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.monitor.MemcachedStatus;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.service.MemcachedService;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CacheMonitorUtil {

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static IServiceQuery serviceQueryMgr = ServiceManagerFactory
			.getServiceQuery();

	/**
	 * 
	 * @return
	 */
	public static List<MemcachedCluster> getMemcachedClusters() {
		List<MemcachedCluster> clusters = clusterManager.getByType(
				MemcachedCluster.TYPE, MemcachedCluster.class);
		return clusters;
	}

	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map getServiceMonitors(String clusterId) {
		List<MemcachedService> mService = serviceQueryMgr.getByCluster(
				clusterId, MemcachedService.class);
		List<String> ips = new ArrayList<String>();
		for (MemcachedService m : mService) {
			ips.add(m.getIp() + ":" + m.getPort());
		}
		String[] servers = ips.toArray(new String[ips.size()]);
		MemcachedStatus demo = new MemcachedStatus(servers);
		return demo.getStats();
	}

	/**
	 * 
	 * @param server
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map getServiceMonitor(String server) {
		String[] servers = { server };
		MemcachedStatus demo = new MemcachedStatus(servers);
		return demo.getStats(server);
	}

	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public static List<MemcachedService> getMemcachedServices(String clusterId) {
		List<MemcachedService> service = serviceQueryMgr.getByCluster(
				clusterId, MemcachedService.class);
		return service;
	}

	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	public static MemcachedService getMemcachedService(String serviceId) {
		MemcachedService service = serviceQueryMgr.get(serviceId,
				MemcachedService.class);
		return service;
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = hour + " hours and " + minute + " minutes";
			}
		}
		return timeStr;
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

}
