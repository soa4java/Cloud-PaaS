/**
 * 
 */
package com.primeton.paas.collect.util;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.zkclient.api.ZkClient;
import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;

import com.primeton.paas.collect.common.StringUtil;
import com.primeton.paas.collect.server.Constants;
import com.primeton.paas.collect.server.Server;
import com.primeton.paas.collect.server.ServerContext;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class PathUtil implements Constants {
	
	public static final String SEPARATOR = "/";
	public static final String ROOT_PATH = SEPARATOR + "Cloud" + SEPARATOR + "PaaS" + SEPARATOR + "Collector";
	
	private static ZkClient zkClient = ZkClientFactory.getZkClient();
	
	public static String getGroupPath() {
		return ROOT_PATH + SEPARATOR + ServerContext.getContext().getGroupName();
	}
	
	public static String getServerPath(String serverName) {
		return getGroupPath() + SEPARATOR + serverName;
	}
	
	public static String getServerPath() {
		return getServerPath(ServerContext.getContext().getServerName());
	}
	
	public static Server getServer(String serverName) {
		if (StringUtil.isEmpty(serverName)) {
			return null;
		}
		String groupPath = getGroupPath();
		boolean exists = zkClient.exists(groupPath);
		if (!exists) {
			zkClient.createPersistent(groupPath, true);
		}
		String path = getServerPath(serverName);
		if (zkClient.exists(path)) {
			return zkClient.readData(path);
		}
		return null;
	}
	
	public static Server getServer() {
		return getServer(ServerContext.getContext().getServerName());
	}
	
	public static List<Server> getServers() {
		List<Server> servers = new ArrayList<Server>();
		String groupPath = getGroupPath();
		boolean exists = zkClient.exists(groupPath);
		if (!exists) {
			zkClient.createPersistent(groupPath, true);
		}
		List<String> nodes = zkClient.getChildren(groupPath);
		if (nodes != null) {
			for (String serverName : nodes) {
				Server server = zkClient.readData(getServerPath(serverName));
				if (server != null) {
					servers.add(server);
				}
			}
		}
		return servers;
	}
	
	public static void setServer(Server server) {
		if (server == null || StringUtil.isEmpty(server.getName())) {
			return;
		}
		
		String path = getServerPath(server.getName());
		if (!zkClient.exists(getGroupPath())) {
			zkClient.createPersistent(getGroupPath(), true);
		}
		boolean exists = zkClient.exists(path);
		if (!exists) {
			zkClient.createEphemeral(path);
		}
		zkClient.writeData(path, server);
	}
	
	public static void deleteServer(String serverName) {
		String path = getServerPath(serverName);
		if (zkClient.exists(path)) {
			zkClient.delete(path);
		}
	}

}
