/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.factory;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.manager.IOrderProcessor;

/**
 * 订单处理器工厂. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class OrderProcessorFactory {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(OrderProcessorFactory.class);
	
	private static Map<String, IOrderProcessor> processors = new ConcurrentHashMap<String, IOrderProcessor>();

	/**
	 * None instance. <br>
	 */
	private OrderProcessorFactory() {
		super();
	}
	
	/**
	 * init. <br>
	 */
	private static void init() {
		processors = new ConcurrentHashMap<String, IOrderProcessor>();
		ServiceLoader<IOrderProcessor> loader = ServiceLoader.load(IOrderProcessor.class);
		for (IOrderProcessor processor : loader) {
			if (processors.containsKey(processor.getType())) {
				logger.warn("The implementation class conflict for type [" + processor.getType() + "], use " 
						+ processors.get(processor.getType()).getClass().getName() + " as default, and ignore "
						+ processor.getClass().getName() + ".");
				return;
			}
			processors.put(processor.getType(), processor);
		}
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static IOrderProcessor getProcessor(String type) {
		if (null == processors || processors.isEmpty()) {
			init();
		}
		return processors.get(type);
	}

}
