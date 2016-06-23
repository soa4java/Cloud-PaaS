/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.gocom.cloud.cesium.mqclient.api.MQClient;
import org.gocom.cloud.cesium.mqclient.spi.ObjectSerializerException;
import org.gocom.cloud.cesium.mqclient.util.ConnectionUtil;
import org.gocom.cloud.cesium.mqclient.util.JsonSerializerUtil;
import org.gocom.cloud.cesium.mqclient.util.StringUtil;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.primeton.paas.cep.model.EventMessage;
import com.primeton.paas.cesium.agent.MonitorMessage;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.monitor.EventListener;

/**
 * 多线程接收监控消息. <br>
 * 
 * 废弃 DataAnalysis.java 该类使用管控接收方式，单线程接收，无法满足大量监控数据写入
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class ReceiveMessageTask implements Runnable, Constants {

	/*** MQ连接***/
	private Connection connection;
	/*** 消息队列名称 ***/
	private String qName;

	/*** 消息监听器 ***/
	//	List<MessageListener> listeners = new ArrayList<MessageListener>();
	/*** true:exit, false:not exit ***/
	private boolean exitFlag = false;

	private QueueingConsumer consumer;
	private Channel channel;

	// 此线程是否启动
	private boolean started = false;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(ReceiveMessageTask.class);
	private static ILogger cesiumLogger = LoggerFactory.getLogger(ReceiveMessageTask.class);
	
	private static List<EventListener> eventlisteners = new ArrayList<EventListener>();
	
	static {
		ServiceLoader<EventListener> loader = ServiceLoader.load(EventListener.class);
		if (loader != null) {
			Iterator<EventListener> iterator = loader.iterator();
			while (iterator.hasNext()) {
				EventListener listener = iterator.next();
				if (listener != null) {
					eventlisteners.add(listener);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param connection
	 * @param qName
	 */
	public ReceiveMessageTask(Connection connection, String qName) {
		this.connection = connection;
		this.qName = qName;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		long begin = System.currentTimeMillis();
		if(logger.isDebugEnabled()) {
			logger.debug("Begin start " + this + ".");
		}
		boolean isSuccess = init();
		if (!isSuccess) {
			logger.info("Start " + this + " failured.");
			return;
		}
		long end = System.currentTimeMillis();
		if (logger.isDebugEnabled()) {
			logger.debug("Start " + this + " success. Time spent " + (end-begin)/1000 + " seconds.");
		}

		this.started = true;

		QueueingConsumer.Delivery delivery = null;
		
		// 从队列中获取消息
		while (true) {
			if (exitFlag) { // exit
				ConnectionUtil.closeChannel(this.channel);
				this.started = false;
				break;
			}
			try {
				delivery = this.consumer.nextDelivery();
				if (delivery != null) {
					byte[] bytes = delivery.getBody();
					String json = StringUtil.bytes2String(bytes);
					
					Object obj = null;
					try {
						obj = JsonSerializerUtil.deserialize(json);
					} catch (ObjectSerializerException t) {
						if (logger.isErrorEnabled()) {
							logger.error(json + " can not serialize to Object.\tError message:\n" + t.getMessage());
						}
					}
					MonitorMessage msg = (MonitorMessage)obj;
					if (logger.isDebugEnabled()) {
						logger.debug(msg + " come in.");
					}
					
					Map<String, Object> data = msg.getBody();
					if (data != null && !data.isEmpty()) {
						if (cesiumLogger.isDebugEnabled()) {
							cesiumLogger.debug("msgTime: " + new Date(((Long)data.get("occur_time")).longValue()) + ",from:" + data.get("ip") );
						}
						for (EventListener listener : eventlisteners) {
							handleEvent(listener, copy(data));
						}
					}
				}
			} catch (Throwable t) {
				if (t instanceof ShutdownSignalException || t instanceof ConsumerCancelledException) {
					logger.info("Connection is already closed, " + this + " will shut down.");
					this.exitFlag = true;
					this.started = false;
					break;
				}
				logger.error(t);
			}
		}
	}
	
	/**
	 * 初始化MQ管道和消息接收器 <br>
	 * 
	 * @throws ConnectionException 
	 */
	private boolean init() {
		if(this.connection == null || !this.connection.isOpen()) {
			if(logger.isErrorEnabled()) {
				logger.error("Connection {" + this.connection + "} is null or is already closed");
			}
			return false;
		}
		
		// If message queue not found then create it
		try {
			this.channel = this.connection.createChannel();
			// if queue not exists then throw exception and channel will close [channel.queueDeclarePassive(queue)]
			// com.rabbitmq.client.ShutdownSignalException
			this.channel.queueDeclarePassive(this.qName); 
		} catch (Throwable t) {
			// queue not exists then create it
			try {
				this.channel = this.connection.createChannel();
				this.channel.queueDeclare(this.qName, false, false, true, null);
			} catch (IOException e) {
				logger.error(e);
				return false;
			}
		}
		// create consumer
		try {
			this.consumer = new QueueingConsumer(this.channel);
			this.channel.basicConsume(this.qName, true, this.consumer);
		} catch (IOException e) {
			logger.error(e);
			return false;
		}
		return true;
	}
	
	/**
	 * Map copy <br>
	 * 
	 * @param e
	 * @return
	 */
	private static Map<String, Object> copy(Map<String, Object> e) {
		if (e == null) {
			return null;
		}
		Map<String, Object> event = new HashMap<String, Object>();
		for (String key : e.keySet()) {
			event.put(key, e.get(key));
		}
		return event;
	}

	/**
	 * 
	 * @param listener
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	private static void handleEvent(EventListener listener, Map<String, Object> event) {
		if (listener == null || event == null) {
			return;
		}
		long begin = System.currentTimeMillis();
		logger.debug("Begin handle event " + event + ".");
		Map<String, Object> result = listener.handle(event);
		if (result == null || result.isEmpty()) {
			return;
		}
		
		if(result.containsKey(ServiceMonitorListener.UNIQKEY)){//包含服务监控event标记，不包含event_name
			result.remove(ServiceMonitorListener.UNIQKEY);
			//发送服务监控消息
			//发送map中的多条服务监控消息
			ServerContext context = ServerContext.getContext();
			MQClient mqClient = context.getMQClient();
			
			for (String key : result.keySet()) {
				EventMessage message = new EventMessage();
				message.putHeader(EVENT_NAME, EVENT_SERVICEMONITOR);
				Map<String,Object> body = new HashMap<String,Object>();
				body = (Map<String,Object>)result.get(key);
				
				message.setBody(body);
				
				try {
					if (CEP_INPUT_EXCHANGE.equals(context.getCEPInputType())) {
						mqClient.sendMessage(context.getCEPInputter(), "", message, false);
					} else {
						mqClient.sendMessage(context.getCEPInputter(), message, false);
					}
				} catch (Throwable t) {
					logger.error(t);
				}
			}
		}
		
		// Esper-Event 
		if (result.containsKey(EVENT_NAME)) {
			EventMessage message = new EventMessage();
			message.putHeader(EVENT_NAME, (String)result.get(EVENT_NAME));
			message.setBody(result);
			
			ServerContext context = ServerContext.getContext();
			
			MQClient mqClient = context.getMQClient();
			try {
				if (CEP_INPUT_EXCHANGE.equals(context.getCEPInputType())) {
					mqClient.sendMessage(context.getCEPInputter(), "", message, false);
				} else {
					mqClient.sendMessage(context.getCEPInputter(), message, false);
				}
			} catch (Throwable t) {
				logger.error(t);
			}
		}
		long end = System.currentTimeMillis();
		logger.debug("End handle event " + event + ". Time spent " + (end-begin)/1000L + " seconds.");
	}
	
	/**
	 * 关闭消息接收线程 <br>
	 */
	public void close() {
		logger.info(this + " will be shut down.");
		this.exitFlag = true;
	}
	
	/**
	 * 状态 <br>
	 * 
	 * @return true:运行
	 */
	public boolean isStarted() {
		return this.started;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer(ReceiveMessageTask.class.getName()).append("-").append(this.qName).toString();
	}
	
}
