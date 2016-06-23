/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.manager;

import com.primeton.paas.manage.api.exception.OrderException;
import com.primeton.paas.manage.api.model.Order;

/**
 * 订单处理器. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IOrderProcessor {
	
	/**
	 * 处理器类型, 与订单类型映射. <br>
	 * 
	 * @see com.primeton.paas.manage.api.model.Order#getOrderType()
	 * 
	 * @return 处理器类型
	 */
	String getType();

	/**
	 * 处理订单. <br>
	 * 
	 * @param order 订单
	 * @throws OrderException
	 */
	void process(Order order) throws OrderException;
	
	/**
	 * 仅仅处理某个订单项. <br>
	 * 
	 * @param order 订单
	 * @param itemId 订单项标识
	 * @throws OrderException
	 */
	void process(Order order, String itemId) throws OrderException;
	
}
