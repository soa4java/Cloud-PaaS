/**
 * 
 */
package com.primeton.paas.console.common;

import com.primeton.paas.manage.api.factory.AuthBaseManagerFactory;
import com.primeton.paas.manage.api.factory.UserManagerFactory;
import com.primeton.paas.manage.api.manager.IAuthManager;
import com.primeton.paas.manage.api.manager.IUserManager;
import com.primeton.paas.manage.api.model.CapUser;
import com.primeton.paas.manage.api.model.User;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 用户登录工具类. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class UserLoginUtil {

	/**
	 * Application
	 */
	public static final String SESSION_APPLICATION_USER = "PAAS_APPLICATION_USER";
	
	/**
	 * Platform
	 */
	public static final String SESSION_PLATFORM_USER = "PAAS_PLATFORM_USER";

	/**
	 * Application
	 */
	private static IUserManager manager = UserManagerFactory.getManager();
	
	/**
	 * Platform
	 */
	private static IAuthManager authMgr = AuthBaseManagerFactory.getManager();

	/**
	 * 用户登录验证. <br>
	 * 
	 * @param userID
	 * @param password
	 * @return
	 */
	public static Result appLogin(String userID, String password) {
		Result result = new Result(false, "");
		if (StringUtil.isEmpty(userID)) {
			result.setMessage("userID is null or blank.");
			return result;
		}
		if (StringUtil.isEmpty(password)) {
			result.setMessage("password is null or blank.");
			return result;
		}

		User user = null;
		try {
			user = manager.get(userID);
		} catch (Exception e) {
			// 可能是链接断开异常，尝试二次查询
			try {
				user = manager.get(userID);
			} catch (Exception e1) {
				e1.printStackTrace();
				if (user == null) {
					result.setMessage("Login error, please retry.");
					return result;
				}
			}
		}

		if (user == null) {
			result.setMessage("User '" + userID + " '  is not exist.");
			return result;
		}

		// Password ok and account active
		if (manager.validatePasswd(userID, password)) {
			if (user.getStatus() == User.USER_STATUS_ACTIVED) {
				UserObject userObject = new UserObject();
				userObject.setUserId(user.getUserId());
				userObject.setUserName(user.getUserName());
				userObject.setUserMail(user.getEmail());
				userObject.setUserRealName(user.getUserName());
				userObject.setUniqueId("" + user.getId());
				userObject.put(SESSION_APPLICATION_USER, user);
				OnlineUserManager.login(userObject);
				result.setSuccess(true);
				result.setMessage("Login success.");
				return result;
			} else if (user.getStatus() == User.USER_STATUS_REJECTED) {
				String notes = StringUtil.isEmpty(user.getNotes()) ? "" : "("
						+ user.getNotes() + ")";
				String message = "User '" + userID
						+ "'s register has been rejected. " + notes;
				result.setMessage(message);
				return result;
			} else {
				String message = "User '" + userID
						+ "' has not been authorized.";
				result.setMessage(message);
				return result;
			}
		} else {
			result.setMessage("Username or password error.");
			return result;
		}
	}
	
	/**
	 * 用户登录验证. <br>
	 * 
	 * @param userID
	 * @param password
	 * @return
	 */
	public static Result platformLogin(String userID, String password) {
		Result result = new Result(false, "");
		if (StringUtil.isEmpty(userID)) {
			result.setMessage("userID is null or blank.");
			return result;
		}
		if (StringUtil.isEmpty(password)) {
			result.setMessage("password is null or blank.");
			return result;
		}
		CapUser user = null;
		try {
			user = authMgr.getUserByUserId(userID);
		} catch (Exception e) {
			// 可能是链接断开异常，尝试二次查询
			try {
				authMgr.getUserByUserId(userID);
			} catch (Exception e1) {
				e1.printStackTrace();
				if (user == null) {
					result.setMessage("Login error, please retry.");
					return result;
				}
			}
		}
		if (user == null) {
			result.setMessage("User '"	+ userID + " '  is not exists.");
			return result;
		}
		
		// Password ok and account active
		if (authMgr.validatePasswd(userID, password)) {
			if (user.getStatus() == User.USER_STATUS_ACTIVED) {
				// User Login
				UserObject userObject = new UserObject();
				userObject.setUserId(user.getUserId());
				userObject.setUserName(user.getUserName());
				userObject.setUserMail(user.getEmail());
				userObject.setUserRealName(user.getUserName());
				userObject.setUniqueId("" + user.getOperatorId());
				userObject.put(SESSION_PLATFORM_USER, user);
				OnlineUserManager.login(userObject);
				result.setSuccess(true);
				result.setMessage("Login success.");
				return result;
			} else if(user.getStatus() == User.USER_STATUS_REJECTED){
				String notes = StringUtil.isEmpty(user.getNotes()) ? "" : "("  + user.getNotes() + ")";
				String message = "User '"	+ userID + "'s register has been rejected. " + notes;
				result.setMessage(message);
				return result;
			} else {
				String message = "User '"	+ userID + "' has not been authorized.";
				result.setMessage(message);
				return result;
			}

		} else {
			result.setMessage("User name or password error.");
			return result;
		}
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class Result {

		private boolean success;
		
		private String message;

		public Result() {
			super();
		}

		public Result(boolean success, String message) {
			super();
			this.success = success;
			this.message = message;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}
	
}
