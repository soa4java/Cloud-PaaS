/**
 * 
 */
package com.primeton.paas.console.platform.controller;

import java.util.ArrayList;
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
import com.primeton.paas.console.platform.service.audit.OrderManagerUtil;
import com.primeton.paas.console.platform.service.srvmgr.ClusterMangerUtil;
import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.cluster.TomcatCluster;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.exception.WebAppException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.OrderManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.TomcatService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 应用管理
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
@Path("/appMgr")
public class AppMgrResource {
	
	// 日志服务
	private static ILogger logger = LoggerFactory.getLogger(AppMgrResource.class);

	// 应用管理
	private static IWebAppManager appManager = WebAppManagerFactory.getManager();

	// 集群管理
	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);
	private static IClusterManager jettyClusterMgr = ClusterManagerFactory
			.getManager(JettyService.TYPE);
	private static IClusterManager tomcatClusterMgr = ClusterManagerFactory
			.getManager(TomcatService.TYPE);

	// 订单管理
	private static IOrderManager orderManager = OrderManagerFactory
			.getManager();

	// 服务管理
	private static IServiceQuery srvQueryMgr = ServiceManagerFactory
			.getServiceQuery();

	private static String WEB_APP_URL = "appUrl"; // 访问路径

	/**
	 * @param criteria
	 * @return
	 */
	public static WebApp[] queryApps(WebApp criteria) {
		String appName = criteria.getName() == null ? null : criteria.getName()
				.trim();
		String owner = criteria.getOwner() == null ? null : criteria.getOwner()
				.trim();

		WebApp[] apps = null;

		if (StringUtil.isEmpty(appName) && StringUtil.isEmpty(owner)) {
			apps = appManager.getAll();
		} else if (!StringUtil.isEmpty(appName)) {
			// 若指定应用名称
			WebApp app = appManager.get(appName);

			if (app != null) {

				// 既指定用户名 又指定拥有者
				if (!StringUtil.isEmpty(owner) && !owner.equals(app.getOwner())) {
					return new WebApp[0];
				}

				apps = new WebApp[1];
				apps[0] = app;
			}

		} else if (!StringUtil.isEmpty(owner)) {
			// 若指定应用所有者
			apps = appManager.getByOwner(owner);
		}

		return apps;
	}

	/**
	 * query platform applications <br/>
	 * 
	 * @param criteria
	 *            查询条件（appName, owner）
	 * @param page
	 *            分页条件
	 * @return 应用列表
	 */
	public static WebApp[] getWebapps(WebApp criteria, PageCond page) {
		WebApp[] apps = appManager.getAll();

		List<WebApp> tempList = new ArrayList<WebApp>();
		List<WebApp> returnList = new ArrayList<WebApp>();

		for (WebApp app : apps) {
			if (StringUtil.contain(app.getName(), criteria.getName())
					&& StringUtil.contain(app.getDisplayName(),
							criteria.getDisplayName())
					&& StringUtil.contain(app.getOwner(), criteria.getOwner())) {
				tempList.add(app);
			}
		}

		List<WebApp> clusters = new ArrayList<WebApp>();
		if (tempList != null && !tempList.isEmpty()) {
			for (WebApp tempApp : tempList) {
				String appName = tempApp.getName();
				String url = appName + SystemVariables.getDomainPostfix();
				ICluster jettyCluster = clusterManager.getByApp(appName,
						JettyCluster.TYPE);
				if (jettyCluster == null) {
					ICluster tomcatCluster = clusterManager.getByApp(appName,
							TomcatCluster.TYPE);
					if (tomcatCluster == null) {
						tempApp.getAttributes().put("appStatus",
								IService.STATE_NOT_RUNNING + "");
					} else {
						int tomcatStatus = ClusterMangerUtil
								.getClusterState(tomcatCluster.getId());
						tempApp.getAttributes().put("appStatus",
								tomcatStatus + "");
					}
				} else {
					int jettyStatus = ClusterMangerUtil.getClusterState(jettyCluster.getId());
					tempApp.getAttributes().put("appStatus", jettyStatus + "");
				}
				tempApp.getAttributes().put(WEB_APP_URL, url);
				// 状态筛选
				if (criteria.getAttributes().get("appStatus") != null) {
					if (tempApp.getAttributes().get("appStatus")
							.equals(criteria.getAttributes().get("appStatus"))) {
						clusters.add(tempApp);
					}
				} else {
					clusters.add(tempApp);
				}
			}
		}
		page.setCount(clusters.size());
		int end = page.getBegin() + page.getLength() - 1;
		if (page.getBegin() + page.getLength() > page.getCount()) {
			end = page.getCount() - 1;
		}

		for (int i = page.getBegin(); i <= end; i++) {
			returnList.add(clusters.get(i));
		}

		return returnList.toArray(new WebApp[returnList.size()]);
	}

	/**
	 * 查询应用关联的集群列表<br>
	 * 
	 * @param appName
	 *            应用名称
	 * @return
	 */
	public static ICluster[] getClusterByApp(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		ICluster[] clusters = clusterManager.getByApp(appName);
		if (clusters != null) {
			for (ICluster cluster : clusters) {
				int size = ClusterMangerUtil.getClusterSize(cluster.getId());
				cluster.getAttributes().put("size", size + "");
			}
		}
		return clusters;
	}

	/**
	 * 查询集群中的服务实例列表<br>
	 * /**
	 * 
	 * @param clusterId
	 * @param type
	 * @return
	 */
	public static IService[] getServicesByCluster(String clusterId, String type) {
		if (StringUtil.isEmpty(clusterId)) {
			return null;
		}

		return srvQueryMgr.getByCluster(clusterId);
	}

	/**
	 * 移除应用<br>
	 * 
	 * @param appName
	 * @return
	 */
	public static void destroyApp(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return;
		}

		WebApp app = appManager.get(appName);
		if (app == null) {
			return;
		}
		logger.info("Begin remove application [" + appName + "].");

		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();

		String currentUser = user.getUserId();
		Order order = new Order();
		order.setSubmitTime(new Date()); 
		order.setOrderStatus(Order.ORDER_STATUS_SUBMITED); 
		order.setOrderType(Order.ORDER_TYPE_DELETE_APP); 
		order.setOwner(currentUser);

		List<OrderItem> itemList = new ArrayList<OrderItem>();
		OrderItem item = new OrderItem();
		item.setItemType(OrderItem.ITEM_TYPE_APP);

		List<OrderItemAttr> attrList = new ArrayList<OrderItemAttr>();
		OrderItemAttr attr = new OrderItemAttr();
		attr.setAttrName(OrderItemAttr.ATTR_APP_NAME);
		attr.setAttrValue(appName);
		attrList.add(attr);

		item.setAttrList(attrList);
		itemList.add(item);
		order.setItemList(itemList);

		Order new_order = null;
		logger.info("Begin create remove-application order .");

		try {
			new_order = orderManager.add(order);
		} catch (Exception e) {
			logger.error(e);
		}
		if (new_order == null || StringUtil.isEmpty(new_order.getOrderId())) {
			return;
		}

		// 更改应用状态为等待销毁
		Map<String, String> appAttr = app.getAttributes();
		appAttr.put("state", String.valueOf(WebApp.STATE_WAIT_DESTORY));
		app.setAttributes(appAttr);

		try {
			appManager.update(app);
		} catch (WebAppException e) {
			logger.error(e);
		}
		// handle order
		String orderId = new_order.getOrderId();
		orderManager.updateStatus(orderId, Order.ORDER_STATUS_APPROVED, null,
				currentUser);
		// 异步调用
		OrderManagerUtil.asynHandleOrder(orderId);
	}

	/**
	 * 启动应用<br>
	 * 
	 * @param appName
	 * @return
	 */
	public static boolean doStartApp(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return false;
		}
		// 同步调用：启动应用
		try {
			ICluster cluster = clusterManager.getByApp(appName,
					JettyCluster.TYPE);
			if (cluster != null) {
				jettyClusterMgr.start(cluster.getId());
			} else {
				cluster = clusterManager.getByApp(appName, TomcatCluster.TYPE);
				if (cluster != null) {
					tomcatClusterMgr.start(cluster.getId());
				}
			}
		} catch (ServiceException e) {
			logger.error("Start app {0} error.", new Object[] { appName });
		}
		return true;
	}

	/**
	 * 停止应用<br>
	 * 
	 * @param appName
	 * @return
	 */
	public static boolean doStopApp(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return false;
		}
		// 同步停止
		try {
			ICluster cluster = clusterManager.getByApp(appName,
					JettyCluster.TYPE);
			if (cluster != null) {
				jettyClusterMgr.stop(cluster.getId());
			} else {
				cluster = clusterManager.getByApp(appName, TomcatCluster.TYPE);
				if (cluster != null) {
					tomcatClusterMgr.stop(cluster.getId());
				}
			}
		} catch (ServiceException e) {
			logger.error("Shutdown app {0} error.", new Object[] { appName }, e);
		}
		return true;
	}

	/**
	 * 获取应用的访问路径<br>
	 * 
	 * @param appName
	 * @return
	 */
	public static String getAppAccessPath(String appName) {
		return appName + SystemVariables.getDomainPostfix();
	}
	
	@Path("/innerAppList")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response list(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		WebApp criteria = new WebApp();
		criteria.setOwner(null);
		
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setDisplayName(jsObj.getString("displayName")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("appStatus"))) { //$NON-NLS-1$ // 0|1|2 // 停止，运行，异常
					String appStatus = jsObj.getString("appStatus"); //$NON-NLS-1$
					criteria.getAttributes().put("appStatus", appStatus); //$NON-NLS-1$
				}
			}
		}
		
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex*pageSize);
		pageCond.setLength(pageSize);
		
		WebApp[] apps = getWebapps(criteria, pageCond);
		
		if (apps == null || apps.length == 0) {
			apps = new WebApp[0];
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", apps); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	@Path("removeApp/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeApp(@PathParam("appName") String appName) {
		destroyApp(appName);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", Boolean.TRUE);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	@Path("startApp/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response startApp(@PathParam("appName") String appName) {
		boolean ifSuccess = doStartApp(appName);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", ifSuccess);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	@Path("stopApp/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response stopApp(@PathParam("appName") String appName) {
		boolean ifSuccess = doStopApp(appName);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", ifSuccess);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	
	@Path("showRelClusters/{appName}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response showRelClusters(@PathParam("appName") String appName) {
		ICluster[] clusters = getClusterByApp(appName);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", clusters);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	@Path("showClusterServices")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response showClusterServices(@FormParam("clusterId") String clusterId,@FormParam("type") String type) {
		IService[] services = getServicesByCluster(clusterId, type);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", services);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
}
