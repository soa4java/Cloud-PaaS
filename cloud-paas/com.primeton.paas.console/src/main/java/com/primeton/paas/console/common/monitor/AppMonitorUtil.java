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
import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.cluster.TomcatCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.factory.VariableManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.IVariableManager;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.monitor.AppMetaData;
import com.primeton.paas.manage.api.monitor.IMonitorDataManager;
import com.primeton.paas.manage.api.monitor.MonitorDataManagerFactory;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.TomcatService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 资源监控
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class AppMonitorUtil {
	
	private static ILogger logger = LoggerFactory.getLogger(AppMonitorUtil.class);
	
	//应用管理
	private static IWebAppManager appManager = WebAppManagerFactory.getManager();
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager();
	private static IServiceQuery srvQueryMgr = ServiceManagerFactory.getServiceQuery();
	private static IMonitorDataManager monitorDataManager = MonitorDataManagerFactory.getManager();
	
	private static IVariableManager varManager = VariableManagerFactory.getManager();
	
	public static final int APP_NOMAL = 0;

	public static final int APP_STOPED = 1;
	
	public static final int APP_EXCEPTION =2;
	
	public static long CEP_MONITORINTERVAL = 10000; // 10s
	
	public static long CEP_ONEMINUTESAGO = 180 * 1000; // 展示3分钟前的数据
	
	/**
	 * 初始化. <br>
	 */
	public static void initCurrentVariable() {
		CEP_MONITORINTERVAL = varManager.getLongValue(
				SystemVariable.CEP_MONITORINTERVAL_KEY, 10) * 1000;
		CEP_ONEMINUTESAGO = varManager.getLongValue(
				SystemVariable.CEP_ONEMINUTESAGO_KEY, 60000);
	}
	
	/**
	 * 
	 * @return
	 */
	public static long getCEP_ONEMINUTESAGO() {
		return varManager.getLongValue(SystemVariable.CEP_ONEMINUTESAGO_KEY,
				180000);
	}
	
	/**
	 * 
	 * @return
	 */
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
	 * 查询所有已开通的应用 <br>
	 * @param userID
	 * @return
	 */
	public static WebApp[] queryOpendApps() {
		return appManager.getAll();
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	public static int getAppState(String appName) {
		ICluster jettyCluster = clusterManager.getByApp(appName, JettyCluster.TYPE);
		ICluster tomcatcluster = clusterManager.getByApp(appName, TomcatCluster.TYPE);
		
		String jettyClusterId = jettyCluster == null ? null : jettyCluster.getId();
		String tomcatClusterId = tomcatcluster == null ? null : tomcatcluster.getId();
		
		if (StringUtil.isEmpty(jettyClusterId)
				&& StringUtil.isEmpty(tomcatClusterId)) {
			return APP_EXCEPTION;
		}
		if (!StringUtil.isEmpty(jettyClusterId)) {
			List<JettyService> services = srvQueryMgr.getByCluster(jettyClusterId, JettyService.class);
			if (services == null || services.isEmpty()) {
				return APP_EXCEPTION;
			}
			
			/**
			 * 只要有一个Jetty服务实例是运行的，应用则是运行状态
			 */
			for (JettyService srv : services){
				int state = srv.getState();
				if (state == IService.STATE_RUNNING) {
					return APP_NOMAL;
				}
			}
		} else {
			List<TomcatService> services = srvQueryMgr.getByCluster(tomcatClusterId, TomcatService.class);
			if (services == null || services.isEmpty()) {
				return APP_EXCEPTION;
			}
			for (TomcatService srv : services){
				int state = srv.getState();
				if (state == IService.STATE_RUNNING) {
					return APP_NOMAL;
				}
			}
		}
		return APP_STOPED;
	}
	
	/**
	 * 获取cpu监控数据
	 * @param appName 应用标识
	 * @param begin	开始时间
	 * @param end	结束时间
	 * @return
	 */
	public static CPUInfo getCPUMonitorInfo(String appName, int minutes) {
		initCurrentVariable();
		long end = new Date().getTime()-CEP_ONEMINUTESAGO;
		long begin = end-minutes*60*1000;
		List<AppMetaData> appMetaDatas = monitorDataManager.getByApp(appName, begin, end);
		appMetaDatas = sort(appMetaDatas);
		try {
			CPUInfo cpuInfo = new CPUInfo();
			StringBuffer usedStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();
			// 没有任何监控数据，补0
			if (appMetaDatas == null || appMetaDatas.isEmpty()) {
				for(int i=0;;i++){
					usedStr.append("0");
					long time = begin+i*CEP_MONITORINTERVAL;
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
			} else {// 前端无数据 时间长度不够,补零
				if (begin + 5 * CEP_MONITORINTERVAL < appMetaDatas.get(0)
						.getTime()) {
					for (int i = 0;; i++) {
						long time = begin + i * CEP_MONITORINTERVAL;
						if (time <= appMetaDatas.get(0).getTime()) {
							usedStr.append("0,");
							timeStr.append(time + ",");
						} else {
							break;
						}
					}
				}
			}
			
			for (int i = 0; i < appMetaDatas.size(); i++) {
				AppMetaData amd = appMetaDatas.get(i); 
				usedStr.append(amd.getCpuUs().toString());//cpu system used
				timeStr.append(amd.getTime().toString());
				if (i != appMetaDatas.size() - 1) {
					usedStr.append(",");
					timeStr.append(",");
				}
				//与下一个数据相差5个采集时间间隔以上则补零
				int nextIndex = i+1;
				if (nextIndex < (appMetaDatas.size() - 1)
						&& appMetaDatas.get(nextIndex).getTime()
								- amd.getTime() > (CEP_MONITORINTERVAL * 5)) {
					AppMetaData nextAmd = appMetaDatas.get(nextIndex);
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
			
			//后段无数据，补零
			if ((appMetaDatas.get(appMetaDatas.size() - 1).getTime() + 5 * CEP_MONITORINTERVAL) < end) {// 监控停止
				for (int i = 0;; i++) {
					long time = appMetaDatas.get(appMetaDatas.size() - 1)
							.getTime() + CEP_MONITORINTERVAL * i;
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
	 * @param appName
	 * @param timeInterval  s
	 * @param begin			
	 * @param end
	 */
	public static CPUInfo getCurrentCPUInfo(String appName, long timeInterval) {
		long end = new Date().getTime()-getCEP_ONEMINUTESAGO();
		long begin = end-timeInterval+1;
		List<AppMetaData> appMetaDatas = monitorDataManager.getByApp(appName, begin, end);
		appMetaDatas = sort(appMetaDatas);
		try {
			CPUInfo cpuInfo = new CPUInfo();
			StringBuffer usedStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();
			if (appMetaDatas == null || appMetaDatas.isEmpty()) {//没有数据 补0
				return cpuInfo;
			}
			for (int i = 0; i < appMetaDatas.size(); i++) {
				AppMetaData amd = appMetaDatas.get(i); 
				usedStr.append(amd.getCpuUs().toString());//cpu user used
				timeStr.append(amd.getTime().toString());
				if (i != appMetaDatas.size() - 1) {
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
	 * @param appName
	 * @param begin
	 * @param end
	 * @return
	 */
	public static MemoryInfo getMemoryMonitorInfo(String appName, int minutes) {
		initCurrentVariable();
		long end = new Date().getTime()-CEP_ONEMINUTESAGO;
		long begin = end-minutes*60*1000;
		List<AppMetaData> appMetaDatas = monitorDataManager.getByApp(appName, begin, end);
		appMetaDatas = sort(appMetaDatas);
		try {
			MemoryInfo memInfo = new MemoryInfo();
			StringBuffer usedStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();
			//没有任何监控数据，补0
			if (appMetaDatas == null || appMetaDatas.isEmpty()) {
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
				if (begin + 5 * CEP_MONITORINTERVAL < appMetaDatas.get(0)
						.getTime()) {
					for (int i = 0;; i++) {
						long time = begin + i * CEP_MONITORINTERVAL;
						if (time <= appMetaDatas.get(0).getTime()) {
							usedStr.append("0,");
							timeStr.append(time + ",");
						} else {
							break;
						}
					}
				}
			}
			
			for (int i = 0; i < appMetaDatas.size(); i++) {
				AppMetaData amd = appMetaDatas.get(i);
				usedStr.append(amd.getMemUs().toString());//mem system used
				timeStr.append(amd.getTime().toString());
				if (i != appMetaDatas.size() - 1) {
					usedStr.append(",");
					timeStr.append(",");
				}
				//	与下一个数据相差5个采集时间间隔以上则补零
				int nextIndex = i+1;
				if (nextIndex < (appMetaDatas.size() - 1)
						&& appMetaDatas.get(nextIndex).getTime()
								- amd.getTime() > (CEP_MONITORINTERVAL * 5)) {
					AppMetaData nextAmd = appMetaDatas.get(nextIndex);
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
			
			if ((appMetaDatas.get(appMetaDatas.size() - 1).getTime() + 5 * CEP_MONITORINTERVAL) < end) {// 监控停止
				for (int i = 0;; i++) {
					long time = appMetaDatas.get(appMetaDatas.size() - 1)
							.getTime() + CEP_MONITORINTERVAL * i;
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
	 * @param appName
	 * @param time
	 * @return
	 */
	public static MemoryInfo getCurrentMemoryInfo(String appName,
			long timeInterval) {
		long end = new Date().getTime()-getCEP_ONEMINUTESAGO();
		long begin = end-timeInterval+1;
		List<AppMetaData> appMetaDatas = monitorDataManager.getByApp(appName, begin, end);
		appMetaDatas = sort(appMetaDatas);
		try {
			MemoryInfo memInfo = new MemoryInfo();
			StringBuffer usedStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();
			
			if (appMetaDatas == null || appMetaDatas.isEmpty()) {//没有数据 补0
				return memInfo;
			}
			
			for (int i = 0; i < appMetaDatas.size(); i++) {
				AppMetaData amd = appMetaDatas.get(i);// 时间顺序
				usedStr.append(amd.getMemUs().toString());// mem system used
				timeStr.append(amd.getTime().toString());
				if (i != appMetaDatas.size() - 1) {
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
	 * @param appName
	 * @param begin
	 * @param end
	 * @return
	 */
	public static LoadAverageInfo getLoadAverageMonitorInfo(String appName,int minutes){
		initCurrentVariable();
		long end = new Date().getTime()-CEP_ONEMINUTESAGO;
		long begin = end-minutes*60*1000;
		List<AppMetaData> appMetaDatas = monitorDataManager.getByApp(appName, begin, end);
		appMetaDatas = sort(appMetaDatas);
		try {
			LoadAverageInfo laInfo = new LoadAverageInfo();
			StringBuffer loadAverageStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();
			//没有任何监控数据，补0
			if (appMetaDatas == null || appMetaDatas.isEmpty()) {
				for (int i = 0;; i++) {
					loadAverageStr.append("0");
					long time = begin+i*CEP_MONITORINTERVAL;
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
				if (begin + 5 * CEP_MONITORINTERVAL < appMetaDatas.get(0)
						.getTime()) {
					for (int i = 0;; i++) {
						long time = begin + i * CEP_MONITORINTERVAL;
						if (time <= appMetaDatas.get(0).getTime()) {
							loadAverageStr.append("0,");
							timeStr.append(time + ",");
						} else {
							break;
						}
					}
				}
			}
			
			for (int i = 0; i < appMetaDatas.size(); i++) {
				AppMetaData amd = appMetaDatas.get(i);
				loadAverageStr.append(amd.getCpuOneLoad().toString());
				timeStr.append(amd.getTime().toString());
				if (i != appMetaDatas.size() - 1) {
					loadAverageStr.append(",");
					timeStr.append(",");
				}
				
				//	与下一个数据相差5个采集时间间隔以上则补零
				int nextIndex = i + 1;
				if (nextIndex < (appMetaDatas.size() - 1)
						&& appMetaDatas.get(nextIndex).getTime()
								- amd.getTime() > (CEP_MONITORINTERVAL * 5)) {
					AppMetaData nextAmd = appMetaDatas.get(nextIndex);
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
			
			if ((appMetaDatas.get(appMetaDatas.size() - 1).getTime() + 5 * CEP_MONITORINTERVAL) < end) {// 监控停止
				for (int i = 0;; i++) {
					long time = appMetaDatas.get(appMetaDatas.size() - 1)
							.getTime() + CEP_MONITORINTERVAL * i;
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
	 * @param appName
	 * @param time
	 * @return
	 */
	public static LoadAverageInfo getCurrentLoadAverageInfo(String appName,
			long timeInterval) {
		long end = new Date().getTime()-getCEP_ONEMINUTESAGO();
		long begin = end-timeInterval+1;
		List<AppMetaData> appMetaDatas = monitorDataManager.getByApp(appName, begin, end);
		appMetaDatas = sort(appMetaDatas);
		try {
			LoadAverageInfo laInfo = new LoadAverageInfo();
			StringBuffer loadAverageStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();
			
			if (appMetaDatas == null || appMetaDatas.isEmpty()) {//没有数据 补0
				return laInfo;
			}
			
			for (int i = 0; i < appMetaDatas.size(); i++) {
				AppMetaData amd = appMetaDatas.get(i);
				loadAverageStr.append(amd.getCpuOneLoad().toString());// cpu
																		// system
																		// used
				timeStr.append(amd.getTime().toString());
				if (i != appMetaDatas.size() - 1) {
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
	 * @param appName
	 * @param begin
	 * @param end
	 * @return
	 */
	public static IOInfo getIOMonitorInfo(String appName, int minutes) {
		initCurrentVariable();
		long end = new Date().getTime()-CEP_ONEMINUTESAGO;
		long begin = end-minutes*60*1000;
		List<AppMetaData> appMetaDatas = monitorDataManager.getByApp(appName, begin, end);
		appMetaDatas = sort(appMetaDatas);
		try {
			IOInfo ioInfo = new IOInfo();
			StringBuffer inputStr = new StringBuffer();
			StringBuffer outputStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();
			// 没有任何监控数据，补0
			if (appMetaDatas == null || appMetaDatas.isEmpty()) {
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
				if (begin + 5 * CEP_MONITORINTERVAL < appMetaDatas.get(0)
						.getTime()) {
					for (int i = 0;; i++) {
						long time = begin + i * CEP_MONITORINTERVAL;
						if (time <= appMetaDatas.get(0).getTime()) {
							inputStr.append("0,");
							outputStr.append("0,");
							timeStr.append(time + ",");
						} else {
							break;
						}
					}
				}
			}
			
			for (int i = 0; i < appMetaDatas.size(); i++) {
				AppMetaData amd = appMetaDatas.get(i);
				inputStr.append(amd.getIoBi().toString());
				outputStr.append(amd.getIoBo().toString());// io bios output
				timeStr.append(amd.getTime().toString());
				if (i != appMetaDatas.size() - 1) {
					inputStr.append(",");
					outputStr.append(",");
					timeStr.append(",");
				}

				// 与下一个数据相差5个采集时间间隔以上则补零
				int nextIndex = i + 1;
				if (nextIndex < (appMetaDatas.size() - 1)
						&& appMetaDatas.get(nextIndex).getTime()
								- amd.getTime() > (CEP_MONITORINTERVAL * 5)) {
					AppMetaData nextAmd = appMetaDatas.get(nextIndex);
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
			if ((appMetaDatas.get(appMetaDatas.size() - 1).getTime() + 5 * CEP_MONITORINTERVAL) < end) {// 监控停止
				for (int i = 0;; i++) {
					long time = appMetaDatas.get(appMetaDatas.size() - 1)
							.getTime() + CEP_MONITORINTERVAL * i;
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
	 * @param appName
	 * @param time
	 * @return
	 */
	public static IOInfo getCurrentIOInfo(String appName,long timeInterval) {
		long end = new Date().getTime()-getCEP_ONEMINUTESAGO();
		long begin = end-timeInterval+1;
		List<AppMetaData> appMetaDatas = monitorDataManager.getByApp(appName, begin, end);
		appMetaDatas = sort(appMetaDatas);
		try {
			IOInfo ioInfo = new IOInfo();
			StringBuffer outputStr = new StringBuffer();
			StringBuffer inputStr = new StringBuffer();
			StringBuffer timeStr = new StringBuffer();
			
			if (appMetaDatas == null || appMetaDatas.isEmpty()) {//没有数据
				return ioInfo;
			}
			
			for (int i = 0; i < appMetaDatas.size(); i++) {
				AppMetaData amd = appMetaDatas.get(i);
				inputStr.append(amd.getIoBi().toString()); // io input bios used
				outputStr.append(amd.getIoBo().toString()); // io output bios
															// used
				timeStr.append(amd.getTime().toString());
				if (i != appMetaDatas.size() - 1) {
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
	public static List<AppMetaData> sort(List<AppMetaData> metaDatas) {
		if (metaDatas == null) {
			return new ArrayList<AppMetaData>();
		}
		if (metaDatas.isEmpty()) {
			return metaDatas;
		}
		Comparator<AppMetaData> comparator = new Comparator<AppMetaData>() {
			public int compare(AppMetaData s1, AppMetaData s2) {
				return (s1.getTime() - s2.getTime()) > 0 ? 1 : -1;
			}
		};
		Collections.sort(metaDatas,comparator);
		return metaDatas;
	}

}
