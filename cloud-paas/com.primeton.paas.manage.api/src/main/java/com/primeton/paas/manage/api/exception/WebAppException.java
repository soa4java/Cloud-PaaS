/**
 * 
 */
package com.primeton.paas.manage.api.exception;

import com.primeton.paas.exception.api.PaasException;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class WebAppException extends PaasException {

	private static final long serialVersionUID = 9113849391425650650L;

	public static final String WEB_APP_NOT_EXISTS = "Webapp is not exists.";
	public static final String WEB_APP_EXISTS = "Webapp is already exists.";

	public WebAppException(String message, Object[] param, Throwable cause) {
		super(message, param, cause);
	}

	public WebAppException(String message, Object[] param) {
		super(message, param);
	}

	public WebAppException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebAppException(String message) {
		super(message);
	}

	public WebAppException(Throwable cause) {
		super(cause);
	}

}
