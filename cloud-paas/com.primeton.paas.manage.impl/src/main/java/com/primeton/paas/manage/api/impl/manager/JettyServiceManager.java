/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.mqclient.api.CommandResultMessage;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.DeployException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.exception.StorageException;
import com.primeton.paas.manage.api.exception.UndeployException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.ServiceUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IJettyServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.SVNRepositoryService;
import com.primeton.paas.manage.api.service.SVNService;
import com.primeton.paas.manage.api.service.WarService;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.model.Storage;

/**
 * Jetty服务管理器. <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class JettyServiceManager extends DefaultServiceManager 
		implements IJettyServiceManager {
	
	public static final String TYPE = JettyService.TYPE;
	
	private static final String SCRIPT_CREATE = "create.sh";
	private static final String SCRIPT_DESTROY = "destroy.sh";
	private static final String SCRIPT_DEPLOY = "deploy.sh";
	private static final String SCRIPT_UNDEPLOY = "undeploy.sh";
	
	public static final String WAR_VERSION = "warVersion";
	
	private static ILogger logger = ManageLoggerFactory.getLogger(JettyServiceManager.class);
	
	/**
	 * Default. <br>
	 */
	public JettyServiceManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#add(com.primeton.paas.manage.api.model.IService, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T extends IService> T add(T service, String clusterId)	throws ServiceException {
		List<JettyService> list = add((JettyService) service, clusterId, 1);
		if (list != null && list.size() > 0) {
			JettyService js = list.get(0);
			return (T) js;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IJettyServiceManager#add(com.primeton.paas.manage.api.service.JettyService, java.lang.String, int)
	 */
	public List<JettyService> add(JettyService service, String clusterId, int number) throws ServiceException {
		List<JettyService> services = new ArrayList<JettyService>();
		if (service == null || StringUtil.isEmpty(clusterId) || number < 1) {
			return null;
		}
		service.setHaMode(IService.HA_MODE_CLUSTER);
		
		String[] ips = null;
		try {
			ips = getHostAssignManager().apply(service.getPackageId(), service.getType(), service.isStandalone(), 
					number, getHostAssignManager().getInstallTimeout(getType()));
		} catch (Throwable t) {
			logger.error("Assign host resource error.", t);
		}
		if (ips == null) {
			logger.error("Assign host resource error, not enough host resource.");
			throw new ServiceException("Not enough host resource for apply. [" + number + "]");
		}
		logger.info("Requires [" + number + "] hosts, already applied [" + ips.length + "] hosts, still need [" + (number - ips.length) + "] hosts.");
		
		long begin = System.currentTimeMillis();
		long end = begin;
		
		// #########################################################################################################################################
		// If enable iaas platform webservice
		// assign storage and mount it to vm
		if (SystemVariables.isIaasEnableStorage()) {
			Storage storage = null;
			String storageId = null;
			if (StringUtil.isEmpty(service.getStorageId())) {
				logger.info("Begin apply Jetty shared storage. [clusterId: " + clusterId + ", instId: "+ service.getId() + ", storagesSize : " + service.getStorageSize() +"], WhiteList [" + ips.toString() + "].");
				List<String> whiteList = Arrays.asList(ips);
				try {
					storage = getStorageManager().apply(service.getStorageSize(), whiteList);
				} catch (StorageException e) {
					logger.error("Apply shared storage failed.", e);
				}
				if (storage == null) {
					logger.error("Apply shared storage is null.");
					throw new ServiceException("Apply shared storage is null.");
				}
				storageId = storage.getId();
				service.setStorageId(storageId);
				
				end = System.currentTimeMillis();
				logger.info("End apply Jetty shared storage. [instId: " + service.getId()+", storageId: " + storageId + "], WhiteList [" + ips.toString() + "]. Time spent " + (end - begin)/1000L + " seconds.");
				
			} else {
				storageId = service.getStorageId();
				logger.info("Jetty have been applied shared storage. [instId: " + service.getId()+", storageId: " + storageId + "].");
				try {
					begin = System.currentTimeMillis();
					logger.info("Begin update white list .[jettyInstId: " + service.getId() + ", storageId: " + storageId + "].");
					getStorageManager().addWhiteLists(storageId, ips);
					end = System.currentTimeMillis();
					logger.info("End update white list .[jettyInstId: " + service.getId()+", storageId: " + storageId + "], Time spent " + (end - begin)/1000L + " seconds.");
				} catch (StorageException e1) {
					logger.error("Update white list failed. ", e1);
					throw new ServiceException(e1);
				}
			}
			
			begin = System.currentTimeMillis();
			logger.info("Begin mount Jetty shared storage. [ips:"+ ips.toString() + ", storageId: " + storageId + "]");
			String ip = null;
			for (int i = 0; i < ips.length; i++) {
				try {
					ip = ips[i];
					getStorageManager().mount(ip, storageId, service.getStoragePath());
					logger.info("Mount shared storage [" + ip + "] success.");
				} catch (StorageException e) {
					logger.error("Mount shared storage [" + ip + "] failed.", e);
				}
			}
			end = System.currentTimeMillis();
			logger.info("End mount Jetty shared storage. [ips:"+ ips.toString() + ", storageId: " + storageId+ "], Time spent " + (end - begin)/1000L + " seconds.");
		}
		// #########################################################################################################################################
		
		
		for (int i=0; i<number; i++) {
			JettyService instance = ServiceUtil.copy(service);
			instance.setIp(ips[i]);
			services.add(super.add(instance, clusterId));
		}
		
		List<CreateJettyTask> tasks = new ArrayList<CreateJettyTask>();
		List<CreateJettyTask> errorTasks = new ArrayList<CreateJettyTask>();
		for (JettyService instance : services) {
			CreateJettyTask thread = new CreateJettyTask(instance);
			Thread t = new Thread(thread);
			t.setDaemon(true);
			t.start();
			tasks.add(thread);
		}
		
		boolean isFinish = false;
		boolean isOk = false;
		long timeout = SystemVariables.getMaxWaitMessageTime() * number;
		begin = System.currentTimeMillis();
		ServiceException exception = null;
		while (true) {
			isFinish = true;
			isOk = true;
			
			if (System.currentTimeMillis() - begin > timeout) {
				break;
			}
			for (CreateJettyTask t : tasks) {
				if (t.isFinish() && t.getException() != null) {
					isOk = false;
					errorTasks.add(t);
					continue;
				}
				if (!t.isFinish()) {
					isFinish = false;
					break;
				}
			}
			if (isFinish) {
				break;
			}
			ThreadUtil.sleep(1000L);
		}
		
		if (isFinish && isOk) {
			return services;
		}
		
		if (errorTasks.size() > 0) {
			for (CreateJettyTask task : errorTasks) {
				exception = task.getException();
				JettyService instance = task.getService();
				logger.error("Create Jetty service fail. [id:"+ instance.getId() + "].", exception);
				begin = System.currentTimeMillis();
				logger.info("Begin release fail Jetty service resource. [id:"+ instance.getId() + ", ip: " + instance.getIp()+ ", storageId: " + instance.getStorageId()+ "].");
				try {
					super.destroy(instance.getId());
				} catch (Throwable t) {
					logger.error(t);
				}
				end = System.currentTimeMillis();
				logger.info("End release fail Jetty service resource. [id:"+ instance.getId() + ", ip: " + instance.getIp()+ ", storageId: " + instance.getStorageId()+ "], Time spent " + (end - begin)/1000L + " seconds.");
				services.remove(instance);
			}
		}
		return services;
	}
	
	/**
	 * 
	 * @param instance
	 * @throws ServiceException
	 */
	private void createContainer(JettyService instance) throws ServiceException {
		try {
			boolean isRunning = getRuntimeManager().agentIsOnline(instance.getIp());
			if (!isRunning) {
				throw new ServiceException("NodeAgent [" + instance.getIp() + " is not running.");
			}
			long begin = System.currentTimeMillis();
			logger.info("Begin create Jetty container [ip:" + instance.getIp() + ", port:" + instance.getPort() + "].");
			Map<String, String> args = new HashMap<String, String>();
			args.put("instId", instance.getId()); //$NON-NLS-1$
			CommandResultMessage message = SendMessageUtil.sendMessage(instance, SCRIPT_CREATE, args, true);
			if (message != null && message.getBody() != null && message.getBody().getSuccess()) {
				logger.info("End create Jetty container [ip:" + instance.getIp() + ", port:" + instance.getPort() + "]. Time spent " + (System.currentTimeMillis() - begin)/1000L + " seconds.");
			} else {
				String info = (message == null || message.getBody() == null) ? "" : message.getBody().toString(); 
				throw new ServiceException("Create Jetty container [ip:" + instance.getIp() + ", port:" + instance.getPort() + "] failured." + info);
			}
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
		JettyService jettyService = getServiceQuery().get(id, JettyService.class);
		if (jettyService == null) {
			return;
		} else if (jettyService.getState() == IService.STATE_RUNNING) {
			try {
				stop(id);
			} catch (ServiceException e) {
				logger.error(e);
			}
		}
		
		String ip = jettyService.getIp();
		int port = jettyService.getPort();
		
		try {
			long begin = System.currentTimeMillis();
			logger.info("Begin destroy Jetty container [ip:" + ip + ", port:" + port + "].");
			Map<String, String> otherArgs = new HashMap<String, String>();
			otherArgs.put("instId", jettyService.getId()); //$NON-NLS-1$
			CommandResultMessage message = SendMessageUtil.sendMessage(jettyService, SCRIPT_DESTROY, otherArgs, true);
			if (message != null && message.getBody() != null && message.getBody().getSuccess()) {
				logger.info("Finish destroy Jetty container [ip:" + ip + ", port:" + port + "] successed. Spent " + (System.currentTimeMillis() - begin)/1000L + " seconds.");
			} else if (message != null && message.getBody() != null) {
				logger.error("Finish destroy Jetty container [ip:" + ip + ", port:" + port + "] failed. ErrOut: \n" + message.getBody().getErrOut());
			} else {
				logger.error("Should't be here, CommandResultMessage is null.");
			}
		} catch (Throwable t) {
			logger.error(t.getMessage());
		}
		super.destroy(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IJettyServiceManager#deploy(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void deploy(String jettyId, String warId,String svnRepoId) throws DeployException {
		if (StringUtil.isEmpty(jettyId) || StringUtil.isEmpty(warId)
				|| StringUtil.isEmpty(svnRepoId)) {
			return;
		}
		try {
			JettyService jettyService = getServiceQuery().get(jettyId, JettyService.class);
			if (jettyService == null) {
				return;
			}

			WarService warService = getServiceQuery().get(warId, WarService.class);
			if (warService == null) {
				throw new DeployException("WarService instance [id:" + warId + "] not found. Can not deploy war.");
			}
			
			SVNRepositoryService svnRepoService = getServiceQuery().get(svnRepoId, SVNRepositoryService.class);
			if (svnRepoService == null) {
				throw new DeployException("SVNRepositoryService instance [id:" + svnRepoId + "] not found. Can not deploy war.");
			}
			
			// Get SVNService (by svnRepoService parentId)
			String svnServiceId = svnRepoService.getParentId();
			SVNService svnService = getServiceQuery().get(svnServiceId,SVNService.class);
			if (svnService == null || StringUtil.isEmpty(svnService.getId())) {
				throw new DeployException("Service instance [id:" + svnServiceId + "] not found. Can not deploy war.");
			}
			
			String appName = jettyService.getAppName();
			String storagePath = jettyService.getStoragePath();
			
			String revision = warService.getRevision();
			// String repoFile = warService.getRepoFile();
			String warName = warService.getRepoFile();
			warName = StringUtil.isEmpty(warName) ? appName + ".war" : warName; //$NON-NLS-1$

			String svnUrl = "http://" + svnService.getIp() + ":" //$NON-NLS-1$ //$NON-NLS-2$
					+ svnService.getPort()
					+ "/" + svnService.getRepoRoot() + "/" + appName + "/war/"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$	
			String svnUser = svnRepoService.getUser();
			String svnPassword = svnRepoService.getPassword();
			
			Map<String, String> args = new HashMap<String, String>();
		    args.put("appName", appName); //$NON-NLS-1$
		    args.put("storagePath", storagePath); //$NON-NLS-1$
			args.put("warName", warName); //$NON-NLS-1$ 
		    args.put("revision", revision); //$NON-NLS-1$
		    args.put("svnUrl", svnUrl); //$NON-NLS-1$
		    args.put("svnUser", svnUser); //$NON-NLS-1$
		    args.put("svnPassword", svnPassword); //$NON-NLS-1$
		    // map.put("repoFile", repoFile); //$NON-NLS-1$
			
			String warVersion = warService.getWarVersion();
			
			long begin = System.currentTimeMillis();
			logger.info("Begin deploy war [service:" + jettyService.getId() + ", ip:" + jettyService.getIp() + "], war [version:" + warVersion + "].");
			
			CommandResultMessage message = SendMessageUtil.sendMessage(jettyService, SCRIPT_DEPLOY, args, true);
			long end = System.currentTimeMillis();
			
			if (message == null) {
				logger.info("Deploy war [service:" + jettyService.getId() + ", ip:" + jettyService.getIp() + "], war [version:" + warVersion + "]. Can not receive agent response, timeout or agent has died.");
			} else {
				if (message.getBody() != null) {
					if (message.getBody().getSuccess()) {
						logger.info("Finish deploy war [service:" + jettyService.getId() + ", ip:" + jettyService.getIp() + "], war [version:" + warVersion + "]. Spent " + (end-begin)/1000L + " seconds." +
								"Result Message body:" + message.getBody().toString());
					} else {
						logger.info("Deploy war [service:" + jettyService.getId() + ", ip:" + jettyService.getIp() + "], war [version:" + warVersion + "] failed." +
								"Result Message body:" + message.getBody().toString());
					}
				} else{
					logger.info("Deploy war [service:" + jettyService.getId() + ", ip:" + jettyService.getIp() + "], war [version:" + warVersion + "]. Received message body is null.");
				}
			}
		} catch (Throwable t) {
			throw new DeployException(t);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IJettyServiceManager#undeploy(java.lang.String)
	 */
	public void undeploy(String id) throws UndeployException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		try {
			JettyService service = getServiceQuery().get(id, JettyService.class);
			if (service == null) {
				return;
			}
			long begin = System.currentTimeMillis();
			logger.info("Begin undeploy war [service:" + service.getId() + ", ip:" + service.getIp() + "]");
			CommandResultMessage message = SendMessageUtil.sendMessage(service, SCRIPT_UNDEPLOY, true);
			long end = System.currentTimeMillis();
			if (message != null && message.getBody() != null
					&& message.getBody().getSuccess()) {
				logger.info("Finish undeploy war [service:" + service.getId() + ", ip:" + service.getIp() + "]. Spent " + (end-begin)/1000L + " seconds.");
			} else if(message == null && logger.isInfoEnabled()) {
				logger.info("Undeploy war [service:" + service.getId() + ", ip:" + service.getIp() + "] failured. Can not receive agent response, timeout or agent has died.");
			} else if(message.getBody() != null && logger.isInfoEnabled()) {
				logger.info("Undeploy war [service:" + service.getId() + ", ip:" + service.getIp() + "] failured. Result message body: " + message.getBody());
			}
		} catch (Throwable t) {
			throw new UndeployException(t);
		}
	}

	/**
	 * 创建Jetty应用容器任务. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	private class CreateJettyTask implements Runnable {
		
		private JettyService service;
		private ServiceException exception;
		private boolean isFinish;

		/**
		 * @param service
		 */
		public CreateJettyTask(JettyService service) {
			super();
			this.service = service;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			isFinish = false;
			if (service == null) {
				isFinish = true;
				return;
			}
			try {
				createContainer(service);
			} catch (ServiceException e) {
				exception = e;
			}
			isFinish = true;
		}
		
		public boolean isFinish() {
			return isFinish;
		}

		public ServiceException getException() {
			return exception;
		}
		
		public JettyService getService() {
			return service;
		}
		
	}
	
}
