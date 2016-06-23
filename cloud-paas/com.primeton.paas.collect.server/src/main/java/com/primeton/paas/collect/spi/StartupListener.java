/**
 * 
 */
package com.primeton.paas.collect.spi;

import com.primeton.paas.collect.server.ServerContext;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface StartupListener {
	
	/**
	 * 
	 * @return
	 */
	String getId();
	
	/**
	 * 
	 * @param context
	 */
	void start(ServerContext context);
	
	/**
	 * 
	 * @param context
	 */
	void stop(ServerContext context);
	
	/**
	 * 
	 * @param context
	 */
	void restart(ServerContext context);

}
