/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import java.util.concurrent.TimeoutException;

import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.exception.ServiceException;

/**
 * 主机分配管理器. <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IHostAssignManager {

	/**
	 * 
	 * @param packageId
	 * @param type
	 * @param isStandalone
	 * @param number
	 * @param timeout
	 * @return
	 * @throws HostException
	 * @throws ServiceException
	 * @throws TimeoutException
	 */
	String[] apply(String packageId, String type, boolean isStandalone,
			int number, long timeout) throws HostException, ServiceException,
			TimeoutException;
	
	/**
	 * 
	 * @param packageId
	 * @param type
	 * @param timeout
	 * @return
	 * @throws HostException
	 * @throws ServiceException
	 * @throws TimeoutException
	 */
	String[] applyMS(String packageId, String type, long timeout)
			throws HostException, ServiceException, TimeoutException;
	
	/**
	 * 
	 * @param packageId
	 * @param serviceType
	 * @return
	 */
	int getMaxInstancesSize(String packageId, String serviceType);
	
	/**
	 * 
	 * @param serviceType
	 * @return
	 */
	long getInstallTimeout(String serviceType);
	
}
