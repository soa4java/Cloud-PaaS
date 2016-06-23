/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.manager;

import java.util.List;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.service.GatewayService;

/**
 * 网关服务管理器. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IGatewayServiceManager extends IServiceManager {
	
	/**
	 * 创建网关服务. <br>
	 * 
	 * @param service 服务信息
	 * @param clusterId 集群标识
	 * @param number 实例数量
	 * @return
	 * @throws ServiceException
	 */
	List<GatewayService> add(GatewayService service, String clusterId, int number) throws ServiceException;
	
	/**
	 * 启动服务监视器(异常终止的服务自动启动). <br>
	 */
	void startupMonitor();
	
	/**
	 * 关闭服务监视器(异常终止的服务自动启动). <br>
	 */
	void shutdownMonitor();
	
}
