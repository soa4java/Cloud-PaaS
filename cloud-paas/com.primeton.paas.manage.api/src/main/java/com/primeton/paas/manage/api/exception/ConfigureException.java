/**
 * 
 */
package com.primeton.paas.manage.api.exception;

import com.primeton.paas.exception.api.PaasException;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ConfigureException extends PaasException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -156388772558599235L;

	public ConfigureException(String message, Object[] param,
			Throwable cause) {
		super(message, param, cause);
	}

	public ConfigureException(String message, Object[] param) {
		super(message, param);
	}

	public ConfigureException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigureException(String message) {
		super(message);
	}

	public ConfigureException(Throwable cause) {
		super(cause);
	}
	
}
