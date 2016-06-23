/**
 * 
 */
package com.primeton.paas.app.config;

import com.primeton.paas.app.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConfigurationRuntimeException extends RuntimeException {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 5424726790697879171L;
	
	Object[] params = new Object[0];
	
	/**
	 * 
	 * @param msg
	 */
	public ConfigurationRuntimeException(String msg) {
		super(msg);
	}
	
	/**
	 * 
	 * @param msg
	 * @param params
	 */
	public ConfigurationRuntimeException(String msg, Object[] params) {
		super(msg);
		this.params = params;
	}
	
	/**
	 * 
	 * @param cause
	 */
	public ConfigurationRuntimeException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public ConfigurationRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	/**
	 * 
	 * @param msg
	 * @param params
	 * @param cause
	 */
	public ConfigurationRuntimeException(String msg, Object[] params, Throwable cause) {
		super(msg, cause);
		this.params = params;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return StringUtil.format(super.getMessage(), params);
	}
	
}
