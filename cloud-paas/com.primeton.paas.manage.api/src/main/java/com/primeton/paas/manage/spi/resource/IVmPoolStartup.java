/**
 * 
 */
package com.primeton.paas.manage.spi.resource;


/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IVmPoolStartup {

	/**
	 * 
	 */
	void start();

	/**
	 * 
	 */
	void stop();
	
	/**
	 * 
	 */
	void restart();
	
}
