/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.AppMonitorDao;
import com.primeton.paas.manage.api.impl.dao.DefaultAppMonitorDao;
import com.primeton.paas.manage.api.impl.util.SystemVariables;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-11-22
 *
 */
public class AppDataBuffer {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(AppDataBuffer.class);
	
	private static BlockingQueue<Map<String, Object>> buffer = new LinkedBlockingDeque<Map<String,Object>>();
	
	private static boolean flag = true;
	private static boolean isStarted = false;
	private static WriteData writeData = null;
	
	/**
	 * start <br>
	 */
	public synchronized static void start() {
		if (isStarted) {
			return;
		}
		flag = true;
		isStarted = true;
		
		writeData = new WriteData();
		Thread thread =  new Thread(writeData);
		thread.setName("WriteAppMonitorData");
		thread.setDaemon(true);
		thread.start();
	}
	
	/**
	 * stop <br>
	 */
	public synchronized static void stop() {
		if (isStarted && writeData != null) {
			flag = false;
			/*
			long timeout = 30000L;
			long begin = System.currentTimeMillis();
			long end = begin;
			
			while (true) {
				end = System.currentTimeMillis();
				if (end - begin > timeout) {
					break;
				}
				
				if (!writeData.isRunning()) {
					break;
				}
			}
			*/			
			isStarted = false;
			writeData = null;
		}
	}
	
	/**
	 * 
	 * @param data
	 */
	public static void put(Map<String, Object> data) {
		if (null == data) {
			return;
		}
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
	 * @author LiZhongwen
	 * @mail lizw@primeton.com
	 * @dept RD
	 * @date 2013-12-3
	 *
	 */
	private static class WriteData implements Runnable {
		
		private AppMonitorDao dao = new DefaultAppMonitorDao();
		private static ILogger logger = ManageLoggerFactory.getLogger(WriteData.class);
		
		//	private boolean isRunning = false;

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			//	isRunning = true;
			int time = SystemVariables.getKeepLastTimeData();
			int count = 0;
			while (flag) {
				try{
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
				} catch (Exception e) {
					logger.error(e.getMessage());
				} finally {
					if (++count == 100) {
						buffer.clear();
						long end = System.currentTimeMillis() - 1000L * 60L * time;
						long begin = 1L;
						dao.delete(begin, end);
						
						count = 0;
					}
				}
			}
			//	isRunning = false;
		}
		
		/*
		public boolean isRunning() {
			return isRunning;
		}
		*/
	}

}
