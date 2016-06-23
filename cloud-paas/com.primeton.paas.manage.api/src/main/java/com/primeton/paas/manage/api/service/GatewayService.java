/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * 网关服务. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class GatewayService extends AbstractService {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4825196078015094733L;
	
	public static final String TYPE = "Gateway";
	
	private static final String PRE_SERVERS = "preServers";
	private static final String MAX_CONNECTION = "maxConnection";

	/**
	 * Default. <br>
	 */
	public GatewayService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPreServers() {
		return getValue(PRE_SERVERS);
	}
	
	/**
	 * 
	 * @param preServers
	 */
	public void setPreServers(String preServers) {
		setValue(PRE_SERVERS, preServers);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxConnection() {
		return getValue(MAX_CONNECTION, 30000);
	}
	
	/**
	 * 
	 * @param maxConnection
	 */
	public void setMaxConnection(int maxConnection) {
		setValue(MAX_CONNECTION, maxConnection);
	}

}
