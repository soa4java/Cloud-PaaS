/**
 * 
 */
package com.primeton.paas.collect.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.api.model.Variable;
import org.gocom.cloud.cesium.common.api.util.JsonSerializerUtil;
import org.gocom.cloud.cesium.manage.runtime.api.model.MQServer;
import org.gocom.cloud.cesium.manage.runtime.api.naming.NamingUtil;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.collect.common.StringUtil;


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
	
	public String getServerHome() {
		return cfg.get(SERVER_HOME);
	}
	
	public String getGroupName() {
		return cfg.get(GROUP_NAME);
	}
	
	public String getServerName() {
		return cfg.get(SERVER_NAME);
	}
	
	public String getRunMode() {
		return cfg.get(RUN_MODE, Server.MODE_SLAVE);
	}
	
	public void setRunMode(String runMode) {
		if (Server.MODE_MASTER.equals(runMode) || Server.MODE_SLAVE.equals(runMode)) {
			cfg.set(RUN_MODE, runMode);
		}
	}
	
	public String getServerIp() {
		return cfg.get(SERVER_IP, "127.0.0.1");
	}
	
	public ServerConfig getServerConfig() {
		return cfg;
	}
	
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
	
	public String getLogRoot() {
		return cfg.get(LOG_ROOT);
	}
	
	public int getAppenderBuffer() {
		return cfg.get(APPENDER_BUFFER, 1024000); // 1000KB
	}

}
