/**
 * 
 */
package com.primeton.paas.app.log;

/**
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class UserLogData {
	
	private String type;
	private String message;
	
	/**
	 * 
	 */
	public UserLogData() {
		super();
	}
	
	/**
	 * 
	 * @param type
	 * @param message
	 */
	public UserLogData(String type, String message) {
		super();
		this.type = type;
		this.message = message;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
