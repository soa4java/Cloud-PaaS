/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.OrderManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.manager.IOrderProcessor;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.util.StringUtil;
import com.sun.xml.stream.buffer.AbstractProcessor;

/**
 * 订单处理其抽象实现. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public abstract class AbstractOrderProcessor implements IOrderProcessor {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(AbstractProcessor.class);
	
	private static IOrderManager orderManager = OrderManagerFactory.getManager();
	
	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();
	
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager();

	/**
	 * Default. <br>
	 */
	public AbstractOrderProcessor() {
		super();
	}
	
	/**
	 * 
	 * @param order
	 * @param itemId
	 */
	protected void performOk(Order order, String itemId) {
		if (null == order || StringUtil.isEmpty(order.getOrderId())) {
			return;
		}
		if (StringUtil.isNotEmpty(itemId)) {
			OrderItem item = getItemById(order, itemId);
			if (null != item && OrderItem.ITEM_STATUS_FAILED == item.getItemStatus()) {
				order.setOrderStatus(Order.ORDER_STATUS_FAILED);
				order.setFinishTime(new Date());
				order.setNotes("Order process error, cause : item [" + item + "] process error.");
				logger.error(order.getNotes());
				getOrderManager().update(order);
				return;
			}
		}
		List<OrderItem> items = order.getItemList();
		if (null == items || items.isEmpty()) {
			order = getOrderManager().getOrderWithItems(order.getOrderId());
			items = order.getItemList();
		}
		if (null == items || items.isEmpty()) {
			order.setOrderStatus(Order.ORDER_STATUS_SUCCEED);
			order.setFinishTime(new Date());
			order.setNotes("WARN, Order no need process, none item information.");
			logger.warn(order.getNotes());
			getOrderManager().update(order);
			return;
		}
		int count = 0;
		StringBuffer errorMessage = new StringBuffer();
		for (OrderItem item : items) {
			if (item == null) {
				continue;
			}
			if (item.getItemStatus() == OrderItem.ITEM_STATUS_SUCCEED) {
				count ++;
			} else if (item.getItemStatus() == OrderItem.ITEM_STATUS_FAILED) {
				errorMessage.append("[").append(item.getItemStatus()).append(",")
						.append(item.getItemId()).append("]").append(",");
			}
		}
		if (count == items.size()) {
			order.setOrderStatus(Order.ORDER_STATUS_SUCCEED);
			order.setFinishTime(new Date());
			order.setNotes("Order process success!");
			getOrderManager().update(order);
			logger.info("Order [" + order.getOrderType() + ", " + order.getOrderId() + "] process success.");
			return;
		}
		if (errorMessage.length() > 0) {
			order.setOrderStatus(Order.ORDER_STATUS_FAILED);
			order.setFinishTime(new Date());
			String message = "Order process error items : " + errorMessage;
			logger.error(message);
			if (message.length() > 500) {
				message = message.substring(0, 500) + " ..."; //$NON-NLS-1$
			}
			order.setNotes(message);
			getOrderManager().update(order);
			return;
		}
	}
	
	/**
	 * 
	 * @param order
	 */
	protected void performOk(Order order) {
		performOk(order, null);
	}
	
	/**
	 * 
	 * @param itemId
	 * @return
	 */
	protected OrderItem getItemById(String itemId) {
		if (StringUtil.isEmpty(itemId)) {
			return null;
		}
		return getOrderManager().getOrderItem(itemId);
	}

	/**
	 * 
	 * @param order
	 * @param itemId
	 * @return
	 */
	protected OrderItem getItemById(Order order, String itemId) {
		if (StringUtil.isEmpty(itemId)) {
			return null;
		}
		if (order == null) {
			return getItemById(itemId);
		}
		// 如果是完整的订单对象
		List<OrderItem> items = order.getItemList();
		if (null != items && !items.isEmpty()) {
			for (OrderItem item : items) {
				if (itemId.equals(item.getItemId())) {
					return item;
				}
			}
			return null;
		}
		// 否则从数据库中获取
		return getItemById(itemId);
	}
	
	/**
	 * 
	 * @param order
	 * @param itemType
	 * @return
	 */
	protected OrderItem getItemByType(Order order, String itemType) {
		if (order == null || StringUtil.isEmpty(itemType)) {
			return null;
		}
		List<OrderItem> itemList = order.getItemList();
		if (null == itemList || itemList.isEmpty()) {
			order = getOrderManager().getOrderWithItems(order.getOrderId());
			itemList = null == order ? null : order.getItemList();
		}
		if (null == itemList || itemList.isEmpty()) {
			return null;
		}
		for (OrderItem item : itemList) {
			if (itemType.equals(item.getItemType())) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param item
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	protected String getItemValue(OrderItem item, String property,
			String defaultValue) {
		if (null == item || StringUtil.isEmpty(property)) {
			return defaultValue;
		}
		List<OrderItemAttr> attrList = item.getAttrList();
		if (attrList == null || attrList.isEmpty()) {
			return defaultValue;
		}
		for (OrderItemAttr attr : attrList) {
			if (property.equals(attr.getAttrName())) {
				return attr.getAttrValue();
			}
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param item
	 * @param property
	 * @return
	 */
	protected String getItemValue(OrderItem item, String property) {
		return getItemValue(item, property, null);
	}
	
	/**
	 * 
	 * @param item
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	protected long getItemValue(OrderItem item, String property,
			long defaultValue) {
		String value = getItemValue(item, property, String.valueOf(defaultValue));
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	/**
	 * 
	 * @param item
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	protected int getItemValue(OrderItem item, String property,
			int defaultValue) {
		String value = getItemValue(item, property, String.valueOf(defaultValue));
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	/**
	 * 
	 * @param item
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	protected byte getItemValue(OrderItem item, String property,
			byte defaultValue) {
		String value = getItemValue(item, property, String.valueOf(defaultValue));
		try {
			return Byte.parseByte(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	/**
	 * 
	 * @param item
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	protected boolean getItemValue(OrderItem item, String property,
			boolean defaultValue) {
		String value = getItemValue(item, property, String.valueOf(defaultValue));
		return Boolean.parseBoolean(value);
	}
	
	/**
	 * 
	 * @return
	 */
	protected static IOrderManager getOrderManager() {
		return orderManager = (null == orderManager) ? OrderManagerFactory
				.getManager() : orderManager;
	}

	/**
	 * 
	 * @return
	 */
	protected static IServiceQuery getServiceQuery() {
		return serviceQuery = (null == serviceQuery) ? ServiceManagerFactory
				.getServiceQuery() : serviceQuery;
	}

	/**
	 * 
	 * @return
	 */
	protected static IClusterManager getClusterManager() {
		return clusterManager = (null == clusterManager) ? ClusterManagerFactory
				.getManager() : clusterManager;
	}
	
}
