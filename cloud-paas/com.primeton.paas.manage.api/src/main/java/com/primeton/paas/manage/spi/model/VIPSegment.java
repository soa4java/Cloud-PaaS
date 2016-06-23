/**
 * 
 */
package com.primeton.paas.manage.spi.model;

import java.io.Serializable;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VIPSegment implements Serializable {
	
	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = 8451158625872253781L;
	
	public static final int STATUS_ENABLED = 1;
	public static final int STATUS_DISABLED = 0;

	private String id;
	private String name;
	private String begin;
	private String end;
	private String netmask;
	private String remarks;
	
	/**
	 * 
	 */
	public VIPSegment() {
		super();
	}

	/**
	 * 
	 * @param begin
	 * @param end
	 * @param netmask
	 */
	public VIPSegment(String begin, String end, String netmask) {
		super();
		this.begin = begin;
		this.end = end;
		this.netmask = netmask;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the begin
	 */
	public String getBegin() {
		return begin;
	}

	/**
	 * @param begin the begin to set
	 */
	public void setBegin(String begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * @return the netmask
	 */
	public String getNetmask() {
		return netmask;
	}

	/**
	 * @param netmask the netmask to set
	 */
	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer()
			.append(VIPSegment.class.getSimpleName())
			.append("{id=").append(id)
			.append(", begin=").append(begin)
			.append(", end=").append(end)
			.append(", netmask=").append(netmask)
			.append("}").toString();
	}
	
}
