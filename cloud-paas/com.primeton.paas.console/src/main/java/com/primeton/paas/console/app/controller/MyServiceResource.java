/**
 *
 */
package com.primeton.paas.console.app.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.platform.service.srvmgr.ClusterMangerUtil;
import com.primeton.paas.console.platform.service.srvmgr.OrderManagerUtils;
import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.cluster.MemcachedCluster;
import com.primeton.paas.manage.api.cluster.MsgQueueCluster;
import com.primeton.paas.manage.api.cluster.MySQLCluster;
import com.primeton.paas.manage.api.cluster.RedisCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.OrderManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.impl.order.SingleCreateMsgQueueOrderProcessor;
import com.primeton.paas.manage.api.impl.order.SingleCreateRedisOrderProcessor;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.MemcachedService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/myService")
public class MyServiceResource {
	
	private static ILogger logger = LoggerFactory.getLogger(MyServiceResource.class);
	
	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);
	
	private static IWebAppManager appManager = WebAppManagerFactory
			.getManager();
	
	private static IServiceQuery srvQueryMgr = ServiceManagerFactory
			.getServiceQuery();
	
	private static IOrderManager orderManager = OrderManagerFactory
			.getManager();
	
	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/listMySQL")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response listMySQL(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", ClusterMangerUtil.getClustersByUser(DataContextManager //$NON-NLS-1$
						.current().getMUODataContext().getUserObject()
						.getUserId(), pageCond, MySQLCluster.class));
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/listHaproxy")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response listHaproxy(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", ClusterMangerUtil.getClustersByUser(user.getUserId(), pageCond, HaProxyCluster.class)); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/listMemcached")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response listMemcached(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		List<MemcachedCluster> list = ClusterMangerUtil.getClustersByUser(user.getUserId(), pageCond, MemcachedCluster.class);
		for (MemcachedCluster c : list) {
			String[] relationApp = appManager.getRelationApp(c.getId());
			String appStr = "";
			if (null != relationApp) {
				int i = 0;
				for (String app : relationApp) {
					appStr += app;
					if (i < relationApp.length - 1) {
						appStr += ",";
					}
					i++;
				}
			}
			c.getAttributes().put("relationApp", appStr); //$NON-NLS-1$
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("/applyService")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response applyService(Order order) {
		ResponseBuilder builder = Response.ok(createOrder(order));
		return builder.build();
	}

	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getMemcachedClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMemcachedClusterDetail(
			@PathParam("clusterId") String clusterId) {
		ResponseBuilder builder = Response.ok(getMemcachedClusterInfo(clusterId));
		return builder.build();
	}

	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("/removeService/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeService(@PathParam("clusterId") String clusterId) {
		boolean rtn = destroyService(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/listRedis")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response listRedis(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);
		String user = DataContextManager.current().getMUODataContext()
				.getUserObject().getUserId();
		List<RedisCluster> clusters = ClusterMangerUtil.getClustersByUser(user, pageCond, RedisCluster.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", clusters); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createRedis")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createRedis(Order order) {
		if (order != null) {
			if (StringUtil.isEmpty(order.getOrderType())) {
				order.setOrderType(SingleCreateRedisOrderProcessor.TYPE);
			}
			// 创建订单提交,但不自动审批,返回订单id
			order = OrderManagerUtils.submitOrder(order, false);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", null == order ? null : order.getOrderId()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public static MemcachedCluster getMemcachedClusterInfo(String clusterId) {
		MemcachedCluster cluster = clusterManager.get(clusterId,
				MemcachedCluster.class);
		if (cluster != null) {
			String appStr = "";
			String[] relationApp = appManager.getRelationApp(cluster.getId());
			if (null != relationApp) {
				int i = 0;
				for (String app : relationApp) {
					appStr += app;
					if (i < relationApp.length - 1) {
						appStr += ",";
					}
					i++;
				}
			}

			String ipStr = "";
			int memorySize = 0;
			int maxConnectionSize = 0;
			List<MemcachedService> sList = srvQueryMgr.getByCluster(clusterId,
					MemcachedService.class);
			if (null != sList && sList.size() > 0) {
				for (int i = 0; i < sList.size(); i++) {
					MemcachedService m = sList.get(i);
					ipStr += m.getIp() + ":" + m.getPort();
					if (i < sList.size() - 1) {
						ipStr += "<br>";
					}
					memorySize = m.getMemorySize();
					maxConnectionSize = m.getMaxConnectionSize();
				}
			}

			cluster.getAttributes().put("appStr", appStr); //$NON-NLS-1$
			cluster.getAttributes().put("ipStr", ipStr); //$NON-NLS-1$
			cluster.getAttributes().put("memorySize", //$NON-NLS-1$
					memorySize * (sList == null ? 0 : sList.size()) + "");
			cluster.getAttributes().put("maxConnectionSize", //$NON-NLS-1$
					maxConnectionSize + "");

			int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(clusterId);
			cluster.getAttributes().put("state", String.valueOf(sizeAndStatus[1])); //$NON-NLS-1$
			cluster.getAttributes().put("size", String.valueOf(sizeAndStatus[0])); //$NON-NLS-1$
		}
		return cluster;
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	public static boolean destroyService(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return false;
		}
		ICluster cluster = clusterManager.get(clusterId);
		if (cluster == null) {
			return false;
		}
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();

		Order order = new Order();
		order.setSubmitTime(new Date());// submitTime
		order.setOrderStatus(Order.ORDER_STATUS_SUBMITED);// /orderStatus
		order.setOrderType(Order.ORDER_TYPE_DELETE_SRV);// orderType
		order.setOwner(user.getUserId());

		List<OrderItem> itemList = new ArrayList<OrderItem>();
		OrderItem item = new OrderItem();
		item.setItemType(cluster.getType());

		List<OrderItemAttr> attrList = new ArrayList<OrderItemAttr>();
		OrderItemAttr attr = new OrderItemAttr();
		attr.setAttrName(OrderItemAttr.ATTR_CLUSTER_ID); // clusterId
		attr.setAttrValue(clusterId);
		attrList.add(attr);

		if (!StringUtil.isEmpty(cluster.getName())) {
			attr = new OrderItemAttr();
			attr.setAttrName(OrderItemAttr.ATTR_DISPLAY_NAME); // displayName
			attr.setAttrValue(cluster.getName());
			attrList.add(attr);
		}

		item.setAttrList(attrList);
		itemList.add(item);
		order.setItemList(itemList);
		return null != orderManager.add(order);
	}
	
	public static Order createOrder(Order order) {
		if (order == null) {
			return null;
		}
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		order.setSubmitTime(new Date());
		order.setBeginTime(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, 3);
		order.setEndTime(cal.getTime());
		order.setOwner(user.getUserId());
		order.setOrderStatus(Order.ORDER_STATUS_SUBMITED);
		order.setOrderType(Order.ORDER_TYPE_CREATE_SRV);
		List<OrderItem> list = order.getItemList();
		List<OrderItem> itemList = new ArrayList<OrderItem>();
		if (list != null && !list.isEmpty()) {
			for (OrderItem t : list) {
				if (t == null || StringUtil.isEmpty(t.getItemType())) {
					continue;
				}
				itemList.add(t);
			}
		}
		order.setItemList(itemList);
		try {
			return orderManager.add(order);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/listMsgQueue")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response listMsgQueue(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);
		String user = DataContextManager.current().getMUODataContext()
				.getUserObject().getUserId();
		List<MsgQueueCluster> clusters = ClusterMangerUtil.getClustersByUser(user, pageCond, MsgQueueCluster.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", clusters); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createMsgQueue")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createMsgQueue(Order order) {
		if (order != null) {
			if (StringUtil.isEmpty(order.getOrderType())) {
				order.setOrderType(SingleCreateMsgQueueOrderProcessor.TYPE);
			}
			// 创建订单提交,但不自动审批,返回订单id
			order = OrderManagerUtils.submitOrder(order, false);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", null == order ? null : order.getOrderId()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

}
