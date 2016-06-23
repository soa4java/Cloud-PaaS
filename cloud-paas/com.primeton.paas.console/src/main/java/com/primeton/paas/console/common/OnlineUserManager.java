/**
 * 
 */
package com.primeton.paas.console.common;


/**
 * 在线用户管理. <br>
 * 
 * @author liming(li-ming@primeton.com)
 */
public class OnlineUserManager {
	
	/**
	 * 
	 * @param userObject
	 */
	public static void login(IUserObject userObject) {
		DataContextManager.current().setMUODataContext(new MUODataContext(userObject));
	}
	
	/**
	 * 
	 * @return
	 */
	public static IUserObject getUserObject() {
		return DataContextManager.current().getMUODataContext().getUserObject();
	}
	
}
