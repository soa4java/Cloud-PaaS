/**
 * 
 */
package com.primeton.paas.manage.api.startup;

/**
 * PAAS Manager Service Startup. <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IManageStartup {

	/**
	 * Do start action. <br>
	 * 
	 * @param configPath
	 */
	void start(String configPath);
	
	
	/**
	 * Do stop action. <br>
	 */
	void stop();
	
	
}
