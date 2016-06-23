/**
 * 
 */
package com.primeton.paas.app.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Java Mail. <br>
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class EmailAuthenticator extends Authenticator {
	
	private String username;
	private String password;
	
	/**
	 * 
	 * @param username
	 * @param password
	 */
	public EmailAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.mail.Authenticator#getPasswordAuthentication()
	 */
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.username, this.password);
	}
}
