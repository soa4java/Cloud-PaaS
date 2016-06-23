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

import com.primeton.paas.manage.api.app.ServiceWarnStrategy;
import com.primeton.paas.manage.api.app.ServiceWarnStrategyItem;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultServiceWarnStrategyDao {

	private static ILogger logger = ManageLoggerFactory
			.getLogger(DefaultServiceWarnStrategyDao.class);

	private final static String STRETCH_SQL_MAP = "serviceWarnStrategySqlMap";

	private static DefaultServiceWarnStrategyDao instance = new DefaultServiceWarnStrategyDao();

	private static BaseDao baseDao = BaseDao.getInstance();

	private DefaultServiceWarnStrategyDao() {
	}

	public static DefaultServiceWarnStrategyDao getInstance() {
		return instance;
	}

	private String getSqlMap(String sqlId) {
		return STRETCH_SQL_MAP + "." + sqlId;
	}

	/**
	 * 
	 * @param clusterId
	 * @param strategyId
	 * @throws DaoException
	 */
	public void addServiceStrategyRel(String clusterId, String strategyId)
			throws DaoException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("clusterId", clusterId); //$NON-NLS-1$
		map.put("strategyId", strategyId); //$NON-NLS-1$
		baseDao.insert(getSqlMap("addServiceStrategyRel"), map); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param clusterId
	 * @param strategyId
	 * @throws DaoException
	 */
	public void updateServiceStrategyRel(String clusterId, String strategyId)
			throws DaoException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("clusterId", clusterId); //$NON-NLS-1$
		map.put("strategyId", strategyId); //$NON-NLS-1$
		baseDao.update(getSqlMap("updateServiceStrategyRel"), map); //$NON-NLS-1$
	}

	public String getServiceStrategyIdByClusterId(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return null;
		}
		String strategyId = null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("clusterId", clusterId); //$NON-NLS-1$
			strategyId = baseDao.queryForObject(
					getSqlMap("queryServiceWarnStrategyByClusterId"), map); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error("DefaultServiceWarnStrategyDao.getServiceStrategyIdByClusterId error!");
		}
		return strategyId;
	}

	public List<String> getServiceByStrategyId(String strategyId)
			throws DaoException {
		if (StringUtil.isEmpty(strategyId)) {
			return new ArrayList<String>();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("strategyId", strategyId); //$NON-NLS-1$
		List<String> clusterIds = new ArrayList<String>();
		clusterIds = baseDao.queryForList(
				getSqlMap("queryServicesByStrategyId"), map); //$NON-NLS-1$
		return clusterIds;
	}

	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public boolean isGlobalStrategy(String clusterId) {
		if (StringUtil.isNotEmpty(clusterId)) {
			return false;
		}
		String strategyId = null;
		try {
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("clusterId", clusterId); //$NON-NLS-1$
			strategyId = baseDao.queryForObject(
					getSqlMap("queryServiceWarnStrategyByClusterId"), paraMap); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e.getMessage());
		}
		if (strategyId != null
				&& strategyId.equals(ServiceWarnStrategy.GLOBAL_STRATEGY_ID)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param strategy
	 * @throws DaoException
	 */
	public void saveStrategy(ServiceWarnStrategy strategy) throws DaoException {
		if (strategy == null || StringUtil.isEmpty(strategy.getStrategyId())) {
			return;
		}
		List<ServiceWarnStrategyItem> items = strategy
				.getServiceWarnStrategyItems();
		int result = baseDao.update(getSqlMap("updateServiceStrategyRel"), //$NON-NLS-1$
				strategy);
		if (result < 1) {
			baseDao.insert(getSqlMap("addServiceStrategyRel"), strategy); //$NON-NLS-1$
		}

		if (items != null && !items.isEmpty()) {
			// strategy
			result = baseDao.update(getSqlMap("updateServiceWarnStrategy"), //$NON-NLS-1$
					strategy);
			if (result < 1) {
				baseDao.insert(getSqlMap("addServiceWarnStrategy"), strategy); //$NON-NLS-1$
			}

			for (ServiceWarnStrategyItem item : items) {
				item.setStrategyId(strategy.getStrategyId());
				result = baseDao.update(
						getSqlMap("updateServiceWarnStrategyItem"), item); //$NON-NLS-1$
				if (result < 1) {
					baseDao.insert(getSqlMap("addServiceWarnStrategyItem"), //$NON-NLS-1$
							item);
				}
			}
		}
	}

	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public ServiceWarnStrategy getWarnStrategy(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return null;
		}
		ServiceWarnStrategy strategy = null;
		List<ServiceWarnStrategyItem> items = new ArrayList<ServiceWarnStrategyItem>();

		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("clusterId", clusterId); //$NON-NLS-1$

		try {
			strategy = (ServiceWarnStrategy) baseDao.queryForObject(
					getSqlMap("queryServiceWarnStrategy"), paraMap); //$NON-NLS-1$
			items = baseDao.queryForList(
					getSqlMap("queryServiceWarnStrategyItems"), paraMap); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e.getMessage());
		}
		if (strategy != null) {
			strategy.setStrategyItems(items);
		}
		return strategy;
	}

	/**
	 * 
	 * @param strategyId
	 * @return
	 */
	public ServiceWarnStrategy getWarnStrategyByStrategyId(String strategyId) {
		if (strategyId == null) {
			return null;
		}

		ServiceWarnStrategy strategy = null;
		List<ServiceWarnStrategyItem> items = new ArrayList<ServiceWarnStrategyItem>();

		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("strategyId", strategyId); //$NON-NLS-1$
		try {
			strategy = (ServiceWarnStrategy) baseDao.queryForObject(
					getSqlMap("queryServiceWarnStrategyByStrategyId"), paraMap); //$NON-NLS-1$
			items = baseDao.queryForList(
					getSqlMap("queryServiceWarnStrategyItemsByStrategyId"), //$NON-NLS-1$
					paraMap);
		} catch (DaoException e) {
			logger.error(e.getMessage());
		}
		if (strategy != null) {
			strategy.setStrategyItems(items);
		}
		return strategy;
	}

	public void removeServiceWarnStrategyByClusterId(String clusterId)
			throws DaoException {
		if (StringUtil.isEmpty(clusterId)) {
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		// map.put("strategyId", clusterId);
		map.put("clusterId", clusterId);

		baseDao.delete("deleteServiceStrategyRel", map);
		baseDao.delete(getSqlMap("removeServiceWarnStrategyItems"), map);
		baseDao.delete(getSqlMap("removeServiceWarnStrategy"), map);
	}

	public void removeServiceWarnStrategyById(String strategyId)
			throws DaoException {
		if (StringUtil.isEmpty(strategyId)) {
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("strategyId", strategyId);
		baseDao.delete(getSqlMap("removeServiceWarnStrategyItems"), map);
		baseDao.delete(getSqlMap("removeServiceWarnStrategy"), map);
	}

}
