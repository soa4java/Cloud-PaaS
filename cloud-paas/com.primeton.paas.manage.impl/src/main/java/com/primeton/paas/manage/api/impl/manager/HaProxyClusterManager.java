/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.CommandResultMessage;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.HaProxyService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class HaProxyClusterManager extends DefaultClusterManager {
	
	public static final String TYPE = HaProxyService.TYPE;
	
	private ILogger logger = ManageLoggerFactory.getLogger(HaProxyServiceManager.class);
	
	private static final String CONFIG_SCRIPT = "config.sh";
	
	private static final String HAPROXY_ID = "haproxyId";
	private static final String HAPROXY_IP = "ip";
	private static final String HAPROXY_PORT = "port";
	private static final String BACK_URLS = "backUrls";
	private static final String HEALTH_URL = "healthUrl";
	private static final String URL_HEALTH_CHECK_INTERVAL = "urlHealthCheckInterval";
	private static final String MAX_CONNECTION_SIZE = "maxConnectionSize";
	private static final String HAPROXY_PROTOCAL = "protocal"; // http | https | tcp
	private static final String CONNECTION_TIMEOUT = "contimeout";

	public HaProxyClusterManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.impl.DefaultClusterManager#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.impl.DefaultClusterManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		try {
			updateConf(id);
			super.start(id);
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.impl.DefaultClusterManager#restart(java.lang.String)
	 */
	public void restart(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		super.stop(id);
		this.start(id);
	}

	/**
	 * 
	 * @param id
	 * @throws Exception
	 */
	private void updateConf(String id) throws Exception {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		HaProxyCluster cluster = get(id, HaProxyCluster.class);
		if (cluster == null) {
			throw new Exception("Cluster [id:" + id + "] not found.");
		}
		// HaProxyCluster haProxyCluster = (HaProxyCluster)cluster;
		List<HaProxyService> haProxyInstances = getServiceQuery().getByCluster(id,
				HaProxyService.class);
		if (haProxyInstances == null || haProxyInstances.size() == 0) {
			throw new Exception("Cluster [id:" + id
					+ "] not exists HaProxy instances.");
		}
		
		String membersInfo = cluster.getMembers();
		if (StringUtil.isEmpty(membersInfo)) {
			String[] clusterIds = getRelationClustersId(id);
			if (clusterIds == null || clusterIds.length == 0) {
				throw new Exception("Cluster [id:" + id
						+ "] relation cluster not found.");
			}
			StringBuffer urlsBuffer = new StringBuffer();
			for (String clusterId : clusterIds) {
				IService[] insts = getServiceQuery().getByCluster(clusterId);
				if (insts != null && insts.length > 0) {
					for (IService inst : insts) {
						urlsBuffer.append(inst.getIp() + ":" + inst.getPort() + ",");
					}
				}
			}
			if(urlsBuffer.length() > 1) {
				urlsBuffer.deleteCharAt(urlsBuffer.length()- 1);
			}
			membersInfo = urlsBuffer.toString();
		}
		
		List<Exception> exceptions = new ArrayList<Exception>();
		for (HaProxyService haProxyService : haProxyInstances) {
			String haProxyIp = haProxyService.getIp();
			int haProxyPort = haProxyService.getPort();
			String haproxyId = haProxyService.getId();
			int maxConnectionSize = haProxyService.getMaxConnectionSize();
			String healthUrl = haProxyService.getHealthUrl();
			long inter = haProxyService.getUrlHealthCheckInterval();
			String protocal = haProxyService.getProtocal();
			long contimeout = haProxyService.getConnTimeout();

			Map<String, String> arguments = new HashMap<String, String>();
			arguments.put(HAPROXY_ID, haproxyId);
			arguments.put(HAPROXY_IP, haProxyIp);
			arguments.put(HAPROXY_PORT, String.valueOf(haProxyPort));
			arguments.put(MAX_CONNECTION_SIZE, String.valueOf(maxConnectionSize));
			arguments.put(URL_HEALTH_CHECK_INTERVAL, String.valueOf(inter));
			arguments.put(HEALTH_URL, healthUrl);
			arguments.put(BACK_URLS, membersInfo);
			arguments.put(HAPROXY_PROTOCAL, protocal);
			arguments.put(CONNECTION_TIMEOUT, String.valueOf(contimeout));

			try {
				String cmd = SystemVariables.getScriptPath(HaProxyService.TYPE, CONFIG_SCRIPT);
				CommandResultMessage message = SendMessageUtil.sendMessage(haProxyIp, cmd, arguments, true);
				if (message == null) {
					String error = "Can not receive NodeAgent [ip:" + haProxyIp + "] response, timeout or NodeAgent is died.";
					throw new Exception(error);
				} else if (message.getBody() == null) {
					String error = "NodeAgent [ip:" + haProxyIp + "] response message incomplete, message body is null.";
					throw new Exception(error);
				} else if (!message.getBody().getSuccess()) {
					throw new Exception(message.getBody().toString());
				} else {
					logger.info("Generate HaProxy [" + haProxyService + "] config file success.");
				}
			} catch (MessageException e) {
				logger.error(e);
				exceptions.add(e);
			}
			if (exceptions.size() > 0) {
				throw new Exception(exceptions.toString());
			}
		}
		
	}
	
}
