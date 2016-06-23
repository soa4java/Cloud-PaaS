/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.IAuthConstants;
import com.primeton.paas.manage.api.model.CapFunction;
import com.primeton.paas.manage.api.model.CapMenu;
import com.primeton.paas.manage.api.model.CapPartyauth;
import com.primeton.paas.manage.api.model.CapResauth;
import com.primeton.paas.manage.api.model.CapRole;
import com.primeton.paas.manage.api.model.CapUser;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultAuthManageDao {

	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultAuthManageDao.class);
	
	private final static String AUTH_BASE_SQL_MAP = "authBaseSqlMap";

	private static DefaultAuthManageDao instance = new DefaultAuthManageDao();

	private static BaseDao baseDao = BaseDao.getInstance();

	// 参与者ID
	private static String CAP_RESAUTH_ENTITY_PARTY_ID_PROPERTY = "partyId";

	// 参与者类型
	private static String CAP_RESAUTH_ENTITY_PARTY_TYPE_PROPERTY = "partyType";

	// 资源类型
	private static String CAP_RESAUTH_ENTITY_RES_TYPE_PROPERTY = "resType";
	
	private static String CAP_FUNCTION_ENTITY_ID_PROPERTY = "funcId";
	

	private DefaultAuthManageDao() {
	}

	public static DefaultAuthManageDao getInstance() {
		return instance;
	}

	private String getSqlMap(String sqlId) {
		return AUTH_BASE_SQL_MAP + "." + sqlId;
	}

	/**
	 * 新增功能
	 * @param func
	 * @throws DaoException
	 */
	public void addFunction(CapFunction func) throws DaoException {
		if (func == null)
			return;
		baseDao.insert(getSqlMap("addFunction"), func); //$NON-NLS-1$
	}
	
	/**
	 * 修改功能
	 * @param func
	 * @throws DaoException
	 */
	public void updateFunction(CapFunction func) throws DaoException {
		if (func == null)
			return;
		baseDao.update(getSqlMap("updateFunction"), func); //$NON-NLS-1$
	}
	
	/**
	 * 根据id，批量删除功能
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public boolean deleteFunction(Map<String,String> criteria) throws DaoException {
		if (criteria == null)
			return false;
		String funcIdStr = criteria.get(CAP_FUNCTION_ENTITY_ID_PROPERTY);
		String[] funcIds = funcIdStr.split(",");
		for (String funcId : funcIds) {
			Map<String,String> c = new HashMap<String, String>();
			c.put(IAuthConstants.TENANT_PROPERTY, criteria.get(IAuthConstants.DEFAULT_TENANT_ID));
			c.put(CAP_FUNCTION_ENTITY_ID_PROPERTY, funcId);
			baseDao.delete(getSqlMap("deleteFunction"), criteria); //$NON-NLS-1$
		}
		return true;
	}
	
	/**
	 * 
	 * @param funcIds
	 * @return
	 * @throws DaoException
	 */
	public boolean deleteFunctionBatchByFunctionIds(String[] funcIds)
			throws DaoException {
		List<String> list = Arrays.asList(funcIds);
		baseDao.delete(getSqlMap("deleteFunctionBatchByFunctionIds"), list); //$NON-NLS-1$
		return true;
	}
	
	/**
	 * 获取所有功能项
	 * @return
	 * @throws DaoException 
	 */
	public List<CapFunction> getAllFunctions() throws DaoException {
		return baseDao.queryForList(getSqlMap("getAllFunctions")); //$NON-NLS-1$
	}
	
	/**
	 * 根据funcId功能id，获取功能信息
	 * @param funcId
	 * @return
	 * @throws DaoException 
	 */
	public CapFunction getFunctionByFuncId(String funcId) throws DaoException {
		if (StringUtil.isEmpty(funcId)) {
			return null;
		}
		return baseDao.queryForObject(getSqlMap("getFunctionByFuncId"), funcId); //$NON-NLS-1$
	}
	
	/**
	 * 根据不同条件查询功能信息
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public List<CapFunction> getFunctionsByCriteria(
			Map<String, String> criteria, IPageCond pageCond)
			throws DaoException {
		return baseDao.queryForList(getSqlMap("getFunctionsByCriteria"), //$NON-NLS-1$
				criteria, pageCond);
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public List<CapFunction> getFunctionsByCriteria(Map<String, String> criteria)
			throws DaoException {
		return baseDao.queryForList(getSqlMap("getFunctionsByCriteria"), //$NON-NLS-1$
				criteria);
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public int getFunctionCountByCriteria(Map<String,String> criteria) throws DaoException {
		Integer size = baseDao.queryForObject(getSqlMap("getFunctionCountByCriteria"), criteria); //$NON-NLS-1$
		return null == size ? 0 : size;
	}
	
	
	
	/**cap_resauth 授权资源管理 DefaultResAuthManager */
	/**
	 * 查询角色和资源授权信息
	 * @param tenantId
	 * @param partyId
	 * @param partyType
	 * @param resType
	 * @return
	 * @throws DaoException 
	 */
	public List<CapResauth> getCapResauthListByCriteria(
			Map<String, String> criteria) throws DaoException {
		return baseDao.queryForList(
				getSqlMap("getCapResauthListByCriteria"), criteria); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param capResauth
	 * @throws DaoException
	 */
	public void insertCapResauth(CapResauth capResauth) throws DaoException {
		if (null == capResauth) {
			return;
		}
		baseDao.insert(getSqlMap("addCapResauth"), capResauth); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public void deleteCapResauthByCriteria(Map<String,String> criteria) throws DaoException {
		baseDao.delete(getSqlMap("deleteCapResauthByCriteria"), criteria); //$NON-NLS-1$
	}
	
	public void deleteCapResauth(CapResauth capResauth) throws DaoException {
		baseDao.delete(getSqlMap("deleteCapResauth"), capResauth); //$NON-NLS-1$
	}
	
	/**
	 * 根据 res_id
	 * @param resAuths
	 * @throws DaoException
	 */
	public void deleteCapResauthBatch(CapResauth[] resAuths) throws DaoException {
		if (null == resAuths || resAuths.length == 0) {
			return;
		}
		List<CapResauth> list = Arrays.asList(resAuths);
		baseDao.delete(getSqlMap("deleteCapResauthBatch"), list); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param partyId
	 * @return
	 * @throws DaoException
	 */
	public List<String> getFunctionIdByPartyId(String partyId) throws DaoException {
		if (StringUtil.isEmpty(partyId)) {
			return new ArrayList<String>();
		}
		return baseDao.queryForList(getSqlMap("getFunctionIdByPartyId"), partyId); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param tenantId
	 * @param partyId
	 * @param partyType
	 * @param resType
	 * @return
	 */
	public int getCount(String tenantId, String partyId, String partyType, String resType) {
		if (StringUtil.isEmpty(tenantId)) {
			return 0;
		}
		try {
			Map<String, String> criteria = new HashMap<String, String>();
			criteria.put(IAuthConstants.TENANT_PROPERTY, tenantId);
			criteria.put(CAP_RESAUTH_ENTITY_PARTY_ID_PROPERTY, partyId);
			criteria.put(CAP_RESAUTH_ENTITY_PARTY_TYPE_PROPERTY, partyType);
			criteria.put(CAP_RESAUTH_ENTITY_RES_TYPE_PROPERTY, resType);
			
			Integer count = baseDao.queryForObject(getSqlMap("getCount"), criteria);
			return null == count ? 0 : count;
		} catch (DaoException e) {
			logger.error(e);
		}
		return 0;
	}
	
	/**
	 * cap_menu 菜单管理DefaultMenuManager
	 * 
	 * @param menu
	 * @throws DaoException
	 */
	public void addCapMenu(CapMenu menu) throws DaoException {
		if (null == menu) {
			return;
		}
		try {
			baseDao.insert(getSqlMap("addCapMenu"), menu); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
	}
	
	/**
	 * 
	 * @param parentId
	 * @return
	 * @throws DaoException
	 */
	public List<CapMenu> getCapMenuByParentMenuId(String parentId) throws DaoException {
		if (StringUtil.isEmpty(parentId)) {
			return new ArrayList<CapMenu>();
		}
		return baseDao.queryForList(getSqlMap("getCapMenuByParentMenuId"), parentId); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	public List<CapMenu> getAllCatalogMenus() throws DaoException {
		return baseDao.queryForList(getSqlMap("getAllCatalogMenus")); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param menuCode
	 * @return
	 * @throws DaoException
	 */
	public CapMenu getCapMenuByMenuCode(String menuCode) throws DaoException {
		return baseDao.queryForObject(getSqlMap("getCapMenuByMenuCode"), menuCode); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param menuId
	 * @return
	 * @throws DaoException
	 */
	public CapMenu getCapMenuByMenuId(String menuId) throws DaoException {
		return baseDao.queryForObject(getSqlMap("getCapMenuByMenuId"), menuId); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public List<CapMenu> getCapMenuByCriteria(Map<String, String> criteria)
			throws DaoException {
		return baseDao.queryForList(getSqlMap("getCapMenuByCriteria"), //$NON-NLS-1$
				null == criteria ? new HashMap<String, String>() : criteria);
	}
	
	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public List<CapMenu> getCapMenuByCriteria(Map<String, String> criteria,
			IPageCond page) throws DaoException {
		return baseDao.queryForList(getSqlMap("getCapMenuByCriteria"), //$NON-NLS-1$
				criteria, page);
	}
	
	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	public String getCapMenuMaxPrimaryKey() throws DaoException {
		return baseDao.queryForObject(getSqlMap("getCapMenuMaxPrimaryKey")); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param resourceId
	 * @throws DaoException
	 */
	public void deleteMenuByLinkRes(String resourceId) throws DaoException {
		if (StringUtil.isEmpty(resourceId)) {
			return;
		}
		baseDao.delete(getSqlMap("deleteCapMenuByResourceId"), resourceId); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param resourceIds
	 * @throws DaoException
	 */
	public void deleteMenuByLinkRess(String[] resourceIds) throws DaoException {
		if (null == resourceIds || resourceIds.length == 0) {
			return;
		}
		baseDao.delete(getSqlMap("deleteCapMenuByResourceId"),  Arrays.asList(resourceIds)); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param menus
	 * @throws DaoException
	 */
	public void deleteMenuBatchByMenuIds(List<CapMenu> menus) throws DaoException {
		if (null == menus || menus.isEmpty()) {
			return;
		}
		baseDao.delete(getSqlMap("deleteMenuBatchByMenuIds"), menus); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param resourceIds
	 * @throws DaoException
	 */
	public void deleteMenuByLinkResourceChange(String[] resourceIds) throws DaoException {
		if (null == resourceIds || resourceIds.length == 0) {
			return;
		}
		for (String resourceId : resourceIds) {
			Map<String, String> criteria = new HashMap<String, String>();
			criteria.put("linkres", resourceId); //$NON-NLS-1$
			List<CapMenu> tempList = getCapMenuByCriteria(criteria);
			for (CapMenu tempMenu : tempList) {
				if ("1".equals(tempMenu.getIsleaf())) { //$NON-NLS-1$
					baseDao.delete(getSqlMap("deleteCapMenuByMenuId"), //$NON-NLS-1$
							tempMenu.getMenuId());
				}
			}
		}
	}
	
	/**
	 * 
	 * @param menuId
	 * @throws DaoException
	 */
	public void deleteMenuByMenuId(String menuId) throws DaoException {
		if (StringUtil.isEmpty(menuId)) {
			return;
		}
		baseDao.delete(getSqlMap("deleteCapMenuByMenuId"), menuId); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param menuId
	 * @throws DaoException
	 */
	public void updateMenuByMenuId(CapMenu capMenu) throws DaoException {
		if (null == capMenu) {
			return;
		}
		baseDao.update(getSqlMap("updateCapMenuByMenuId"), capMenu); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param capPartyauth
	 * @throws DaoException 
	 */
	public int addPartyAuth(CapPartyauth capPartyauth) throws DaoException {
		return null == capPartyauth ? -1 : baseDao.insert(getSqlMap("addPartyAuth"), capPartyauth); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public List<CapPartyauth> getCapPartyauthByCriteria(Map<String,String> criteria) 
			throws DaoException {
		return baseDao.queryForList(getSqlMap("getCapPartyauthByCriteria") //$NON-NLS-1$
				, null == criteria ? new HashMap<String, String>() : criteria);
	}
	
	/**
	 * 
	 * @param capPartyauths
	 * @throws DaoException
	 */
	public void deletePartyAuthByIds(CapPartyauth[] capPartyauths) throws DaoException {
		if (null == capPartyauths || capPartyauths.length == 0) {
			return;
		}
		baseDao.delete(getSqlMap("deletePartyAuthByIds"), Arrays.asList(capPartyauths));
	}
	
	/**
	 * 
	 * @param criteria
	 * @throws DaoException
	 */
	public void deletePartyAuthByCriteria(Map<String,String> criteria) 
			throws DaoException {
		baseDao.delete(getSqlMap("deletePartyAuthByCriteria") //$NON-NLS-1$
				, null == criteria ? new HashMap<String, String>() : criteria);
	}
	
	/**
	 * cap_role DefaultRoleManager角色管理
	 * 
	 * @param roleCode
	 * @return
	 * @throws DaoException
	 */
	public CapRole getRoleByRoleCode(String roleCode) throws DaoException {
		return baseDao.queryForObject(getSqlMap("getRoleByRoleCode"), roleCode); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public List<CapRole> getCapRoleByCriteria(Map<String, String> criteria,
			IPageCond page) throws DaoException {
		return baseDao.queryForList(getSqlMap("getCapRoleByCriteria") //$NON-NLS-1$
				, null == criteria ? new HashMap<String, String>() : criteria
				, null == page ? new PageCond() : page);
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public List<CapRole> getCapRoleByCriteria(Map<String,String> criteria) 
			throws DaoException {
		return baseDao.queryForList(getSqlMap("getCapRoleByCriteria")
				, null == criteria ? new HashMap<String, String>() : criteria); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public int getRoleCountByCriteria(Map<String, String> criteria) throws DaoException {
		Integer size = baseDao.queryForObject(getSqlMap("getRoleCountByCriteria") //$NON-NLS-1$
				, null == criteria ? new HashMap<String, String>() : criteria); 
		return null == size ? 0 : size;
	}
	
	/**
	 * 
	 * @param role
	 * @throws DaoException
	 */
	public int insertCapRole(CapRole role) throws DaoException {
		if (null == role) {
			return -1;
		}
		return baseDao.insert(getSqlMap("insertCapRole"), role); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	public String getCapRoleMaxPrimaryKey() throws DaoException {
		return baseDao.queryForObject(getSqlMap("getCapRoleMaxPrimaryKey")); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param role
	 * @throws DaoException
	 */
	public void updateCapRole(CapRole role) throws DaoException {
		if (null == role) {
			return;
		}
		baseDao.update(getSqlMap("updateCapRole"), role); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param roleId
	 * @throws DaoException
	 */
	public void deleteCapRoleByRoleId(String roleId) throws DaoException {
		if (StringUtil.isEmpty(roleId)) {
			return;
		}
		baseDao.delete(getSqlMap("deleteCapRole"), roleId); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param user
	 * @throws DaoException
	 */
	public void addCapUser(CapUser user) throws DaoException {
		if (null == user) {
			return;
		}
		baseDao.insert(getSqlMap("addCapUser"), user); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws DaoException
	 */
	public CapUser getCapUserByUserId(String userId) throws DaoException {
		if (StringUtil.isEmpty(userId)) {
			return null;
		}
		return baseDao.queryForObject(getSqlMap("getCapUserByUserId"), userId); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public CapUser isIllegalCapUser(Map<String,String> criteria) throws DaoException {
		return baseDao.queryForObject(getSqlMap("isIllegalCapUser") //$NON-NLS-1$
				, null == criteria ? new HashMap<String, String>() : criteria);
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 * @throws DaoException
	 */
	public int updateCapUser(CapUser user) throws DaoException {
		if (null == user) {
			return -1;
		}
		return baseDao.update(getSqlMap("updateCapUser"), user); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	public List<CapUser> getAllCapUsers() throws DaoException {
		return baseDao.queryForList(getSqlMap("getAllCapUsers")); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param criteria
	 * @param pageCond
	 * @return
	 * @throws DaoException
	 */
	public List<CapUser> getAllCapUserByCriteria(Map<String, String> criteria,
			IPageCond pageCond)	throws DaoException {
		return baseDao.queryForList(getSqlMap("getAllCapUserByCriteria") //$NON-NLS-1$
				, null == criteria ? new HashMap<String, String>() : criteria
				, null == pageCond ? new PageCond() : pageCond);
	}
	
	/**
	 * 
	 * @param criteria
	 * @param pageCond
	 * @return
	 * @throws DaoException
	 */
	public List<CapUser> getUserByCriteria(Map<String, String> criteria, IPageCond pageCond) 
			throws DaoException {
		return baseDao.queryForList(getSqlMap("getCapUserByCriteria") //$NON-NLS-1$
				, null == criteria ? new HashMap<String, String>() : criteria
				, null == pageCond ? new PageCond() : pageCond);
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public List<CapUser> getUserByCriteria(Map<String, String> criteria) throws DaoException {
		return baseDao.queryForList(getSqlMap("getCapUserByCriteria") //$NON-NLS-1$
				, null == criteria ? new HashMap<String, String>() : criteria);
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public int getUserCountByCriteria(Map<String, String> criteria) throws DaoException {
		Integer size = baseDao.queryForObject(getSqlMap("getUserCountByCriteria") //$NON-NLS-1$
				, null == criteria ? new HashMap<String, String>() : criteria);
		return null == size ? 0 : size;
	}
	
	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	public String getCapUserMaxPrimaryKey() throws DaoException {
		return baseDao.queryForObject(getSqlMap("getCapUserMaxPrimaryKey")); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws DaoException
	 */
	public int deleteCapUserById(String userId) throws DaoException {
		if (StringUtil.isEmpty(userId)) {
			return -1;
		}
		return baseDao.delete(getSqlMap("deleteCapUserById"), userId); //$NON-NLS-1$
	}
	
}
