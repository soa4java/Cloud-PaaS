/**
 * 
 */
package com.primeton.paas.console.platform.controller;

import java.util.ArrayList;
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

import net.sf.json.JSONObject;

import com.primeton.paas.console.common.Entry;
import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.console.platform.service.srvmgr.CEPEngineClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.CardBinClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.ClusterMangerUtil;
import com.primeton.paas.console.platform.service.srvmgr.CollectorClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.EsbClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.GatewayClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.HaproxyClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.JettyClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.JobCtrlClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.KeepalivedMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.MailClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.MemcachedClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.MsgQueueClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.MySqlServiceMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.MysqlClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.NginxClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.OpenAPIClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.OrderManagerUtils;
import com.primeton.paas.console.platform.service.srvmgr.RedisClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.RedisSentinelClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.ServiceManagerUtil;
import com.primeton.paas.console.platform.service.srvmgr.SmsClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.SrvStretchMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.SvnClusterMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.TomcatClusterMgrUtil;
import com.primeton.paas.manage.api.cluster.CEPEngineCluster;
import com.primeton.paas.manage.api.cluster.CardBinCluster;
import com.primeton.paas.manage.api.cluster.CollectorCluster;
import com.primeton.paas.manage.api.cluster.EsbCluster;
import com.primeton.paas.manage.api.cluster.GatewayCluster;
import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.cluster.JobCtrlCluster;
import com.primeton.paas.manage.api.cluster.KeepalivedCluster;
import com.primeton.paas.manage.api.cluster.MailCluster;
import com.primeton.paas.manage.api.cluster.MemcachedCluster;
import com.primeton.paas.manage.api.cluster.MsgQueueCluster;
import com.primeton.paas.manage.api.cluster.MySQLCluster;
import com.primeton.paas.manage.api.cluster.NginxCluster;
import com.primeton.paas.manage.api.cluster.OpenAPICluster;
import com.primeton.paas.manage.api.cluster.RedisCluster;
import com.primeton.paas.manage.api.cluster.RedisSentinelCluster;
import com.primeton.paas.manage.api.cluster.SVNCluster;
import com.primeton.paas.manage.api.cluster.SmsCluster;
import com.primeton.paas.manage.api.cluster.TomcatCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.order.CreateEsbOrderProcessor;
import com.primeton.paas.manage.api.impl.order.CreateGatewayOrderProcessor;
import com.primeton.paas.manage.api.impl.order.CreateJobCtrlOrderProcessor;
import com.primeton.paas.manage.api.impl.order.CreateRedisSentinelOrderProcessor;
import com.primeton.paas.manage.api.impl.order.SingleCreateMsgQueueOrderProcessor;
import com.primeton.paas.manage.api.impl.order.SingleCreateRedisOrderProcessor;
import com.primeton.paas.manage.api.impl.order.SingleDestroyServiceOrderProcessor;
import com.primeton.paas.manage.api.impl.order.UpdateGatewayOrderProcessor;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.CEPEngineService;
import com.primeton.paas.manage.api.service.CardBinService;
import com.primeton.paas.manage.api.service.CollectorService;
import com.primeton.paas.manage.api.service.GatewayService;
import com.primeton.paas.manage.api.service.HaProxyService;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.JobCtrlService;
import com.primeton.paas.manage.api.service.KeepalivedService;
import com.primeton.paas.manage.api.service.MailService;
import com.primeton.paas.manage.api.service.MemcachedService;
import com.primeton.paas.manage.api.service.MsgQueueService;
import com.primeton.paas.manage.api.service.MySQLService;
import com.primeton.paas.manage.api.service.NginxService;
import com.primeton.paas.manage.api.service.OpenAPIService;
import com.primeton.paas.manage.api.service.RedisSentinelService;
import com.primeton.paas.manage.api.service.RedisService;
import com.primeton.paas.manage.api.service.SVNService;
import com.primeton.paas.manage.api.service.SmsService;
import com.primeton.paas.manage.api.service.TomcatService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 服务管理. <br>
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
@SuppressWarnings("deprecation")
@Path("/service")
public class ServiceMgrResource {
	
