/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import java.util.List;

import com.primeton.paas.manage.api.model.CapFunction;
import com.primeton.paas.manage.api.model.CapMenu;
import com.primeton.paas.manage.api.model.CapRole;
import com.primeton.paas.manage.api.model.CapUser;
import com.primeton.paas.manage.api.model.IPageCond;

/**
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public interface IAuthManager {

	/**
	 * 
	 * @param user
	 * @param page
	 * @return
	 */
	CapUser[] getUsers(CapUser user,IPageCond page);
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	CapUser getUserByUserId(String userId);
	
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
	 * @return
	 */
	boolean checkUserIdIsExist(String userId);
	
	/**
	 * 
	 * @param userId
	 * @param phone
	 * @return
	 */
	boolean checkUserPhoneIsExist(String userId,String phone);
	
	/**
	 * 
	 * @param userId
	 * @param email
	 * @return
	 */
	boolean checkUserEamilIsExist(String userId,String email);
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	boolean addUser(CapUser user);
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	boolean addAppUser(CapUser user);
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	boolean registeAppUser(CapUser user);
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	boolean updateUser(CapUser user);
	
	/**
	 * 
	 * @param userIds
	 * @return
	 */
	boolean deleteUsers(String[] userIds);
	
	/**
	 * 
	 * @param roles
	 * @param userId
	 * @return
	 */
	boolean saveUserAndRoleAuth(List<CapRole> roles,String userId);
	
	/**
	 * 
	 * @param functions
	 * @param roleId
	 * @return
	 */
	boolean saveRoleAndFunctionAuth(List<CapFunction> functions,String roleId);
	
	/**
	 * 
	 * @param role
	 * @param page
	 * @return
	 */
	CapRole[] getRoles(CapRole role,IPageCond page);
	
	/**
	 * 
	 * @param roleCode
	 * @return
	 */
	CapRole getRoleByRoleCode(String roleCode);
	
	/**
	 * 
	 * @param roleCode
	 * @return
	 */
	boolean checkRoleCodeIsExist(String roleCode);
	
	/**
	 * 
	 * @param role
	 * @return
	 */
	boolean addCapRole(CapRole role);
	
	/**
	 * 
	 * @param role
	 * @return
	 */
	boolean updateCapRole(CapRole role);
	
	/**
	 * 
	 * @param roleIds
	 * @return
	 */
	boolean deleteRoles(String[] roleIds);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	CapRole[] getCapRoleByUserId(String userId);
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	CapRole[] getInverCapRoleByUserId(String userId);
	
	/**
	 * 
	 * @param funcIds
	 * @return
	 */
	boolean deleteFunctions(String[] funcIds);
	
	/**
	 * 
	 * @param function
	 * @param page
	 * @return
	 */
	CapFunction[] getFunctions(CapFunction function, IPageCond page);
	
	/**
	 * 
	 * @param funcId
	 * @return
	 */
	boolean checkFuncIdIsExist(String funcId);
	
	/**
	 * 
	 * @param funcId
	 * @return
	 */
	CapFunction getFunctionByFuncId(String funcId);
	
	/**
	 * 
	 * @param function
	 * @return
	 */
	boolean addCapFunction(CapFunction function);
	
	/**
	 * 
	 * @param function
	 * @return
	 */
	boolean updateCapFunction(CapFunction function);
	
	/**
	 * 
	 * @param roleId
	 * @return
	 */
	CapFunction[] getCapFunctionByRoleId(String roleId);
	
	/**
	 * 
	 * @param roleId
	 * @return
	 */
	CapFunction[] getInverCapFunctionByRoleId(String roleId);
	
	/**
	 * 
	 * @return
	 */
	CapMenu[] getAllMenu();
	
	/**
	 * 
	 * @param page
	 * @return
	 */
	CapMenu[] getFirstLevelMenu(IPageCond page);
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	CapMenu[] getMenuByUserId(String userId);
	
	/**
	 * 
	 * @param menu
	 * @return
	 */
	boolean addFirstLevelCapMenu(CapMenu menu);
	
	/**
	 * 
	 * @param menu
	 * @return
	 */
	boolean addCapMenu(CapMenu menu);
	
	/**
	 * 
	 * @param menu
	 * @return
	 */
	boolean updateCapMenu(CapMenu menu);
	
	/**
	 * 
	 * @param menuId
	 * @return
	 */
	boolean deleteCapMenu(String menuId);
	
}
