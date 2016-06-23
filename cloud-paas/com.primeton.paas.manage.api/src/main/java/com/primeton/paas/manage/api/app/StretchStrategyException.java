/**
 * 
 */
package com.primeton.paas.manage.api.app;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class StretchStrategyException extends Exception {

	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = -8782479188701733473L;
	
	public static final String STRETCH_STRATEGY_NOT_EXIST = "Stretch strategy is not exist.";
	public static final String STRETCH_STRATEGY_ALREADY_EXIST = "Stretch strategy is already exist.";

	/**
	 * 
	 */
	public StretchStrategyException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public StretchStrategyException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public StretchStrategyException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public StretchStrategyException(Throwable cause) {
		super(cause);
	}

}
