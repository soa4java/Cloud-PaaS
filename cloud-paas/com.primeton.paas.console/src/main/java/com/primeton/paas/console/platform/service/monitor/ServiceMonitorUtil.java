/**
 * 
 */
package com.primeton.paas.console.platform.service.monitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gocom.cloud.cesium.mqclient.util.StringUtil;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.console.common.monitor.ServiceMonitorInfo;
import com.primeton.paas.console.common.stretch.ServiceStrategyItemInfo;
import com.primeton.paas.manage.api.app.IServiceWarnStrategyManager;
import com.primeton.paas.manage.api.app.ServiceWarnStrategy;
import com.primeton.paas.manage.api.app.ServiceWarnStrategyItem;
import com.primeton.paas.manage.api.cluster.NginxCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceWarnStrategyManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.listener.ServiceEventListener;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.monitor.IMonitorDataManager;
import com.primeton.paas.manage.api.monitor.MonitorDataManagerFactory;
import com.primeton.paas.manage.api.monitor.ServiceMetaData;

/**
 * 平台（应用）服务监控
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class ServiceMonitorUtil {
	
	private static IMonitorDataManager monitorDataManager = MonitorDataManagerFactory
			.getManager();

	private static IServiceWarnStrategyManager serviceWarnMgr = ServiceWarnStrategyManagerFactory
			.getManager();

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager();

	private static ILogger logger = ManageLoggerFactory
			.getLogger(ServiceMonitorUtil.class);

	private static IWebAppManager appManager = WebAppManagerFactory
			.getManager();
	private static IServiceQuery query = ServiceManagerFactory
			.getServiceQuery();
	
	/**
	 * 服务监控数据
	 * @param clusterId
	 * @return
	 */
	public static ServiceMonitorInfo getServiceMonitorData(String clusterId) {
		ServiceMetaData metaData = monitorDataManager.getByClusterId(clusterId);
		ServiceMonitorInfo smi = new ServiceMonitorInfo();
		if (metaData != null && metaData.getCpuUs() != null) {
			smi.setCpuUse(metaData.getCpuUs().toString());
			smi.setCpuFree(String.valueOf((100.0f-metaData.getCpuUs())));
			
			smi.setMemUse(metaData.getMemUs().toString());
			smi.setMemFree(String.valueOf((100.0f-metaData.getMemUs())));
			
			smi.setOneload(metaData.getCpuOneLoad().toString());
			
			smi.setIoi(metaData.getIoBi().toString());
			smi.setIoo(metaData.getIoBo().toString());
			
			smi.setCurrentTime(metaData.getTime().toString());
			
			ServiceWarnStrategy sws = getServiceWarnStrategy(clusterId);
			smi.setEnableFlag(sws==null?false:sws.isEnable());
		}
		return smi;
	}
	
	/**
	 * @param appName
	 * @return
	 */
	public static boolean queryIfAppEnableServiceMonitor(String appName) {
		return serviceWarnMgr.queryAppServiceMonitor(appName);
	}
	
	/**
	 * 获取服务报警策略信息
	 * 
	 * @param clusterId	服务集群ID
	 * @return			策略信息
	 */
	public static ServiceWarnStrategy getServiceWarnStrategy(String clusterId) {
		return serviceWarnMgr.get(clusterId);
	}
	
	/**
	 * 
	 * @return
	 */
	public static ServiceWarnStrategy getGlobalServiceStrategyConfigInfo() {
		return serviceWarnMgr.getGlobalStrategy();
	}
	
	/**
	 * 设置服务报警策略为全局策略
	 */
	public static boolean setServiceGlobalStretchStrategy(String clusterId) {
		//保存clusterId服务为全局报警策略
		ServiceWarnStrategy sws = serviceWarnMgr.getGlobalStrategy();
		sws.setClusterId(clusterId);
		serviceWarnMgr.save(sws);
		return true;
	}
	
	
	/**
	 * 保存服务报警策略
	 * 
	 * @param strategyItem	策略项
	 * @param clusterId		服务集群ID
	 * @param appName		应用名
	 * @return				保存结果 false|true
	 */
	public static boolean saveServiceWarnStrategy(
			ServiceStrategyItemInfo strategyItem, String clusterId) {
		if (clusterId == null || strategyItem == null) {
			return false;
		}
		ServiceWarnStrategy strategy = serviceWarnMgr.get(clusterId);
		String strategyId = clusterId;

		if (strategy == null) {
			strategy = new ServiceWarnStrategy();
			strategy.setStrategyId(strategyId);
		} else {
			strategy.setStrategyId(strategyId);
		}
		strategy.setAlarmType(strategyItem.getAlarmType() == null ? ServiceWarnStrategy.SMS_ALARM
				: strategyItem.getAlarmType());
		strategy.setAlarmAddress(strategyItem.getAlarmAddress());
		// strategy.setAppName(appName);
		strategy.setClusterId(clusterId);
		strategy.setContinuedTime(strategyItem.getContinuedTime());
		strategy.setIgnoreTime(strategyItem.getIgnoreTime());
		strategy.setEnable("N".equals(strategyItem.getEnableFlag()) ? false : true); //$NON-NLS-1$

		List<ServiceWarnStrategyItem> items = new ArrayList<ServiceWarnStrategyItem>();
		strategy.setStrategyItems(items);// 清空

		ServiceWarnStrategyItem ssiCPU = new ServiceWarnStrategyItem();
		ssiCPU.setItemType(ServiceStrategyItemInfo.TYPE_CPU);
		ssiCPU.setThreshold(strategyItem.getCpuThreshold());
		ssiCPU.setStrategyId(strategyId);
		items.add(ssiCPU);
		ServiceWarnStrategyItem ssiMemeroy = new ServiceWarnStrategyItem();
		ssiMemeroy.setItemType(ServiceStrategyItemInfo.TYPE_MEMORY);
		ssiMemeroy.setThreshold(strategyItem.getMemThreshold());
		ssiMemeroy.setStrategyId(strategyId);
		items.add(ssiMemeroy);
		ServiceWarnStrategyItem ssiLB = new ServiceWarnStrategyItem();
		ssiLB.setItemType(ServiceStrategyItemInfo.TYPE_LB);
		ssiLB.setThreshold(strategyItem.getLbThreshold());
		ssiLB.setStrategyId(strategyId);
		items.add(ssiLB);

		strategy.addStrategyItems(items);

		serviceWarnMgr.save(strategy);
		return true;
	}
	
	/**
	 * 保存全局配置
	 * 
	 * @param strategyItem
	 * @return
	 */
	public static boolean saveServiceGlobalStrategy(
			ServiceStrategyItemInfo strategyItem) {
		ServiceWarnStrategy strategy = serviceWarnMgr.getGlobalStrategy();
		String strategyId = null;

		if (strategy == null) {
			strategy = new ServiceWarnStrategy();
			strategyId = ServiceWarnStrategy.GLOBAL_STRATEGY_ID;
			strategy.setStrategyId(strategyId);
		}

		strategy.setAlarmType(strategyItem.getAlarmType() == null ? ServiceWarnStrategy.SMS_ALARM
				: strategyItem.getAlarmType());
		strategy.setAlarmAddress(strategyItem.getAlarmAddress());

		strategy.setContinuedTime(strategyItem.getContinuedTime());
		strategy.setIgnoreTime(strategyItem.getIgnoreTime());
		strategy.setEnable("N".equals(strategyItem.getEnableFlag()) ? false //$NON-NLS-1$
				: true);

		List<ServiceWarnStrategyItem> items = new ArrayList<ServiceWarnStrategyItem>();
		strategy.setStrategyItems(items); 

		ServiceWarnStrategyItem ssiCPU = new ServiceWarnStrategyItem();
		ssiCPU.setItemType(ServiceStrategyItemInfo.TYPE_CPU);
		ssiCPU.setThreshold(strategyItem.getCpuThreshold());
		ssiCPU.setStrategyId(strategyId);
		items.add(ssiCPU);
		ServiceWarnStrategyItem ssiMemeroy = new ServiceWarnStrategyItem();
		ssiMemeroy.setItemType(ServiceStrategyItemInfo.TYPE_MEMORY);
		ssiMemeroy.setThreshold(strategyItem.getMemThreshold());
		ssiMemeroy.setStrategyId(strategyId);
		items.add(ssiMemeroy);
		ServiceWarnStrategyItem ssiLB = new ServiceWarnStrategyItem();
		ssiLB.setItemType(ServiceStrategyItemInfo.TYPE_LB);
		ssiLB.setThreshold(strategyItem.getLbThreshold());
		ssiLB.setStrategyId(strategyId);
		items.add(ssiLB);

		strategy.addStrategyItems(items);

		serviceWarnMgr.save(strategy);
		return true;
	}
	
	/**
	 * 启用服务监控
	 * @param appName
	 */
	public static void enableServiceMonitor(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return;
		}
		ICluster[] clusters = clusterManager.getByApp(appName);
		List<ServiceEventListener> serviceListeners = loadServiceEventListeners();
		for (ServiceEventListener listener : serviceListeners) {
			for(ICluster cluster:clusters){
				try {
					listener.doCreate(cluster);
				} catch (Throwable t) {
					logger.error(t);
				}
			}
		}
		for (ICluster cluster : clusters) {
			IService[] services = query.getByCluster(cluster.getId());
			if(services==null||services.length<=0||IService.MODE_LOGIC.equals(services[0].getMode())){
				continue;
			}
			ServiceWarnStrategy strategy = createDefaultStrategy(cluster, appName);
			serviceWarnMgr.save(strategy);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private static List<ServiceEventListener> loadServiceEventListeners() {
		List<ServiceEventListener> listeners = new ArrayList<ServiceEventListener>();
		ServiceExtensionLoader<ServiceEventListener> loader = ServiceExtensionLoader
				.load(ServiceEventListener.class);
		if (loader != null) {
			Iterator<ServiceEventListener> iterator = loader.iterator();
			while (iterator.hasNext()) {
				ServiceEventListener listener = iterator.next();
				if (listener != null) {
					listeners.add(listener);
				}
			}
		}
		return listeners;
	}
	
	/**
	 * 
	 * @param cluster
	 * @param appName
	 * @return
	 */
	private static ServiceWarnStrategy createDefaultStrategy(ICluster cluster,
			String appName) {
		if (null == cluster) {
			return null;
		}
		ServiceWarnStrategy strategy = serviceWarnMgr.getGlobalStrategy();
		strategy.setClusterId(cluster.getId());
		return strategy;
	}
	
	/**
	 * 禁用服务监控
	 * @param appName
	 */
	public static void disableServiceMonitor(String appName) {
		WebApp webApp = appManager.get(appName);
		ICluster[] clusters = clusterManager.getByApp(webApp.getName());
		List<ServiceEventListener> serviceListeners = loadServiceEventListeners();
		for (ServiceEventListener listener : serviceListeners) {
			for (ICluster cluster : clusters) {
				try {
					listener.doDestroy(cluster);
				} catch (Throwable t) {
					logger.error(t);
				}
			}
		}
		for (ICluster cluster : clusters) {
			serviceWarnMgr.remove(cluster.getId());
		}
	}
	
	/**
	 * 获取平台服务集群
	 * @return
	 */
	public static List<ICluster> getPlatformClusters() {
		// 获取需要监控的服务集群
		// Nginx
		List<ICluster> clist = new ArrayList<ICluster>();
		ICluster[] clusters = clusterManager.getByType(NginxCluster.TYPE);
		if (clusters != null) {
			for (ICluster cluster : clusters) {
				cluster.setName(cluster.getName()+"("+cluster.getId()+")");
				clist.add(cluster);
			}
		}
		return clist;
	}
	
	/**
	 * 启用服务监控
	 * @param clusterId
	 */
	public static void enableSingleServiceMonitor(String clusterId){
		if (StringUtil.isEmpty(clusterId)) {
			return;
		}
		ICluster cluster = clusterManager.get(clusterId);
		List<ServiceEventListener> serviceListeners = loadServiceEventListeners();
		for (ServiceEventListener listener : serviceListeners) {
				try {
					listener.doCreate(cluster);
				} catch (Throwable t) {
					logger.error(t);
				}
		}
		IService[] services = query.getByCluster(clusterId);
		if(services==null||services.length<=0||IService.MODE_LOGIC.equals(services[0].getMode())){
			logger.error("Create cluster service monitor failed ["+clusterId+"].");
			return;
		}
		ServiceWarnStrategy strategy = createDefaultStrategy(clusterId); 
		serviceWarnMgr.save(strategy);
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	private static ServiceWarnStrategy createDefaultStrategy(String clusterId){
		ServiceWarnStrategy strategy = serviceWarnMgr.getGlobalStrategy();
		strategy.setClusterId(clusterId);
		return strategy;
	}
	
	/**
	 * 禁用服务监控
	 * @param clusterId
	 */
	public static void disableSingleServiceMonitor(String clusterId){
		ICluster cluster = clusterManager.get(clusterId);
		List<ServiceEventListener> serviceListeners = loadServiceEventListeners();
		for (ServiceEventListener listener : serviceListeners) {
			try {
				listener.doDestroy(cluster);
			} catch (Throwable t) {
				logger.error(t);
			}
		}
		serviceWarnMgr.remove(clusterId);
	}
	
	/**
	 * @param clusterId
	 * @return
	 */
	public static boolean queryIfClusterEnableServiceMonitor(String clusterId){
		return serviceWarnMgr.queryServiceMonitor(clusterId);
	}
	
}
