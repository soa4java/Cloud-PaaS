/**
 * 
 */
package com.primeton.paas.cep.engine;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class Engine {
	
	public static final String MODE_MASTER = "master";
	public static final String MODE_SLAVE = "slave";
	
	private String name;
	private String mode = MODE_SLAVE;
	private String ip = "127.0.0.1";
	private long time = System.currentTimeMillis();
	
	public Engine() {
		super();
	}

	public Engine(String name, String mode) {
		super();
		this.name = name;
		this.mode = mode;
	}

	public Engine(String name, String mode, String ip) {
		super();
		this.name = name;
		this.mode = mode;
		this.ip = ip;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Engine [name=" + name + ", mode=" + mode + ", ip=" + ip
				+ ", time=" + time + "]";
	}

}
