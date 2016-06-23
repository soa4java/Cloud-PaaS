/**
 * 
 */
package com.primeton.paas.app.runtime.impl;

import java.io.File;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.config.Configuration;
import com.primeton.paas.app.config.ConfigurationManager;
import com.primeton.paas.app.runtime.IRuntimeListener;
import com.primeton.paas.app.runtime.IRuntimeListenerManager;
import com.primeton.paas.app.runtime.IRuntimeListenerRegistry;
import com.primeton.paas.app.runtime.RuntimeEvent;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RuntimeListenerManagerImpl implements IRuntimeListenerManager {
	
	private static ILogger logger = SystemLoggerFactory.getLogger(RuntimeListenerManagerImpl.class);

	private RuntimeEvent runtimeEvent = null;

	public RuntimeListenerManagerImpl() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.runtime.IRuntimeListenerManager#startListener()
	 */
	public void startListener() {
		runtimeEvent = prepareRuntimeEvent();
		List<IRuntimeListener> listeners = IRuntimeListenerRegistry.INSTANCE.getRuntimeListeners();
		for (IRuntimeListener listener : listeners) {
			try {
				long start = System.currentTimeMillis();
				if (logger.isDebugEnabled())
					logger.debug("Start the runtime listener '" + listener.getClass().getCanonicalName() + "'");

				listener.start(runtimeEvent);
				long end = System.currentTimeMillis();
				if (logger.isDebugEnabled())
					logger.debug("The runtime listener '" + listener.getClass().getCanonicalName() + "' end,in " + (end - start) + " ms.");

			} catch (Throwable e) {
				logger.error("Runtime Listener start error. ", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.runtime.IRuntimeListenerManager#stopListener()
	 */
	public void stopListener() {
		List<IRuntimeListener> listeners = IRuntimeListenerRegistry.INSTANCE.getRuntimeListeners();
		for (int i = listeners.size() - 1; i >= 0; i--) {
			IRuntimeListener listener = listeners.get(i);
			try {
				listener.stop(runtimeEvent);
			} catch (Throwable e) {
				logger.error("Runtime Listener stop error. ", e);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	private RuntimeEvent prepareRuntimeEvent() {
		RuntimeEvent runtimeEvent = new RuntimeEvent();

		String configDirPath = ServerContext.getInstance().getConfigDirPath();
		for (String configType : ConfigurationManager.getConfigFileTypes()) {
			String configFileName = ConfigurationManager.getConfigFileName(configType);
			String configFilePath = configDirPath + File.separator + configFileName;
			Configuration config = Configuration.initConfiguration(configFilePath);
			runtimeEvent.addConfiguration(configType, config);
			runtimeEvent.setServerContext(ServerContext.getInstance());
		}
		return runtimeEvent;
	}
	
}
