/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;

import com.primeton.paas.manage.api.impl.dao.DefaultAuthManageDao;
import com.primeton.paas.manage.api.impl.util.IAuthConstants;
import com.primeton.paas.manage.api.model.CapFunction;
import com.primeton.paas.manage.api.model.CapPartyauth;
import com.primeton.paas.manage.api.model.CapRole;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultRoleManager {
	
	private DefaultAuthManageDao dacbDao = DefaultAuthManageDao.getInstance();
	
	public static String CAP_ROLE_ENTITY_ID_PROPERTY = "roleId";

	public static String CAP_ROLE_ENTITY_CODE_PROPERTY = "roleCode";

	public static String CAP_ROLE_ENTITY_NAME_PROPERTY = "roleName";

	public static String SPRING_BEAN_NAME = "DefaultRoleManagerBean";

	public static String CAP_PARTY_ID = "partyId";

	public static String CAP_PARTY_TYPE = "partyType";
	
	public static final String 	SYSADMIN = "sysadmin";

	private DefaultResAuthManager dram;
	
	/**
	 * Default. <br>
	 */
	public DefaultRoleManager() {
		dram = new DefaultResAuthManager();
	}

	/**
	 * 
	 * @param role
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public CapRole[] getRolesByCriteria(CapRole role, IPageCond page)
			throws DaoException {
		if (role == null && page == null) {
			return getAllRoleList();
		}
		String roleId = null;
		if (role != null) {
			roleId = role.getRoleId();
		}
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY,
				IAuthConstants.DEFAULT_TENANT_ID);
		List<CapRole> list = null;
		if (StringUtil.isEmpty(roleId) || "0".equals(roleId)) { //$NON-NLS-1$
			if (role != null) {
				String roleCode = role.getRoleCode();
				String roleName = role.getRoleName();
				if (StringUtil.isNotEmpty(roleCode)) {
					criteria.put(CAP_ROLE_ENTITY_CODE_PROPERTY, roleCode);
				}
				if (StringUtil.isNotEmpty(roleName)) {
					criteria.put(CAP_ROLE_ENTITY_NAME_PROPERTY, roleName);
				}
			}
			if (page == null) {
				list = dacbDao.getCapRoleByCriteria(criteria);
			} else {
				if (page.getCount() <= 0) {
					page.setCount(dacbDao.getRoleCountByCriteria(criteria));
				}
				list = dacbDao.getCapRoleByCriteria(criteria, page);
			}
			if (list != null) {
				return list.toArray(new CapRole[list.size()]);
			}
			return new CapRole[0];
		} else {
			criteria.put(CAP_ROLE_ENTITY_ID_PROPERTY, roleId);
			return getCapRoleByCriteria(criteria, page);
		}
	}

	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public CapRole[] getCapRoleByCriteria(Map<String, String> criteria,
			IPageCond page) throws DaoException {
		List<CapRole> list = null;
		if (page == null) {
			list = dacbDao.getCapRoleByCriteria(criteria, null);
		} else {
			if (page.getCount() <= 0) {
				page.setCount(dacbDao.getRoleCountByCriteria(criteria));
			}
			list = dacbDao.getCapRoleByCriteria(criteria, page);
		}
		if (list != null) {
			return list.toArray(new CapRole[list.size()]);
		}
		return new CapRole[0];
	}

	/**
	 * 
	 * @param roleCode
	 * @return
	 * @throws DaoException
	 */
	public CapRole getRoleByRoleCode(String roleCode) throws DaoException {
		return dacbDao.getRoleByRoleCode(roleCode);
	}
	
	/**
	 * 
	 * @param roleID
	 * @param tenantID
	 * @return
	 * @throws DaoException
	 */
	public CapRole getRoleByRoleIDAndTenant(String roleID, String tenantID)
			throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY,
				IAuthConstants.DEFAULT_TENANT_ID);
		criteria.put(CAP_ROLE_ENTITY_ID_PROPERTY, roleID);
		List<CapRole> list = dacbDao.getCapRoleByCriteria(criteria, null);
		return list.get(0);
	}

	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	public CapRole[] getAllRoleList() throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		List<CapRole> list = dacbDao.getCapRoleByCriteria(criteria);
		return list.toArray(new CapRole[list.size()]);
	}

	/**
	 * 
	 * @param roleId
	 * @return
	 * @throws DaoException
	 */
	public CapRole getRoleByRoleId(String roleId) throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY,
				IAuthConstants.DEFAULT_TENANT_ID);
		criteria.put(CAP_ROLE_ENTITY_ID_PROPERTY, roleId);
		List<CapRole> list = dacbDao.getCapRoleByCriteria(criteria);
		return list.get(0);
	}

	/**
	 * 
	 * @param roleCode
	 * @return
	 * @throws DaoException
	 */
	public boolean isRoleCodeExist(String roleCode) throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY,
				IAuthConstants.DEFAULT_TENANT_ID);
		criteria.put(CAP_ROLE_ENTITY_CODE_PROPERTY, roleCode);
		List<CapRole> list = dacbDao.getCapRoleByCriteria(criteria);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param role
	 * @return
	 * @throws DaoException
	 */
	public boolean insertRole(CapRole role) throws DaoException {
		role.setTenantId(IAuthConstants.DEFAULT_TENANT_ID);
		role.setCreatetime(new Date());
		if (role.getCreateuser() == null) {
			role.setCreateuser(SYSADMIN);
		}
		if (role.getRoleId() == null) {
			String maxKey = dacbDao.getCapRoleMaxPrimaryKey();
			String nextKey = String.valueOf(Integer.parseInt(maxKey) + 1);
			role.setRoleId(nextKey);
		}
		dacbDao.insertCapRole(role);
		return true;
	}

	/**
	 * 
	 * @param role
	 * @return
	 * @throws DaoException
	 */
	public boolean updateRole(CapRole role) throws DaoException {
		if (role.getRoleCode() != null) {
			dacbDao.updateCapRole(role);
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param partyTypeId
	 * @param partyId
	 * @param O
	 * @return
	 * @throws DaoException
	 */
	public CapRole[] getCapRoleListByAuthParty(String partyTypeId,
			String partyId, String O) throws DaoException {
		Map<String, String> partyauthCriteria = new HashMap<String, String>();
		partyauthCriteria.put(IAuthConstants.TENANT_PROPERTY,
				IAuthConstants.DEFAULT_TENANT_ID);
		partyauthCriteria.put(CAP_PARTY_ID, partyId);
		partyauthCriteria.put(CAP_PARTY_TYPE, partyTypeId);
		List<CapPartyauth> list = dacbDao
				.getCapPartyauthByCriteria(partyauthCriteria);
		List<CapRole> returnList = new ArrayList<CapRole>();
		if (!list.isEmpty()) {
			for (CapPartyauth authRole : list) {
				returnList.add(getRoleByRoleId(authRole.getRoleId()));
			}
		}
		return returnList.toArray(new CapRole[returnList.size()]);
	}

	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public CapRole[] queryRoleListByCriteria(Map<String, String> criteria)
			throws DaoException {
		List<CapRole> list = dacbDao.getCapRoleByCriteria(criteria);
		return list.toArray(new CapRole[list.size()]);
	}

	/**
	 * 
	 * @param roleId
	 * @throws DaoException
	 */
	public void deleteRoleByRoleId(String roleId) throws DaoException {
		saveAuthWithFunc(roleId, null);
		dacbDao.deleteCapRoleByRoleId(roleId);
	}

	/**
	 * 
	 * @param roleIds
	 * @throws DaoException
	 */
	public void deleteRoleBatch(String[] roleIds) throws DaoException {
		for (String roleId : roleIds) {
			deleteRoleByRoleId(roleId);
		}
	}
	
	/**
	 * 
	 * @param roleId
	 * @return
	 * @throws DaoException
	 */
	public CapFunction[] getCapFunctionByRoleId(String roleId)
			throws DaoException {
		String[] funcIds = dram.getFunctionIdByPartyId(roleId);
		List<CapFunction> allFunctions = dacbDao.getAllFunctions();
		List<CapFunction> reList = new ArrayList<CapFunction>();
		for (CapFunction func : allFunctions) {
			for (String funcId : funcIds) {
				if (funcId.equals(func.getFuncId())) {
					reList.add(func);
				}
			}
		}
		return reList.toArray(new CapFunction[reList.size()]);
	}

	/**
	 * 
	 * @param roleId
	 * @param capFuncs
	 * @throws DaoException
	 */
	public void saveAuthWithFunc(String roleId, CapFunction[] capFuncs)
			throws DaoException {
		if (capFuncs == null || capFuncs.length <= 0) {
			dram.deleteCapResauthByParty(roleId);
			return;
		}
		CapFunction[] authFuncs = null;
		try {
			authFuncs = getCapFunctionByRoleId(roleId);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		List<CapFunction> toAdd = new ArrayList<CapFunction>();
		List<CapFunction> toDel = new ArrayList<CapFunction>();
		for (CapFunction capFunc : authFuncs) {
			if (!contain(capFuncs, capFunc)) {
				toDel.add(capFunc);
			}
		}
		for (CapFunction func : capFuncs) {
			if (!contain(authFuncs, func)) {
				toAdd.add(func);
			}
		}
		dram.addAndDelAuth(toAdd.toArray(new CapFunction[toAdd.size()]),
				toDel.toArray(new CapFunction[toDel.size()]), roleId);
	}
	
	/**
	 * 
	 * @param authRoles
	 * @param capRole
	 * @return
	 */
	private boolean contain(CapFunction[] authFuncs, CapFunction func) {
		for (CapFunction authFunc : authFuncs) {
			if (func.getFuncId().equals(authFunc.getFuncId())) {
				return true;
			}
		}
		return false;
	}

}