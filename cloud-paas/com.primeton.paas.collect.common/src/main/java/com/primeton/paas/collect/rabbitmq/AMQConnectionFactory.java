/**
 * 
 */
package com.primeton.paas.collect.rabbitmq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.primeton.paas.collect.common.StringUtil;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-12-17
 *
 */
public class AMQConnectionFactory implements Runnable {
	
	public static final String DEFAULT = "default";
	
	private static Map<String, AMQConnection> connections = new ConcurrentHashMap<String, AMQConnection>();
	
	private static boolean isInit = false;
	
	private AMQConnectionFactory() {
		super();
	}
	
	private static synchronized void init() {
		if (isInit) {
			return;
		}
		Thread t = new Thread(new AMQConnectionFactory());
		t.setDaemon(true);
		t.setName("AMQP Connection Check Interval");
		t.start();
		isInit = true;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		return getConnection(DEFAULT);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Connection getConnection(String name) {
		AMQConnection conn = connections.get(name);
		return conn == null ? null : conn.getConnection();
	}
	
	/**
	 * 
	 * @param name
	 * @param override
	 * @param url
	 * @param username
	 * @param passward
	 * @param vhost
	 * @param timeout
	 * @return
	 */
	public synchronized static Connection getConnection(String name, boolean override, String url, 
			String username, String password, String vhost, int timeout) throws Exception {
		if (!isInit) {
			init();
		}
		if (StringUtil.isEmpty(url)) {
			return null;
		}
		name = StringUtil.isEmpty(name) ? DEFAULT : name;
		if (connections.containsKey(name) && !override) {
			return connections.get(name).getConnection();
		}
		ConnectionFactory factory = new ConnectionFactory();
		List<Address> addresses = new ArrayList<Address>();
		String[] servers = url.split(","); // 192.168.100.1:5672,192.168.100.2:5672...
		for (String server : servers) {
			String[] array = server.split(":");
			Address address = new Address(array[0], Integer.parseInt(array[1]));
			addresses.add(address);
		}
		
		factory.setConnectionTimeout(timeout > 0 ? timeout : 30000);
		factory.setUsername(StringUtil.isEmpty(username) ? ConnectionFactory.DEFAULT_USER : username);
		factory.setPassword(StringUtil.isEmpty(password) ? ConnectionFactory.DEFAULT_PASS : password);
		factory.setVirtualHost(StringUtil.isEmpty(vhost) ? ConnectionFactory.DEFAULT_VHOST : vhost);
		Connection connection = null;
		
		Exception exception = null;
		if (addresses.isEmpty()) {
			throw new IllegalArgumentException("AMQP connection url {" + url + "} is illegal.");
		} else {
			int count = 0;
			int index = (int)(addresses.size() * Math.random());

			while (count < addresses.size()) {
				Address address = addresses.get(index);
				try {
					connection = factory.newConnection(new Address[] { address });
					if (connection != null && connection.isOpen()) {
						break;
					}
				} catch (Exception e) {
					exception = e;
					e.printStackTrace();
				}
				count ++;
				index = (index + 1) % addresses.size();
			}
		}
		
		if (connection != null && connection.isOpen()) {
			AMQConnection conn = new AMQConnection();
			conn.setConnection(connection);
			conn.setPassword(password);
			conn.setTimeout(timeout);
			conn.setUrl(url);
			conn.setUsername(username);
			conn.setVhost(vhost);
			connections.put(name, conn);
			
			return connection;
		} else if (exception != null) {
			throw exception;
		}
		return null;
	}
	
	/**
	 * 
	 */
	private static void checkConnection() {
		for (String name : connections.keySet()) {
			AMQConnection conn = connections.get(name);
			if (conn == null) {
				connections.remove(name);
				return;
			}
			if (conn.getConnection() == null || !conn.getConnection().isOpen()) {
				try {
					getConnection(name, true, conn.getUrl(), conn.getUsername(), conn.getPassword(), conn.getVhost(), conn.getTimeout());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public static void clear() {
		for (String name : connections.keySet()) {
			AMQConnection conn = connections.remove(name);
			if (conn != null && conn.getConnection() != null) {
				try {
					conn.getConnection().close();
				} catch (IOException e) {
					// e.printStackTrace();
					conn.getConnection().abort();
				}
			}
		}
		connections.clear();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (true) {
			checkConnection();
			
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// ignore
				// e.printStackTrace();
			}
		}
	}

}
