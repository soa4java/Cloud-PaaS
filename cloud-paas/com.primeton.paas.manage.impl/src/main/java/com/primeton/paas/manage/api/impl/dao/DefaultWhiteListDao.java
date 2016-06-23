/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.model.WhiteList;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultWhiteListDao implements WhiteListDao {

	private static final String NAMESPACE = "storageSqlMap";
	
	private static BaseDao baseDao = BaseDao.getInstance();
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultStorageDao.class);
	
	private static String getStatementName(String id) {
		return NAMESPACE + "." + id;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.WhiteListDao#insert(com.primeton.paas.manage.api.impl.dao.WhiteList)
	 */
	public boolean insert(WhiteList whiteList) {
		if (whiteList == null || StringUtil.isEmpty(whiteList.getId())
				|| StringUtil.isEmpty(whiteList.getIp())) {
			return false;
		}
		WhiteList obj = getWhiteList(whiteList.getId(), whiteList.getIp());
		if (obj != null) {
			return false;
		}
		try {
			baseDao.insert(getStatementName("insertWhiteList"), whiteList); //$NON-NLS-1$
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.WhiteListDao#update(com.primeton.paas.manage.api.impl.dao.WhiteList)
	 */
	public void update(WhiteList whiteList) {
		if (null == whiteList) {
			return;
		}
		try {
			baseDao.update(getStatementName("updateWhiteList"), whiteList); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.WhiteListDao#delete(java.lang.String, java.lang.String)
	 */
	public void delete(String storageId, String ip) {
		Map<String, String> condition = new HashMap<String, String>();
		if (StringUtil.isNotEmpty(storageId)) {
			condition.put("id", storageId); //$NON-NLS-1$
		}
		if (StringUtil.isNotEmpty(ip)) {
			condition.put("ip", ip); //$NON-NLS-1$
		}
		try {
			baseDao.delete(getStatementName("deleteWhiteList"), condition); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.WhiteListDao#getWhiteList(java.lang.String, java.lang.String)
	 */
	public WhiteList getWhiteList(String storageId, String ip) {
		if (StringUtil.isNotEmpty(storageId) && StringUtil.isNotEmpty(ip)) {
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("id", storageId); //$NON-NLS-1$
			condition.put("ip", ip); //$NON-NLS-1$

			List<WhiteList> list = getWhiteLists(condition);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		}
		return null;
	}
	
	public WhiteList getWhiteListByMountPoint(String mountPoint, String ip) {
		if (StringUtil.isNotEmpty(mountPoint) && StringUtil.isNotEmpty(ip)) {
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("path", mountPoint); //$NON-NLS-1$
			condition.put("ip", ip); //$NON-NLS-1$
			List<WhiteList> list = getWhiteLists(condition);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.WhiteListDao#getWhiteListsByStorage(java.lang.String)
	 */
	public List<WhiteList> getWhiteListsByStorage(String storageId) {
		List<WhiteList> list = null;
		if (StringUtil.isNotEmpty(storageId)) {
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("id", storageId); //$NON-NLS-1$
			list = getWhiteLists(condition);
		}
		return list == null ? new ArrayList<WhiteList>() : list;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.WhiteListDao#getWhiteListsByIp(java.lang.String)
	 */
	public List<WhiteList> getWhiteListsByIp(String ip) {
		List<WhiteList> list = null;
		if (StringUtil.isNotEmpty(ip)) {
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("ip", ip); //$NON-NLS-1$
			list = getWhiteLists(condition);
		}
		return list == null ? new ArrayList<WhiteList>() : list;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.WhiteListDao#getAll()
	 */
	public List<WhiteList> getAll() {
		return getWhiteLists(null);
	}
	
	private List<WhiteList> getWhiteLists(Map<String, String> condition) {
		if (condition == null) {
			condition = new HashMap<String, String>();
		}
		try {
			return baseDao.queryForList(getStatementName("selectWhiteList"), condition); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		return new ArrayList<WhiteList>();
	}

}
