/**
 * 
 */
package com.primeton.paas.cep.spi;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.cep.engine.ServerContext;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-11-11
 *
 */
public abstract class AbstractStartupListener implements StartupListener {
	
	private boolean isStarted;
	
	private static ILogger logger = LoggerFactory.getLogger(AbstractStartupListener.class);

	/**
	 * 
	 * @param context
	 */
	public abstract void doStart(ServerContext context);

	/**
	 * 
	 * @param context
	 */
	public abstract void doStop(ServerContext context);

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.StartupListener#getId()
	 */
	public String getId() {
		return AbstractStartupListener.class.getName();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.StartupListener#start(com.primeton.paas.cep.engine.ServerContext)
	 */
	public synchronized void start(ServerContext context) {
		if (isStarted) {
			logger.warn(this + " is already started.");
			return;
		}
		try {
			doStart(context);
			isStarted = true;
		} catch (Throwable t) {
			logger.error(t);
		}
	}



	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.StartupListener#stop(com.primeton.paas.cep.engine.ServerContext)
	 */
	public synchronized void stop(ServerContext context) {
		if (!isStarted) {
			logger.warn(this + " is already stoped.");
			return;
		}
		try {
			doStop(context);
			isStarted = false;
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.spi.StartupListener#restart(com.primeton.paas.cep.engine.ServerContext)
	 */
	public synchronized void restart(ServerContext context) {
		if (isStarted) {
			stop(context);
		}
		start(context);
	}

}
