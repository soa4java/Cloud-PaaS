/**
 * 
 */
package com.primeton.paas.cep.engine;

import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.Message;
import org.gocom.cloud.cesium.mqclient.api.MessageListener;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.cep.model.EventMessage;
import com.primeton.paas.cep.spi.EPSException;
import com.primeton.paas.cep.spi.EPSInstanceManager;
import com.primeton.paas.cep.spi.EPSInstanceManagerFactory;
import com.primeton.paas.cep.util.StringUtil;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EventMessageListener implements MessageListener, Constants {
	
	private EPSInstanceManager manager;
	
	private static ILogger logger = LoggerFactory.getLogger(EventMessageListener.class);

	/**
	 * 
	 */
	public EventMessageListener() {
		super();
		manager = EPSInstanceManagerFactory.getManager();
	}

	/**
	 * @param manager
	 */
	public EventMessageListener(EPSInstanceManager manager) {
		super();
		this.manager = manager;
	}

	/* (non-Javadoc)
	 * @see org.gocom.cloud.cesium.mqclient.api.MessageListener#handleMessage(org.gocom.cloud.cesium.mqclient.api.Message)
	 */
	public void handleMessage(Message<?> message) {
		if (message == null) {
			return;
		}
		if (message instanceof EventMessage) {
			EventMessage eventMessage = (EventMessage)message;
			Map<String, String> headers = eventMessage.getHeaders();
			if (headers != null && !headers.isEmpty()) {
				final String eventName = headers.get(EVENT_NAME);
				if (StringUtil.isNotEmpty(eventName)) {
					if (eventMessage.getBody() != null && !eventMessage.getBody().isEmpty()) {
						Map<String, Object> event = eventMessage.getBody();
						// throw it to Engine
						try {
							manager.sendEvent(event, eventName);
						} catch (EPSException e) {
							logger.error(e);
						}
					}
					
				}
			}
		} else {
			logger.warn("Ignore message : " + message + ".");
		}

	}

}
