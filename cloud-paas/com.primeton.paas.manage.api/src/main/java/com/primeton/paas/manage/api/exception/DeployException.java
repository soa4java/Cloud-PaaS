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
public class DeployException extends PaasException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1491526146510871522L;

	public DeployException(String message, Object[] param, Throwable cause) {
		super(message, param, cause);
	}

	public DeployException(String message, Object[] param) {
		super(message, param);
	}

	public DeployException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeployException(String message) {
		super(message);
	}

	public DeployException(Throwable cause) {
		super(cause);
	}


}
