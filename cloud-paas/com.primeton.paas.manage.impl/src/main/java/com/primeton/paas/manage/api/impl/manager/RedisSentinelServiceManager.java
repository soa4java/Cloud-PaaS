/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.mqclient.api.Command;
import org.gocom.cloud.cesium.mqclient.api.ServiceCommandMessage;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.cluster.RedisCluster;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IRedisSentinelServiceManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.RedisSentinelService;
import com.primeton.paas.manage.api.service.RedisService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Redis哨兵服务管理器. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RedisSentinelServiceManager extends DefaultServiceManager implements
		IRedisSentinelServiceManager {
	
	public static final String TYPE = RedisSentinelService.TYPE;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(RedisSentinelServiceManager.class);
	
	private static SentinelDaemon daemon;
	
	/**
	 * Key:Redis集群标识, Value:Redis主服务(ip:port)
	 */
	private Map<String, String> redises = new HashMap<String, String>();

	/**
	 * Default. <br>
	 */
	public RedisSentinelServiceManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#add(com.primeton.paas.manage.api.model.IService, java.lang.String)
	 */
	public <T extends IService> T add(T service, String clusterId)
			throws ServiceException {
		return create(service, clusterId);
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		
		RedisSentinelService service = getServiceQuery().get(id, RedisSentinelService.class);
		if (null == service) {
			return;
		}
		
		StringBuffer clustersBuffer = new StringBuffer();
		StringBuffer masterIPsBuffer = new StringBuffer();
		StringBuffer masterPortsBuffer = new StringBuffer();
		
		IClusterManager manager = ClusterManagerFactory.getManager();
		String clusterId = manager.getClusterId(id);
		
		List<RedisCluster> redisClusters = manager.getByType(RedisService.TYPE, RedisCluster.class);
		if (null == redisClusters || redisClusters.isEmpty()) {
			clustersBuffer.append("null"); //$NON-NLS-1$
			masterIPsBuffer.append("127.0.0.1"); //$NON-NLS-1$
			masterPortsBuffer.append("6379"); //$NON-NLS-1$
		} else {
			for (RedisCluster cluster : redisClusters) {
				RedisService master = getMasterRedis(cluster.getId());
				if (null == master) {
					continue;
				}
				//clustersBuffer.append(",").append(cluster.getId()); //$NON-NLS-1$
				String aliasName = cluster.getAliasName();
				aliasName = StringUtil.isEmpty(aliasName) ? cluster.getId() : aliasName;
				if (clustersBuffer.indexOf("," + aliasName) >= 0 || clustersBuffer.indexOf(aliasName + ",") >= 0) {
					aliasName = cluster.getId();
				}
				clustersBuffer.append(",").append(aliasName); //$NON-NLS-1$
				masterIPsBuffer.append(",").append(master.getIp()); //$NON-NLS-1$
				masterPortsBuffer.append(",").append(master.getPort()); //$NON-NLS-1$
			}
		}
		
		if (clustersBuffer.charAt(0) == ',') { //$NON-NLS-1$
			clustersBuffer = clustersBuffer.deleteCharAt(0);
		}
		if (masterIPsBuffer.charAt(0) == ',') { //$NON-NLS-1$
			masterIPsBuffer = masterIPsBuffer.deleteCharAt(0);
		}
		if (masterPortsBuffer.charAt(0) == ',') { //$NON-NLS-1$
			masterPortsBuffer = masterPortsBuffer.deleteCharAt(0);
		}
		
		// start
		ServiceCommandMessage message = new ServiceCommandMessage();
		message.setAction(ServiceCommandMessage.ACTION_START);
		message.setClusterName(clusterId);
		message.setIp(service.getIp());
		message.setPort(service.getPort());
		message.setSrvDefName(RedisSentinelService.TYPE);
		message.setSrvInstId(service.getId());
		message.setNeedResponse(true);
		
		Map<String, String> args = copy(service.getAttributes());
		args.put("ip", service.getIp()); //$NON-NLS-1$
		args.put("port", String.valueOf(service.getPort())); //$NON-NLS-1$
		args.put("redis_clusters", clustersBuffer.toString()); //$NON-NLS-1$
		args.put("redis_ips", masterIPsBuffer.toString()); //$NON-NLS-1$
		args.put("redis_ports", masterPortsBuffer.toString()); //$NON-NLS-1$
		
		Command body = new Command(args, SystemVariables.getScriptPath(RedisSentinelService.TYPE, SH_START));
		message.setBody(body);
		
		try {
			SendMessageUtil.sendMessage(message, SystemVariables.getMaxWaitMessageTime());
		} catch (Throwable t) {
			throw new ServiceException(t);
		}
	}
	
	/**
	 * 
	 * @param cluster
	 * @return
	 */
	protected RedisService getMasterRedis(String cluster) {
		if (null == cluster) {
			return null;
		}
		List<RedisService> services = getServiceQuery().getByCluster(cluster, RedisService.class);
		if (null == services || services.isEmpty()) {
			return null;
		}
		if (services.size() == 1) {
			return services.get(0);
		}
		for (RedisService service : services) {
			if (RedisService.RUN_AS_MASTER.equalsIgnoreCase(service.getRunMode())) {
				return service;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	private static Map<String, String> copy(Map<String, String> map) {
		if (null == map) {
			return null;
		}
		Map<String, String> newMap = new HashMap<String, String>();
		for (String key : map.keySet()) {
			newMap.put(key, map.get(key));
		}
		return newMap;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IRedisSentinelServiceManager#startupMonitor()
	 */
	public void startupMonitor() {
		if (null != daemon && daemon.isRunning()) {
			logger.warn("Redis service monitor daemon thread has been startup.");
			return;
		}
		daemon = new SentinelDaemon();
		Thread thread = new Thread(daemon);
		thread.setDaemon(true);
		thread.setName("Redis_Daemon"); //$NON-NLS-1$
		thread.start();
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IRedisSentinelServiceManager#shutdownMonitor()
	 */
	public void shutdownMonitor() {
		if (null == daemon || !daemon.isRunning()) {
			logger.warn("Redis service monitor daemon thread has been shut down.");
			return;
		}
		daemon.shutdown();
		daemon = null;
	}

	/**
	 * 如果Redis服务集群有变化,重启哨兵服务. <br>
	 */
	protected void sync() {
		IClusterManager manager = ClusterManagerFactory.getManager();
		ICluster[] clusters = manager.getByType(RedisService.TYPE);
		if (null == clusters || clusters.length == 0) {
			if (redises.isEmpty()) {
				return;
			}
			reboot();
		}
		Map<String, String> nowRedis = new HashMap<String, String>();
		for (ICluster cluster : clusters) {
			RedisService service = getMasterRedis(cluster.getId());
			nowRedis.put(cluster.getId(), null == service ? "" : service.getIp() + ":" + service.getPort()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (!equals(nowRedis, redises)) {
			reboot();
			redises.clear();
			redises = nowRedis;
		}
	}
	
	/**
	 * 
	 * @param map1
	 * @param map2
	 * @return
	 */
	private static boolean equals(Map<String, String> map1, Map<String, String> map2) {
		if (map1 == map2) {
			return true;
		}
		if (null == map1 && null == map2) {
			return true;
		}
		if (map1.size() != map2.size()) {
			return false;
		}
		for (String key : map1.keySet()) {
			if (!map2.containsKey(key)) {
				return false;
			}
			if (!equals(map1.get(key), map2.get(key))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	private static boolean equals(String value1, String value2) {
		if (null == value1 && null == value2) {
			return true;
		}
		if (null != value1) {
			return value1.equals(value2);
		}
		return false;
	}
	
	/**
	 * 重启所有的哨兵服务. <br>
	 */
	protected void reboot() {
		IService[] services = getServiceQuery().getByType(RedisSentinelService.TYPE);
		if (null == services || services.length == 0) {
			return;
		}
		for (IService service : services) {
			try {
				restart(service.getId());
			} catch (Throwable t) {
				logger.error(t.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	protected class SentinelDaemon implements Runnable {
		
		private boolean signal = true; 

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			while (signal) {
				// 健康检查
				try {
					sync();
				} catch (Throwable t) {
					logger.error(t);
				}
				// 休眠一下
				ThreadUtil.sleep(30L * 1000L); // 30s
			}
		}
		
		/**
		 * Shut down monitor. <br>
		 */
		public void shutdown() {
			this.signal = false;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isRunning() {
			return this.signal;
		}
		
	}
	
}
