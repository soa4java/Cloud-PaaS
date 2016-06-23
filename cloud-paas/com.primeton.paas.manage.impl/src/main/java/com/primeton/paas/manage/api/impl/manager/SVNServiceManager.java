/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.HashMap;
import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.CommandResultMessage;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.ISVNServiceManager;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.SVNRepositoryService;
import com.primeton.paas.manage.api.service.SVNService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class SVNServiceManager extends DefaultServiceManager 
		implements ISVNServiceManager {
	
	public static final String TYPE = SVNService.TYPE;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultHostAssignManager.class);

	public SVNServiceManager() {
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
	public <T extends IService> T add(T service, String clusterId)
			throws ServiceException {
		T instance = super.create(service, clusterId);
		if (instance != null && StringUtil.isNotEmpty(instance.getIp())) {
			String ip = instance.getIp();
			long timeout = getHostAssignManager().getInstallTimeout(SVNRepositoryService.TYPE);
			Host host = getHostManager().get(ip);
			if (host == null) {
				throw new ServiceException("Host [" + ip + "] not found.");
			
			} else if (host.getTypes().contains(SVNRepositoryService.TYPE)) {
				logger.info("Host [" + ip + "] is already installed service [" + SVNRepositoryService.TYPE + "].");
			}
			// download service package
			logger.info("Host [" + ip + "] download [" + SVNRepositoryService.TYPE + "] service package.");
			Map<String, String> args = new HashMap<String, String>();
			args.put("url", SystemVariables.getHttpRpository()); //$NON-NLS-1$
			args.put("name", SVNRepositoryService.TYPE); //$NON-NLS-1$
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
					throw new ServiceException("Host [" + ip + "] download service [" + SVNRepositoryService.TYPE + "] install package from [" + SystemVariables.getHttpRpository() + "] failured.");
				}
				// SUCCESS
				long end = System.currentTimeMillis();
				if (logger.isInfoEnabled()) {
					logger.info("Host [" + ip + "] download service [" + SVNRepositoryService.TYPE + "] install package from [" + SystemVariables.getHttpRpository() + "] success. Time spents " + (end-begin)/1000L + " seconds.");
				}
			} catch (MessageException e) {
				throw new ServiceException(e);
			}
			
			// UPDATE HOST TYPE
			host = getHostManager().get(ip);
			if (host != null) {
				host.addType(SVNRepositoryService.TYPE);
				try {
					getHostManager().update(host);
				} catch (HostException e) {
					logger.error(e);
				}
			}		
		}
		return instance;
	}
	
}
