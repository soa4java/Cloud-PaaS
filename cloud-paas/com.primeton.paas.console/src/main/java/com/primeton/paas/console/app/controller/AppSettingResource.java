/**
 *
 */
package com.primeton.paas.console.app.controller;

import java.util.ArrayList;
import java.util.Arrays;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.config.model.DataSourceModel;
import com.primeton.paas.app.config.model.UserLogModel;
import com.primeton.paas.app.config.model.VariableModel;
import com.primeton.paas.console.app.service.util.AppLogHelper;
import com.primeton.paas.console.app.service.util.AppUtil;
import com.primeton.paas.console.app.service.util.DataSourceUtil;
import com.primeton.paas.console.app.service.util.LogModel;
import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.cluster.MySQLCluster;
import com.primeton.paas.manage.api.cluster.TomcatCluster;
import com.primeton.paas.manage.api.config.AppConfigManagerFactory;
import com.primeton.paas.manage.api.config.IVariableConfigManager;
import com.primeton.paas.manage.api.config.VariableConfig;
import com.primeton.paas.manage.api.exception.ConfigureException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/appSetting")
public class AppSettingResource {
	
	private static ILogger logger = LoggerFactory.getLogger(AppSettingResource.class);

	private static final int DEFAULT_USER_LOG_COUNT = 10;
	
	private static List<String> levels = new ArrayList<String>();
	
	static {
		levels.add(UserLogModel.LEVEL_DEBUG);
		levels.add(UserLogModel.LEVEL_INFO);
		levels.add(UserLogModel.LEVEL_WARN);
		levels.add(UserLogModel.LEVEL_ERROR);
	}
	
	private static IVariableConfigManager varConfigMgr = AppConfigManagerFactory
			.getVariableConfigManager();

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	/**
	 * 
	 * @param appName
	 * @return
	 */
	public static String getAppType(String appName) {
		ICluster cluster = clusterManager.getByApp(appName, JettyCluster.TYPE);
		if (null != cluster) {
			return JettyCluster.TYPE;
		}
		cluster = clusterManager.getByApp(appName, TomcatCluster.TYPE);
		if (null != cluster) {
			return TomcatCluster.TYPE;
		}
		return JettyCluster.TYPE;
	}

	/**
	 * 查询已开通且已启动的应用 <br>
	 * 
	 * @param userID
	 * @return 应用列表
	 */
	public static List<WebApp> queryApps(String userID) {
		List<WebApp> appList = new ArrayList<WebApp>();
		WebApp[] apps = AppUtil.getWebapps(userID);
		if (apps != null && apps.length > 0) {
			for (WebApp app : apps) {
				int appState = app.getState();
				// 只查询 “已开通” 的应用
				if (appState != WebApp.STATE_OPEND) {
					continue;
				}
				appList.add(app);
			}
			if (appList.size() > 0) {
				for (WebApp app : appList) {
					app.getAttributes()
							.put("serverType", AppUtil.queryServerType(app.getName())); //$NON-NLS-1$
				}
			}
		}
		return appList;
	}

	/**
	 * 查询我的应用的变量 <br>
	 * 
	 * @param appName
	 *            应用标识
	 * @return 应用变量列表
	 */
	public static List<VariableModel> queryAppVars(String appName) {
		List<VariableModel> varList = new ArrayList<VariableModel>();
		VariableConfig varConf = varConfigMgr.getVariableConfig(appName);
		if (varConf != null && varConf.getVariableModels() != null
				&& varConf.getVariableModels().length > 0) {
			return Arrays.asList(varConf.getVariableModels());
		}
		return varList;
	}

	/**
	 * 查询变量 <br>
	 * 
	 * @param appName
	 * @param varName
	 * @return true存在 false不存在
	 */
	public static boolean isExistVar(String appName, String varName) {
		VariableModel var = varConfigMgr.getVariableModel(appName, varName);
		if (var == null) {
			return false;
		}
		return true;
	}

	/**
	 * 判断变量是否存在 <br>
	 * 
	 * @param appName
	 * @param varName
	 * @return 应用变量
	 */
	public static VariableModel queryAppVar(String appName, String varName) {
		VariableModel var = varConfigMgr.getVariableModel(appName, varName);
		return var;
	}

	/**
	 * 添加应用变量 <br>
	 * 
	 * @param var
	 * @param appName
	 * @return 添加结果
	 */
	public static boolean doAddAppVar(VariableModel var, String appName) {
		try {
			varConfigMgr.addVariableModel(appName, var);
		} catch (ConfigureException e) {
			logger.error("Add app {0} variable {1} error", new Object[] { appName, var }, e);
			return false;
		}
		return true;
	}

