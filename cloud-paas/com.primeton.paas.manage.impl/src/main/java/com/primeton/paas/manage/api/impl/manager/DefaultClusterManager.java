/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.gocom.cloud.cesium.common.spi.dao.DefaultBaseDao;
import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.cesium.manage.cluster.api.ClusterManager;
import org.gocom.cloud.cesium.manage.cluster.api.exception.ClusterManagerException;
import org.gocom.cloud.cesium.manage.service.api.ServiceInstanceManager;
import org.gocom.cloud.cesium.model.api.Cluster;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.util.CesiumFactory;
import com.primeton.paas.manage.api.impl.util.ObjectUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.AbstractClusterManager;
import com.primeton.paas.manage.api.manager.IServiceManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 默认集群管理器实现. <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultClusterManager extends AbstractClusterManager {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultClusterManager.class);
	
	// Cesium
	private static ClusterManager clusterManager = CesiumFactory.getClusterManager();
	private static ServiceInstanceManager instanceManager = CesiumFactory.getServiceInstanceManager();
	
	// PaaS
	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();
	
	/**
	 * Default constructor <br>
	 */
	public DefaultClusterManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#getType()
	 */
	public String getType() {
		return DEFAULT_TYPE;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#create(com.primeton.paas.manage.api.model.ICluster)
	 */
	public ICluster create(ICluster cluster) throws ClusterException {
		if (cluster == null) {
			return null;
		}
		if (StringUtil.isEmpty(cluster.getId())) {
			String id = getNewId();
			cluster.setId(id);
		}
		ICluster target = get(cluster.getId());
		if (target != null) {
			throw new ClusterException(ClusterException.CLUSTER_EXISTS + target.toString());
		}
		try {
			getClusterManager().saveCluster(ObjectUtil.toCesium(cluster));
		} catch (Throwable t) {
			throw new ClusterException("Create cluster [" + cluster.toString() + "] error, error message:", t);
		}
		return get(cluster.getId());
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#destory(java.lang.String)
	 */
	public void destroy(String id) throws ClusterException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		try {
			ICluster cluster = get(id);
			if (cluster == null) {
				return;
			}
			IServiceManager manager = ServiceManagerFactory.getManager(cluster.getType());
			IService[] instances = getServiceQuery().getByCluster(id);
			
			long begin = System.currentTimeMillis();
			logger.info("Begin destory cluster { " + cluster.toString() + " }.");
			// Destory all instance
			if (instances != null && instances.length > 0) {
				for (IService inst : instances) {
					if (inst != null) {
						try {
							manager.destroy(inst.getId());
							logger.info("Destory instance [id:" + inst.getId() + ", cluster:" + id + "] success.");
						} catch (ServiceException e) {
							logger.error(e);
						}
					}
				}
			}
			// Destory cluster
			getClusterManager().deleteCluster(id);
			long end = System.currentTimeMillis();
			logger.info("Finish destory cluster {" + cluster.toString() + "}, Spent " + (end-begin)/1000L + " seconds.");
		} catch (Throwable t) {
			throw new ClusterException("Destory cluster [id:" + id + "] error.", t);
		}

	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#update(com.primeton.paas.manage.api.model.ICluster)
	 */
	public void update(ICluster cluster) throws ClusterException {
		if (cluster == null)
			return;
		if (StringUtil.isEmpty(cluster.getId())) {
			throw new ClusterException("Cluster id is null or blank, "
					+ cluster.toString());
		}
		ICluster c = get(cluster.getId());
		if (c == null) {
			throw new ClusterException(ClusterException.CLUSTER_NOT_EXISTS + "{" + cluster.getId() + "}");
		}
		c.getAttributes().putAll(cluster.getAttributes());
		c.setDesc(cluster.getDesc());
		c.setMaxSize(cluster.getMaxSize());
		c.setMinSize(cluster.getMinSize());
		c.setName(cluster.getName());
		c.setOwner(cluster.getOwner());

		try {
			getClusterManager().saveCluster(ObjectUtil.toCesium(c));
		} catch (Throwable t) {
			throw new ClusterException(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#get(java.lang.String)
	 */
	public ICluster get(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			Cluster cluster = getClusterManager().getCluster(id);
			return ObjectUtil.toPAAS(cluster);
		} catch (Throwable t) {
			logger.error(t.getMessage());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#getAll(com.primeton.paas.manage.api.model.IPageCond)
	 */
	public ICluster[] getByApp(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return new ICluster[0];
		}
		List<ICluster> list = new ArrayList<ICluster>();
		try {
			List<Cluster> clusters = getClusterManager().getClustersByAppName(appName);
			if(clusters != null && clusters.size() > 0) {
				for (Cluster cluster : clusters) {
					if(cluster != null) {
						list.add(ObjectUtil.toPAAS(cluster));
					}
				}
			}
		} catch (Throwable t) {
			logger.error(t.getMessage());
		}
		return list.toArray(new ICluster[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#getByApp(java.lang.String, java.lang.String)
	 */
	public ICluster getByApp(String appName, String type) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(type)) {
			return null;
		}
		ICluster[] clusters = getByApp(appName);
		if (clusters != null && clusters.length > 0) {
			for (ICluster cluster : clusters) {
				if (type.equals(cluster.getType())) {
					return cluster;
				}
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#getByApp(java.lang.String, java.lang.String)
	 */
	public ICluster[] getByType(String appName, String type) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(type)) {
			return null;
		}
		List<ICluster> list = new ArrayList<ICluster>();
		ICluster[] clusters = getByApp(appName);
		if (clusters != null && clusters.length > 0) {
			for (ICluster cluster : clusters) {
				if (type.equals(cluster.getType())) {
					list.add(cluster);
				}
			}
		}
		return list.toArray(new ICluster[list.size()]);
	}


	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#getByType(java.lang.String, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public ICluster[] getByType(String type, IPageCond pageCond) {
		if (StringUtil.isEmpty(type)) {
			return new ICluster[0];
		}
		List<ICluster> list = new ArrayList<ICluster>();
		try {
			List<Cluster> clusters = getClusterManager().getClustersByServiceDefName(type);
			if (clusters != null && clusters.size() > 0) {
				if (pageCond == null) {
					for (Cluster cluster : clusters) {
						if (cluster != null) {
							list.add(ObjectUtil.toPAAS(cluster));
						}
					}
				} else {
					pageCond.setCount(clusters.size());
					int end = pageCond.getBegin() + pageCond.getLength() - 1;
					if (pageCond.getBegin() + pageCond.getLength() > pageCond
							.getCount()) {
						end = pageCond.getCount() - 1;
					}
					for (int i=pageCond.getBegin(); i<=end; i++) {
						list.add(ObjectUtil.toPAAS(clusters.get(i)));
					}
				}
			}
		} catch (Throwable t) {
			logger.debug(t.getMessage());
		}
		return list.toArray(new ICluster[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#getByOwner(java.lang.String, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public ICluster[] getByOwner(String owner, IPageCond pageCond) {
		if (StringUtil.isEmpty(owner)) {
			return new ICluster[0];
		}
		List<ICluster> list = new ArrayList<ICluster>();
		try {
			List<Cluster> clusters = getClusterManager().getClustersByOwner(owner);
			if (clusters != null) {
				if (pageCond == null) {
					for (Cluster cluster : clusters) {
						if (cluster != null) {
							list.add(ObjectUtil.toPAAS(cluster));
						}
					}
				} else {
					pageCond.setCount(clusters.size());
					int end = pageCond.getBegin() + pageCond.getLength() - 1;
					if (pageCond.getBegin() + pageCond.getLength() > pageCond
							.getCount()) {
						end = pageCond.getCount() - 1;
					}
					for (int i=pageCond.getBegin(); i<=end; i++) {
						list.add(ObjectUtil.toPAAS(clusters.get(i)));
					}
				}
			}
		} catch (Throwable t) {
			logger.debug(t.getMessage());
		}
		return list.toArray(new ICluster[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#getByOwner(java.lang.String, java.lang.String, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public ICluster[] getByOwner(String owner, String type, IPageCond pageCond) {
		if (StringUtil.isEmpty(owner)) {
			return getByType(type, pageCond);
		}
		if (StringUtil.isEmpty(type)) {
			return getByOwner(owner, pageCond);
		}
		ICluster[] clusters = getByOwner(owner, null);
		
		List<ICluster> list = new ArrayList<ICluster>();
		if (clusters != null) {
			for (ICluster cluster : clusters) {
				if (cluster != null && type.equals(cluster.getType())) {
					list.add(cluster);
				}
			}
		}
		if (pageCond != null) {
			pageCond.setCount(list.size());
			if (pageCond.getBegin() >= list.size()) {
				return new ICluster[0];
			} else {
				List<ICluster> _return = new ArrayList<ICluster>();
				int end = pageCond.getBegin() + pageCond.getLength() - 1;
				if (pageCond.getBegin() + pageCond.getLength() > pageCond
						.getCount()) {
					end = pageCond.getCount() - 1;
				}
				for (int i = pageCond.getBegin(); i <= end; i++) {
					_return.add(list.get(i));
				}
				return _return.toArray(new ICluster[_return.size()]);
			}
		}
		return list.toArray(new ICluster[list.size()]); // no page
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		IService[] instances = getServiceQuery().getByCluster(id);
		if (null == instances || instances.length == 0) {
			return;
		}
		String type = instances[0].getType();
		IServiceManager manager = ServiceManagerFactory.getManager(type);
		if (manager == null) {
			logger.info("IServiceManager for type [" + type + "] implementation class not found.");
			try {
				getInstanceManager().startCluster(id, SystemVariables.getMaxWaitMessageTime());
				return;
			} catch (Throwable t) {
				throw new ServiceException(t);
			}
		}
		
		for (IService service : instances) {
			manager.start(service.getId());
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#stop(java.lang.String)
	 */
	public void stop(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		IService[] instances = getServiceQuery().getByCluster(id);
		if (null == instances || instances.length == 0) {
			return;
		}
		String type = instances[0].getType();
		IServiceManager manager = ServiceManagerFactory.getManager(type);
		if (manager == null) {
			logger.info("IServiceManager for type [" + type + "] implementation class not found.");
			try {
				getInstanceManager().stopCluster(id, SystemVariables.getMaxWaitMessageTime());
				return;
			} catch (Throwable t) {
				throw new ServiceException(t);
			}
		}
		
		for (IService service : instances) {
			manager.stop(service.getId());
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#restart(java.lang.String)
	 */
	public void restart(String id) throws ServiceException {
		stop(id);
		start(id);
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getNewId() {
		try {
			long id = DefaultBaseDao.getInstance().getNewId("CLD_CLUSTER"); //$NON-NLS-1$
			return String.valueOf(id);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return UUID.randomUUID().toString().toUpperCase();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#get(java.lang.String, java.lang.Class)
	 */
	public <T extends ICluster> T get(String id, Class<T> clazz) {
		if (StringUtil.isEmpty(id) || clazz == null) {
			return null;
		}
		return clazz.cast(get(id));
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#getByType(java.lang.String, com.primeton.paas.manage.api.model.IPageCond, java.lang.Class)
	 */
	public <T extends ICluster> List<T> getByType(String type, IPageCond pageCond, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (StringUtil.isEmpty(type) || clazz == null) {
			return list;
		}
		ICluster[] clusters = getByType(type, pageCond);
		if (clusters != null && clusters.length > 0) {
			for (ICluster cluster : clusters) {
				if (cluster != null) {
					list.add(clazz.cast(cluster));
				}
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#getByOwner(java.lang.String, java.lang.String, com.primeton.paas.manage.api.model.IPageCond, java.lang.Class)
	 */
	public <T extends ICluster> List<T> getByOwner(String owner, String type, IPageCond pageCond, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (StringUtil.isEmpty(type) || clazz == null) {
			return list;
		}
		ICluster[] clusters = getByOwner(owner, type, pageCond);
		if (clusters != null && clusters.length > 0) {
			for (ICluster cluster : clusters) {
				if (cluster != null) {
					list.add(clazz.cast(cluster));
				}
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IClusterManager#create(com.primeton.paas.manage.api.model.ICluster, java.lang.Class)
	 */
	public <T extends ICluster> T create(T cluster, Class<T> clazz)
			throws ClusterException {
		if (cluster == null || clazz == null) {
			return null;
		}
		return clazz.cast(create(cluster));
	}


	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#bindCluster(java.lang.String, java.lang.String)
	 */
	public void bindCluster(String upperCluster, String lowerCluster) throws ClusterException {
		if (StringUtil.isEmpty(upperCluster)
				|| StringUtil.isEmpty(lowerCluster)) {
			return;
		}
		ICluster upper = get(upperCluster);
		ICluster lower = get(lowerCluster);
		if(upper == null || lower == null) {
			return;
		}
		try {
			getClusterManager().createClusterRelation(upperCluster, lowerCluster);
		} catch (ClusterManagerException e) {
			throw new ClusterException(e);
		}
	}


	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#unbindCluster(java.lang.String, java.lang.String)
	 */
	public void unbindCluster(String upperCluster, String lowerCluster) throws ClusterException {
		if (StringUtil.isEmpty(upperCluster)
				|| StringUtil.isEmpty(lowerCluster)) {
			return;
		}
		try {
			getClusterManager().deleteClusterRelation(upperCluster, lowerCluster);
		} catch (ClusterManagerException e) {
			throw new ClusterException(e);
		}
	}


	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#getClusterId(java.lang.String)
	 */
	public String getClusterId(String serviceId) {
		if (StringUtil.isEmpty(serviceId)) {
			return null;
		}
		try {
			return getClusterManager().getClusteNameByServiceInstId(Long.parseLong(serviceId));
		} catch (Throwable t) {
			logger.error(t);
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#getRelationClustersId(java.lang.String)
	 */
	public String[] getRelationClustersId(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			List<String> list = getClusterManager().getRelClusterNames(id);
			if (list != null && list.size() > 0) {
				return list.toArray(new String[list.size()]);
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#getRelationClustersId(java.lang.String, java.lang.String)
	 */
	//@Override
	public String[] getRelationClustersId(String id, String type) {
		if(StringUtil.isEmpty(id) || StringUtil.isEmpty(type)) {
			return null;
		}
		String[] ids = getRelationClustersId(id);
		if(ids == null || ids.length == 0) {
			return null;
		}
		List<String> rs = new ArrayList<String>();
		for (String i : ids) {
			ICluster cluster = get(i);
			if (cluster != null && type.equals(cluster.getType())) {
				rs.add(i);
			}
		}
		return rs.toArray(new String[rs.size()]);
	}


	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#isExistsRelation(java.lang.String, java.lang.String)
	 */
	public boolean isExistsRelation(String clusterOne, String clusterTwo) {
		if (StringUtil.isEmpty(clusterOne) || StringUtil.isEmpty(clusterTwo)) {
			return false;
		}
		try {
			return getClusterManager().clusterRelExist(clusterOne, clusterTwo);
		} catch (Throwable t) {
			logger.error(t);
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#getByType(java.lang.String)
	 */
	public ICluster[] getByType(String type) {
		if (StringUtil.isEmpty(type)) {
			return new ICluster[0];
		}
		List<ICluster> list = new ArrayList<ICluster>();
		try {
			List<Cluster> clusters = getClusterManager().getClustersByServiceDefName(type);
			if (clusters != null && clusters.size() > 0) {
				for (Cluster cluster : clusters) {
					if (cluster != null) {
						list.add(ObjectUtil.toPAAS(cluster));
					}
				}
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return list.toArray(new ICluster[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#getByType(java.lang.String, java.lang.Class)
	 */
	public <T extends ICluster> List<T> getByType(String type, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (StringUtil.isEmpty(type) || clazz == null) {
			return list;
		}
		ICluster[] clusters = getByType(type);
		if (clusters != null && clusters.length > 0) {
			for (ICluster cluster : clusters) {
				list.add(clazz.cast(cluster));
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#increase(java.lang.String, int)
	 */
	public <T extends IService> T[] increase(String clusterId, int num) throws ServiceException {
		logger.warn("Nothing to do, You should define your implements for service increase action.");
		return null;
	}


	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IClusterManager#decrease(java.lang.String, int)
	 */
	public void decrease(String clusterId, int number) throws ServiceException {
		logger.warn("Nothing to do, You should define your implements for service decrease action.");
	}

	/**
	 * 
	 * @return
	 */
	protected ClusterManager getClusterManager() {
		return clusterManager = (null == clusterManager) ? CesiumFactory.getClusterManager() : clusterManager;
	}

	/**
	 * 
	 * @return
	 */
	protected ServiceInstanceManager getInstanceManager() {
		return instanceManager = (null == instanceManager) ? CesiumFactory.getServiceInstanceManager() : instanceManager;
	}

	/**
	 * 
	 * @return
	 */
	protected IServiceQuery getServiceQuery() {
		return serviceQuery = (null == serviceQuery) ? ServiceManagerFactory.getServiceQuery() : serviceQuery;
	}

}
