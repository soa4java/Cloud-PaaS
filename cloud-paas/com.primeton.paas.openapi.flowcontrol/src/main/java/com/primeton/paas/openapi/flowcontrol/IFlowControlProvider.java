/**
 * 
 */
package com.primeton.paas.openapi.flowcontrol;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IFlowControlProvider {
	
	/**
	 * 
	 * @return
	 */
	public IFlowControlHandler getFlowControlHandler();

}
