/**
 * 
 */
package com.primeton.paas.exception.api;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class PaasRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 3420233476544249485L;

	/**
	 * 
	 * @param message
	 */
	public PaasRuntimeException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param param
	 */
	public PaasRuntimeException(String message, Object[] param) {
		super(PaasException.format(message, param));
	}

	/**
	 * 
	 * @param cause
	 */
	public PaasRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public PaasRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param message
	 * @param param
	 * @param cause
	 */
	public PaasRuntimeException(String message, Object[] param, Throwable cause) {
		super(PaasException.format(message, param), cause);
	}

}