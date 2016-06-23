/**
 * 
 */
package com.primeton.paas.openapi.msgreduce.impl;

import com.primeton.paas.openapi.msgreduce.IMsgReduceHandler;
import com.primeton.paas.openapi.msgreduce.IMsgReduceProvider;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultMsgReduceProvider implements IMsgReduceProvider {
	
	private IMsgReduceHandler msgReduceHandler = null;
	
	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.msgreduce.IMsgReduceProvider#getMsgReduceHandler()
	 */
	public IMsgReduceHandler getMsgReduceHandler() {
		if (msgReduceHandler == null) {
			msgReduceHandler = new DefaultMsgReduceHandler();
		}
		return msgReduceHandler;
	}

}
