/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Iterator;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.util.ExceptionUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class OrderManagerFactory {

	private static ILogger logger = ManageLoggerFactory
			.getLogger(OrderManagerFactory.class);

	private static IOrderManager manager;

	private OrderManagerFactory() {
	}

	/**
	 * 
	 * @return
	 */
	public static synchronized IOrderManager getManager() {
		if (null == manager) {
			ServiceExtensionLoader<IOrderManager> loader = ServiceExtensionLoader
					.load(IOrderManager.class);
			if (loader != null) {
				List<Throwable> errorList = loader.getErrorList();
				if (errorList.size() > 0) {
					logger.error("The Factory mistakes, prompt error ["
							+ ExceptionUtil.getCauseMessage(errorList.get(0))
							+ "]");
				} else {
					Iterator<IOrderManager> iterator = loader.iterator();
					while (iterator.hasNext()) {
						manager = iterator.next();
						if (manager != null) {
							return manager;
						}
					}
				}
			}
		}
		return manager;
	}

}
