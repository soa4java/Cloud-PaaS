/**
 * 
 */
package com.primeton.paas.app.util;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class XmlUtilException extends RuntimeException {
	
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * The default constructor.<BR>
	 */
	public XmlUtilException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public XmlUtilException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public XmlUtilException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public XmlUtilException(Throwable cause) {
		super(cause);
	}
	
}
