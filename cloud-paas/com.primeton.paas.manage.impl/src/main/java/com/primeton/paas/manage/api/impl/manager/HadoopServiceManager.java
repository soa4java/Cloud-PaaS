/**
 *
 */
package com.primeton.paas.manage.api.impl.manager;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.manager.IHadoopServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.HadoopService;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HadoopServiceManager extends DefaultServiceManager 
		implements IHadoopServiceManager {
	
	public static final String TYPE = HadoopService.TYPE;

	/**
	 * Default. <br>
	 */
	public HadoopServiceManager() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#add(com.primeton.paas.manage.api.model.IService, java.lang.String)
	 */
	public <T extends IService> T add(T service, String clusterId)
			throws ServiceException {
		return create(service, clusterId);
	}
	
}
