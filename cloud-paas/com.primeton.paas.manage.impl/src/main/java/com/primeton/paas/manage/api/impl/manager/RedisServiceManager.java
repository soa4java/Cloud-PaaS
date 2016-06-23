/**
 *
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.Command;
import org.gocom.cloud.cesium.mqclient.api.ServiceCommandMessage;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.ServiceUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IRedisServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.RedisService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RedisServiceManager extends DefaultServiceManager 
		implements IRedisServiceManager {
	
	public static final String TYPE = RedisService.TYPE;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(RedisServiceManager.class);

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		RedisService service = getServiceQuery().get(id, RedisService.class);
		if (service == null) {
			String message ="Redis instance [" + id + "] not exists, can not execute start action.";
			if (logger.isDebugEnabled()) {
				logger.debug(message);
			}
			return;
		}
		if (RedisService.RUN_AS_MASTER.equalsIgnoreCase(service.getRunMode())) {
			super.start(id);
			return;
		}
		IClusterManager manager = ClusterManagerFactory.getManager();
		String clusterId = manager.getClusterId(id);
		if (StringUtil.isEmpty(clusterId)) {
			super.start(id);
			return;
		}
		
		List<RedisService> services = getServiceQuery().getByCluster(clusterId, RedisService.class);
		if (null == services || services.isEmpty() || services.size() == 1) {
			super.start(id);
			return;
		}
		// Find master
		RedisService master = null;
		for (RedisService instance : services) {
			if (RedisService.RUN_AS_MASTER.equalsIgnoreCase(instance.getRunMode())) {
				master = instance;
				break;
			}
		}
		if (null == master) {
			super.start(id);
			return;
		}
		// start
		ServiceCommandMessage message = new ServiceCommandMessage();
		message.setAction(ServiceCommandMessage.ACTION_START);
		message.setClusterName(clusterId);
		message.setIp(service.getIp());
		message.setPort(service.getPort());
		message.setSrvDefName(RedisService.TYPE);
		message.setSrvInstId(service.getId());
		message.setNeedResponse(true);
		
		Map<String, String> args = copy(service.getAttributes());
		args.put("ip", service.getIp()); //$NON-NLS-1$
		args.put("port", String.valueOf(service.getPort())); //$NON-NLS-1$
		args.put("masterIp", master.getIp()); //$NON-NLS-1$
		args.put("masterPort", String.valueOf(master.getPort())); //$NON-NLS-1$
		
		Command body = new Command(args, SystemVariables.getScriptPath(RedisService.TYPE, SH_START));
		message.setBody(body);
		
		try {
			SendMessageUtil.sendMessage(message, SystemVariables.getMaxWaitMessageTime());
		} catch (MessageException e) {
			throw new ServiceException(e);
		}
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
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#add(com.primeton.paas.manage.api.model.IService, java.lang.String)
	 */
	public <T extends IService> T add(T service, String clusterId)
			throws ServiceException {
		return create(service, clusterId);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IRedisServiceManager#add(com.primeton.paas.manage.api.service.RedisService, java.lang.String, int)
	 */
	public List<RedisService> add(RedisService service, String clusterId,
			int number) throws ServiceException {
		if (null == service || StringUtil.isEmpty(clusterId) || number < 1) {
			return new ArrayList<RedisService>();
		}
		service.setHaMode(IService.HA_MODE_CLUSTER);
		// 申请机器
		String[] ips = null;
		try {
			ips = getHostAssignManager().apply(service.getPackageId(), service.getType(), service.isStandalone(), 
					number, getHostAssignManager().getInstallTimeout(getType()));
		} catch (Throwable t) {
			logger.error("Assign host resource error.", t);
		}
		if (ips == null || ips.length == 0) {
			logger.error("Assign host resource error, not enough host resource.");
			throw new ServiceException("Not enough host resource for apply. require [" + number + "]");
		}
		if (number == ips.length) {
			logger.info("Create redis service required machine [" + number + "] has applied for.");
		} else {
			try {
				getHostManager().release(ips);
			} catch (HostException e) {
				logger.error(e);
			}
			throw new ServiceException("Not enough host resource for apply, require [" + number + "], left [" + ips.length + "].");
		}
		
		List<RedisService> services = new ArrayList<RedisService>();
		for (int i=0; i<number; i++) {
			RedisService instance = ServiceUtil.copy(service);
			instance.setRunMode(i == 0? RedisService.RUN_AS_MASTER : RedisService.RUN_AS_SLAVE);
			instance.setIp(ips[i]); // 设置IP
			services.add(super.add(instance, clusterId)); // 保存至数据库
		}
		return services;
	}
	
}
