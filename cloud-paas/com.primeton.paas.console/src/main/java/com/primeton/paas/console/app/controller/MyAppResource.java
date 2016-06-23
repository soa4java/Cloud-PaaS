/**
 *
 */
package com.primeton.paas.console.app.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.app.service.util.AppUtil;
import com.primeton.paas.console.app.service.util.FileUtil;
import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.console.platform.service.srvmgr.ClusterMangerUtil;
import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.cluster.MemcachedCluster;
import com.primeton.paas.manage.api.exception.WebAppException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.HostTemplateManagerFactory;
import com.primeton.paas.manage.api.factory.OrderManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IHostTemplateManager;
import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.HostTemplate;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.service.HaProxyService;
import com.primeton.paas.manage.api.service.MemcachedService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/myApp")
public class MyAppResource {

	private static ILogger logger = LoggerFactory.getLogger(MyAppResource.class);
	
	private static IOrderManager orderManager = OrderManagerFactory
			.getManager();

	private static IWebAppManager appManager = WebAppManagerFactory
			.getManager();

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static IServiceQuery srvQueryMgr = ServiceManagerFactory
			.getServiceQuery();

	private static IHostTemplateManager templateManager = HostTemplateManagerFactory
			.getManager();

	private static IServiceQuery serviceQuery = ServiceManagerFactory
			.getServiceQuery();
	
