/**
 *
 */
package com.primeton.paas.console.app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import net.sf.json.JSONObject;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
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
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/myOrder")
public class MyOrderResource {
	
	private static ILogger logger = LoggerFactory.getLogger(MyOrderResource.class);
	
	private static IOrderManager orderManager = OrderManagerFactory
			.getManager();

	private static IWebAppManager appManager = WebAppManagerFactory
			.getManager();

	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/list")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response list(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		Order criteria = new Order();
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String currentUser = user.getUserId();
		criteria.setOwner(currentUser);

		Date submitTimeBegin = null;
		Date submitTimeEnd = null;

		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {

				criteria.setOrderId(jsObj.getString("orderId")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("orderType"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.setOrderType(jsObj.getString("orderType")); //$NON-NLS-1$
				}
				if (!"defaultValue".equals(jsObj.getString("orderStatus"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.setOrderStatus(jsObj.getInt("orderStatus")); //$NON-NLS-1$
				}

				SimpleDateFormat sim = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss"); //$NON-NLS-1$
				String submitTimeBeginStr = (String) jsObj
						.get("submitTimeBegin"); //$NON-NLS-1$
				String submitTimeEndStr = (String) jsObj.get("submitTimeEnd"); //$NON-NLS-1$
				if (StringUtil.isNotEmpty(submitTimeBeginStr)) {
					try {
						submitTimeBegin = sim.parse(submitTimeBeginStr);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if (StringUtil.isNotEmpty(submitTimeEndStr)) {
					try {
						submitTimeEnd = sim.parse(submitTimeEndStr);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}

		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		Order[] orders = getOrders(criteria, submitTimeBegin,
				submitTimeEnd, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", orders); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Path("delete/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") String id) {
		lgcDelOrders(id, "lm");

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("", ""); //$NON-NLS-1$ //$NON-NLS-2$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Path("details/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response details(@PathParam("id") String id) {
		Order order = getOrderDetails(id);
		ResponseBuilder builder = Response.ok(order);
		return builder.build();
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Path("revokeOrders/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response revokeOrders(@PathParam("id") String id) {
		boolean rtn = false;
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String currentUser = user.getUserId();
		String handler = currentUser;
		if (id != null) {
			String rs = revokeOrders(id, handler);
			if (rs == null) {
				rtn = true;
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Path("getOrderStatus/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response orderStatus(@PathParam("id") String id) {
		int orderStatus = getOrderStatus(id);
		ResponseBuilder builder = Response.ok(orderStatus);
		return builder.build();
	}
	
	/**
	 * 查询订单信息（分页）
	 * 
	 * @param criteriaType
	 * @param page
	 * @return
	 */
	public static Order[] getOrders(Order criteria, Date submitTimeBegin,
			Date submitTimeEnd, PageCond page) {
		try {
			return orderManager.getOrders(criteria, submitTimeBegin,
					submitTimeEnd, null, null, page);
		} catch (Exception e) {
			logger.error(e);
		}
		return new Order[0];
	}

	/**
	 * 通过订单编号查询订单详细信息（订单项详情）
	 * 
	 * @param orderId
	 * @return
	 */
	public static Order getOrderDetails(String orderId) {
		try {
			return orderManager.getOrderWithItems(orderId);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 通过订单编号查询当前订单状态
	 * 
	 * @param orderId
	 * @return
	 */
	public static int getOrderStatus(String orderId) {
		try {
			Order order = orderManager.getOrder(orderId);
			return null == order ? -1 : order.getOrderStatus(); //$NON-NLS-1$
		} catch (Exception e) {
			logger.error(e);
		}
		return -1;
	}

	/**
	 * 撤销订单
	 * 
	 * @param orderIds
	 * @param handler
	 * @return
	 */
	public static String revokeOrders(String orderIds, String handler) {
		if (orderIds == null || orderIds.trim().length() == 0) {
			return "orderId is null or empty.";
		}

		String[] ids = orderIds.split(",");
		for (int i = 0; i < ids.length; i++) {
			String curOrderId = ids[i];
			Order order = getOrderByOrdId(curOrderId);
			if (order == null)
				continue;
			int orderStatus = getOrderStatus(curOrderId);
			try {
				if (orderStatus != Order.ORDER_STATUS_SUBMITED) {
					return "Order [ "
							+ order.getOrderId()
							+ " ] are processed, does not allow the withdrawal.";
				}

				orderManager.updateStatus(curOrderId,
						Order.ORDER_STATUS_REVOKED, null, handler);

				Order newOrder = orderManager.getOrder(curOrderId);
				newOrder.setHandleTime(new Date());
				orderManager.update(newOrder);

				OrderItemAttr attr = orderManager.getAttrByName(curOrderId,
						OrderItem.ITEM_TYPE_APP, OrderItemAttr.ATTR_APP_NAME);
				if (null != attr && null != attr.getAttrValue()
						&& attr.getAttrValue().trim().length() > 0) {
					String appName = attr.getAttrValue();
					WebApp app = appManager.get(appName);

					if (app != null) {
						Map<String, String> appAttr = app.getAttributes();
						int appState = appAttr.get("state") == null ? 0 //$NON-NLS-1$
								: Integer.parseInt(appAttr.get("state")); //$NON-NLS-1$
						if (WebApp.STATE_WAIT_DESTORY == appState) {
							appAttr.put(
									"state", String.valueOf(WebApp.STATE_OPEND)); //$NON-NLS-1$
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
				return "Order [ " + order.getOrderId()
						+ " ] undo error, Please retry it.";
			}
		}
		return null;
	}

	/**
	 * 删除订单（逻辑删除）
	 * 
	 * @param orderIds
	 * @param handler
	 * @return
	 */
	public static String lgcDelOrders(String orderIds, String handler) {
		if (orderIds == null || orderIds.trim().length() == 0) {
			return "orderId is null or is empty.";
		}
		String[] ids = orderIds.split(",");
		for (int i = 0; i < ids.length; i++) {
			String curOrderId = ids[i];
			Order curOrder = getOrderByOrdId(curOrderId);
			if (curOrder == null)
				continue;
			try {
				orderManager.updateStatus(curOrderId,
						Order.ORDER_STATUS_DELETED, null, handler);
			} catch (Exception e) {
				logger.error(e);
				return "Order [ " + curOrderId + " ] delete error.";
			}
		}
		return null;
	}

	/**
	 * 删除订单（物理删除）
	 * 
	 * @param orderIds
	 * @param handler
	 * @return
	 */
	public static String phyDelOrders(String orderIds) {
		if (orderIds == null || orderIds.trim().length() == 0) {
			return "orderId is null or is empty.";
		}
		try {
			orderManager.remove(orderIds);
		} catch (Exception e) {
			logger.error(e);
			return "Order [ " + orderIds + " ] delete error.";
		}
		return null;
	}

	/**
	 * 根据OrderId 查询订单基本信息
	 * 
	 * @param orderId
	 * @return
	 */
	public static Order getOrderByOrdId(String orderId) {
		try {
			return orderManager.getOrder(orderId);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 获取Order中指定类型订单项 <br>
	 * 
	 * @param order
	 * @param itemType
	 * @return
	 */
	public static OrderItem getItemByType(Order order, String itemType) {
		if (order == null || itemType == null || itemType.trim().length() < 1
				|| order.getItemList() == null || order.getItemList().isEmpty()) {
			return null;
		}
		List<OrderItem> itemList = order.getItemList();
		for (OrderItem item : itemList) {
			String curType = item.getItemType();
			if (itemType.equals(curType)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 获取Order中指定订单项的值 <br>
	 * 
	 * @param order
	 * @param itemAttrName
	 * @param defaultValue
	 * @return
	 */
	public static String getItemStringValue(OrderItem orderitem,
			String itemAttrName, String defaultValue) {
		if (orderitem == null || itemAttrName == null
				|| itemAttrName.trim().length() < 1) {
			return defaultValue;
		}
		List<OrderItemAttr> attrList = orderitem.getAttrList();
		if (attrList == null || attrList.isEmpty()) {
			return defaultValue;
		}
		for (OrderItemAttr attr : attrList) {
			String attrName = attr.getAttrName();
			String attrValue = attr.getAttrValue();
			if (itemAttrName.equals(attrName)) {
				return attrValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 获取Order中指定订单项的值 <br>
	 * 
	 * @param order
	 * @param itemAttrName
	 * @param defaultValue
	 * @return
	 */
	public static int getItemIntValue(OrderItem orderitem, String itemAttrName,
			int defaultValue) {
		if (orderitem == null || itemAttrName == null
				|| itemAttrName.trim().length() < 1) {
			return defaultValue;
		}
		List<OrderItemAttr> attrList = orderitem.getAttrList();
		if (attrList == null || attrList.isEmpty()) {
			return defaultValue;
		}
		try {
			for (OrderItemAttr attr : attrList) {
				String attrName = attr.getAttrName();
				String attrValue = attr.getAttrValue();
				if (itemAttrName.equals(attrName)) {
					return Integer.parseInt(attrValue);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param orderId
	 * @param itemId
	 */
	public static void asynRedoItem(String orderId, String itemId) {
		long begin = System.currentTimeMillis();
		logger.info("[REDO] Begin process order {0} item {1}.", new Object[] { orderId, itemId });
		try {
			orderManager.handleItem(orderId, itemId);
		} catch (OrderException e) {
			logger.error("Process order {0} item {1} error.", new Object[] { orderId, itemId }, e);
		}

		long end = System.currentTimeMillis();
		logger.info("[REDO] End process order {0} item {1}, spent {3} ms.", new Object[] { orderId, itemId, end-begin });
	}

}