/**
 * 
 */
package com.primeton.paas.openapi.admin.runtime;

import java.io.File;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.primeton.paas.openapi.admin.ServerContext;
import com.primeton.paas.openapi.base.uitl.XmlUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RuntimeManager {

	private static ILogger log = LoggerFactory.getLogger(RuntimeManager.class);

	private static String START_FILE_NAME = "handler-startup.xml";

	private static boolean isStoped = true;

	public static void start() {
		if (!isStoped) {
			if (log.isWarnEnabled()) {
				log.warn("Warning: The runtime has started,please stop first.");
			}
			return;
		}
		if (log.isInfoEnabled()) {
			log.info("The runtime is starting...");
		}
		prepareRuntimeListener();

		IRuntimeListenerManager.INSTANCE.startListener();

		if (log.isInfoEnabled()) {
			log.info("The runtime is started.");
		}
		isStoped = false;

	}

	public static void stop() {

		if (isStoped) {
			if (log.isWarnEnabled()) {
				log.warn("Warning: The runtime has been stoped,please start first.");
			}
			return;
		}
		isStoped = true;
		if (log.isInfoEnabled()) {
			log.info("The runtime is stoping....");
		}

		IRuntimeListenerManager.INSTANCE.stopListener();
		IRuntimeListenerRegistry.INSTANCE.getRuntimeListeners().clear();
		if (log.isInfoEnabled()) {
			log.info("The runtime is stoped.");
		}
	}

	private static void prepareRuntimeListener() {
		String handFilePath = ServerContext.getInstance().getConfigDirPath()
				+ File.separator + START_FILE_NAME;
		if (!new File(handFilePath).exists()) {
			if (log.isErrorEnabled()) {
				log.error("App handle config start file '" + START_FILE_NAME
						+ "' not exist. ");
			}
		} else {
			try {
				Document doc = XmlUtil.parseFile(handFilePath);
				NodeList nodeList = XmlUtil.findNodes(doc.getDocumentElement(),
						"handler");
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					String handleClass = XmlUtil.getAttrValue((Element) node,
							"handle-class");
					String order = XmlUtil
							.getAttrValue((Element) node, "order");
					registerStartHandle(handleClass, order);

				}
			} catch (Throwable e) {
				if (log.isInfoEnabled()) {
					log.info("Parse the document error.", e);
				}
			}

		}
	}

	@SuppressWarnings("rawtypes")
	private static void registerStartHandle(String handleClass, String order) {
		try {
			Class clazz = Thread.currentThread().getContextClassLoader()
					.loadClass(handleClass);
			Object obj = clazz.newInstance();

			if (obj instanceof IRuntimeListener) {
				if (log.isInfoEnabled()) {
					log.info("Register runtimeListener " + clazz.getName());
				}
				if (order == null || order.length() == 0) {
					IRuntimeListenerRegistry.INSTANCE
							.registerRuntimeListener((IRuntimeListener) obj);
				} else {
					int orderIndex = Integer.parseInt(order);
					IRuntimeListenerRegistry.INSTANCE.registerRuntimeListener(
							(IRuntimeListener) obj, orderIndex);
				}

			} else {
				if (log.isInfoEnabled()) {
					log.info("The  RuntimeListener '"
							+ handleClass
							+ "' isn't the instance of class IRuntimeListener.class");
				}
			}
		} catch (Exception e) {
			if (log.isInfoEnabled()) {
				log.info("Load the runtimeListener error.", e);
			}
		}
	}

}