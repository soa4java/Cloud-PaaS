/**
 * 
 */
package com.primeton.paas.openapi.flowcontrol.impl;

import com.primeton.paas.openapi.flowcontrol.IFlowControlHandler;
import com.primeton.paas.openapi.flowcontrol.IFlowControlProvider;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultFlowControlProvider implements IFlowControlProvider {
	
	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.flowcontrol.IFlowControlProvider#getFlowControlHandler()
	 */
	public IFlowControlHandler getFlowControlHandler() {
		return new DefaultFlowControlHandler();
	}
	
}
