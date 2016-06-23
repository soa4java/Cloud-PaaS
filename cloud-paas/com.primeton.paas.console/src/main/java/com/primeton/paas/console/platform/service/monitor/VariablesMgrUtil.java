/**
 * 
 */
package com.primeton.paas.console.platform.service.monitor;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.factory.VariableManagerFactory;
import com.primeton.paas.manage.api.manager.IVariableManager;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.Variable;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 系统变量管理
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class VariablesMgrUtil {

	private static ILogger logger = LoggerFactory.getLogger(VariablesMgrUtil.class);

	private static IVariableManager varManager = VariableManagerFactory
			.getManager();

	/**
	 * 根据条件查询变量信息（分页）
	 * 
	 * @param critetia
	 * @param page
	 * @return
	 */
	public static Variable[] getVariables(Variable critetia, PageCond page) {
		try {
			return varManager.get(critetia, page);
		} catch (Exception e) {
			logger.error(e);
		}
		return new Variable[0];
	}

	/**
	 * 查询变量信息
	 * 
	 * @param critetia
	 * @param page
	 * @return
	 */
	public static Variable queryVar(String varKey) {
		if (StringUtil.isEmpty(varKey)) {
			return null;
		}
		try {
			return varManager.get(varKey);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 更新变量配置 <br>
	 * 
	 * @param var
	 * @return
	 */
	public static boolean updateVar(Variable var) {
		if (var == null) {
			return true;
		}
		try {
			varManager.save(var);
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 更新变量配置 <br>
	 * 
	 * @param varKey
	 * @param varValue
	 * @param varDesc
	 * @return
	 */
	public static boolean updateVar(String varKey, String varValue,
			String varDesc) {
		Variable var = new Variable();
		var.setVarKey(varKey);
		var.setVarValue(varValue);
		var.setDescription(varDesc);
		return updateVar(var);
	}

	/**
	 * 移除变量配置 <br>
	 * 
	 * @param varKey
	 * @return
	 */
	public static boolean removeVar(String varKey) {
		if (StringUtil.isEmpty(varKey)) {
			return false;
		}
		try {
			varManager.remove(varKey);
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 批量删除变量配置 <br>
	 * 
	 * @param vars
	 * @return
	 */
	public static boolean removeVars(String[] varKeys) {
		if (varKeys == null || varKeys.length < 1) {
			return false;
		}
		try {
			for (String varKey : varKeys) {
				varManager.remove(varKey);
			}
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 查询变量是否存在<br>
	 * 
	 * @param varKey
	 * @return true 不存在|false 存在
	 */
	public static boolean isExistPasVar(String varKey) {
		return null != queryVar(varKey);
	}

	/**
	 * 添加系统变量 <br>
	 * 
	 * @param var
	 * @return
	 */
	public static boolean addPasVar(Variable var) {
		try {
			varManager.save(var);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 添加系统变量 <br>
	 * 
	 * @param key
	 * @param value
	 * @param desc
	 * @return
	 */
	public static boolean addPasVar(String key, String value, String desc) {
		Variable var = new Variable();
		var.setVarKey(key);
		var.setVarValue(value);
		var.setDescription(desc);
		return addPasVar(var);
	}

}
