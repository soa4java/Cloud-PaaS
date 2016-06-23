/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.util.ExceptionUtil;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ClusterManagerFactory {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(ClusterManagerFactory.class);
	
	private static Map<String, IClusterManager> managers = new ConcurrentHashMap<String, IClusterManager>();
	
	private ClusterManagerFactory() {}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static synchronized IClusterManager getManager(String type) {
		if (StringUtil.isEmpty(type)) {
			type = IClusterManager.DEFAULT_TYPE;
		}
		if (null == managers || managers.isEmpty()) {
			load();
		}
		IClusterManager manager = managers.get(type);
		return null == manager ? getManager(IClusterManager.DEFAULT_TYPE) : manager;
	}
	
	/**
	 * 
	 * @return Default
	 */
	public static IClusterManager getManager() {
		return getManager(IClusterManager.DEFAULT_TYPE);
	}
	
	private static void load() {
		if (null == managers) {
			managers = new ConcurrentHashMap<String, IClusterManager>();
		}
		try {
			ServiceExtensionLoader<IClusterManager> loader = ServiceExtensionLoader
					.load(IClusterManager.class);
			if (loader != null) {
				List<Throwable> errorList = loader.getErrorList();
				if (errorList.size() > 0) {
					logger.error(ExceptionUtil.getCauseMessage(errorList.get(0)));
				} else {
					Iterator<IClusterManager> iterator = loader.iterator();
					while (iterator.hasNext()) {
						IClusterManager manager = iterator.next();
						managers.put(manager.getType(), manager);
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	
}
