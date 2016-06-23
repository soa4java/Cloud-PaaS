/**
 * 
 */
package com.primeton.paas.mail.server.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class MyAuthenticator extends Authenticator {

	private String username;
	private String password;

	/**
	 * 
	 * @param username
	 * @param password
	 */
	public MyAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * 
	 */
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.username, this.password);
	}

}
