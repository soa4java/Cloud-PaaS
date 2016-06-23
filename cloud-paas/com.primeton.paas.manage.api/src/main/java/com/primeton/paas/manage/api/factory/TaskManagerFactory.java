/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Iterator;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.api.manager.ITaskManager;
import com.primeton.paas.manage.api.util.ExceptionUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class TaskManagerFactory {

	private static ILogger logger = ManageLoggerFactory
			.getLogger(TaskManagerFactory.class);

	private TaskManagerFactory() {
	}

	private static ITaskManager manager = null;

	/**
	 * 
	 * @return
	 */
	public static ITaskManager getManager() {
		synchronized (TaskManagerFactory.class) {
			if (manager == null) {
				ServiceExtensionLoader<ITaskManager> loader = ServiceExtensionLoader
						.load(ITaskManager.class);
				if (null != loader) {
					List<Throwable> errorList = loader.getErrorList();
					if (errorList.size() > 0) {
						logger.error("The Factory mistakes, prompt error ["
								+ ExceptionUtil.getCauseMessage(errorList
										.get(0)) + "]");
					} else {
						Iterator<ITaskManager> iterator = loader.iterator();
						while (iterator.hasNext()) {
							return manager = iterator.next();
						}
					}
				}
			}
		}
		return manager;
	}

}
