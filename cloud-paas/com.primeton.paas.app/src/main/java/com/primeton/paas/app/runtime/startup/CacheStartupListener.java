/**
 * 
 */
package com.primeton.paas.app.runtime.startup;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.api.cache.CacheServiceException;
import com.primeton.paas.app.api.cache.CacheServiceFactory;
import com.primeton.paas.app.api.cache.ICacheService;
import com.primeton.paas.app.runtime.IRuntimeListener;
import com.primeton.paas.app.runtime.RuntimeEvent;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;


/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CacheStartupListener implements IRuntimeListener {
	
	private static ILogger logger = SystemLoggerFactory.getLogger(CacheStartupListener.class);
	
	/**
	 * 
	 */
	public void start(RuntimeEvent envent) {
		//
	}

	/**
	 * 
	 */
	public void stop(RuntimeEvent envent) {
		try {
			ICacheService service = CacheServiceFactory.getCacheService();
			if (service != null) {
				service.shutdown();
			}
		} catch (CacheServiceException e) {
			logger.error(e);
		}
	}
	
}
