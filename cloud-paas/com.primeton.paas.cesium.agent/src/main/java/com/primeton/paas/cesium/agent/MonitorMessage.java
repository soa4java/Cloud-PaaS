/**
 * 
 */
package com.primeton.paas.cesium.agent;

import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.AbstractMessage;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class MonitorMessage extends AbstractMessage <Map<String, Object>> {
	
	// CPU [ top -bcisSH -n 1 | grep Cpu ]
	public static final String CPU_US = "cpu_us";
	public static final String CPU_SY = "cpu_sy";
	public static final String CPU_NI = "cpu_ni";
	public static final String CPU_ID = "cpu_id";
	public static final String CPU_WA = "cpu_wa";
	public static final String CPU_HI = "cpu_hi";
	public static final String CPU_SI = "cpu_si";
	public static final String CPU_ST = "cpu_st";
	
	// Mem [ top -bcisSH -n 1 | grep Mem ]
	public static final String MEM_TOTAL = "mem_total";
	public static final String MEM_USED = "mem_used";
	public static final String MEM_FREE = "mem_free";
	public static final String MEM_BUFFERS = "mem_buffers";
	
	// CPU Load [ top -bcisSH -n 1 | grep 'load average:' ]
	public static final String CPU_ONELOAD = "cpu_oneload";
	public static final String CPU_FIVELOAD = "cpu_fiveload";
	public static final String CPU_FIFTEENLOAD = "cpu_fifteenload";
	
	// others
	public static final String MEM_US = "mem_us"; // %
	public static final String IP = "ip";
	public static final String OCCUR_TIME = "occur_time";
	
	// IO [ vmstat -S K ]
	public static final String IO_SI = "io_si";
	public static final String IO_SO = "io_so";
	public static final String IO_BI = "io_bi";
	public static final String IO_BO = "io_bo";
	
	// STO
	public static final String STO_FILESYSTEM = "sto_filesystem";
	public static final String STO_SIZE = "sto_size";
	public static final String STO_USED = "sto_used";
	public static final String STO_AVAIL = "sto_avail";
	public static final String STO_USE = "sto_use";
	public static final String STO_MOUNTED  = "sto_mounted";
	

	private Map<String, Object> body;
	
	/**
	 * 
	 */
	public MonitorMessage() {
		super();
	}

	/**
	 * @param body
	 */
	public MonitorMessage(Map<String, Object> body) {
		super();
		this.body = body;
	}

	/* (non-Javadoc)
	 * @see org.gocom.cloud.cesium.mqclient.api.Message#getBody()
	 */
	public Map<String, Object> getBody() {
		return body;
	}

	/* (non-Javadoc)
	 * @see org.gocom.cloud.cesium.mqclient.api.Message#setBody(java.lang.Object)
	 */
	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer(super.toString())
			.append("-header:" + getHeaders())
			.append("-body:" + getBody())
			.toString();
	}

}
