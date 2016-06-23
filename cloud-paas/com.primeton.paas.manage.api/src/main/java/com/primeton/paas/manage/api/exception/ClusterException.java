/**
 * 
 */
package com.primeton.paas.manage.api.exception;

import com.primeton.paas.exception.api.PaasException;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ClusterException extends PaasException {

	private static final long serialVersionUID = 9022579921987769881L;
	
	public static final String CLUSTER_NOT_EXISTS = "Cluster is not exists.";
	public static final String CLUSTER_EXISTS = "Cluster is already exists.";

	/**
	 * @param message
	 * @param cause
	 */
	public ClusterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ClusterException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ClusterException(Throwable cause) {
		super(cause);
	}
	
}
