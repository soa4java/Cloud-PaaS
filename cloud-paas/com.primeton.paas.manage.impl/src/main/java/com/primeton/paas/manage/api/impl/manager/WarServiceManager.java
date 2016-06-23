/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.ObjectUtil;
import com.primeton.paas.manage.api.manager.IWarServiceManager;
import com.primeton.paas.manage.api.service.WarService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class WarServiceManager extends DefaultServiceManager 
		implements IWarServiceManager {
	
	public static final String TYPE = WarService.TYPE;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(WarServiceManager.class);
	
	public WarServiceManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IWarServiceManager#update(java.lang.String, com.primeton.paas.manage.api.service.WarService)
	 */
	public void update(String clusterId, WarService service) throws ServiceException {
		if (StringUtil.isEmpty(clusterId) || service == null
				|| StringUtil.isEmpty(service.getId())) {
			return;
		}
		WarService warService = getServiceQuery().get(service.getId(), WarService.class);
		if (warService == null) {
			throw new ServiceException(ServiceException.SERVICE_INSTANCE_NOT_EXISTS);
		}
		try {
			getInstanceManager().saveServiceInst(clusterId, ObjectUtil.toCesium(service));
		} catch (Throwable t) {
			logger.error(t);
			throw new ServiceException(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IWarServiceManager#setWarDeployFlag(java.lang.String, java.lang.String, boolean)
	 */
	public void setWarDeployFlag(String clusterId, String serviceId, boolean isDeployed) throws ServiceException {
		if (StringUtil.isEmpty(serviceId) || StringUtil.isEmpty(clusterId)) {
			return;
		}
		WarService warService = getServiceQuery().get(serviceId, WarService.class);
		if (warService == null) {
			throw new ServiceException(ServiceException.SERVICE_INSTANCE_NOT_EXISTS);
		}
		warService.setDeployVersion(isDeployed);
		try {
			getInstanceManager().saveServiceInst(clusterId, ObjectUtil.toCesium(warService));
		} catch (Throwable t) {
			logger.error(t);
			throw new ServiceException(t);
		}
	}

}
