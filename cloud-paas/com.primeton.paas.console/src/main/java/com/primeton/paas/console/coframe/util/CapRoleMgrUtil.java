/**
 * 
 */
package com.primeton.paas.console.coframe.util;

import java.util.List;

import com.primeton.paas.manage.api.factory.AuthBaseManagerFactory;
import com.primeton.paas.manage.api.manager.IAuthManager;
import com.primeton.paas.manage.api.model.CapFunction;
import com.primeton.paas.manage.api.model.CapRole;
import com.primeton.paas.manage.api.model.PageCond;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CapRoleMgrUtil {
	
	private static IAuthManager authMgr = AuthBaseManagerFactory.getManager();

	public static CapRole[] getRoles(CapRole role, PageCond page) {
		return authMgr.getRoles(role, page);
	}

	public static CapRole getRoleByRoleCode(String roleCode) {
		return authMgr.getRoleByRoleCode(roleCode);
	}

	/**
	 * 获取已有权限的角色
	 * 
	 * @param userId
	 * @return
	 */
	public static CapRole[] getCapRoleByUserId(String userId) {
		return authMgr.getCapRoleByUserId(userId);
	}

	/**
	 * 获取没有权限的角色
	 * 
	 * @param userId
	 * @return
	 */
	public static CapRole[] getInverCapRoleByUserId(String userId) {
		return authMgr.getInverCapRoleByUserId(userId);
	}

	public static boolean saveRoleAndFunctionAuth(List<CapFunction> funcs,
			String roleId) {
		return authMgr.saveRoleAndFunctionAuth(funcs, roleId);
	}

	public static boolean checkRoleCodeIsExist(String roleCode) {
		return authMgr.checkRoleCodeIsExist(roleCode);
	}

	public static boolean addCapRole(CapRole role) {
		return authMgr.addCapRole(role);
	}

	public static boolean updateCapRole(CapRole role) {
		return authMgr.updateCapRole(role);
	}

	public static boolean deleteRoles(String[] roleIds) {
		return authMgr.deleteRoles(roleIds);
	}
	
}
