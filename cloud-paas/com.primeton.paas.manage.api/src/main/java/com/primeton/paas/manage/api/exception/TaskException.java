/**
 * 
 */
package com.primeton.paas.manage.api.exception;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class TaskException extends Exception {

	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public TaskException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TaskException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public TaskException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public TaskException(Throwable cause) {
		super(cause);
	}
	
}
