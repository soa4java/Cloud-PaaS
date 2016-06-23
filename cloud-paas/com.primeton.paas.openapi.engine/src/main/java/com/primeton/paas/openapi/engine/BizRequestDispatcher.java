/**
 * 
 */
package com.primeton.paas.openapi.engine;

import com.primeton.paas.openapi.engine.config.ServiceGradePolicyManager;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class BizRequestDispatcher {
	
	/**
	 * 
	 * @param req
	 */
	public static void dispatchRequest(BizRequest req) {
		boolean isHighPriority = ServiceGradePolicyManager.getInstance()
				.isHighPriortiy(req.getBizCode());
		if (isHighPriority) {
			BizRequestQueueManager.addFastRequest(req);
		} else {
			BizRequestQueueManager.addSlowRequest(req);
		}
	}
	
}
