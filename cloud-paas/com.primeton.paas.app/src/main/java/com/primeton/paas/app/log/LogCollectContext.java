/**
 * 
 */
package com.primeton.paas.app.log;

import org.gocom.cloud.cesium.manage.runtime.api.model.MQServer;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-12-20
 *
 */
public class LogCollectContext {
	
	private static LogCollectContext INSTANCE = new LogCollectContext();

	private LogCollectContext() {
		super();
	}
	
	private String[] queues = new String[0];
	private MQServer mqServer;
	
	/**
	 * 
	 * @return
	 */
	public static LogCollectContext getContext() {
		return INSTANCE;
	}
	
	
	public String[] getQueues() {
		return queues;
	}
	
	public void setQueues(String[] queues) {
		this.queues = queues;
	}
	
	public MQServer getMQServer() {
		return mqServer;
	}
	
	public void setMQServer(MQServer mqServer) {
		this.mqServer = mqServer;
	}

}
