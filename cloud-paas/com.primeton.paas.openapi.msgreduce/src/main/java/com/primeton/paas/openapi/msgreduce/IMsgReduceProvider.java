/**
 * 
 */
package com.primeton.paas.openapi.msgreduce;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IMsgReduceProvider {
	
	/**
	 * 
	 * @return
	 */
	public IMsgReduceHandler getMsgReduceHandler();
	
}
