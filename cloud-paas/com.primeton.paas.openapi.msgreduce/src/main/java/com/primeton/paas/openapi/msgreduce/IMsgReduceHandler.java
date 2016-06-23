/**
 * 
 */
package com.primeton.paas.openapi.msgreduce;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IMsgReduceHandler {
	
	/**
	 * 
	 * @param jsonMsgStr
	 * @param bizCode
	 * @return
	 */
	public String reduceJsonMsg(String jsonMsgStr, String bizCode); 
	
}
