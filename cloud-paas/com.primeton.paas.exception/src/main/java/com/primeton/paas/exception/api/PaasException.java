/**
 * 
 */
package com.primeton.paas.exception.api;

import java.text.MessageFormat;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class PaasException extends Exception {

	private static final long serialVersionUID = 3420233476544249485L;

	/**
	 * 
	 * @param message
	 */
	public PaasException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param param
	 */
	public PaasException(String message, Object[] param) {
		super(format(message, param));
	}

	/**
	 * 
	 * @param cause
	 */
	public PaasException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public PaasException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param message
	 * @param param
	 * @param cause
	 */
	public PaasException(String message, Object[] param, Throwable cause) {
		super(format(message, param), cause);
	}

	/**
	 * 
	 * @param message
	 * @param params
	 * @return
	 */
	static String format(String message, Object[] params) {
		if (message.trim().length() > 0) {
			if (params != null && params.length > 0) {
				return new MessageFormat(message).format(params);
			}
		}
		return message;
	}

}