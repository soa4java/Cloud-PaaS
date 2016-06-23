/**
 * 
 */
package com.primeton.paas.openapi.admin.runtime.impl;

import java.io.File;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.openapi.admin.ServerContext;
import com.primeton.paas.openapi.admin.config.Configuration;
import com.primeton.paas.openapi.admin.config.ConfigurationManager;
import com.primeton.paas.openapi.admin.runtime.IRuntimeListener;
import com.primeton.paas.openapi.admin.runtime.IRuntimeListenerManager;
import com.primeton.paas.openapi.admin.runtime.IRuntimeListenerRegistry;
import com.primeton.paas.openapi.admin.runtime.RuntimeEvent;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RuntimeListenerManagerImpl implements IRuntimeListenerManager {
	
	private static ILogger logger = LoggerFactory.getLogger(RuntimeListenerManagerImpl.class);

	private RuntimeEvent runtimeEvent = null;

	public RuntimeListenerManagerImpl() {
	}

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
				if (logger.isErrorEnabled())
					logger.error("Runtime Listener start error. ", e);
			}
		}
	}

	public void stopListener() {
		List<IRuntimeListener> listeners = IRuntimeListenerRegistry.INSTANCE.getRuntimeListeners();
		for (int i = listeners.size() - 1; i >= 0; i--) {
			IRuntimeListener listener = listeners.get(i);
			try {
				listener.stop(runtimeEvent);
			} catch (Throwable e) {
				if (logger.isErrorEnabled())
					logger.error("Runtime Listener stop error. ", e);
			}
		}
	}

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
