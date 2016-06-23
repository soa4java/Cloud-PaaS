/**
 * 
 */
package com.primeton.paas.cep.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.api.model.Variable;
import org.gocom.cloud.cesium.common.api.util.JsonSerializerUtil;
import org.gocom.cloud.cesium.manage.runtime.api.model.MQServer;
import org.gocom.cloud.cesium.manage.runtime.api.naming.NamingUtil;
import org.gocom.cloud.cesium.mqclient.api.MQClient;
import org.gocom.cloud.cesium.mqclient.api.MQClientFactory;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.cep.util.StringUtil;


/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ServerContext implements Constants {
	
	private static ILogger logger = LoggerFactory.getLogger(ServerContext.class);
	
	private static ServerConfig cfg = new ServerConfig(); 
	
	private ServerContext() {
		super();
	}
	
	private static ServerContext INSTANCE = new ServerContext();
	
	/**
	 * Singleton instance <br>
	 * 
	 * @return ServerContext
	 */
	public static ServerContext getContext() {
		return INSTANCE;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getServerHome() {
		return cfg.get(SERVER_HOME);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getGroupName() {
		return cfg.get(GROUP_NAME);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEngineName() {
		return cfg.get(ENGINE_NAME);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRunMode() {
		return cfg.get(RUN_MODE, Engine.MODE_SLAVE);
	}
	
	/**
	 * 
	 * @param runMode
	 */
	public void setRunMode(String runMode) {
		if (Engine.MODE_MASTER.equals(runMode) || Engine.MODE_SLAVE.equals(runMode)) {
			cfg.set(RUN_MODE, runMode);
		}
		System.out.println("Error run mode [" + runMode + "]");
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEngineIp() {
		return cfg.get(ENGINE_IP, "127.0.0.1");
	}
	
	/**
	 * 
	 * @return
	 */
	public ServerConfig getServerConfig() {
		return cfg;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MQServer getMQServer() {
		String name = cfg.get(MQ_SERVER);
		if (StringUtil.isNotEmpty(name)) {
			Variable var = NamingUtil.lookupVariable(name);
			if (var != null && StringUtil.isNotEmpty(var.getValue())) {
				String json = var.getValue();
				try {
					Object obj = JsonSerializerUtil.deserialize(json);
					if (obj instanceof MQServer) {
						return (MQServer)obj;
					} else if (obj instanceof Map) {
						Map<String, String> args = (Map<String, String>)obj;
						MQServer mqServer = new MQServer();
						mqServer.getAttrs().putAll(args);
						return mqServer;
					}
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public MQClient getMQClient() {
		MQClient mqClient = MQClientFactory.getMQClient();
		if (mqClient == null) {
			MQServer server = getMQServer();
			if (server != null) {
				mqClient = MQClientFactory.createMQClient(server.getAttrs());
			}
		}
		return mqClient;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getMQDests() {
		List<String> list = new ArrayList<String>();
		String mqDests = cfg.get(MQ_DESTS);
		if (mqDests == null) {
			return list;
		}
		String[] array = mqDests.split(",");
		if (array != null && array.length > 0) {
			return Arrays.asList(array);
		}
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getMQTypes() {
		List<String> list = new ArrayList<String>();
		String mqTypes = cfg.get(MQ_TYPES);
		if (mqTypes == null) {
			return list;
		}
		String[] array = mqTypes.split(",");
		if (array != null && array.length > 0) {
			return Arrays.asList(array);
		}
		return list;
	}

}
