/**
 * 
 */
package com.primeton.paas.sms.server.startup;


import java.io.File;

import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider;
import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.common.spi.StartupListenerManager;
import com.primeton.paas.common.spi.http.HttpClient;
import com.primeton.paas.common.spi.http.HttpServer;
import com.primeton.paas.sms.server.conf.SmsConfUtil;
import com.primeton.paas.sms.server.log.SmsRollingFileAppender;
import com.primeton.paas.sms.spi.SmsConstants;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SmsServer implements IServer {
	
	/* (none Javadoc)
	 * @see com.primeton.paas.cardbin.server.startup.IServer#start(java.io.File)
	 */
	public void start(File serverHome) {
		long begin = System.currentTimeMillis();
	
		try {
			System.out.println("\n********************************************************");
			System.out.println("**********    Start The UnionPay SMS Server    *********");
			System.out.println("********************************************************\n");
		
			SmsRollingFileAppender.setLogRootDir(serverHome);
			LoggerFactory.setLoggerProvider(new Log4jLoggerProvider(serverHome.getAbsolutePath() + "/conf/log4j.xml")); //$NON-NLS-1$
			LoggerFactory.refresh();
		
			String host = SystemProperties.getProperty(SmsConstants.HOST_KEY, SmsConstants.DEFAULT_HOST_VALUE);
			int port = SystemProperties.getProperty(SmsConstants.PORT_KEY, Integer.class, SmsConstants.DEFAULT_PORT_VALUE);
			HttpServer server = HttpServer.getHttpServer(host, port);
			server.publish(SmsConstants.SMS_SERVER_REMOTE_OBJECT_NAME, this);
			
			
			//initialize zookeeper
			String zkConfig = SystemProperties.getProperty(SmsConstants.SERVER_HOME_KEY) + File.separator
					+ "conf" + File.separator + SmsConstants.ZK_CONFIG_XML;
			File zkconf = new File(zkConfig);
			if (zkconf.exists() && zkconf.isFile()) {
				ZkClientFactory.init(zkconf);
			} else {
				throw new RuntimeException("File " + zkConfig + " not found.");
			}
			//init sms config
			SmsConfUtil.initSmsConf();
			//
			StartupListenerManager.startListener();
		
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		System.out.println("[SMS Server] startup in " +  (end-begin) +" ms ["+ (end - begin)/1000 + " Seconds].");
	}
	
	/* (none Javadoc)
	 * @see com.primeton.paas.cardbin.server.startup.IServer#stop(java.io.File)
	 */
	public void stop(File serverHome) {
		long begin = System.currentTimeMillis();
		try {
			System.out.println("\n********************************************************");
			System.out.println("**********    Stop The UnionPay SMS Server    *********");
			System.out.println("********************************************************\n");
			
			StartupListenerManager.stopListener();
			LoggerFactory.destroy();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		System.out.println("[SMS Server] stop in " +  (end-begin) +" ms ["+ (end - begin)/1000 + " Seconds].");
	}

	/* (none Javadoc)
	 * @see com.primeton.paas.cardbin.server.startup.IServer#stopRemote(java.io.File)
	 */
	public void stopRemote(File serverHome) {
		String ip = SystemProperties.getProperty(SmsConstants.HOST_KEY, SmsConstants.DEFAULT_HOST_VALUE);
		int port = SystemProperties.getProperty(SmsConstants.PORT_KEY, int.class, SmsConstants.DEFAULT_PORT_VALUE);
		try {
			IServer server = HttpClient.getRemoteObject(ip, port, SmsConstants.SMS_SERVER_REMOTE_OBJECT_NAME, IServer.class, false);
			server.stop(serverHome);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}
