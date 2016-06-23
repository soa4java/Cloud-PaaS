/**
 * 
 */
package com.primeton.paas.openapi.flowcontrol;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IFlowControlHandler {

	/**
	 * 
	 * @param transactionId
	 * @param custId
	 * @param bizCode
	 * @return
	 */
	public boolean canInvoke(String transactionId, String custId, String bizCode);

	/**
	 * 
	 * @param transactionId
	 * @param custId
	 * @param bizCode
	 * @param completionStatus
	 */
	public void afterInvoke(String transactionId, String custId, String bizCode, int completionStatus);

}
