/**
 *
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.cluster.SVNRepositoryCluster;
import com.primeton.paas.manage.api.cluster.TomcatCluster;
import com.primeton.paas.manage.api.cluster.WarCluster;
import com.primeton.paas.manage.api.exception.DeployException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.util.ServiceUtil;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.ITomcatServiceManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.SVNRepositoryService;
import com.primeton.paas.manage.api.service.TomcatService;
import com.primeton.paas.manage.api.service.WarService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Tomcat 集群管理器. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class TomcatClusterManager extends DefaultClusterManager {

	public static final String TYPE = TomcatService.TYPE;
	
	private ILogger logger = ManageLoggerFactory.getLogger(TomcatClusterManager.class);
	
	private static ITomcatServiceManager tomcatServiceManager = ServiceManagerFactory.getManager(TomcatService.TYPE);
	
	/**
	 * Default. <br>
	 */
	public TomcatClusterManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultClusterManager#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultClusterManager#increase(java.lang.String, int)
	 */
	@SuppressWarnings("unchecked")
	public <T extends IService> T[] increase(String clusterId, int num)
			throws ServiceException {
		if (StringUtil.isEmpty(clusterId) || num <= 0) {
			return null;
		}
		TomcatCluster cluster = get(clusterId, TomcatCluster.class);
		if (cluster == null) {
			return null;
		}
		List<TomcatService> instances = getServiceQuery().getByCluster(clusterId, TomcatService.class);
		if (instances == null || instances.isEmpty()) {
			throw new ServiceException("Increase tomcat instance error, cluster {id:" + clusterId + "} instance not found.");
		}
		final int maxSize = cluster.getMaxSize();
		if (instances.size() >= maxSize) {
			logger.warn("Not allow to increase tomcat instance, max size is " + maxSize + ", current size is " + instances.size());
			return null;
		}
		
		int increaseSize = maxSize - instances.size();
		increaseSize = num > increaseSize ? increaseSize : num;
		long begin = System.currentTimeMillis();
		logger.info("Begin increase tomcat instances [cluster:" + clusterId + ", increaseSize:" + increaseSize + "].");
		
		TomcatService service = ServiceUtil.copy(instances.get(0));
		service.setId(null);
		service.setIp(null);
		service.setPort(-1); //$NON-NLS-1$
		service.setAdminPort(-1); //$NON-NLS-1$
		service.setAjpPort(-1); //$NON-NLS-1$
		service.setShutdownPort(-1); //$NON-NLS-1$
		service.setCreatedDate(new Date());
		
		List<TomcatService> newInstances = null;
		try {
			newInstances = getTomcatServiceManager().add(service, clusterId, increaseSize);
		} catch (ServiceException e) {
			throw new ServiceException("Increase tomcat service [cluster=" + clusterId + "] error.", e);
		}
		long end = System.currentTimeMillis();
		logger.info("End increase tomcat instances [cluster:" + clusterId + ", increaseSize:" + increaseSize + "]. Time spent " + (end-begin)/1000L + " seconds.");
		if (null == newInstances || newInstances.isEmpty()) {
			throw new ServiceException("Error occured when increase tomcat [cluster:" + clusterId + ", increaseSize:" + increaseSize + "] instances.");
		}
		// deploy war for new instances, if old instances has been deployed. 
		deployWar(instances.get(0), newInstances);
		
		// start tomcat instances
		for (TomcatService instance : newInstances) {
			try {
				getTomcatServiceManager().start(instance.getId());
			} catch (ServiceException e) {
				logger.error(e);
			}
		}
		
		// update HA mapping
		String[] ids = getRelHaByTomcat(clusterId);
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				try {
					ClusterManagerFactory.getManager(HaProxyCluster.TYPE).restart(id);
				} catch (ServiceException e) {
					logger.error("Restart haproxy [" + id + "] error.", e);
				}
			}
		}
		
		return (T[]) newInstances.toArray(new TomcatService[newInstances.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultClusterManager#decrease(java.lang.String, int)
	 */
	public void decrease(String clusterId, int number) throws ServiceException {
		if (StringUtil.isEmpty(clusterId) || number < 1) {
			return;
		}
		TomcatCluster cluster = get(clusterId, TomcatCluster.class);
		if (cluster == null) {
			logger.warn("Tomcat cluster {0} not exists.", new Object[] { clusterId });
			return;
		}
		List<TomcatService> instances = getServiceQuery().getByCluster(clusterId, TomcatService.class);
		if (instances == null || instances.isEmpty()) {
			throw new ServiceException(StringUtil.format("Decrease tomcat cluster {0} error, none instances."
					, new Object[] { clusterId }));
		}
		final int minSize = cluster.getMinSize();
		if (instances.size() <= minSize) {
			logger.warn("Not allow to decrease tomcat instance, min size is " + minSize + ", current size is " + instances.size());
			return;
		}
		// 计算可以销毁的实例数量
		int decreaseSize = instances.size() - minSize; // 最多可以销毁的实例数量
		decreaseSize = number > decreaseSize ? decreaseSize : number;
		if (decreaseSize < 1) {
			return;
		}
		long begin = System.currentTimeMillis();
		logger.info("Begin increase tomcat cluster {0}:{1}.", new Object[] { clusterId, decreaseSize });
		// 销毁部分实例
		for (int i=0; i<decreaseSize; i++) {
			try {
				getTomcatServiceManager().destroy(instances.get(i).getId());
			} catch (Throwable t) {
				logger.error("Destroy tomcat service {0} error.", new Object[] { instances.get(i).getId() });
			}
		}
		// 更新负载均衡
		String[] ids = getRelHaByTomcat(clusterId);
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				try {
					ClusterManagerFactory.getManager(HaProxyCluster.TYPE).restart(id);
				} catch (ServiceException e) {
					logger.error("Restart HaProxy cluster {0} error.", new Object[] { id }, e);
				}
			}
		}
		long end = System.currentTimeMillis();
		logger.info("End increase tomcat cluster {0}:{1}, spent {2} ms.", new Object[] { clusterId, decreaseSize, end - begin });
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	protected String[] getRelHaByTomcat(String clusterId) {
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
					TomcatCluster.TYPE);
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
	
	/**
	 * 
	 * @param service
	 * @param newServices
	 */
	protected void deployWar(TomcatService service, List<TomcatService> newServices) {
		if (null == service || null == newServices || newServices.isEmpty()) {
			return;
		}
		String appName = service.getAppName();
		IClusterManager svnRepoClusterManager = ClusterManagerFactory.getManager(SVNRepositoryCluster.TYPE);
		IClusterManager warClusterManager = ClusterManagerFactory.getManager(WarCluster.TYPE);
		ICluster svnRepoCluster = svnRepoClusterManager.getByApp(appName, SVNRepositoryCluster.TYPE);
		if (null == svnRepoCluster) {
			return;
		}
		List<SVNRepositoryService> svnRepoServices = getServiceQuery().getByCluster(
				svnRepoCluster.getId(), SVNRepositoryService.class);
		if (null == svnRepoServices || svnRepoServices.isEmpty()) {
			return;
		}
		SVNRepositoryService svnRepoService = svnRepoServices.get(0);
		String svnRepoId = svnRepoService.getId();
		ICluster warCluster = warClusterManager.getByApp(appName, WarService.TYPE);
		String warclusterId = warCluster == null ? null : warCluster.getId();
		if (StringUtil.isEmpty(warclusterId)) {
			return;
		}
		List<WarService> warServices = getServiceQuery().getByCluster(warclusterId,
				WarService.class);
		if (null == warServices || warServices.isEmpty()) {
			return;
		}
		String warId = null;
		for (WarService war : warServices) {
			if (war.isDeployVersion()) {
				warId = war.getId();
				break;
			}
		}
		if (StringUtil.isEmpty(warId)) {
			return;
		}
		List<DeployWarTask> deployWarTasks = new ArrayList<DeployWarTask>();
		for (TomcatService instance : newServices) {
			try {
				// getTomcatServiceManager().deploy(instance.getId(), warId, svnRepoId);
				DeployWarTask task = DeployWarTask.deploy(instance.getId(), warId, svnRepoId);
				deployWarTasks.add(task);
			} catch (Throwable e) {
				logger.error(e.getMessage());
			}
		}
		long begin = System.currentTimeMillis();
		long timeout = 1000L * 60 * 10;
		while (true) {
			int count = 0;
			long end = System.currentTimeMillis();
			if (end - begin > timeout) {
				break;
			}
			for (DeployWarTask task : deployWarTasks) {
				if (task.isFinish()) {
					count ++;
				} else {
					if (task.getException() != null) {
						count ++;
						logger.error("Deploy tomcat war error, " + task.getException());
					}
				}
			}
			if (count == deployWarTasks.size()) {
				break;
			}
			ThreadUtil.sleep(1000L);
		}
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	private static class DeployWarTask implements Runnable {
		
		private static ILogger log = ManageLoggerFactory.getLogger(DeployWarTask.class);
		
		private String tomcatId;
		private String warId;
		private String svnRepoId;
		
		private boolean isFinish;
		private Exception exception;
		
		/**
		 * 
		 * @param tomcatId
		 * @param warId
		 * @param svnRepoId
		 */
		public DeployWarTask(String tomcatId, String warId, String svnRepoId) {
			super();
			this.tomcatId = tomcatId;
			this.warId = warId;
			this.svnRepoId = svnRepoId;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				getTomcatServiceManager().deploy(tomcatId, warId, svnRepoId);
			} catch (DeployException e) {
				log.error(e.getMessage());
				exception = e;
			}
			isFinish = true;
		}
		
		protected boolean isFinish() {
			return isFinish;
		}

		protected Exception getException() {
			return exception;
		}

		/**
		 * 
		 * @param tomcatId
		 * @param warId
		 * @param svnRepoId
		 */
		public static DeployWarTask deploy(String tomcatId, String warId, String svnRepoId) {
			if (StringUtil.isEmpty(tomcatId) || StringUtil.isEmpty(warId) 
					|| StringUtil.isEmpty(svnRepoId)) {
				return null;
			}
			DeployWarTask task = new DeployWarTask(tomcatId, warId, svnRepoId);
			Thread t = new Thread(task);
			t.setName("Deploy_Tomcat_War_Task"); //$NON-NLS-1$
			t.setDaemon(true);
			t.start();
			return task;
		}
		
	}

	/**
	 * 
	 * @return
	 */
	protected static ITomcatServiceManager getTomcatServiceManager() {
		return tomcatServiceManager = (null == tomcatServiceManager) 
				? (ITomcatServiceManager)ServiceManagerFactory.getManager(TomcatService.TYPE) 
				: tomcatServiceManager;
	}
	
}
