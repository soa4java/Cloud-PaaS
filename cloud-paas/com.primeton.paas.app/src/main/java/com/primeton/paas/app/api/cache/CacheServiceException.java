/**
 * 
 */
package com.primeton.paas.app.api.cache;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class CacheServiceException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5041811399382958735L;
	
	public CacheServiceException() {
		super();
	}

	public CacheServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheServiceException(String message) {
		super(message);
	}

	public CacheServiceException(Throwable cause) {
		super(cause);
	}
	
}
