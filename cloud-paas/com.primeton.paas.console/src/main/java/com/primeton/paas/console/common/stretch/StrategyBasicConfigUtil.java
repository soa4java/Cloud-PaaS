/**
 * 
 */
package com.primeton.paas.console.common.stretch;

import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.manage.api.factory.VariableManagerFactory;
import com.primeton.paas.manage.api.manager.IVariableManager;

/**
 * 
 * @author liyanping(mailto:liyp@primeton.com)
 */
public class StrategyBasicConfigUtil {

	private static final IVariableManager varManager = VariableManagerFactory
			.getManager();

	private static StrategyItemInfo itemInfo = null;

	static {
		itemInfo = new StrategyItemInfo();
	}

	/**
	 * 
	 * @return
	 */
	public static StrategyItemInfo getItemInfoList() {

		List<Integer> cpuUsageOpts = getCpuUsageOpts();

		List<Integer> memUsageOpts = getMemUsageOpts();

		List<Integer> netTrafficOpts = getNetTrafficOpts();

		List<Integer> stretchScaleOpts = getStretchScaleOpts();

		List<Integer> durationOpts = getDurationOpts();

		List<Integer> ioRateOpts = loadIORateOpts();

		List<Integer> ignoreTimeOpts = getIgnoreTimeOpts();

		List<String> lbOpts = getLoadbalanceOpts();

		itemInfo.setCpuUsageList(cpuUsageOpts);
		itemInfo.setDurationList(durationOpts);
		itemInfo.setIoRateList(ioRateOpts);
		itemInfo.setMemUsageList(memUsageOpts);
		itemInfo.setNetTrafficList(netTrafficOpts);
		itemInfo.setStretchScaleList(stretchScaleOpts);
		itemInfo.setIgnoreTimeList(ignoreTimeOpts);
		itemInfo.setLbList(lbOpts);
		return itemInfo;
	}

	/**
	 * 
	 * @return
	 */
	public static StrategyItemInfo getItemOpts() {
		// cpu/mem/stretchScale/duration/ignoreTime/loadbalance
		String defaultValue = "20,40,60,80;20,40,60,80;1,2,3,4;5,10,30,60;15,30,45,60;0.5,1.0,1.5,2.0";
		String allOpts = varManager.getStringValue(SystemVariable.ALL_OPTS_KEY, defaultValue);
		if (allOpts == null) {
			allOpts = defaultValue;
		}

		String[] cfgs = allOpts.split(";");
		if (cfgs == null | cfgs.length != 6) {
			cfgs = defaultValue.split(";");
		}
		String cpuCfg = cfgs[0];
		String memCfg = cfgs[1];
		String stretchScaleCfg = cfgs[2];
		String durationCfg = cfgs[3];
		String ignoreTimeCfg = cfgs[4];
		String loadbalanceCfg = cfgs[5];

		List<Integer> cpuUsageOpts = new ArrayList<Integer>();
		String[] opts = cpuCfg.split(",");
		for (String temp : opts) {
			cpuUsageOpts.add(Integer.parseInt(temp));
		}

		List<Integer> memUsageOpts = new ArrayList<Integer>();
		opts = memCfg.split(",");
		for (String temp : opts) {
			memUsageOpts.add(Integer.parseInt(temp));
		}
		List<Integer> stretchScaleOpts = new ArrayList<Integer>();
		opts = stretchScaleCfg.split(",");
		for (String temp : opts) {
			stretchScaleOpts.add(Integer.parseInt(temp));
		}
		List<Integer> durationOpts = new ArrayList<Integer>();
		opts = durationCfg.split(",");
		for (String temp : opts) {
			durationOpts.add(Integer.parseInt(temp));
		}
		List<Integer> ignoreTimeOpts = new ArrayList<Integer>();
		opts = ignoreTimeCfg.split(",");
		for (String temp : opts) {
			ignoreTimeOpts.add(Integer.parseInt(temp));
		}
		List<String> loadBalanceOpts = new ArrayList<String>();
		opts = loadbalanceCfg.split(",");
		for (String temp : opts) {
			loadBalanceOpts.add(temp);
		}

		// CPU Usage
		// List<Integer> cpuUsageOpts = getCpuUsageOpts();
		Integer[] cpuUsageArr = cpuUsageOpts.toArray(new Integer[cpuUsageOpts
				.size()]);
		itemInfo.setCpuUsageArr(cpuUsageArr);

		// Memory Usage
		// List<Integer> memUsageOpts = getMemUsageOpts();
		Integer[] memUsageArr = memUsageOpts.toArray(new Integer[memUsageOpts
				.size()]);
		itemInfo.setMemUsageArr(memUsageArr);

		// Network Traffic
		// List<Integer> netTrafficOpts = getNetTrafficOpts();
		// Integer[] netTrafficArr = netTrafficOpts.toArray(new
		// Integer[netTrafficOpts.size()]);
		// itemInfo.setNetTrafficArr(netTrafficArr);

		// IO Rate
		// List<Integer> ioRateOpts = loadIORateOpts();
		// Integer[] ioRateArr = ioRateOpts.toArray(new
		// Integer[ioRateOpts.size()]);
		// itemInfo.setIoRateArr(ioRateArr);

		// StretchScale
		// List<Integer> stretchScaleOpts = getStretchScaleOpts();
		Integer[] stretchScaleArr = stretchScaleOpts
				.toArray(new Integer[stretchScaleOpts.size()]);
		itemInfo.setStretchScaleArr(stretchScaleArr);

		// Duration
		// List<Integer> durationOpts = getDurationOpts();
		Integer[] durationArr = durationOpts.toArray(new Integer[durationOpts
				.size()]);
		itemInfo.setDurationArr(durationArr);

		// ignoreTime
		// List<Integer> ignoreTimeOpts = getIgnoreTimeOpts();
		Integer[] ignoreTimeArr = ignoreTimeOpts
				.toArray(new Integer[ignoreTimeOpts.size()]);
		itemInfo.setIgnoreTimeArr(ignoreTimeArr);

		// load balance
		// List<String> loadBalanceOpts = getLoadbalanceOpts();
		String[] loadBalanceAttr = loadBalanceOpts
				.toArray(new String[loadBalanceOpts.size()]);
		itemInfo.setLbArr(loadBalanceAttr);
		return itemInfo;
	}

