/**
 * 
 */
package com.primeton.paas.openapi.engine;

import com.primeton.paas.openapi.security.ISecurityHandler;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IBizInvoker {
	
	/**
	 * 
	 * @return
	 */
	public String getBizCode();

	/**
	 * 
	 * @param req
	 */
	public BizInvokeResult invokeBiz(BizRequest req);

	/**
	 * 
	 * @param securityHandler
	 */
	public void setSecurityHandler(ISecurityHandler securityHandler);
	
}
