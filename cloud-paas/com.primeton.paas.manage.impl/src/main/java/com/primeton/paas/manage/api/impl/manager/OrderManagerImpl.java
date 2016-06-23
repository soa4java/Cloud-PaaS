/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.OrderException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.OrderProcessorFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultOrderDao;
import com.primeton.paas.manage.api.impl.util.ServiceOpenUtil;
import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.manager.IOrderProcessor;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.util.RandomUtil;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class OrderManagerImpl implements IOrderManager {

	private static ILogger logger = ManageLoggerFactory.getLogger(OrderManagerImpl.class);
	
	private static DefaultOrderDao orderDao = DefaultOrderDao.getInstance();
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IOrderManager#add(com.primeton.paas.manage.api.model.Order)
	 */
	public Order add(Order order) {
		if (order == null) {
			return null;
		}
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		List<OrderItemAttr> itemAttrList = new ArrayList<OrderItemAttr>();
		
		orderItemList = order.getItemList();	
		
		try{
			String orderId = RandomUtil.generateId();
			order.setOrderId(orderId);
			order.setOrderStatus(Order.ORDER_STATUS_SUBMITED);
			if (orderItemList != null && !orderItemList.isEmpty()) {
				for (OrderItem item : orderItemList) {
					String itemId = RandomUtil.generateId();
					item.setOrderId(orderId);
					item.setItemId(itemId);
					
					List<OrderItemAttr> t_attrs = item.getAttrList();
					if (t_attrs != null && t_attrs.size()>0) {
						for(OrderItemAttr att : t_attrs){
							String attrId = RandomUtil.generateId();
							att.setId(attrId);
							att.setItemId(item.getItemId());
						}
						itemAttrList.addAll(t_attrs);
					}
				}
			}
			
			orderDao.addOrder(order);
			
			if(orderItemList!= null && !orderItemList.isEmpty()){
				orderDao.addOrderItems(orderItemList);
			}
			
			if(itemAttrList!= null && !itemAttrList.isEmpty()){
				orderDao.addItemAttrs(itemAttrList);
			}
		} catch (DaoException e) {
			logger.error(e);
		}
		return order;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IOrderManager#remove(java.lang.String)
	 */
	public void remove(String orderId) {
		if (StringUtil.isEmpty(orderId)) {
			return;
		}
		Order cldOrder = getOrder(orderId);
		if (cldOrder == null) {
			return;
		}
		try {
			// del itemAttrs
			orderDao.delItemAttrs(orderId);

			// del orderItems
			orderDao.delOrderItems(orderId);

			// del order
			orderDao.delOrder(orderId);
		} catch (DaoException e) {
			logger.error(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IOrderManager#update(com.primeton.paas.manage.api.model.Order)
	 */
	public void update(Order order) {
		if (order == null || StringUtil.isEmpty(order.getOrderId())) {
			return;
		}
		try {
			orderDao.updateOrder(order);
		} catch (DaoException e) {
			logger.error(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IOrderManager#updateStatus(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	public void updateStatus(String orderId, int status, String notes,
			String handler) {
		if (StringUtil.isEmpty(orderId)) {
			return;
		}
		Order order = getOrder(orderId);
		if (order == null) {
			return;
		}
		order.setOrderStatus(status);
		order.setNotes(notes);
		order.setHandler(handler);

		update(order);
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IOrderManager#updateItemStatus(java.lang.String, int)
	 */
	public void updateItemStatus(String itemId, int status) {
		if(StringUtil.isEmpty(itemId) || status < 0 ) {
			return;
		}
		OrderItem item = getOrderItem(itemId);
		if(item == null) {
			logger.info("can not find item info {itemId:" + itemId +"}.");
			return;
		}
		item.setItemStatus(status);
		try {
			orderDao.updateItemStatus(item);
		} catch (DaoException e) {
			logger.error(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IOrderManager#updateItem(com.primeton.paas.manage.api.model.OrderItem)
	 */
	public void updateItem(OrderItem item) {
		if (item == null || item.getItemId() == null) {
			return;
		}
		try {
			orderDao.updateItem(item);
		} catch (DaoException e) {
			logger.error(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IOrderManager#updateItemAttrs(com.primeton.paas.manage.api.model.OrderItem)
	 */
	public void updateItemAttrs(OrderItem item) {
		if (item == null || StringUtil.isEmpty(item.getItemId())) {
			return;
		}
		List<OrderItemAttr> attrList = item.getAttrList();
		if (attrList == null || attrList.isEmpty()) {
			logger.info("Update item attrs cancelled, attributes is empty.{itemId:"+item.getItemId());
		}
		try {
			orderDao.updateItemAttrs(attrList);
		} catch (DaoException e) {
			logger.error(e);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IOrderManager#getOrderItem(java.lang.String)
	 */
	public OrderItem getOrderItem(String itemId) {
		if (StringUtil.isEmpty(itemId)) {
			return null;
		}
		OrderItem item = null;
		try {
			item = orderDao.getOrderItem(itemId);
		} catch (DaoException e) {
			logger.error(e);
		}
		return item;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IOrderManager#getOrder(java.lang.String)
	 */
	public Order getOrder(String orderId) {
		if (StringUtil.isEmpty(orderId)) {
			return null;
		}
		Order order = null;
		try {
			order = orderDao.getOrder(orderId);
		} catch (DaoException e) {
			logger.error(e);
		}
		if (order == null) {
			return null;
		}
		Date handleBegin = order.getHandleTime();
		Date finishDate = order.getFinishTime();
		if (handleBegin != null && finishDate != null) {
			long period = (finishDate.getTime() - handleBegin.getTime()) / 1000;
			order.setHandlePeriod(period + "  sec.");
		} else {
			order.setHandlePeriod(""); // Processing
		}
		return order;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IOrderManager#getOrderWithItems(java.lang.String)
	 */
	public Order getOrderWithItems(String orderId) {
		if (StringUtil.isEmpty(orderId)) {
			return null;
		}
		Order order = getOrder(orderId);
		if (order == null)
			return null;
		
		OrderItem[] items = getOrderItems(orderId);
		
		if (items == null || items.length < 1) {
			order.setItemList(new ArrayList<OrderItem>());
			return order;
		}
		
		List<OrderItem> itemList = Arrays.asList(items);
		for (OrderItem t_item : itemList) {
			OrderItemAttr[] itemAttrs = getOrderItemAttrs(t_item.getItemId());
			if (itemAttrs == null || itemAttrs.length < 1) {
				continue;
			}
			List<OrderItemAttr> attrList = Arrays.asList(itemAttrs);
			t_item.setAttrList(attrList);
		}
		order.setItemList(itemList);
		
		return order;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IOrderManager#getOrders(com.primeton.paas.manage.api.model.Order, java.util.Date, java.util.Date, java.util.Date, java.util.Date, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public Order[] getOrders(Order order, Date submitTimeBegin,	Date submitTimeEnd, Date handleTimeBegin, Date handleTimeEnd,IPageCond page) {
		Map<String,Object> criteria = new HashMap<String,Object>();
	
		if (order != null) {
			criteria.put("orderId", order.getOrderId() == null ? "" : order //$NON-NLS-1$
					.getOrderId().trim());
			criteria.put("orderType", order.getOrderType()); //$NON-NLS-1$
			criteria.put("orderStatus", order.getOrderStatus()); //$NON-NLS-1$
			criteria.put("owner", order.getOwner()); //$NON-NLS-1$
			criteria.put("handler", order.getHandler() == null ? "" : order //$NON-NLS-1$
					.getHandler().trim());
		}
		
		criteria.put("submitTimeBegin", submitTimeBegin); //$NON-NLS-1$
		criteria.put("submitTimeEnd", submitTimeEnd); //$NON-NLS-1$
		criteria.put("handleTimeBegin", handleTimeBegin); //$NON-NLS-1$
		criteria.put("handleTimeEnd", handleTimeEnd); //$NON-NLS-1$
		
		List<Order> orderList = new ArrayList<Order>();
		try {
			if (page == null) {
				orderList = orderDao.getOrders(criteria);
			} else {
				if (page.getCount() <= 0) {
					page.setCount(orderDao.getOrderCount(criteria));
				}
				orderList = orderDao.getOrders(criteria,page);
			}
		} catch (DaoException e) {
			logger.error(e);
		}
		if (orderList != null && orderList.size() > 0) {
			return orderList.toArray(new Order[orderList.size()]);
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IOrderManager#getOrderItems(java.lang.String)
	 */
	public OrderItem[] getOrderItems(String orderId) {
		if (StringUtil.isEmpty(orderId)) {
			return null;
		}
		List<OrderItem> items = new ArrayList<OrderItem>();
		try {
			items = orderDao.getOrderItems(orderId);
		} catch (DaoException e) {
			logger.error(e);
		}
		if (items == null || items.size() < 1) {
			return null;
		}
		for (OrderItem item : items) {
			Date handleBegin = item == null ? null : item.getHandleTime();
			Date handleEnd = item == null ? null : item.getFinishTime();
			if (handleBegin != null && handleEnd != null) {
				long period = (handleEnd.getTime() - handleBegin.getTime()) / 1000;
				item.setHandlePeriod(period + "  sec.");
			} else {
				item.setHandlePeriod("");
			}
		}
		return items.toArray(new OrderItem[items.size()]);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IOrderManager#getItemByType(java.lang.String, java.lang.String)
	 */
	public OrderItem getItemByType(String orderId, String itemType) {
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(itemType)) {
			return null;
		}
		OrderItem item = null;
		try {
			item = orderDao.getItemByType(orderId, itemType);
		} catch (DaoException e) {
			logger.error(e);
		}
		return item;
	}

	/* 
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IOrderManager#getAttrByName(java.lang.String, java.lang.String, java.lang.String)
	 */
	public OrderItemAttr getAttrByName(String orderId, String itemType,
			String attrName) {
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(attrName)) {
			return null;
		}
		OrderItemAttr attr = null;
		try {
			attr = orderDao.getAttrByName(orderId, itemType, attrName);
		} catch (DaoException e) {
			logger.error(e);
		}
		return attr;
	}
	
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IOrderManager#getOrderItemAttrs(java.lang.String)
	 */
	public OrderItemAttr[] getOrderItemAttrs(String itemId) {
		if (StringUtil.isEmpty(itemId)) {
			return null;
		}
		List<OrderItemAttr> attrs = new ArrayList<OrderItemAttr>();
		try {
			attrs = orderDao.getItemAttrs(itemId);
		} catch (DaoException e) {
			logger.error(e);
		}
		if (attrs != null && attrs.size() > 0) {
			return attrs.toArray(new OrderItemAttr[attrs.size()]);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IOrderManager#handle(java.lang.String)
	 */
	public void handle(String orderId) throws OrderException {
		if (StringUtil.isEmpty(orderId)) {
			return;
		}
		Order order = getOrderWithItems(orderId);
		if (order == null) {
			return;
		}
		
		order.setOrderStatus(Order.ORDER_STATUS_APPROVED);
		order.setNotes("");
		order.setHandleTime(new Date());
		update(order);
		
		// 使用不同类型的订单处理器来处理订单
		IOrderProcessor processor = OrderProcessorFactory.getProcessor(order.getOrderType());
		if (null != processor) {
			processor.process(order);
			return;
		}
		
		// Comments by ZhongWen.Li
		// 以下是历史代码，暂时不做修改
		// FIXME 未来需要重构，全部遵循规范，按照扩展接口开发新的实现
		
		List<OrderItem> items = order.getItemList();
		if (items == null || items.isEmpty()) {
			logger.info("Order {0} items not exists.", new Object[] { order.getOrderId() });
			return;
		}
		
		long begin = System.currentTimeMillis();
		logger.info("Begin process order {0} [{1}].", new Object[] { order.getOrderId(), order.getOrderType() });
		
		
		int orderStatus = Order.ORDER_STATUS_SUCCEED; //status : succeed
		String notes = "The order has been processed."; //$NON-NLS-1$
		String orderType = order.getOrderType();
		
		if (order == null || order.getItemList() == null
				|| order.getItemList().isEmpty()) {
			return;
		}
		OrderItem appItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_APP);
		String appName = OrderHandler.getItemStringValue(appItem, OrderItemAttr.ATTR_APP_NAME, "");
		
		try {
			if (Order.ORDER_TYPE_CREATE_APP.equals(orderType)) {
				// create inner application
				OrderHandler.createApp(order);
			} else if (Order.ORDER_TYPE_DELETE_APP.equals(orderType)) {
				// remove inner application
				OrderHandler.removeApp(appItem,appName);
			} else if (Order.ORDER_TYPE_CREATE_SRV.equals(orderType)) {
				// create service 
				OrderHandler.createService(order);
			} else if (Order.ORDER_TYPE_DELETE_SRV.equals(orderType)) {
				// remove service
				OrderHandler.removeService(order);
			} else if (Order.ORDER_TYPE_STRETCH_STRATEGY.equals(orderType)) {
				// application stretch strategy config
				OrderHandler.stretchStrategy(order);
			} else if (Order.ORDER_TYPE_UPDATE_SRV.equals(orderType)) {
				// update service config
				OrderHandler.modifyService(order);
			}
		} catch (Exception e){
			orderStatus = Order.ORDER_STATUS_FAILED;
			notes = "Order process error:" + e.getMessage();
			logger.error("error:", e);
		}
		
		// update order status
		// updateStatus(orderId, orderStatus, notes, order.getHandler());
		if (!Order.ORDER_TYPE_CREATE_APP.equals(orderType)) {
			notes = notes.length() > 500 ? notes.substring(0, 500) : notes;
			order.setNotes(notes);
			order.setOrderStatus(orderStatus);
			order.setFinishTime(new Date());
			update(order);
		}
		
		long end = System.currentTimeMillis();
		logger.info("Finish process order {" + order.toString() + "}. Time Spent : " + (end - begin) / 1000 + " seconds.");
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IOrderManager#handleItem(java.lang.String, java.lang.String)
	 */
	public void handleItem(String orderId, String itemId)
			throws OrderException {
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(itemId)) {
			return;
		}
		Order order = getOrderWithItems(orderId);
		if (order == null) {
			return;
		}
		List<OrderItem> items = order.getItemList();
		if (items == null || items.isEmpty()) {
			return;
		}
		// 使用不同类型的订单处理器来处理某个订单项
		IOrderProcessor processor = OrderProcessorFactory.getProcessor(order.getOrderType());
		if (null != processor) {
			processor.process(order, itemId);
			return;
		}
		
		OrderItem appItem = OrderHandler.getItemByType(order, OrderItem.ITEM_TYPE_APP);
		String appName = OrderHandler.getItemStringValue(appItem, OrderItemAttr.ATTR_APP_NAME, "");
		
		
		long begin = System.currentTimeMillis();
		logger.info("Begin process Item {orderId:" +orderId +",itemId:"+itemId  + "}.");
		
		OrderItem item = getOrderItem(itemId);
		if (item == null) {
			logger.error("Process item cancelled, can not find item info.{itemId:"+itemId+"}.");
		}
			
		String itemType = item.getItemType();
		String orderType = order.getOrderType();
		
		try {
			if (Order.ORDER_TYPE_CREATE_APP.equals(orderType)) {
				if (OrderItem.ITEM_TYPE_APP.equals(itemType)) {
					//create inner application
					ServiceOpenUtil.createInnerApplication(order);
				} else {
					//create services
					OrderHandler.doCreateService(order, item, appName);
				}
			} else if (Order.ORDER_TYPE_CREATE_SRV.equals(orderType)) {
				//create services
				OrderHandler.doCreateService(order, item, null);
			} else if (Order.ORDER_TYPE_STRETCH_STRATEGY.equals(orderType)) {
				//stretch strategy config
				OrderHandler.stretchStrategy(order);
			} else if (Order.ORDER_TYPE_DELETE_APP.equals(orderType)) {
				//remove inner application & remove outer application
				OrderHandler.removeApp(appItem,appName);
			} else if(Order.ORDER_TYPE_DELETE_SRV.equals(orderType)) {
				//remove service
				OrderHandler.doRemoveService(item);	
			} 
		} catch (Exception e) {
			logger.error(e);
			// update orderItem status
			item.setItemStatus(OrderItem.ITEM_STATUS_FAILED);
			item.setFinishTime(new Date());
			updateItem(item);
		}
		
		long end = System.currentTimeMillis();
		logger.info("Finish process order item  {id:" + itemId + ",type:"
				+ itemType + "}. Time Spent : " + (end - begin) / 1000
				+ " seconds.");
		return;
	}
	
}	
	
