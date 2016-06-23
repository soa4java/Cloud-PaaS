/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MemcachedStatus {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(MemcachedStatus.class);
	
	private MemcachedClient client;
	
	/**
	 * 
	 * @param servers
	 */
	public MemcachedStatus(String[] servers) {
		if (null == servers || servers.length == 0) {
			throw new IllegalArgumentException("Argument servers is null or no elements.");
		}
		StringBuffer address = new StringBuffer();
		for (String server : servers) {
			address.append(server).append(' ');
		}
		address.deleteCharAt(address.length() - 1);
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(address.toString()));
		try {
			client = builder.build();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<InetSocketAddress, Map<String, String>> getStats() {
		try {
			return null == client ? new HashMap<InetSocketAddress, Map<String, String>>() 
					: client.getStats();
		} catch (Throwable t) {
			logger.error(t.getMessage());
		} finally {
			this.close();
		}
		return new HashMap<InetSocketAddress, Map<String, String>>();
	}
	
	/**
	 * 
	 * @param server
	 * @return
	 */
	public Map<String,String> getStats(String server){
		if (null == server || null == client) {
			return new HashMap<String,String>();
		}
		String ip = server.substring(0, server.indexOf(":"));
		int port = Integer.parseInt(server.substring(server.indexOf(":") + 1,
				server.length()));
		try {
			return client.getStats().get(new InetSocketAddress(ip, port));
		} catch (Throwable t) {
			logger.error(t.getMessage());
		} finally {
			this.close();
		}
		return new HashMap<String,String>();
	}
	
	/**
	 * 
	 */
	public void close(){
		if (client == null) {
			return;
		}
		try {
			client.shutdown();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
}