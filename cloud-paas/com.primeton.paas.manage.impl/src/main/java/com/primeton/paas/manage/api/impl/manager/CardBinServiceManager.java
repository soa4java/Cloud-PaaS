/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.manager.ICardBinServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.CardBinService;

/**
 * Marked @Deprecated by ZhongWen.Li. <br>
 * 
 * @author Hao
 *
 */
@Deprecated
public class CardBinServiceManager extends DefaultServiceManager 
		implements ICardBinServiceManager {
	
	public static final String TYPE = CardBinService.TYPE;

	public CardBinServiceManager() {
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
