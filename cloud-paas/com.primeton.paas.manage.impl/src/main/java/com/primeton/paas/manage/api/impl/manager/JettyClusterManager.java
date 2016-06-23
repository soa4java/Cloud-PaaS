/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.ServiceUtil;
import com.primeton.paas.manage.api.manager.IJettyServiceManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.MemcachedService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Jetty集群管理器. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class JettyClusterManager extends DefaultClusterManager {

	public static final String TYPE = JettyService.TYPE;

	private ILogger logger = ManageLoggerFactory.getLogger(JettyClusterManager.class);

	private static IJettyServiceManager jettyServiceManager = ServiceManagerFactory.getManager(JettyService.TYPE);
	
	private static final String SCRIPT_CONFIG = "config.sh";

	/**
	 * Default. <br>
	 */
	public JettyClusterManager() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.impl.DefaultClusterManager#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultClusterManager#increase(java.lang.String, int)
	 */
	@SuppressWarnings("unchecked")
	public <T extends IService> T[] increase(String clusterId, int num) throws ServiceException {
		if (StringUtil.isEmpty(clusterId) || num <= 0) {
			return null;
		}
		JettyCluster cluster = get(clusterId, JettyCluster.class);
		if (cluster == null) {
			return null;
		}
		List<JettyService> instances = getServiceQuery().getByCluster(clusterId, JettyService.class);
		if (instances == null || instances.isEmpty()) {
			throw new ServiceException("Increase Jetty instance error, cluster {id:" + clusterId + "} instance not found.");
		}
		final int maxSize = cluster.getMaxSize();
		if (instances.size() >= maxSize) {
			logger.warn("Not allow to increase Jetty instance, max size is " + maxSize + ", current size is " + instances.size());
			return null;
		}
		int increaseSize = maxSize - instances.size();
		increaseSize = num > increaseSize ? increaseSize : num;

		long begin = System.currentTimeMillis();
		logger.info("Begin increase Jetty instances. [clusterId=" + clusterId + ", increaseSize=" + increaseSize + "].");

		JettyService service = ServiceUtil.copy(instances.get(0));
		service.setId(null);
		service.setIp(null);
		service.setPort(-1); //$NON-NLS-1$

		List<JettyService> incInstList = new ArrayList<JettyService>();
		try {
			incInstList = getJettyServiceManager().add(service, clusterId, increaseSize);
		} catch (ServiceException e) {
			throw new ServiceException("Increase Jetty instance error. {clusterId=" + clusterId + "}:" + e);
		}
		long end = System.currentTimeMillis();
		logger.info("End increase Jetty Cluster instances. {clusterId=" + clusterId + ", increaseSize=" + increaseSize + "}. Time Spend " + (end - begin) / 1000 + " seconds.");

		// * start jetty instances
		begin = System.currentTimeMillis();
		logger.info("Begin start jetty instances {clusterId:" + clusterId + ", increaseSize=" + incInstList.size() + "}.");
		String instId = null;
		for (JettyService instance : incInstList) {
			try {
				instId = instance.getId();
				logger.info("Begin start jetty instance : {instId = " + instId + "}.");
				getJettyServiceManager().start(instId);
				logger.info("End start jetty instance : {instId = " + instId + "}.Time spend " + (System.currentTimeMillis() - begin) / 1000 + " seconds.");
			} catch (ServiceException e) {
				logger.error("Start jetty instance : {instId = " + instId + "}.", e);
			}
		}
		end = System.currentTimeMillis();
		logger.info("End start jetty instances {clusterId:" + clusterId + ", increaseSize=" + incInstList.size() + "}. Time Spend " + (end - begin) / 1000 + " seconds.");

		String[] ids = getRelHaByJetty(clusterId);
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				try {
					ClusterManagerFactory.getManager(HaProxyCluster.TYPE).restart(id);
				} catch (ServiceException e) {
					logger.error("Restart haproxy error {id=" + id + "}", e);
				}
			}
		}
		return (T[]) incInstList.toArray(new JettyService[incInstList.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultClusterManager#decrease(java.lang.String, int)
	 */
	public void decrease(String clusterId, int number) throws ServiceException {
		JettyCluster cluster = get(clusterId, JettyCluster.class);
		if (cluster == null) { // cluster not found
			throw new ServiceException("Reduce Jetty instance error, cluster {id:" + clusterId + "} not found.");
		}

		List<JettyService> instances = getServiceQuery().getByCluster(clusterId, JettyService.class);
		if (instances == null || instances.isEmpty()) { // Service instance not found
			throw new ServiceException("Reduce Jetty instance error, cluster {id:" + clusterId + "} instance not found.");
		}

		int current = instances.size();
		if (current - number < 1) {
			throw new ServiceException("Reduce Jetty instance error,cluster {id:" + clusterId + "} instance not enough,{current:" + current + ",decrease num : " + number);
		}
		List<JettyService> startInsts = new ArrayList<JettyService>();
		List<JettyService> stopInsts = new ArrayList<JettyService>();
		for (JettyService inst : instances) {
			if (inst.getState() == IService.STATE_RUNNING) {
				startInsts.add(inst);
			} else {
				stopInsts.add(inst);
			}
		}
		
		List<JettyService> destroyInsts = new ArrayList<JettyService>();
		int count = 0;
		for (JettyService js : stopInsts) {
			if (count >= number) {
				break;
			} else {
				destroyInsts.add(js);
				count++;
			}
		}
		for (JettyService js : startInsts) {
			if (count >= number) {
				break;
			} else {
				destroyInsts.add(js);
				count++;
			}
		}
		
		// destory instance
		String instId = null;
		for (JettyService inst : destroyInsts) {
			try {
				instId = inst.getId();
				long begin = System.currentTimeMillis();
				logger.info("Begin destory instances {clusterId:" + clusterId + ",instId:" + instId + "}.");
				getJettyServiceManager().destroy(inst.getId());
				long end = System.currentTimeMillis();
				logger.info("End destory jetty instances {clusterId:" + clusterId + ",instId:" + instId + "}. Time Spend " + (end - begin) / 1000 + " seconds.");
			} catch (ServiceException e) {
				if (logger.isErrorEnabled()) {
					logger.error("destory instances {clusterId:" + clusterId + ",instId:" + instId + "}.", e);
				}
			}
		}

		String[] ids = getRelHaByJetty(clusterId);
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				try {
					long begin = System.currentTimeMillis();
					logger.info("Begin restart haproxy cluster. {id :" + id + "}.");

					ClusterManagerFactory.getManager(HaProxyCluster.TYPE).restart(id);
					
					long end = System.currentTimeMillis();
					logger.info("End restart haproxy cluster. {id :" + id + "}. Time Spend " + (end - begin) / 1000 + " seconds.");
				} catch (ServiceException e) {
					if (logger.isErrorEnabled()) {
						logger.error("Restart haproxy error {id=" + id + "}", e);
					}
				}
			}
		} else {
			logger.warn("Can not find rel haproxy clusters. {jetty clusterId :" + clusterId + "} !!!!");
		}
	}
	
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	protected String[] getRelHaByJetty(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return new String[0];
		}
		ICluster[] haproxyClusters = getByType(HaProxyCluster.TYPE);
		if (haproxyClusters == null || haproxyClusters.length == 0) {
			return new String[0];
		}

		List<String> haIds = new ArrayList<String>();
		for (ICluster cluster : haproxyClusters) {
			if (null == cluster || StringUtil.isEmpty(cluster.getId())) {
				continue;
			}
			String[] relClusterIds = getRelationClustersId(cluster.getId(),
					JettyCluster.TYPE);
			if (relClusterIds == null || relClusterIds.length == 0) {
				continue;
			}
			for (String id : relClusterIds) {
				if (clusterId.equals(id)) {
					haIds.add(cluster.getId());
				}
			}
		}
		return haIds.toArray(new String[haIds.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultClusterManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		List<JettyService> instances = getServiceQuery().getByCluster(id,
				JettyService.class);
		if (instances == null || instances.isEmpty()) {
			return;
		}
		StringBuffer memcachedString = new StringBuffer();
		if (instances.get(0).isEnableSession()) {
			String[] ids = getRelationClustersId(id, MemcachedService.TYPE);
			if (ids != null && ids.length > 0) {
				String mid = ids[0];
				for (String i : ids) {
					ICluster cluster = get(i);
					if (cluster.getName().toUpperCase()
							.indexOf(OrderItem.ITEM_TYPE_SESSION.toUpperCase()) >= 0) {
						mid = cluster.getId();
						break;
					}
				}
				List<MemcachedService> services = getServiceQuery().getByCluster(
						mid, MemcachedService.class);
				if (services != null && !services.isEmpty()) {
					for (MemcachedService memcachedService : services) {
						memcachedString.append(memcachedService.getIp())
								.append(":").append(memcachedService.getPort())
								.append(",");
					}
				}
			}
		}
		Map<String, String> args = new HashMap<String, String>();
		if (memcachedString.length() > 1) {
			memcachedString.deleteCharAt(memcachedString.length() - 1);
			args.put("memcachedServers", memcachedString.toString()); //$NON-NLS-1$
		}
		// To enable or disable distribute session configuration
		for (JettyService jettyService : instances) {
			try {
				SendMessageUtil.sendMessage(jettyService, SCRIPT_CONFIG, args, true);
			} catch (MessageException e) {
				logger.error(e);
			}
		}
		super.start(id);
	}

	/**
	 * 
	 * @return
	 */
	protected static IJettyServiceManager getJettyServiceManager() {
		return jettyServiceManager = (null == jettyServiceManager) 
				? (IJettyServiceManager)ServiceManagerFactory.getManager(JettyService.TYPE) 
				: jettyServiceManager;
	}
	
}
