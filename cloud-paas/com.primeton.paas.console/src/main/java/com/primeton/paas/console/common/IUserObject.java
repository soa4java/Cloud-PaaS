/**
 * 
 */
package com.primeton.paas.console.common;

import java.util.Map;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IUserObject {
	
	/**
	 * 
	 * @return
	 */
	String getUniqueId();

	/**
	 * 
	 * @return
	 */
	String getSessionId();
	
	/**
	 * 
	 * @return
	 */
	String getUserId();

	/**
	 * 
	 * @return
	 */
	String getUserMail();

	/**
	 * 
	 * @return
	 */
	String getUserName();

	/**
	 * 
	 * @return
	 */
	String getUserOrgId();

	/**
	 * 
	 * @return
	 */
	String getUserOrgName();

	/**
	 * 
	 * @return
	 */
	String getUserRealName();

	/**
	 * 
	 * @return
	 */
	String getUserRemoteIP();

	/**
	 * 
	 * @param map
	 */
	void setAttributes(Map<String, Object> map);

	/**
	 * 
	 * @return
	 */
	Map<String, Object> getAttributes();

	/**
	 * 
	 * @param s
	 * @param obj
	 */
	void put(String s, Object obj);

	/**
	 * 
	 * @param s
	 * @return
	 */
	Object get(String s);

	/**
	 * 
	 * @param s
	 * @return
	 */
	boolean contains(String s);

	/**
	 * 
	 * @param s
	 * @return
	 */
	Object remove(String s);

	/**
	 * 
	 */
	void clear();

}
