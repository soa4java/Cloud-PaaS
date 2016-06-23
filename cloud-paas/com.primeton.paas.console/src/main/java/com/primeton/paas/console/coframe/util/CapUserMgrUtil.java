/**
 * 
 */
package com.primeton.paas.console.coframe.util;

import java.util.List;

import com.primeton.paas.manage.api.factory.AuthBaseManagerFactory;
import com.primeton.paas.manage.api.manager.IAuthManager;
import com.primeton.paas.manage.api.model.CapRole;
import com.primeton.paas.manage.api.model.CapUser;
import com.primeton.paas.manage.api.model.PageCond;

/**
 * 权限管理-用户管理
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class CapUserMgrUtil {
	
	private static IAuthManager authMgr = AuthBaseManagerFactory.getManager();

	/**
	 * 查询用户
	 * 
	 * @param user
	 * @param page
	 * @return
	 */
	public static CapUser[] getUsers(CapUser user, PageCond page) {
		return authMgr.getUsers(user, page);
	}

	public static CapUser getUserByUserId(String userId) {
		return authMgr.getUserByUserId(userId);
	}

	/**
	 * userId是否可用
	 * 
	 * @param userId
	 * @return true 不存在可用 |false 已存在不可用
	 */
	public static boolean checkUserIdIsExist(String userId) {
		return authMgr.checkUserIdIsExist(userId);
	}

	/**
	 * userPhone 是否可用
	 * 
	 * @param phone
	 * @return true 不存在可用 |false 已存在不可用
	 */
	public static boolean checkUserPhoneIsExist(String userId, String phone) {
		return authMgr.checkUserPhoneIsExist(userId, phone);
	}

	/**
	 * userEmail 是否可用
	 * 
	 * @param email
	 * @return true 不存在可用 |false 已存在不可用
	 */
	public static boolean checkUserEamilIsExist(String userId, String email) {
		return authMgr.checkUserEamilIsExist(userId, email);
	}

	/**
	 * 增加一个空权限用户
	 * 
	 * @param user
	 * @return
	 */
	public static boolean addUser(CapUser user) {
		return authMgr.addUser(user);
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public static boolean updateCapUser(CapUser user) {
		return authMgr.updateUser(user);
	}

	/**
	 * 增加一个自带自助服务菜单权限的用户
	 * 
	 * @param user
	 * @return
	 */
	public static boolean addAppUser(CapUser user) {
		return authMgr.addAppUser(user);
	}

	/**
	 * 
	 * @param userIds
	 * @return
	 */
	public static boolean deleteUsers(String[] userIds) {
		return authMgr.deleteUsers(userIds);
	}

	/**
	 * 
	 * @param roles
	 * @param userId
	 * @return
	 */
	public static boolean saveUserAndRoleAuth(List<CapRole> roles, String userId) {
		return authMgr.saveUserAndRoleAuth(roles, userId);
	}
	
}
