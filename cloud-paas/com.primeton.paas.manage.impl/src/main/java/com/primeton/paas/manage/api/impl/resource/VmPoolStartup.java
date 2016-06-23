/**
 * 
 */
package com.primeton.paas.manage.api.impl.resource;

import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.spi.factory.VmPoolConfigFactory;
import com.primeton.paas.manage.spi.model.VmPoolConfig;
import com.primeton.paas.manage.spi.resource.IVmPoolConfig;
import com.primeton.paas.manage.spi.resource.IVmPoolStartup;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VmPoolStartup implements IVmPoolStartup {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(VmPoolStartup.class);
	
	private boolean isStarted = false;
	
	private IVmPoolConfig configManager;
	
	/**
	 * 
	 */
	public VmPoolStartup() {
		super();
		configManager = VmPoolConfigFactory.getManager();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmPoolManager#start()
	 */
	public void start() {
		if (this.isStarted) {
			return;
		}
		if (!SystemVariables.isIaasEnableVm()) {
			logger.warn("Can not start VM resource monitor, IAAS-VM API has been disabled.");
			return;
		}
		List<VmPoolConfig> configs = configManager.getAllEnabled();
		if (configs == null || configs.isEmpty()) {
			return;
		}
		for (VmPoolConfig vmPoolConfig : configs) {
			HostResourceMonitor monitor = new HostResourceMonitor(vmPoolConfig.getId(), vmPoolConfig);
			HostResourceMonitorManager.register(monitor);
		}
		this.isStarted = true;
		logger.info("Start VM POOL success.");
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmPoolManager#stop()
	 */
	public void stop() {
		if (!this.isStarted) {
			return;
		}
		HostResourceMonitorManager.unregister();
		this.isStarted = true;
		logger.info("Stop VM POOL success.");
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmPoolManager#restart()
	 */
	public void restart() {
		stop();
		start();
	}

}
