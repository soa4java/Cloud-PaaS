/**
 *
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.spi.model.StoragePoolConfig;

/**
 * 
 * @author liming(mailto:li-ming@primeton.com)
 */
public class StoragePoolConfigDao {

	private final static String USER_SQL_MAP = "storagePoolSqlMap";

	private static StoragePoolConfigDao instance = new StoragePoolConfigDao();

	private static BaseDao baseDao = BaseDao.getInstance();;

	private StoragePoolConfigDao() {
	}

	/**
	 * 
	 * @return
	 */
	public static StoragePoolConfigDao getInstance() {
		return instance;
	}

	private String getSqlMap(String sqlId) {
		return USER_SQL_MAP + "." + sqlId;
	}

	public void add(StoragePoolConfig sharedStoragePoolConfig) throws DaoException {
		baseDao.insert(getSqlMap("add"), sharedStoragePoolConfig); //$NON-NLS-1$
	}

	public int count(Map<String, Object> criteria) throws DaoException {
		if (criteria == null)
			criteria = new HashMap<String, Object>();
		Integer num = (Integer) baseDao.queryForObject(
				getSqlMap("countByCondition"), criteria); //$NON-NLS-1$
		return null == num ? 0 : num.intValue();
	}

	public StoragePoolConfig get(String id)
			throws DaoException {
		return baseDao.queryForObject(getSqlMap("getById"),id); //$NON-NLS-1$
	}

	public List<StoragePoolConfig> getAll(Map<String, Object> criteria)
			throws DaoException {
		return baseDao.queryForList(getSqlMap("getAllByCondition"), criteria); //$NON-NLS-1$
	}

	public List<StoragePoolConfig> getAll(Map<String, Object> criteria, IPageCond pageCond)
			throws DaoException {
		return baseDao.queryForList(getSqlMap("getAllByCondition"), criteria, pageCond); //$NON-NLS-1$
	}
	
	public void update(StoragePoolConfig sharedStoragePoolConfig)
			throws DaoException {
		baseDao.update(getSqlMap("update"), sharedStoragePoolConfig); //$NON-NLS-1$
	}
	
	public void delete(String id) throws DaoException {
		baseDao.delete(getSqlMap("delete"), id); //$NON-NLS-1$
	}

}
