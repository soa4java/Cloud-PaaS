/**
 * 
 */
package com.primeton.paas.app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.gocom.cloud.cesium.common.api.exception.TimeoutException;
import org.gocom.cloud.cesium.config.api.client.ConfigListenerManager;
import org.gocom.cloud.cesium.config.api.client.ConfigManager;
import org.gocom.cloud.cesium.config.api.client.ConfigManagerFactory;
import org.gocom.cloud.cesium.zkclient.api.ZkConfig;
import org.gocom.cloud.common.logger.api.ILogger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.primeton.paas.app.AppConstants;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class AppUtil {
	
	private static ILogger logger = SystemLoggerFactory.getLogger(AppUtil.class);
	
	/**
	 * 
	 * @param zkConfig
	 * @return
	 */
	public static ConfigManager getConfigManager(ZkConfig zkConfig) {
		ConfigManager configManager = null;
		try {
			configManager = ConfigManagerFactory.createConfigManager();
		} catch (TimeoutException e) {
			if (logger.isErrorEnabled()) logger.error("Failed to connect to "+zkConfig.getZkServers() +" after timeout "+zkConfig.getConnectionTimeout()+" ms",e);
		}
		return configManager;
	}
	
	/**
	 * 
	 * @param zkConfig
	 * @return
	 */
	public static ConfigListenerManager getConfigListenerManager(ZkConfig zkConfig) {
		ConfigListenerManager configListenerManager = null;
		try {
			configListenerManager = ConfigListenerManager.getInstance();
		} catch (TimeoutException e) {
			if (logger.isErrorEnabled()) logger.error("Failed to connect to " + zkConfig.getZkServers() + " after timeout " + zkConfig.getConnectionTimeout() + " ms", e);
		}
		return configListenerManager;
	}
	
	/**
	 * 
	 * @param logLevel
	 * @param xmlFile
	 * @throws XmlUtilException
	 * @throws IOException
	 */
	public static void updateUserLoggerLevel(String logLevel, String xmlFile) throws XmlUtilException, IOException {
		if(logLevel == null || logLevel.trim().length() == 0 || xmlFile == null || xmlFile.trim().length() == 0) {
			return;
		}
		File file = new File(xmlFile);
		if (file == null || !file.exists()) {
			throw new IOException("File {" + xmlFile + "} not found.");
		}
		if (file.isDirectory()) {
			throw new IOException("File {" + file.getAbsolutePath() + "} is a directory, not xml file.");
		}
		Document doc = XmlUtil.parseFile(xmlFile);
		Node appender0 = XmlUtil.findNode(doc, "/appender[@name='ROLLING_FILE']"); //$NON-NLS-1$
		XmlUtil.setAttribute(appender0, "param[@name='Threshold']", "value", logLevel); //$NON-NLS-1$
		
		Node appender1 = XmlUtil.findNode(doc, "/appender[@name='COLLECTOR']"); //$NON-NLS-1$
		XmlUtil.setAttribute(appender1, "param[@name='Threshold']", "value", logLevel); //$NON-NLS-1$
		XmlUtil.saveToFile(doc, xmlFile);
	}
	
	/**
	 * 
	 * @param logLevel
	 * @param xmlFile
	 * @throws XmlUtilException
	 * @throws IOException
	 */
	public static void updateSystemLoggerLevel(String logLevel, String xmlFile)
			throws XmlUtilException, IOException {
		if (logLevel == null || logLevel.trim().length() == 0
				|| xmlFile == null || xmlFile.trim().length() == 0) {
			return;
		}
		File file = new File(xmlFile);
		if(file == null || !file.exists()) {
			throw new IOException("File {" + xmlFile + "} not found.");
		}
		if(file.isDirectory()) {
			throw new IOException("File {" + file.getAbsolutePath() + "} is a directory, not xml file.");
		}
		Document doc = XmlUtil.parseFile(xmlFile);
		Node appender0 = XmlUtil.findNode(doc, "/appender[@name='ROLLING_FILE']"); //$NON-NLS-1$
		XmlUtil.setAttribute(appender0, "param[@name='Threshold']", "value", logLevel); //$NON-NLS-1$
		XmlUtil.saveToFile(doc, xmlFile); 
		
		Node appender1 = XmlUtil.findNode(doc, "/appender[@name='COLLECTOR']"); //$NON-NLS-1$
		XmlUtil.setAttribute(appender1, "param[@name='Threshold']", "value", logLevel); //$NON-NLS-1$
		XmlUtil.saveToFile(doc, xmlFile);
	}
	
	/**
	 * 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, AppConstants.SYS_ENCODING));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}
