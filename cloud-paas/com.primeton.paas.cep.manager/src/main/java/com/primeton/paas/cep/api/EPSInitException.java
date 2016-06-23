/**
 * 
 */
package com.primeton.paas.cep.api;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EPSInitException extends Exception {

	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = -1211298440610997236L;

	/**
	 * 
	 */
	public EPSInitException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EPSInitException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public EPSInitException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public EPSInitException(Throwable cause) {
		super(cause);
	}

}
