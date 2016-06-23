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
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultAuthManageDao;
import com.primeton.paas.manage.api.impl.util.IAuthConstants;
import com.primeton.paas.manage.api.impl.util.MD5Util;
import com.primeton.paas.manage.api.model.CapPartyauth;
import com.primeton.paas.manage.api.model.CapRole;
import com.primeton.paas.manage.api.model.CapUser;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultUserManager {

	private ILogger log = ManageLoggerFactory.getLogger(DefaultRoleManager.class);
	
	private DefaultAuthManageDao dacbDao = DefaultAuthManageDao.getInstance();

	public final static String DEFAULT_PASSWORKD = "000000";
	
	public final static String CAP_USER_ENTITY_USERID_PROPERTY = "userId";
	
	public final static String CAP_USER_ENTITY_USERSTATUS_PROPERTY = "status";
	
	public final static String CAP_USER_ENTITY_PASSWORD_PROPERTY = "password";

	public final static String CAP_USER_ENTITY_OPERATORID_PROPERTY = "operatorId";

	public final static String CAP_USER_ENTITY_USERNAME_PROPERTY = "userName";

	public final static String CAP_USER_ENTITY_USEREMAIL_PROPERTY = "email";
	
	public final static String CAP_USER_ENTITY_USERPHONE_PROPERTY = "phone";
	
	public final static String CAP_USER_ENTITY_USERTEL_PROPERTY = "tel";
	
	public final static String CAP_USER_ENTITY_CREATEUSER_PROPERTY = "createuser";

	public final static String CAP_PARTY_ID = "partyId";

	public final static String CAP_PARTY_TYPE = "partyType";

	public static final String 	DEFAULD_CREATER = "sysadmin";
	
	private DefaultPartyAuthManager dpam;
	
	/**
	 * Default. <br>
	 */
	public DefaultUserManager() {
		dpam = new DefaultPartyAuthManager();
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public boolean createUser(CapUser user) {
		if(user==null||user.getUserId()==null){
			return false;
		}
		try {
			if(user.getPassword()==null){
				user.setPassword(DEFAULT_PASSWORKD);
			}
			user.setPassword(MD5Util.md5(user.getPassword()));
			if (user.getStatus() == 0) { //$NON-NLS-1$
				user.setStatus(4);
			}
			user.setTenantId(IAuthConstants.DEFAULT_TENANT_ID);
			user.setCreatetime(new Date());
			if(user.getCreateuser()==null){
				user.setCreateuser(DEFAULD_CREATER);
			}
			user.setOperatorId(getCapUserNextKey());
			dacbDao.addCapUser(user);
			return true;
		} catch (Throwable t) {
			log.error("Add user error: " + t.getCause().getMessage());
			return false;
		}
	}

	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	private synchronized String getCapUserNextKey() throws DaoException{
		String maxKey = dacbDao.getCapUserMaxPrimaryKey();
		String nextKey = "100";
		if(maxKey!=null){
			nextKey = String.valueOf(Long.parseLong(maxKey)+1);
		}
		return nextKey;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws DaoException
	 */
	public boolean deleteUser(String userId) throws DaoException {
		saveUserAuth(userId, null);
		int result = dacbDao.deleteCapUserById(userId);
		log.info("Delete ["+result+"] User id:[ "+userId+" ] success!");
		return true;
	}

	/**
	 * 
	 * @param userIds
	 * @throws DaoException
	 */
	public void deleteCapUser(String[] userIds) throws DaoException {
		for (String userId : userIds) {
			deleteUser(userId);
		}
	}
	
	/**
	 * 
	 * @param userId
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean isIllegalUser(String userId, String password) throws Exception {
		String encryptPWD = MD5Util.md5(password);
		Map<String,String> criteria = new HashMap<String,String>();
		criteria.put(CAP_USER_ENTITY_USERID_PROPERTY, userId);
		criteria.put(IAuthConstants.TENANT_PROPERTY, IAuthConstants.DEFAULT_TENANT_ID);
		criteria.put(CAP_USER_ENTITY_PASSWORD_PROPERTY, encryptPWD);
		CapUser capUsers = dacbDao.isIllegalCapUser(criteria);
		if (capUsers!=null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 * @throws DaoException
	 */
	public CapUser getCapUserByUserId(String userId) throws DaoException {
		return dacbDao.getCapUserByUserId(userId);
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean updateCapUser(CapUser user) throws Exception {
		if(user.getUserId()==null){
			log.info("UserId is null");
			return false;
		}
		int result = dacbDao.updateCapUser(user);
		if(result==1){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	public CapUser[] getAllUserList() throws DaoException {
		List<CapUser> capUsers = dacbDao.getAllCapUsers();
		return capUsers.toArray(new CapUser[capUsers.size()]);
	}

	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public CapUser[] getUserByCriteria(Map<String, String> criteria,
			IPageCond page) throws DaoException {
		List<CapUser> list = new ArrayList<CapUser>();
		if (page == null) {
			list = dacbDao.getUserByCriteria(criteria);
		} else {
			if (page.getCount() <= 0) {
				page.setCount(dacbDao.getUserCountByCriteria(criteria));
			}
			list = dacbDao.getUserByCriteria(criteria,page);
		}
		
		if(list!=null&&!list.isEmpty()){
			return list.toArray(new CapUser[list.size()]);
		}
		return new CapUser[0];
	}
	
	/**
	 * 
	 * @param capUser
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public CapUser[] getUsersByCriteria(CapUser capUser, IPageCond page) throws DaoException {
		String operatorId = null;
		if (capUser != null) {
			capUser.getOperatorId();
		}
		Map<String,String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY, IAuthConstants.DEFAULT_TENANT_ID);
		List<CapUser> list = null;
		if (StringUtil.isEmpty(operatorId) || "0".equals(operatorId)) { //$NON-NLS-1$
			if (capUser != null) {
				String userId = capUser.getUserId();
				int status = capUser.getStatus();
				String userName = capUser.getUserName();
				String userEmail = capUser.getEmail();
				String userTeL = capUser.getTel();
				String userPhone = capUser.getPhone();
				String createuser = capUser.getCreateuser();
				if (StringUtil.isNotEmpty(userId)) {
					criteria.put(CAP_USER_ENTITY_USERID_PROPERTY, userId);
				}
				if (status != 0) { //$NON-NLS-1$
					criteria.put(CAP_USER_ENTITY_USERSTATUS_PROPERTY, String.valueOf(status));
				}
				if (StringUtil.isNotEmpty(userEmail)) {
					criteria.put(CAP_USER_ENTITY_USEREMAIL_PROPERTY, userEmail);
				}
				if (StringUtil.isNotEmpty(userTeL)) {
					criteria.put(CAP_USER_ENTITY_USERTEL_PROPERTY, userTeL);
				}
				if (StringUtil.isNotEmpty(userName)) {
					criteria.put(CAP_USER_ENTITY_USERNAME_PROPERTY, userName);
				}
				if (StringUtil.isNotEmpty(userPhone)) {
					criteria.put(CAP_USER_ENTITY_USERPHONE_PROPERTY, userPhone);
				}
				if (StringUtil.isNotEmpty(createuser)) {
					criteria.put(CAP_USER_ENTITY_CREATEUSER_PROPERTY, createuser);
				}
			}
			if (page == null) {
				list = dacbDao.getUserByCriteria(criteria);
			} else {
				if (page.getCount() <= 0) {
					page.setCount(dacbDao.getUserCountByCriteria(criteria));
				}
				list = dacbDao.getUserByCriteria(criteria,page);
			}
			
			if(list!=null){
				return list.toArray(new CapUser[list.size()]);
			}
			return new CapUser[0];
		}else{
			criteria.put(CAP_USER_ENTITY_OPERATORID_PROPERTY, operatorId);
			return getUserByCriteria(criteria,page);
		}
	}

	/**
	 * 
	 * @param userId
	 * @return
	 * @throws DaoException
	 */
	public boolean deleteUserAuth(String userId) throws DaoException {
		if (StringUtil.isEmpty(userId)) {
			return false;
		}
		saveUserAuth(userId, null);
		return true;
	}

	/**
	 * 
	 * @param partyId
	 * @param tenantId
	 * @return
	 * @throws DaoException
	 */
	public CapRole[] getCapRoleListByUser(String partyId, String tenantId)
			throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY,
				IAuthConstants.DEFAULT_TENANT_ID);
		criteria.put(CAP_PARTY_ID, partyId);
		List<CapPartyauth> authRoles = dacbDao
				.getCapPartyauthByCriteria(criteria);

		List<CapRole> returnList = new ArrayList<CapRole>();
		List<CapRole> allRole = dacbDao.getCapRoleByCriteria(null);
		for (CapRole role : allRole) {
			for (CapPartyauth authRole : authRoles) {
				if (authRole.getRoleId().equals(role.getRoleId())) {
					returnList.add(role);
				}
			}
		}
		return returnList.toArray(new CapRole[returnList.size()]);
	}

	/**
	 * 
	 * @param authRoles
	 * @param capRole
	 * @return
	 */
	private boolean contain(CapRole[] authRoles, CapRole capRole) {
		if (null == authRoles || authRoles.length == 0 || null == capRole) {
			return false;
		}
		for (CapRole role : authRoles) {
			if (role.getRoleId().equals(capRole.getRoleId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param userId
	 * @param roles
	 * @throws DaoException
	 */
	public void saveUserAuth(String userId, CapRole[] roles)
			throws DaoException {
		if (roles == null || roles.length <= 0) {
			dpam.deletePartyAuthByPartyId(userId);
			return;
		}
		CapRole[] authRoles = null;
		try {
			authRoles = this.getCapRoleListByUser(userId, IAuthConstants.DEFAULT_TENANT_ID);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		List<CapRole> toAdd = new ArrayList<CapRole>();
		List<CapRole> toDel = new ArrayList<CapRole>();
		for (CapRole capRole : authRoles) {
			if (!contain(roles, capRole)) {
				toDel.add(capRole);
			}
		}
		for (CapRole role : roles) {
			if (!contain(authRoles, role)) {
				toAdd.add(role);
			}
		}
		dpam.addAndDelAuth(toAdd.toArray(new CapRole[toAdd.size()]), toDel.toArray(new CapRole[toDel.size()]), userId);
	}
	
	/**
	 * 
	 * @param userId
	 * @param oldPasswd
	 * @return
	 * @throws DaoException
	 */
	public boolean validatePasswd(String userId, String oldPasswd)
			throws DaoException {
		if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(oldPasswd)) {
			return false;
		}
		CapUser user = getCapUserByUserId(userId);
		if (user != null) {
			String password = user.getPassword();
			String encrptPwd = MD5Util.md5(oldPasswd == null ? "" : oldPasswd);
			return (password != null && password.equals(encrptPwd));
		}
		return false;
	}

}