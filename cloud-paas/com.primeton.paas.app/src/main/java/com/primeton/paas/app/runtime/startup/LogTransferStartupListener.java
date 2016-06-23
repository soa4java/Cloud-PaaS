/**
 * 
 */
package com.primeton.paas.app.runtime.startup;

import java.util.Map;

import org.gocom.cloud.cesium.common.api.model.Variable;
import org.gocom.cloud.cesium.common.api.util.JsonSerializerUtil;
import org.gocom.cloud.cesium.manage.runtime.api.model.MQServer;
import org.gocom.cloud.cesium.manage.runtime.api.naming.NamingUtil;

import com.primeton.paas.app.AppConstants;
import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.log.LogBuffer;
import com.primeton.paas.app.log.LogCollectContext;
import com.primeton.paas.app.runtime.IRuntimeListener;
import com.primeton.paas.app.runtime.RuntimeEvent;
import com.primeton.paas.collect.rabbitmq.AMQConnectionFactory;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-12-20
 *
 */
public class LogTransferStartupListener implements IRuntimeListener {
	
	public static final String PREFIX = "PAAS.LOG.";
	public static final String QUEUE_GROUP = PREFIX + "QueueGroup";
	public static final String MQSERVER = PREFIX + "MQServer";

	/**
	 * 
	 */
	public LogTransferStartupListener() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.runtime.IRuntimeListener#start(com.primeton.paas.app.runtime.RuntimeEvent)
	 */
	@SuppressWarnings("unchecked")
	public void start(RuntimeEvent envent) {
		ServerContext serverContext = ServerContext.getInstance();
		int mode = serverContext.getRunMode();
		// Development Mode
		if(AppConstants.RUN_MODE_SIMULATOR == mode) {
			return;
		}
		
		// Product Mode
		Variable var1 = NamingUtil.lookupVariable(QUEUE_GROUP);
		if(var1 != null) {
			String val = var1.getValue();
			if(val != null && val.trim().length() > 0) {
				String[] queues = val.split(",");
				LogCollectContext.getContext().setQueues(queues);
			}
		}
		
		Variable var = NamingUtil.lookupVariable(MQSERVER);
		if(var != null) {
			String val = var.getValue();
			MQServer server = null;
			if(val != null && val.trim().length() > 0) {
				try {
					Object obj = (Map<String, String>)JsonSerializerUtil.deserialize(val);
					if (obj instanceof Map) {
						Map<String, String> args = (Map<String, String>)obj;
						server = new MQServer();
						server.addAttrs(args);
					} else if (obj instanceof MQServer) {
						server = (MQServer)obj;
					}
					LogCollectContext.getContext().setMQServer(server);
					
				} catch (Exception e) {
					 e.printStackTrace();
				}
			}
			if (server == null) {
				throw new RuntimeException("MQServer not found for collect application log.");
			}
		}
		
		MQServer server = LogCollectContext.getContext().getMQServer();
		try {
			AMQConnectionFactory.getConnection(AMQConnectionFactory.DEFAULT, true, server.getUrl(), 
					server.getUserName(), server.getPassword(), server.getVhost(), server.getConnectionTimeout());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		LogBuffer.start();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.runtime.IRuntimeListener#stop(com.primeton.paas.app.runtime.RuntimeEvent)
	 */
	public void stop(RuntimeEvent envent) {
		ServerContext serverContext = ServerContext.getInstance();
		int mode = serverContext.getRunMode();
		// Development Mode
		if(AppConstants.RUN_MODE_SIMULATOR == mode) {
			return;
		}
		
		LogBuffer.stop();
		
		// Product Mode
		AMQConnectionFactory.clear();
	}

}
