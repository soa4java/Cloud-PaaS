/**
 * 
 */
package com.primeton.paas.app.api.mail;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class MailResult {
	
	public static final String SEND_RESULT_SUCCESSED = "0";
	public static final String SEND_RESULT_ERROR = "1";
	public static final String SEND_RESULT_NOTSEND = "3";
	
	public static final String CODE_ADDRESS_ERROR = "10";
	public static final String CODE_AUTHENTICATION_ERROR = "11";
	public static final String CODE_MESSAGEING_EXCEPTION = "12";
	
	private String mailId;
	
	private String state;

	private String exception;

	private String exceptionCode;
	
	public MailResult() {
		super();
	}

	public String getMailId() {
		return mailId;
	}
	
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getException() {
		return exception;
	}
	
	public void setException(String exception) {
		this.exception = exception;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}
	
	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String toString() {
		return "MailResult [mailId=" + mailId + ", state=" + state
				+ ", exception=" + exception + ", exceptionCode="
				+ exceptionCode + "]";
	}
	
}