	/**
	 * 查询我的 平台应用/内部应用
	 * 
	 * @param userID
	 *            云平台租户ID <br>
	 * @return 应用列表
	 */
	public static List<WebApp> getWebapps(String owner) {
		List<WebApp> appList = new ArrayList<WebApp>();
		// 查询用户应用列表
		WebApp[] apps = AppUtil.getWebapps(owner);
		if (apps != null && apps.length > 0) {
			// 增加应用容器类型显示
			for (WebApp app : apps) {
				// 查询应用关联集群 Jetty或Tomcat
				app.getAttributes().put("serverType",
						AppUtil.queryServerType(app.getName()));
				// 查询协议类型
				// app.getAttributes().put("protocal",
				// queryProtocolType(app.getName()));
				HaProxyCluster haCluster = queryHaProxyCluster(app.getName());
				if (null != haCluster) {
					app.getAttributes().put("protocal",
							haCluster.getProtocolType());
					if (haCluster.getIsEnableDomain().equals("N")) {
						String ip = "";
						// HaProxy集群实例
						List<HaProxyService> services = serviceQuery
								.getByCluster(haCluster.getId(),
										HaProxyService.class);
						for (HaProxyService s : services) {
							ip = s.getIp() + ":" + s.getPort();
							if (IService.HA_MODE_MASTER.equals(s.getHaMode())) {
								ip = s.getIp() + ":" + s.getPort();
								break;
							}
						}
						app.getAttributes().put("protocal",
								HaProxyCluster.PROTOCOL_HTTP);
						app.setSecondaryDomain(ip);
					}
				} else {
					app.getAttributes().put("protocal", null);
				}
			}
			appList = Arrays.asList(apps);
		}
		return appList;
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	public static String queryProtocolType(String appName) {
		HaProxyCluster cluster = (HaProxyCluster) clusterManager.getByApp(
				appName, HaProxyCluster.TYPE);
		return null == cluster ? null : cluster.getProtocolType();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	public static HaProxyCluster queryHaProxyCluster(String appName) {
		return (HaProxyCluster) clusterManager.getByApp(appName,
				HaProxyCluster.TYPE);
	}

	/**
	 * 生成订单
	 * 
	 * @param order
	 * @return
	 */
	public static Order createInnerAppOrder(Order order) {
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
		order.setOrderStatus(Order.ORDER_STATUS_SUBMITED);
		order.setOrderType(Order.ORDER_TYPE_CREATE_APP);

		Order new_order = null;

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

		OrderItem item = new OrderItem();
		item.setItemStatus(OrderItem.ITEM_STATUS_INIT);
		item.setItemType(OrderItem.ITEM_TYPE_WAR);
		itemList.add(item);

		item = new OrderItem();
		item.setItemStatus(OrderItem.ITEM_STATUS_INIT);
		item.setItemType(OrderItem.ITEM_TYPE_SVN_REPO);
		itemList.add(item);

		item = new OrderItem();
		item.setItemStatus(OrderItem.ITEM_STATUS_INIT);
		item.setItemType(OrderItem.ITEM_TYPE_NGINX);
		itemList.add(item);
		order.setItemList(itemList);
		try {
			new_order = orderManager.add(order);
		} catch (Exception e) {
			logger.error(e);
		}

		return new_order;
	}

	/**
	 * 删除应用 <br>
	 * 
	 * @param appID 应用标识
	 */
	public static boolean destroyApp(String appName, String appType) {
		if (StringUtil.isEmpty(appName)) {
			return false;
		}
		// 判断应用是否允许移除（只有“已开通”状态的应用才可执行移除）
		WebApp app = appManager.get(appName);
		if (app == null) {
			return false;
		}
		Map<String, String> appAttr = app.getAttributes();
		int appState = appAttr.get("state") == null ? 0 : Integer
				.parseInt(appAttr.get("state"));
		if (WebApp.STATE_OPEND != appState) {
			return false;
		}
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		Order order = new Order();
		order.setSubmitTime(new Date());// submitTime
		order.setOrderStatus(Order.ORDER_STATUS_SUBMITED);// /orderStatus
		order.setOwner(user.getUserId());

		order.setOrderType(Order.ORDER_TYPE_DELETE_APP);// orderType
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
		try {
			new_order = orderManager.add(order);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}

		if (new_order != null) {
			// 更改应用状态为等待销毁
			appAttr.put("state", String.valueOf(WebApp.STATE_WAIT_DESTORY));
			app.setAttributes(appAttr);
			try {
				appManager.update(app);
			} catch (WebAppException e) {
				logger.error("Update app {0} error.", new Object[] { app }, e);
			}
		}
		return true;
	}

	/**
	 * 查询外部应用 已绑定的服务集群
	 * 
	 * @param appName
	 * @return
	 */
	public static List<ICluster> getBindClusters(String appName) {
		ICluster[] clusters = clusterManager.getByApp(appName);
		List<ICluster> clusterList = new ArrayList<ICluster>();
		if (null != clusters) {
			for (ICluster c : clusters) {
				if (c.getType().equals(HaProxyCluster.TYPE)
						|| c.getType().equals(MemcachedCluster.TYPE)) {
					// 集群状态
					int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(c.getId());
					c.getAttributes().put("state", String.valueOf(sizeAndStatus[1]));
					clusterList.add(c);
				}
			}
		}
		return clusterList;
	}

	/**
	 * 查询外部应用 未绑定的服务集群
	 * 
	 * @param appName
	 * @return
	 */
	public static List<ICluster> getUnBindClusters(String userID, String appName) {
		List<ICluster> bindClusters = getBindClusters(appName);
		Map<String, ICluster> m = new HashMap<String, ICluster>();
		if (null != bindClusters) {
			for (ICluster c : bindClusters) {
				m.put(c.getId(), c);
			}
		}
		List<ICluster> clusterList = new ArrayList<ICluster>();
		List<MemcachedCluster> mClusters = clusterManager.getByOwner(userID,
				MemcachedCluster.TYPE, null, MemcachedCluster.class);
		List<HaProxyCluster> hClusters = clusterManager.getByOwner(userID,
				HaProxyCluster.TYPE, null, HaProxyCluster.class);

		if (null != mClusters) {
			for (ICluster mCluster : mClusters) {
				if (m.get(mCluster.getId()) == null) {
					// 集群状态
					int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(mCluster.getId());
					mCluster.getAttributes()
							.put("state", String.valueOf(sizeAndStatus[1]));
					clusterList.add(mCluster);
				}
			}
		}

		if (null != hClusters) {
			for (ICluster hCluster : hClusters) {
				if (m.get(hCluster.getId()) == null) {
					// 集群状态
					int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(hCluster.getId());
					hCluster.getAttributes()
							.put("state", String.valueOf(sizeAndStatus[1]));
					clusterList.add(hCluster);
				}
			}
		}
		return clusterList;
	}

	/**
	 * 
	 * @param appName
	 *            应用标识
	 * @param type
	 *            类型
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getClusterInfo(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return null;
		}
		ICluster cluster = clusterManager.get(clusterId);
		Map info = new HashMap();
		if (null != cluster) {
			info.put("cluster", cluster);
			if (cluster.getType().equals(MemcachedCluster.TYPE)) {
				List<MemcachedService> sList = srvQueryMgr.getByCluster(
						clusterId, MemcachedService.class);
				if (null != sList && sList.size() > 0) {
					String ipStr = "";
					for (int i = 0; i < sList.size(); i++) {
						MemcachedService m = sList.get(i);
						ipStr += m.getIp() + ":" + m.getPort();
						if (i == 0) {
							info.put("service", m);
						}
						if (i < sList.size() - 1) {
							ipStr += "<br>";
						}
					}
					info.put("ipStr", ipStr);

				}
			} else if (cluster.getType().equals(HaProxyCluster.TYPE)) {
				HaProxyCluster h = (HaProxyCluster) cluster;
				String members = h.getMembers();
				List<HaProxyService> sList = srvQueryMgr.getByCluster(
						clusterId, HaProxyService.class);
				if (null != sList && sList.size() > 0) {
					String ipStr = "";
					String protocol = "";
					for (int i = 0; i < sList.size(); i++) {
						HaProxyService m = sList.get(i);
						protocol = m.getProtocal();
						ipStr += m.getIp() + ":" + m.getPort();
						if (i == 0) {
							info.put("service", m);
						}
						if (i < sList.size() - 1) {
							ipStr += "<br>";
						}
					}
					HostTemplate template = templateManager.getTemplate(sList.get(0).getPackageId());
					String hostPackageName = null == template ? "" : template.getTemplateName();
					info.put("ipStr", members);
					// info.put("members", members);
					info.put("protocol", protocol);
					info.put("hostPackageName", hostPackageName);
					info.put("url", "// TODO"); // TODO

					System.out.println(ipStr);
					System.out.println(members);
				}
			}
		}
		return info;
	}

	@Path("/myWebapps")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response myWebapps(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex);
		pageCond.setLength(pageSize);

		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String currentUser = user.getUserId();

		List<WebApp> appList = getWebapps(currentUser);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", appList); //$NON-NLS-1$
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
		MyOrderResource.lgcDelOrders(id, "lm");

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
		Order order = MyOrderResource.getOrderDetails(id);
		// Map<String,Object> result = new HashMap<String,Object>();
		// result.put("data", order);
		ResponseBuilder builder = Response.ok(order);
		return builder.build();
	}

	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("/applyApp")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response applyApp(Order order) {
		if (null != order) {
			order = createInnerAppOrder(order);
		}
		ResponseBuilder builder = Response.ok(order);
		return builder.build();
	}

	/**
	 * 
	 * @param appID
	 * @return
	 */
	@Path("isExistDomain/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response isExistDomain(@PathParam("id") String appID) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(appID)) {
			boolean bln = appManager.get(appID) != null;
			result.put("data", bln); //$NON-NLS-1$
		}
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("/getBindClusters/{appName}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getAppBindClusters(@PathParam("appName") String appName) {
		List<ICluster> clusters = getBindClusters(appName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", clusters); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("/getUnBindClusters/{appName}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getUnBindClusters(@PathParam("appName") String appName) {
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String currentUser = user.getUserId();

		List<ICluster> clusters = getUnBindClusters(
				currentUser, appName);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", clusters); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @param clusterId
	 * @return
	 */
	@Path("bindCluster/{appName}-{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response bindCluster(@PathParam("appName") String appName,
			@PathParam("clusterId") String clusterId) {
		if (appName != null && clusterId != null) {
			appManager.bind(appName, clusterId);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", Boolean.TRUE); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @param clusterId
	 * @return
	 */
	@Path("unBindCluster/{appName}-{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response unBindCluster(@PathParam("appName") String appName,
			@PathParam("clusterId") String clusterId) {
		if (appName != null && clusterId != null) {
			appManager.unbind(appName, clusterId);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", Boolean.TRUE); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @param appType
	 * @return
	 */
	@Path("/removeApp/{appName}&{appType}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeApp(@PathParam("appName") String appName,
			@PathParam("appType") String appType) {
		boolean rtn = false;
		if (appName != null && appType != null) {
			rtn = destroyApp(appName, appType);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param uploadFile
	 * @param fileInfo
	 * @param deployTag
	 * @return
	 */
	@POST
	@Path("/uploadFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
			@FormDataParam("uploadFile") InputStream uploadFile,
			@FormDataParam("uploadFile") FormDataContentDisposition fileInfo,
			@FormDataParam("deployTag") String deployTag) {
		dataMsg.put(deployTag, "W"); //$NON-NLS-1$
		boolean rtn = false;
		String tempPath = SystemVariable.getSslCertificateTempPath();
		File dir = new File(tempPath);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
		}
		String filePath = tempPath + File.separator + deployTag + ".zip"; //$NON-NLS-1$
		try {
			FileUtil.saveFile(uploadFile, filePath);
			rtn = true;
			dataMsg.put(deployTag, "S," + filePath); //$NON-NLS-1$
		} catch (IOException ex) {
			rtn = false;
			dataMsg.put(deployTag, "F"); //$NON-NLS-1$
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		return null;
	}

	/**
	 * 
	 */
	private static Map<String, String> dataMsg = new HashMap<String, String>();

	@Path("getDeployMsg/{deployTag}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDeployMsg(@PathParam("deployTag") String deployTag) {
		String v = "F"; //$NON-NLS-1$
		if (deployTag != null && deployTag != null) {
			v = dataMsg.get(deployTag);
			if (v == null) {
				int i = 0;
				while (i < 20) {
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
					}
					v = dataMsg.get(deployTag);
					if (v != null) {
						break;
					}
					i++;
				}
			}
			if (v != null) {
				int i = 0;
				while (i < 50) {
					v = dataMsg.get(deployTag);
					if (v != null && !v.equals("W")) { //$NON-NLS-1$
						break;
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
					i++;
				}
				dataMsg.remove(deployTag);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", v); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

}
