/**
 * 
 */
package com.primeton.paas.common.spi;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IStartupListener {
	
	/**
	 * start. <br>
	 */
	void start();
	
	/**
	 * stop. <br>
	 */
	void stop();
	
}
