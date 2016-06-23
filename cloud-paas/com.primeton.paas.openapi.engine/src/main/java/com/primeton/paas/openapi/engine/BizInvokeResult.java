/**
 * 
 */
package com.primeton.paas.openapi.engine;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class BizInvokeResult {
	
	public static final int COMPLETED_STATUS_SUCCESS = 0;
	
	public static final int COMPLETED_STATUS_FAILURE = 1;
	
	public static final int COMPLETED_STATUS_TIMEOUT = -1;

	private String transactionId;

	private String requestId;

	private int completedStatus = 0;

	private String resultBodyStr;

	private Throwable exception;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public int getCompletedStauts() {
		return completedStatus;
	}

	public void setCompletedStatus(int completedStatus) {
		this.completedStatus = completedStatus;
	}

	public String getResultBodyStr() {
		return resultBodyStr;
	}

	public void setResultBodyStr(String resultBodyStr) {
		this.resultBodyStr = resultBodyStr;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
		this.completedStatus = 1;
	}
	
}
