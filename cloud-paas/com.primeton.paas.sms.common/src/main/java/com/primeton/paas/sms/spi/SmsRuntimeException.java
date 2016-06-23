/**
 * 
 */
package com.primeton.paas.sms.spi;

import com.primeton.paas.exception.api.PaasRuntimeException;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public class SmsRuntimeException extends PaasRuntimeException {
	
	private static final long serialVersionUID = 2552744130642516948L;

	/**
	 * @param cause
	 */
	public SmsRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SmsRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public SmsRuntimeException(String message, Object[] param, Throwable cause) {
		super(message, param, cause);
	}

	/**
	 * @param message
	 */
	public SmsRuntimeException(String message) {
		super(message);
	}
	
	public SmsRuntimeException(String message, Object[] param) {
		super(message, param);
	}

}

