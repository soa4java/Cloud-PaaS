/**
 * 
 */
package com.primeton.paas.manage.api.manager;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IRuntimeManager {

	/**
	 * If Agent online then return true. <br>
	 * 
	 * @param ip
	 * @return
	 */
	boolean agentIsOnline(String ip);
	
	/**
	 * Online agents. <br>
	 * 
	 * @return
	 */
	String[] getOnlineAgents();
	
	/**
	 * If service instance is running then return true. <br>
	 * 
	 * @param serviceType
	 * @param clusterId
	 * @param instanceId
	 * @return
	 */
	boolean srvIsRunning(String serviceType, String clusterId, String instanceId);
	
}
