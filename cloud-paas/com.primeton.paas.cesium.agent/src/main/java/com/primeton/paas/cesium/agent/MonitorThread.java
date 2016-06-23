/**
 * 
 */
package com.primeton.paas.cesium.agent;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.agent.api.RuntimeExec;
import org.gocom.cloud.cesium.agent.api.RuntimeExecFactory;
import org.gocom.cloud.cesium.agent.util.FileUtil;
import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.mqclient.api.MQClientFactory;
import org.gocom.cloud.cesium.mqclient.util.JsonSerializerUtil;
import org.gocom.cloud.cesium.mqclient.util.StringUtil;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class MonitorThread extends Thread {
	
	private static ILogger logger = LoggerFactory.getLogger(MonitorThread.class);

	/** prefix **/
	private static final String PREFIX = "PAAS.MONITOR.";
	private static final String EXCHANGE = PREFIX + "Exchange";
	private static final String INTERVAL = PREFIX + "Interval";
	private static final String MQSERVER = PREFIX + "MQServer";
	private static final String STATUS = PREFIX + "Status";
	private static final String MQSERVER_NAME = "monitor"; 
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private String agentHome;
	private String localhost;
	private String exchange;
	
	private boolean exitFlag = false;
	private boolean isRunning;
	
	private String sh;
	private String cpuPath;
	private String memPath;
	private String loadPath;
	private String ioPath;
	private String stoPath;
	
	private long interval = 60000L; // 60s
	private boolean isEnable = false;
	
	private List<Execute> executes = new ArrayList<Execute>();
	
	/**
	 * @param agentHome
	 * @param localhost
	 */
	public MonitorThread(String agentHome, String localhost) {
		super();
		this.agentHome = agentHome;
		this.localhost = localhost;
	}
	
	/**
	 * init. <br>
	 */
	private void init() {
		// rabbitmq_server address
		String rabbitmq = VariableUtil.getValue(MQSERVER, null);
		if (rabbitmq != null && rabbitmq.trim().length() > 0) {
			try {
				@SuppressWarnings("unchecked")
				Map<String, String> args = (Map<String, String>)JsonSerializerUtil.deserialize(rabbitmq);
				if (args != null) {
					MQClientFactory.createMQClient(MQSERVER_NAME, args);
				}
			} catch (Throwable t) {
				logger.error(t);
			}
		}
		
		// message exchange destination
		exchange = VariableUtil.getValue(EXCHANGE, "agentMonitorExchange"); //$NON-NLS-1$
		try {
			MQClientFactory.getMQClient(MQSERVER_NAME).createExchange(exchange, "fanout", false); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Throwable t) {
			logger.error(t);
		}
		
		interval = VariableUtil.getValue(INTERVAL, 5000L);
		isEnable = VariableUtil.getValue(STATUS, false);
	}
	
	public void run() {
		// init variables
		init();
		
		if (isEnable) {
			logger.info("Monitoring system has been enabled.");
		} else {
			logger.info("Monitoring system has been disabled.");
			// return;
		}
		if (!FileUtil.existDirectory(agentHome)) {
			logger.warn(agentHome + " not found.");
			return;
		}
		sh = agentHome + File.separator + "bin" + File.separator + "monitor.sh";
		if (!FileUtil.existFile(sh)) {
			logger.warn("File " + sh + " not found.");
			return;
		}
		
		Execute cpuExec = new Execute(new String[]{sh, "Cpu"}, interval);
		Execute memExec = new Execute(new String[]{sh, "Mem"}, interval);
		Execute loadExec = new Execute(new String[]{sh, "Load"}, interval);
		Execute ioExec = new Execute(new String[]{sh, "IO"}, interval);
		Execute stoExec = new Execute(new String[]{sh, "Sto"}, interval);
		
		executes.add(cpuExec);
		executes.add(memExec);
		executes.add(loadExec);
		executes.add(ioExec);
		executes.add(stoExec);
		
		Thread t1 = new Thread(cpuExec);
		Thread t2 = new Thread(memExec);
		Thread t3 = new Thread(loadExec);
		Thread t4 = new Thread(ioExec);
		Thread t5 = new Thread(stoExec);
		
		t1.setDaemon(true);
		t2.setDaemon(true);
		t3.setDaemon(true);
		t4.setDaemon(true);
		t5.setDaemon(true);
		
		t1.setName("Exec-Monitor-Cpu");
		t2.setName("Exec-Monitor-Mem");
		t3.setName("Exec-Monitor-Load");
		t4.setName("Exec-Monitor-IO");
		t5.setName("Exec-Monitor-Sto");
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		
		
		cpuPath = agentHome + File.separator + "work" + File.separator + "host" + File.separator + "CPU";
		memPath = agentHome + File.separator + "work" + File.separator + "host" + File.separator + "MEM";
		loadPath = agentHome + File.separator + "work" + File.separator + "host" + File.separator + "LOAD";
		ioPath = agentHome + File.separator + "work" + File.separator + "host" + File.separator + "IO";
		stoPath = agentHome + File.separator + "work" + File.separator + "host" + File.separator + "STO";
		
		this.isRunning = true;
		
		int count = 0;
		while (true) {
			if (exitFlag) {
				break;
			}
			// if enable monitor then send data
			if (isEnable) {
				try {
					Map<String, Object> data = readOut();
					if (logger.isDebugEnabled()) {
						logger.debug(data);
					}
					send(data);
				} catch (Throwable t) {
					logger.error(t);
				}
			} else {
				logger.debug("Monitoring system has been disabled.");
			}
			
			if (++count == 50) {
				isEnable = VariableUtil.getValue(STATUS, false);
				count = 0;
				cpuExec.setExec(isEnable);
				memExec.setExec(isEnable);
				loadExec.setExec(isEnable);
				ioExec.setExec(isEnable);
				stoExec.setExec(isEnable);
			}
			
			ThreadUtil.sleep(interval);
		}
		
		// close
		for (Execute exec : executes) {
			exec.close();
		}
		this.isRunning = false;
	}
	
	/**
	 * Read data <br>
	 * 
	 * @return
	 */
	private Map<String, Object> readOut() {
		String cpuContent = FileUtil.read(cpuPath);
		String memContent = FileUtil.read(memPath);
		String loadContent = FileUtil.read(loadPath);
		String ioContent = FileUtil.read(ioPath);
		String stoContent = FileUtil.read(stoPath);
		return analysis(cpuContent, memContent, loadContent, ioContent,stoContent);
	}
	
	/**
	 * send to message queue <br>
	 * 
	 * @param data
	 */
	private void send(Map<String, Object> data) {
		if (data != null && !data.isEmpty()) {
			MonitorMessage message = new MonitorMessage(data);
			try {
				MQClientFactory.getMQClient(MQSERVER_NAME).sendMessage(exchange, "", message, false);
			} catch (Throwable t) {
				logger.error(t);
			}
		}
	}

	/**
	 * @return the isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	public void close() {
		this.exitFlag = true;
	}

	// Cpu(s):  0.0%us,  0.2%sy,  0.0%ni, 99.6%id,  0.2%wa,  0.0%hi,  0.0%si,  0.0%st
	// Mem:   4155904k total,  4106820k used,    49084k free,   216640k buffers
	// top - 22:05:08 up 25 days, 12:31,  3 users,  load average: 0.00, 0.01, 0.05
	// procs -----------memory---------- ---swap-- -----io---- -system-- -----cpu------ r b swpd free buff cache si so bi bo in cs us sy id wa st 0 0 1968 14404 199968 3399520 0 0 5 88 0 0 0 0 100 0 0
	
	private Map<String, Object> analysis(String cpu, String mem, String load, String io, String sto) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (cpu == null || cpu.trim().length() == 0
				|| mem == null || mem.trim().length() == 0
				|| load == null || load.trim().length() == 0
				|| io == null || io.trim().length() == 0
				|| ! cpu.startsWith("Cpu")
				|| ! mem.startsWith("Mem")
				|| ! load.startsWith("top")
				|| ! io.startsWith("procs")
				) {
			return data;
		}
		
		if (io.endsWith(LINE_SEPARATOR)) {
			io.replace(LINE_SEPARATOR, "");
		}
		
		// last line
		cpu = cpu.substring(cpu.lastIndexOf(LINE_SEPARATOR) + LINE_SEPARATOR.length());
		
		// CPU
		String[] cpuItems = cpu.split(":")[1].split(",");
		for (String item : cpuItems) {
			String[] array = item.trim().split("%");
			String key = "cpu_" + array[1].trim();
			double value = Double.parseDouble(array[0].trim());
			data.put(key, value);
		}
		
		// MEM (KB)
		String[] memItems = mem.split(":")[1].split(",");
		for (String item : memItems) {
			String[] array = item.trim().split("k");
			String key = "mem_" + array[1].trim();
			long value = Long.parseLong(array[0].trim());
			data.put(key, value);
		}
		
		double memTotal = (Long)data.get(MonitorMessage.MEM_TOTAL);
		double memUsed = (Long)data.get(MonitorMessage.MEM_USED);
		double mem_us = memUsed * 100 / memTotal;
		BigDecimal decimal = new BigDecimal(mem_us);
		mem_us = decimal.setScale(1, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		
		data.put(MonitorMessage.MEM_US, mem_us); // memory used percentage
		
		// CPU LOAD
		String[] loadItems = load.split(",");
		double oneLoad = Double.parseDouble(loadItems[loadItems.length - 3].split(":")[1].trim());
		double fiveLoad = Double.parseDouble(loadItems[loadItems.length - 2].trim());
		double fifteenLoad = Double.parseDouble(loadItems[loadItems.length - 1].trim());
		decimal = new BigDecimal(oneLoad);
		data.put(MonitorMessage.CPU_ONELOAD, decimal.setScale(2, BigDecimal.ROUND_HALF_DOWN));
		decimal = new BigDecimal(fiveLoad);
		data.put(MonitorMessage.CPU_FIVELOAD, decimal.setScale(2, BigDecimal.ROUND_HALF_DOWN));
		decimal = new BigDecimal(fifteenLoad);
		data.put(MonitorMessage.CPU_FIFTEENLOAD, decimal.setScale(2, BigDecimal.ROUND_HALF_DOWN));
		
		// IO (KB)
		String[] ioItems = io.split(" ");
		long io_si = Long.parseLong(ioItems[ioItems.length - 11].trim());
		long io_so = Long.parseLong(ioItems[ioItems.length - 10].trim());
		long io_bi = Long.parseLong(ioItems[ioItems.length - 9].trim());
		long io_bo = Long.parseLong(ioItems[ioItems.length - 8].trim());
		data.put(MonitorMessage.IO_SI, io_si);
		data.put(MonitorMessage.IO_SO, io_so);
		data.put(MonitorMessage.IO_BI, io_bi);
		data.put(MonitorMessage.IO_BO, io_bo);

		// STO
		if (StringUtil.isNotEmpty(sto) && sto.startsWith("/dev/hda")) { //$NON-NLS-1$
			try {
				String[] stoItems = sto.split(LINE_SEPARATOR);
				if (stoItems != null && stoItems.length > 0) {
					String stoFilesystemTemp = "";
					String stoSizeTemp = "";
					String stoUsedTemp = "";
					String stoAvailTemp = "";
					String stoUseTemp = "";
					String stoMountedTemp = "";
					for (String item : stoItems) {
						String[] iData = item.replaceAll("\\s+", ",").split(",");
						stoFilesystemTemp += iData[0]+";";
						stoSizeTemp += iData[1] + ";";
						stoUsedTemp += iData[2] + ";";
						stoAvailTemp += iData[3] + ";";
						stoUseTemp += iData[4] + ";";
						stoMountedTemp += iData[5] + ";";
					}
					if (stoFilesystemTemp.length() > 1) 
						stoFilesystemTemp = stoFilesystemTemp.substring(0, stoFilesystemTemp.length()-1);
					
					if (stoSizeTemp.length() > 1) 
						stoSizeTemp = stoSizeTemp.substring(0, stoSizeTemp.length()-1);
					
					if (stoUsedTemp.length() > 1) 
						stoUsedTemp = stoUsedTemp.substring(0, stoUsedTemp.length()-1);
					
					if (stoAvailTemp.length() > 1) 
						stoAvailTemp = stoAvailTemp.substring(0, stoAvailTemp.length()-1);
					
					if (stoUseTemp.length() > 1) 
						stoUseTemp = stoUseTemp.substring(0, stoUseTemp.length()-1);
					
					if (stoMountedTemp.length() > 1) 
						stoMountedTemp = stoMountedTemp.substring(0, stoMountedTemp.length()-1);
					
					data.put(MonitorMessage.STO_FILESYSTEM, stoFilesystemTemp);
					data.put(MonitorMessage.STO_SIZE, stoSizeTemp);
					data.put(MonitorMessage.STO_USED, stoUsedTemp);
					data.put(MonitorMessage.STO_AVAIL, stoAvailTemp);
					data.put(MonitorMessage.STO_USE, stoUseTemp);
					data.put(MonitorMessage.STO_MOUNTED, stoMountedTemp);
				} else {
					data.put(MonitorMessage.STO_FILESYSTEM, 0); //$NON-NLS-1$
					data.put(MonitorMessage.STO_SIZE, 0); //$NON-NLS-1$
					data.put(MonitorMessage.STO_USED, 0); //$NON-NLS-1$
					data.put(MonitorMessage.STO_AVAIL, 0); //$NON-NLS-1$
					data.put(MonitorMessage.STO_USE, 0); //$NON-NLS-1$
					data.put(MonitorMessage.STO_MOUNTED, 0); //$NON-NLS-1$
				}
			} catch (Throwable t) {
				logger.error(t.getMessage());
			}
		}
		
		// Network
		
		// IP, Time
		data.put(MonitorMessage.IP, localhost);
		data.put(MonitorMessage.OCCUR_TIME, System.currentTimeMillis());
		
		return data;
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	private static class Execute implements Runnable {
		
		private static ILogger log = LoggerFactory.getLogger(Execute.class);
		
		private String[] cmd;
		private long interval;
		
		private boolean flag = true;
		private boolean exec = true;
		
		/**
		 * @param cmd
		 * @param interval
		 */
		public Execute(String[] cmd, long interval) {
			super();
			this.cmd = cmd;
			this.interval = interval;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			interval = interval > 0 ? interval : 1000L;
			RuntimeExec exec = RuntimeExecFactory.getRuntimeExec();
			while (flag) {
				if (this.exec) {
					try {
						exec.execute(cmd);
					} catch (Throwable t) {
						log.error(t);
					}
				}
				ThreadUtil.sleep(interval);
			}
		}
		
		/**
		 * @param exec the exec to set
		 */
		public void setExec(boolean exec) {
			this.exec = exec;
		}

		/**
		 * 
		 */
		public void close() {
			flag = false;
		}
		
	}
	
}
