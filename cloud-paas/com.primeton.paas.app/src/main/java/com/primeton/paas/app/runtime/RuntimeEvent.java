/**
 * 
 */
package com.primeton.paas.app.runtime;

import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.config.Configuration;
import com.primeton.paas.app.config.ConfigurationManager;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RuntimeEvent{
	
	private ServerContext serverContext = null;
	
	private ConfigurationManager configManager=ConfigurationManager.getInstance();
	
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
