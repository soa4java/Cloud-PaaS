/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IAuthManager;
import com.primeton.paas.manage.api.model.CapFunction;
import com.primeton.paas.manage.api.model.CapMenu;
import com.primeton.paas.manage.api.model.CapResauth;
import com.primeton.paas.manage.api.model.CapRole;
import com.primeton.paas.manage.api.model.CapUser;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.monitor.AppMetaData;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultAuthBaseManager implements IAuthManager {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultAuthBaseManager.class);
	
	private DefaultFunctionManager functionMgr;
	private DefaultMenuManager menuMgr;
	private DefaultPartyAuthManager partyAuthMgr;
	private DefaultResAuthManager resAuthMgr;
	private DefaultRoleManager roleMgr;
	private DefaultUserManager userMgr;
	
	/**
	 * Default. <br>
	 */
	public DefaultAuthBaseManager() {
		this.functionMgr = new DefaultFunctionManager();
		this.menuMgr = new DefaultMenuManager();
		this.partyAuthMgr = new DefaultPartyAuthManager();
		this.resAuthMgr = new DefaultResAuthManager();
		this.roleMgr = new DefaultRoleManager();
		this.userMgr = new DefaultUserManager();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getUsers(com.primeton.paas.manage.api.model.CapUser, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public CapUser[] getUsers(CapUser user, IPageCond page) {
		try {
			return userMgr.getUsersByCriteria(user, page);
		} catch (DaoException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getUserByUserId(java.lang.String)
	 */
	public CapUser getUserByUserId(String userId) {
		try {
			return userMgr.getCapUserByUserId(userId);
		} catch (DaoException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#validatePasswd(java.lang.String, java.lang.String)
	 */
	public boolean validatePasswd(String userId, String oldPasswd){
		try {
			return userMgr.validatePasswd(userId, oldPasswd);
		} catch (DaoException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		return false;
	}
	
	/**
	 * true ������ ����|false �Ѵ��� ������
	 */
	public boolean checkUserIdIsExist(String userId){
		CapUser user = null;
		try {
			user = userMgr.getCapUserByUserId(userId);
		} catch (DaoException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		if(user==null){
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#checkUserPhoneIsExist(java.lang.String, java.lang.String)
	 */
	public boolean checkUserPhoneIsExist(String userId, String phone) {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put("phone", phone); //$NON-NLS-1$
		try {
			CapUser[] users = userMgr.getUserByCriteria(criteria, null);
			if (null != users && users.length > 0) {
				if (userId == null)
					return false;
				else if (userId.equals(users[0].getUserId()))
					return true;
				return false;
			}
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#checkUserEamilIsExist(java.lang.String, java.lang.String)
	 */
	public boolean checkUserEamilIsExist(String userId, String email) {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put("email", email); //$NON-NLS-1$
		try {
			CapUser[] users = userMgr.getUserByCriteria(criteria, null);
			if (null != users && users.length > 0) {
				if (userId == null)
					return false;
				else if (userId.equals(users[0].getUserId()))
					return true;
				return false;
			} else
				return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#addUser(com.primeton.paas.manage.api.model.CapUser)
	 */
	public boolean addUser(CapUser user) {
		if (null == user) {
			return false;
		}
		return userMgr.createUser(user);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#addAppUser(com.primeton.paas.manage.api.model.CapUser)
	 */
	public boolean addAppUser(CapUser user) {
		boolean result = addUser(user);
		if (result) {
			try {
				String defaultAppuserRoleCode = SystemVariables
						.getDefaultAppUserRole();
				CapRole defaultAppUserRole = roleMgr
						.getRoleByRoleCode(defaultAppuserRoleCode);
				if (defaultAppUserRole != null) {
					CapRole[] roles = new CapRole[] { defaultAppUserRole };
					userMgr.saveUserAuth(user.getUserId(), roles);
					return true;
				}
			} catch (DaoException e) {
				logger.error("AddAppUser Error:" + e.getCause().getMessage());
				return false;
			}
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#registeAppUser(com.primeton.paas.manage.api.model.CapUser)
	 */
	public boolean registeAppUser(CapUser user) {
		if (user == null) {
			return false;
		}
		user.setStatus(1);
		addAppUser(user);
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#updateUser(com.primeton.paas.manage.api.model.CapUser)
	 */
	public boolean updateUser(CapUser user) {
		try {
			return userMgr.updateCapUser(user);
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#deleteUsers(java.lang.String[])
	 */
	public boolean deleteUsers(String[] userIds) {
		try {
			userMgr.deleteCapUser(userIds);
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#saveUserAndRoleAuth(java.util.List, java.lang.String)
	 */
	public boolean saveUserAndRoleAuth(List<CapRole> roles, String userId) {
		try {
			userMgr.saveUserAuth(userId,
					roles.toArray(new CapRole[roles.size()]));
			return true;
		} catch (DaoException e) {
			logger.error(e);
			return false;
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#saveRoleAndFunctionAuth(java.util.List, java.lang.String)
	 */
	public boolean saveRoleAndFunctionAuth(List<CapFunction> functions,
			String roleId) {
		try {
			roleMgr.saveAuthWithFunc(roleId,
					functions.toArray(new CapFunction[functions.size()]));
			return true;
		} catch (DaoException e) {
			logger.error(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getRoles(com.primeton.paas.manage.api.model.CapRole, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public CapRole[] getRoles(CapRole role, IPageCond page) {
		try {
			return roleMgr.getRolesByCriteria(role, page);
		} catch (DaoException e) {
			logger.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getRoleByRoleCode(java.lang.String)
	 */
	public CapRole getRoleByRoleCode(String roleCode) {
		try {
			return roleMgr.getRoleByRoleCode(roleCode);
		} catch (DaoException e) {
			logger.error(e);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#deleteRoles(java.lang.String[])
	 */
	public boolean deleteRoles(String[] roleIds) {
		try {
			roleMgr.deleteRoleBatch(roleIds);
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getFunctions(com.primeton.paas.manage.api.model.CapFunction, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public CapFunction[] getFunctions(CapFunction function, IPageCond page) {
		try {
			return functionMgr.getFunctionsByCriteria(function, page);
		} catch (DaoException e) {
			logger.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#checkFuncIdIsExist(java.lang.String)
	 */
	public boolean checkFuncIdIsExist(String funcId) {
		CapFunction function = functionMgr.getFunctionByFuncId(funcId);
		if (function != null && function.getFuncId() != null) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getFunctionByFuncId(java.lang.String)
	 */
	public CapFunction getFunctionByFuncId(String funcId) {
		return functionMgr.getFunctionByFuncId(funcId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#addCapFunction(com.primeton.paas.manage.api.model.CapFunction)
	 */
	public boolean addCapFunction(CapFunction function) {
		try {
			return functionMgr.insertFunction(function);
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#updateCapFunction(com.primeton.paas.manage.api.model.CapFunction)
	 */
	public boolean updateCapFunction(CapFunction function) {
		try {
			return functionMgr.updateFunction(function);
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#deleteFunctions(java.lang.String[])
	 */
	public boolean deleteFunctions(String[] funcIds) {
		return functionMgr.deleteFunctionBatch(funcIds);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getAllMenu()
	 */
	public CapMenu[] getAllMenu() {
		try {
			return menuMgr.queryMenuListByCriteria(null);
		} catch (DaoException e) {
			logger.error(e);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getFirstLevelMenu(com.primeton.paas.manage.api.model.IPageCond)
	 */
	public CapMenu[] getFirstLevelMenu(IPageCond page) {
		try {
			return menuMgr.getFirstLevelMenu(page);
		} catch (DaoException e) {
			logger.error(e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public CapMenu[] getFunctionMenuByUserId(String userId){
		String[] roleIds = new String[0];
		try {
			roleIds = partyAuthMgr.getCapRoleIdByPartyId(userId);
		} catch (DaoException e1) {
			if (logger.isErrorEnabled()) {
				logger.error(e1);
			}
		}
		if (roleIds == null || roleIds.length <= 0) {
			return null;
		}
		List<CapMenu> menus = new ArrayList<CapMenu>();
		try{
			CapResauth[] resAuthAll = resAuthMgr.getAllCapResauth();
			CapMenu[] menuAll = menuMgr.getAllCapMenu();
			for (String roleId : roleIds) {
				List<CapResauth> resAuths = new ArrayList<CapResauth>();
				for (CapResauth capResauth : resAuthAll) {
					if (roleId.equals(capResauth.getPartyId())) {
						resAuths.add(capResauth);
					}
				}
				for (CapResauth resAuth : resAuths) {
					List<CapMenu> roleMenus = new ArrayList<CapMenu>();
					for (CapMenu menu : menuAll) {
						if (resAuth.getResId().equals(menu.getLinkRes())
								&& resAuth.getResType().equals(
										menu.getLinkType())) {
							roleMenus.add(menu);
						}
					}
					for (CapMenu menu : roleMenus) {
						menus.add(menu);
					}
				}
			}
		} catch (DaoException e) {
			logger.error(e);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getMenuByUserId(java.lang.String)
	 */
	public CapMenu[] getMenuByUserId(String userId) {
		String[] roleIds = new String[0];
		try {
			roleIds = partyAuthMgr.getCapRoleIdByPartyId(userId);
		} catch (DaoException e1) {
			logger.error(e1);
		}
		if(roleIds==null||roleIds.length<=0){
			return null;
		}
		List<CapMenu> menus = new ArrayList<CapMenu>();
		try{
			CapResauth[] resAuthAll = resAuthMgr.getAllCapResauth();
			CapMenu[] menuAll = menuMgr.getAllCapMenu();
			for (String roleId : roleIds) {
				List<CapResauth> resAuths = new ArrayList<CapResauth>();
				for (CapResauth capResauth : resAuthAll) {
					if (roleId.equals(capResauth.getPartyId())) {
						resAuths.add(capResauth);
					}
				}
				for (CapResauth resAuth : resAuths) {
					List<CapMenu> roleMenus = new ArrayList<CapMenu>();
					for (CapMenu menu : menuAll) {
						if (resAuth.getResId().equals(menu.getLinkRes())
								&& resAuth.getResType().equals(
										menu.getLinkType())) {
							roleMenus.add(menu);
						}
					}
					for (CapMenu menu : roleMenus) {
						if (!menus.contains(menu)) {
							menus.add(menu);
						}
					}
				}
			}
			List<String> parentMenuIds = new ArrayList<String>();
			List<CapMenu> parentMenu = new ArrayList<CapMenu>();
			for (CapMenu menu : menus) {
				if (!parentMenuIds.contains(menu.getParentMenuId())) {
					parentMenuIds.add(menu.getParentMenuId());
					for (CapMenu tempMenu : menuAll) {
						if (tempMenu.getMenuId().equals(menu.getParentMenuId())
								&& tempMenu.getParentMenuId() == null) {
							parentMenu.add(tempMenu);
						}
					}
				}
			}

			menus.addAll(parentMenu);
		} catch (DaoException e) {
			logger.error(e);
		}
		Collections.sort(menus, new Comparator<CapMenu>() {
			public int compare(CapMenu o1, CapMenu o2) {
				return o1.getMenuCode().compareTo(o2.getMenuCode());
			}
		});
		return menus.toArray(new CapMenu[menus.size()]);
	}
	
	/**
	 * 
	 * @param metaDatas
	 * @return
	 */
	public static List<AppMetaData> sort(List<AppMetaData> metaDatas) {
		if (metaDatas == null) {
			return new ArrayList<AppMetaData>();
		}
		if (metaDatas.isEmpty()) {
			return metaDatas;
		}
		Comparator<AppMetaData> comparator = new Comparator<AppMetaData>() {
			public int compare(AppMetaData s1, AppMetaData s2) {
				return (s1.getTime() - s2.getTime()) > 0 ? 1 : -1;
			}
		};
		Collections.sort(metaDatas, comparator);
		return metaDatas;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#addFirstLevelCapMenu(com.primeton.paas.manage.api.model.CapMenu)
	 */
	public boolean addFirstLevelCapMenu(CapMenu menu) {
		return menuMgr.insertFirstLevelMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#addCapMenu(com.primeton.paas.manage.api.model.CapMenu)
	 */
	public boolean addCapMenu(CapMenu menu) {
		return menuMgr.insertMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#updateCapMenu(com.primeton.paas.manage.api.model.CapMenu)
	 */
	public boolean updateCapMenu(CapMenu menu) {
		if (menu == null || menu.getMenuId() == null) {
			return false;
		}
		CapMenu oldMenu = menuMgr.getCapMenuByMenuId(menu.getMenuId());
		oldMenu.setMenuCode(menu.getMenuCode());
		oldMenu.setMenuName(menu.getMenuName());
		oldMenu.setImagepath(menu.getImagepath());
		oldMenu.setLinkRes(menu.getLinkRes());
		oldMenu.setLinkAction(menu.getLinkAction());
		oldMenu.setLinkType(menu.getLinkType());
		try {
			menuMgr.updateMenu(oldMenu);
			return true;
		} catch (DaoException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#deleteCapMenu(java.lang.String)
	 */
	public boolean deleteCapMenu(String menuId) {
		CapMenu menu = menuMgr.getCapMenuByMenuId(menuId);
		if (menu == null) {
			return false;
		}
		try {
			if (menu.getMenuLevel() == 1) {
				return menuMgr.deleteFirstLevelMenu(menu);
			} else {
				return menuMgr.deleteCapMenuByMenuId(menuId);
			}
		} catch (DaoException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getCapRoleByUserId(java.lang.String)
	 */
	public CapRole[] getCapRoleByUserId(String userId) {
		try {
			String[] roleIds = partyAuthMgr.getCapRoleIdByPartyId(userId);
			if (roleIds.length <= 0) {
				return new CapRole[0];
			}
			CapRole[] allRoles = roleMgr.getAllRoleList();
			List<CapRole> list = new ArrayList<CapRole>();
			for (String roleId : roleIds) {
				for (CapRole role : allRoles) {
					if (roleId.equals(role.getRoleId())) {
						list.add(role);
					}
				}
			}
			return list.toArray(new CapRole[list.size()]);
		} catch (DaoException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		return new CapRole[0];
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getInverCapRoleByUserId(java.lang.String)
	 */
	public CapRole[] getInverCapRoleByUserId(String userId) {
		try {
			String[] roleIds = partyAuthMgr.getCapRoleIdByPartyId(userId);
			CapRole[] allRoles = roleMgr.getAllRoleList();
			if (roleIds.length <= 0) {
				return allRoles;
			}
			List<CapRole> list = new ArrayList<CapRole>();
			List<String> hasAuthRoleIds = Arrays.asList(roleIds);
			for (CapRole role : allRoles) {
				if (!hasAuthRoleIds.contains(role.getRoleId())) {
					list.add(role);
				}
			}
			return list.toArray(new CapRole[list.size()]);
		} catch (DaoException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		return new CapRole[0];
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getCapFunctionByRoleId(java.lang.String)
	 */
	public CapFunction[] getCapFunctionByRoleId(String roleId) {
		try {
			String[] funcIds = resAuthMgr.getFunctionIdByPartyId(roleId);
			if (funcIds.length <= 0) {
				return new CapFunction[0];
			}
			CapFunction[] allFuncs = functionMgr.getFuncListByCriteria(null);
			List<CapFunction> list = new ArrayList<CapFunction>();
			for (String funId : funcIds) {
				for (CapFunction func : allFuncs) {
					if (funId.equals(func.getFuncId())) {
						list.add(func);
					}
				}
			}
			return list.toArray(new CapFunction[list.size()]);
		} catch (DaoException e) {
			logger.error(e);
		}
		return new CapFunction[0];
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#getInverCapFunctionByRoleId(java.lang.String)
	 */
	public CapFunction[] getInverCapFunctionByRoleId(String roleId) {
		try {
			String[] funcIds = resAuthMgr.getFunctionIdByPartyId(roleId);
			CapFunction[] allFuncs = functionMgr.getFuncListByCriteria(null);
			if (funcIds.length <= 0) {
				return allFuncs;
			}
			List<CapFunction> list = new ArrayList<CapFunction>();
			List<String> hasAuthFuncIds = Arrays.asList(funcIds);
			for (CapFunction func : allFuncs) {
				if (!hasAuthFuncIds.contains(func.getFuncId())) {
					list.add(func);
				}
			}
			return list.toArray(new CapFunction[list.size()]);
		} catch (DaoException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		return new CapFunction[0];
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#checkRoleCodeIsExist(java.lang.String)
	 */
	public boolean checkRoleCodeIsExist(String roleCode) {
		try {
			CapRole role = roleMgr.getRoleByRoleCode(roleCode);
			if (role != null && role.getRoleCode() != null) {
				return true;
			}
			return false;
		} catch (DaoException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#addCapRole(com.primeton.paas.manage.api.model.CapRole)
	 */
	public boolean addCapRole(CapRole role) {
		try {
			return roleMgr.insertRole(role);
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IAuthManager#updateCapRole(com.primeton.paas.manage.api.model.CapRole)
	 */
	public boolean updateCapRole(CapRole role) {
		try {
			return roleMgr.updateRole(role);
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}
	
}
