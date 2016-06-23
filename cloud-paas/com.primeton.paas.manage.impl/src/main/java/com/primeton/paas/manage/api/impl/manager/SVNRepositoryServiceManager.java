/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.HashMap;
import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.CommandResultMessage;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.ISVNRepositoryServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.SVNRepositoryService;
import com.primeton.paas.manage.api.service.SVNService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class SVNRepositoryServiceManager extends DefaultServiceManager 
		implements ISVNRepositoryServiceManager {
	
	public static final String TYPE = SVNRepositoryService.TYPE;
	
	public static final String CREATE_REPOS_SCRIPT = "createRepos.sh";
	public static final String REMOVE_REPOS_SCRIPT = "removeRepos.sh";
	public static final String ADD_USER_SCRIPT = "addUser.sh";
	public static final String DELETE_USER_SCRIPT = "deleteUser.sh";
	
	private ILogger logger = ManageLoggerFactory.getLogger(SVNRepositoryServiceManager.class);
	
	/**
	 * Default. <br>
	 */
	public SVNRepositoryServiceManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}


	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#add(com.primeton.paas.manage.api.model.IService, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T extends IService> T add(T service, String clusterId)
			throws ServiceException {
		if (service == null || StringUtil.isEmpty(clusterId)) {
			return null;
		}
		SVNRepositoryService instance = (SVNRepositoryService)service;
		instance.setHaMode(IService.HA_MODE_CLUSTER);

		SVNService svnService = getSVNService();
		if(svnService == null) {
			throw new ServiceException("SVNService instance not available in the current environment.");
		}
		service.setParentId(svnService.getId());
		String agentID = svnService.getIp();
		SVNRepositoryService repositoryService = super.add(instance, clusterId);
		
		
		Map<String, String> args = service.getAttributes();
		if(args == null) {
			args = new HashMap<String, String>();
		}
		if (repositoryService != null) {
			long begin = System.currentTimeMillis();
			logger.info("Begin create SVN repository [id:" + repositoryService.getId() + ", ip:" + repositoryService.getIp() + ", repoName:" + repositoryService.getRepoName() + "].");
			try {
				CommandResultMessage rs1 = SendMessageUtil.sendMessage(agentID, 
						SystemVariables.getScriptPath(service.getType(), CREATE_REPOS_SCRIPT), args, true);
				
				if (rs1 != null && rs1.getBody() != null
						&& rs1.getBody().getSuccess()) {
					long end = System.currentTimeMillis();
					logger.info("Finish create SVN repository [id:" + repositoryService.getId() + ", ip:" + repositoryService.getIp() + ", repoName:" + repositoryService.getRepoName() + "]. Spent " + (end-begin)/1000L + " seconds.");
					logger.info("Begin create SVN user [id:" + repositoryService.getId() + ", ip:" + repositoryService.getIp() + ", repoName:" + repositoryService.getRepoName() + ", user:" + repositoryService.getUser() + "].");
					CommandResultMessage rs2 = SendMessageUtil.sendMessage(agentID, 
							SystemVariables.getScriptPath(service.getType(), ADD_USER_SCRIPT), args, true);
					
					if(rs2 != null && rs2.getBody() != null && rs2.getBody().getSuccess()) {
						end = System.currentTimeMillis();
						logger.info("Finish create SVN user [id:" + repositoryService.getId() + ", ip:" + repositoryService.getIp() + ", repoName:" + repositoryService.getRepoName() + ", user:" + repositoryService.getUser() + "]. Spent " + (end-begin)/1000L + " seconds.");
						return (T)repositoryService;
					} else {
						logger.info("Create SVN user [id:" + repositoryService.getId() + ", ip:" + repositoryService.getIp() + ", repoName:" + repositoryService.getRepoName() + ", user:" + repositoryService.getUser() + "] failured. Will delete repository and persistence information.");
						SendMessageUtil.sendMessage(agentID, SystemVariables.getScriptPath(service.getType(), REMOVE_REPOS_SCRIPT), args, false);
						destroy(repositoryService.getId());
					}
				} else {
					destroy(repositoryService.getId());
					logger.info("Create SVN repository [id:" + repositoryService.getId() + ", ip:" + repositoryService.getIp() + ", repoName:" + repositoryService.getRepoName() + "] failured and will delete persistence information.");
				}
			} catch (MessageException e) {
				destroy(repositoryService.getId());
				logger.error(e);
			}
			
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#destory(java.lang.String)
	 */
	public void destroy(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		SVNRepositoryService service = getServiceQuery().get(id, SVNRepositoryService.class);
		if (service == null) {
			logger.warn("Service instance [id:" + id
					+ "] not found, no need destory.");
			return;
		}

		String svnServiceId = service.getParentId();
		SVNService svnService = getServiceQuery().get(svnServiceId, SVNService.class);
		if (svnService==null) {
			return;
		}
		
		String agentID = svnService.getIp();
		Map<String, String> args = service.getAttributes();
		if(args == null) {
			args = new HashMap<String, String>();
		}
		
		try {
			//	CommandResultMessage message = SendMessageUtil.sendMessage(service, REMOVE_REPOS_SCRIPT, true);
			CommandResultMessage message = SendMessageUtil.sendMessage(agentID, 
					SystemVariables.getScriptPath(service.getType(), REMOVE_REPOS_SCRIPT), args, true);
			if(message == null) {
				String error = "Destory svn repository [id:" + service.getId() + ", repoName:" + service.getRepoName() + ", ip:" + service.getIp() + "] failured. Can not receive agent response, timeout or agent has died.";
				logger.info(error);
				throw new ServiceException(error);
				
			} else if(message.getBody() != null && message.getBody().getSuccess()) {
				logger.info("Destory svn repository [id:" + service.getId() + ", repoName:" + service.getRepoName() + ", ip:" + service.getIp() + "] success.");
			}  else {
				throw new ServiceException(message.getBody().toString());
			}
		} catch (MessageException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
		super.destroy(id);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (logger.isWarnEnabled()) {
			logger.warn("Not allow call this method.");
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#stop(java.lang.String)
	 */
	public void stop(String id) throws ServiceException {
		if (logger.isWarnEnabled()) {
			logger.warn("Not allow call this method.");
		}
	}

	/**
	 * 
	 * @return SVNService
	 */
	private SVNService getSVNService() {
		IService[] services = getServiceQuery().getByType(SVNService.TYPE);
		if (services == null || services.length == 0) {
			// TODO create svn service
			return null;
		} else if (services.length >= 1) {
			return (SVNService) services[0];
		}
		return null;
	}

}