	private static IServiceQuery srvQueryMgr = ServiceManagerFactory.getServiceQuery();
	
	/**
	 * 
	 * @param clusterId
	 * @param type
	 * @return
	 */
	@Path("startCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public Response startCluster(@FormParam("clusterId") String clusterId,
			@FormParam("type") String type) {
		boolean rs = false;
		try {
			IClusterManager clusterManager = ClusterManagerFactory.getManager(type);
			clusterManager = (null == clusterManager) ? ClusterManagerFactory.getManager(IClusterManager.DEFAULT_TYPE) : clusterManager;
			clusterManager.start(clusterId);
			rs = true;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param type
	 * @return
	 */
	@Path("restartCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public Response restartCluster(@FormParam("clusterId") String clusterId,
			@FormParam("type") String type) {
		boolean rs = false;
		try {
			IClusterManager clusterManager = ClusterManagerFactory.getManager(type);
			clusterManager = (null == clusterManager) ? ClusterManagerFactory.getManager(IClusterManager.DEFAULT_TYPE) : clusterManager;
			clusterManager.restart(clusterId);
			rs = true;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param type
	 * @return
	 */
	@Path("stopCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public Response stopCluster(@FormParam("clusterId") String clusterId,
			@FormParam("type") String type) {
		boolean rs = false;
		try {
			IClusterManager clusterManager = ClusterManagerFactory.getManager(type);
			clusterManager = (null == clusterManager) ? ClusterManagerFactory.getManager(IClusterManager.DEFAULT_TYPE) : clusterManager;
			clusterManager.stop(clusterId);
			rs = true;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param type
	 * @return
	 */
	@Path("removeCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public Response removeCluster(@FormParam("clusterId") String clusterId,
			@FormParam("type") String type) {
		String rs = ClusterMangerUtil.removeCluster(clusterId, type);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getCluster/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCluster(@PathParam("clusterId") String clusterId) {
		ICluster cluster = ClusterMangerUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param type
	 * @param serviceId
	 * @return
	 */
	@Path("startService")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response startService(@FormParam("type") String type,
			@FormParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.startService(serviceId, type);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param type
	 * @param serviceId
	 * @return
	 */
	@Path("stopService")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response stopService(@FormParam("type") String type,
			@FormParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.stopService(serviceId, type);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param type
	 * @param serviceId
	 * @return
	 */
	@Path("restartService")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response restartService(@FormParam("type") String type,
			@FormParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.restartService(serviceId, type);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
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
	@Path("/mailCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response list(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		MailCluster criteria = new MailCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put("status", //$NON-NLS-1$
							jsObj.getString("status")); //$NON-NLS-1$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<MailCluster> list = MailClusterMgrUtil.getMailClusters(criteria,
				pageCond);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new MailCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getMailClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMailClusterDetail(
			@PathParam("clusterId") String clusterId) {
		MailCluster cluster = MailClusterMgrUtil.getClusterDetail(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster);  //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("mailService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMailServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<MailService> list = srvQueryMgr.getByCluster(clusterId, MailService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new MailService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeMailService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeMailService(@PathParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.removeService(serviceId, MailService.TYPE);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createMail")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createMail(Order order) {
		// 创建订单，并提交自动审批 返回订单id
		if (order != null) {
			order = OrderManagerUtils.createOrder(order);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", order.getOrderId()); //$NON-NLS-1$
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
	@Path("/smsCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	@Deprecated
	public Response listSmsCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		SmsCluster criteria = new SmsCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put("status", //$NON-NLS-1$
							jsObj.getString("status")); //$NON-NLS-1$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<SmsCluster> list = SmsClusterMgrUtil.getSmsClusters(criteria,
				pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new SmsCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getSmsClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Deprecated
	public Response getSmsClusterDetail(@PathParam("clusterId") String clusterId) {
		SmsCluster cluster = SmsClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("smsService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Deprecated
	public Response getSmsServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<SmsService> list = srvQueryMgr.getByCluster(clusterId, SmsService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new SmsService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeSmsService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Deprecated
	public Response removeSmsService(@PathParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.removeService(serviceId, SmsService.TYPE);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createSms")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createSms(Order order) {
		// 创建订单，并提交自动审批 返回订单id
		if (order != null) {
			order = OrderManagerUtils.createOrder(order);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", order.getOrderId()); //$NON-NLS-1$
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
	@Path("/openapiCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listOpenAPICluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		OpenAPICluster criteria = new OpenAPICluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<OpenAPICluster> list = OpenAPIClusterMgrUtil.getOpenAPIClusters(
				criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new OpenAPICluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getOpenAPIClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOpenAPIClusterDetail(
			@PathParam("clusterId") String clusterId) {
		OpenAPICluster cluster = OpenAPIClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("openapiService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOpenAPIServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<OpenAPIService> list = srvQueryMgr.getByCluster(clusterId, OpenAPIService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new OpenAPIService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeOpenAPIService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeOpenAPIService(
			@PathParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.removeService(serviceId, OpenAPIService.TYPE);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createOpenAPI")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	@Deprecated
	public Response createOpenAPI(Order order) {
		// 创建订单，并提交自动审批 返回订单id
		if (order != null) {
			order = OrderManagerUtils.createOrder(order);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", order.getOrderId()); //$NON-NLS-1$
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
	@Path("/cardbinCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	@Deprecated
	public Response listCardBinCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		CardBinCluster criteria = new CardBinCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<CardBinCluster> list = CardBinClusterMgrUtil.getCardBinClusters(
				criteria, pageCond);// .getSmsClusters(criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new CardBinCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getCardBinClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Deprecated
	public Response getCardBinClusterDetail(
			@PathParam("clusterId") String clusterId) {
		CardBinCluster cluster = CardBinClusterMgrUtil
				.getClusterDetail(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("cardbinService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Deprecated
	public Response getCardBinServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<CardBinService> list = srvQueryMgr.getByCluster(clusterId, CardBinService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new CardBinService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeCardBinService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Deprecated
	public Response removeCardBinService(
			@PathParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.removeService(serviceId, CardBinService.TYPE);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createCardBin")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createCardBin(Order order) {
		// 创建订单，并提交自动审批 返回订单id
		if (order != null) {
			order = OrderManagerUtils.createOrder(order);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", order.getOrderId()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param serviceId
	 * @return
	 */
	@Path("getCardBinServiceInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	@Deprecated
	public Response getCardBinServiceInfo(
			@FormParam("clusterId") String clusterId,
			@FormParam("serviceId") String serviceId) {
		CardBinService inst = srvQueryMgr.get(serviceId, CardBinService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", inst); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("configCardBin")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response configCardBin(Order order) {
		// 创建订单，并提交自动审批 返回订单id
		if (order != null) {
			order = OrderManagerUtils.createConfigOrder(order);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", order.getOrderId()); //$NON-NLS-1$
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
	@Path("/memcachedCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listMemcachedCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		MemcachedCluster criteria = new MemcachedCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<MemcachedCluster> list = MemcachedClusterMgrUtil
				.getMemcachedClusters(criteria, pageCond);// .getSmsClusters(criteria,
															// pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new MemcachedCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
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
			@PathParam("clusterId") String clusterId) { //$NON-NLS-1$
		MemcachedCluster cluster = MemcachedClusterMgrUtil
				.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("memcachedService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMemcachedServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<MemcachedService> list = srvQueryMgr.getByCluster(clusterId, MemcachedService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new MemcachedService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeMemcachedService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeMemcachedService(
			@PathParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.removeService(serviceId, MemcachedService.TYPE);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
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
	@Path("/svnCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listSvnCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		SVNCluster criteria = new SVNCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<SVNCluster> list = SvnClusterMgrUtil.getSvnClusters(criteria,
				pageCond);// .getSmsClusters(criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new SVNCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getSvnClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSvnClusterDetail(@PathParam("clusterId") String clusterId) { //$NON-NLS-1$
		SVNCluster cluster = SvnClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("svnService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSvnServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<SVNService> list = srvQueryMgr.getByCluster(clusterId, SVNService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new SVNService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeSvnService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeSvnService(@PathParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.removeService(serviceId, SVNService.TYPE);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createSvn")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createSvn(Order order) {
		// 创建订单，并提交自动审批 返回订单id
		if (order != null) {
			order = OrderManagerUtils.createOrder(order);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", order.getOrderId()); //$NON-NLS-1$
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
	@Path("/cepCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listCepCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		CEPEngineCluster criteria = new CEPEngineCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<CEPEngineCluster> list = CEPEngineClusterMgrUtil
				.getCEPAnalysisCluster(criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new CEPEngineCluster[list.size()]));
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result); //$NON-NLS-1$
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getCepClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCepClusterDetail(@PathParam("clusterId") String clusterId) {
		CEPEngineCluster cluster = CEPEngineClusterMgrUtil
				.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("cepService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCepServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<CEPEngineService> list = srvQueryMgr.getByCluster(clusterId, CEPEngineService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new CEPEngineService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeCepService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCepService(@PathParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.removeService(serviceId, CEPEngineService.TYPE);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createCep")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createCep(Order order) {
		// 创建订单，并提交自动审批 返回订单id
		if (order != null) {
			order = OrderManagerUtils.createOrder(order);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", order.getOrderId()); //$NON-NLS-1$
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
	@Path("/collectorCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listCollectorCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		CollectorCluster criteria = new CollectorCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<CollectorCluster> list = CollectorClusterMgrUtil
				.getCollectorClusters(criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new CollectorCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getCollectorClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCollectorClusterDetail(
			@PathParam("clusterId") String clusterId) {
		CollectorCluster cluster = CollectorClusterMgrUtil
				.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("collectorService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCollectorServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<CollectorService> list = srvQueryMgr.getByCluster(clusterId, CollectorService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new CollectorService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeCollectorService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCollectorService(
			@PathParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.removeService(serviceId, CollectorService.TYPE);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createCollector")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createCollector(Order order) {
		// 创建订单，并提交自动审批 返回订单id
		if (order != null) {
			order = OrderManagerUtils.createOrder(order);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", order.getOrderId()); //$NON-NLS-1$
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
	@Path("/keepalivedCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listKeepalivedCluster(
			@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		KeepalivedCluster criteria = new KeepalivedCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<KeepalivedCluster> list = KeepalivedMgrUtil.getClusters(criteria,
				pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new KeepalivedCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getKeepalivedClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKeepalivedClusterDetail(
			@PathParam("clusterId") String clusterId) {
		KeepalivedCluster cluster = KeepalivedMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("keepalivedService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKeepalivedServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<KeepalivedService> list = KeepalivedMgrUtil
				.getInstances(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new KeepalivedService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * Mysql
	 */
	@Path("/mysqlCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listMysqlCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		MySQLCluster criteria = new MySQLCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<MySQLCluster> list = MysqlClusterMgrUtil.getMysqlClusters(
				criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new MySQLCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getMysqlClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMysqlClusterDetail(
			@PathParam("clusterId") String clusterId) {
		MySQLCluster cluster = MysqlClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("mysqlService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMysqlServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<MySQLService> list = MySqlServiceMgrUtil
				.getServicesByCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new MySQLService[list.size()])); //$NON-NLS-1$
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
	@Path("/haproxyCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listHaproxyCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		HaProxyCluster criteria = new HaProxyCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<HaProxyCluster> list = HaproxyClusterMgrUtil.getHaproxyClusters(
				criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new HaProxyCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getHaproxyClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHaproxyClusterDetail(
			@PathParam("clusterId") String clusterId) {
		HaProxyCluster cluster = HaproxyClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("haproxyService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHaproxyServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<HaProxyService> list = srvQueryMgr.getByCluster(clusterId, HaProxyService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new HaProxyService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeHaproxyService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeHaproxyService(
			@PathParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.removeService(serviceId, HaProxyService.TYPE);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @return
	 */
	@Path("getRelServiceTypes")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRelServiceTypes() {
		List<Entry> list = new ArrayList<Entry>();
		String types = SystemVariable.getHaproxyRelSrvType();
		String[] typeArr = types.split(",");
		if (typeArr != null && typeArr.length > 0) {
			for (String t : typeArr) {
				Entry cm = new Entry();
				cm.setId(t);
				cm.setText(t);
				list.add(cm);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new Entry[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@Path("getAllRelServiceByType/{type}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRelServiceTypes(@PathParam("type") String type) {
		ICluster[] clusters = HaproxyClusterMgrUtil
				.getAllRelServiceByType(type);
		List<Entry> list = new ArrayList<Entry>();

		if (clusters != null && clusters.length > 0) {
			for (ICluster t : clusters) {
				Entry cm = new Entry();
				cm.setId(t.getId());
				cm.setText(t.getName());
				list.add(cm);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new Entry[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @return
	 */
	@Path("getAllNginxClusters")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllNginxClusters() {
		List<NginxCluster> dataList = HaproxyClusterMgrUtil
				.getAllNginxClusters();
		List<Entry> list = new ArrayList<Entry>();
		for (NginxCluster t : dataList) {
			Entry cm = new Entry();
			cm.setId(t.getId());
			cm.setText(t.getName());
			list.add(cm);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new Entry[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createHaproxy")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createHaproxy(Order order) {
		// 创建订单，并提交自动审批 返回订单id
		if (order != null) {
			order = OrderManagerUtils.createOrder(order);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", order.getOrderId()); //$NON-NLS-1$
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
	@Path("/nginxCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listNginxCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		NginxCluster criteria = new NginxCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<NginxCluster> list = NginxClusterMgrUtil.getNginxClusters(
				criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new NginxCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getNginxClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNginxClusterDetail(
			@PathParam("clusterId") String clusterId) {
		NginxCluster cluster = NginxClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("nginxService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNginxServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<NginxService> list = srvQueryMgr.getByCluster(clusterId, NginxService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new NginxService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeNginxService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeNginxService(@PathParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.removeService(serviceId, NginxService.TYPE);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param hosts
	 * @return
	 */
	@Path("validateNginxHosts/{hosts}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateNginxHosts(@PathParam("hosts") String hosts) {
		boolean rs = validateHosts(hosts);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	private static boolean validateHosts(String hosts) {
		if (StringUtil.isEmpty(hosts)) {
			return true;
		}
		String[] hostArr = hosts.trim().split(",");
		if (hostArr != null && hostArr.length > 0) {
			for (String h : hostArr) {
				if (StringUtil.isEmpty(h)) {
					continue;
				}
				String[] ips = h.split("\\.");
				if (ips != null && ips.length == 4) {
					for (int i = 0; i < ips.length; i++) {
						// 127.0.0.1
						int v = Integer.parseInt(ips[i]);
						if (v < 0 || v > 255) {
							return false;
						}
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createNginx")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createNginx(Order order) {
		// 创建订单，并提交自动审批 返回订单id
		if (order != null) {
			order = OrderManagerUtils.createOrder(order);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", order.getOrderId()); //$NON-NLS-1$
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
	@Path("/jettyCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listJettyCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		JettyCluster criteria = new JettyCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<JettyCluster> list = JettyClusterMgrUtil.getJettyClusters(
				criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new JettyCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getJettyClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJettyClusterDetail(
			@PathParam("clusterId") String clusterId) {
		JettyCluster cluster = JettyClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("jettyService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJettyServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<JettyService> list = srvQueryMgr.getByCluster(clusterId, JettyService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new JettyService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeJettyService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeJettyService(@PathParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.removeService(serviceId, JettyService.TYPE);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param num
	 * @param type
	 * @return
	 */
	@Path("/srvManulIncStretch")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response srvManulIncStretch(
			@FormParam("clusterId") String clusterId,
			@FormParam("num") String num, @FormParam("type") String type) {
		String resultStr = SrvStretchMgrUtil.manulIncStretch(clusterId, type,
				Integer.parseInt(num));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", resultStr); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param num
	 * @param type
	 * @return
	 */
	@Path("/srvManulDecStretch")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response srvManulDecStretch(
			@FormParam("clusterId") String clusterId,
			@FormParam("num") String num, @FormParam("type") String type) {
		String resultStr = SrvStretchMgrUtil.manulDecStretch(clusterId, type,
				Integer.parseInt(num));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", resultStr); //$NON-NLS-1$
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
	@Path("/tomcatCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listTomcatCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		TomcatCluster criteria = new TomcatCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<TomcatCluster> list = TomcatClusterMgrUtil.getTomcatClusters(
				criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new TomcatCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getTomcatClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTomcatClusterDetail(
			@PathParam("clusterId") String clusterId) {
		TomcatCluster cluster = TomcatClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("tomcatService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTomcatServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<TomcatService> list = srvQueryMgr.getByCluster(clusterId, TomcatService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new TomcatService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	@Path("removeTomcatService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeTomcatService(@PathParam("serviceId") String serviceId) {
		boolean rs = ServiceManagerUtil.removeService(serviceId, TomcatService.TYPE);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
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
	@Path("/redisCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listRedisCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		RedisCluster criteria = new RedisCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<RedisCluster> list = RedisClusterMgrUtil.getRedisClusters(
				criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new RedisCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getRedisClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRedisClusterDetail(
			@PathParam("clusterId") String clusterId) {
		RedisCluster cluster = RedisClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("redisService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRedisServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<RedisService> list = srvQueryMgr.getByCluster(clusterId, RedisService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new RedisService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeRedisService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeRedisService(@PathParam("serviceId") String serviceId) {
		
		Order order = new Order();
		order.setOrderType(SingleDestroyServiceOrderProcessor.TYPE);
		
		OrderItem item = new OrderItem();
		item.setItemType(OrderItem.ITEM_TYPE_DEFAULT);
		
		OrderItemAttr attr = new OrderItemAttr();
		attr.setAttrName(OrderItemAttr.ATTR_SERVICE_ID);
		attr.setAttrValue(serviceId);
		
		item.addItemAttr(attr);
		
		order.addItem(item);
		
		// 创建订单，并提交自动审批 返回订单id
		order = OrderManagerUtils.submitOrder(order, true);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", Boolean.TRUE); //$NON-NLS-1$
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
			// order = SrvApplyUtil.createInnerService(order);
			if (StringUtil.isEmpty(order.getOrderType())) {
				order.setOrderType(SingleCreateRedisOrderProcessor.TYPE);
			}
			// 创建订单，并提交自动审批 返回订单id
			order = OrderManagerUtils.submitOrder(order, true);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", null == order ? null : order.getOrderId()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	@Path("existsRedisAliasName/{aliasName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response existsRedisAliasName(@PathParam("aliasName") String aliasName) {
		boolean rs = false;
		if (StringUtil.isNotEmpty(aliasName)) {
			List<RedisCluster> clusters = ClusterManagerFactory.getManager().getByType(RedisCluster.TYPE, RedisCluster.class);
			if (null != clusters && !clusters.isEmpty()) {
				for (RedisCluster cluster : clusters) {
					if (aliasName.equals(cluster.getAliasName())) {
						rs = true;
						break;
					}
				}
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs);
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
	@Path("/gatewayCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listGatewayCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		GatewayCluster criteria = new GatewayCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<GatewayCluster> list = GatewayClusterMgrUtil.getGatewayClusters(
				criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new GatewayCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getGatewayClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGatewayClusterDetail(
			@PathParam("clusterId") String clusterId) {
		GatewayCluster cluster = GatewayClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("gatewayService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGatewayServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<GatewayService> list = srvQueryMgr.getByCluster(clusterId, GatewayService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new GatewayService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeGatewayService")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public Response removeGatewayService(@FormParam("clusterId") String clusterId) {
		
		Order order = new Order();
		order.setOrderType(SingleDestroyServiceOrderProcessor.TYPE);
		
		OrderItem item = new OrderItem();
		item.setItemType(OrderItem.ITEM_TYPE_DEFAULT);
		
		OrderItemAttr attr = new OrderItemAttr();
		attr.setAttrName(OrderItemAttr.ATTR_CLUSTER_ID);
		attr.setAttrValue(clusterId);
		
		item.addItemAttr(attr);
		
		order.addItem(item);
		
		// 创建订单，并提交自动审批 返回订单id
		order = OrderManagerUtils.submitOrder(order, true);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", null != order); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createGateway")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createGateway(Order order) {
		if (order != null) {
			if (StringUtil.isEmpty(order.getOrderType())) {
				order.setOrderType(CreateGatewayOrderProcessor.TYPE);
			}
			// 创建订单，并提交自动审批 返回订单id
			order = OrderManagerUtils.submitOrder(order, true);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", null == order ? null : order.getOrderId()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("updateGateway")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response updateGateway(Order order) {
		if (order != null) {
			if (StringUtil.isEmpty(order.getOrderType())) {
				order.setOrderType(UpdateGatewayOrderProcessor.TYPE);
			}
			// 创建订单，并提交自动审批 返回订单id
			order = OrderManagerUtils.submitOrder(order, true);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", null == order ? null : order.getOrderId()); //$NON-NLS-1$
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
	@Path("/redisSentinelCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listRedisSentinelCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		RedisSentinelCluster criteria = new RedisSentinelCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<RedisSentinelCluster> list = RedisSentinelClusterMgrUtil.getRedisClusters(
				criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new RedisSentinelCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getRedisSentinelClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRedisSentinelClusterDetail(
			@PathParam("clusterId") String clusterId) {
		RedisSentinelCluster cluster = RedisSentinelClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("redisSentinelService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRedisSentinelServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<RedisSentinelService> list = srvQueryMgr.getByCluster(clusterId, RedisSentinelService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new RedisSentinelService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("removeRedisSentinelService")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public Response removeRedisSentinelService(@FormParam("clusterId") String clusterId) {
		
		Order order = new Order();
		order.setOrderType(SingleDestroyServiceOrderProcessor.TYPE);
		
		OrderItem item = new OrderItem();
		item.setItemType(OrderItem.ITEM_TYPE_DEFAULT);
		
		OrderItemAttr attr = new OrderItemAttr();
		attr.setAttrName(OrderItemAttr.ATTR_CLUSTER_ID);
		attr.setAttrValue(clusterId);
		
		item.addItemAttr(attr);
		
		order.addItem(item);
		
		// 创建订单，并提交自动审批 返回订单id
		order = OrderManagerUtils.submitOrder(order, true);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", Boolean.TRUE); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createRedisSentinel")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createSentinelRedis(Order order) {
		if (order != null) {
			if (StringUtil.isEmpty(order.getOrderType())) {
				order.setOrderType(CreateRedisSentinelOrderProcessor.TYPE);
			}
			// 创建订单，并提交自动审批 返回订单id
			order = OrderManagerUtils.submitOrder(order, true /* 自动审批 */);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", null == order ? null : order.getOrderId()); //$NON-NLS-1$
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
	@Path("/jobCtrlCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listJobCtrlCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		JobCtrlCluster criteria = new JobCtrlCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<JobCtrlCluster> list = JobCtrlClusterMgrUtil.getClusters(criteria,
				pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new JobCtrlCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getJobCtrlClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJobCtrlClusterDetail(
			@PathParam("clusterId") String clusterId) {
		JobCtrlCluster cluster = JobCtrlClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("jobCtrlService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJobCtrlServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<JobCtrlService> list = srvQueryMgr.getByCluster(clusterId,
				JobCtrlService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new JobCtrlService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createJobCtrl")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createJobCtrl(Order order) {
		if (order != null) {
			if (StringUtil.isEmpty(order.getOrderType())) {
				order.setOrderType(CreateJobCtrlOrderProcessor.TYPE);
			}
			// 创建订单，并提交自动审批 返回订单id
			order = OrderManagerUtils.submitOrder(order, true /* 自动审批 */);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", null == order ? null : order.getOrderId()); //$NON-NLS-1$
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
	@Path("/esbCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listEsbCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		EsbCluster criteria = new EsbCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<EsbCluster> list = EsbClusterMgrUtil.getClusters(criteria,
				pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new EsbCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getEsbClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEsbClusterDetail(
			@PathParam("clusterId") String clusterId) {
		EsbCluster cluster = EsbClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("esbService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEsbServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		IService[] list = srvQueryMgr.getByCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	@Path("createEsb")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response createEsb(Order order) {
		if (order != null) {
			if (StringUtil.isEmpty(order.getOrderType())) {
				order.setOrderType(CreateEsbOrderProcessor.TYPE);
			}
			// 创建订单，并提交自动审批 返回订单id
			order = OrderManagerUtils.submitOrder(order, true /* 自动审批 */);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", null == order ? null : order.getOrderId()); //$NON-NLS-1$
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
	@Path("/msgQueueCluster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response listMsgQueueCluster(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		MsgQueueCluster criteria = new MsgQueueCluster();
		criteria.setName(null);
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.getAttributes().put(
							"status", jsObj.getString("status")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		List<MsgQueueCluster> list = MsgQueueClusterMgrUtil.getMsgQueueClusters(
				criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new MsgQueueCluster[list.size()])); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getMsgQueueClusterDetail/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMsgQueueClusterDetail(
			@PathParam("clusterId") String clusterId) {
		MsgQueueCluster cluster = MsgQueueClusterMgrUtil.getCluster(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cluster); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("msgQueueService/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMsgQueueServiceByClusterId(
			@PathParam("clusterId") String clusterId) {
		List<MsgQueueService> list = srvQueryMgr.getByCluster(clusterId, MsgQueueService.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new MsgQueueService[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	@Path("removeMsgQueueService/{serviceId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeMsgQueueService(@PathParam("serviceId") String serviceId) {
		
		Order order = new Order();
		order.setOrderType(SingleDestroyServiceOrderProcessor.TYPE);
		
		OrderItem item = new OrderItem();
		item.setItemType(OrderItem.ITEM_TYPE_DEFAULT);
		
		OrderItemAttr attr = new OrderItemAttr();
		attr.setAttrName(OrderItemAttr.ATTR_SERVICE_ID);
		attr.setAttrValue(serviceId);
		
		item.addItemAttr(attr);
		
		order.addItem(item);
		
		// 创建订单，并提交自动审批 返回订单id
		order = OrderManagerUtils.submitOrder(order, true);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", Boolean.TRUE); //$NON-NLS-1$
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
			// 创建订单，并提交自动审批 返回订单id
			order = OrderManagerUtils.submitOrder(order, true);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", null == order ? null : order.getOrderId()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
}