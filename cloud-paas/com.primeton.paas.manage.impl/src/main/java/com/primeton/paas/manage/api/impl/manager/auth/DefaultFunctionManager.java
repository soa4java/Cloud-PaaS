/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;

import com.primeton.paas.manage.api.impl.dao.DefaultAuthManageDao;
import com.primeton.paas.manage.api.impl.util.IAuthConstants;
import com.primeton.paas.manage.api.model.CapFunction;
import com.primeton.paas.manage.api.model.CapMenu;
import com.primeton.paas.manage.api.model.CapResauth;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultFunctionManager {
	
	private DefaultAuthManageDao dacbDao = DefaultAuthManageDao.getInstance();

	public final static String CAP_FUNCTION_ENTITY_ID_PROPERTY = "funcId";
	public final static String CAP_FUNCTION_ENTITY_NAME_PROPERTY = "funcName";
	public final static String CAP_FUNCTION_ENTITY_TYPE_PROPERTY = "funcType";
	public final static String CAP_FUNCTION_ENTITY_ACTION_PROPERTY = "funcAction";
	public final static String CAP_FUNCTION_ENTITY_CHECK_PROPERTY = "isCheck";
	public final static String CAP_FUNCTION_ENTITY_MENU_PROPERTY = "isMenu";
	public final static String CAP_FUNCTION_ENTITY_CREATER_PROPERTY = "createuser";
	
	private DefaultMenuManager menuManager;
	private DefaultResAuthManager resAuthMgr;

	/**
	 * Default. <br>
	 */
	public DefaultFunctionManager(){
		menuManager = new DefaultMenuManager();
		resAuthMgr = new DefaultResAuthManager();
	}
	
	/**
	 * 
	 * @param funcId
	 * @return
	 */
	public CapFunction getFunctionByFuncId(String funcId) {
		if (StringUtil.isEmpty(funcId)) {
			return null;
		} else {
			try {
				return dacbDao.getFunctionByFuncId(funcId);
			} catch (DaoException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * 
	 * @param function
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public CapFunction[] getFunctionsByCriteria(CapFunction function,IPageCond page) throws DaoException{
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY,IAuthConstants.DEFAULT_TENANT_ID);
		List<CapFunction> list = null;

		if (function != null) {
			String funcId = function.getFuncId();//
			String funcName = function.getFuncName();//
			String funcType = function.getFuncType();
			String funcAction = function.getFuncAction();//
			String isCheck = function.getIsCheck();
			String isMenu = function.getIsMenu();
			String createuser = function.getCreateuser();
			if (StringUtil.isNotEmpty(funcId)) {
				criteria.put(CAP_FUNCTION_ENTITY_ID_PROPERTY, funcId);
			}
			if (StringUtil.isNotEmpty(funcName)) {
				criteria.put(CAP_FUNCTION_ENTITY_NAME_PROPERTY, funcName);
			}
			if (StringUtil.isNotEmpty(funcType)) {
				criteria.put(CAP_FUNCTION_ENTITY_TYPE_PROPERTY, funcType);
			}
			if (StringUtil.isNotEmpty(funcAction)) {
				criteria.put(CAP_FUNCTION_ENTITY_ACTION_PROPERTY, funcAction);
			}
			if (StringUtil.isNotEmpty(isCheck)) {
				criteria.put(CAP_FUNCTION_ENTITY_CHECK_PROPERTY, isCheck);
			}
			if (StringUtil.isNotEmpty(isMenu)) {
				criteria.put(CAP_FUNCTION_ENTITY_MENU_PROPERTY, isMenu);
			}
			if (StringUtil.isNotEmpty(createuser)) {
				criteria.put(CAP_FUNCTION_ENTITY_CREATER_PROPERTY, createuser);
			}
		}
		if (page == null) {
			list = dacbDao.getFunctionsByCriteria(criteria);
		} else {
			if (page.getCount() <= 0) {
				page.setCount(dacbDao.getFunctionCountByCriteria(criteria));
			}
			list = dacbDao.getFunctionsByCriteria(criteria, page);
		}

		if (list != null) {
			return list.toArray(new CapFunction[list.size()]);
		}
		return new CapFunction[0];
	}
	
	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public CapFunction[] getFunctionsByCriteria(Map<String, String> criteria,
			IPageCond page) throws DaoException {
		if (criteria == null) {
			criteria = new HashMap<String, String>();
		} else {
			criteria.put(IAuthConstants.TENANT_PROPERTY,
					IAuthConstants.DEFAULT_TENANT_ID);
		}
		List<CapFunction> list = null;
		
		if (page == null) {
			list = dacbDao.getFunctionsByCriteria(criteria,null);
		} else {
			if (page.getCount() <= 0) {
				page.setCount(dacbDao.getFunctionCountByCriteria(criteria));
			}
			list = dacbDao.getFunctionsByCriteria(criteria,page);
		}
		if (list != null && !list.isEmpty()) {
			return list.toArray(new CapFunction[list.size()]);
		}
		return null;
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public int getFunctionCountByCriteria(Map<String, String> criteria)
			throws DaoException {
		return dacbDao.getFunctionCountByCriteria(criteria);
	}
	
	/**
	 * 
	 * @param func
	 * @return
	 * @throws DaoException
	 */
	public boolean insertFunction(CapFunction func) throws DaoException {
		func.setTenantId(IAuthConstants.DEFAULT_TENANT_ID);
		func.setCreatetime(new Date());
		if (func.getCreateuser() == null) {
			func.setCreateuser("sysadmin");
		}
		dacbDao.addFunction(func);
		return true;
	}

	/**
	 * 
	 * @param func
	 * @return
	 * @throws DaoException
	 */
	public boolean updateFunction(CapFunction func) throws DaoException {
		if (func == null || func.getFuncId() == null) {
			return false;
		}
		dacbDao.updateFunction(func);
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(DefaultMenuManager.CAP_MENU_ENTITY_LINK_RES_PROPERTY,
				func.getFuncId());
		CapMenu[] menus = menuManager.queryMenuListByCriteria(criteria);
		if (menus.length > 0) {
			for (CapMenu menu : menus) {
				menu.setLinkAction(func.getFuncAction());
				menu.setLinkRes(func.getFuncId());
			}
			menuManager.updateMenuBatch(menus);
		}

		return true;
	}
	
	/**
	 * 
	 * @param function
	 * @return
	 * @throws DaoException
	 */
	public boolean deleteFunction(CapFunction function) throws DaoException {
		if (function == null || function.getFuncId() == null) {
			return false;
		}
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(CAP_FUNCTION_ENTITY_ID_PROPERTY, function.getFuncId());
		dacbDao.deleteFunction(criteria);
		deleteMenuByFunction(function.getFuncId());
		deleteResauthByFunction(function.getFuncId());
		return true;
	}
	
	/**
	 * 
	 * @param funcId
	 * @throws DaoException
	 */
	public void deleteMenuByFunction(String funcId) throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(DefaultMenuManager.CAP_MENU_ENTITY_LINK_RES_PROPERTY,
				funcId);
		CapMenu[] menus = menuManager.queryMenuListByCriteria(criteria);
		menuManager.deleteMenuBatchByMenuIds(menus);
	}
	
	/**
	 * 
	 * @param funcId
	 * @throws DaoException
	 */
	public void deleteResauthByFunction(String funcId) throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(DefaultResAuthManager.CAP_RESAUTH_ENTITY_RES_ID_PROPERTY,
				funcId);
		CapResauth[] resAuths = resAuthMgr.getCapResauthByCriteria(criteria);
		resAuthMgr.deleteCapResauthBatch(resAuths);
	}
	
	/**
	 * 
	 * @param funcIds
	 * @return
	 */
	public boolean deleteFunctionBatch(String[] funcIds) {
		try {
			dacbDao.deleteFunctionBatchByFunctionIds(funcIds);
			for (String funcId : funcIds) {
				deleteMenuByFunction(funcId);
				deleteResauthByFunction(funcId);
			}
		} catch (DaoException e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public CapFunction[] getFuncListByCriteria(Map<String, String> criteria)
			throws DaoException {
		List<CapFunction> list = dacbDao.getFunctionsByCriteria(criteria);
		return list.toArray(new CapFunction[list.size()]);
	}
	
	/**
	 * 
	 * @param funcAction
	 * @return
	 * @throws DaoException
	 */
	public boolean isFuncActionExist(String funcAction) throws DaoException {
		if (funcAction.indexOf("?") != -1) { //$NON-NLS-1$
			// funcAction?xx=xx
			funcAction = StringUtil.substringBefore(funcAction, "?"); //$NON-NLS-1$
		}
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(CAP_FUNCTION_ENTITY_ACTION_PROPERTY, funcAction);
		CapFunction[] functions = getFunctionsByCriteria(criteria, null);
		if (functions != null && functions.length > 0) {
			return true;
		}
		return false;
	}

}