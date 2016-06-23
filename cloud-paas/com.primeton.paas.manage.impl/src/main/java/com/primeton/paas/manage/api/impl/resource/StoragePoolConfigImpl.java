/**
 * 
 */
package com.primeton.paas.manage.api.impl.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.StoragePoolConfigDao;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.model.StoragePoolConfig;
import com.primeton.paas.manage.spi.resource.IStoragePoolConfig;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class StoragePoolConfigImpl implements IStoragePoolConfig {

	private static ILogger logger = ManageLoggerFactory
			.getLogger(StoragePoolConfigImpl.class);
	private static StoragePoolConfigDao storagePoolConfigDao = StoragePoolConfigDao.getInstance();

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStoragePoolConfig#add(com.primeton.paas.manage.spi.model.StoragePoolConfig)
	 */
	public boolean add(StoragePoolConfig config) {
		if (null == config) {
			return false;
		}
		try {
			storagePoolConfigDao.add(config);
			if (StoragePoolConfig.ENABLE == config.getIsEnable()) {
				StorageResourceMonitor monitor = new StorageResourceMonitor(
						config.getId(), config);
				StorageResourceMonitorManager.register(monitor);
			}
			return true;
		} catch (Throwable t) {
			logger.error(t);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStoragePoolConfig#delete(java.lang.String)
	 */
	public void delete(String id) {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		StoragePoolConfig config = get(id);
		if (config == null)
			return;
		try {
			storagePoolConfigDao.delete(id);
			StorageResourceMonitorManager.unregister(id);
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStoragePoolConfig#get(java.lang.String)
	 */
	public StoragePoolConfig get(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			return storagePoolConfigDao.get(id);
		} catch (Throwable t) {
			logger.error(t);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStoragePoolConfig#update(com.primeton.paas.manage.spi.model.StoragePoolConfig)
	 */
	public boolean update(StoragePoolConfig config) {
		if (config == null || StringUtil.isEmpty(config.getId())) {
			return false;
		}
		try {
			storagePoolConfigDao.update(config);
			StorageResourceMonitorManager.unregister(config.getId());
			if (StoragePoolConfig.ENABLE == config.getIsEnable()) {
				StorageResourceMonitor monitor = new StorageResourceMonitor(
						config.getId(), config);
				StorageResourceMonitorManager.register(monitor);
			}
			return true;
		} catch (Throwable t) {
			logger.error(t);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStoragePoolConfig#getAll(java.util.Map, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public List<StoragePoolConfig> getAll(Map<String, Object> criteria,
			IPageCond pageCond) {
		List<StoragePoolConfig> configs = new ArrayList<StoragePoolConfig>();
		try {
			if (pageCond == null) {
				configs = storagePoolConfigDao.getAll(criteria);
			} else {
				if (pageCond.getCount() <= 0) {
					pageCond.setCount(storagePoolConfigDao.count(criteria));
				}
				configs = storagePoolConfigDao.getAll(criteria, pageCond);
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return null == configs ? new ArrayList<StoragePoolConfig>() : configs;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStoragePoolConfig#getAllDisabled()
	 */
	public List<StoragePoolConfig> getAllDisabled() {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("isEnable", StoragePoolConfig.DISABLE); //$NON-NLS-1$
		List<StoragePoolConfig> list = null;
		try {
			list = storagePoolConfigDao.getAll(criteria);
		} catch (Throwable t) {
			logger.error(t);
		}
		return list == null ? new ArrayList<StoragePoolConfig>() : list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStoragePoolConfig#getAllEnabled()
	 */
	public List<StoragePoolConfig> getAllEnabled() {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("isEnable", StoragePoolConfig.ENABLE); //$NON-NLS-1$
		List<StoragePoolConfig> list = null;
		try {
			list = storagePoolConfigDao.getAll(criteria);
		} catch (Throwable t) {
			logger.error(t);
		}
		return list == null ? new ArrayList<StoragePoolConfig>() : list;
	}

}
