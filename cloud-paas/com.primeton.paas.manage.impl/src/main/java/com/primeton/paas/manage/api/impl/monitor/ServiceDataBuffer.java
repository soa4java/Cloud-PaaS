/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultServiceMonitorDao;
import com.primeton.paas.manage.api.impl.dao.ServiceMonitorDao;
import com.primeton.paas.manage.api.impl.util.SystemVariables;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class ServiceDataBuffer {

	private static ILogger logger = ManageLoggerFactory.getLogger(ServiceDataBuffer.class);
	
	private static BlockingQueue<Map<String, Object>> buffer = new LinkedBlockingDeque<Map<String,Object>>();
	
	private static boolean flag = true;
	private static boolean isStarted = false;
	private static WriteServiceData writeData = null;
	
	/**
	 * start <br>
	 */
	public synchronized static void start() {
		if (isStarted) {
			return;
		}
		flag = true;
		isStarted = true;
		
		writeData = new WriteServiceData();
		Thread thread =  new Thread(writeData);
		thread.setName("WriteServiceMonitorData");
		thread.setDaemon(true);
		thread.start();
	}
	
	/**
	 * stop <br>
	 */
	public synchronized static void stop() {
		if (isStarted && writeData != null) {
			flag = false;
			isStarted = false;
			writeData = null;
		}
	}
	
	/**
	 * 
	 * @param data
	 */
	public static void put(Map<String, Object> data) {
		try {
			buffer.put(data);
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}
	
	/**
	 * 
	 * @param size
	 * @param timeout
	 * @return
	 */
	public static List<Map<String, Object>> take(int size, long timeout) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		long begin = System.currentTimeMillis();
		long end = begin;
		
		while (true) {
			end = System.currentTimeMillis();
			if (end - begin >= timeout) {
				break;
			}
			
			if (buffer.isEmpty()) {
				ThreadUtil.sleep(1000L);
			} else {
				try {
					Map<String, Object> e = buffer.take();
					if (e != null && !e.isEmpty()) {
						list.add(e);
					}
				} catch (InterruptedException e) {
					logger.error(e);
				}
			}
			if (list.size() >= size) {
				break;
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	private static class WriteServiceData implements Runnable{
		
		private ServiceMonitorDao dao = new DefaultServiceMonitorDao();
		
		private static ILogger logger = ManageLoggerFactory.getLogger(WriteServiceData.class);
		
		public void run() {
			int time = SystemVariables.getKeepLastTimeData();
			int count = 0;
			while (flag) {
				List<Map<String, Object>> list = take(1000, 5000L);
				if (list.isEmpty()) {
					ThreadUtil.sleep(2000L);
				} else {
					try {
						dao.addBatch(list);
						list.clear();
					} catch (Throwable t) {
						logger.error(t.getMessage());
					}
				}
				
				if (++count == 100) {
					long end = System.currentTimeMillis() - 1000L * 60L * time;
					long begin = 1L;
					int dataCount = dao.delete(begin, end);
					logger.info("Have deleted service monitor data before " + new Date(end) + " ,counts:" + dataCount);
					count = 0;
				}
			}
		}
		
	}
	
}
