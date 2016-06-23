/**
 * 
 */
package com.primeton.paas.cep.spi;

import com.primeton.paas.cep.engine.ServerContext;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-11-11
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
