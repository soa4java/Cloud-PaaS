/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;

import com.primeton.paas.manage.api.impl.dao.DefaultAuthManageDao;
import com.primeton.paas.manage.api.impl.util.IAuthConstants;
import com.primeton.paas.manage.api.model.CapPartyauth;
import com.primeton.paas.manage.api.model.CapRole;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultPartyAuthManager {
	
	private DefaultAuthManageDao dacbDao = DefaultAuthManageDao.getInstance();
	
	public static String CAP_PARTYAUTH_ENTITY_ROLE_TYPE_PROPERTY = "roleType";
	
	public static String CAP_PARTYAUTH_ENTITY_ROLE_ID_PROPERTY = "roleId";

	public static String CAP_PARTYAUTH_ENTITY_PARTY_ID_PROPERTY = "partyId";

	public static String CAP_PARTYAUTH_ENTITY_PARTY_TYPE_PROPERTY = "partyType";

	/**
	 * 
	 * @param capPartyauth
	 * @return
	 * @throws DaoException
	 */
	public int savePartyAuth(CapPartyauth capPartyauth) throws DaoException {
		return dacbDao.addPartyAuth(capPartyauth);
	}
	
	/**
	 * 
	 * @param capPartyauths
	 * @throws DaoException
	 */
	public void savePartyAuthBatch(CapPartyauth[] capPartyauths)
			throws DaoException {
		for (CapPartyauth capPartyauth : capPartyauths) {
			savePartyAuth(capPartyauth);
		}
	}
	
	/**
	 * 
	 * @param partyId
	 * @return
	 * @throws DaoException
	 */
	public String[] getCapRoleIdByPartyId(String partyId) throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		if (partyId != null) {
			criteria.put(CAP_PARTYAUTH_ENTITY_PARTY_ID_PROPERTY, partyId);
		}
		CapPartyauth[] partyAuths = getCapPartyauthByCriteria(criteria);
		List<String> roleIds = new ArrayList<String>();
		for (CapPartyauth partyAuth : partyAuths) {
			roleIds.add(partyAuth.getRoleId());
		}
		return roleIds.toArray(new String[roleIds.size()]);
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public CapPartyauth[] getCapPartyauthByCriteria(Map<String, String> criteria)
			throws DaoException {
		List<CapPartyauth> list = dacbDao.getCapPartyauthByCriteria(criteria);
		return list.toArray(new CapPartyauth[list.size()]);
	}

	/**
	 * 
	 * @param capPartyauth
	 * @throws DaoException
	 */
	public void deletePartyAuth(CapPartyauth capPartyauth) throws DaoException {
		deletePartyAuthByPartyId(capPartyauth.getPartyId());
	}

	/**
	 * 
	 * @param capPartyauths
	 * @throws DaoException
	 */
	public void deletePartyAuthBatch(CapPartyauth[] capPartyauths)
			throws DaoException {
		for (CapPartyauth capPartyauth : capPartyauths) {
			delPartyAuthByRole(capPartyauth.getRoleId(),
					capPartyauth.getPartyId());
		}
	}

	/**
	 * 
	 * @param toAdd
	 * @param toDel
	 * @throws DaoException
	 */
	public void insertAndDelete(CapPartyauth[] toAdd, CapPartyauth[] toDel)
			throws DaoException {
		if (toAdd != null && toAdd.length > 0) {
			this.savePartyAuthBatch(toAdd);
		}
		if (toDel != null && toDel.length > 0) {
			this.deletePartyAuthBatch(toDel);
		}
	}
	
	/**
	 * 
	 * @param toAdd
	 * @param toDel
	 * @param partyId
	 * @throws DaoException
	 */
	public void addAndDelAuth(CapRole[] toAdd, CapRole[] toDel, String partyId)
			throws DaoException {
		if (toAdd != null && toAdd.length > 0) {
			List<CapPartyauth> toAddAuth = new ArrayList<CapPartyauth>();
			for (CapRole role : toAdd) {
				CapPartyauth cp = new CapPartyauth();
				cp.setTenantId(IAuthConstants.DEFAULT_TENANT_ID);
				cp.setRoleId(role.getRoleId());
				cp.setRoleType(IAuthConstants.DEFAULT_ROLE_TYPE);
				cp.setPartyId(partyId);
				cp.setPartyType(IAuthConstants.DEFAULT_PARTY_TYPE);
				cp.setCreateuser(role.getCreateuser());
				cp.setCreatetime(role.getCreatetime());
				toAddAuth.add(cp);
			}
			this.savePartyAuthBatch(toAddAuth
					.toArray(new CapPartyauth[toAddAuth.size()]));
		}
		if (toDel != null && toDel.length > 0) {
			List<CapPartyauth> toDelAuth = new ArrayList<CapPartyauth>();
			for (CapRole role : toDel) {
				CapPartyauth cp = new CapPartyauth();
				cp.setTenantId(IAuthConstants.DEFAULT_TENANT_ID);
				cp.setRoleId(role.getRoleId());
				cp.setRoleType(IAuthConstants.DEFAULT_ROLE_TYPE);
				cp.setPartyId(partyId);
				cp.setPartyType(IAuthConstants.DEFAULT_PARTY_TYPE);
				cp.setCreateuser(role.getCreateuser());
				cp.setCreatetime(role.getCreatetime());
				toDelAuth.add(cp);
			}
			this.deletePartyAuthBatch(toDelAuth
					.toArray(new CapPartyauth[toDelAuth.size()]));
		}
	}

	/**
	 * 
	 * @param roleId
	 * @param partyId
	 * @throws DaoException
	 */
	public void delPartyAuthByRole(String roleId, String partyId)
			throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY,
				IAuthConstants.DEFAULT_TENANT_ID);
		criteria.put(CAP_PARTYAUTH_ENTITY_ROLE_ID_PROPERTY, roleId);
		criteria.put(CAP_PARTYAUTH_ENTITY_PARTY_ID_PROPERTY, partyId);
		dacbDao.deletePartyAuthByCriteria(criteria);
	}

	/**
	 * 
	 * @param partyId
	 * @throws DaoException
	 */
	public void deletePartyAuthByPartyId(String partyId) throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY,
				IAuthConstants.DEFAULT_TENANT_ID);
		criteria.put(CAP_PARTYAUTH_ENTITY_PARTY_ID_PROPERTY, partyId);
		dacbDao.deletePartyAuthByCriteria(criteria);
	}
	
}