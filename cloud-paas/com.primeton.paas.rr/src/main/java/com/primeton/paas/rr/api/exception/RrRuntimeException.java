/**
 * 
 */
package com.primeton.paas.rr.api.exception;

import com.primeton.paas.exception.api.PaasRuntimeException;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RrRuntimeException extends PaasRuntimeException {

	private static final long serialVersionUID = 9073737454804074819L;

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public RrRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param message
	 */
	public RrRuntimeException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param cause
	 */
	public RrRuntimeException(Throwable cause) {
		super(cause);
	}

}
