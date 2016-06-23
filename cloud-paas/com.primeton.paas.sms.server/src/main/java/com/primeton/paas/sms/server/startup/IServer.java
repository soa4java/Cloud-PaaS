/**
 * 
 */
package com.primeton.paas.sms.server.startup;

import java.io.File;

/**
 *
 * @author liyanping(liyp@primeton.com)
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
