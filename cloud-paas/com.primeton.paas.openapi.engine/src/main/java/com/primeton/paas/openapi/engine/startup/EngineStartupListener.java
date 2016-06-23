/**
 * 
 */
package com.primeton.paas.openapi.engine.startup;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.openapi.admin.runtime.IRuntimeListener;
import com.primeton.paas.openapi.admin.runtime.RuntimeEvent;
import com.primeton.paas.openapi.engine.BizInvokeResultCache;
import com.primeton.paas.openapi.engine.BizInvokerManager;
import com.primeton.paas.openapi.engine.BizRequestQueueManager;
import com.primeton.paas.openapi.engine.config.ServiceGradePolicyManager;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class EngineStartupListener implements IRuntimeListener {

	private static ILogger log = LoggerFactory.getLogger(EngineStartupListener.class);

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.runtime.IRuntimeListener#start(com.primeton.openplatform.openapi.admin.runtime.RuntimeEvent)
	 */
	public void start(RuntimeEvent envent) {
		BizRequestQueueManager.startThreadPool();
		//	BizResponseQueueManager.startThreadPool();
		log.debug("EngineStartupListener started.");
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.runtime.IRuntimeListener#stop(com.primeton.openplatform.openapi.admin.runtime.RuntimeEvent)
	 */
	public void stop(RuntimeEvent envent) {
		BizRequestQueueManager.stopThreadPool();
		//	BizResponseQueueManager.stopThreadPool();
		
		BizInvokerManager.clear();
		
		ServiceGradePolicyManager.clear();
		//	BizRequestCache.clear();
		BizInvokeResultCache.clear();
		log.debug("EngineStartupListener stopped.");
	}
	
}
