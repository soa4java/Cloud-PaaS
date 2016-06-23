/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.app.IStrategyQueryManager;
import com.primeton.paas.manage.api.app.StretchStrategy;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultStretchStrategyDao;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class StrategyQueryManagerImpl implements IStrategyQueryManager {

	private static ILogger logger = ManageLoggerFactory.getLogger(StrategyQueryManagerImpl.class);
	
	private static DefaultStretchStrategyDao stretchDao = DefaultStretchStrategyDao.getInstance();
	
	/* 
	 * (none Javadoc)
	 * @see com.primeton.paas.manage.api.app.IStretchStrategyManager#get(java.lang.String, java.lang.String)
	 */
	public StretchStrategy get(String appName, String strategyType) {
		if (StringUtil.isEmpty(strategyType) || StringUtil.isEmpty(appName)) {
			return null;
		}
		StretchStrategy strategy = null;
		try {
			strategy = stretchDao.getStrategy(appName, strategyType);
		} catch (Throwable t) {
			logger.error(t);
		}
		return strategy;
	}

	/* 
	 * (none Javadoc)
	 * @see com.primeton.paas.manage.api.app.IStretchStrategyManager#get(java.lang.String)
	 */
	public List<StretchStrategy> get(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		List<StretchStrategy> strategyList = new ArrayList<StretchStrategy>();
		try {
			StretchStrategy incStrategy = stretchDao.getStrategy(appName,
					StretchStrategy.STRATEGY_INCREASE);
			StretchStrategy decStrategy = stretchDao.getStrategy(appName,
					StretchStrategy.STRATEGY_DECREASE);
			if (incStrategy != null) {
				strategyList.add(incStrategy);
			}
			if (decStrategy != null) {
				strategyList.add(decStrategy);
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return strategyList;
	}
	
	/* 
	 * (none Javadoc)
	 * @see com.primeton.paas.manage.api.app.IStretchStrategyManager#getGlobal()
	 */
	public List<StretchStrategy> getGlobal() {
		return get(StretchStrategy.GLOBAL_STRATEGY);
	}

	/* 
	 * (none Javadoc)
	 * @see com.primeton.paas.manage.api.app.IStretchStrategyManager#getGlobal(java.lang.String)
	 */
	public StretchStrategy getGlobal(String strategyType) {
		if (StringUtil.isEmpty(strategyType)) {
			return null;
		}
		return get(StretchStrategy.GLOBAL_STRATEGY, strategyType);
	}
	
	/* 
	 * (none Javadoc)
	 * @see com.primeton.paas.manage.api.app.IStretchStrategyManager#getAll(com.primeton.paas.manage.api.model.IPageCond)
	 */
	public List<StretchStrategy> getAll(StretchStrategy criteria, IPageCond pageCond) {
		List<StretchStrategy> strategyList = new ArrayList<StretchStrategy>();
		try {
			if (pageCond == null) {
				strategyList = stretchDao.getStrategys(criteria);
			} else {
				if (pageCond.getCount() <= 0) {
					pageCond.setCount(stretchDao.getStrategyCont(criteria));
				}
				strategyList = stretchDao.getStrategys(criteria,pageCond);
			}
		} catch (DaoException e) {
			logger.error(e);
		}
		return null == strategyList ? new ArrayList<StretchStrategy>() : strategyList;
	}
	
}
