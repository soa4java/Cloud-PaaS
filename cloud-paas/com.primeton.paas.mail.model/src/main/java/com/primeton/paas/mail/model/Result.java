/**
 * 
 */
package com.primeton.paas.mail.model;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class Result {

	private String mailId;
	private String state;
	private String exception;
	private String exceptionCode;

	public Result() {
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

}
