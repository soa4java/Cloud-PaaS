/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface Constants {
	
	/** prefix **/
	String PREFIX = "PAAS.MONITOR.";
	
	//	String QUEUE_GROUP = PREFIX + "QueueGroup";
	String MONITOR_EXCHANGE = PREFIX + "Exchange";
	
	String MQSERVER = PREFIX + "MQServer";
	
	String CEPMQSERVER = PREFIX + "CEPMQServer";
	
	String STATUS = PREFIX + "Status";
	
	/** MQClient name **/
	String MQCLIENT_NAME = "monitor";
	
	String CEP_INPUTTER = PREFIX +  "CEPInputter";
	String CEP_INPUT_TYPE = PREFIX +  "CEPInputType";
	String CEP_INPUT_EXCHANGE = "exchange";
	String CEP_INPUT_QUEUE = "queue";
	
	String EVENT_NAME = "eventName";
	String APP_NAME = "appName";
	String INSTANCE_ID = "instanceId";
	String CLUSTER_ID = "clusterId";
	
	String EVENT_APPMONITOR = "appMonitorEvent";
	String EVENT_SERVICEMONITOR = "serviceMonitorEvent";
	
}
