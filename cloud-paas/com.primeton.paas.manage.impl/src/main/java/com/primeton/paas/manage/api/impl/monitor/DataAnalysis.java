/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.gocom.cloud.cesium.mqclient.api.MQClient;
import org.gocom.cloud.cesium.mqclient.api.Message;
import org.gocom.cloud.cesium.mqclient.api.MessageListener;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.cep.model.EventMessage;
import com.primeton.paas.cesium.agent.MonitorMessage;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.monitor.EventListener;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DataAnalysis implements MessageListener, Constants {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DataAnalysis.class);
	
	private static List<EventListener> listeners = new ArrayList<EventListener>();
	
	/**
	 * Load all. <br>
	 */
	static {
		ServiceLoader<EventListener> loader = ServiceLoader.load(EventListener.class);
		if (loader != null) {
			Iterator<EventListener> iterator = loader.iterator();
			while (iterator.hasNext()) {
				EventListener listener = iterator.next();
				if (listener != null) {
					listeners.add(listener);
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public DataAnalysis() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.gocom.cloud.cesium.mqclient.api.MessageListener#handleMessage(org.gocom.cloud.cesium.mqclient.api.Message)
	 */
	public void handleMessage(Message<?> message) {
		if (message == null || !(message instanceof MonitorMessage)) {
			return;
		}
		MonitorMessage msg = (MonitorMessage)message;
		Map<String, Object> data = msg.getBody();
		if (data == null || data.isEmpty()) {
			return;
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug(msg + " come in.");
		}
		
		for (EventListener listener : listeners) {
			handleEvent(listener, copy(data));
		}
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
	private static void handleEvent(EventListener listener, Map<String, Object> event) {
		if (listener == null || event == null) {
			return;
		}
		long begin = System.currentTimeMillis();
		if (logger.isDebugEnabled()) {
			logger.debug("Begin handle event " + event + ".");
		}
		Map<String, Object> result = listener.handle(event);
		if (result == null || result.isEmpty()) {
			return;
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
	
}
