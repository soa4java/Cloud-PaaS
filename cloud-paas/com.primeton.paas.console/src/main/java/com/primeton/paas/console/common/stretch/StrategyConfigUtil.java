/**
 * 
 */
package com.primeton.paas.console.common.stretch;

import com.primeton.paas.manage.api.app.IStrategyQueryManager;
import com.primeton.paas.manage.api.app.StrategyItem;
import com.primeton.paas.manage.api.app.StretchStrategy;
import com.primeton.paas.manage.api.factory.StretchStrategyManagerFactory;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public class StrategyConfigUtil {

	private static IStrategyQueryManager queryManager = StretchStrategyManagerFactory
			.getQueryManager();

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
		// 查询应用当前伸缩策略
		StretchStrategy stretchInc = queryManager.get(appName,
				StretchStrategy.STRATEGY_INCREASE); // “伸”策略
		StretchStrategy stretchDec = queryManager.get(appName,
				StretchStrategy.STRATEGY_DECREASE); // “缩”策略

		StrategyConfigInfo strategyInfo = new StrategyConfigInfo();
		String strategyName = "";

		StrategyConfigInfo.StretchItem incItem = new StrategyConfigInfo.StretchItem();
		StrategyConfigInfo.StretchItem decItem = new StrategyConfigInfo.StretchItem();

		if (stretchInc != null) {
			strategyName = stretchInc.getStrategyName();

			incItem.setContinuedTime(stretchInc.getContinuedTime());
			incItem.setIgnoreTime(stretchInc.getIgnoreTime());
			incItem.setStrategyType(StretchStrategy.STRATEGY_INCREASE);
			incItem.setEnableFlag(stretchInc.getIsEnable() ? "Y" : "N");
			incItem.setStretchSize(stretchInc.getStretchSize());

			if (stretchInc.getStrategyItems() != null
					&& !stretchInc.getStrategyItems().isEmpty()) {
				for (StrategyItem item : stretchInc.getStrategyItems()) {
					String type = item.getItemType();
					String threshold = item.getThreshold();
					if (StrategyItem.TYPE_CPU.equals(type)) {
						incItem.setCpuThreshold(threshold);
					} else if (StrategyItem.TYPE_MEMORY.equals(type)) {
						incItem.setMemThreshold(threshold);
					} else if (StrategyItem.TYPE_LB.equals(type)) {
						incItem.setLbThreshold(threshold);
					}
				}
			}
		} else {
			incItem = null;
		}

		if (stretchDec != null) {
			strategyName = stretchDec.getStrategyName();

			decItem.setContinuedTime(stretchDec.getContinuedTime());
			decItem.setIgnoreTime(stretchDec.getIgnoreTime());
			decItem.setStrategyType(StretchStrategy.STRATEGY_DECREASE);
			decItem.setEnableFlag(stretchDec.getIsEnable() ? "Y" : "N");
			decItem.setStretchSize(stretchDec.getStretchSize());

			if (stretchDec.getStrategyItems() != null
					&& !stretchDec.getStrategyItems().isEmpty()) {
				for (StrategyItem item : stretchDec.getStrategyItems()) {
					String type = item.getItemType();
					String threshold = item.getThreshold();
					if (StrategyItem.TYPE_CPU.equals(type)) {
						decItem.setCpuThreshold(threshold);
					} else if (StrategyItem.TYPE_MEMORY.equals(type)) {
						decItem.setMemThreshold(threshold);
					} else if (StrategyItem.TYPE_LB.equals(type)) {
						decItem.setLbThreshold(threshold);
					}
				}
			}
		} else {
			decItem = null;
		}

		strategyInfo.setAppName(appName);
		strategyInfo.setStrategyName(StringUtil.isEmpty(strategyName) ? appName
				: strategyName);
		strategyInfo.setIncStrategy(incItem);
		strategyInfo.setDecStrategy(decItem);
		return strategyInfo;
	}

	/**
	 * 获取当前全局伸缩策略详情 <br>
	 * 
	 * @return
	 */
	public static StrategyConfigInfo getGlobalStretchStrategy() {
		StrategyConfigInfo globalConf = new StrategyConfigInfo();

		StrategyConfigInfo.StretchItem incItem = new StrategyConfigInfo.StretchItem();
		StrategyConfigInfo.StretchItem decItem = new StrategyConfigInfo.StretchItem();

		StretchStrategy stretchInc = queryManager
				.getGlobal(StretchStrategy.STRATEGY_INCREASE); // 全局 “伸” 策略
		StretchStrategy stretchDec = queryManager
				.getGlobal(StretchStrategy.STRATEGY_DECREASE); // 全局 “缩” 策略

		String strategyName = "";

		if (stretchInc != null) {
			strategyName = stretchInc.getStrategyName();

			incItem.setContinuedTime(stretchInc.getContinuedTime());
			incItem.setIgnoreTime(stretchInc.getIgnoreTime());
			incItem.setStrategyType(StretchStrategy.STRATEGY_INCREASE);
			incItem.setEnableFlag(stretchInc.getIsEnable() ? "Y" : "N");
			incItem.setStretchSize(stretchInc.getStretchSize());

			if (stretchInc.getStrategyItems() != null
					&& !stretchInc.getStrategyItems().isEmpty()) {
				for (StrategyItem item : stretchInc.getStrategyItems()) {
					String type = item.getItemType();
					String threshold = item.getThreshold();
					if (StrategyItem.TYPE_CPU.equals(type)) {
						incItem.setCpuThreshold(threshold);
					} else if (StrategyItem.TYPE_MEMORY.equals(type)) {
						incItem.setMemThreshold(threshold);
					} else if (StrategyItem.TYPE_LB.equals(type)) {
						incItem.setLbThreshold(threshold);
					}
				}
			}
		}

		if (stretchDec != null) {
			strategyName = stretchDec.getStrategyName();

			decItem.setContinuedTime(stretchDec.getContinuedTime());
			decItem.setIgnoreTime(stretchDec.getIgnoreTime());
			decItem.setStrategyType(StretchStrategy.STRATEGY_DECREASE);
			decItem.setEnableFlag(stretchDec.getIsEnable() ? "Y" : "N");
			decItem.setStretchSize(stretchDec.getStretchSize());

			if (stretchDec.getStrategyItems() != null
					&& !stretchDec.getStrategyItems().isEmpty()) {
				for (StrategyItem item : stretchDec.getStrategyItems()) {

					String type = item.getItemType();
					String threshold = item.getThreshold();

					if (StrategyItem.TYPE_CPU.equals(type)) {
						decItem.setCpuThreshold(threshold);

					} else if (StrategyItem.TYPE_MEMORY.equals(type)) {
						decItem.setMemThreshold(threshold);

					} else if (StrategyItem.TYPE_LB.equals(type)) {
						decItem.setLbThreshold(threshold);

					}
				}
			}
		}

		globalConf.setStrategyName(strategyName);
		globalConf.setAppName(StretchStrategy.GLOBAL_STRATEGY);
		globalConf.setIncStrategy(incItem);
		globalConf.setDecStrategy(decItem);
		return globalConf;
	}

}
