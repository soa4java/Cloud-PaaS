/**
 * 
 */
package com.primeton.paas.manage.api.factory;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ClusterFactory {
	
	private static Map<String, ICluster> templates = new ConcurrentHashMap<String, ICluster>();

	private ClusterFactory() {}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static ICluster newInstance(String type) {
		if (StringUtil.isEmpty(type)) {
			return null;
		}
		if (null == templates || templates.isEmpty()) {
			init();
		}
		ICluster template = templates.get(type);
		try {
			return null == template ? null : template.getClass().newInstance();
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
			templates = new ConcurrentHashMap<String, ICluster>();
		}
		try {
			ServiceLoader<ICluster> loader = ServiceLoader.load(ICluster.class);
			if (loader != null) {
				for (ICluster cluster : loader) {
					templates.put(cluster.getType(), cluster);
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
	public static <T extends ICluster> String getType(Class<T> clazz) {
		if (null == clazz) {
			return null;
		}
		for (ICluster cluster : templates.values()) {
			if (clazz == cluster.getClass()) {
				return cluster.getType();
			}
			if (clazz.getName().equals(cluster.getClass().getName())) {
				return cluster.getType();
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
