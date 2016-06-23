/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.manage.service.api.ServiceInstanceManager;
import org.gocom.cloud.cesium.model.api.ServiceInstance;
import org.gocom.cloud.cesium.mqclient.api.CommandResultMessage;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.exception.StorageException;
import com.primeton.paas.manage.api.factory.HostAssignManagerFactory;
import com.primeton.paas.manage.api.factory.HostManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.RuntimeManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.util.CesiumFactory;
import com.primeton.paas.manage.api.impl.util.ObjectUtil;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IHostAssignManager;
import com.primeton.paas.manage.api.manager.IHostManager;
import com.primeton.paas.manage.api.manager.IRuntimeManager;
import com.primeton.paas.manage.api.manager.IServiceManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.factory.StorageManagerFactory;
import com.primeton.paas.manage.spi.model.Storage;
import com.primeton.paas.manage.spi.resource.IStorageManager;

/**
 * 服务管理器默认实现. <br>
 *  
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultServiceManager implements IServiceManager {
	
	public static final String TYPE = "Default";
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultServiceManager.class);
	
	private static ServiceInstanceManager instanceManager = CesiumFactory.getServiceInstanceManager();
	
	private static IHostManager hostManager = HostManagerFactory.getHostManager();
	
	private static IHostAssignManager hostAssignManager = HostAssignManagerFactory.getManager();
	
	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();
	
	private static IStorageManager storageManager = StorageManagerFactory.getManager();
	
	private static IRuntimeManager runtimeManager =  RuntimeManagerFactory.getManager();
	
	/**
	 * Service : start.sh
	 */
	public static final String SH_START = "start.sh";
	
	/**
	 * Service : stop.sh
	 */
	public static final String SH_STOP = "stop.sh";
	
	/**
	 * Common : download.sh <br>
	 */
	public static final String SH_DOWNLOAD = "download.sh";
	
	/**
	 * Service : install.sh <br>
	 */
	public static final String SH_INSTALL = "install.sh";
	
	/**
	 * Service : uninstall.sh <br>
	 */
	public static final String SH_UNINSTALL = "uninstall.sh";
	
	/**
	 * Service : create.sh <br>
	 */
	public static final String SH_CREATE = "create.sh";
	
	/**
	 * Service : destroy.sh <br>
	 */
	public static final String SH_DESTROY = "destroy.sh";
	
	/**
	 * Default. <br>
	 */
	public DefaultServiceManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#add(com.primeton.paas.manage.api.model.IService, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T extends IService> T add(T service, String clusterId) throws ServiceException {
		if (service == null || StringUtil.isEmpty(clusterId)) {
			return null;
		}
		if (IService.MODE_PHYSICAL.equals(service.getMode()) && StringUtil.isEmpty(service.getIp())) {
			throw new ServiceException("Can not add service " + service + ", ip is null or blank.");
		}
		try {
			ServiceInstance instance = getInstanceManager().saveServiceInst(clusterId, ObjectUtil.toCesium(service));
			return (T)ObjectUtil.toPAAS(instance);
		} catch (Throwable t) {
			throw new ServiceException(t);
		}
	}
	
	/**
	 * 
	 * @param service
	 * @param clusterId
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends IService> T create(T service, String clusterId) throws ServiceException {
		if (service == null || StringUtil.isEmpty(clusterId)) {
			return null;
		}
		if (IService.MODE_PHYSICAL.equals(service.getMode()) && StringUtil.isEmpty(service.getIp())) {
			String ip = null;
			try {
				String[] ips = getHostAssignManager().apply(
						service.getPackageId(), service.getType(),
						service.isStandalone(), 1,
						getHostAssignManager().getInstallTimeout(getType()));
				if (ips != null && ips.length > 0) {
					ip = ips[0];
				}
			} catch (Throwable t) {
				throw new ServiceException(t);
			}
			service.setIp(ip);
		}
		try {
			ServiceInstance instance = getInstanceManager().saveServiceInst(clusterId, ObjectUtil.toCesium(service));
			return (T) ObjectUtil.toPAAS(instance);
		} catch (Throwable t) {
			throw new ServiceException(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#update(com.primeton.paas.manage.api.model.IService, java.lang.String)
	 */
	public <T extends IService> void update(T service, String clusterId)
			throws ServiceException {
		if (service == null || StringUtil.isEmpty(clusterId)) {
			return;
		}
		try {
			getInstanceManager().saveServiceInst(clusterId, ObjectUtil.toCesium(service));
		} catch (Throwable t) {
			throw new ServiceException(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#destory(java.lang.String)
	 */
	public void destroy(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		IService service = getServiceQuery().get(id);
		if(service == null) {
			return;
		}
		String ip = service.getIp();
		// instance status
		if(service.getState() == IService.STATE_RUNNING) {
			try {
				stop(id);
			} catch (ServiceException e) {
				if(logger.isErrorEnabled()) {
					logger.error(e);
				}
			}
		}
		// delete instance
		try {
			getInstanceManager().deleteServiceInst(Long.parseLong(id));
			if(logger.isInfoEnabled()) {
				logger.info("Delete service instance [id:" + id + "] success.");
			}
		} catch (Throwable t) {
			throw new ServiceException(t.getMessage());
		}
		
		IService[] instants = getServiceQuery().getByIp(ip);
		Host host  = getHostManager().get(ip);
		if ((instants == null || instants.length < 1) && host != null && host.isStandalone()) {
			//	release storage
			List<Storage> storages = getStorageManager().getByHost(ip);
			if (storages != null && ! storages.isEmpty()) {
				try {
					logger.info("Release action : unmount storage at host {0}.", new Object[] { ip });
					getStorageManager().unmount(ip);
					getStorageManager().removeWhiteListsByIp(ip);
					
					for (Storage ss : storages) {
						List<String> wl = getStorageManager().getWhiteLists(ss.getId());
						if (wl == null || wl.size() == 0) {
							getStorageManager().release(ss.getId());
						}
					}
				} catch (StorageException e) {
					if(logger.isErrorEnabled()) {
						logger.error("Release action : release storage at host {0} error.", new Object[] { ip }, e);
					}
				}
			}
			
			//release host
			try {
				getHostManager().release(ip);
				logger.info("Release host {0} success.", new Object[] {ip});
			} catch (HostException e) {
				logger.error("Release host {0} error.", new Object[] {ip}, e);
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		IService service = getServiceQuery().get(id);
		if (service == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Service instance [" + id + "] not exists, can not execute start action.");
			}
			return;
		}
		try {
			CommandResultMessage message = getInstanceManager().startServiceInst(Long.parseLong(id), SystemVariables.getMaxWaitMessageTime());
			if (message != null && message.getBody() != null && message.getBody().getSuccess()) {
				logger.info("Start service instance [" + service.getType() + ":" + id + "] success.");
				return;
			} else if(message != null && message.getBody() != null) {
				logger.debug(message.getBody().toString());
			}
		} catch (Throwable t) {
			logger.info("Start service instance [" + service.getType() + ":" + id + "] error.");
			throw new ServiceException(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#stop(java.lang.String)
	 */
	public void stop(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		IService service = getServiceQuery().get(id);
		if(service == null) {
			if(logger.isDebugEnabled()) {
				logger.debug("Service instance [" + id + "] not exists, can not execute stop action.");
			}
			return;
		}
		try {
			CommandResultMessage message = getInstanceManager().stopServiceInst(Long.parseLong(id), SystemVariables.getMaxWaitMessageTime());
			if (message != null && message.getBody() != null && message.getBody().getSuccess()) {
				logger.info("Stop service instance [" + service.getType() + ":" + id + "] success.");
				return;
			} else if(message != null && message.getBody() != null) {
				if (logger.isDebugEnabled()) {
					logger.debug(message.getBody().toString());
				}
			}
		} catch (Throwable t) {
			logger.info("Start service instance [" + service.getType() + ":" + id + "] error.");
			throw new ServiceException(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#restart(java.lang.String)
	 */
	public void restart(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		/*
		try {
			getInstanceManager().restartServiceInst(Long.parseLong(id));
		} catch (Throwable t) {
			throw new ServiceException(t);
		}
		*/
		stop(id);
		start(id);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#install(java.lang.String, long)
	 */
	public void install(String ip, long timeout) throws ServiceException {
		if (StringUtil.isEmpty(ip)) {
			return;
		}
		timeout = timeout > 0 ? timeout : 100000L;
		
		Host host = getHostManager().get(ip);
		if (host == null) {
			throw new ServiceException("Host [" + ip + "] not found.");
		} else if (host.getTypes().contains(getType())) {
			throw new ServiceException("Host [" + ip + "] is already installed service [" + getType() + "].");
		}
		
		// download service package
		Map<String, String> args = new HashMap<String, String>();
		args.put("url", SystemVariables.getHttpRpository()); //$NON-NLS-1$
		args.put("name", getType()); //$NON-NLS-1$
		String download_sh = SystemVariables.getBinHome() + "/Common/bin/" + SH_DOWNLOAD; //$NON-NLS-1$
		
		long begin = System.currentTimeMillis();
		try {
			CommandResultMessage downloadResult = SendMessageUtil.sendMessage(ip, download_sh, args, true, timeout);
			if (downloadResult == null) {
				throw new ServiceException("Not receiving a response message to the NodeAgent [" + ip + "].");
			} else if (downloadResult.getBody() == null) {
				throw new ServiceException("The contents of the message is incomplete, missing message body.");
			} else if (!downloadResult.getBody().getSuccess()) {
				if (logger.isErrorEnabled()) {
					logger.error("Error out is :\n" + downloadResult.getBody().toString());
				}
				throw new ServiceException("Host [" + ip + "] download service [" + getType() + "] install package from [" + SystemVariables.getHttpRpository() + "] failured.");
			}
			// SUCCESS
			long end = System.currentTimeMillis();
			if (logger.isInfoEnabled()) {
				logger.info("Host [" + ip + "] download service [" + getType() + "] install package from [" + SystemVariables.getHttpRpository() + "] success. Time spents " + (end-begin)/1000L + " seconds.");
			}
		} catch (MessageException e) {
			throw new ServiceException(e);
		}
		
		begin = System.currentTimeMillis();
		// install service package
		String install_sh = SystemVariables.getScriptPath(getType(), SH_INSTALL);
		try {
			CommandResultMessage installResult = SendMessageUtil.sendMessage(ip, install_sh, new HashMap<String, String>(), true, timeout);
			if (installResult == null) {
				throw new ServiceException("Not receiving a response message to the NodeAgent [" + ip + "].");
			} else if (installResult.getBody() == null) {
				throw new ServiceException("The contents of the message is incomplete, missing message body.");
			} else if (!installResult.getBody().getSuccess()) {
				if (logger.isErrorEnabled()) {
					logger.error("Error out is :\n" + installResult.getBody().toString());
				}
				throw new ServiceException("Host [" + ip + "] install service [" + getType() + "] failured.");
			}
			// SUCCESS
			long end = System.currentTimeMillis();
			if (logger.isInfoEnabled()) {
				logger.info("Host [" + ip + "] install service [" + getType() + "] success. Time spents " + (end-begin)/1000L + " seconds.");
			}
		} catch (MessageException e) {
			throw new ServiceException("Host [" + ip + "] install service [" + getType() + "] error.", e);
		}
		
		// UPDATE HOST TYPE
		host = getHostManager().get(ip);
		if (host != null) {
			host.addType(getType());
			try {
				getHostManager().update(host);
			} catch (HostException e) {
				if (logger.isErrorEnabled()) {
					logger.error(e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#uninstall(java.lang.String, long)
	 */
	public void uninstall(String ip, long timeout) throws ServiceException {
		if (StringUtil.isEmpty(ip)) {
			return;
		}
		timeout = timeout > 0 ? timeout : 100000L;
		
		Host host = getHostManager().get(ip);
		if (host == null) {
			return;
		} else if (!host.getTypes().contains(getType())) {
			throw new ServiceException("Host [" + ip + "] service [" + getType() + "] has not been installed.");
		}
		
		host.removeType(getType());
		String uninstall_sh = SystemVariables.getScriptPath(getType(), SH_UNINSTALL);
		try {
			getHostManager().update(host);
			SendMessageUtil.sendMessage(ip, uninstall_sh, new HashMap<String, String>(), false, timeout);
			logger.info("Host [" + ip + "] uninstall service [" + getType() + "] success.");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	protected static ServiceInstanceManager getInstanceManager() {
		return instanceManager = (null == instanceManager)
				? CesiumFactory.getServiceInstanceManager()
				: instanceManager;
	}

	/**
	 * 
	 * @return
	 */
	protected static IHostManager getHostManager() {
		return hostManager = (null == hostManager)
				? HostManagerFactory.getHostManager()
				: hostManager;
	}

	/**
	 * 
	 * @return
	 */
	protected static IHostAssignManager getHostAssignManager() {
		return hostAssignManager = (null == hostAssignManager)
				? HostAssignManagerFactory.getManager()
				: hostAssignManager;
	}

	/**
	 * 
	 * @return
	 */
	protected static IServiceQuery getServiceQuery() {
		return serviceQuery = (null == serviceQuery)
				? ServiceManagerFactory.getServiceQuery()
				: serviceQuery;
	}

	/**
	 * 
	 * @return
	 */
	protected static IStorageManager getStorageManager() {
		return storageManager = (null == storageManager)
				? StorageManagerFactory.getManager()
				: storageManager;
	}

	/**
	 * 
	 * @return
	 */
	protected static IRuntimeManager getRuntimeManager() {
		return runtimeManager = (null == runtimeManager)
				? RuntimeManagerFactory.getManager()
				: runtimeManager;
	}


}
