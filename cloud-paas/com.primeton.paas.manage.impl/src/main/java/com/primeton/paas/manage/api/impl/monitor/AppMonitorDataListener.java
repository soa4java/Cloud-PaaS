/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.Message;
import org.gocom.cloud.cesium.mqclient.api.MessageListener;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.cep.model.EventMessage;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-11-18
 *
 */
public class AppMonitorDataListener implements MessageListener {

	private static final String MSG_TYPE_KEY = "MSG_TYPE";
	private static final String MSG_TYPE_VALUE = "AVG_APP_MONITOR";
	
	private static ILogger logger = ManageLoggerFactory.getLogger(AppMonitorDataListener.class);
	
	/* (non-Javadoc)
	 * @see org.gocom.cloud.cesium.mqclient.api.MessageListener#handleMessage(org.gocom.cloud.cesium.mqclient.api.Message)
	 */
	public void handleMessage(Message<?> message) {
		EventMessage msg = (EventMessage)message;
		if (logger.isDebugEnabled()) {
			logger.debug(message + " come in.");
		}
		Map<String, String> header = msg.getHeaders();
		Map<String, Object> body = msg.getBody();
		if (header == null || header.isEmpty()
				|| body == null || body.isEmpty()) {
			return;
		}
		
		String msgType = header.get(MSG_TYPE_KEY);
		
		if (StringUtil.isNotEmpty(msgType)) {
			if (MSG_TYPE_VALUE.equals(msgType)) {
				// Throw it to buffer
				AppDataBuffer.put(body);
			}
		}
	}

}
