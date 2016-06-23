/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import java.util.Map;
import java.util.UUID;

import org.gocom.cloud.cesium.common.api.util.JsonSerializerUtil;
import org.gocom.cloud.cesium.manage.runtime.api.model.MQServer;
import org.gocom.cloud.cesium.mqclient.api.MQClient;
import org.gocom.cloud.cesium.mqclient.api.MQClientFactory;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.cesium.mqclient.util.ConnectionUtil;
import org.gocom.cloud.common.logger.api.ILogger;

import com.rabbitmq.client.Connection;
import com.primeton.paas.common.spi.AbstractStartupListener;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.VariableManagerFactory;
import com.primeton.paas.manage.api.manager.IVariableManager;

/**
 *
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class StartupListener extends AbstractStartupListener implements Constants {

	private ServerContext context = ServerContext.getContext();
	
	private static ILogger logger = ManageLoggerFactory.getLogger(StartupListener.class);
	
	private static final String WARNNING_EXCHANGE = "warnMonitorExchange";
	private static final String WARNNING_SERVICE_EXCHANGE = "warnServiceMonitorExchange";

	private static final String AVG_EXCHANGE = "avgMonitorExchange";
	private static final String AVG_SERVICE_EXCHANGE = "avgServiceMonitorExchange";
	
	private String warnQueueName;
	private String avgQueueName;
	
	private String avgServiceQueueName;
	private String warnServiceQueueName;
	
	private static final String MONITOR_ENABLE_KEY = "monitor_enable";
	
	private boolean isMonitorEnable = true;
	
	private Connection connection = null; 
	
	private static final int THREAD_SIZE = 15;
	
	/**
	 * Default. <br>
	 */
	public StartupListener() {
		super();
		IVariableManager varManager = VariableManagerFactory.getManager();
		isMonitorEnable = varManager.getBoolValue(MONITOR_ENABLE_KEY, true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.common.spi.AbstractStartupListener#doStart()
	 */
	@SuppressWarnings("unchecked")
	protected void doStart() {
		if (! isMonitorEnable)
			return;
		
		HostDataBuffer.start();
		AppDataBuffer.start();
		ServiceDataBuffer.start();
		
		// Create MQClient for monitor
		String value0 = VariableUtil.getValue(MQSERVER, null);
		if (value0 == null) {
			throw new RuntimeException("Variable [" + MQSERVER + "] not found.");
		}
		MQClient mqClient= null;
		try {
			Object obj = JsonSerializerUtil.deserialize(value0);
			Map<String, String> args = null;
			if (obj instanceof Map) {
				args = (Map<String, String>)JsonSerializerUtil.deserialize(value0);
			} else if (obj instanceof MQServer) {
				args = ((MQServer)obj).getAttrs();
			}
			
			mqClient = MQClientFactory.createMQClient(MQCLIENT_NAME, args);
			connection = ConnectionUtil.getConnection(args);
		} catch (Throwable t) {
			logger.error(t);
		}
		
		if (mqClient == null) {
			throw new RuntimeException("Can not create MQClient, please check variable [" + MQSERVER + "].");
		}
		
		String exchange = VariableUtil.getValue(MONITOR_EXCHANGE, "agentMonitorExchange");
		context.setExchange(exchange);

		// CEP
		String inputType = VariableUtil.getValue(CEP_INPUT_TYPE, CEP_INPUT_EXCHANGE);
		context.setCEPInputType(inputType);
		String inputter = VariableUtil.getValue(CEP_INPUTTER, "EventEntrance");
		context.setCEPInputter(inputter);
		
		try {
			if (CEP_INPUT_EXCHANGE.equals(inputType)) {
				mqClient.createExchange(inputter, "fanout", false);
			} else {
				mqClient.createQueue(inputter, false, true, true);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		
		String tempQueue = UUID.randomUUID().toString();
		context.setTempQueue(tempQueue);
		try {
			mqClient.createExchange(context.getExchange(), "fanout", false);
			mqClient.createQueue(tempQueue, false, false, true);
			mqClient.queueBindExchange(context.getExchange(), tempQueue, "", null);
			//	mqClient.subscribeQueue(tempQueue, new DataAnalysis());
			for (int i = 0; i < THREAD_SIZE; i++) {
				Thread t = new Thread(new ReceiveMessageTask(connection, tempQueue));
				t.setDaemon(true);
				t.setName("MyReceiveMessageThread-"+i);
				t.start();
			}
		} catch (Exception e) {
			logger.error(e);
		}
		
		avgQueueName = UUID.randomUUID().toString();
		try {
			mqClient.createExchange(AVG_EXCHANGE, "fanout", false);
			mqClient.createQueue(avgQueueName, false, true, true);
			// bind
			mqClient.queueBindExchange(AVG_EXCHANGE, avgQueueName, "", null);
			// subscribe
			mqClient.subscribeQueue(avgQueueName, new AppMonitorDataListener());
		} catch (MessageException e) {
			logger.error(e);
		}
		
		warnQueueName = UUID.randomUUID().toString();
		try {
			mqClient.createExchange(WARNNING_EXCHANGE, "fanout", false); //$NON-NLS-1$
			mqClient.createQueue(warnQueueName, false, true, true);
			// bind
			mqClient.queueBindExchange(WARNNING_EXCHANGE, warnQueueName, "", null);
			// subscribe
			mqClient.subscribeQueue(warnQueueName, new AppStretchEventListener());
		} catch (MessageException e) {
			logger.error(e);
		}
		
		avgServiceQueueName = UUID.randomUUID().toString();
		try {
			mqClient.createExchange(AVG_SERVICE_EXCHANGE, "fanout", false); //$NON-NLS-1$
			mqClient.createQueue(avgServiceQueueName, false, true, true);
			// bind
			mqClient.queueBindExchange(AVG_SERVICE_EXCHANGE, avgServiceQueueName, "", null);
			// subscribe
			mqClient.subscribeQueue(avgServiceQueueName, new ServiceMonitorDataListener());
		} catch (MessageException e) {
			logger.error(e);
		}

		warnServiceQueueName = UUID.randomUUID().toString();
		try {
			mqClient.createExchange(WARNNING_SERVICE_EXCHANGE, "fanout", false);
			mqClient.createQueue(warnServiceQueueName, false, true, true);
			// bind
			mqClient.queueBindExchange(WARNNING_SERVICE_EXCHANGE, warnServiceQueueName, "", null);
			// subscribe
			mqClient.subscribeQueue(warnServiceQueueName, new ServiceUpperEventListener());
		} catch (MessageException e) {
			logger.error(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.common.spi.AbstractStartupListener#doStop()
	 */
	protected void doStop() {
		if (! isMonitorEnable)
			return;
		
		MQClient mqClient = context.getMQClient();
		if (mqClient == null) {
			return;
		}
		
		try {
			mqClient.destroyQueue(avgQueueName);
		} catch (MessageException e) {
			logger.error(e);
		}
		
		try {
			mqClient.destroyQueue(warnQueueName);
		} catch (MessageException e) {
			logger.error(e);
		}
		
		try {
			mqClient.destroyQueue(context.getTempQueue());
		} catch (MessageException e) {
			logger.error(e);
		}
		
		HostDataBuffer.stop();
		AppDataBuffer.stop();
		ServiceDataBuffer.stop();
	}
	
}
