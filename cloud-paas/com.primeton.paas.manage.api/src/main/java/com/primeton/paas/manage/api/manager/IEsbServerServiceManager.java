/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.manager;

import java.util.List;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.service.EsbServerService;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IEsbServerServiceManager extends IServiceManager {

	/**
	 * 
	 * @param service
	 * @param clusterId
	 * @param number
	 * @return
	 * @throws ServiceException
	 */
	List<EsbServerService> add(EsbServerService service, String clusterId,
			int number) throws ServiceException;
	
}
