/**
 * 
 */
package com.primeton.paas.collect.server;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface Constants {
	
	String SERVER_HOME = "server_home";
	String GROUP_NAME = "group_name";
	String SERVER_NAME = "server_name";
	String RUN_MODE = "run_mode";
	String SERVER_IP = "server_ip";

	/*** (queue)/(exchange), Example: queue1,exchange1,... **/
	String MQ_DESTS = "mq_dests";
	/***(queue)/(exchange), Example: queue, exchange, ... **/
	String MQ_TYPES = "mq_types";
	String MQ_TYPE_QUEUE = "queue";
	String MQ_TYPE_EXCHANGE = "exchange";
	
	String MQ_SERVER = "mq_server";
	
	String LOG_ROOT = "log_root";
	
	/*** log4j appender buffer size (B) ***/
	String APPENDER_BUFFER = "appender_buffer";
	
}
