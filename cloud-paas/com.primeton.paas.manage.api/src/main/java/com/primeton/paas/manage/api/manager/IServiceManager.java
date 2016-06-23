/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.model.IService;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IServiceManager {
	
	/**
	 * default service manager <br>
	 */
	public static final String DEFAULT_MANAGER = "Default";
	
	/**
	 * 
	 * @return
	 */
	String getType();
	
	/**
	 * 
	 * @param ip
	 * @param timeout
	 * @throws ServiceException
	 */
	void install(String ip, long timeout) throws ServiceException;
	
	/**
	 * 
	 * @param ip
	 * @param timeout
	 * @throws ServiceException
	 */
	void uninstall(String ip, long timeout) throws ServiceException;
	
	/**
	 * 
	 * @param service
	 * @param clusterId
	 * @return
	 * @throws ServiceException
	 */
	<T extends IService> T add(T service, String clusterId) throws ServiceException;
	
	/**
	 * 
	 * @param service
	 * @param clusterId
	 * @throws ServiceException
	 */
	<T extends IService> void update(T service, String clusterId) throws ServiceException; 
	
	/**
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	void destroy(String id) throws ServiceException;
	
	/**
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	void start(String id) throws ServiceException;
	
	/**
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	void stop(String id) throws ServiceException;
	
	/**
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	void restart(String id) throws ServiceException;
	
}
