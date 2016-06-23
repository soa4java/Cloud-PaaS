package com.primeton.paas.app.api.log;

/**
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public interface IUserLogModify {
	
	/**
	 * 
	 * @param type
	 * @param level
	 */
	void doModifyLevel(String type, String level);
	
}
