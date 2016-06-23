/**
 * 
 */
package com.primeton.paas.cep.engine;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.mqclient.api.MQClient;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.cesium.zkclient.api.ZkChildListener;
import org.gocom.cloud.cesium.zkclient.api.ZkClient;
import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;
import org.gocom.cloud.cesium.zkclient.api.ZkDataListener;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.cep.model.EPSInstance;
import com.primeton.paas.cep.spi.EPSException;
import com.primeton.paas.cep.spi.EPSInstanceManager;
import com.primeton.paas.cep.spi.EPSInstanceManagerFactory;
import com.primeton.paas.cep.spi.StartupListener;
import com.primeton.paas.cep.spi.StartupListenerManager;
import com.primeton.paas.cep.util.PathUtil;
import com.primeton.paas.cep.util.StringUtil;

/**
 * Engine core thread. <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EngineThread extends Thread implements Constants {
	
	private static final long ENGINE_INTERVAL = 2000L;	// 2s
	
	private static ILogger logger = LoggerFactory.getLogger(EngineThread.class);
	
	private boolean isRunning = false;
	private boolean flag = true;
	private boolean isSubscribe = false;
	// exchangeName - queueName (temp)
	private Map<String, String> exchangeBindQueues = new ConcurrentHashMap<String, String>();
	
	private ServerContext context = ServerContext.getContext();
	private ZkClient zkClient = ZkClientFactory.getZkClient();
	private EPSInstanceManager manager = EPSInstanceManagerFactory.getManager();
	
	// instanceId-EPSInstance
	private Map<String, EPSInstance> epsBuffer = new ConcurrentHashMap<String, EPSInstance>();
	
	private Engine engine = new Engine(); // self

	public EngineThread() {
		super();
		setName(EngineThread.class.getSimpleName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		logger.info("\n========================================================================"
				+ "\n======================== Engine startup success. ======================="
				+ "\n========================================================================\n");
		
		this.isRunning = true;
		
		register();
		
		StartupListenerManager.start();
		
		int count0 = 0;
		int count1 = 0;
		int count2 = 0;
		
		while (this.flag) {
			if (Engine.MODE_SLAVE.equals(context.getRunMode())) { // run as slave now
				List<Engine> engines = PathUtil.getEngines();
				Engine master = null;
				for (Engine e : engines) {
					if (Engine.MODE_MASTER.equals(e.getMode())) {
						master = e;
						break;
					}
				}
				// now no master engine
				// self run mode change to master
				if (master == null) { 
					this.engine.setMode(Engine.MODE_MASTER);
					this.engine.setTime(System.currentTimeMillis());
					context.setRunMode(Engine.MODE_MASTER);
					PathUtil.setEngine(this.engine);
					logger.info("\n========================================================================"
							+ "\n======================== Run mode change to master. ===================="
							+ "\n========================================================================\n");
				}
			} else { // run as master now
				if (++count0 == 5) {
					List<Engine> engines = PathUtil.getEngines();
					long time = this.engine.getTime();
					for (Engine e : engines) {
						if (e != null && !this.engine.getName().equals(e.getName())
								&& Engine.MODE_MASTER.equals(e.getMode())
								&& e.getTime() <= time) {
							this.engine.setMode(Engine.MODE_SLAVE);
							context.setRunMode(Engine.MODE_SLAVE);
							PathUtil.setEngine(this.engine);
							logger.info("\n========================================================================"
									+ "\n======================== Run mode change to slave. ====================="
									+ "\n========================================================================\n");
							break;
						}
					}
					count0 = 0;
				}
			}
			
			// [slave engine not working]
			if (Engine.MODE_MASTER.equals(context.getRunMode()) && (!isSubscribe)) {
				subscribe();
			} else if (Engine.MODE_SLAVE.equals(context.getRunMode()) && isSubscribe) {
				unsubscribe();
			}
			
			if (++count1 == 30) { // 2*30=60s
				Engine self = PathUtil.getEngine(context.getEngineName());
				if (self == null) {
					PathUtil.setEngine(this.engine);
				}
				count1 = 0;
			}
			
			// EPS (Memory with zookeeper sync)
			if (++count2 == 120) { // 2*150=300s
				syncEPS();
				count2 = 0;
			}
			
			ThreadUtil.sleep(ENGINE_INTERVAL);
		}
		
		long begin = System.currentTimeMillis();
		logger.info("Begin close all " + StartupListener.class.getSimpleName() + ".");
		StartupListenerManager.stop();
		long end = System.currentTimeMillis();
		logger.info("End close all " + StartupListener.class.getSimpleName() + ", time spent " + (end-begin)/1000L + " seconds.");
		
		// unsubscribe
		begin = System.currentTimeMillis();
		logger.info("Begin unsubscribe all message queues.");
		if (isSubscribe) {
			unsubscribe();
		}
		end = System.currentTimeMillis();
		logger.info("End unsubscribe all message queues, time spent " + (end-begin)/1000L + " seconds.");
		
		// exit
		begin = System.currentTimeMillis();
		logger.info("Begin unregister Engine and EPS instances.");
		unregister();
		end = System.currentTimeMillis();
		logger.info("End unregister Engine and EPS instances, time spent " + (end-begin)/1000L + " seconds.");
		
		logger.info("\n========================================================================"
				+ "\n======================== Engine shut down success. ====================="
				+ "\n========================================================================\n");
		this.isRunning = false;
	}
	
	/**
	 * 
	 */
	public void close() {
		this.flag = false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	/**
	 * 
	 */
	private void register() {
		
		String engineGroupPath = PathUtil.getEngineGroupPath(); // /CEP/Engine/default
		if (!zkClient.exists(engineGroupPath)) {
			zkClient.createPersistent(engineGroupPath, true);
		}
		// /CEP/Engine/group1/... [Engine instances]
		List<Engine> engines = PathUtil.getEngines();
		
		// find master engine
		Engine master = null;
		for (Engine engine : engines) {
			if (Engine.MODE_MASTER.equals(engine.getMode())) {
				master = engine;
				break;
			}
		}
		
		// register Engine
		this.engine.setName(context.getEngineName());
		String runMode = master == null ? Engine.MODE_MASTER : Engine.MODE_SLAVE;
		this.engine.setMode(runMode);
		this.engine.setIp(context.getEngineIp());
		this.engine.setTime(System.currentTimeMillis());
		context.setRunMode(runMode);
		PathUtil.setEngine(this.engine); 
		logger.info("Register " + this.engine + " as " + runMode + " node.");
		
		zkClient.subscribeChildChanges(engineGroupPath, new ZkChildListener() {
			public void handleChildChange(String parentPath, List<String> currentChilds)
					throws Exception {
				logger.info("[" + parentPath + "] Engine instances " + currentChilds + ".");
			}
		});
		
		// very important
		String epsGroupPath = PathUtil.getEPSGroupPath(); // /CEP/EPS/default[group]
		if (!zkClient.exists(epsGroupPath)) {
			zkClient.createPersistent(epsGroupPath, true);
		}
		List<EPSInstance> instances = PathUtil.getEPSs();
		for (EPSInstance instance : instances) {
			registerEPS(instance);
		}
		
		zkClient.subscribeChildChanges(epsGroupPath, new ZkChildListener() {
			
			public void handleChildChange(String parentPath, List<String> currentChilds)
					throws Exception {
				logger.info("[" + parentPath + "] Now EPS instances " + currentChilds + ".");
				if (currentChilds != null && !currentChilds.isEmpty()) {
					for (String id : epsBuffer.keySet()) {
						if (!currentChilds.contains(id)) {
							unregisterEPS(id);
						}
					}
					for (String id : currentChilds) {
						if (!epsBuffer.containsKey(id)) {
							EPSInstance instance = PathUtil.getEPS(id);
							if (instance != null) {
								registerEPS(instance);
							}
							
						}
					}
				}
			}
		});
		
	}
	
	/**
	 * 
	 * @param instance
	 */
	private void registerEPS(EPSInstance instance) {
		if (instance == null || StringUtil.isEmpty(instance.getId())
				|| epsBuffer.containsKey(instance.getId())) {
			return;
		}
		epsBuffer.put(instance.getId(), instance);
		if (EPSInstance.ENABLE.equals(instance.getEnable())) {
			try {
				manager.start(instance.getId());
			} catch (EPSException e) {
				logger.error(e);
			}
		}
		zkClient.subscribeDataChanges(PathUtil.getEPSPath(instance.getId()), new ZkDataListener() {
			
			public void handleDataDeleted(String path) throws Exception {
				if (StringUtil.isEmpty(path)) {
					return;
				}
				String id = path.substring(path.lastIndexOf(PathUtil.SEPARATOR) + 1);
				epsBuffer.remove(id);
				manager.stop(id);
				//zkClient.unsubscribeDataChanges(path);
			}
			
			public void handleDataChange(String path, Object data) throws Exception {
				if (StringUtil.isEmpty(path) || data == null || !(data instanceof EPSInstance)) {
					return;
				}
				EPSInstance instance = (EPSInstance)data;
				//String id = path.substring(path.lastIndexOf(PathUtil.SEPARATOR) + 1);
				String id = instance.getId();
				EPSInstance old = epsBuffer.remove(id);
				if (old != null) { // UPDATE
					// DISABLE - previous
					if (EPSInstance.DISABLE.equals(old.getEnable())) {
						// ENABLE - now
						if (EPSInstance.ENABLE.equals(instance.getEnable())) {
							manager.start(id);
						}
					// ENABLE - previous
					} else {
						// ENABLE && UPDATE arguments - now
						if (EPSInstance.ENABLE.equals(instance.getEnable()) 
								&& (!old.getEps().equals(instance.getEps())
										|| !old.getListeners().equals(instance.getListeners()))
						) {
							manager.restart(id);
						// DISABLE - now
						} else if (EPSInstance.DISABLE.equals(instance.getEnable())) {
							manager.stop(id);
						}
					}
				} else { // Register
					if (EPSInstance.ENABLE.equals(instance.getEnable())) {
						manager.start(id);
					}
				}
				epsBuffer.put(id, instance);
			}
		});
		
		logger.info("Register " + EPSInstance.class.getSimpleName() + " [" + instance.getId() + "] success.");
	}
	
	/**
	 * 
	 * @param id
	 */
	private void unregisterEPS(String id) {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		EPSInstance instance = epsBuffer.remove(id);
		if (instance == null) {
			return;
		}
		try {
			zkClient.unsubscribeDataChanges(PathUtil.getEPSPath(id));
			manager.stop(id);
		} catch (EPSException e) {
			logger.error(e);
		}
		
	}
	
	/**
	 * 
	 */
	private void unregister() {
		for (String id :epsBuffer.keySet()) {
			unregisterEPS(id);
		}
		epsBuffer.clear();
		
		// unregister Engine
		PathUtil.deleteEngine(context.getEngineName());
	}
	
	/**
	 * 
	 */
	private void subscribe() {
		if (isSubscribe) {
			return;
		}
		this.isSubscribe = true;
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
	}
	
	private void subscribeExchange(String exchangeName) {
		try {
			MQClient mqClient = context.getMQClient();
			mqClient.createExchange(exchangeName, "fanout", false);
			String queueName = UUID.randomUUID().toString();
			//mqClient.createQueue(queueName, false, true, true);
			mqClient.createQueue(queueName, false);
			mqClient.queueBindExchange(exchangeName, queueName, "", null);
			String tempQueue = exchangeBindQueues.remove(exchangeName);
			if (StringUtil.isNotEmpty(tempQueue)) {
				try {
					mqClient.destroyQueue(tempQueue);
				} catch (MessageException e) {
					logger.error(e);
				}
			}
			exchangeBindQueues.put(exchangeName, queueName);
			mqClient.subscribeQueue(queueName, new EventMessageListener());
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 
	 * @param queueName
	 */
	private void subscribeQueue(String queueName) {
		try {
			MQClient mqClient = context.getMQClient();
			mqClient.createQueue(queueName, false, true, true);
			mqClient.subscribeQueue(queueName, new EventMessageListener());
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	/**
	 * 
	 */
	private void unsubscribe() {
		if (!isSubscribe) {
			return;
		}
		this.isSubscribe = false;
		List<String> dests = context.getMQDests();
		List<String> types = context.getMQTypes();
		if (dests == null || types == null || dests.size() != types.size()) {
			logger.warn("Message destination configuration error, check JVM parameter -Dmq_dests -Dmq_types");
			return;
		}
		
		for (int i=0; i<dests.size(); i++) {
			String type = types.get(i);
			String dest = dests.get(i);
			
			if (MQ_TYPE_QUEUE.equals(type)) {
				try {
					MQClient mqClient = context.getMQClient();
					mqClient.unSubscribeQueue(dest);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}

		for (String queueName : exchangeBindQueues.values()) {
			try {
				MQClient mqClient = context.getMQClient();
				mqClient.destroyQueue(queueName); // temp
			} catch (Exception e) {
				logger.error(e);
			}
			exchangeBindQueues.clear();
		}
	}
	
	/**
	 * sync
	 */
	private void syncEPS() {
		long begin = System.currentTimeMillis();
		logger.info("Start synchronize zookeeper and Engine-Mem EPS instances.");
		List<EPSInstance> zkInstances = PathUtil.getEPSs();
		
		logger.info("Scan Engine-Mem EPS instances.");
		for (String epsId : epsBuffer.keySet()) {
			if (StringUtil.isEmpty(epsId)) {
				epsBuffer.remove(epsId);
			}
			EPSInstance zk = null;
			for (EPSInstance instance : zkInstances) {
				if (epsId.equals(instance.getId())) {
					zk = instance;
					break;
				}
			}
			if (zk == null) {
				unregisterEPS(epsId);
			} else {
				EPSInstance mem = epsBuffer.get(epsId);
				boolean isRunning = manager.isRunning(epsId);
				if (mem.getEnable().equals(zk.getEnable())) {
					if (EPSInstance.ENABLE.equals(mem.getEnable())
							&& !isRunning) {
						try {
							manager.start(epsId);
						} catch (EPSException e) {
							logger.error(e);
						}
					} else if (EPSInstance.DISABLE.equals(mem.getEnable())
							&& isRunning) {
						try {
							manager.stop(epsId);
						} catch (EPSException e) {
							logger.error(e);
						}
					}
				} else {
					epsBuffer.put(epsId, zk);
					if (EPSInstance.ENABLE.equals(zk.getEnable())) {
						try {
							manager.start(epsId);
						} catch (EPSException e) {
							logger.error(e);
						}
					} else {
						try {
							manager.stop(epsId);
						} catch (EPSException e) {
							logger.error(e);
						}
					}
				}
			}
		}
		
		logger.info("Scan zookeeper EPS instances.");
		for (EPSInstance instance : zkInstances) {
			String epsId = instance.getId();
			if (!epsBuffer.containsKey(epsId)) {
				registerEPS(instance);
			}
		}
		
		long end = System.currentTimeMillis();
		logger.info("End synchronize zookeeper and Engine-Mem EPS instances, time spent " + (end-begin)/1000L + " seconds.");
	}
	
}
