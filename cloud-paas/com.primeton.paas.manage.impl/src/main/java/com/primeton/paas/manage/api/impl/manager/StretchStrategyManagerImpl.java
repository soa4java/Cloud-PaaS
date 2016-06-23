/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.cep.api.EPSException;
import com.primeton.paas.cep.api.EPSInstanceManager;
import com.primeton.paas.cep.api.EPSInstanceManagerFactory;
import com.primeton.paas.cep.model.EPS;
import com.primeton.paas.cep.model.EPSInstance;
import com.primeton.paas.cep.model.PreparedEPS;
import com.primeton.paas.manage.api.app.IStrategyQueryManager;
import com.primeton.paas.manage.api.app.IStretchStrategyManager;
import com.primeton.paas.manage.api.app.StrategyItem;
import com.primeton.paas.manage.api.app.StretchStrategy;
import com.primeton.paas.manage.api.app.StretchStrategyException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.StretchStrategyManagerFactory;
import com.primeton.paas.manage.api.impl.dao.AppEPSRel;
import com.primeton.paas.manage.api.impl.dao.AppEPSRelDao;
import com.primeton.paas.manage.api.impl.dao.DefaultAppEPSRelDao;
import com.primeton.paas.manage.api.impl.dao.DefaultStretchStrategyDao;
import com.primeton.paas.manage.api.impl.monitor.Constants;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public class StretchStrategyManagerImpl implements IStretchStrategyManager {

	private static ILogger logger = ManageLoggerFactory.getLogger(StretchStrategyManagerImpl.class);
	
	private static DefaultStretchStrategyDao stretchDao = DefaultStretchStrategyDao.getInstance();
	
	private AppEPSRelDao appEPSRelDao = new DefaultAppEPSRelDao();
	private EPSInstanceManager epsInstanceManager = EPSInstanceManagerFactory.getManager();
	
	private static final String lowerListenerName = "com.primeton.paas.cep.listener.LowerWarnEventListener";
	private static final String upperListenerName = "com.primeton.paas.cep.listener.UpperWarnEventListener";

	private static final String esper_sql = "select appName, avg(cpu_us), avg(cpu_sy), avg(cpu_ni), avg(cpu_id), avg(cpu_wa), avg(cpu_hi), avg(cpu_si), avg(cpu_st), avg(cpu_oneload), avg(cpu_fiveload), avg(cpu_fifteenload), avg(mem_total), avg(mem_used), avg(mem_free), avg(mem_buffers), avg(mem_us), avg(io_si), avg(io_so), avg(io_bi), avg(io_bo) from appMonitorEvent(100-cpu_us>=0, 100-cpu_sy>=0, 100-cpu_ni>=0, 100-cpu_id>=0, 100-cpu_wa>=0, 100-cpu_hi>=0, 100-cpu_si>=0, 100-cpu_st>=0, 100-mem_us>=0).win:time(? min) where appName = ? having 1=2 ";
	
	private static final IStrategyQueryManager queryManager = StretchStrategyManagerFactory.getQueryManager();
	
	/* 
	 * (none Javadoc)
	 * @see com.primeton.paas.manage.api.app.IStretchStrategyManager#save(com.primeton.paas.manage.api.app.StretchStrategy)
	 */
	public void save(StretchStrategy strategy) throws StretchStrategyException {
		if (strategy == null || StringUtil.isEmpty(strategy.getAppName())) {
			return;
		}
		String appName = strategy.getAppName();
		boolean isGlobalStrategy = false;
		boolean isSuccess = false;

		if (StretchStrategy.GLOBAL_STRATEGY.equals(appName)) {
			isGlobalStrategy = true;
		}
		List<String> appNameList = null;
		try {
			if (StringUtil.isEmpty(strategy.getStrategyName())) {
				strategy.setStrategyName(appName);
			}
			logger.info("update stretch strategy.{appName=" + appName
					+ ", strategtyName=" + strategy.getStrategyName() + "}.");
			stretchDao.saveStrategy(strategy);

			if (isGlobalStrategy) {
				logger.info("update global stretch strategy : update relation application's cep rules");
				appNameList = stretchDao
						.getAppsByStrategyName(StretchStrategy.GLOBAL_STRATEGY);
			}
			isSuccess = true;
		} catch (Throwable t) {
			logger.error(t);
		}
		if (!isSuccess) {
			logger.info("update stretch strategy.{appName=" + appName
					+ ", strategtyName=" + strategy.getStrategyName()
					+ "} failed.");
			return;
		}
		if (isGlobalStrategy) {
			if (appNameList != null && appNameList.size() > 0) {
				for (String name : appNameList) {
					logger.info("update application '" + appName
							+ " 's cep rule");
					saveAppCepRule(name, strategy);
				}
			}
		} else {
			logger.info("update app stretch strategy : only update application '"
					+ appName + " 's cep rule");
			saveAppCepRule(appName, strategy);
		}
	}
	
	/**
	 * 
	 * @param appName
	 * @param strategy
	 * @return
	 */
	private boolean saveAppCepRule(String appName, StretchStrategy strategy) {
		int cpu = 0;
		int mem = 0;
		float lb = 0;

		String strategyName = strategy.getStrategyName();
		String strategyType = strategy.getStrategyType(); // INC | DEC

		boolean isEnable = strategy.getIsEnable();

		if (!isEnable) {
			logger.info("Get app-eps-rel.{appName:" + appName + "}.");
			AppEPSRel rel = appEPSRelDao.get(appName);
			if (rel != null) {
				String instId = null;
				if (StretchStrategy.STRATEGY_INCREASE.equals(strategyType)) {
					instId = rel.getIncInstId();
					rel.setIncInstId(null);
				} else if (StretchStrategy.STRATEGY_DECREASE
						.equals(strategyType)) {
					instId = rel.getDecInstId();
					rel.setDecInstId(null);
				}
				if (StringUtil.isNotEmpty(instId)
						&& Integer.parseInt(instId) > 0) {
					try {
						logger.info("Disable app cep rule. {strategyName:"
								+ strategyName + ",strategyType:"
								+ strategyType + ",cepInstId:" + instId + "}.");
						epsInstanceManager.disable(instId);
					} catch (EPSException e) {
						logger.error(e);
						throw new RuntimeException("");
					}
				}
			}
			return true;
		}

		logger.info("Begin save app cep rule. {strategyName:" + strategyName
				+ ",strategyType:" + strategyType + "}.");

		if (StretchStrategy.GLOBAL_STRATEGY.equals(strategyName)) {
			strategy = queryManager.get(appName, strategyType);
		}
		List<StrategyItem> itemList = strategy == null ? null : strategy
				.getStrategyItems();
		if (strategy == null || itemList == null || itemList.isEmpty()) {
			logger.info("Save application cep rules cancelled. strategy is null.{appName= "
					+ appName + ",strategyType=" + strategyType + "}.");
			return true;
		}

		for (StrategyItem item : itemList) {
			String type = item.getItemType();
			String threshold = StringUtil.isEmpty(item.getThreshold()) ? "0"
					: item.getThreshold();

			if (StrategyItem.TYPE_CPU.equals(type)) {
				cpu = Integer.parseInt(threshold);

			} else if (StrategyItem.TYPE_MEMORY.equals(type)) {
				mem = Integer.parseInt(threshold);

			} else if (StrategyItem.TYPE_LB.equals(type)) {
				lb = Float.parseFloat(threshold);

			}
		}

		StringBuffer sbf = new StringBuffer();
		sbf.append(esper_sql);

		long time = strategy.getContinuedTime();
		String statement = null;
		if (StretchStrategy.STRATEGY_DECREASE.equals(strategyType)) {
			if (cpu > 0) {
				sbf.append(" or avg(cpu_us) <= " + cpu);
			}
			if (mem > 0) {
				sbf.append(" or avg(mem_us) <= " + mem);
			}
			if (lb > 0) {
				sbf.append(" or avg(cpu_oneload) - " + lb + " <= 0 ");
			}
		} else if (StretchStrategy.STRATEGY_INCREASE.equals(strategyType)) {
			if (cpu > 0) {
				sbf.append(" or avg(cpu_us) >= " + cpu);
			}
			if (mem > 0) {
				sbf.append(" or avg(mem_us) >= " + mem);
			}
			if (lb > 0) {
				sbf.append(" or avg(cpu_oneload) - " + lb + " >= 0 ");
			}
		}

		AppEPSRel rel = appEPSRelDao.get(appName);
		boolean exists = true;
		if (rel == null) {
			exists = false;
			rel = new AppEPSRel();
			rel.setAppName(appName);
			appEPSRelDao.add(rel);
		}

		String epsId = null;
		String listener = null;
		if (StretchStrategy.STRATEGY_DECREASE.equals(strategyType)) {
			epsId = rel.getDecInstId();
			listener = lowerListenerName;
		} else if (StretchStrategy.STRATEGY_INCREASE.equals(strategyType)) {
			epsId = rel.getIncInstId();
			listener = upperListenerName;
		}
		statement = sbf.toString();

		if (exists && StringUtil.isNotEmpty(epsId) && !"0".equals(epsId)) { // UPDATE
			EPS eps = new PreparedEPS(statement);

			time = time > 0 ? time : 10L;
			eps.setLong(1, time);
			eps.setString(2, appName);

			EPSInstance instance = epsInstanceManager.get(epsId);
			if (instance == null) {
				instance = new EPSInstance();
				instance.setCreateTime(System.currentTimeMillis());
				instance.setEventName(Constants.EVENT_APPMONITOR);
				instance.setEnable(EPSInstance.DISABLE);
				try {
					epsInstanceManager.register(instance);
				} catch (EPSException e) {
					logger.error(e);
				}
				if (StretchStrategy.STRATEGY_DECREASE.equals(strategyType)) {
					rel.setDecInstId(instance.getId());
				} else if (StretchStrategy.STRATEGY_INCREASE
						.equals(strategyType)) {
					rel.setIncInstId(instance.getId());
				}
				appEPSRelDao.update(rel);

			}
			instance.setEps(eps.exportStatement());

			try {
				epsInstanceManager.update(instance);
			} catch (EPSException e) {
				logger.error(e);
			}

		} else { // ADD
			EPS eps = new PreparedEPS(statement);
			time = time > 0 ? time : 10L;
			eps.setLong(1, time);
			eps.setString(2, appName);

			EPSInstance instance = new EPSInstance(eps);
			instance.setCreateTime(System.currentTimeMillis());
			instance.setEventName(Constants.EVENT_APPMONITOR);
			instance.setEnable(strategy.getIsEnable() ? EPSInstance.ENABLE
					: EPSInstance.DISABLE);
			instance.addListener(listener);
			EPSInstance result = null;

			try {
				result = epsInstanceManager.register(instance);
			} catch (EPSException e) {
				logger.error(e);
			}
			if (result != null) {
				if (StretchStrategy.STRATEGY_DECREASE.equals(strategyType)) {
					rel.setDecInstId(result.getId());
				} else if (StretchStrategy.STRATEGY_INCREASE
						.equals(strategyType)) {
					rel.setIncInstId(result.getId());
				}
			}
			appEPSRelDao.update(rel);
		}
		return true;
	}
	
	/* 
	 * (none Javadoc)
	 * @see com.primeton.paas.manage.api.app.IStretchStrategyManager#remove(java.lang.String)
	 */
	public void remove(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return;
		}
		AppEPSRel rel = appEPSRelDao.get(appName);
		if (rel != null) {
			if (StringUtil.isNotEmpty(rel.getDecInstId())) {
				try {
					epsInstanceManager.unregister(rel.getDecInstId());
				} catch (EPSException e) {
					logger.error(e);
				}
			}
			if (StringUtil.isNotEmpty(rel.getIncInstId())) {
				try {
					epsInstanceManager.unregister(rel.getIncInstId());
				} catch (EPSException e) {
					logger.error(e);
				}
			}
			rel.setDecInstId("");
			rel.setIncInstId("");
			appEPSRelDao.update(rel);
		}

		try {
			StretchStrategy incStretch = queryManager.get(appName,
					StretchStrategy.STRATEGY_INCREASE);
			if (incStretch == null) {
				return;
			}
			String strategyName = incStretch.getStrategyName();
			if (StringUtil.isNotEmpty(strategyName)
					&& strategyName.equals(StretchStrategy.GLOBAL_STRATEGY)) {
				// del strategy relationship with application
				stretchDao.removeAppStrategyRel(appName);
			} else {
				// del strategy items
				stretchDao.removeStrategyItems(appName);
				// del strategy
				stretchDao.removeStrategy(appName);
				// del strategy relationship with application
				stretchDao.removeAppStrategyRel(appName);
			}
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	
	/* 
	 * (none Javadoc)
	 * @see com.primeton.paas.manage.api.app.IStretchStrategyManager#removeGlobal()
	 */
	public void removeGlobal() {
		try {
			// del strategy items
			stretchDao.removeStrategyItems(StretchStrategy.GLOBAL_STRATEGY);
			// del strategy
			stretchDao.removeStrategy(StretchStrategy.GLOBAL_STRATEGY);
			// del strategy relationship with application
			stretchDao.removeAppStrategyRel(StretchStrategy.GLOBAL_STRATEGY);
		} catch (Throwable t) {
			logger.error(t);
		}
	}
	
}
