/**
 * 
 */
package com.primeton.paas.openapi.security;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface ISecurityProvider {
	
	/**
	 * 
	 * @return
	 */
	public ISecurityHandler getSecurityHander();
	
}