	/**
	 * 移除应用变量 <br>
	 * 
	 * @param varName
	 *            变量名称
	 * @param appName
	 *            应用名称
	 * @return 移除结果
	 */
	public static boolean doRemoveAppVar(String varName, String appName) {
		try {
			varConfigMgr.removeVariableModel(appName, varName);
		} catch (ConfigureException e) {
			logger.error("Remove app {0} variable {1} error", new Object[] {
					appName, varName }, e);
			return false;
		}
		return true;
	}

	/**
	 * 移除应用变量 <br>
	 * 
	 * @param vars
	 *            变量
	 * @param appName
	 *            应用标识
	 * @return 移除结果
	 */

	public static boolean doRemoveAppVars(String vars, String appName) {
		if (StringUtil.isEmpty(vars) || StringUtil.isEmpty(appName)) {
			return false;
		}
		String[] varArray = vars.split(",");
		if (varArray != null && varArray.length > 0) {
			for (String varName : varArray) {
				try {
					varConfigMgr.removeVariableModel(appName, varName);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}
		return true;
	}

	/**
	 * 更新应用变量 <br>
	 * 
	 * @param var
	 * @param appName
	 * @return 更新结果
	 */
	public static boolean doUpdateAppVar(VariableModel var, String appName) {
		if (StringUtil.isEmpty(appName) || var == null) {
			return false;
		}
		try {
			varConfigMgr.updateVariableModel(appName, var);
		} catch (ConfigureException e) {
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
	@Path("/listAppVar")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response listAppVar(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {

		String appName = "";
		if (null != keyData) {
			appName = keyData;
		}

		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex);
		pageCond.setLength(pageSize);

		List<VariableModel> varList = queryAppVars(appName);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", varList); //$NON-NLS-1$
		result.put("total", varList.size()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @return
	 */
	@Path("/listApp")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listApp() {
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String currentUser = user.getUserId();
		List<WebApp> appList = queryApps(currentUser);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", appList); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @return
	 */
	@Path("/listAppLog")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAppLog() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", levels); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @return
	 */
	@Path("/listDBService")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listDBService() {
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String currentUser = user.getUserId();
		List<MySQLCluster> dbList = DataSourceUtil
				.queryAllDBService(currentUser);
		ResponseBuilder builder = Response.ok(dbList);
		return builder.build();
	}

	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/listAppDataSource")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response listAppDataSource(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		String appName = "";
		if (null != keyData) {
			appName = keyData;
		}
		List<DataSourceModel> dsList = DataSourceUtil
				.queryDataSources(appName);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", dsList); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param dsModel
	 * @param appName
	 * @return
	 */
	@Path("/addAppDataSource/{appName}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addAppDataSource(DataSourceModel dsModel,
			@PathParam("appName") String appName) {
		boolean rtn = false;
		if (dsModel != null && appName != null) {
			rtn = DataSourceUtil.addAppDataSource(appName, dsModel);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param dsModel
	 * @param appName
	 * @return
	 */
	@Path("/updateDataSource/{appName}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDataSource(DataSourceModel dsModel,
			@PathParam("appName") String appName) {
		boolean rtn = false;
		if (dsModel != null && appName != null) {
			rtn = DataSourceUtil.updateDataSource(appName, dsModel);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @param dsName
	 * @return
	 */
	@Path("/deleteAppDataSource/{appName}&{dsName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAppDataSource(@PathParam("appName") String appName,
			@PathParam("dsName") String dsName) {
		boolean rtn = false;
		if (appName != null) {
			rtn = DataSourceUtil.removeDataSource(appName, dsName);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param vModel
	 * @param appName
	 * @return
	 */
	@Path("/addAppVar/{appName}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addAppVar(VariableModel vModel,
			@PathParam("appName") String appName) {
		boolean rtn = false;
		if (vModel != null && appName != null) {
			rtn = doAddAppVar(vModel, appName);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param vModel
	 * @param appName
	 * @return
	 */
	@Path("/updateAppVar/{appName}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateAppVar(VariableModel vModel,
			@PathParam("appName") String appName) {
		boolean rtn = false;
		if (vModel != null && appName != null) {
			rtn = doUpdateAppVar(vModel, appName);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @param varName
	 * @return
	 */
	@Path("/deleteAppVar/{appName}&{varName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAppVar(@PathParam("appName") String appName,
			@PathParam("varName") String varName) {
		boolean rtn = false;
		if (appName != null) {
			rtn = doRemoveAppVars(varName, appName);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @param varName
	 * @return
	 */
	@Path("/isExistVarName/{appName}&{varName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response isExistVarName(@PathParam("appName") String appName,
			@PathParam("varName") String varName) {
		boolean rtn = false;
		if (appName != null) {
			VariableModel vm = queryAppVar(appName, varName);
			if (vm != null) {
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
	 * @param appName
	 * @param dataSourceName
	 * @return
	 */
	@Path("/isExistDataSource/{appName}&{dataSourceName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response isExistDataSource(@PathParam("appName") String appName,
			@PathParam("dataSourceName") String dataSourceName) {
		boolean rtn = false;
		if (appName != null) {
			rtn = DataSourceUtil
					.isExistDataSource(appName, dataSourceName);
		}
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
	@Path("/getAppLog")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getAppLog(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		String appName = keyData == null ? null : keyData;
		UserLogModel userLogModel = AppLogHelper.queryAppLog(appName);
		Map<String, String> userlogs = new HashMap<String, String>();
		if (userLogModel != null) {
			userlogs = userLogModel.getUserLogs();
		}
		List<LogModel> list = new ArrayList<LogModel>();
		for (Map.Entry<String, String> entry : userlogs.entrySet()) {
			list.add(new LogModel(entry.getKey(), entry.getValue()));
		}
		ResponseBuilder builder = Response.ok(list);
		return builder.build();
	}

	/**
	 * 
	 * @param keyData
	 * @param appName
	 * @return
	 */
	@Path("/updateAppLog/{appName}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateAppLog(@FormParam("keyData") String keyData,
			@PathParam("appName") String appName) {
		List<LogModel> list = new ArrayList<LogModel>();
		if (null != keyData) {
			JSONArray userlogsJSON = JSONArray.fromObject(keyData);
			Object[] objArray = userlogsJSON.toArray();
			if (objArray.length > 0) {
				for (Object o : objArray) {
					JSONObject obj = JSONObject.fromObject(o);
					String type = String.valueOf(obj.get("type")); //$NON-NLS-1$
					if (type == null) {
						continue;
					}
					String level = String.valueOf(obj.get("level")); //$NON-NLS-1$
					if (level == null) {
						level = "INFO"; //$NON-NLS-1$
					}
					LogModel ulsm = new LogModel(type, level);
					list.add(ulsm);
				}
			}
		}
		boolean rs = false;
		UserLogModel ulm = new UserLogModel();
		Map<String, String> userLogs = new HashMap<String, String>();
		for (LogModel u : list) {
			userLogs.put(u.getType(), u.getLevel());
		}
		ulm.setUserLogs(userLogs);
		// save ulm
		rs = AppLogHelper.appLogSave(appName, ulm);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @param logType
	 * @return
	 */
	@Path("/checkLogType/{appName}&{logType}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkLogType(@PathParam("appName") String appName,
			@PathParam("logType") String logType) {
		boolean rs = true;
		if (logType != null && appName != null) {
			UserLogModel userLogModel = AppLogHelper.queryAppLog(appName);
			Map<String, String> userlogs = new HashMap<String, String>();
			if (userLogModel != null) {
				userlogs = userLogModel.getUserLogs();
			}
			for (String key : userlogs.keySet()) {
				if (key.equalsIgnoreCase(logType)) {
					rs = false;
					break;
				}
			}

			if ("system".equals(logType)) { //$NON-NLS-1$
				rs = false;
			}
		} else {
			rs = false;
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param logModel
	 * @param appName
	 * @return
	 */
	@Path("/addUserLog/{appName}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUserLog(LogModel logModel,
			@PathParam("appName") String appName) {
		boolean rs = false;
		if (logModel != null && appName != null) {
			UserLogModel userLogModel = AppLogHelper.queryAppLog(appName);
			Map<String, String> userlogs = new HashMap<String, String>();
			if (userLogModel != null) {
				userlogs = userLogModel.getUserLogs();
			}
			if (userlogs.size() < DEFAULT_USER_LOG_COUNT) {
				rs = AppLogHelper.addUserLog(appName, logModel);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @param logType
	 * @return
	 */
	@Path("/deleteUserLog/{appName}&{logType}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUserLog(@PathParam("appName") String appName,
			@PathParam("logType") String logType) {
		boolean rs = false;
		if (appName != null && logType != null) {
			if (!UserLogModel.DEFAULT_USERLOG_TYPE.equalsIgnoreCase(logType)) {
				rs = AppLogHelper.deleteUserLog(appName, logType);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("/getAppServerType/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAppServerType(@PathParam("appName") String appName) {
		String serverType = "";
		if (appName != null) {
			serverType = AppUtil.queryServerType(appName);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", serverType); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

}
