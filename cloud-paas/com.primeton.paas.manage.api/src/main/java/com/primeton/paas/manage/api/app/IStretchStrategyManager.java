/**
 * 
 */
package com.primeton.paas.manage.api.app;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IStretchStrategyManager {

	/**
	 * 
	 * @param strategy
	 * @throws StretchStrategyException
	 */
	void save(StretchStrategy strategy) throws StretchStrategyException;
	
	/**
	 * 
	 * @param appName
	 */
	void remove(String appName);
	
	/**
	 * 
	 */
	void removeGlobal();
	
	
}
