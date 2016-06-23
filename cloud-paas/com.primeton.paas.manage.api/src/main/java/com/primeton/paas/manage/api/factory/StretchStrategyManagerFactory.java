/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Iterator;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.api.app.IStrategyQueryManager;
import com.primeton.paas.manage.api.app.IStretchStrategyManager;
import com.primeton.paas.manage.api.util.ExceptionUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class StretchStrategyManagerFactory {

	private static ILogger logger = ManageLoggerFactory
			.getLogger(StretchStrategyManagerFactory.class);

	private StretchStrategyManagerFactory() {
	}

	private static IStretchStrategyManager manager = null;

	private static IStrategyQueryManager queryManager = null;

	/**
	 * 
	 * @return
	 */
	public static IStretchStrategyManager getManager() {
		synchronized (StretchStrategyManagerFactory.class) {
			if (manager == null) {
				ServiceExtensionLoader<IStretchStrategyManager> loader = ServiceExtensionLoader
						.load(IStretchStrategyManager.class);
				if (null != loader) {
					List<Throwable> errorList = loader.getErrorList();
					if (errorList.size() > 0) {
						logger.error("The Factory mistakes, prompt error ["
								+ ExceptionUtil.getCauseMessage(errorList
										.get(0)) + "]");
					} else {
						Iterator<IStretchStrategyManager> iterator = loader
								.iterator();
						while (iterator.hasNext()) {
							manager = iterator.next();
							if (manager != null) {
								break;
							}
						}
					}
				}
			}
		}
		return manager;
	}

	/**
	 * 
	 * @return
	 */
	public static IStrategyQueryManager getQueryManager() {
		synchronized (StretchStrategyManagerFactory.class) {
			if (queryManager == null) {
				ServiceExtensionLoader<IStrategyQueryManager> loader = ServiceExtensionLoader
						.load(IStrategyQueryManager.class);
				if (null != loader) {
					List<Throwable> errorList = loader.getErrorList();
					if (errorList.size() > 0) {
						logger.error("The Factory mistakes, prompt error ["
								+ ExceptionUtil.getCauseMessage(errorList
										.get(0)) + "]");
					} else {
						Iterator<IStrategyQueryManager> iterator = loader
								.iterator();
						while (iterator.hasNext()) {
							queryManager = iterator.next();
							if (queryManager != null) {
								break;
							}
						}
					}
				}
			}
		}
		return queryManager;
	}

}
