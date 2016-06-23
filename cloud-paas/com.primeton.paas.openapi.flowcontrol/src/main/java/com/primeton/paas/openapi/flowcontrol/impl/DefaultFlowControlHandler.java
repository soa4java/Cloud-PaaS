package com.primeton.paas.openapi.flowcontrol.impl;

import com.primeton.paas.openapi.flowcontrol.IFlowControlHandler;

/**
 * Ĭ�ϵ��������ƴ�����
 * 
 * @author Hao
 *
 */
public class DefaultFlowControlHandler implements IFlowControlHandler {
	
	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.flowcontrol.IFlowControlHandler#canInvoke(java.lang.String, java.lang.String)
	 */
	public boolean canInvoke(String transactionId,String custId, String bizCode) {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.flowcontrol.IFlowControlHandler#invoke(java.lang.String, java.lang.String)
	 */
	public void afterInvoke(String transactionId,String custId, String bizCode,int completionStatus) {
		// TODO Auto-generated method stub
	}
}
