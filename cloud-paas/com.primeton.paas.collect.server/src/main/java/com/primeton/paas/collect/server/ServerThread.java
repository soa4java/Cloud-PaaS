/**
 * 
 */
package com.primeton.paas.collect.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.manage.runtime.api.model.MQServer;
import org.gocom.cloud.cesium.zkclient.api.ZkChildListener;
import org.gocom.cloud.cesium.zkclient.api.ZkClient;
import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.collect.rabbitmq.AMQConnectionFactory;
import com.primeton.paas.collect.rabbitmq.AMQUtil;
import com.primeton.paas.collect.rabbitmq.MessageConsumer;
import com.primeton.paas.collect.spi.StartupListener;
import com.primeton.paas.collect.spi.StartupListenerManager;
import com.primeton.paas.collect.util.PathUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-12-17
 *
 */
public class ServerThread extends Thread implements Constants {
	
	private static ILogger logger = LoggerFactory.getLogger(ServerThread.class);
	
	private static final long HEART_BEAT = 2000L;	// 2s
	
	private static ServerContext context = ServerContext.getContext();
	private ZkClient zkClient = ZkClientFactory.getZkClient();
	
	private boolean isRunning = false;
	private boolean flag = true;
	
	private boolean isSubscribe = false;
	
	private Map<String, MessageConsumer> messageConsumers = new ConcurrentHashMap<String, MessageConsumer>();
	private Map<String, String> exchangeBindings = new ConcurrentHashMap<String, String>();
	
	private Server server = new Server();

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		logger.info("\n========================================================================"
				+ "\n======================== Server startup success. ======================="
				+ "\n========================================================================\n");
		
		isRunning = true;
		// init amqp connection
		init();
		
		// start log buffer
		LogBuffer.start();
		
		// register
		register();
		
		// For user extends
		StartupListenerManager.start();
		
		int count0 = 0;
		int count1 = 0;
		// 
		while (flag) {
			
			if (Server.MODE_SLAVE.equals(context.getRunMode())) { // I am not master now
				// Find master server
				List<Server> servers = PathUtil.getServers();
				Server master = null;
				for (Server e : servers) {
					if (Server.MODE_MASTER.equals(e.getMode())) {
						master = e;
						break;
					}
				}
				
				// Change me to master mode
				if (master == null) {
					this.server.setMode(Server.MODE_MASTER);
					this.server.setTime(System.currentTimeMillis());
					context.setRunMode(Server.MODE_MASTER);
					PathUtil.setServer(this.server);
					logger.info("\n========================================================================"
							+ "\n======================== Run mode change to master. ===================="
							+ "\n========================================================================\n");
				}
			} else { // I am is master now
				if (++count1 == 5) {
					List<Server> servers = PathUtil.getServers();
					long time = this.server.getTime();
					for (Server e : servers) {
						if (e != null && !this.server.getName().equals(e.getName()) 
								&& Server.MODE_MASTER.equals(e.getMode())
								&& e.getTime() <= time) {
							this.server.setMode(Server.MODE_SLAVE);
							context.setRunMode(Server.MODE_SLAVE);
							PathUtil.setServer(this.server);
							logger.info("\n========================================================================"
									+ "\n======================== Run mode change to slave. ====================="
									+ "\n========================================================================\n");
							break;
						}
					}
					count1 = 0;
				}
			}
			
			// if run as master then subscribe else not subscribe
			if (Server.MODE_MASTER.equals(context.getRunMode()) && (!isSubscribe)) {
				subscribe();
			} else if (Server.MODE_SLAVE.equals(context.getRunMode()) && isSubscribe) {
				unsubscribe();
			}
			
			// if my node not found on zookeeper then create it
			if (++count0 == 30) { // 30 * HEART_BEAT
				Server self = PathUtil.getServer();
				if (self == null) {
					PathUtil.setServer(this.server);
				}
				count0 = 0;
			}
			
			// Sorry, I would like to take a break
			ThreadUtil.sleep(HEART_BEAT);
		}
		
		long begin = System.currentTimeMillis();
		logger.info("Begin close all " + StartupListener.class.getSimpleName() + ".");
		StartupListenerManager.stop();
		long end = System.currentTimeMillis();
		logger.info("End close all " + StartupListener.class.getSimpleName() + ", time spent " + (end-begin)/1000L + " seconds.");

		if (isSubscribe) {
			begin = System.currentTimeMillis();
			logger.info("Begin unsubscribe all message queues.");
			unsubscribe();
			end = System.currentTimeMillis();
			logger.info("End unsubscribe all message queues, time spent " + (end-begin)/1000L + " seconds.");
		}		
		
		begin = System.currentTimeMillis();
		logger.info("Waiting LogBuffer clear.");
		LogBuffer.stop(1000L);
		end = System.currentTimeMillis();
		logger.info("LogBuffer cleared, time spent " + (end-begin)/1000L + " seconds.");
		
