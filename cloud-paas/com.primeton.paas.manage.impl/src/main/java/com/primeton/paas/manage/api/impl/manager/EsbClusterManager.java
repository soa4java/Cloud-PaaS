/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.manage.api.impl.manager;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.cluster.EsbCluster;
import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IServiceManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class EsbClusterManager extends DefaultClusterManager {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(EsbClusterManager.class);
	
	public static final String TYPE = EsbCluster.TYPE;

	/**
	 * Default. <br>
	 */
	public EsbClusterManager() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultClusterManager#getType()
	 */
	public String getType() {
		return TYPE;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultClusterManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		IService[] instances = getServiceQuery().getByCluster(id);
		if (null == instances || instances.length == 0) {
			super.start(id);
			return;
		}
		for (IService service : instances) {
			try {
				IServiceManager manager = ServiceManagerFactory.getManager(service.getType());
				manager.start(service.getId());
			} catch (Throwable t) {
				logger.error("Start {0} service {1} error.", new Object[] { service.getType(), service.getId() }, t);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultClusterManager#stop(java.lang.String)
	 */
	public void stop(String id) throws ServiceException {
		IService[] instances = getServiceQuery().getByCluster(id);
		if (null == instances || instances.length == 0) {
			super.stop(id);
			return;
		}
		for (IService service : instances) {
			try {
				IServiceManager manager = ServiceManagerFactory.getManager(service.getType());
				manager.stop(service.getId());
			} catch (Throwable t) {
				logger.error("Shut down {0} service {1} error.", new Object[] { service.getType(), service.getId() }, t);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultClusterManager#destroy(java.lang.String)
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
			// 获取集群的所有服务实例
			// ESB 集群中含有多种类型的服务实例
			// 每种服务的销毁需要调用该服务类型的管理器进行操作
			IService[] instances = getServiceQuery().getByCluster(id);
			long begin = System.currentTimeMillis();
			logger.info("Begin destory cluster {0}.", new Object[] { id });
			// Destory all instance
			if (instances != null && instances.length > 0) {
				for (IService service : instances) {
					if (service != null) {
						try {
							IServiceManager manager = ServiceManagerFactory.getManager(service.getType());
							manager.destroy(service.getId());
							logger.info("Destory service {0} @ {1} success.", new Object[] { service.getId(), id });
						} catch (ServiceException e) {
							logger.error(e);
						}
					}
				}
			}
			// Destory cluster
			getClusterManager().deleteCluster(id);
			long end = System.currentTimeMillis();
			logger.info("Finish destory cluster {0}, spent {1} ms.", new Object[] { id, end-begin });
		} catch (Throwable t) {
			throw new ClusterException("Destory cluster [" + id + "] error.", t);
		}
	}

}
