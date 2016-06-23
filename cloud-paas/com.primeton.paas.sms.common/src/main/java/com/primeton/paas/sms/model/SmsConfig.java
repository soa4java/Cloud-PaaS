/**
 * 
 */
package com.primeton.paas.sms.model;

import java.io.Serializable;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public class SmsConfig implements Serializable {

	private static final long serialVersionUID = 5353890830503642298L;

	private String username;

	private String password;

	private String remoteAddr;

	private int remotePort;

	private String serviceCode;

	private long waitTime = 30000;

	private String extendCode;

	private String protocol = "mmpp2.0";

	private int pause = 0;

	private int registeredDelivery = 0;

	private int version = 0x20;

	private int socketTimeout = 2000;
	
	public SmsConfig() {
		super();
	}
	
	public String toString(){
		return "{username=" + username
			 + ", remoteAddr=" + remoteAddr
			 + ", remotePort="+ remotePort
			 + ", serviceCode=" + serviceCode
			 + ", extendCode=" + extendCode
			 + ", protocol=" + protocol
			 + ", registeredDelivery=" + registeredDelivery
			 + "}";
	}

	/**
	 * @return
	 */
	public String getExtendCode() {
		return extendCode;
	}

	/**
	 * @param extendCode
	 */
	public void setExtendCode(String extendCode) {
		this.extendCode = extendCode;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return
	 */
	public int getPause() {
		return pause;
	}

	/**
	 * @param pause
	 */
	public void setPause(int pause) {
		this.pause = pause;
	}

	/**
	 * @return
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return
	 */
	public int getRegisteredDelivery() {
		return registeredDelivery;
	}

	/**
	 * @param registeredDelivery
	 */
	public void setRegisteredDelivery(int registeredDelivery) {
		this.registeredDelivery = registeredDelivery;
	}

	/**
	 * @return
	 */
	public String getRemoteAddr() {
		return remoteAddr;
	}

	/**
	 * @param remoteAddr
	 */
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	/**
	 * @return
	 */
	public int getRemotePort() {
		return remotePort;
	}

	/**
	 * @param remotePort
	 */
	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	/**
	 * @return
	 */
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * @param serviceCode
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return
	 */
	public long getWaitTime() {
		return waitTime;
	}

	/**
	 * @param waitTime
	 */
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
	
	/**
	 * @return
	 */
	public int getVersion() {
		return version;
	}
	
	/**
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return
	 */
	public int getSocketTimeout() {
		return socketTimeout;
	}

	/**
	 * @param socketTimeout
	 */
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	
}
