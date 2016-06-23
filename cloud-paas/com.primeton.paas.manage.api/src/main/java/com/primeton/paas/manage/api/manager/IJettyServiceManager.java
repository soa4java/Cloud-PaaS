/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import java.util.List;

import com.primeton.paas.manage.api.exception.DeployException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.exception.UndeployException;
import com.primeton.paas.manage.api.service.JettyService;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IJettyServiceManager extends IServiceManager {
	
	/**
	 * 创建Jetty服务. <br>
	 * 
	 * @param service
	 * @param clusterId
	 * @param number
	 * @return
	 * @throws ServiceException
	 */
	List<JettyService> add(JettyService service, String clusterId, int number)
			throws ServiceException;

	/**
	 * 部署应用WAR. <br>
	 * 
	 * @param jettyId
	 * @param warId
	 * @param svnRepoId
	 * @throws DeployException
	 */
	void deploy(String jettyId, String warId, String svnRepoId)
			throws DeployException;	
	
	/**
	 * 卸载应用WAR. <br>
	 * 
	 * @param id
	 * @throws UndeployException
	 */
	void undeploy(String id) throws UndeployException;
	
}
