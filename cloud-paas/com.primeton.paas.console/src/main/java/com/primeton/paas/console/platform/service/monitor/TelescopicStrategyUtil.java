/**
 * 
 */
package com.primeton.paas.console.platform.service.monitor;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.common.stretch.StrategyBasicConfigUtil;
import com.primeton.paas.console.common.stretch.StrategyConfigInfo;
import com.primeton.paas.console.common.stretch.StrategyConfigUtil;
import com.primeton.paas.console.common.stretch.StrategyItemInfo;
import com.primeton.paas.manage.api.app.IStretchStrategyManager;
import com.primeton.paas.manage.api.app.StrategyItem;
import com.primeton.paas.manage.api.app.StretchStrategy;
import com.primeton.paas.manage.api.app.StretchStrategyException;
import com.primeton.paas.manage.api.factory.StretchStrategyManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class TelescopicStrategyUtil {

	private static ILogger logger = LoggerFactory.getLogger(TelescopicStrategyUtil.class);

	private static IStretchStrategyManager stretchMgr = StretchStrategyManagerFactory
			.getManager();

	private static IWebAppManager appManager = WebAppManagerFactory.getManager();

	/**
	 * 获取配置项下拉框 可选项列表 --array 形式 <br>
	 * 
	 * @return
	 */
	public static StrategyItemInfo getItemInfo() {
		return StrategyBasicConfigUtil.getItemOpts();
	}

	/**
	 * 获取所有已开通的应用
	 * 
	 * @return
	 */
	public static List<WebApp> queryApps() {
		List<WebApp> appList = new ArrayList<WebApp>();

		WebApp[] apps = appManager.getAll();
		if (apps == null || apps.length < 1) {
			return appList;
		}
		for (int i = 0; i < apps.length; i++) {
			WebApp app = apps[i];
			// opened application
			int state = app.getState();
			if (state != WebApp.STATE_OPEND) {
				continue;
			}
			appList.add(app);
		}
		return appList;
	}

	/**
	 * 获取指定应用当前的伸缩策略详情 <br>
	 * 
	 * @param appName
	 * @return
	 */
	public static StrategyConfigInfo getAppStretchStrategy(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		return StrategyConfigUtil.getAppStretchStrategy(appName);
	}

	/**
	 * 获取当前全局伸缩策略详情 <br>
	 * 
	 * @return
	 */
	public static StrategyConfigInfo getGlobalStretchStrategy() {
		return StrategyConfigUtil.getGlobalStretchStrategy();
	}

	/**
	 * 为应用设置个性化伸缩策略配置 <br>
	 * 
	 * @param stretchConf
	 *            个性化伸缩策略配置详情
	 * @return
	 */
	public static boolean setAppStretchStrategy(StrategyConfigInfo stretchConf) {
		if (stretchConf == null) {
			return false;
		}
		boolean incSuccess = true;
		boolean decSuccess = true;

		String appName = stretchConf.getAppName();

		StrategyConfigInfo.StretchItem incItem = stretchConf.getIncStrategy();
		StrategyConfigInfo.StretchItem decItem = stretchConf.getDecStrategy();

		// “伸”策略
		if (incItem != null) {
			StretchStrategy incStrategy = new StretchStrategy();
			incStrategy.setAppName(appName);
			incStrategy.setStrategyName(appName); // 个性化配置： 应用名作为 伸缩策略名
			incStrategy.setStrategyType(StretchStrategy.STRATEGY_INCREASE); // “伸”
			incStrategy.setIsEnable("Y".equals(incItem.getEnableFlag())); //$NON-NLS-1$
			incStrategy.setContinuedTime(incItem.getContinuedTime());
			incStrategy.setIgnoreTime(incItem.getIgnoreTime());
			incStrategy.setStretchSize(incItem.getStretchSize());

			List<StrategyItem> items = new ArrayList<StrategyItem>();
			StrategyItem cpu = new StrategyItem();
			cpu.setThreshold(incItem.getCpuThreshold());
			cpu.setItemType(StrategyItem.TYPE_CPU);
			items.add(cpu);

			StrategyItem memory = new StrategyItem();
			memory.setThreshold(incItem.getMemThreshold());
			memory.setItemType(StrategyItem.TYPE_MEMORY);
			items.add(memory);

			// load balance
			StrategyItem loadBalance = new StrategyItem();
			loadBalance.setThreshold(incItem.getLbThreshold());
			loadBalance.setItemType(StrategyItem.TYPE_LB);
			items.add(loadBalance);

			incStrategy.setStrategyItems(items);

			//
			try {
				stretchMgr.save(incStrategy);
			} catch (StretchStrategyException e) {
				logger.error("Save application {0} increase strategy error.", new Object[] { appName }, e);
				incSuccess = false;
			}
		}

		// “缩”策略
		if (decItem != null) {
			StretchStrategy decStrategy = new StretchStrategy();
			decStrategy.setAppName(appName);
			decStrategy.setStrategyName(appName); // 个性化配置： 应用名作为 伸缩策略名
			decStrategy.setStrategyType(StretchStrategy.STRATEGY_DECREASE); // “缩”
			decStrategy.setIsEnable("Y".equals(decItem.getEnableFlag())); //$NON-NLS-1$
			decStrategy.setContinuedTime(decItem.getContinuedTime());
			decStrategy.setIgnoreTime(decItem.getIgnoreTime());
			decStrategy.setStretchSize(decItem.getStretchSize());

			List<StrategyItem> items = new ArrayList<StrategyItem>();
			StrategyItem cpu = new StrategyItem();
			cpu.setThreshold(decItem.getCpuThreshold());
			cpu.setItemType(StrategyItem.TYPE_CPU);
			items.add(cpu);

			StrategyItem memory = new StrategyItem();
			memory.setThreshold(decItem.getMemThreshold());
			memory.setItemType(StrategyItem.TYPE_MEMORY);
			items.add(memory);

			// load balance
			StrategyItem loadBalance = new StrategyItem();
			loadBalance.setThreshold(decItem.getLbThreshold());
			loadBalance.setItemType(StrategyItem.TYPE_LB);
			items.add(loadBalance);

			decStrategy.setStrategyItems(items);

			try {
				stretchMgr.save(decStrategy);
			} catch (StretchStrategyException e) {
				logger.error("Save application {0} increase strategy error.", new Object[] { appName }, e);
				decSuccess = false;
			}
		}
		return incSuccess && decSuccess;
	}

	/**
	 * 为应用设置 全局伸缩策略： 提交伸缩策略变更订单 <br>
	 * 
	 * @param isIncEnable
	 *            是否启用全局 伸
	 * @param isDecEnable
	 *            是否启用全局 缩
	 * @return
	 */
	public static boolean setGlobalStretchStrategy(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return false;
		}
		StretchStrategy incStrategy = new StretchStrategy();
		incStrategy.setAppName(appName);
		incStrategy.setStrategyName(StretchStrategy.GLOBAL_STRATEGY);// “全局”
		incStrategy.setStrategyType(StretchStrategy.STRATEGY_INCREASE);// “伸”

		StretchStrategy decStrategy = new StretchStrategy();
		decStrategy.setAppName(appName);
		decStrategy.setStrategyName(StretchStrategy.GLOBAL_STRATEGY);// “全局”
		decStrategy.setStrategyType(StretchStrategy.STRATEGY_DECREASE);// “缩”

		try {
			stretchMgr.save(incStrategy);
		} catch (StretchStrategyException e) {
			logger.error("Save application {0} increase [global] strategy error.", new Object[] { appName }, e);
		}

		try {
			stretchMgr.save(decStrategy);
		} catch (StretchStrategyException e) {
			logger.error("Save application {0} increase [global] strategy error.", new Object[] { appName }, e);
		}
		return true;
	}

	/**
	 * @param stretchConf
	 * @return 更新后的详情
	 */
	public static StrategyConfigInfo updateGlobalStretchStrategy(
			StrategyConfigInfo stretchConf) {
		if (stretchConf == null) {
			return null;
		}
		stretchConf.setAppName(stretchConf.getAppName());
		stretchConf.setStrategyName(StretchStrategy.GLOBAL_STRATEGY);

		StrategyConfigInfo.StretchItem incItem = stretchConf.getIncStrategy();
		StrategyConfigInfo.StretchItem decItem = stretchConf.getDecStrategy();

		StretchStrategy incStretch = new StretchStrategy();
		StretchStrategy decStretch = new StretchStrategy();

		// “伸” 策略
		if (incItem != null) {
			incStretch.setAppName(StretchStrategy.GLOBAL_STRATEGY);
			incStretch.setIsEnable("Y".equals(incItem.getEnableFlag()));

			incStretch.setStrategyType(StretchStrategy.STRATEGY_INCREASE);

			incStretch.setContinuedTime(incItem.getContinuedTime());
			incStretch.setIgnoreTime(incItem.getIgnoreTime());
			incStretch.setStretchSize(incItem.getStretchSize());

			List<StrategyItem> items = new ArrayList<StrategyItem>();

			// cpu
			StrategyItem cpu = new StrategyItem();
			cpu.setItemType(StrategyItem.TYPE_CPU);
			cpu.setThreshold(incItem.getCpuThreshold());
			items.add(cpu);

			// memory
			StrategyItem memory = new StrategyItem();
			memory.setItemType(StrategyItem.TYPE_MEMORY);
			memory.setThreshold(incItem.getMemThreshold());
			items.add(memory);
			// load balance
			StrategyItem loadBalance = new StrategyItem();
			loadBalance.setThreshold(incItem.getLbThreshold());
			loadBalance.setItemType(StrategyItem.TYPE_LB);
			items.add(loadBalance);

			incStretch.setStrategyItems(items);

		} else {
			incStretch.setIsEnable(false);
		}

		// 缩” 策略
		if (decItem != null) {
			decStretch.setIsEnable("Y".equals(decItem.getEnableFlag()));
			decStretch.setAppName(StretchStrategy.GLOBAL_STRATEGY);

			decStretch.setStrategyType(StretchStrategy.STRATEGY_DECREASE);

			decStretch.setContinuedTime(decItem.getContinuedTime());
			decStretch.setIgnoreTime(decItem.getIgnoreTime());
			decStretch.setStretchSize(decItem.getStretchSize());

			List<StrategyItem> items = new ArrayList<StrategyItem>();

			// cpu
			StrategyItem cpu = new StrategyItem();
			cpu.setItemType(StrategyItem.TYPE_CPU);
			cpu.setThreshold(decItem.getCpuThreshold());
			items.add(cpu);

			// memory
			StrategyItem memory = new StrategyItem();
			memory.setItemType(StrategyItem.TYPE_MEMORY);
			memory.setThreshold(decItem.getMemThreshold());
			items.add(memory);

			// load balance
			StrategyItem loadBalance = new StrategyItem();
			loadBalance.setThreshold(decItem.getLbThreshold());
			loadBalance.setItemType(StrategyItem.TYPE_LB);
			items.add(loadBalance);

			decStretch.setStrategyItems(items);

		} else {
			decStretch.setIsEnable(false);
		}

		// 更新 “伸” 配置
		try {
			stretchMgr.save(incStretch);
		} catch (StretchStrategyException e) {
			logger.error("Save application decrease [global] strategy error.", e);
		}

		// 更新 “缩” 配置
		try {
			stretchMgr.save(decStretch);
		} catch (StretchStrategyException e) {
			logger.error("Save application decrease [global] strategy error.", e);
		}
		stretchConf = StrategyConfigUtil.getGlobalStretchStrategy();
		return stretchConf;
	}

}
