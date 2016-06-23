/**
 * 
 */
package com.primeton.paas.cesium.agent;

import org.gocom.cloud.cesium.agent.AgentConfig;
import org.gocom.cloud.cesium.agent.api.StartupListener;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MonitorStartup implements StartupListener {
	
	private ILogger logger = LoggerFactory.getLogger(MonitorStartup.class);
	
	private MonitorThread thread;

	/* (non-Javadoc)
	 * @see org.gocom.cloud.cesium.agent.api.StartupListener#start(org.gocom.cloud.cesium.agent.AgentConfig)
	 */
	public void start(AgentConfig config) {
		logger.info("Start " + this + " success.");
		
		thread = new MonitorThread(config.getAgentHome(), config.getAgentIp());
		thread.setDaemon(true);
		thread.setName(MonitorThread.class.getSimpleName());
		thread.start();
	}

	/* (non-Javadoc)
	 * @see org.gocom.cloud.cesium.agent.api.StartupListener#stop(org.gocom.cloud.cesium.agent.AgentConfig)
	 */
	public void stop(AgentConfig config) {
		logger.info("Stop " + this + " success.");
		if (thread != null && thread.isRunning()) {
			thread.close();
		}
	}

}
