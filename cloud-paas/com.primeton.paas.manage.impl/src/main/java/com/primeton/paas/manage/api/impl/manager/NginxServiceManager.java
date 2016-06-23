/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.manager.INginxServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.NginxService;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class NginxServiceManager extends DefaultServiceManager 
		implements INginxServiceManager {
	
	public static final String TYPE = NginxService.TYPE;
	
	public NginxServiceManager() {
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
		return super.create(service, clusterId);
	}

	/* 
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.INginxServiceManager#add(com.primeton.paas.manage.api.service.NginxService, java.lang.String, java.lang.String)
	 */
	/*
	public List<NginxService> add(NginxService service, String nginxClusterId,
			String keepalivedClusterId) throws ServiceException {
		List<NginxService> services = new ArrayList<NginxService>();
		if (service == null || StringUtil.isNullOrBlank(nginxClusterId)
				|| StringUtil.isNullOrBlank(keepalivedClusterId)) {
			return services;
		}
		long timeout = manager.getInstallTimeout(getType());
		String[] hosts = null;

		if (service.isStandalone()) {
			try {
				hosts = manager.applyMS(service.getPackageId(), service.getType(), timeout);
			} catch (Throwable t) {
				if (logger.isErrorEnabled()) {
					logger.error(t);
				}
			}
		} else {
			try {
				hosts = manager.apply(service.getPackageId(), service.getType(), false, 2, timeout);
			} catch (Throwable t) {
				if (logger.isErrorEnabled()) {
					logger.error(t);
				}
			}
		}
		if (hosts == null || hosts.length < 2) {
			throw new ServiceException("Didn't apply enough to host resources. Can not create nginx service.");
		}

		NginxService master = ServiceUtil.copy(service);
		master.setHaMode(IService.HA_MODE_MASTER);
		master.setIp(hosts[0]);
		NginxService _master = super.add(master, nginxClusterId);
		services.add(_master);
		
		NginxService slave = ServiceUtil.copy(service);
		slave.setHaMode(IService.HA_MODE_SLAVE);
		slave.setIp(hosts[1]);
		NginxService _slave = super.add(slave, nginxClusterId);
		services.add(_slave);
		
		String vip = null;
		try {
			vip = vipManager.apply();
		} catch (VIPException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		if (vip == null) {
			throw new ServiceException("Can not create keepalived instance, apply vip resource failured.");
		}
		KeepalivedService ks = new KeepalivedService();
		ks.setVirtualIpAddress(vip);
		ks.setAdvertInt(1);
		ks.setAuthPass("1111");
		ks.setAuthType("PASS");
		ks.setCreatedBy(service.getOwner());
		ks.setCreatedDate(new Date());
		ks.setInterface("eth0");
		ks.setNotificationEmail("root@localhost");
		ks.setNotificationEmailFrom("localhost");
		ks.setOwner(service.getOwner());
		ks.setPackageId(service.getPackageId());
		ks.setSmtpConnectTimeout(30);
		ks.setSmtpServer("localhost");
		ks.setStandalone(true);
		ks.setVirtualRouterId("51");
		ks.setVrrpScriptInterval(2);
		ks.setVrrpScriptWeight(2);
		
		KeepalivedService ksm = ServiceUtil.copy(ks); 
		ksm.setHaMode(IService.HA_MODE_MASTER);
		ksm.setIp(_master.getIp());
		ksm.setMcastSrcIp(_master.getIp());
		ksm.setName("master");
		ksm.setParentId(_master.getId());
		ksm.setPriority(200);
		ksm.setRouterId(_master.getId());
		ksm.setVrrpState(KeepalivedService.VRRP_STATE_MASTER);
		String script = SystemVariables.getBinHome() + "/" + getType() + "/bin/monitor_" + _master.getId() + ".sh";
		ksm.setVrrpScriptPath(script);
		
		KeepalivedService kss = ServiceUtil.copy(ks);
		kss.setHaMode(IService.HA_MODE_SLAVE);
		kss.setIp(_slave.getIp());
		kss.setMcastSrcIp(_slave.getIp());
		kss.setName("slave");
		kss.setParentId(_slave.getId());
		kss.setPriority(100);
		kss.setRouterId(_slave.getId());
		kss.setVrrpState(KeepalivedService.VRRP_STATE_BACKUP);
		String _script = SystemVariables.getBinHome() + "/" + getType() + "/bin/monitor_" + _slave.getId() + ".sh";
		kss.setVrrpScriptPath(_script);
		
		keepalivedServiceManager.add(ksm, keepalivedClusterId);
		keepalivedServiceManager.add(kss, keepalivedClusterId);
		
		return services;
	}
	*/

}