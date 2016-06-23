/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MsgQueueService extends AbstractService {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2205844212663032365L;
	
	public static final String TYPE = "MsgQueue";

	/**
	 * Default. <br>
	 */
	public MsgQueueService() {
		super();
		setType(TYPE);
		setMode(MODE_PHYSICAL);
	}
	
	private static final String VHOST = "vhost";
	private static final String USER = "user";
	private static final String PASSWORD = "password";

	/**
	 * 
	 * @return
	 */
	public String getVhost() {
		return getValue(VHOST, "/");
	}
	
	/**
	 * 
	 * @param vhost
	 */
	public void setVhost(String vhost) {
		setValue(VHOST, vhost);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUser() {
		return getValue(USER, "paas"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param user
	 */
	public void setUser(String user) {
		setValue(USER, user);
	}

	/**
	 * 
	 * @return
	 */
	public String getPassword() {
		return getValue(PASSWORD, "paas"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		setValue(PASSWORD, password);
	}
	
}
