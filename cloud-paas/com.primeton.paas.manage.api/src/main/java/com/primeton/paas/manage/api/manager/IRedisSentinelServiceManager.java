/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.manager;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IRedisSentinelServiceManager extends IServiceManager {
	
	/**
	 * 启动服务监视器. <br>
	 */
	void startupMonitor();
	
	/**
	 * 关闭服务监视器. <br>
	 */
	void shutdownMonitor();

}
