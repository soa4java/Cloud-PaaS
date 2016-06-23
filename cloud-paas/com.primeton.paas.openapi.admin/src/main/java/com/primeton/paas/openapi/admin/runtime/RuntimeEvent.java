/**
 * 
 */
package com.primeton.paas.openapi.admin.runtime;

import com.primeton.paas.openapi.admin.ServerContext;
import com.primeton.paas.openapi.admin.config.Configuration;
import com.primeton.paas.openapi.admin.config.ConfigurationManager;


/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RuntimeEvent{
	
	private ServerContext serverContext = null;
	
	private ConfigurationManager configManager = ConfigurationManager
			.getInstance();
	
	public ServerContext getServerContext(){
		return serverContext;
	}
	
	public void setServerContext(ServerContext serverContext) {
		this.serverContext = serverContext;
	}
	
	public void addConfiguration(String configFileType,Configuration config) {
		configManager.register(configFileType, config);
	}

	public Configuration getConfiguration(String configFileType) {
		return configManager.getConfiguration(configFileType);
	}

	public Configuration[] getConfigurations() {
		return configManager.getConfigurations();
	}
	
}
