/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Iterator;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.util.ExceptionUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class WebAppManagerFactory {

	private static ILogger logger = ManageLoggerFactory
			.getLogger(WebAppManagerFactory.class);

	private WebAppManagerFactory() {
	}

	private static IWebAppManager manager;

	/**
	 * 
	 * @return
	 */
	public static synchronized IWebAppManager getManager() {
		if (null == manager) {
			ServiceExtensionLoader<IWebAppManager> loader = ServiceExtensionLoader
					.load(IWebAppManager.class);
			if (loader != null) {
				List<Throwable> errorList = loader.getErrorList();
				if (errorList.size() > 0) {
					logger.error("The Factory mistakes, prompt error ["
							+ ExceptionUtil.getCauseMessage(errorList.get(0))
							+ "]");
				} else {
					Iterator<IWebAppManager> iterator = loader.iterator();
					while (iterator.hasNext()) {
						return manager = iterator.next();
					}
				}
			}
		}
		return manager;
	}

}
