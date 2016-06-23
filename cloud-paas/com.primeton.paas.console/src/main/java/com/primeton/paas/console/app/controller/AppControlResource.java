/**
 *
 */
package com.primeton.paas.console.app.controller;

import java.util.ArrayList;
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

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.app.service.util.AppUtil;
import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.cluster.TomcatCluster;
import com.primeton.paas.manage.api.cluster.WarCluster;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.exception.UndeployException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IJettyServiceManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.ITomcatServiceManager;
import com.primeton.paas.manage.api.manager.IWarServiceManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.service.HaProxyService;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.TomcatService;
import com.primeton.paas.manage.api.service.WarService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/appControl")
public class AppControlResource {
	
	private static ILogger logger = LoggerFactory.getLogger(AppCertResource.class);

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static IServiceQuery srvQueryMgr = ServiceManagerFactory
			.getServiceQuery();

	private static IClusterManager jettyClusterManager = ClusterManagerFactory
			.getManager(JettyService.TYPE);

	private static IClusterManager tomcatClusterManager = ClusterManagerFactory
			.getManager(TomcatService.TYPE);

	private static IJettyServiceManager jettyServiceManager = ServiceManagerFactory
			.getManager(JettyService.TYPE);

	private static ITomcatServiceManager tomcatServiceManager = ServiceManagerFactory
			.getManager(TomcatService.TYPE);

	private static IWarServiceManager warServiceMgr = ServiceManagerFactory
			.getManager(WarService.TYPE);

	private static IServiceQuery serviceQuery = ServiceManagerFactory
			.getServiceQuery();

	/**
	 * 查询应用
	 * 
	 * @param user
	 * @return 应用列表
	 */
	public static List<WebApp> queryApps(String user) {
		List<WebApp> appList = new ArrayList<WebApp>();
		if (StringUtil.isEmpty(user)) {
			return appList;
		}
		WebApp[] apps = AppUtil.getWebapps(user);
		if (apps != null && apps.length > 0) {
			for (WebApp app : apps) {
				try {
					if (app.getState() != WebApp.STATE_OPEND) {
						continue;
					}
					int status = JettyService.STATE_NOT_RUNNING;
					ICluster serverCluster = clusterManager.getByApp(
							app.getName(), JettyCluster.TYPE);
					if (serverCluster != null) {
						List<JettyService> jettyServices = srvQueryMgr
								.getByCluster(serverCluster.getId(),
										JettyService.class);
						for (JettyService s : jettyServices) {
							if (s.getState() == IService.STATE_RUNNING) {
								status = s.getState();
								break;
							}
						}
					}
					if (serverCluster == null) {
						serverCluster = clusterManager.getByApp(app.getName(),
								TomcatCluster.TYPE);
						List<TomcatService> tomcatServices = srvQueryMgr
								.getByCluster(serverCluster.getId(),
										TomcatService.class);
						for (TomcatService s : tomcatServices) {
							if (s.getState() == IService.STATE_RUNNING) {
								status = s.getState();
								break;
							}
						}
					}

					Map<String, String> attr = app.getAttributes();
					attr.put("state", String.valueOf(status)); //$NON-NLS-1$ // 应用状态
					app.setAttributes(attr);

					// app.getAttributes().put("protocal",
					// MyApplicationClient.queryProtocolType(app.getName()));
					HaProxyCluster haCluster = MyAppResource
							.queryHaProxyCluster(app.getName());
					if (null != haCluster) {
						app.getAttributes().put(
								"protocal", haCluster.getProtocolType()); //$NON-NLS-1$
						if (haCluster.getIsEnableDomain().equals("N")) { //$NON-NLS-1$
							String ip = "";
							// HaProxy集群实例
							List<HaProxyService> services = serviceQuery
									.getByCluster(haCluster.getId(),
											HaProxyService.class);
							for (HaProxyService s : services) {
								ip = s.getIp() + ":" + s.getPort();
								if (IService.HA_MODE_MASTER.equals(s
										.getHaMode())) {
									ip = s.getIp() + ":" + s.getPort();
									break;
								}
							}
							app.getAttributes().put(
									"protocal", HaProxyCluster.PROTOCOL_HTTP); //$NON-NLS-1$
							app.setSecondaryDomain(ip);
						}

					} else {
						app.getAttributes().put("protocal", null); //$NON-NLS-1$
					}
					appList.add(app);
				} catch (Exception e) {
					continue;
				}
			}
		}
		return appList;
	}

