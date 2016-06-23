/**
 * 
 */
package com.primeton.paas.cardbin.server.startup;

import java.io.File;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider;
import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.cardbin.server.log.PaasRollingFileAppender;
import com.primeton.paas.cardbin.server.task.FetchDataFilesTask;
import com.primeton.paas.cardbin.server.task.ScanFileTask;
import com.primeton.paas.cardbin.spi.CardBinConstants;
import com.primeton.paas.common.spi.StartupListenerManager;
import com.primeton.paas.common.spi.http.HttpClient;
import com.primeton.paas.common.spi.http.HttpServer;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class FinanceCardBinServer implements IServer {

	private static ILogger logger = LoggerFactory.getLogger(FinanceCardBinServer.class);
	
	private static final String FINANCE_CARDBIN_SERVER_REMOTE_OBJECT_NAME = "financeCardBinServer";

	public void start(File serverHome) {
		long begin = System.currentTimeMillis();
		try {
			System.out.println("\n********************************************************");
			System.out.println("*  Start The UnionPay Finance CardBin Server  *");
			System.out.println("********************************************************\n");
			PaasRollingFileAppender.setLogRootDir(serverHome);
			LoggerFactory.setLoggerProvider(new Log4jLoggerProvider(serverHome.getAbsolutePath() + "/conf/log4j.xml"));
			LoggerFactory.refresh();
			
			// WebService IP & Port
			String host = SystemProperties.getProperty(CardBinConstants.HOST_KEY, CardBinConstants.DEFAULT_HOST_VALUE);
			int port = SystemProperties.getProperty(CardBinConstants.WS_PORT_KEY, Integer.class, CardBinConstants.DEFAULT_WS_PORT_VALUE);

			
			String isEnable = SystemProperties.getProperty(CardBinConstants.SYNC_IS_ENABLE_KEY, "N");//
			if (isEnable != null && "Y".equals(isEnable.toUpperCase())) {
				logger.info("Enable cardbin synchronize.");
				
				//cardbin fetch file thread
				FetchDataFilesTask syncTask = new FetchDataFilesTask();
				Thread fetchThread = new Thread(syncTask);
				fetchThread.setName("fetchThread");
				fetchThread.setDaemon(true);
				fetchThread.start();
				
			} else {
				logger.info("Not Enable cardbin synchronize.");
			}
			
			//cardbin scan sync thread
			ScanFileTask task = new ScanFileTask();
			Thread scanThread = new Thread(task);
			scanThread.setName("scanThread");
			scanThread.setDaemon(true);
			scanThread.start();
			
			HttpServer server = HttpServer.getHttpServer(host, port);
			server.publish(FINANCE_CARDBIN_SERVER_REMOTE_OBJECT_NAME, this);
			StartupListenerManager.startListener();
		} catch (Throwable e) {
			logger.info("error:"+e.getMessage());
		}
		System.out.println("[cardbin server] startup in " + (System.currentTimeMillis() - begin) + " ms.");
	}

	public void stop(File serverHome) {
		long begin = System.currentTimeMillis();
		try {
			System.out.println("\n********************************************************");
			System.out.println("*  Stop The UnionPay Finance CardBin Server  *");
			System.out.println("********************************************************\n");
			StartupListenerManager.stopListener();
			LoggerFactory.destroy();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("[cardbin server] stop in " + (System.currentTimeMillis() - begin) + " ms.");
	}

	public void stopRemote(File serverHome) {
		String ip = SystemProperties.getProperty(CardBinConstants.HOST_KEY, CardBinConstants.DEFAULT_HOST_VALUE);
		int port = SystemProperties.getProperty(CardBinConstants.PORT_KEY, int.class, CardBinConstants.DEFAULT_PORT_VALUE);
		try {
			IServer server = HttpClient.getRemoteObject(ip, port, FINANCE_CARDBIN_SERVER_REMOTE_OBJECT_NAME, IServer.class, false);
			server.stop(serverHome);
		} catch (Throwable e) {
		}
	}
	
}
