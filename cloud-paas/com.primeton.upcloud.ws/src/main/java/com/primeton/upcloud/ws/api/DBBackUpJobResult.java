/**
 * 
 */
package com.primeton.upcloud.ws.api;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class DBBackUpJobResult {
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_FAIL = "FAIL";
	
	private String id;
	private String status;
	private String message;
	private DBBackUp dbBackup;
	
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
	

	public DBBackUp getDbBackup() {
		return dbBackup;
	}

	public void setDbBackup(DBBackUp dbBackup) {
		this.dbBackup = dbBackup;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "DBBackUpJobResult [id=" + id + ", status=" + status + ", message="
				+ message + ", dbBackup=" + dbBackup + "]";
	}
	
}
