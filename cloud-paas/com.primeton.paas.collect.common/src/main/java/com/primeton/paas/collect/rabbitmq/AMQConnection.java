/**
 * 
 */
package com.primeton.paas.collect.rabbitmq;

import com.rabbitmq.client.Connection;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-12-17
 *
 */
public class AMQConnection {
	
	private Connection connection;
	
	private String url;
	private String username;
	private String password;
	private String vhost;
	private int timeout;
	
	/**
	 * 
	 */
	public AMQConnection() {
		super();
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the vhost
	 */
	public String getVhost() {
		return vhost;
	}

	/**
	 * @param vhost the vhost to set
	 */
	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AMQConnection [connection=" + connection + ", url=" + url
				+ ", username=" + username + ", password=" + password
				+ ", vhost=" + vhost + ", timeout=" + timeout + "]";
	}
	

}
