/**
 * 
 */
package com.primeton.paas.manage.spi.exception;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VmException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3819574037969028407L;

	/**
	 * 
	 */
	public VmException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VmException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public VmException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public VmException(Throwable cause) {
		super(cause);
	}

}
