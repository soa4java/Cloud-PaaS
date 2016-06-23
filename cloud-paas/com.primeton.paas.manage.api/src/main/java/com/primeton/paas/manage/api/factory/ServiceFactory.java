/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ServiceFactory {
	
	private static Map<String, IService> templates = new ConcurrentHashMap<String, IService>();

	private ServiceFactory() {}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IService> T newInstance(String type) {
		if (StringUtil.isEmpty(type)) {
			return null;
		}
		if (null == templates || templates.isEmpty()) {
			init();
		}
		IService template = templates.get(type);
		try {
			return null == template ? null : (T) template.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * init. <br>
	 */
	private static void init() {
		if (null == templates) {
			templates = new ConcurrentHashMap<String, IService>();
		}
		try {
			ServiceLoader<IService> loader = ServiceLoader.load(IService.class);
			if (loader != null) {
				for (IService service : loader) {
					templates.put(service.getType(), service);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T extends IService> String getType(Class<T> clazz) {
		if (null == clazz) {
			return null;
		}
		for (IService service : templates.values()) {
			if (clazz == service.getClass()) {
				return service.getType();
			}
			if (clazz.getName().equals(service.getClass().getName())) {
				return service.getType();
			}
		}
		try {
			return clazz.newInstance().getType();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
	
}
