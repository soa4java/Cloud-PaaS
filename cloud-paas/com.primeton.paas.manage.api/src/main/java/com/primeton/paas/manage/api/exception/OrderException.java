/**
 * 
 */
package com.primeton.paas.manage.api.exception;

import com.primeton.paas.exception.api.PaasException;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class OrderException extends PaasException {

	private static final long serialVersionUID = -2179736436814489701L;

	public OrderException(String message, Object[] param, Throwable cause) {
		super(message, param, cause);
	}

	public OrderException(String message, Object[] param) {
		super(message, param);
	}

	public OrderException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrderException(String message) {
		super(message);
	}

	public OrderException(Throwable cause) {
		super(cause);
	}
	
}
