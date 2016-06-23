/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Iterator;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.api.app.IServiceWarnStrategyManager;
import com.primeton.paas.manage.api.util.ExceptionUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ServiceWarnStrategyManagerFactory {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(ServiceWarnStrategyManagerFactory.class);
	
	private ServiceWarnStrategyManagerFactory() {}
	

	private static IServiceWarnStrategyManager manager = null;
	
	/**
	 * 
	 * @return
	 */
	public static IServiceWarnStrategyManager getManager() {
		synchronized (StretchStrategyManagerFactory.class) {
			if (manager == null) {
				ServiceExtensionLoader<IServiceWarnStrategyManager> loader = ServiceExtensionLoader.load(IServiceWarnStrategyManager.class);
				if(null != loader){
					List<Throwable> errorList = loader.getErrorList();	
					if (errorList.size() > 0) {
						logger.error("The Factory mistakes, prompt error ["+ExceptionUtil.getCauseMessage(errorList.get(0))+"]");
					} else {
						Iterator<IServiceWarnStrategyManager> iterator = loader.iterator();
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
	
}
