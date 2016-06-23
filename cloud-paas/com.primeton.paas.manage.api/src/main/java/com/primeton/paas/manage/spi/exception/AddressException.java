/**
 * 
 */
package com.primeton.paas.manage.spi.exception;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class AddressException extends Exception {
	
	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = -154984151893360918L;

	public static final String NO_AVAILABLE_VIP = "None available ip address.";

	/**
	 * Default. <br>
	 */
	public AddressException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AddressException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public AddressException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AddressException(Throwable cause) {
		super(cause);
	}

}