	/**
	 * 启动应用 <br>
	 * @param appName  应用标识
	 * @return 成功则返回true,否则返回false
	 */
	public static boolean doStartApp(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return false;
		}
		String type = JettyCluster.TYPE;
		ICluster cluster = clusterManager.getByApp(appName,type);
		if (cluster == null) {
			type = TomcatCluster.TYPE;
			cluster = clusterManager.getByApp(appName, type);
		}
		String clusterId = cluster == null ? null : cluster.getId();
		if (StringUtil.isEmpty(clusterId)) {
			return false;
		}
		try {
			if (type.equals(JettyCluster.TYPE)) {
				jettyClusterManager.start(clusterId);
			} if (type.equals(TomcatCluster.TYPE)) {
				tomcatClusterManager.start(clusterId);
			} else {
				clusterManager.start(clusterId);
			}
		} catch (ServiceException e) {
			logger.error("Start app {0} error.", new Object[] {appName}, e);
			return false;
		}
		return true;
	}

	
	/**
	 * 停止应用 <br>
	 * @param appName  应用标识
	 * @return 成功则返回true,否则返回false
	 */
	public static boolean doStopApp(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return false;
		}
		String type = JettyCluster.TYPE;
		ICluster cluster = clusterManager.getByApp(appName,type);
		if (cluster == null) {
			type = TomcatCluster.TYPE;
			cluster = clusterManager.getByApp(appName, type);
		}
		String clusterId = cluster==null ? null : cluster.getId();
		if (StringUtil.isEmpty(clusterId)) {
			return false;
		}
		try {
			if (type.equals(JettyCluster.TYPE)) {
				jettyClusterManager.stop(clusterId);
			} else if (type.equals(TomcatCluster.TYPE)) {
				tomcatClusterManager.stop(clusterId);
			} else {
				clusterManager.stop(clusterId);
			}
		} catch (ServiceException e) {
			logger.error("Stop app {0} error.", new Object[] {appName}, e);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 重启应用 <br>
	 * 
	 * @param appName  应用标识
	 * @return 成功则返回true,否则返回false
	 */
	public static boolean doRestartApp(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return false;
		}
		String type = JettyCluster.TYPE;
		ICluster cluster = clusterManager.getByApp(appName,type);
		if (cluster == null) {
			type = TomcatCluster.TYPE;
			cluster = clusterManager.getByApp(appName, type);
		}
		String clusterId = cluster==null ? null : cluster.getId();
		
		if (StringUtil.isEmpty(clusterId)) {
			return false;
		}
		try {
			if (JettyCluster.TYPE.equals(type)) {
				jettyClusterManager.restart(clusterId);}
			else if (TomcatCluster.TYPE.equals(type)){
				tomcatClusterManager.restart(clusterId);
			} else {
				clusterManager.restart(clusterId);
			}
		} catch (ServiceException e) {
			logger.error("Restart app {0} error.", new Object[] {appName}, e);
			return false;
		}
		return true;
	}
	
	/**
	 * 卸载应用
	 * @param appName 应用标识
	 * @return	卸载成功返回true，否则返回false
	 */
	public static boolean doUninstallApp(String appName) {
		if (StringUtil.isEmpty(appName) ){
			return false;
		}
		//获取jettyServices
		ICluster cluster = clusterManager.getByApp(appName, JettyCluster.TYPE);
		if (cluster == null) {
			cluster = clusterManager.getByApp(appName, TomcatCluster.TYPE);
			if (cluster == null) {
				return false;
			} else {
				return uninstallTomcatApp(appName,cluster);
			}
		}
		String jettyClusterId = cluster.getId();
		List<JettyService> services = srvQueryMgr.getByCluster(jettyClusterId,JettyService.class);
		if (services == null || services.isEmpty()) {
			return false;
		}
		for (JettyService jettyService : services) {
			try {
				jettyServiceManager.undeploy(jettyService.getId());
			} catch (UndeployException e) {
				logger.error("Undeploy app {0} error.", new Object[] {appName}, e);
				return false;
			}
		}
		try {
			jettyClusterManager.stop(jettyClusterId);
			jettyServiceManager.undeploy(services.get(0).getId());
			ICluster warCluster = clusterManager.getByApp(appName, WarCluster.TYPE);
			if (warCluster == null) {
				return true;
			}
			// Update warService(isdeployFlag)
			String warClusterId = warCluster.getId();
			List<WarService> versions = srvQueryMgr.getByCluster(warCluster.getId(), WarService.class);
			if (versions != null && !versions.isEmpty()) {
				for (WarService war : versions) {
					// 更改warService信息
					warServiceMgr.setWarDeployFlag(warClusterId, war.getId(),
							false);
				}
			}
			// Start Jetty Cluster
			jettyClusterManager.start(jettyClusterId);
		} catch (Throwable e) {
			logger.error(e);
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param appName
	 * @param cluster
	 * @return
	 */
	public static boolean uninstallTomcatApp(String appName, ICluster cluster) {
		String clusterId = cluster.getId();
		List<TomcatService> services = srvQueryMgr.getByCluster(clusterId, TomcatService.class);
		if (services == null || services.isEmpty()) {
			return false;
		}
		try {
			tomcatClusterManager.stop(clusterId);
			// 卸载所有实例
			for (TomcatService service : services) {
				try {
					tomcatServiceManager.undeploy(service.getId());
				} catch (Throwable t) {
					logger.error(t);
				}
			}
			ICluster warCluster = clusterManager.getByApp(appName, WarCluster.TYPE);
			if (warCluster == null) {
				return true;
			}
			
			// Update warService(isdeployFlag)
			String warClusterId = warCluster.getId();
			List<WarService> versions = srvQueryMgr.getByCluster(warCluster.getId(), WarService.class);
			if (versions != null && !versions.isEmpty()) {
				for (WarService war : versions) {
					//更改warService信息
					warServiceMgr.setWarDeployFlag(warClusterId,war.getId(),false);
				}
			}
			tomcatClusterManager.start(clusterId);
		} catch (Throwable e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/listApp")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response listApp(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex);
		pageCond.setLength(pageSize);

		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String currentUser = user.getUserId();

		List<WebApp> appList = queryApps(currentUser);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", appList); //$NON-NLS-1$
		result.put("total", appList.size()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("/startApp/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response startApp(@PathParam("appName") String appName) {
		boolean rtn = false;
		if (appName != null) {
			rtn = doStartApp(appName);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("/stopApp/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response stopApp(@PathParam("appName") String appName) {
		boolean rtn = false;
		if (appName != null) {
			rtn = doStopApp(appName);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("/restartApp/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response restartApp(@PathParam("appName") String appName) {
		boolean rtn = false;
		if (appName != null) {
			rtn = doRestartApp(appName);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("/uninstallApp/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallApp(@PathParam("appName") String appName) {
		boolean rtn = false;
		if (appName != null) {
			rtn = doUninstallApp(appName);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
}
