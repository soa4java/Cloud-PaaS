/**
 *
 */
package com.primeton.paas.manage.api.manager;

import java.util.List;

import com.primeton.paas.manage.api.exception.DeployException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.exception.UndeployException;
import com.primeton.paas.manage.api.service.TomcatService;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface ITomcatServiceManager extends IServiceManager {

	/**
	 * 创建多实例Tomcat服务. <br>
	 * 
	 * @param service Tomcat服务
	 * @param clusterId 集群标识
	 * @param number 集群数量
	 * @return 创建的服务
	 * @throws ServiceException
	 */
	List<TomcatService> add(TomcatService service, String clusterId, int number)
			throws ServiceException;
	
	/**
	 * 部署应用WAR. <br>
	 * 
	 * @param tomcatId Tomcat服务标识
	 * @param warId WAR版本标识
	 * @param svnRepoId SVN仓库服务标识
	 * @throws DeployException
	 */
	void deploy(String tomcatId, String warId, String svnRepoId)
			throws DeployException;
	
	/**
	 * 
	 * @param id Tomcat服务标识
	 * @throws UndeployException
	 */
	void undeploy(String id) throws UndeployException;
	
}
