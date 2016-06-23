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
import com.primeton.paas.manage.api.model.CapFunction;
import com.primeton.paas.manage.api.model.CapResauth;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultResAuthManager {
	
	private DefaultAuthManageDao dacbDao = DefaultAuthManageDao.getInstance();
	
	public static String CAP_RESAUTH_ENTITY_PARTY_ID_PROPERTY = "partyId";

	public static String CAP_RESAUTH_ENTITY_PARTY_TYPE_PROPERTY = "partyType";

	public static String CAP_RESAUTH_ENTITY_RES_TYPE_PROPERTY = "resType";

	public static String CAP_RESAUTH_ENTITY_RES_ID_PROPERTY = "resId";
	
	/**
	 * Default. <br>
	 */
	public DefaultResAuthManager() {
	}

	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	public CapResauth[] getAllCapResauth() throws DaoException {
		return getCapResauthByCriteria(null);
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public CapResauth[] getCapResauthByCriteria(Map<String, String> criteria)
			throws DaoException {
		List<CapResauth> list = dacbDao.getCapResauthListByCriteria(criteria);
		return list.toArray(new CapResauth[list.size()]);
	}
	
	/**
	 * 
	 * @param partyId
	 * @param partyTypeId
	 * @param resType
	 * @return
	 * @throws DaoException
	 */
	public CapResauth[] getCapResauthListByResType(String partyId,
			String partyTypeId, String resType) throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY,
				IAuthConstants.DEFAULT_TENANT_ID);
		criteria.put(CAP_RESAUTH_ENTITY_PARTY_ID_PROPERTY, partyId);
		criteria.put(CAP_RESAUTH_ENTITY_PARTY_TYPE_PROPERTY, partyTypeId);
		criteria.put(CAP_RESAUTH_ENTITY_RES_TYPE_PROPERTY, resType);
		List<CapResauth> list = dacbDao.getCapResauthListByCriteria(criteria);

		return list.toArray(new CapResauth[list.size()]);
	}

	/**
	 * 
	 * @param partyId
	 * @param partyTypeId
	 * @param resId
	 * @param resType
	 * @return
	 * @throws DaoException
	 */
	public CapResauth getCapResauthByResIdAndType(String partyId,
			String partyTypeId, String resId, String resType)
			throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY,
				IAuthConstants.DEFAULT_TENANT_ID);
		criteria.put(CAP_RESAUTH_ENTITY_PARTY_ID_PROPERTY, partyId);
		criteria.put(CAP_RESAUTH_ENTITY_PARTY_TYPE_PROPERTY, partyTypeId);
		criteria.put(CAP_RESAUTH_ENTITY_RES_TYPE_PROPERTY, resType);
		criteria.put(CAP_RESAUTH_ENTITY_RES_ID_PROPERTY, resId);
		List<CapResauth> list = dacbDao.getCapResauthListByCriteria(criteria);
		return list.get(0);
	}

	/**
	 * 
	 * @param capResauth
	 * @throws DaoException
	 */
	public void insertCapResauth(CapResauth capResauth) throws DaoException {
		dacbDao.insertCapResauth(capResauth);
	}

	/**
	 * 
	 * @param capResauths
	 * @throws DaoException
	 */
	private void insertCapResauthBatch(CapResauth[] capResauths)
			throws DaoException {
		for (CapResauth capResauth : capResauths) {
			insertCapResauth(capResauth);
		}
	}

	/**
	 * 
	 * @param capResauth
	 * @throws DaoException
	 */
	public void deleteCapResauth(CapResauth capResauth) throws DaoException {
		dacbDao.deleteCapResauth(capResauth);
	}

	/**
	 * 
	 * @param capResauths
	 * @throws DaoException
	 */
	public void deleteCapResauthBatch(CapResauth[] capResauths)
			throws DaoException {
		for (CapResauth capResauth : capResauths) {
			deleteCapResauth(capResauth);
		}
	}

	/**
	 * 
	 * @param toAdd
	 * @param toDel
	 * @param roleId
	 * @throws DaoException
	 */
	public void addAndDelAuth(CapFunction[] toAdd, CapFunction[] toDel,
			String roleId) throws DaoException {
		if (toAdd != null && toAdd.length > 0) {
			List<CapResauth> toAddAuth = new ArrayList<CapResauth>();
			for (CapFunction func : toAdd) {
				CapResauth cr = new CapResauth();
				cr.setTenantId(IAuthConstants.DEFAULT_TENANT_ID);
				cr.setPartyId(roleId);
				cr.setPartyType(IAuthConstants.DEFAULT_ROLE_TYPE);
				cr.setResId(func.getFuncId());
				cr.setResType(IAuthConstants.DEFAULT_RES_TYPE);
				cr.setResState(IAuthConstants.DEFAULT_RES_STATE);
				cr.setPartyScope(IAuthConstants.DEFAULT_PARTY_SCOPE);
				cr.setCreateuser(func.getCreateuser());
				toAddAuth.add(cr);
			}
			insertCapResauthBatch(toAddAuth.toArray(new CapResauth[toAddAuth
					.size()]));
		}
		if (toDel != null && toDel.length > 0) {
			List<CapResauth> toDelAuth = new ArrayList<CapResauth>();
			for (CapFunction func : toDel) {
				CapResauth cr = new CapResauth();
				cr.setTenantId(IAuthConstants.DEFAULT_TENANT_ID);
				cr.setPartyId(roleId);
				cr.setPartyType(IAuthConstants.DEFAULT_ROLE_TYPE);
				cr.setResId(func.getFuncId());
				cr.setResType(IAuthConstants.DEFAULT_RES_TYPE);
				cr.setResState(IAuthConstants.DEFAULT_RES_STATE);
				cr.setPartyScope(IAuthConstants.DEFAULT_PARTY_SCOPE);
				cr.setCreateuser(func.getCreateuser());
				toDelAuth.add(cr);
			}
			deleteCapResauthBatch(toDelAuth.toArray(new CapResauth[toDelAuth
					.size()]));
		}
	}
	
	/**
	 * 
	 * @param partyId
	 * @return
	 */
	public CapResauth[] getCapResauthListByParty(String partyId) {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY,
				IAuthConstants.DEFAULT_TENANT_ID);
		criteria.put(CAP_RESAUTH_ENTITY_PARTY_ID_PROPERTY, partyId);
		List<CapResauth> list = new ArrayList<CapResauth>();
		try {
			list = dacbDao.getCapResauthListByCriteria(criteria);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return list.toArray(new CapResauth[list.size()]);
	}

	/**
	 * 
	 * @param partyId
	 * @throws DaoException
	 */
	public void deleteCapResauthByParty(String partyId) throws DaoException {
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(IAuthConstants.TENANT_PROPERTY,
				IAuthConstants.DEFAULT_TENANT_ID);
		criteria.put(CAP_RESAUTH_ENTITY_PARTY_ID_PROPERTY, partyId);
		// criteria.put(CAP_RESAUTH_ENTITY_PARTY_TYPE_PROPERTY, partyTypeId);
		dacbDao.deleteCapResauthByCriteria(criteria);
	}
	
	/**
	 * 
	 * @param partyId
	 * @return
	 * @throws DaoException
	 */
	public String[] getFunctionIdByPartyId(String partyId) throws DaoException {
		List<String> list = dacbDao.getFunctionIdByPartyId(partyId);
		return list.toArray(new String[list.size()]);
	}
	
}