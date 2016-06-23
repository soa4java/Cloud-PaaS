/**
 * 
 */
package com.primeton.paas.console.common.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.manage.api.factory.HostManagerFactory;
import com.primeton.paas.manage.api.factory.VariableManagerFactory;
import com.primeton.paas.manage.api.manager.IHostManager;
import com.primeton.paas.manage.api.manager.IVariableManager;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.monitor.IMonitorDataManager;
import com.primeton.paas.manage.api.monitor.MetaData;
import com.primeton.paas.manage.api.monitor.MonitorDataManagerFactory;

/**
 * 主机监控工具类
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class HostMonitorUtil {

	// 日志
	private static ILogger logger = LoggerFactory.getLogger(HostMonitorUtil.class);
	// 主机管理
	private static IHostManager hostManager = HostManagerFactory
			.getHostManager();

	private static IMonitorDataManager monitorDataManager = MonitorDataManagerFactory
			.getManager();

	private static IVariableManager varManager = VariableManagerFactory
			.getManager();

	public static long CEP_MONITORINTERVAL = 10000;// 5s

	public static long CEP_ONEMINUTESAGO = 180 * 1000;// 展示1分钟前的数据

	public static void initCurrentVariable() {
		CEP_MONITORINTERVAL = varManager.getLongValue(
				SystemVariable.CEP_MONITORINTERVAL_KEY, 10) * 1000;
		CEP_ONEMINUTESAGO = varManager.getLongValue(
				SystemVariable.CEP_ONEMINUTESAGO_KEY, 60000);
	}

	public static long getCEP_ONEMINUTESAGO() {
		return varManager.getLongValue(SystemVariable.CEP_ONEMINUTESAGO_KEY,
				180000);
	}

	public static long getCEP_MONITORINTERVAL() {
		return varManager.getLongValue(SystemVariable.CEP_MONITORINTERVAL_KEY,
				10) * 1000;
	}

	/**
	 * 监控页面刷新时间间隔
	 * 
	 * @return
	 */
	public static Integer[] getRefreshTime() {
		List<Integer> list = new ArrayList<Integer>();
		String value = varManager.getStringValue(
				SystemVariable.MONITOR_REFRESHT_TIME_KEY, "10,30,45,60");
		String[] times = value.split(",");
		for (String str : times) {
			Integer size = Integer.parseInt(str);
			list.add(size);
		}
		return list.toArray(new Integer[list.size()]);
	}

	/**
	 * 
	 * @return
	 */
	public static Integer[] getTimeLengths() {
		List<Integer> list = new ArrayList<Integer>();
		String value = varManager.getStringValue(
				SystemVariable.MONITOR_TIME_LENGTHS_KEY, "10,30,60");
		String[] times = value.split(",");
		for (String str : times) {
			Integer size = Integer.parseInt(str);
			list.add(size);
		}
		return list.toArray(new Integer[list.size()]);
	}

	/**
	 * 获取所有主机
	 * 
	 * @return
	 */
	public static Host[] queryUsedHost() {
		Host[] hosts = hostManager.getAll();
		List<Host> hostList = new ArrayList<Host>();

		if (hosts == null) {
			return new Host[0];
		}
		for (Host host : hosts) {
			if (host.isAssigned()) {
				hostList.add(host);
			}
		}
		if (hostList.isEmpty()) {
			return new Host[0];
		}
		return hostList.toArray(new Host[hostList.size()]);
	}

	/**
	 * 获取主机状态
	 * 
	 * @param ip
	 * @return true 在线| false 离线
	 */
	public static boolean getHostState(String ip) {
		Host host = hostManager.get(ip);
		return host.isControlable();
	}

	/**
	 * 获取cpu监控数据
	 * 
	 * @param ip
	 *            主机IP
	 * @param begin
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	public static CPUInfo getCPUMonitorInfo(String ip, int minutes) {
		initCurrentVariable();
		long end = new Date().getTime() - CEP_ONEMINUTESAGO;
		long begin = end - minutes * 60 * 1000;
		List<MetaData> metaDatas = monitorDataManager.getByIp(ip, begin, end);
		metaDatas = sort(metaDatas);
		try {
			CPUInfo cpuInfo = new CPUInfo();
			StringBuffer usedStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();
			// 没有任何监控数据，补0
			if (metaDatas == null || metaDatas.isEmpty()) {
				for (int i = 0;; i++) {
					usedStr.append("0");
					long time = begin + i * CEP_MONITORINTERVAL;
					timeStr.append(time);
					if (time <= end) {
						usedStr.append(",");
						timeStr.append(",");
					} else {
						break;
					}
				}
				cpuInfo.setTimes(timeStr.toString());
				cpuInfo.setUses(usedStr.toString());
				return cpuInfo;
			} else {// 时间长度不够,补零
				if ((begin + 5 * CEP_MONITORINTERVAL) < metaDatas.get(0)
						.getTime()) {
					for (int i = 0;; i++) {
						long time = begin + i * CEP_MONITORINTERVAL;
						if (time <= metaDatas.get(0).getTime()) {
							usedStr.append("0,");
							timeStr.append(time + ",");
						} else {
							break;
						}
					}
				}
			}

			for (int i = 0; i < metaDatas.size(); i++) {
				MetaData amd = metaDatas.get(i);
				usedStr.append(amd.getCpuUs().toString());// cpu user use
				timeStr.append(amd.getTime().toString());
				if (i != metaDatas.size() - 1) {
					usedStr.append(",");
					timeStr.append(",");
				}
				// 与下一个数据相差5个采集时间间隔以上则补零
				int nextIndex = i + 1;
				if (nextIndex < (metaDatas.size() - 1)
						&& metaDatas.get(nextIndex).getTime() - amd.getTime() > (CEP_MONITORINTERVAL * 5)) {
					MetaData nextAmd = metaDatas.get(nextIndex);
					for (int j = 0;; j++) {
						long time = amd.getTime() + j * CEP_MONITORINTERVAL;
						if (time < nextAmd.getTime()) {
							usedStr.append("0,");
							timeStr.append(time);
							timeStr.append(",");
						} else {
							break;
						}
					}
				}
			}

			if ((metaDatas.get(metaDatas.size() - 1).getTime() + 5 * CEP_MONITORINTERVAL) < end) {// 监控停止
				for (int i = 0;; i++) {
					long time = metaDatas.get(metaDatas.size() - 1).getTime()
							+ CEP_MONITORINTERVAL * i;
					if (time < end) {
						usedStr.append(",0");
						timeStr.append(",");
						timeStr.append(time);
					} else {
						break;
					}
				}
			}
			cpuInfo.setTimes(timeStr.toString());
			cpuInfo.setUses(usedStr.toString());
			return cpuInfo;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 动态获取当前cpu监控数据
	 * 
	 * @param ip
	 *            主机IP
	 * @param timeInterval
	 *            s
	 * @param begin
	 * @param end
	 */
	public static CPUInfo getCurrentCPUInfo(String ip, long timeInterval) {
		long end = new Date().getTime() - getCEP_ONEMINUTESAGO();
		long begin = end - timeInterval + 1;
		List<MetaData> metaDatas = monitorDataManager.getByIp(ip, begin, end);
		metaDatas = sort(metaDatas);
		try {
			CPUInfo cpuInfo = new CPUInfo();
			StringBuffer usedStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();

			if (metaDatas == null || metaDatas.isEmpty()) {
				return cpuInfo;
			}

			for (int i = 0; i < metaDatas.size(); i++) {
				MetaData md = metaDatas.get(i);
				usedStr.append(md.getCpuUs().toString());// cpu user use
				timeStr.append(md.getTime().toString());
				if (i != metaDatas.size() - 1) {
					usedStr.append(",");
					timeStr.append(",");
				}
			}
			cpuInfo.setTimes(timeStr.toString());
			cpuInfo.setUses(usedStr.toString());
			return cpuInfo;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 获取内存监控数据
	 * 
	 * @param ip
	 * @param begin
	 * @param end
	 * @return
	 */
	public static MemoryInfo getMemoryMonitorInfo(String ip, int minutes) {
		initCurrentVariable();
		long end = new Date().getTime() - CEP_ONEMINUTESAGO;
		long begin = new Date(end - minutes * 60 * 1000).getTime();
		List<MetaData> metaDatas = monitorDataManager.getByIp(ip, begin, end);
		metaDatas = sort(metaDatas);
		try {
			MemoryInfo memInfo = new MemoryInfo();
			StringBuffer usedStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();
			// 没有任何监控数据，补0
			if (metaDatas == null || metaDatas.isEmpty()) {
				for (int i = 0;; i++) {
					usedStr.append("0");
					long time = begin + i * CEP_MONITORINTERVAL;
					timeStr.append(time);
					if (time <= end) {
						usedStr.append(",");
						timeStr.append(",");
					} else {
						break;
					}
				}
				memInfo.setTimes(timeStr.toString());
				memInfo.setUses(usedStr.toString());
				return memInfo;
			} else {// 时间长度不够,补零
				if (begin + 5 * CEP_MONITORINTERVAL < metaDatas.get(0)
						.getTime()) {
					for (int i = 0;; i++) {
						long time = begin + i * CEP_MONITORINTERVAL;
						if (time <= metaDatas.get(0).getTime()) {
							usedStr.append("0,");
							timeStr.append(time + ",");
						} else {
							break;
						}
					}
				}
			}

			for (int i = 0; i < metaDatas.size(); i++) {
				MetaData amd = metaDatas.get(i);
				usedStr.append(amd.getMemUs().toString());// mem user use
				timeStr.append(amd.getTime().toString());
				if (i != metaDatas.size() - 1) {
					usedStr.append(",");
					timeStr.append(",");
				}
				// 与下一个数据相差5个采集时间间隔以上则补零
				int nextIndex = i + 1;
				if (nextIndex < (metaDatas.size() - 1)
						&& metaDatas.get(nextIndex).getTime() - amd.getTime() > (CEP_MONITORINTERVAL * 5)) {
					MetaData nextAmd = metaDatas.get(nextIndex);
					for (int j = 0;; j++) {
						long time = amd.getTime() + j * CEP_MONITORINTERVAL;
						if (time < nextAmd.getTime()) {
							usedStr.append("0,");
							timeStr.append(time);
							timeStr.append(",");
						} else {
							break;
						}
					}
				}
			}

			if ((metaDatas.get(metaDatas.size() - 1).getTime() + 5 * CEP_MONITORINTERVAL) < end) {// 监控停止
				for (int i = 0;; i++) {
					long time = metaDatas.get(metaDatas.size() - 1).getTime()
							+ CEP_MONITORINTERVAL * i;
					if (time < end) {
						usedStr.append(",0");
						timeStr.append(",");
						timeStr.append(time);
					} else {
						break;
					}
				}
			}

			memInfo.setTimes(timeStr.toString());
			memInfo.setUses(usedStr.toString());
			return memInfo;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;

	}

	/**
	 * 动态获取当前Memory监控数据
	 * 
	 * @param ip
	 * @param time
	 * @return
	 */
	public static MemoryInfo getCurrentMemoryInfo(String ip, long timeInterval) {
		long end = new Date().getTime() - getCEP_ONEMINUTESAGO();
		long begin = end - timeInterval + 1;
		List<MetaData> metaDatas = monitorDataManager.getByIp(ip, begin, end);
		metaDatas = sort(metaDatas);
		try {
			MemoryInfo memInfo = new MemoryInfo();
			StringBuffer usedStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();

			if (metaDatas == null || metaDatas.isEmpty()) {// 没有数据 补0
				return memInfo;
			}

			for (int i = 0; i < metaDatas.size(); i++) {
				MetaData amd = metaDatas.get(i);
				usedStr.append(amd.getMemUs().toString());// mem user use
				timeStr.append(amd.getTime().toString());
				if (i != metaDatas.size() - 1) {
					usedStr.append(",");
					timeStr.append(",");
				}
			}
			memInfo.setTimes(timeStr.toString());
			memInfo.setUses(usedStr.toString());
			return memInfo;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 获取OneLoadAverage监控数据
	 * 
	 * @param ip
	 * @param begin
	 * @param end
	 * @return
	 */
	public static LoadAverageInfo getLoadAverageMonitorInfo(String ip,
			int minutes) {
		initCurrentVariable();
		long end = new Date().getTime() - CEP_ONEMINUTESAGO;
		long begin = new Date(end - minutes * 60 * 1000).getTime();
		List<MetaData> metaDatas = monitorDataManager.getByIp(ip, begin, end);
		metaDatas = sort(metaDatas);
		try {
			LoadAverageInfo laInfo = new LoadAverageInfo();
			StringBuffer loadAverageStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();
			// 没有任何监控数据，补0
			if (metaDatas == null || metaDatas.isEmpty()) {
				for (int i = 0;; i++) {
					loadAverageStr.append("0");
					long time = begin + i * CEP_MONITORINTERVAL;
					timeStr.append(time);
					if (time <= end) {
						loadAverageStr.append(",");
						timeStr.append(",");
					} else {
						break;
					}
				}
				laInfo.setTimes(timeStr.toString());
				laInfo.setLoadAverages(loadAverageStr.toString());

				return laInfo;
			} else {// 时间长度不够,补零
				if (begin + 5 * CEP_MONITORINTERVAL < metaDatas.get(0)
						.getTime()) {
					for (int i = 0;; i++) {
						long time = begin + i * CEP_MONITORINTERVAL;
						if (time <= metaDatas.get(0).getTime()) {
							loadAverageStr.append("0,");
							timeStr.append(time + ",");
						} else {
							break;
						}
					}
				}
			}

			for (int i = 0; i < metaDatas.size(); i++) {
				MetaData amd = metaDatas.get(i);
				loadAverageStr.append(amd.getCpuOneLoad().toString());// one
																		// loadaverage
				timeStr.append(amd.getTime().toString());
				if (i != metaDatas.size() - 1) {
					loadAverageStr.append(",");
					timeStr.append(",");
				}
				// 与下一个数据相差5个采集时间间隔以上则补零
				int nextIndex = i + 1;
				if (nextIndex < (metaDatas.size() - 1)
						&& metaDatas.get(nextIndex).getTime() - amd.getTime() > (CEP_MONITORINTERVAL * 5)) {
					MetaData nextAmd = metaDatas.get(nextIndex);
					for (int j = 0;; j++) {
						long time = amd.getTime() + j * CEP_MONITORINTERVAL;
						if (time < nextAmd.getTime()) {
							loadAverageStr.append("0,");
							timeStr.append(time);
							timeStr.append(",");
						} else {
							break;
						}
					}
				}
			}

			if ((metaDatas.get(metaDatas.size() - 1).getTime() + 5 * CEP_MONITORINTERVAL) < end) {// 监控停止
				for (int i = 0;; i++) {
					long time = metaDatas.get(metaDatas.size() - 1).getTime()
							+ CEP_MONITORINTERVAL * i;
					if (time < end) {
						loadAverageStr.append(",0");
						timeStr.append(",");
						timeStr.append(time);
					} else {
						break;
					}
				}
			}

			laInfo.setTimes(timeStr.toString());
			laInfo.setLoadAverages(loadAverageStr.toString());

			return laInfo;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 动态获取当前OneLoadAverage监控数据
	 * 
	 * @param ip
	 * @param time
	 * @return
	 */
	public static LoadAverageInfo getCurrentLoadAverageInfo(String ip,
			long timeInterval) {
		long end = new Date().getTime() - getCEP_ONEMINUTESAGO();
		long begin = end - timeInterval + 1;
		List<MetaData> metaDatas = monitorDataManager.getByIp(ip, begin, end);
		metaDatas = sort(metaDatas);
		try {
			LoadAverageInfo laInfo = new LoadAverageInfo();
			StringBuffer loadAverageStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();

			if (metaDatas == null || metaDatas.isEmpty()) {// 没有数据 补0
				return laInfo;
			}

			for (int i = 0; i < metaDatas.size(); i++) {
				MetaData md = metaDatas.get(i);
				loadAverageStr.append(md.getCpuOneLoad().toString());// cpu
																		// system
																		// used
				timeStr.append(md.getTime().toString());
				if (i != metaDatas.size() - 1) {
					loadAverageStr.append(",");
					timeStr.append(",");
				}
			}
			laInfo.setTimes(timeStr.toString());
			laInfo.setLoadAverages(loadAverageStr.toString());
			return laInfo;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 获取IO监控数据
	 * 
	 * @param ip
	 * @param begin
	 * @param end
	 * @return
	 */
	public static IOInfo getIOMonitorInfo(String ip, int minutes) {
		initCurrentVariable();
		long end = new Date().getTime() - CEP_ONEMINUTESAGO;
		long begin = new Date(end - minutes * 60 * 1000).getTime();
		List<MetaData> metaDatas = monitorDataManager.getByIp(ip, begin, end);
		metaDatas = sort(metaDatas);
		try {
			IOInfo ioInfo = new IOInfo();
			StringBuffer inputStr = new StringBuffer();
			StringBuffer outputStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();
			// 没有任何监控数据，补0
			if (metaDatas == null || metaDatas.isEmpty()) {
				for (int i = 0;; i++) {
					inputStr.append("0");
					outputStr.append("0");
					long time = begin + i * CEP_MONITORINTERVAL;
					timeStr.append(time);
					if (time <= end) {
						inputStr.append(",");
						outputStr.append(",");
						timeStr.append(",");
					} else {
						break;
					}
				}
				ioInfo.setTimes(timeStr.toString());
				ioInfo.setInputs(inputStr.toString());
				ioInfo.setOutputs(outputStr.toString());
				return ioInfo;
			} else {// 时间长度不够,补零
				if (begin + 5 * CEP_MONITORINTERVAL < metaDatas.get(0)
						.getTime()) {
					for (int i = 0;; i++) {
						long time = begin + i * CEP_MONITORINTERVAL;
						if (time <= metaDatas.get(0).getTime()) {
							inputStr.append("0,");
							outputStr.append("0,");
							timeStr.append(time + ",");
						} else {
							break;
						}
					}
				}
			}

			for (int i = 0; i < metaDatas.size(); i++) {
				MetaData amd = metaDatas.get(i);
				inputStr.append(amd.getIoBi().toString()); // io bios input
				outputStr.append(amd.getIoBo().toString()); // io bios output
				timeStr.append(amd.getTime().toString());
				if (i != metaDatas.size() - 1) {
					inputStr.append(",");
					outputStr.append(",");
					timeStr.append(",");
				}
				// 与下一个数据相差5个采集时间间隔以上则补零
				int nextIndex = i + 1;
				if (nextIndex < (metaDatas.size() - 1)
						&& metaDatas.get(nextIndex).getTime() - amd.getTime() > (CEP_MONITORINTERVAL * 5)) {
					MetaData nextAmd = metaDatas.get(nextIndex);
					for (int j = 0;; j++) {
						long time = amd.getTime() + j * CEP_MONITORINTERVAL;
						if (time < nextAmd.getTime()) {
							inputStr.append("0,");
							outputStr.append("0,");
							timeStr.append(time);
							timeStr.append(",");
						} else {
							break;
						}
					}
				}
			}

			if ((metaDatas.get(metaDatas.size() - 1).getTime() + 5 * CEP_MONITORINTERVAL) < end) {// 监控停止
				for (int i = 0;; i++) {
					long time = metaDatas.get(metaDatas.size() - 1).getTime()
							+ CEP_MONITORINTERVAL * i;
					if (time < end) {
						inputStr.append(",0");
						outputStr.append(",0");
						timeStr.append(",");
						timeStr.append(time);
					} else {
						break;
					}
				}
			}
			ioInfo.setTimes(timeStr.toString());
			ioInfo.setInputs(inputStr.toString());
			ioInfo.setOutputs(outputStr.toString());
			return ioInfo;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 动态获取当前IO监控数据
	 * 
	 * @param appName
	 * @param time
	 * @return
	 */
	public static IOInfo getCurrentIOInfo(String ip, long timeInterval) {
		long end = new Date().getTime() - getCEP_ONEMINUTESAGO();
		long begin = end - timeInterval + 1;
		List<MetaData> metaDatas = monitorDataManager.getByIp(ip, begin, end);
		metaDatas = sort(metaDatas);
		try {
			IOInfo ioInfo = new IOInfo();
			StringBuffer outputStr = new StringBuffer();
			StringBuffer inputStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();

			if (metaDatas == null || metaDatas.isEmpty()) {// 没有数据
				return ioInfo;
			}

			for (int i = 0; i < metaDatas.size(); i++) {
				MetaData md = metaDatas.get(i);
				inputStr.append(md.getIoBi().toString()); // io input bios used
				outputStr.append(md.getIoBo().toString()); // io output bios
															// used
				timeStr.append(md.getTime().toString());
				if (i != metaDatas.size() - 1) {
					inputStr.append(",");
					outputStr.append(",");
					timeStr.append(",");
				}
			}
			ioInfo.setTimes(timeStr.toString());
			ioInfo.setInputs(inputStr.toString());
			ioInfo.setOutputs(outputStr.toString());
			return ioInfo;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 
	 * @param metaDatas
	 * @return
	 */
	public static List<MetaData> sort(List<MetaData> metaDatas) {
		if (metaDatas == null) {
			return new ArrayList<MetaData>();
		}
		if (metaDatas.isEmpty()) {
			return metaDatas;
		}
		Comparator<MetaData> comparator = new Comparator<MetaData>() {
			public int compare(MetaData s1, MetaData s2) {
				return (s1.getTime() - s2.getTime()) > 0 ? 1 : -1;
			}
		};
		Collections.sort(metaDatas, comparator);
		return metaDatas;
	}

}
