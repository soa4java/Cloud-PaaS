/**
 * 
 */
package com.primeton.paas.console.coframe.util;

import com.primeton.paas.manage.api.factory.AuthBaseManagerFactory;
import com.primeton.paas.manage.api.manager.IAuthManager;
import com.primeton.paas.manage.api.model.CapFunction;
import com.primeton.paas.manage.api.model.PageCond;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CapFunctionMgrUtil {
	
	private static IAuthManager authMgr = AuthBaseManagerFactory.getManager();

	public static CapFunction[] getFunctions(CapFunction function, PageCond page) {
		return authMgr.getFunctions(function, page);
	}

	public static boolean checkFuncIdIsExist(String funcId) {
		return authMgr.checkFuncIdIsExist(funcId);
	}

	public static CapFunction getFunctionByFuncId(String funcId) {
		return authMgr.getFunctionByFuncId(funcId);
	}

	public static boolean deleteFunctions(String[] ids) {
		return authMgr.deleteFunctions(ids);
	}

	public static CapFunction[] getCapFunctionByRoleId(String roleId) {
		return authMgr.getCapFunctionByRoleId(roleId);
	}

	public static CapFunction[] getInverCapFunctionByRoleId(String roleId) {
		return authMgr.getInverCapFunctionByRoleId(roleId);
	}

	public static boolean addCapFunction(CapFunction capFunction) {
		return authMgr.addCapFunction(capFunction);
	}

	public static boolean updateCapFunction(CapFunction capFunction) {
		return authMgr.updateCapFunction(capFunction);
	}

}