	/**
	 * 加载“持续时间” 设置的可选项
	 */
	private static List<Integer> getDurationOpts() {
		List<Integer> durationOpts = new ArrayList<Integer>();
		String defaultOpts = "10,20,30,40,50,60";

		String cfg = varManager.getStringValue(SystemVariable.STRATEGY_DURATION_KEY,
				defaultOpts);
		if (cfg == null || cfg.isEmpty()) {
			cfg = defaultOpts;
		}
		String[] opts = cfg.split(",");
		for (String temp : opts) {
			durationOpts.add(Integer.parseInt(temp));
		}
		return durationOpts;
	}

	/**
	 * 加载“CPU利用率” 设置的可选项
	 */
	private static List<Integer> getCpuUsageOpts() {
		List<Integer> cpuUsageOpts = new ArrayList<Integer>();
		String defaultOpts = "50,60,70,80";

		String cfg = varManager.getStringValue(SystemVariable.CPU_USAGE_KEY, defaultOpts);
		if (cfg == null || cfg.isEmpty()) {
			cfg = defaultOpts;
		}
		String[] opts = cfg.split(",");
		for (String temp : opts) {
			cpuUsageOpts.add(Integer.parseInt(temp));
		}
		return cpuUsageOpts;
	}

	/**
	 * 加载“内存使用率” 设置的可选项
	 */
	private static List<Integer> getMemUsageOpts() {
		List<Integer> memUsageOpts = new ArrayList<Integer>();
		String defaultOpts = "50,60,70,80";

		String cfg = varManager.getStringValue(SystemVariable.MEM_USAGE_KEY, defaultOpts);
		if (cfg == null || cfg.isEmpty()) {
			cfg = defaultOpts;
		}
		String[] opts = cfg.split(",");
		for (String temp : opts) {
			memUsageOpts.add(Integer.parseInt(temp));
		}
		return memUsageOpts;
	}

	/**
	 * 加载“网络流量” 设置的可选项
	 */
	private static List<Integer> getNetTrafficOpts() {
		List<Integer> netTrafficOpts = new ArrayList<Integer>();
		String defaultOpts = "40,60,80,100";

		String cfg = varManager.getStringValue(SystemVariable.NET_TRAFFIC_KEY, defaultOpts);
		if (cfg == null || cfg.isEmpty()) {
			cfg = defaultOpts;
		}
		String[] opts = cfg.split(",");
		for (String temp : opts) {
			netTrafficOpts.add(Integer.parseInt(temp));
		}
		return netTrafficOpts;
	}

	/**
	 * 加载“IO读写速率”设置的可选项
	 */
	private static List<Integer> loadIORateOpts() {
		List<Integer> ioRateOpts = new ArrayList<Integer>();
		String defaultOpts = "40,60,80,100";

		String cfg = varManager.getStringValue(SystemVariable.IO_RATE_KEY, defaultOpts);
		if (cfg == null || cfg.isEmpty()) {
			cfg = defaultOpts;
		}
		String[] opts = cfg.split(",");
		for (String temp : opts) {
			ioRateOpts.add(Integer.parseInt(temp));
		}
		return ioRateOpts;
	}

	/**
	 * 加载“负载均衡”设置的可选项
	 */
	private static List<String> getLoadbalanceOpts() {
		List<String> lbOpts = new ArrayList<String>();
		String defaultOpts = "0.5,1.0,1.5,2.0";

		String cfg = varManager.getStringValue(SystemVariable.LOAD_BALANCE_KEY, defaultOpts);
		if (cfg == null || cfg.isEmpty()) {
			cfg = defaultOpts;
		}
		String[] opts = cfg.split(",");
		for (String temp : opts) {
			lbOpts.add(temp);
		}
		return lbOpts;
	}

	/**
	 * 加载“伸缩幅度”可选项
	 */
	private static List<Integer> getStretchScaleOpts() {
		List<Integer> stretchScaleOpts = new ArrayList<Integer>();
		String defaultOpts = "1,2,3,4";

		String cfg = varManager.getStringValue(SystemVariable.STRETCH_SCALE_KEY, defaultOpts);
		if (cfg == null || cfg.isEmpty()) {
			cfg = defaultOpts;
		}
		String[] opts = cfg.split(",");
		for (String temp : opts) {
			stretchScaleOpts.add(Integer.parseInt(temp));
		}

		return stretchScaleOpts;
	}

	/**
	 * 加载伸缩后 忽略的时间间隔
	 */
	private static List<Integer> getIgnoreTimeOpts() {
		List<Integer> ignoreTimeOpts = new ArrayList<Integer>();
		String defaultOpts = "10,20,30,40,50,60";

		String cfg = varManager.getStringValue(SystemVariable.IGNORE_TIME_KEY, defaultOpts);
		if (cfg == null || cfg.isEmpty()) {
			cfg = defaultOpts;
		}
		String[] opts = cfg.split(",");
		for (String temp : opts) {
			ignoreTimeOpts.add(Integer.parseInt(temp));
		}
		return ignoreTimeOpts;
	}

}