		begin = System.currentTimeMillis();
		logger.info("Begin unregister Server.");
		unregister();
		end = System.currentTimeMillis();
		logger.info("End unregister Server, time spent " + (end-begin)/1000L + " seconds.");

		
		logger.info("\n========================================================================"
				+ "\n======================== Server shut down success. ====================="
				+ "\n========================================================================\n");
		isRunning = false;
	}
	
	private void init() {
		ServerContext context = ServerContext.getContext();
		MQServer mqServer = context.getMQServer();
		if (mqServer == null) {
			throw new RuntimeException("RabbitMQ connection parameter not found on zookeeper.");
		}
		try {
			AMQConnectionFactory.getConnection(AMQConnectionFactory.DEFAULT, true, mqServer.getUrl(), mqServer.getUserName(), 
					mqServer.getPassword(), mqServer.getVhost(), mqServer.getConnectionTimeout());
			logger.info("Init AMQConnection success.");
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	private void register() {
		
		// Cloud/UPAAS/Collector/group1
		String groupPath = PathUtil.getGroupPath();
		if (!zkClient.exists(groupPath)) {
			zkClient.createPersistent(groupPath, true);
		}
		
		// Find master server
		List<Server> servers = PathUtil.getServers();
		Server master = null;
		for (Server server : servers) {
			if (Server.MODE_MASTER.equals(server.getMode())) {
				master = server;
				break;
			}
		}
		
		// Register server
		String runMode = master == null ? Server.MODE_MASTER : Server.MODE_SLAVE;
				
		this.server.setIp(context.getServerIp());
		this.server.setName(context.getServerName());
		this.server.setMode(runMode);
		this.server.setTime(System.currentTimeMillis());
		context.setRunMode(runMode);
		PathUtil.setServer(this.server);
		logger.info("Register " + this.server + " as " + runMode + " node.");
		
		// 
		zkClient.subscribeChildChanges(PathUtil.getGroupPath(), new ZkChildListener() {
			
			public void handleChildChange(String parentPath, List<String> currentChilds)
					throws Exception {
				logger.info("[" + parentPath + "] server instances " + currentChilds + ".");
			}
		});
		logger.info("Register server success.");
	}
	
	
	private void unregister() {
		PathUtil.deleteServer(context.getServerName());
		logger.info("Unregister server success.");
	}
	
	/**
	 * 
	 */
	private void subscribe() {
		if (isSubscribe) {
			return;
		}
		List<String> dests = context.getMQDests();
		List<String> types = context.getMQTypes();
		
		if (dests == null || types == null || dests.size() != types.size()) {
			logger.warn("Message destination configuration error, check JVM parameter -Dmq_dests -Dmq_types");
			return;
		}
		
		for (int i=0; i<dests.size(); i++) {
			String type = types.get(i);
			String dest = dests.get(i);
			if (MQ_TYPE_EXCHANGE.equals(type)) {
				subscribeExchange(dest);
			} else if (MQ_TYPE_QUEUE.equals(type)) {
				subscribeQueue(dest);
			}
		}
		logger.info("Subscribe message queue success.");
		isSubscribe = true;
	}
	
	/**
	 * Temp queue binding exchange and subscribe temp queue <br>
	 *  
	 * @param exchange
	 */
	private void subscribeExchange(String exchange) {
		// new exchange if not exists
		String queue = UUID.randomUUID().toString();
		Channel channel = null;
		try {
			Connection conn = AMQConnectionFactory.getConnection();
			channel = conn.createChannel();
			channel.queueDeclare(queue, false, false, false, null);
			logger.info("New message queue [" + queue + "] success.");
			channel.exchangeDeclare(exchange, "fanout");
			logger.info("New message exchange [" + exchange + "] success.");
			channel.queueBind(queue, exchange, "");
			logger.info("Message queue [" + queue + "] bind exchange [" + exchange + "] success.");
			
			exchangeBindings.put(exchange, queue);
		} catch (Throwable t) {
			logger.error(t);
		} finally {
			AMQUtil.close(channel);
		}
		
		subscribeQueue(queue);
	}
	
	/**
	 * subscribe queue <br>
	 * 
	 * @param queue
	 */
	private void subscribeQueue(String queue) {
		// new queue if not exists
		Channel channel = null;
		try {
			Connection conn = AMQConnectionFactory.getConnection();
			channel = conn.createChannel();
			try {
				channel.queueDeclarePassive(queue);
			} catch (Throwable t) {
				channel = conn.createChannel();
				channel.queueDeclare(queue, false, false, false, null);
				logger.info("New message queue [" + queue + "] success.");
			}
		} catch (Throwable t) {
			logger.error(t);
		} finally {
			AMQUtil.close(channel);
		}
		
		LogMessageConsumer consumer = new LogMessageConsumer(AMQConnectionFactory.DEFAULT, queue, 1000L);
		messageConsumers.put(queue, consumer);
		consumer.open();
		logger.info("Open message consumer " + consumer + " success.");
	}
	
	/**
	 * 
	 */
	private void unsubscribe() {
		
		Channel channel = null;
		try {
			Connection conn = AMQConnectionFactory.getConnection();
			channel = conn.createChannel();
			
			// Close consumer
			for (MessageConsumer consumer : messageConsumers.values()) {
				consumer.close();
			}
			messageConsumers.clear();
			
			// unbind && destroy - temp queue
			for (String exchange : exchangeBindings.keySet()) {
				try {
					if (!channel.isOpen()) {
						AMQUtil.close(channel);
						channel = conn.createChannel();
					}
					String queue = exchangeBindings.get(exchange);
					channel.queueUnbind(queue, exchange, "");
					channel.queueDelete(queue);
				} catch (IOException e) {
					logger.error(e);
				}
			}
		} catch (Throwable t) {
			logger.error(t);
		} finally {
			AMQUtil.close(channel);
		}
		logger.info("Unsubscribe message queue success.");
		isSubscribe = false;
	}
	
	/**
	 * is running <br>
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	/**
	 * stop thread <br>
	 * 
	 */
	public void close() {
		flag = false;
	}

}
