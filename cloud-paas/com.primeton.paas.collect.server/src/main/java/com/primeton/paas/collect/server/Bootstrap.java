/**
 * 
 */
package com.primeton.paas.collect.server;

import java.io.File;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider;

import com.primeton.paas.collect.common.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class Bootstrap implements Constants {
	
	private static ILogger logger = null;

	// JAVA_OPTS -Dserver_home ${server_home} -Dgroup_name ${group_name} -Dserver_name ${server_name} -Dserver_ip ${server_ip} -Dmq_server=${mq_server} -Dmq_dests=${mq_dests} -Dmq_types=${mq_types}
	// -Dlog_root ${log_root} -Dappender_buffer ${appender_buffer}
	public static void main(String[] args) {

		final String server_home = System.getProperty(SERVER_HOME);
		final String group_name = System.getProperty(GROUP_NAME);
		final String server_name = System.getProperty(SERVER_NAME);
		final String server_ip = System.getProperty(SERVER_IP);
		final String mq_server = System.getProperty(MQ_SERVER);
		final String mq_dests = System.getProperty(MQ_DESTS);
		final String mq_types = System.getProperty(MQ_TYPES);
		final String log_root = System.getProperty(LOG_ROOT);
		final String appender_buffer = System.getProperty(APPENDER_BUFFER);
		
		ServerContext context = ServerContext.getContext();
		ServerConfig cfg = context.getServerConfig();
		cfg.set(SERVER_HOME, server_home);
		cfg.set(GROUP_NAME, group_name);
		cfg.set(SERVER_NAME, server_name);
		cfg.set(SERVER_IP, server_ip);
		cfg.set(MQ_SERVER, mq_server); // name [/Cloud/Cesium/Variables/${name}]
		cfg.set(MQ_TYPES, mq_types); // exchange | queue
		cfg.set(MQ_DESTS, mq_dests); // name
		cfg.set(LOG_ROOT, log_root);
		if (StringUtil.isNotEmpty(appender_buffer)) {
			try {
				cfg.set(APPENDER_BUFFER, Integer.parseInt(appender_buffer));
			} catch (NumberFormatException e) {
				// ignore
			}
		}
		
		File f = new File(server_home);
		
		if (f.exists() && f.isDirectory()) {
			System.out.println("[INFO ] Server home is [" + server_home + "].");
		} else {
			System.out.println("[ERROR] Server home [" + server_home + "] not found. System exit 1.");
			System.exit(1);
		}
		
		String log4j = context.getServerHome() + File.separator + "conf" + File.separator + "log4j.properties"; //$NON-NLS-1$ //$NON-NLS-2$
		f = new File(log4j);
		boolean exist = f.exists() && f.isFile();
		if (!exist) {
			log4j = context.getServerHome() + File.separator + "conf" + File.separator + "log4j.xml"; //$NON-NLS-1$ //$NON-NLS-2$
			f = new File(log4j);
			exist = f.exists() && f.isFile();
		}
		if (exist) {
			LoggerFactory.setLoggerProvider(new Log4jLoggerProvider(log4j));
			logger = LoggerFactory.getLogger(Bootstrap.class);
		} else {
			System.out.println("[WARN ] Log4j configuration file [" + log4j + "] not found, can not init log4j environment.");
		}
		
		String zkConfig = context.getServerHome() + File.separator + "conf" + File.separator + "zkConfig.xml"; //$NON-NLS-1$ //$NON-NLS-2$
		f = new File(log4j);
		exist = f.exists() && f.isFile();
		if (exist) {
			ZkClientFactory.init(new File(zkConfig));
		} else {
			logger.warn("File [" + zkConfig + "] not found. System exit 1.");
			System.exit(1);
		}
		
		ServerThread thread = new ServerThread();
		thread.setName(ServerThread.class.getSimpleName());
		
		ShutdownHook hook = new ShutdownHook(thread);
		logger.info("Add " + hook + " for stop server.");
		Runtime.getRuntime().addShutdownHook(new Thread(hook));
		
		thread.start();
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	private static class ShutdownHook implements Runnable {
		
		private ServerThread thread;
		private static ILogger logger = LoggerFactory.getLogger(ShutdownHook.class);
		
		/**
		 * @param thread
		 */
		public ShutdownHook(ServerThread thread) {
			super();
			this.thread = thread;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			logger.info("JVM will be shut down, waiting for master thread to stop.");
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
