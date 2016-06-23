/**
 * 
 */
package com.primeton.paas.mail.server.startup;

import java.io.File;

import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider;
import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.mail.server.config.Constants;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class BootStrap implements Constants {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String serverHome = System.getProperty(SERVER_HOME_KEY);
		String attachmentHome = System.getProperty(ATTACHMENT_HOME);

		File file = new File(serverHome);
		if (file == null || file.exists() == false || file.isFile()) {
			throw new RuntimeException("Server home not found.");
		}
		if (serverHome.endsWith(File.separator)) {
			serverHome = serverHome.substring(0, serverHome.length() - 1);
		}
		if (attachmentHome.endsWith(File.separator)) {
			attachmentHome = attachmentHome.substring(0, attachmentHome.length() - 1);
		}
		System.setProperty(Constants.SERVER_HOME_KEY, serverHome);
		System.setProperty(Constants.ATTACHMENT_HOME, attachmentHome);

		start();
	}

	/**
	 * 
	 */
	public static void start() {
		String log4j = SystemProperties.getProperty(Constants.SERVER_HOME_KEY)
				+ File.separator + "conf" + File.separator + Constants.LOG4J; //$NON-NLS-1$
		LoggerFactory.setLoggerProvider(new Log4jLoggerProvider(log4j));

		String zkConfig = SystemProperties
				.getProperty(Constants.SERVER_HOME_KEY)
				+ File.separator
				+ "conf" + File.separator + Constants.ZK_CONFIG_XML; //$NON-NLS-1$
		File zkXml = new File(zkConfig);
		if (zkXml.exists() && zkXml.isFile()) {
			ZkClientFactory.init(zkXml);
		} else {
			throw new RuntimeException("File " + zkConfig + " not found.");
		}

		MailCoreThread thread = new MailCoreThread();
		thread.start();
	}

}
