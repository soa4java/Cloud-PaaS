/**
 * 
 */
package com.primeton.paas.openapi.admin.config;

import com.primeton.paas.openapi.base.uitl.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConfigurationRuntimeException extends RuntimeException {

	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 5424726790697879171L;

	Object[] params = new Object[0];

	public ConfigurationRuntimeException(String msg) {
		super(msg);
	}

	public ConfigurationRuntimeException(String msg, Object[] params) {
		super(msg);
		this.params = params;
	}

	public ConfigurationRuntimeException(Throwable cause) {
		super(cause);
	}

	public ConfigurationRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ConfigurationRuntimeException(String msg, Object[] params,
			Throwable cause) {
		super(msg, cause);
		this.params = params;
	}

	public String getMessage() {
		return StringUtil.format(super.getMessage(), params);
	}

}
