/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.manager.ISmsServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.SmsService;

/**
 * Marked @Deprecated by ZhongWen.Li
 *
 * @author liyanping(liyp@primeton.com)
 */
@Deprecated
public class SmsServiceManager extends DefaultServiceManager 
		implements ISmsServiceManager {

	public static final String TYPE = SmsService.TYPE;
	
	/**
	 * Default. <br>
	 */
	public SmsServiceManager(){
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
		return super.create(service, clusterId);
	}
	
}
