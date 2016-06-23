/**
 * 
 */
package com.primeton.paas.app.api.mail;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MailException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7041827601151296157L;

	public MailException() {
		super();
	}

	public MailException(String message, Throwable cause) {
		super(message, cause);
	}

	public MailException(String message) {
		super(message);
	}

	public MailException(Throwable cause) {
		super(cause);
	}

}
