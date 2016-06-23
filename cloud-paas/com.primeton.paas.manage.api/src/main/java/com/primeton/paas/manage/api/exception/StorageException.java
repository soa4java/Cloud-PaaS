/**
 * 
 */
package com.primeton.paas.manage.api.exception;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class StorageException extends Exception {

	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = 5004670174649531856L;
	
	/**
	 * Storage do not exist. <br>
	 */
	public static final String NOT_EXIST_EXCEPTION = "Storage do not exist.";
	/**
	 * Storage already exist. <br>
	 */
	public static final String ALREADY_EXIST_EXCEPTION = "Storage already exist.";
	
	/**
	 * Storage mount exception. <br>
	 */
	public static final String MOUNT_EXCEPTION = "Storage mount exception.";
	/**
	 * Storage unmount exception. <br>
	 */
	public static final String UNMOUNT_EXCEPTION = "Storage unmount exception.";

	/**
	 * 
	 */
	public StorageException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public StorageException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public StorageException(Throwable cause) {
		super(cause);
	}

}
