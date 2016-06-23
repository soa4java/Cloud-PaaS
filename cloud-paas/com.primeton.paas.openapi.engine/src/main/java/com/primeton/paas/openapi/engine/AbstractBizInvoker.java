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
public abstract class AbstractBizInvoker implements IBizInvoker {

	protected ISecurityHandler securityHandler;

	public void setSecurityHandler(ISecurityHandler securityHandler) {
		this.securityHandler = securityHandler;
	}
	
}
