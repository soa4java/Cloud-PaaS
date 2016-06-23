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

import com.primeton.paas.manage.api.app.StrategyItem;
import com.primeton.paas.manage.api.app.StretchStrategy;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public class DefaultStretchStrategyDao {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultStretchStrategyDao.class);
	
	private final static String STRETCH_SQL_MAP = "stretchSqlMap";

	private static DefaultStretchStrategyDao instance = new DefaultStretchStrategyDao();
	
	private static BaseDao baseDao = BaseDao.getInstance();;
	
	private DefaultStretchStrategyDao() {
	}

	public static DefaultStretchStrategyDao getInstance() {
		return instance;
	}
	
	private String getSqlMap(String sqlId){
		return STRETCH_SQL_MAP + "." + sqlId;
	}
	
	/**
	 * 
	 * @param appName
	 * @param strategyName
	 * @throws DaoException 
	 */
	public void addAppStrategyRel(String appName, String strategyName) throws DaoException{
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(strategyName)) {
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("appName", appName); //$NON-NLS-1$
		map.put("strategyName", strategyName); //$NON-NLS-1$
		baseDao.insert(getSqlMap("addAppStrategyRel"), map); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	public boolean isGlobalStrategy(String appName){
		if (StringUtil.isNotEmpty(appName)) {
			return false;
		}
		StretchStrategy strategy = null;
		try {
			strategy = baseDao.queryForObject(getSqlMap("queryStrategy"),appName); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e.getMessage());
		}
		if (strategy != null && strategy.getStrategyName().equals(StretchStrategy.GLOBAL_STRATEGY)){
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param strategy
	 * @throws DaoException
	 */
	public void saveStrategy(StretchStrategy strategy) throws DaoException {
		if (strategy == null || StringUtil.isEmpty(strategy.getAppName()) 
				|| StringUtil.isEmpty(strategy.getStrategyName()) ) {
			return; 
		}
		List<StrategyItem> items = strategy.getStrategyItems();
		//relation
		int result = baseDao.update(getSqlMap("updateAppStrategyRel"), strategy);
		if (result < 1) {
			baseDao.insert(getSqlMap("addAppStrategyRel"), strategy);
		}
		if (items != null && !items.isEmpty()) {
			//strategy 
			result = baseDao.update(getSqlMap("updateStrategy"), strategy);
			if (result < 1) {
				baseDao.insert(getSqlMap("addStrategy"), strategy);
			}
			for (StrategyItem item : items) {
				item.setStrategyName(strategy.getStrategyName());
				item.setStrategyType(strategy.getStrategyType());
				result = baseDao.update(getSqlMap("updateStrategyItem"), item);
				if (result < 1) {
					baseDao.insert(getSqlMap("addStrategyItem"), item);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param appName
	 * @param strategyType
	 * @return
	 */
	public StretchStrategy getStrategy(String appName, String strategyType) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(strategyType)) {
			return null;
		}
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("appName", appName); //$NON-NLS-1$
		paraMap.put("strategyType", strategyType); //$NON-NLS-1$
		
		StretchStrategy strategy = null;
		List<StrategyItem> items = new ArrayList<StrategyItem>();
		try {
			strategy = (StretchStrategy)baseDao.queryForObject(getSqlMap("queryStrategy"), paraMap); //$NON-NLS-1$
			items = baseDao.queryForList(getSqlMap("queryStrategyItems"), paraMap); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e.getMessage());
		}
		if (strategy != null ) {
			strategy.setStrategyItems(items);
		}
		return strategy;
	}
	
	/**
	 * 
	 * @param appName
	 * @throws DaoException
	 */
	public void removeStrategyItems(String appName) throws DaoException {
		removeStrategyItems(appName, null);
	}
	
	/**
	 * 
	 * @param appName
	 * @param strategyType
	 * @throws DaoException
	 */
	public void removeStrategyItems(String appName, String strategyType)
			throws DaoException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("appName", appName); //$NON-NLS-1$
		map.put("strategyType", strategyType); //$NON-NLS-1$
		baseDao.delete(getSqlMap("removeStrategyItems"), map); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param appName
	 * @throws DaoException
	 */
	public void removeStrategy(String appName) throws DaoException{
		removeStrategy(appName, null);
	}
	
	/**
	 * 
	 * @param appName
	 * @param strategyType
	 * @throws DaoException
	 */
	public void removeStrategy(String appName,String strategyType) throws DaoException{
		Map<String, String> map = new HashMap<String, String>();
		map.put("appName", appName); //$NON-NLS-1$
		map.put("strategyType", strategyType); //$NON-NLS-1$
		baseDao.delete(getSqlMap("removeStrategy"), map); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param appName
	 * @throws DaoException
	 */
	public void removeAppStrategyRel(String appName) throws DaoException{
		baseDao.delete(getSqlMap("removeAppStrategyRel"), appName);  //$NON-NLS-1$
	}

	/**
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public int getStrategyCont(StretchStrategy criteria) throws DaoException {
		if (criteria == null)
			criteria = new StretchStrategy();
		Integer num = (Integer)baseDao.queryForObject(getSqlMap("getStrategyCont"),criteria); //$NON-NLS-1$
		return num.intValue();
	}

	/**
	 * @param user
	 * @param pc
	 * @return
	 * @throws DaoException
	 */
	public List<StretchStrategy> getStrategys(StretchStrategy criteria,
			IPageCond page) throws DaoException {
		if (criteria == null)
			criteria = new StretchStrategy();
		return baseDao.queryForList(getSqlMap("getStrategys"), criteria, page); //$NON-NLS-1$
	}
	
	/**
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public List<StretchStrategy> getStrategys(StretchStrategy criteria)
			throws DaoException {
		if (criteria == null)
			criteria = new StretchStrategy();
		return baseDao.queryForList(getSqlMap("getStrategys"), criteria); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param strategyName
	 * @return
	 * @throws DaoException
	 */
	public List<String> getAppsByStrategyName(String strategyName)
			throws DaoException {
		List<String> appNameList = baseDao.queryForList(
				getSqlMap("getAppsByStrategyName"), strategyName); //$NON-NLS-1$
		return appNameList;
	}
	
}
