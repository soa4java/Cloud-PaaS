/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import com.primeton.paas.manage.api.exception.ConfigureException;
import com.primeton.paas.manage.api.exception.WebAppException;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.WebApp;

/**
 * 应用管理器. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IWebAppManager {

	/**
	 * 
	 * @param webApp
	 * @throws WebAppException
	 */
	void add(WebApp webApp) throws WebAppException;
	
	/**
	 * 
	 * @param app
	 * @throws WebAppException
	 */
	void update(WebApp app) throws WebAppException;
	
	/**
	 * 
	 * @param name
	 */
	void delete(String name);
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	WebApp get(String name);
	
	/**
	 * 
	 * @return
	 */
	WebApp[] getAll();
	
	/**
	 * 
	 * @param pageCond
	 * @return
	 */
	WebApp[] getAll(IPageCond pageCond);
	
	/**
	 * 
	 * @param owner
	 * @return
	 */
	WebApp[] getByOwner(String owner);
	
	/**
	 * 
	 * @param owner
	 * @param pageCond
	 * @return
	 */
	WebApp[] getByOwner(String owner, IPageCond pageCond);
	
	/**
	 * 
	 * @param appName
	 * @param clusterId
	 */
	void bind(String appName, String clusterId);
	
	/**
	 * 
	 * @param appName
	 * @param clusterId
	 */
	void unbind(String appName, String clusterId);
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	String[] getRelationApp(String clusterId);
	
	/**
	 * 
	 * @param appName
	 * @throws ConfigureException
	 */
	void initAppConfig(String appName) throws ConfigureException;
	
	/**
	 * 
	 * @param appName
	 */
	void refreshAppService(String appName);
	
	/**
	 * 
	 * @param appName
	 */
	void deleteAppConfig(String appName);
	
	/**
	 * 
	 * @param appName
	 * @param module
	 */
	void deleteAppConfigModule(String appName, String module);
	
}
