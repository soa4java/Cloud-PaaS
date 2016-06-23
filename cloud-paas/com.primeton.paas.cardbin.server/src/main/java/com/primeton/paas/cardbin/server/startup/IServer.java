/**
 * 
 */
package com.primeton.paas.cardbin.server.startup;

import java.io.File;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IServer {

	/**
	 * 
	 * @param serverHome
	 */
	void start(File serverHome);

	/**
	 * 
	 * @param serverHome
	 */
	void stop(File serverHome);

	/**
	 * 
	 * @param serverHome
	 */
	void stopRemote(File serverHome);
	
}
