/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Iterator;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.api.manager.IHostManager;
import com.primeton.paas.manage.api.util.ExceptionUtil;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class HostManagerFactory {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(HostManagerFactory.class);
	
	private static IHostManager manager = null;
	
	private HostManagerFactory() {}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized IHostManager getHostManager() {
		if (manager != null) {
			return manager;
		}
		ServiceExtensionLoader<IHostManager> loader = ServiceExtensionLoader.load(IHostManager.class);
		if(loader != null) {
			List<Throwable> errorList = loader.getErrorList();	
			if (errorList.size() > 0) {
				logger.error("The Factory mistakes, prompt error ["+ExceptionUtil.getCauseMessage(errorList.get(0))+"]");
			} else {
				Iterator<IHostManager> iterator = loader.iterator();
				while(iterator.hasNext()) {
					manager = iterator.next();
					if (manager != null) {
						break;
					}
				}
			}
		}
		return manager;
	}
	
}
