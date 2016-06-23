/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.manage.runtime.api.client.InstanceManager;
import org.gocom.cloud.cesium.manage.runtime.api.model.NodeAgent;

import com.primeton.paas.manage.api.impl.util.CesiumFactory;
import com.primeton.paas.manage.api.manager.IRuntimeManager;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class RuntimeManagerImpl implements IRuntimeManager {
	
	private InstanceManager instanceManager;
	
	public RuntimeManagerImpl() {
		super();
		instanceManager = CesiumFactory.getInstanceManager();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IRuntimeManager#getAgentState(java.lang.String)
	 */
	public boolean agentIsOnline(String ip) {
		if (StringUtil.isEmpty(ip)) {
			return false;
		}
		return instanceManager.existsAgent(ip);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IRuntimeManager#getAgents()
	 */
	public String[] getOnlineAgents() {
		List<String> list = new ArrayList<String>();
		NodeAgent[] agents = instanceManager.getRunningAgents();
		if (agents != null && agents.length > 0) {
			for (NodeAgent nodeAgent : agents) {
				if (nodeAgent.getIp() != null) {
					list.add(nodeAgent.getIp());
				}
			}
		}
		return list.toArray(new String[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IRuntimeManager#getInstanceState(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean srvIsRunning(String serviceType, String clusterId, String instanceId) {
		if (StringUtil.isEmpty(serviceType) || StringUtil.isEmpty(clusterId)
				|| StringUtil.isEmpty(instanceId)) {
			return false;
		}
		return instanceManager.existsInstance(serviceType, clusterId, Long.parseLong(instanceId));
	}

}
