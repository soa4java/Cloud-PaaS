/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import org.gocom.cloud.cesium.mqclient.api.MQClient;
import org.gocom.cloud.cesium.mqclient.api.MQClientFactory;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ServerContext implements Constants {
	
	private String cepInputType = CEP_INPUT_EXCHANGE;
	private String cepInputter = "EventEntrance";
	//	private String[] queueGroup;
	private String exchange;
	private String tempQueue;
	
	private static ServerContext context = new ServerContext();
	
	private ServerContext() {
		super();
	}
	
	public static ServerContext getContext() {
		return context;
	}
	
	public MQClient getMQClient() {
		return MQClientFactory.getMQClient(MQCLIENT_NAME);
	}
	
	public String getCEPInputType() {
		return cepInputType;
	}
	
	public void setCEPInputType(String type) {
		this.cepInputType = type;
	}
	
	public String getCEPInputter() {
		return cepInputter;
	}
	
	public void setCEPInputter(String inputter) {
		this.cepInputter = inputter;
	}

	/**
	 * @return the exchange
	 */
	public String getExchange() {
		return exchange;
	}

	/**
	 * @param exchange the exchange to set
	 */
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	/**
	 * @return the tempQueue
	 */
	public String getTempQueue() {
		return tempQueue;
	}

	/**
	 * @param tempQueue the tempQueue to set
	 */
	public void setTempQueue(String tempQueue) {
		this.tempQueue = tempQueue;
	}

	/*
	public String[] getQueueGroup() {
		return queueGroup;
	}

	public void setQueueGroup(String[] queueGroup) {
		this.queueGroup = queueGroup;
	}
	*/
	
}
