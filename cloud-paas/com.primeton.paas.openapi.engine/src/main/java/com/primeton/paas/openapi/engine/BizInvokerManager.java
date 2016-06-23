/**
 * 
 */
package com.primeton.paas.openapi.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class BizInvokerManager {
	
	private static BizInvokerManager instance = new BizInvokerManager();

	private Map<String/* bizCode */, IBizInvoker> bizInvokers = new HashMap<String, IBizInvoker>();

	private BizInvokerManager() {
		try {
			ServiceExtensionLoader<IBizInvoker> loader = ServiceExtensionLoader.load(IBizInvoker.class);
			for (Iterator<IBizInvoker> it = loader.iterator(); it.hasNext();) {
				IBizInvoker invoker = it.next();
				bizInvokers.put(invoker.getBizCode(), invoker);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	};

	public static BizInvokerManager getInstance() {
		if (instance == null)
			instance = new BizInvokerManager();
		return instance;
	}

	public static void clear() {
		if (instance != null) {
			if (instance.bizInvokers != null)
				instance.bizInvokers.clear();
			instance = null;
		}
	}

	public IBizInvoker getBizInvoker(String bizCode) {
		return bizInvokers.get(bizCode);
	}
	
}
