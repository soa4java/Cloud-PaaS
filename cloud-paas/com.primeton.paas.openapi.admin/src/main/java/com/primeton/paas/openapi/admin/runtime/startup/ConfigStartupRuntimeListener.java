/**
 * 
 */
package com.primeton.paas.openapi.admin.runtime.startup;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.openapi.admin.config.Configuration;
import com.primeton.paas.openapi.admin.config.ConfigurationManager;
import com.primeton.paas.openapi.admin.config.impl.ConfigurationLoader;
import com.primeton.paas.openapi.admin.runtime.IRuntimeListener;
import com.primeton.paas.openapi.admin.runtime.RuntimeEvent;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConfigStartupRuntimeListener implements IRuntimeListener {
	
	private static ILogger log = LoggerFactory.getLogger(ConfigStartupRuntimeListener.class);

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.openapi.admin.runtime.IRuntimeListener#start(com.primeton.paas.openapi.admin.runtime.RuntimeEvent)
	 */
	public void start(RuntimeEvent event) {
		long begin = System.currentTimeMillis();
		log.debug("ConfigStartupRuntimeListener start begin.");
		Configuration[] configs = event.getConfigurations();
		ConfigurationLoader.initLoad(configs);
		log.debug("ConfigStartupRuntimeListener start end.({0}ms)", new Object[]{System.currentTimeMillis() - begin});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.openapi.admin.runtime.IRuntimeListener#stop(com.primeton.paas.openapi.admin.runtime.RuntimeEvent)
	 */
	public void stop(RuntimeEvent event) {
		long begin = System.currentTimeMillis();
		log.info("ConfigStartupRuntimeListener stop begin.");
		Configuration[] configs = event.getConfigurations();
		ConfigurationLoader.unLoad(configs);
	
		ConfigurationManager.getInstance().clear();
		log.info("ConfigStartupRuntimeListener stop end.({0})", new Object[]{System.currentTimeMillis() - begin});
	}
	
}
