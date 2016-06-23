/**
 * 
 */
package com.primeton.paas.manage.api.exception;

import com.primeton.paas.exception.api.PaasException;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class HostException extends PaasException {
	
	private static final long serialVersionUID = 3816365096324157257L;
	
	/**
	 * @param message
	 * @param cause
	 */
	public HostException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public HostException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public HostException(Throwable cause) {
		super(cause);
	}


}
