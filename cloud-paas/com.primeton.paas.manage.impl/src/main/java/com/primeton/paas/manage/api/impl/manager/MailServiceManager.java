/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.manager.IMailServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.MailService;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class MailServiceManager extends DefaultServiceManager 
		implements IMailServiceManager {

	public static final String TYPE = MailService.TYPE;
	
	public MailServiceManager(){
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
