/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.ServiceUtil;
import com.primeton.paas.manage.api.manager.IGatewayServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.GatewayService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Gateway Service Manager. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class GatewayServiceManager extends DefaultServiceManager 
		implements IGatewayServiceManager {
	
	public static final String TYPE = GatewayService.TYPE;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(GatewayServiceManager.class);
	
	private static GatewayDaemon daemon;

	/**
	 * Default. <br>
	 */
	public GatewayServiceManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#add(com.primeton.paas.manage.api.model.IService, java.lang.String)
	 */
	public <T extends IService> T add(T service, String clusterId)
			throws ServiceException {
		return create(service, clusterId);
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IGatewayServiceManager#add(com.primeton.paas.manage.api.service.GatewayService, int)
	 */
	public List<GatewayService> add(GatewayService service, int number)
			throws ServiceException {
		List<GatewayService> services = new ArrayList<GatewayService>();
		if (null == service || number < 1) {
			return services;
		}
		if (number == 1) {
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IGatewayServiceManager#add(com.primeton.paas.manage.api.service.GatewayService, java.lang.String, int)
	 */
	public List<GatewayService> add(GatewayService service, String clusterId,
			int number) throws ServiceException {
		List<GatewayService> services = new ArrayList<GatewayService>();
		if (null == service || number < 1 || StringUtil.isEmpty(clusterId)) {
			logger.error("Illegal arguments, can not save Gateway service.");
			return services;
		}
		if (number == 1) {
			GatewayService instance = add(service, clusterId);
			services.add(instance);
			return services;
		}
		service.setHaMode(IService.HA_MODE_CLUSTER);
		// 申请主机资源
		String[] ips = null;
		try {
			ips = getHostAssignManager().apply(service.getPackageId(), service.getType(), service.isStandalone(), 
					number, getHostAssignManager().getInstallTimeout(getType()));
		} catch (Throwable t) {
			logger.error("Assign host resource for package : {0}, number : {1}, type : Gateway error.", //$NON-NLS-1$
					new Object[] {service.getPackageId(), number}, t);
		}
		if (ips == null || ips.length == 0) {
			logger.error("Assign host resource error, not enough host resource."); //$NON-NLS-1$
			throw new ServiceException("Not enough host resource for apply. require [" + number + "]"); //$NON-NLS-1$
		}
		if (number == ips.length) {
			// 申请成功
			logger.info("Create Gateway service required machine [" + number + "] has been applied for."); //$NON-NLS-1$
		} else {
			try {
				getHostManager().release(ips);
			} catch (HostException e) {
				logger.error(e);
			}
			throw new ServiceException("Not enough host resource for apply, require [" + number + "], left [" + ips.length + "]."); //$NON-NLS-1$
		}
		
		// 保存服务实例
		for (int i=0; i<number; i++) {
			GatewayService instance = ServiceUtil.copy(service);
			instance.setIp(ips[i]);
			services.add(super.add(instance, clusterId)); // 保存至数据库
		}
		return services;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IGatewayServiceManager#startupMonitor()
	 */
	public void startupMonitor() {
		if (null != daemon && daemon.isRunning()) {
			logger.warn("Gateway service monitor daemon thread has been startup.");
			return;
		}
		daemon = new GatewayDaemon();
		Thread thread = new Thread(daemon);
		thread.setDaemon(true);
		thread.setName("Gateway_Daemon"); //$NON-NLS-1$
		thread.start();
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IGatewayServiceManager#shutdownMonitor()
	 */
	public void shutdownMonitor() {
		if (null == daemon || !daemon.isRunning()) {
			logger.warn("Gateway service monitor daemon thread has been shut down.");
			return;
		}
		daemon.shutdown();
		daemon = null;
	}
	
	/**
	 * 检查是否存在异常终止的网关服务,如果存在则启动. <br>
	 */
	protected void healthCheck() {
		List<GatewayService> services = getServiceQuery().getByType(GatewayService.TYPE, GatewayService.class);
		if (null == services || services.isEmpty()) {
			logger.debug("[Health Check] None Gateway service instances now.");
			return;
		}
		for (GatewayService service : services) {
			if (IService.STATE_RUNNING != service.getState()) {
				logger.warn("Gateway service [" + service.getId() + "] aborts unexpectedly, then start it.");
				try {
					start(service.getId());
				} catch (Throwable t) {
					logger.warn("Gateway service [" + service.getId() + "] aborts unexpectedly, start error.");
					logger.debug(t);
				}
			}
		}
	}

	/**
	 * Gateway service daemon thred. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	protected class GatewayDaemon implements Runnable {
		
		private boolean signal = true; 

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			while (signal) {
				// 健康检查
				try {
					healthCheck();
				} catch (Throwable t) {
					logger.error(t);
				}
				logger.debug("Finish Gateway service health check, wait 5s check again.");
				// 休眠一下
				ThreadUtil.sleep(5L * 1000L); // 5s
			}
		}
		
		/**
		 * Shut down monitor. <br>
		 */
		public void shutdown() {
			this.signal = false;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isRunning() {
			return this.signal;
		}
		
	}
	
}
