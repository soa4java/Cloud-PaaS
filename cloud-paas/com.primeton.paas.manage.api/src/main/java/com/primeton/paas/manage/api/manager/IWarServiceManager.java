/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.service.WarService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IWarServiceManager extends IServiceManager {

	/**
	 * 
	 * @param clusterId
	 * @param service
	 * @throws ServiceException
	 */
	void update(String clusterId, WarService service) throws ServiceException;
	
	/**
	 * 
	 * @param clusterId
	 * @param serviceId
	 * @param isDeployed
	 * @throws ServiceException
	 */
	void setWarDeployFlag(String clusterId, String serviceId, boolean isDeployed)
			throws ServiceException;
	
}
