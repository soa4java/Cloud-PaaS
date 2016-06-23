/**
 * 
 */
package com.primeton.paas.manage.api.app;

import java.util.List;

import com.primeton.paas.manage.api.model.IPageCond;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public interface IStrategyQueryManager {
	
	/**
	 * 
	 * @param appName
	 * @param strategyType
	 * @return
	 */
	StretchStrategy get(String appName, String strategyType);
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	List<StretchStrategy> get(String appName);
	
	/**
	 * 
	 * @return
	 */
	List<StretchStrategy> getGlobal();
	
	/**
	 * 
	 * @param strategyType
	 * @return
	 */
	StretchStrategy getGlobal(String strategyType);
	
	/**
	 * 
	 * @param criteria
	 * @param pageCond
	 * @return
	 */
	List<StretchStrategy> getAll(StretchStrategy criteria, IPageCond pageCond);
	
}
