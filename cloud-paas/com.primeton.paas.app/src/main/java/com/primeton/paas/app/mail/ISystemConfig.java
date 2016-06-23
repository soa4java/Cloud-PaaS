/**
 * 
 */
package com.primeton.paas.app.mail;

import org.gocom.cloud.cesium.mqclient.api.MQServer;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public interface ISystemConfig {
	
	/**
	 * 
	 * @return
	 */
	String[] getQueueGroup();
	
	/**
	 * 
	 * @return
	 */
	MQServer getMQServer();
	
}
