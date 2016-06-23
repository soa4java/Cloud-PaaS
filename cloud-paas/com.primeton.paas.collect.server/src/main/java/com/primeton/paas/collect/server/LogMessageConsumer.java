/**
 * 
 */
package com.primeton.paas.collect.server;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.collect.common.StringUtil;
import com.primeton.paas.collect.rabbitmq.MessageConsumer;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class LogMessageConsumer extends MessageConsumer {
	
	private static ILogger logger = LoggerFactory.getLogger(LogMessageConsumer.class);

	/**
	 * @param connName
	 * @param queueName
	 * @param timeout
	 */
	public LogMessageConsumer(String connName, String queueName, long timeout) {
		super(connName, queueName, timeout);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.collect.rabbitmq.MessageConsumer#doMessage(byte[])
	 */
	public void doMessage(byte[] message) {
		if (message == null || message.length == 0) {
			return;
		}
		
		String log = StringUtil.toString(message);
		LogBuffer.cache(log);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Put <----------------------------------------------\n" 
				+ log + "\n----------------------------------------------> to buffer success.");
		}
	}

}
