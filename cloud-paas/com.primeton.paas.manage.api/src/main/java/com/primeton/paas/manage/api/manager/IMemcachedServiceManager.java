/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import java.util.List;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.service.MemcachedService;


/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IMemcachedServiceManager extends IServiceManager {

	/**
	 * 
	 * @param service
	 * @param clusterId
	 * @param number
	 * @return
	 * @throws ServiceException
	 */
	List<MemcachedService> add(MemcachedService service, String clusterId, int number) throws ServiceException;
	
}
