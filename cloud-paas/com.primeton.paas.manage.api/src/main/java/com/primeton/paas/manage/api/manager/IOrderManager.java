/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import java.util.Date;

import com.primeton.paas.manage.api.exception.OrderException;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IOrderManager {

	/**
	 * 
	 * @param order
	 * @return
	 */
	Order add(Order order);
	
	/**
	 * 
	 * @param orderId
	 */
	void remove(String orderId);
	
	/**
	 * 
	 * @param order
	 */
	void update(Order order);
	
	/**
	 * 
	 * @param orderId
	 * @param status
	 * @param notes
	 * @param handler
	 */
	void updateStatus(String orderId, int status, String notes, String handler);
	
	/**
	 * 
	 * @param itemId
	 * @param status
	 */
	void updateItemStatus(String itemId, int status);
	
	/**
	 * 
	 * @param item
	 */
	void updateItem(OrderItem item);
	
	
	/**
	 * 
	 * @param item
	 */
	void updateItemAttrs(OrderItem item);
	
	
	/**
	 * 
	 * @param orderId
	 * @return
	 */
	Order getOrder(String orderId);
	
	/**
	 * 
	 * @param orderId
	 * @return
	 */
	Order getOrderWithItems(String orderId);
	
	/**
	 * 
	 * @param order
	 * @param submitTimeBegin
	 * @param submitTimeEnd
	 * @param handleTimeBegin
	 * @param handleTimeEnd
	 * @param pageCond
	 * @return
	 */
	Order[] getOrders(Order order, Date submitTimeBegin, Date submitTimeEnd,
			Date handleTimeBegin, Date handleTimeEnd, IPageCond pageCond);
	
	/**
	 * 
	 * @param orderId
	 * @return
	 */
	OrderItem[] getOrderItems(String orderId);
	
	/**
	 * 
	 * @param orderId
	 * @param itemType
	 * @return
	 */
	OrderItem getItemByType(String orderId, String itemType);
	
	/**
	 * 
	 * @param orderId
	 * @param itemType
	 * @param attrName
	 * @return
	 */
	OrderItemAttr getAttrByName(String orderId, String itemType, String attrName);
	
	/**
	 * 
	 * @param itemId
	 * @return
	 */
	OrderItem getOrderItem(String itemId);
	
	/**
	 * 
	 * @param itemId
	 * @return
	 */
	OrderItemAttr[] getOrderItemAttrs(String itemId);
	
	/**
	 * 
	 * @param orderId
	 * @throws OrderException
	 */
	void handle(String orderId) throws OrderException;
	
	/**
	 * 
	 * @param orderId
	 * @param itemId
	 * @throws OrderException
	 */
	void handleItem(String orderId, String itemId) throws OrderException;
	
}
