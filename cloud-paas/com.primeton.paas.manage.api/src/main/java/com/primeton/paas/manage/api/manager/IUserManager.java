/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.User;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IUserManager {

	/**
	 * 
	 * @param user
	 */
	void add(User user);
	
	/**
	 * 
	 * @param userId
	 */
	void remove(String userId);
	
	/**
	 * 
	 * @param user
	 */
	void update(User user);
	
	/**
	 * 
	 * @param userId
	 * @param oldPasswd
	 * @return
	 */
	boolean validatePasswd(String userId, String oldPasswd);
	
	/**
	 * 
	 * @param userId
	 * @param password
	 */
	void resetPasswd(String userId, String password);
	
	/**
	 * 
	 * @param userId
	 * @param status
	 * @param handler
	 */
	void updateStatus(String userId, int status, String handler);
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	User get(String userId);
	
	/**
	 * 
	 * @param pageCond
	 * @return
	 */
	User[] get(IPageCond pageCond);
	
	/**
	 * 
	 * @param user
	 * @param pageCond
	 * @return
	 */
	User[] get(User user, IPageCond pageCond);

	/**
	 * 
	 * @param userId
	 * @param phone
	 * @return
	 */
	User get(String userId, String phone);
	
	/**
	 * 
	 * @param userId
	 * @param phone
	 * @return
	 */
	String resetForgottenPassword(String userId, String phone);
	
}
