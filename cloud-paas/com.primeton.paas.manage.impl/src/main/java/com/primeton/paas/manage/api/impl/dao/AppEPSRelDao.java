/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;


/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface AppEPSRelDao {
	
	/**
	 * 
	 * @param rel
	 * @return
	 */
	boolean add(AppEPSRel rel);
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	boolean delete(String appName);
	
	/**
	 * 
	 * @param rel
	 * @return
	 */
	boolean update(AppEPSRel rel);
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	AppEPSRel get(String appName);

}
