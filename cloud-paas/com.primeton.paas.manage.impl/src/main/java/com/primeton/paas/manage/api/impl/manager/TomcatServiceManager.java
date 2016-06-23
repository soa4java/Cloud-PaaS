/**
 *
 */
package com.primeton.paas.manage.api.impl.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.mqclient.api.CommandResultMessage;
import org.gocom.cloud.cesium.mqclient.api.StringMessage;
import org.gocom.cloud.cesium.mqclient.api.WriteFileMessage;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.cluster.MemcachedCluster;
import com.primeton.paas.manage.api.exception.DeployException;
import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.exception.UndeployException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.ServiceUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.impl.util.VelocityUtil;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.ITomcatServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.MemcachedService;
import com.primeton.paas.manage.api.service.SVNRepositoryService;
import com.primeton.paas.manage.api.service.SVNService;
import com.primeton.paas.manage.api.service.TomcatService;
import com.primeton.paas.manage.api.service.WarService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Tomcat服务管理器. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class TomcatServiceManager extends DefaultServiceManager 
		implements ITomcatServiceManager {
	
	public static final String TYPE = TomcatService.TYPE;
	
	/**
	 * 创建Tomcat应用容器实例. <br>
	 */
	protected static final String SCRIPT_CREATE = "create.sh";
	
	/**
	 * 销毁Tomcat应用容器实例. <br>
	 */
	protected static final String SCRIPT_DESTROY = "destroy.sh";
	
	/**
	 * 部署应用WAR. <br>
	 */
	protected static final String SCRIPT_DEPLOY = "deploy.sh";
	
	/**
	 * 卸载应用WAR. <br>
	 */
	protected static final String SCRIPT_UNDEPLOY = "undeploy.sh";
	
	private static ILogger logger = ManageLoggerFactory.getLogger(TomcatServiceManager.class);
	
	/**
	 * Default. <br>
	 */
	public TomcatServiceManager() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#add(com.primeton.paas.manage.api.model.IService, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T extends IService> T add(T service, String clusterId)
			throws ServiceException {
		List<TomcatService> services = add((TomcatService)service, clusterId, 1);
		return null == services || services.isEmpty() ? null : (T)services.get(0);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.ITomcatServiceManager#add(com.primeton.paas.manage.api.service.TomcatService, java.lang.String, int)
	 */
	public List<TomcatService> add(TomcatService service, String clusterId,
			int number) throws ServiceException {
		List<TomcatService> services = new ArrayList<TomcatService>();
		if (null == service || StringUtil.isEmpty(clusterId) || number <= 0) {
			return services;
		}
		service.setHaMode(IService.HA_MODE_CLUSTER);
		String[] ips = null;
		try {
			ips = getHostAssignManager().apply(service.getPackageId(), service.getType(), service.isStandalone(), 
					number, getHostAssignManager().getInstallTimeout(getType()));
		} catch (Throwable t) {
			logger.error("Assign host resource for package : {0}, number : {1}, type : tomcat error.", 
					new Object[] {service.getPackageId(), number}, t);
		}
		if (ips == null || ips.length == 0) {
			logger.error("Assign host resource error, not enough host resource.");
			throw new ServiceException("Not enough host resource for apply. require [" + number + "]");
		}
		if (number == ips.length) {
			logger.info("Create tomcat service required machine [" + number + "] has been applied for.");
		} else {
			try {
				getHostManager().release(ips);
			} catch (HostException e) {
				logger.error(e);
			}
			throw new ServiceException("Not enough host resource for apply, require [" + number + "], left [" + ips.length + "].");
		}
		
		long begin = System.currentTimeMillis();
		long end = begin;
		
		for (int i=0; i<number; i++) {
			TomcatService instance = ServiceUtil.copy(service);
			instance.setIp(ips[i]);
			services.add(super.add(instance, clusterId)); // 保存至数据库
		}
		
		List<CreateTomcatTask> tasks = new ArrayList<CreateTomcatTask>();
		List<CreateTomcatTask> errorTasks = new ArrayList<CreateTomcatTask>();
		for (TomcatService instance : services) {
			CreateTomcatTask task = new CreateTomcatTask(instance);
			Thread t = new Thread(task);
			t.setName("create-tomcat-" + instance.getId() + "-task"); //$NON-NLS-1$ //$NON-NLS-2$
			t.setDaemon(true);
			t.start();
			tasks.add(task);
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
			for (CreateTomcatTask t : tasks) {
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
		// ERROR
		if (!errorTasks.isEmpty()) {
			for (CreateTomcatTask task : errorTasks) {
				TomcatService instance = task.getService();
				exception = null == exception ? task.getException() : exception;
				logger.error("Create tomcat instance [" + instance.getId() + "] error.", task.getException());
				begin = System.currentTimeMillis();
				logger.info("Begin destroy error tomcat instance [" + instance.getId() + "].");
				try {
					super.destroy(instance.getId());
				} catch (Throwable t) {
					logger.error(t.getMessage());
				}
				end = System.currentTimeMillis();
				logger.info("End destroy error tomcat instance [" + instance.getId() + "]. Time spent " + (end-begin)/1000L + " seconds.");
				// clean error instance
				services.remove(instance);
			}
		}
		return services;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#destroy(java.lang.String)
	 */
	public void destroy(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		TomcatService service = getServiceQuery().get(id, TomcatService.class);
		if (null == service) {
			return;
		}
		if (service.getState() == IService.STATE_RUNNING) {
			try {
				stop(id);
			} catch (ServiceException e) {
				logger.error(e);
			}
		}
		String ip = service.getIp();
		int port = service.getPort();
		
		try {
			long begin = System.currentTimeMillis();
			logger.info("Begin destroy tomcat instance [ip:" + ip + ", port:" + port + "].");
			Map<String, String> args = new HashMap<String, String>();
			args.put("srvId", service.getId()); //$NON-NLS-1$
			CommandResultMessage message = SendMessageUtil.sendMessage(service, SCRIPT_DESTROY, args, true);
			if (message != null && message.getBody() != null
					&& message.getBody().getSuccess()) {
				logger.info("End destroy tomcat instance [ip:" + ip + ", port:" + port + "] successed. Spent " + (System.currentTimeMillis() - begin)/1000L + " seconds.");
			} else if (message != null && message.getBody() != null) {
				logger.error("Finish destroy tomcat instance [ip:" + ip + ", port:" + port + "] error. ErrOut: \n" + message.getBody().getErrOut());
			} else {
				logger.error("CommandResultMessage is null.");
			}
		} catch (Throwable t) {
			logger.error(t.getMessage());
		}
		// clean database
		super.destroy(id);
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.ITomcatServiceManager#deploy(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void deploy(String tomcatId, String warId, String svnRepoId)
			throws DeployException {
		if (StringUtil.isEmpty(tomcatId) || StringUtil.isEmpty(warId)
				|| StringUtil.isEmpty(svnRepoId)) {
			return;
		}
		try {
			TomcatService service = getServiceQuery().get(tomcatId, TomcatService.class);
			if (null == service) {
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
			
			String appName = service.getAppName();
			String revision = warService.getRevision();
			String warName = warService.getRepoFile();
			warName = StringUtil.isEmpty(warName) ? appName + ".war" : warName; //$NON-NLS-1$
			
			// http | https
			String svnUrl = "http://" + svnService.getIp() + ":" //$NON-NLS-1$ //$NON-NLS-2$
					+ svnService.getPort()
					+ "/" + svnService.getRepoRoot() + "/" + appName + "/war/"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$		
			String svnUser = svnRepoService.getUser();
			String svnPassword = svnRepoService.getPassword();
			
			Map<String, String> args = new HashMap<String, String>();
			args.put("srvId", tomcatId); //$NON-NLS-1$
			args.put("appName", appName); //$NON-NLS-1$
			args.put("warName", warName); //$NON-NLS-1$
		    args.put("revision", revision); //$NON-NLS-1$
		    args.put("svnUrl", svnUrl); //$NON-NLS-1$
		    args.put("svnUser", svnUser); //$NON-NLS-1$
		    args.put("svnPassword", svnPassword); //$NON-NLS-1$
			
			String warVersion = warService.getWarVersion();
			
			long begin = System.currentTimeMillis();
			logger.info("Begin deploy war [service:" + service.getId() + ", ip:" + service.getIp() + "], war [version:" + warVersion + "].");
			
			CommandResultMessage message = SendMessageUtil.sendMessage(service, SCRIPT_DEPLOY, args, true);
			long end = System.currentTimeMillis();
			if (message == null) {
				logger.info("Deploy war [service:" + service.getId() + ", ip:" + service.getIp() + "], war [version:" + warVersion + "]. Can not receive agent response, timeout or agent has died.");
			} else {
				if (message.getBody() != null) {
					if (message.getBody().getSuccess()) {
						logger.info("Finish deploy war [service:" + service.getId() + ", ip:" + service.getIp() + "], war [version:" + warVersion + "]. Spent " + (end-begin)/1000L + " seconds." +
								"Result Message body:" + message.getBody().toString());
					} else {
						logger.info("Deploy war [service:" + service.getId() + ", ip:" + service.getIp() + "], war [version:" + warVersion + "] error." +
								"Result Message body:" + message.getBody().toString());
					}
				} else{
					logger.info("Deploy war [service:" + service.getId() + ", ip:" + service.getIp() + "], war [version:" + warVersion + "]. Received message body is null.");
				}
			}
		} catch (Throwable t) {
			throw new DeployException(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.ITomcatServiceManager#undeploy(java.lang.String)
	 */
	public void undeploy(String id) throws UndeployException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		try {
			TomcatService service = getServiceQuery().get(id, TomcatService.class);
			if (null == service) {
				return;
			}
			long begin = System.currentTimeMillis();
			logger.info("Begin undeploy war [service:" + service.getId() + ", ip:" + service.getIp() + "]");
			Map<String, String> args = new HashMap<String, String>();
			args.put("srvId", id); //$NON-NLS-1$
			
			CommandResultMessage message = SendMessageUtil.sendMessage(service, SCRIPT_UNDEPLOY, args, true);
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
	 * 
	 * @param instance
	 * @throws ServiceException
	 */
	private void createContainer(TomcatService instance) throws ServiceException {
		if (null == instance || StringUtil.isEmpty(instance.getId())) {
			return;
		}
		try {
			boolean isRunning = getRuntimeManager().agentIsOnline(instance.getIp());
			if (!isRunning) {
				throw new ServiceException("NodeAgent [" + instance.getIp() + " is not running.");
			}
			long begin = System.currentTimeMillis();
			logger.info("Begin create tomcat instance [ip:" + instance.getIp() + ", port:" + instance.getPort() + "].");
			Map<String, String> args = new HashMap<String, String>();
			args.put("srvId", instance.getId()); //$NON-NLS-1$
			CommandResultMessage message = SendMessageUtil.sendMessage(instance, SCRIPT_CREATE, args, true);
			if (message != null && message.getBody() != null
					&& message.getBody().getSuccess()) {
				logger.info("End create tomcat instance [ip:" + instance.getIp() + ", port:" + instance.getPort() + "]. Time spent " + (System.currentTimeMillis() - begin)/1000L + " seconds.");
			} else {
				String info = (message == null || message.getBody() == null) ? "" : message.getBody().toString(); 
				throw new ServiceException("Create tomcat instance [ip:" + instance.getIp() + ", port:" + instance.getPort() + "] error. Message : " + info);
			}
		} catch (Throwable t) {
			throw new ServiceException(t);
		}
	}
	
	/**
	 * 创建Tomcat应用服务器. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	private class CreateTomcatTask implements Runnable {
		
		private TomcatService service;
		private ServiceException exception;
		private boolean isFinish;

		/**
		 * 
		 * @param service
		 */
		public CreateTomcatTask(TomcatService service) {
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

		/**
		 * 
		 * @return
		 */
		public TomcatService getService() {
			return service;
		}

		/**
		 * 
		 * @return
		 */
		public ServiceException getException() {
			return exception;
		}

		/**
		 * 
		 * @return
		 */
		public boolean isFinish() {
			return isFinish;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		TomcatService service = getServiceQuery().get(id, TomcatService.class);
		if (null == service) {
			logger.warn("Can not start Tomcat {0}, service not exists.", new Object[] { id });
			return;
		}
		beforeStart(service);
		// To start
		super.start(id);
	}

	/**
	 * @param service
	 */
	protected void beforeStart(TomcatService service) {
		if (null == service) {
			return;
		}
		// 修改Tomcat ${catlina.home}/conf/context.xml文件
		{
			String content = getContextConfig(service);
			WriteFileMessage message = new WriteFileMessage(content, SystemVariables.getProgramsHome() 
					+ "/Tomcat/" + service.getId() + "/apache-tomcat-7.0.62/conf/context.xml"); //$NON-NLS-1$
			message.setEncoding(StringUtil.CHARSET_UTF8);
			message.setNeedResponse(true);
			
			for (int i=0; i<5; i++) {
				try {
					StringMessage result = SendMessageUtil.sendMessage(message, service.getIp(), 1000L * 60 * 5); //$NON-NLS-1$
					if (null != result && result.isOk()) {
						logger.info(result.getBody());
						break;
					}
					logger.error(null == result ? StringUtil.format("No response while send modify Tomcat service {0} configuration file context.xml on host {1}.", 
							new Object[] { service.getId(), service.getIp() }) : result.getBody());
				} catch (MessageException e) {
					logger.error(e);
				}
			}
		}
		// 修改Tomcat ${catlina.home}/conf/server.xml文件
		{
			//
		}
	}

	/**
	 * @param service
	 * @return
	 */
	private String getContextConfig(TomcatService service) {
		if (null == service) {
			return null;
		}
		Map<String, Object> context = new HashMap<String, Object>();
		// 计算memcached nodes
		String template = "context-default.xml";
		if (service.isEnableSession()) {
			String nodes = getSessionMemServers(service.getId());
			if (StringUtil.isEmpty(nodes)) {
				template = "context-default.xml";
			} else {
				context.put("memcachedNodes", nodes); //$NON-NLS-1$
			}
		}
		
		InputStream in = null;
		try {
			in = TomcatServiceManager.class.getResourceAsStream("/META-INF/templates/Tomcat/" + template); //$NON-NLS-1$
			String content = VelocityUtil.parse(in, context, template); //$NON-NLS-1$
			return content;
		} catch (Throwable t) {
			logger.error(t);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return null;
	}
	
	/**
	 * 计算用作容器存储Session的Memcached服务实例. <br>
	 * 
	 * @param id tomcat实例标识
	 * @return
	 */
	private String getSessionMemServers(String id) {
		try {
			IClusterManager manager = ClusterManagerFactory.getManager();
			String cluster = manager.getClusterId(id);
			String[] clusters = manager.getRelationClustersId(cluster, MemcachedService.TYPE);
			if (null == clusters || clusters.length == 0) {
				return null;
			}
			String mem = null;
			for (String c : clusters) {
				MemcachedCluster o = manager.get(c, MemcachedCluster.class);
				if (null != o && null != o.getName() && o.getName().toLowerCase().contains("session")) {
					mem = c;
					break;
				}
			}
			if (null != mem) {
				IService[] services = getServiceQuery().getByCluster(mem);
				if (null != services && services.length > 0) {
					StringBuffer servers = new StringBuffer();
					int i = 1;
					// n1:192.168.11.121:11211,n2:192.168.11.123:11218,n3:192.168.11.123.11211...
					for (IService service : services) {
						servers.append("n").append(i).append(":") //$NON-NLS-1$ //$NON-NLS-2$
								.append(service.getIp()).append(":") //$NON-NLS-1$
								.append(service.getPort());
						if (i < services.length) {
							servers.append(","); //$NON-NLS-1$
						}
						i ++;
					}
					return servers.toString();
				}
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return null;
	}

}
