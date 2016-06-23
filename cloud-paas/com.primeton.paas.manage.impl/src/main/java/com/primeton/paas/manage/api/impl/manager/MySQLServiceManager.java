/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.CommandResultMessage;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.exception.StorageException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IMySQLServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.MySQLService;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.model.Storage;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class MySQLServiceManager extends DefaultServiceManager 
		implements IMySQLServiceManager {

	public static final String TYPE = MySQLService.TYPE;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(MySQLServiceManager.class);
	
	private static final String CREATE_SCRIPT = "create.sh";
	private static final String DESTROY_SCRIPT = "destroy.sh";
	
	public MySQLServiceManager() {
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
	public <T extends IService> T add(T service, String clusterId) throws ServiceException {
		MySQLService instance = (MySQLService)super.create(service, clusterId);
		if (instance == null) {
			throw new ServiceException("Create service " + service
					+ " error.");
		}
		Storage storage = null;
		if (service.isStandalone() && SystemVariables.isIaasEnableStorage()) {
			List<String> whiteList = new ArrayList<String>();
			whiteList.add(instance.getIp());
			try {
				storage = getStorageManager().apply(instance.getStorageSize(), whiteList);
			} catch (StorageException e) {
				logger.error(e);
			}
			if (storage == null) {
				throw new ServiceException("Apply shared storage failured.");
			}
			try {
				getStorageManager().mount(instance.getIp(), storage.getId(), instance.getStoragePath());
			} catch (StorageException e) {
				throw new ServiceException(e);
			}
		}
		
		
		Map<String, String> arguments = new HashMap<String, String>();
		arguments.put("storagePath", instance.getStoragePath()); //$NON-NLS-1$
		arguments.put("user", instance.getUser()); //$NON-NLS-1$
		arguments.put("password", instance.getPassword()); //$NON-NLS-1$
		arguments.put("schemaName", instance.getSchema()); //$NON-NLS-1$
		arguments.put("characterSet", instance.getCharacterSet()); //$NON-NLS-1$
		
		try {
			String cmd = SystemVariables.getScriptPath(MySQLService.TYPE, CREATE_SCRIPT);
			CommandResultMessage message = SendMessageUtil.sendMessage(instance.getIp(), cmd, arguments, true);
			
			if (message == null) {
				super.destroy(instance.getId());
				throw new ServiceException("Can not receive agent response, timeout or agent is died.");
			} else if(message.getBody() != null && message.getBody().getSuccess()) {
				return (T)instance;
			} else {
				super.destroy(instance.getId());
				throw new ServiceException(message.getBody().toString());
			}
		} catch (Throwable t) {
			super.destroy(instance.getId());
			logger.error(t);
			throw new ServiceException(t);
		}
	}


	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#destory(java.lang.String)
	 */
	public void destroy(String id) throws ServiceException {
		if(StringUtil.isEmpty(id)) {
			return;
		}
		MySQLService instance = getServiceQuery().get(id, MySQLService.class);
		if (instance == null) {
			return;
		}
		if (instance.getState() == IService.STATE_RUNNING) {
			try {
				stop(id);
			} catch (ServiceException e) {
				logger.error(e);
			}
		}
		String ip = instance.getIp();
		int port = instance.getPort();
		
		try {
			long begin = System.currentTimeMillis();
			Map<String, String> arguments = new HashMap<String, String>();
			arguments.put("storagePath", instance.getStoragePath()); //$NON-NLS-1$
			arguments.put("id", instance.getId()); //$NON-NLS-1$
			String cmd = SystemVariables.getScriptPath(MySQLService.TYPE, DESTROY_SCRIPT);
			CommandResultMessage message = SendMessageUtil.sendMessage(instance.getIp(), cmd, arguments, false);
			if (message != null && message.getBody() != null && message.getBody().getSuccess()) {
				if(logger.isInfoEnabled()) {
					logger.info("Finish destroy MySQL [ip:" + ip + ", port:" + port + "]. Spent " + (System.currentTimeMillis() - begin)/1000L + " seconds.");
				}
			} else if (message != null && message.getBody() != null) {
				logger.error("Finish destroy MySQL [ip:" + ip + ", port:" + port + "] failed. ErrOut: \n" + message.getBody().getErrOut());
			} else {
				logger.error("CommandResultMessage is null.");
			}
		} catch (Throwable e) {
			logger.error(e);
		}
		super.destroy(id);
	}
	
}
