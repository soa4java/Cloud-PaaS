/**
 * 
 */
package com.primeton.paas.app.log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.primeton.paas.app.ServerContext;
import com.primeton.paas.collect.common.JsonSerializeUtil;
import com.primeton.paas.collect.common.LogMetaData;
import com.primeton.paas.collect.common.StringUtil;
import com.primeton.paas.collect.rabbitmq.AMQConnectionFactory;
import com.primeton.paas.collect.rabbitmq.AMQUtil;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-12-20
 *
 */
public class LogBuffer {
	
	//	private static BlockingQueue<String> userLogCache = new LinkedBlockingQueue<String>();
	private static BlockingQueue<UserLogData> userLogDataCache = new LinkedBlockingQueue<UserLogData>();
	private static BlockingQueue<String> systemLogCache = new LinkedBlockingQueue<String>();
	
	private static boolean isStarted;
	private static Transfer userTransfer = new Transfer(LogMetaData.TYPE_APP);
	private static Transfer systemTransfer = new Transfer(LogMetaData.TYPE_SYSTEM);
	
	/**
	 * 
	 */
	public static synchronized void start() {
		if (isStarted) {
			return;
		}
		
		Thread system = new Thread(systemTransfer);
		system.setDaemon(true);
		system.setName("TransferSystemLogThread");
		system.start();
		
		Thread user = new Thread(userTransfer);
		user.setDaemon(true);
		user.setName("TransferUserLogThread");
		user.start();
		
		isStarted = true;
	}
	
	/**
	 * 
	 */
	public static synchronized void stop() {
		if (isStarted) {
			userTransfer.close();
			systemTransfer.close();
			isStarted = false;
		}
	}
	
	/**
	 * 
	 * @param timeout
	 */
	public static synchronized void stop(long timeout) {
		if (isStarted) {
			long begin = System.currentTimeMillis();
			long end = begin;
			
			userTransfer.close();
			systemTransfer.close();
			
			while (true) {
				end = System.currentTimeMillis();
				if (end - begin > timeout) {
					break;
				}
				if (userTransfer.isRunning() || systemTransfer.isRunning()) {
					continue;
				} else {
					break;
				}
			}
		}
		isStarted = false;
	}
	
	/**
	 * cache log to buffer <br>
	 * 
	 * @param userLog
	 */
	/*
	public static void cacheUserLog(String userLog) {
		if (isStarted) {
			start();
		}
		if (StringUtil.isNotEmpty(userLog)) {
			try {
				userLogCache.put(userLog);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	*/

	/**
	 * cache userlogdata to buffer
	 * @param userLog
	 */
	public static void cacheUserLog(UserLogData userLog) {
		if (isStarted) {
			start();
		}
		if(userLog!=null&&userLog.getType()!=null){
			try{
				userLogDataCache.put(userLog);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * cache log to buffer <br>
	 * 
	 * @param systemLog
	 */
	public static void cacheSystemLog(String systemLog) {
		if (isStarted) {
			start();
		}
		if (StringUtil.isNotEmpty(systemLog)) {
			try {
				systemLogCache.put(systemLog);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 
	 * @param number
	 * @param timeout
	 * @param type user/system
	 * @return
	 */
	private static LogMetaData take(int number, long timeout, String type) {
		BlockingQueue<String> cache = null;
		if (LogMetaData.TYPE_APP.equals(type)) {
			//	cache = userLogCache;
			return takeUserLogData(timeout);
		} else if (LogMetaData.TYPE_SYSTEM.equals(type)) {
			cache = systemLogCache;
		} else {
			return null;
		}
		
		long begin = System.currentTimeMillis();
		long end = begin;
		
		StringBuffer stringBuffer = new StringBuffer();
		int count = 0;
		while (true) {
			
			// timeout
			end = System.currentTimeMillis();
			if (end - begin > timeout) {
				break;
			}
			
			// wait
			if (cache.isEmpty()) {
				ThreadUtil.sleep(100L);
				continue;
			}
			
			try {
				String log = cache.take();
				if (log != null) {
					stringBuffer.append(log);
					count ++;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// ok
			if (count >= number) {
				break;
			}
		}
		
		// get system info
		ServerContext context = ServerContext.getInstance();
		
		if (stringBuffer.length() > 0) {
			LogMetaData metaData = new LogMetaData();
			metaData.setAppName(context.getAppName());
			metaData.setContent(stringBuffer.toString());
			metaData.setInstance(context.getInstId());
			metaData.setTime(System.currentTimeMillis());
			metaData.setType(type);
			return metaData;
		}
		
		return null;
	}
	
	/**
	 * @param timeout
	 * @return
	 */
	private static LogMetaData takeUserLogData(long timeout){
		BlockingQueue<UserLogData> cache = userLogDataCache;
		
		long begin = System.currentTimeMillis();
		long end = begin;
		UserLogData log = null;
		
		while (true) {
			// timeout
			end = System.currentTimeMillis();
			if (end - begin > timeout) {
				break;
			}
			
			if (cache.isEmpty()) {
				ThreadUtil.sleep(100L);
				continue;
			}
		
			try {
				log = cache.take();
				if(log!=null){
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// get system info
		ServerContext context = ServerContext.getInstance();
		
		if (log != null) {
			LogMetaData metaData = new LogMetaData();
			metaData.setAppName(context.getAppName());
			metaData.setContent(log.getMessage().toString());
			metaData.setInstance(context.getInstId());
			metaData.setTime(System.currentTimeMillis());
			metaData.setType(log.getType());
			return metaData;
		}
		
		return null;
	}
	
	/**
	 * 
	 * @author LiZhongwen
	 * @mail lizw@primeton.com
	 * @dept RD
	 * @date 2013-12-20
	 *
	 */
	private static class Transfer implements Runnable {
		
		private boolean isRunning = false;
		private boolean flag = true;
		private String type;
		
		/**
		 * @param type
		 */
		public Transfer(String type) {
			super();
			this.type = type;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			
			LogCollectContext context = LogCollectContext.getContext();
			String queues[] = context.getQueues();
			if (queues == null || queues.length == 0) {
				return;
			}
			
			String queue = null;
			int index = (int)(Math.random() * queues.length);
			queue = queues[index];
			
			isRunning = true;
			// 
			while (flag) {
				LogMetaData metaData = take(1000, 2000L, type);
				if (metaData == null) {
					ThreadUtil.sleep(2000L);
					continue;
				}
				Channel channel = null;
				try {
					String json = JsonSerializeUtil.serialize(metaData);
					Connection conn = AMQConnectionFactory.getConnection();
					channel = conn.createChannel();
					try {
						channel.queueDeclarePassive(queue);
					} catch (Throwable e) {
						channel = conn.createChannel();
						channel.queueDeclare(queue, false, false, false, null);
					}
					channel.basicPublish("", queue, MessageProperties.TEXT_PLAIN, StringUtil.toBytes(json));
				} catch (Throwable t) {
					t.printStackTrace();
				} finally {
					AMQUtil.close(channel);
				}
			}
			
			isRunning = false;
		}
		
		public boolean isRunning() {
			return isRunning;
		}
		
		public void close() {
			flag = false;
		}
		
	}

}
