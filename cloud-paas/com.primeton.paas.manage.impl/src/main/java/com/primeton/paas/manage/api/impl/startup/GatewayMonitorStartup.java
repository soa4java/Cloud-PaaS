/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.startup;

import com.primeton.paas.common.spi.AbstractStartupListener;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IGatewayServiceManager;
import com.primeton.paas.manage.api.service.GatewayService;

/**
 * 网关服务启动服务监视器:异常终止的服务自动启动. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class GatewayMonitorStartup extends AbstractStartupListener {

	/**
	 * Default. <br>
	 */
	public GatewayMonitorStartup() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.common.spi.AbstractStartupListener#doStart()
	 */
	protected void doStart() {
		IGatewayServiceManager manager = ServiceManagerFactory
				.getManager(GatewayService.TYPE);
		if (null != manager) {
			manager.startupMonitor();
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.common.spi.AbstractStartupListener#doStop()
	 */
	protected void doStop() {
		IGatewayServiceManager manager = ServiceManagerFactory
				.getManager(GatewayService.TYPE);
		if (null != manager) {
			manager.shutdownMonitor();
		}
	}

}
