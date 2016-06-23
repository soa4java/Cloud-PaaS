/**
 * 
 */
package com.primeton.paas.manage.api.exception;

import com.primeton.paas.exception.api.PaasException;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class UndeployException extends PaasException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7241729734622935047L;

	public UndeployException(String message, Object[] param, Throwable cause) {
		super(message, param, cause);
	}

	public UndeployException(String message, Object[] param) {
		super(message, param);
	}

	public UndeployException(String message, Throwable cause) {
		super(message, cause);
	}

	public UndeployException(String message) {
		super(message);
	}

	public UndeployException(Throwable cause) {
		super(cause);
	}

}
