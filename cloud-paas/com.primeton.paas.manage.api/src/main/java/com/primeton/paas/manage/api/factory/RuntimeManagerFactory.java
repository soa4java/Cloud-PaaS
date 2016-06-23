/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Iterator;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.api.manager.IRuntimeManager;
import com.primeton.paas.manage.api.util.ExceptionUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class RuntimeManagerFactory {

	private static ILogger logger = ManageLoggerFactory
			.getLogger(RuntimeManagerFactory.class);

	private static IRuntimeManager manager = null;

	private RuntimeManagerFactory() {
	}

	/**
	 * 
	 * @return
	 */
	public static synchronized IRuntimeManager getManager() {
		if (manager == null) {
			ServiceExtensionLoader<IRuntimeManager> loader = ServiceExtensionLoader
					.load(IRuntimeManager.class);
			if (loader != null) {
				List<Throwable> errorList = loader.getErrorList();
				if (errorList.size() > 0) {
					logger.error("The Factory mistakes, prompt error ["
							+ ExceptionUtil.getCauseMessage(errorList.get(0))
							+ "]");
				} else {
					Iterator<IRuntimeManager> iterator = loader.iterator();
					while (iterator.hasNext()) {
						return manager = iterator.next();
					}
				}
			}
		}
		return manager;
	}

}
