/**
 * 
 */
package com.primeton.paas.cep.spi;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EventTypeException extends Exception {

	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = 7522061209225712821L;

	/**
	 * 
	 */
	public EventTypeException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EventTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public EventTypeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public EventTypeException(Throwable cause) {
		super(cause);
	}
	

}
