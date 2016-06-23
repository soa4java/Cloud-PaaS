/**
 *
 */
package com.primeton.paas.console.platform.service.audit;

import java.util.Date;
import java.util.Map;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.exception.OrderException;
import com.primeton.paas.manage.api.exception.WebAppException;
import com.primeton.paas.manage.api.factory.OrderManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 订单管理
 * 
 * @author liming(li-ming@primeton.com)
 *
 */
public class OrderManagerUtil {

	// 订单管理类实例
	private static IOrderManager orderManager = OrderManagerFactory
			.getManager();

	// 日志
	private static ILogger logger = LoggerFactory.getLogger(OrderManagerUtil.class);

	private static IWebAppManager appManager = WebAppManagerFactory
			.getManager();

	/**
	 * 
	 * 查询订单信息（分页）
	 * 
	 * @param criteriaType
	 * @param page
	 * @return
	 */
	public static Order[] getOrders(Order criteria, Date submitTimeBegin,
			Date submitTimeEnd, Date handleTimeBegin, Date handleTimeEnd,
			PageCond page) {
		return orderManager.getOrders(criteria, submitTimeBegin, submitTimeEnd,
				handleTimeBegin, handleTimeEnd, page);
	}

	/**
	 * 批量删除订单
	 * 
	 * @param orders
	 * @return
	 */
	public static boolean removeOrders(Order[] orders) {
		if (orders == null || orders.length < 1) {
			return true;
		}
		for (Order cur : orders) {
			String orderId = cur.getOrderId();
			removeOrder(orderId);
		}
		return true;
	}

	/**
	 * 批量删除订单
	 * 
	 * @param orders
	 * @return
	 */
	public static boolean removeOrders(String[] ids) {
		if (ids == null || ids.length < 1) {
			return true;
		}
		for (String orderId : ids) {
			removeOrder(orderId);
		}
		return true;
	}

	/**
	 * 删除单个订单
	 * 
	 * @param orderID
	 * @return
	 */
	public static boolean removeOrder(String orderID) {
		logger.info("Remove order {0}.", new Object[] { orderID });
		if (StringUtil.isEmpty(orderID)) {
			return true;
		}
		orderManager.remove(orderID);
		return true;
	}

	/**
	 * 通过订单编号查询订单基本信息
	 * 
	 * @param orderId
	 * @return
	 */
	public static Order getOrder(String orderId) {
		try {
			return orderManager.getOrder(orderId);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 通过订单编号查询订单状态信息
	 * 
	 * @param orderId
	 * @return
	 */
	public static int getOrderStatus(String orderId) {
		try {
			Order order = orderManager.getOrder(orderId);
			if (order != null) {
				return order.getOrderStatus();
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return 0;
	}

	/**
	 * 通过订单编号查询订单项
	 * 
	 * @param orderId
	 * @return
	 */
	public static OrderItem[] getOrderItems(String orderId) {
		try {
			return orderManager.getOrderItems(orderId);
		} catch (Exception e) {
			logger.error(e);
		}
		return new OrderItem[0];
	}

	/**
	 * 通过订单项编号查询订单项属性详情
	 * 
	 * @param itemId
	 * @return
	 */
	public static OrderItemAttr[] getOrderItemAttrs(String itemId) {
		try {
			return orderManager.getOrderItemAttrs(itemId);
		} catch (Exception e) {
			logger.error(e);
		}
		return new OrderItemAttr[0];
	}

	/**
	 * 通过订单编号查询订单详细信息（订单项详情）
	 * 
	 * @param orderId
	 * @return
	 */
	public static Order getOrderWithItems(String orderId) {
		try {
			return orderManager.getOrderWithItems(orderId);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 查询订单项的状态
	 * 
	 * @param itemId
	 * @return
	 */
	public static int getItemStatus(String itemId) {
		try {
			OrderItem item = orderManager.getOrderItem(itemId);
			if (item != null) {
				return item.getItemStatus();
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return OrderItem.ITEM_STATUS_INIT;
	}

	/**
	 * 同意订单申请
	 * 
	 * @param orderId
	 * @param handler
	 * @return
	 */
	public static boolean approveOrder(String orderId, String handler) {
		Order order = orderManager.getOrder(orderId);
		if (order == null)
			return false;
		orderManager.updateStatus(orderId, Order.ORDER_STATUS_APPROVED, null,
				handler);
		logger.info("Async proccess order {0}, handler {1}.", new Object[] { order, handler });
		asynHandleOrder(orderId);
		return true;
	}

	/**
	 * 
	 * @param orderId
	 */
	public static void asynHandleOrder(final String orderId) {
		Thread t = new Thread() {
			public void run() {
				logger.info("Begin process order {0}.", new Object[] { orderId });
				long begin = System.currentTimeMillis();
				try {
					orderManager.handle(orderId);
				} catch (OrderException e) {
					logger.error(e);
				}
				
				long end = System.currentTimeMillis();
				logger.info("End process order {0}, spent {1} ms.", new Object[] { orderId, end-begin });
				
			};
		};
		t.setName("OrderProcessTask-" + orderId); //$NON-NLS-1$
		t.setDaemon(true);
		t.start();
	}

	/**
	 * 拒绝订单申请
	 * 
	 * @param orderId
	 * @param notes
	 * @param handler
	 * @return
	 */
	public static boolean rejectOrder(String orderId, String notes,
			String handler) {
		logger.info("Reject order {0}.", new Object[] { orderId });

		Order order = orderManager.getOrder(orderId);
		if (order == null)
			return false;

		try {
			orderManager.updateStatus(orderId, Order.ORDER_STATUS_REJECTED,
					notes, handler);
			Order orderNew = orderManager.getOrder(orderId);
			orderNew.setHandleTime(new Date());
			orderManager.update(orderNew);

			OrderItemAttr attr = orderManager.getAttrByName(orderId,
					OrderItem.ITEM_TYPE_APP, OrderItemAttr.ATTR_APP_NAME);
			if (null != attr && null != attr.getAttrValue()
					&& !"".equals(attr.getAttrValue())) {
				String appName = attr.getAttrValue();
				WebApp app = appManager.get(appName);
				if (app != null) {
					Map<String, String> appAttr = app.getAttributes();
					int appState = appAttr.get("state") == null ? 0 : Integer
							.parseInt(appAttr.get("state"));
					if (WebApp.STATE_WAIT_DESTORY == appState) {
						appAttr.put("state", String.valueOf(WebApp.STATE_OPEND));
						app.setAttributes(appAttr);
						try {
							appManager.update(app);
						} catch (WebAppException e) {
							logger.error(e);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException("Order [ " + orderId + " ] process error.", e);
		}
		return true;
	}

	/**
	 * 指定订单项重新执行<br>
	 * 
	 * @param orderId
	 * @param itemId
	 * @return
	 */
	public static boolean redoItem(String orderId, String itemId) {
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(itemId)) {
			return false;
		}
		asynRedoItem(orderId, itemId);
		return true;
	}

	/**
	 * 
	 * @param orderId
	 * @param itemId
	 */
	public static void asynRedoItem(String orderId, String itemId) {
		logger.info("Begin redo order {0} item {1}.", new Object[] { orderId, itemId });
		long begin = System.currentTimeMillis();
		try {
			orderManager.handleItem(orderId, itemId);
		} catch (OrderException e) {
			logger.info("Redo order {0} item {1} error.", new Object[] { orderId, itemId }, e);
		}
		long end = System.currentTimeMillis();
		logger.info("End redo order {0} item {1}, spent {2} ms.", new Object[] { orderId, itemId, end - begin });
	}

}
