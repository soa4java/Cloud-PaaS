/**
 * 
 */
package com.primeton.paas.openapi.engine;

import java.util.UUID;

import javax.servlet.AsyncContext;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class BizRequest {
	
	private String transactionId;
	
	private String custId;	 
	
	private String bizCode; 
	
	private String bizParams; 
	
	private AsyncContext actx; 

	public BizRequest() {
		transactionId= UUID.randomUUID().toString();
	}
	
	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public AsyncContext getActx() {
		return actx;
	}

	public void setActx(AsyncContext actx) {
		this.actx = actx;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getBizParams() {
		return bizParams;
	}

	public void setBizParams(String bizParams) {
		this.bizParams = bizParams;
	}
	
}
