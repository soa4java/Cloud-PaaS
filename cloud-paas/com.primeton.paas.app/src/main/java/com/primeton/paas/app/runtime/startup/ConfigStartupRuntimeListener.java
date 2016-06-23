/**
 * 
 */
package com.primeton.paas.app.runtime.startup;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.config.Configuration;
import com.primeton.paas.app.config.ConfigurationManager;
import com.primeton.paas.app.config.impl.ConfigurationLoader;
import com.primeton.paas.app.runtime.IRuntimeListener;
import com.primeton.paas.app.runtime.RuntimeEvent;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;


/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConfigStartupRuntimeListener implements IRuntimeListener  {
	
	private static ILogger log = SystemLoggerFactory.getLogger(ConfigStartupRuntimeListener.class);

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.runtime.IRuntimeListener#start(com.primeton.paas.app.runtime.RuntimeEvent)
	 */
	public void start(RuntimeEvent event) {
		long begin = System.currentTimeMillis();
		log.debug("ConfigStartupRuntimeListener start begin.");

		Configuration[] configs = event.getConfigurations();
		
		ConfigurationLoader.initLoad(configs);
		log.debug("ConfigStartupRuntimeListener start end.({0} ms)", new Object[]{System.currentTimeMillis() - begin});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.runtime.IRuntimeListener#stop(com.primeton.paas.app.runtime.RuntimeEvent)
	 */
	public void stop(RuntimeEvent event) {
		long begin = System.currentTimeMillis();
		log.info("ConfigStartupRuntimeListener stop begin.");
		Configuration[] configs = event.getConfigurations();
		ConfigurationLoader.unLoad(configs);
	
		ConfigurationManager.getInstance().clear();
		log.info("ConfigStartupRuntimeListener stop end.({0} ms)", new Object[]{System.currentTimeMillis() - begin});
	}
	
}
