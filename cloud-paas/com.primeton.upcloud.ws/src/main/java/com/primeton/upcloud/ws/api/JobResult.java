/**
 * 
 */
package com.primeton.upcloud.ws.api;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class JobResult {
	
	public static final String STATUS_OK = "OK";
	public static final String STATUS_ERROR = "ERROR";

	private String id;
	private String status;
	private String message;
	
	/**
	 * 
	 */
	public JobResult() {
		super();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "JobResult [id=" + id + ", status=" + status + ", message="
				+ message + "]";
	}
	
}
