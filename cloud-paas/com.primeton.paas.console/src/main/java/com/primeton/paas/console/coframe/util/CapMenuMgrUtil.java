/**
 * 
 */
package com.primeton.paas.console.coframe.util;

import com.primeton.paas.manage.api.factory.AuthBaseManagerFactory;
import com.primeton.paas.manage.api.manager.IAuthManager;
import com.primeton.paas.manage.api.model.CapMenu;
import com.primeton.paas.manage.api.model.PageCond;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CapMenuMgrUtil {
	
	private static IAuthManager authMgr = AuthBaseManagerFactory.getManager();

	public static CapMenu[] getAllMenu() {
		CapMenu[] menus = authMgr.getAllMenu();
		return menus;
	}

	public static CapMenu[] getFirstLevelMenu(PageCond page) {
		return authMgr.getFirstLevelMenu(page);
	}

	public static CapMenu[] getMenuByUserId(String userId) {
		return authMgr.getMenuByUserId(userId);
	}

	public static boolean addFirstLevelCapMenu(CapMenu menu) {
		return authMgr.addFirstLevelCapMenu(menu);
	}

	public static boolean addCapMenu(CapMenu menu) {
		return authMgr.addCapMenu(menu);
	}

	public static boolean updateCapMenu(CapMenu menu) {
		return authMgr.updateCapMenu(menu);
	}

	public static boolean deleteCapMenu(String menuId) {
		return authMgr.deleteCapMenu(menuId);
	}
	
}
