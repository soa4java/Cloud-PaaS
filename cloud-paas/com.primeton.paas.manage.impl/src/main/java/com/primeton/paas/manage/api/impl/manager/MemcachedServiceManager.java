/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.ServiceUtil;
import com.primeton.paas.manage.api.manager.IMemcachedServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.MemcachedService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class MemcachedServiceManager extends DefaultServiceManager 
		implements IMemcachedServiceManager {
	
	public static final String TYPE = MemcachedService.TYPE;
	
	private ILogger logger = ManageLoggerFactory.getLogger(MemcachedServiceManager.class);
	
	public MemcachedServiceManager() {
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
	 * @see com.primeton.paas.manage.api.manager.IMemcachedServiceManager#add(com.primeton.paas.manage.api.service.MemcachedService, java.lang.String, int)
	 */
	public List<MemcachedService> add(MemcachedService service,
			String clusterId, int number) throws ServiceException {
		List<MemcachedService> services = new ArrayList<MemcachedService>();
		if (service == null || StringUtil.isEmpty(clusterId) || number < 1) {
			return services;
		}
		String[] ips = null;
		try {
			ips = getHostAssignManager().apply(service.getPackageId(), getType(), service.isStandalone(), number, 
					getHostAssignManager().getInstallTimeout(getType()));
		} catch (Throwable t) {
			if (logger.isErrorEnabled()) {
				logger.error(t);
			}
		}
		if (ips == null || ips.length < number) {
			throw new ServiceException("Didn't apply enough to host resources. Can not create memcached service.");
		}
		for (int i=0; i<number; i++) {
			MemcachedService instance = ServiceUtil.copy(service);
			instance.setHaMode(IService.HA_MODE_BLOCK);
			instance.setIp(ips[i]);
			services.add(super.add(instance, clusterId));
		}
		return services;
	}

}
