/**
 * 
 */
package com.primeton.paas.openapi.security.impl;

import com.primeton.paas.openapi.security.ISecurityHandler;
import com.primeton.paas.openapi.security.ISecurityProvider;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultSecurityProvider implements ISecurityProvider {
	
	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.security.ISecurityProvider#getSecurityHander()
	 */
	public ISecurityHandler getSecurityHander() {
		return new DefaultSecurityHandler();
	}
	
}
