/**
 * 
 */
package com.primeton.paas.manage.api.impl.resource;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.factory.HostManagerFactory;
import com.primeton.paas.manage.api.factory.HostTemplateManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IHostManager;
import com.primeton.paas.manage.api.manager.IHostTemplateManager;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.HostTemplate;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.factory.VmManagerFactory;
import com.primeton.paas.manage.spi.factory.VmPoolConfigFactory;
import com.primeton.paas.manage.spi.model.VmPoolConfig;
import com.primeton.paas.manage.spi.resource.IVmManager;
import com.primeton.paas.manage.spi.resource.IVmPoolConfig;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HostResourceMonitor implements Runnable {

	private static ILogger logger = ManageLoggerFactory.getLogger(HostResourceMonitor.class);
	
	private boolean flag = true;
	private boolean isRunning = false;
	
	private String id;
	private VmPoolConfig vmPoolConfig;
	
	private int currentRetrySize = 0;
	
	private static IVmManager vmManager = VmManagerFactory.getManager();
	private static IHostManager hostManager = HostManagerFactory.getHostManager();
	private static IVmPoolConfig vmPoolConfigManager = VmPoolConfigFactory.getManager();
	private static IHostTemplateManager hostTemplateManager = HostTemplateManagerFactory.getManager();
	
	/**
	 * 
	 * @param id
	 * @param vmPoolConfig
	 */
	public HostResourceMonitor(String id, VmPoolConfig vmPoolConfig) {
		super();
		this.id = id;
		this.vmPoolConfig = vmPoolConfig;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (!SystemVariables.isIaasEnableVm()) {
			logger.warn("Can not start host resource monitor, IAAS VM API has been disabled.");
			return;
		}
		if (this.vmPoolConfig == null) {
			logger.warn("vmPoolConfig is null, can not start host resource monitor.");
			return;
		} else if (this.vmPoolConfig.getIsEnable() == VmPoolConfig.DISABLE) {
			return;
		}
		
		this.isRunning = true;
		logger.info("Start host resource monitor [" + this.vmPoolConfig.getId() + "] success.");
		
		HostTemplate template = hostTemplateManager.getTemplate(this.vmPoolConfig.getId());
		String imageId = template.getImageId();
		String profileId = template.getProfileId();
		
		while (this.flag) {
			Host[] hosts = hostManager.getByPackage(this.vmPoolConfig.getId());
			int left = 0;
			if (hosts != null && hosts.length > 0) {
				for (Host host : hosts) {
					if (!host.isAssigned()) {
						left ++;
					}
				}
			}
			
			if (left >= this.vmPoolConfig.getMinSize() && left <= vmPoolConfig.getMaxSize()) {
				if (logger.isInfoEnabled()) {
					logger.info("VmPool [" + this.vmPoolConfig.getId() + "] current size is " + left + ".");
				}
			} else if (left < this.vmPoolConfig.getMinSize()) {
				int size = this.vmPoolConfig.getMinSize() - left;
				size = size > this.vmPoolConfig.getIncreaseSize() ? this.vmPoolConfig.getIncreaseSize() : size;
				List<Host> createHosts = null;
				try {
					createHosts = vmManager.create(imageId, profileId, size, this.vmPoolConfig.getCreateTimeout() * 1000L);
					currentRetrySize = 0;
				} catch (Throwable t) {
					currentRetrySize ++;
					logger.error("Create vm error,id:"
							+ vmPoolConfig.getId() + " , currentRetrySize:"
							+ currentRetrySize + " , error:" + t);
					if (currentRetrySize >= vmPoolConfig.getRetrySize()) {
						this.close();
						vmPoolConfig.setIsEnable(VmPoolConfig.DISABLE);
						vmPoolConfigManager.update(vmPoolConfig);
						logger.warn("Disable " + vmPoolConfig + ", cause : invoke error " + vmPoolConfig.getRetrySize() + " times.");
					}
				}
				if (createHosts == null || createHosts.isEmpty()) {
					logger.warn("Create vm [imageId:" + imageId
							+ ", profileId:" + profileId + ", number:" + size
							+ "] error.");
				} else {
					for (Host host : createHosts) {
						host.setPackageId(this.vmPoolConfig.getId());
						host.setAssigned(false);
						try {
							hostManager.add(host);
						} catch (HostException e) {
							logger.error(e);
						}
					}
				}
			} else if (left > this.vmPoolConfig.getMaxSize()) { 
				int size = left - this.vmPoolConfig.getMaxSize();
				size = size > this.vmPoolConfig.getDecreaseSize() ? this.vmPoolConfig.getDecreaseSize() : size;
				List<Host> destroyHosts = null;
				try {
					destroyHosts = hostManager.apply(this.vmPoolConfig.getId(), size);
				} catch (HostException e) {
					if (logger.isErrorEnabled()) {
						logger.error(e);
					}
				}
				List<String> vmIDs = new ArrayList<String>();
				if (destroyHosts != null && !destroyHosts.isEmpty()) {
					for (Host host : destroyHosts) {
						String id = host.getId();
						if (StringUtil.isNotEmpty(id)) {
							vmIDs.add(id);
						}
						try {
							hostManager.delete(host.getIp());
						} catch (HostException e) {
							if (logger.isErrorEnabled()) {
								logger.error(e);
							}
						}
					}
					try {
						if(vmIDs.size()>0){
							vmManager.destroy(vmIDs.toArray(new String[vmIDs.size()]), this.vmPoolConfig.getDestroyTimeout() * 1000L * vmIDs.size());
						}
					} catch (Throwable t) {
						if (logger.isErrorEnabled()) {
							logger.error(t);
						}
					}
				}
			}
			
			ThreadUtil.sleep(this.vmPoolConfig.getTimeInterval() * 1000L);
		}
		this.isRunning = false;
	}
	
	public void close() {
		this.flag = false;
	}
	
	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.vmPoolConfig.toString();
	}
	
}
