/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.Calendar;
import java.util.Date;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.platform.service.audit.OrderManagerUtil;
import com.primeton.paas.manage.api.factory.OrderManagerFactory;
import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.model.Order;

/**
 * 订单管理工具类. <br>
 * 
 * @author YanPing.Li (mailto:liyp@primeton.com)
 */
public class OrderManagerUtils {

	private static ILogger logger = LoggerFactory.getLogger(OrderManagerUtils.class);

	private static IOrderManager orderManager = OrderManagerFactory
			.getManager();

	/**
	 * @param order
	 * @return
	 */
	public static Order createOrder(Order order) {
		if (null == order) {
			return null;
		}
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		// submitTime & beginTime & endTime
		order.setSubmitTime(new Date());
		order.setBeginTime(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, 3);
		order.setEndTime(cal.getTime());

		order.setOwner(user.getUserId());
		order.setOrderStatus(Order.ORDER_STATUS_SUBMITED); // 已提交
		order.setOrderType(Order.ORDER_TYPE_CREATE_SRV); // 服务创建

		Order newOrder = null;
		try {
			newOrder = orderManager.add(order);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}

		String orderId = newOrder == null ? "" : newOrder.getOrderId();
		if (orderId == null) {
			return null;
		}
		// handle order
		orderManager.updateStatus(orderId, Order.ORDER_STATUS_APPROVED, null,
				user.getUserId());
		OrderManagerUtil.asynHandleOrder(orderId);
		return newOrder;
	}

	/**
	 * @param order
	 * @return
	 */
	public static Order createConfigOrder(Order order) {
		if (null == order) {
			return null;
		}
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String currentUser = user.getUserId();

		// submitTime & beginTime & endTime
		order.setSubmitTime(new Date());
		order.setBeginTime(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, 3);
		order.setEndTime(cal.getTime());

		order.setOwner(currentUser);
		order.setOrderStatus(Order.ORDER_STATUS_SUBMITED);// 已提交
		order.setOrderType(Order.ORDER_TYPE_UPDATE_SRV); // 服务配置

		Order newOrder = null;
		try {
			newOrder = orderManager.add(order);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}

		String orderId = newOrder == null ? "" : newOrder.getOrderId();
		if (orderId == null) {
			return null;
		}
		// handle order
		orderManager.updateStatus(orderId, Order.ORDER_STATUS_APPROVED, null,
				currentUser);
		// async process
		OrderManagerUtil.asynHandleOrder(orderId);
		return newOrder;
	}
	
	/**
	 * 提交订单. <br>
	 * 
	 * @param order 订单
	 * @param autoApprove 是否自动审批
	 * @return 订单(with ID)
	 */
	public static Order submitOrder(Order order, boolean autoApprove) {
		if (null == order) {
			return null;
		}
		String owner = DataContextManager.current().getMUODataContext()
				.getUserObject().getUserId();

		// submitTime & beginTime & endTime
		order.setSubmitTime(new Date());
		order.setBeginTime(new Date());
		order.setEndTime(new Date());

		order.setOwner(owner);
		order.setOrderStatus(Order.ORDER_STATUS_SUBMITED); // 已提交

		Order newOrder = null;
		try {
			newOrder = orderManager.add(order);
			logger.info("Save order {0} success.", new Object[] { newOrder });
		} catch (Throwable e) {
			logger.error(e);
			return null;
		}
		if (newOrder == null) {
			return null;
		}
		// 如果自动审批订单
		if (autoApprove) {
			orderManager.updateStatus(newOrder.getOrderId(),
					Order.ORDER_STATUS_APPROVED, null, owner);
			OrderManagerUtil.asynHandleOrder(newOrder.getOrderId());
		}
		return newOrder;
	}
	
}
