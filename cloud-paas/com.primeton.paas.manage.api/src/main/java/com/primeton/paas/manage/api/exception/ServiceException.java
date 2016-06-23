/**
 * 
 */
package com.primeton.paas.manage.api.exception;

import com.primeton.paas.exception.api.PaasException;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ServiceException extends PaasException {

	private static final long serialVersionUID = -8707659607777542350L;
	
	public static final String SERVICE_INSTANCE_NOT_EXISTS = "ServiceInstance is not exists.";
	public static final String SERVICE_INSTANCE_EXISTS = "ServiceInstance is already exists.";

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServiceException(Throwable cause) {
		super(cause);
	}

	
}
