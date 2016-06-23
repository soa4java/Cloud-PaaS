/**
 * 
 */
package com.primeton.paas.cep.spi;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EPSException extends Exception {

	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = 5987403727116858589L;

	/**
	 * 
	 */
	public EPSException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EPSException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public EPSException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public EPSException(Throwable cause) {
		super(cause);
	}

}
