/**
 * 
 */
package com.primeton.paas.cep.engine;

import java.io.File;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider;

import com.primeton.paas.cep.util.FileUtil;

/**
 * Engine bootstrap main <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class Bootstrap implements Constants {
	
	private static ILogger logger = null;
	
	// JAVA_OPTS -Dserver_home ${server_home} -Dgroup_name=${group_name} -Dengine_name=${engine_name} -Dmq_server=${mq_server} -Dmq_dists=${mq_dests} -Dmq_types=${mq_types} 
	public static void main(String[] args) {
		final String server_home = System.getProperty(SERVER_HOME);
		final String group_name = System.getProperty(GROUP_NAME);
		final String engine_name = System.getProperty(ENGINE_NAME);
		final String engine_ip = System.getProperty(ENGINE_IP);
		final String mq_server = System.getProperty(MQ_SERVER);
		final String mq_dests = System.getProperty(MQ_DESTS);
		final String mq_types = System.getProperty(MQ_TYPES);
		
		ServerContext context = ServerContext.getContext();
		ServerConfig cfg = context.getServerConfig();
		cfg.set(SERVER_HOME, server_home);
		cfg.set(GROUP_NAME, group_name);
		cfg.set(ENGINE_NAME, engine_name);
		cfg.set(ENGINE_IP, engine_ip);
		cfg.set(MQ_SERVER, mq_server); // name [/Cloud/Cesium/Variables/${name}]
		cfg.set(MQ_TYPES, mq_types); // exchange | queue
		cfg.set(MQ_DESTS, mq_dests); // name
		
		// validate ${server_home}
		if (!FileUtil.exist(server_home, false)) {
			System.out.println("[Error] Engine home [" + server_home + "] not found. System exit 1.");
			System.exit(1);
		} else {
			System.out.println("[INFO] Engine home is [" + server_home + "].");
		}
		
		String log4j = context.getServerHome() + File.separator + "conf" + File.separator + "log4j.properties";
		boolean exist = FileUtil.exist(log4j, true);
		if (!exist) {
			log4j = context.getServerHome() + File.separator + "conf" + File.separator + "log4j.xml";
			exist = FileUtil.exist(log4j, true);
		}
		if (exist) {
			LoggerFactory.setLoggerProvider(new Log4jLoggerProvider(log4j));
			logger = LoggerFactory.getLogger(Bootstrap.class);
		} else {
			System.out.println("[WARN] Log4j configuration file [" + log4j + "] not found, can not init log4j environment.");
		}
		
		String zkConfig = context.getServerHome() + File.separator + "conf" + File.separator + "zkConfig.xml";
		if (FileUtil.exist(zkConfig, true)) {
			ZkClientFactory.init(new File(zkConfig));
		} else {
			logger.warn("File [" + zkConfig + "] not found. System exit 1.");
			System.exit(1);
		}
		
		EngineThread thread = new EngineThread();
		thread.setName(EngineThread.class.getSimpleName());
		
		ShutdownHook hook = new ShutdownHook(thread);
		logger.info("Add " + hook + " for stop Engine (kill ${pid}).");
		Runtime.getRuntime().addShutdownHook(new Thread(hook));
		
		thread.start();
	}
	
	/**
	 * For close CEPEngine <br>
	 * 
	 * @author lizhongwen(mailto:lizw@primeton.com)
	 *
	 */
	private static class ShutdownHook implements Runnable {
		
		private EngineThread thread;
		private static ILogger logger = LoggerFactory.getLogger(ShutdownHook.class);
		
		/**
		 * @param thread
		 */
		public ShutdownHook(EngineThread thread) {
			super();
			this.thread = thread;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			logger.info("JVM will be shut down. (kill siganl) Waiting for EngineThread to stop.");
			if (thread != null && thread.isRunning()) {
				thread.close();
			} else {
				return;
			}
			
			long timeout = 60000L;
			long begin = System.currentTimeMillis();
			long end = System.currentTimeMillis();
			while (true) {
				end = System.currentTimeMillis();
				if (end - begin > timeout) {
					break;
				}
				if (!thread.isRunning()) {
					break;
				}
				ThreadUtil.sleep(100L);
			}
			
			ZkClientFactory.destroy();
			
			LoggerFactory.refresh();
			
			LoggerFactory.destroy();
		}
		
	}
	
}
