/**
 * 
 */
package com.primeton.paas.openapi.httpin.servlet;

import com.primeton.paas.openapi.engine.BizRequest;
import com.primeton.paas.openapi.engine.BizRequestDispatcher;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class TaskExecutor implements Runnable {
	
	private BizRequest req;
	
	public TaskExecutor(BizRequest bizReq) {
		this.req = bizReq;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		BizRequestDispatcher.dispatchRequest(req);
	}
	
}
