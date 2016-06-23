/**
 * 
 */
package com.primeton.paas.cep.engine;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface Constants {
	
	String SERVER_HOME = "server_home";
	String GROUP_NAME = "group_name";
	String ENGINE_NAME = "engine_name";
	String RUN_MODE = "run_mode";
	String ENGINE_IP = "engine_ip";
	
	/*** (queue),(exchange),..., Example: queue1,exchange1,... **/
	String MQ_DESTS = "mq_dests";
	/*** (queue),(exchange), Example: queue,exchange, ... **/
	String MQ_TYPES = "mq_types";
	String MQ_TYPE_QUEUE = "queue";
	String MQ_TYPE_EXCHANGE = "exchange";
	
	String MQ_SERVER = "mq_server";
	
	String EVENT_NAME = "eventName";
	
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

	public static final String APP_NAME = "appName";
	
	public static final String CLUSTER_ID = "clusterId";

}
