/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.manager.IKeepalivedServiceManager;
import com.primeton.paas.manage.api.service.KeepalivedService;
import com.primeton.paas.manage.spi.exception.AddressException;
import com.primeton.paas.manage.spi.factory.VIPManagerFactory;
import com.primeton.paas.manage.spi.resource.IVIPManager;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class KeepalivedServiceManager extends DefaultServiceManager 
		implements IKeepalivedServiceManager {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(KeepalivedServiceManager.class);
	
	public static final String TYPE = KeepalivedService.TYPE;
	
	private static IVIPManager vipManager = VIPManagerFactory.getManager();
	
	/**
	 * Default <br>
	 */
	public KeepalivedServiceManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#destroy(java.lang.String)
	 */
	public void destroy(String id) throws ServiceException {
		KeepalivedService service = getServiceQuery().get(id,
				KeepalivedService.class);
		if (service == null) {
			return;
		}
		String vipStr = service.getVirtualIpAddress();
		String vip = vipStr.substring(0, vipStr.indexOf("/")); //$NON-NLS-1$
		super.destroy(id);
		try {
			vipManager.release(vip);
		} catch (AddressException e) {
			logger.error(e);
		}
	}

}
