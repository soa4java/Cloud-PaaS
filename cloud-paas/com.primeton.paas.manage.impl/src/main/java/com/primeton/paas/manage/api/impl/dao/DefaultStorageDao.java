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
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.model.Storage;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultStorageDao implements StorageDao {
	
	private static final String NAMESPACE = "storageSqlMap";
	
	private static BaseDao baseDao = BaseDao.getInstance();
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultStorageDao.class);
	
	private static String getStatementName(String id) {
		return NAMESPACE + "." + id;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.SharedStorageDao#insert(com.primeton.paas.manage.api.model.SharedStorage)
	 */
	public boolean insert(Storage storage) {
		if (storage == null || StringUtil.isEmpty(storage.getId())) {
			return false;
		}
		Storage obj = get(storage.getId());
		if (obj != null) {
			logger.warn(obj + " is already exists.");
			return false;
		}
		try {
			baseDao.insert(getStatementName("insert"), storage); //$NON-NLS-1$
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.SharedStorageDao#update(com.primeton.paas.manage.api.model.SharedStorage)
	 */
	public void update(Storage storage) {
		if (storage == null || StringUtil.isEmpty(storage.getId())) {
			return;
		}
		try {
			baseDao.update(getStatementName("update"), storage); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.SharedStorageDao#delete(java.lang.String)
	 */
	public void delete(String id) {
		if (StringUtil.isNotEmpty(id)) {
			try {
				baseDao.delete(getStatementName("delete"), id); //$NON-NLS-1$
			} catch (DaoException e) {
				logger.error(e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.SharedStorageDao#get(java.lang.String)
	 */
	public Storage get(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			return baseDao.queryForObject(getStatementName("selectById"), id); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.SharedStorageDao#getAll()
	 */
	public List<Storage> getAll() {
		return getAll(new HashMap<String, Object>());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.StorageDao#getAll(java.util.Map)
	 */
	public List<Storage> getAll(Map<String, Object> criteria) {
		try {
			return baseDao.queryForList(getStatementName("selectAllByCondition"), criteria); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		return new ArrayList<Storage>();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.SharedStorageDao#getAll(com.primeton.paas.manage.api.model.IPageCond)
	 */
	public List<Storage> getAll(IPageCond pageCond) {
		if (pageCond == null) {
			return getAll();
		}
		try {
			Integer count = baseDao.queryForObject(getStatementName("countAll")); //$NON-NLS-1$
			if (count == null || count == 0) {
				return new ArrayList<Storage>();
			}
			pageCond.setCount(count);
			return baseDao.queryForList(getStatementName("selectAll"), pageCond); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		return new ArrayList<Storage>();
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.StorageDao#getAll(java.util.Map, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public List<Storage> getAll(Map<String, Object> criteria, IPageCond pageCond) {
		if (pageCond == null) {
			return getAll();
		}
		try {
			Integer count = baseDao.queryForObject(getStatementName("getStorageCount"),criteria); //$NON-NLS-1$
			if (count == null || count == 0) {
				return new ArrayList<Storage>();
			}
			pageCond.setCount(count);
			return baseDao.queryForList(getStatementName("getStorages"),criteria, pageCond); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		return new ArrayList<Storage>();
	}

}
