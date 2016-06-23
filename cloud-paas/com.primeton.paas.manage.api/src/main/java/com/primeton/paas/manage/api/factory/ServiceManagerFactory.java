/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import com.primeton.paas.manage.api.manager.IServiceManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ServiceManagerFactory {
	
	private static IServiceQuery serviceQuery;
	
	private static Map<String, IServiceManager> serviceManagers = new ConcurrentHashMap<String, IServiceManager>();

	private ServiceManagerFactory() {}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IServiceManager> T getManager(String type) {
		if (StringUtil.isEmpty(type)) {
			type = IServiceManager.DEFAULT_MANAGER;
		}
		if (null == serviceManagers || serviceManagers.isEmpty()) {
			load();
		}
		IServiceManager manager = serviceManagers.get(type);
		return (T) manager;
	}
	
	/**
	 * 
	 */
	private static void load() {
		if (null == serviceManagers) {
			serviceManagers = new ConcurrentHashMap<String, IServiceManager>();
		}
		try {
			ServiceLoader<IServiceManager> loader = ServiceLoader.load(IServiceManager.class);
			if (loader != null) {
				for (IServiceManager manager : loader) {
					serviceManagers.put(manager.getType(), manager);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static IServiceManager getManager() {
		return getManager(IServiceManager.DEFAULT_MANAGER);
	}
	
	/**
	 * 
	 * @return
	 */
	public static IServiceQuery getServiceQuery() {
		if (null != serviceQuery) {
			return serviceQuery;
		}
		try {
			ServiceLoader<IServiceQuery> loader = ServiceLoader.load(IServiceQuery.class);
			if (loader != null) {
				for (IServiceQuery obj : loader) {
					if (obj != null) {
						serviceQuery = obj;
						break;
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return serviceQuery;
	}

}
