/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.manager.ICEPEngineServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.CEPEngineService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class CEPEngineServiceManager extends DefaultServiceManager 
		implements ICEPEngineServiceManager {
	
	public static final String TYPE = CEPEngineService.TYPE;

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#add(com.primeton.paas.manage.api.model.IService, java.lang.String)
	 */
	public <T extends IService> T add(T service, String clusterId)
			throws ServiceException {
		return create(service, clusterId);
	}

}
