/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Iterator;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.api.manager.IAuthManager;
import com.primeton.paas.manage.api.util.ExceptionUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class AuthBaseManagerFactory {

	private static ILogger logger = ManageLoggerFactory
			.getLogger(AuthBaseManagerFactory.class);

	private AuthBaseManagerFactory() {
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized IAuthManager getManager() {
		ServiceExtensionLoader<IAuthManager> loader = ServiceExtensionLoader
				.load(IAuthManager.class);
		if (loader != null) {
			List<Throwable> errorList = loader.getErrorList();
			if (errorList.size() > 0) {
				logger.error("The Factory mistakes, prompt error ["
						+ ExceptionUtil.getCauseMessage(errorList.get(0)) + "]");
			} else {
				Iterator<IAuthManager> iterator = loader.iterator();
				while (iterator.hasNext()) {
					return iterator.next();
				}
			}
		}
		return null;
	}

}
