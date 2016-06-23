/**
 *
 */
package com.primeton.paas.console.app.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.config.model.UserLogModel;
import com.primeton.paas.console.app.service.util.AppLogHelper;
import com.primeton.paas.console.app.service.util.AppUtil;
import com.primeton.paas.console.app.service.util.FileUtil;
import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.util.RuntimeExec;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 应用日志下载. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/appLog")
public class AppLogResource {
	
	private static ILogger logger = LoggerFactory.getLogger(AppLogResource.class);
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	public static String getLogURL(String appName) {
		if (null == appName || appName.trim().length() == 0) {
			return null;
		}
		String logType = "user"; //$NON-NLS-1$
		String logRootPath = SystemVariables.getLogRootPath();

		//${logRootPath}/error.txt
		String errorFile = logRootPath + File.separator + "error.txt";
		
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(logRootPath)) {
			return errorFile;
		}
		//${logRootPath}/sample/tmp/sample.zip
		String zip = logRootPath + File.separator + appName + File.separator
				+ "tmp" + File.separator + appName + "_" + logType + ".zip"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		//opt/upaas/bin/Console/zip-appLog.sh sample user
		String cmd = SystemVariables.getBinHome()
				+ "/Console/bin/zip-appLog.sh " + appName + " " + logType + " " + logRootPath; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		RuntimeExec exec = new RuntimeExec();
		try {
			exec.execute(cmd, 1000L*60);
			File logZip = new File(zip);
			if (logZip.exists() && logZip.isFile()) {
				return zip;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return errorFile;
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	public static String getLogURL(String appName, String logType) {
		String logRootPath = SystemVariables.getLogRootPath();
		// ${logRootPath}/error.txt
		String errorFile = logRootPath + File.separator + "error.txt";
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(logRootPath)) {
			return errorFile;
		}
		if (StringUtil.isEmpty(logType) || "null".equalsIgnoreCase(logType)) {
			logType = "userall";
		}
		// ${logRootPath}/sample/tmp/sample.zip
		String zip = logRootPath + File.separator + appName + File.separator
				+ "tmp" + File.separator + appName + "_" + logType + ".zip"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		// zip-appLog.sh sample user
		String cmd = SystemVariables.getBinHome()
				+ "/Console/bin/zip-appLog.sh " + appName + " " + logType + " " + logRootPath; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		RuntimeExec exec = new RuntimeExec();
		try {
			exec.execute(cmd, 1000L * 60);
			File logZip = new File(zip);
			if (logZip.exists() && logZip.isFile()) {
				return zip;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return errorFile;
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

 		IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
 		String currentUser = user.getUserId();

		List<WebApp> appList = AppUtil.queryOpendApps(currentUser,pageCond);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", appList); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("/getUserLogs")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getUserLogs(@FormParam("appName") String appName) {
		//获取用户日志类型
		UserLogModel ulm = AppLogHelper.queryAppLog(appName);
		Map<String,String> userlogs = new HashMap<String, String>();
		if(ulm!=null){
			userlogs = ulm.getUserLogs();
		}
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		for (String type : userlogs.keySet()) {
			Map<String,String> rs = new HashMap<String, String>();
			rs.put("logType", type); //$NON-NLS-1$
			rs.put("appName", appName); //$NON-NLS-1$
			list.add(rs);
		}
		// 老应用（只有一种日志）
		if (list.isEmpty()) {
			Map<String,String> rs = new HashMap<String, String>();
			rs.put("logType", "user"); //$NON-NLS-1$
			rs.put("appName", appName); //$NON-NLS-1$
			list.add(rs);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @param postfix
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
   @Path("/download/{appName}.{postfix}")
   @GET
   @Produces(MediaType.APPLICATION_OCTET_STREAM)
	public byte[] download(@PathParam("appName") String appName,
			@PathParam("postfix") String postfix,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		String path = getLogURL(appName);
		byte[] fb = FileUtil.readFile(path);
		response.setHeader("Content-Disposition", "attachment;filename=" + appName + "_user." + postfix); //$NON-NLS-1$  //$NON-NLS-2$
		response.addHeader("content-type", "application/" + postfix); //$NON-NLS-1$ //$NON-NLS-2$
		return fb;
	}
   
   /**
    * 
    * @param appName
    * @param logType
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   @Path("/multidownload/{appName}.{logType}")
   @GET
   @Produces(MediaType.APPLICATION_OCTET_STREAM)
	public byte[] multidownload(@PathParam("appName") String appName,
			@PathParam("logType") String logType,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		if (StringUtil.isEmpty(logType) || "null".equalsIgnoreCase(logType)) { //$NON-NLS-1$
			logType = "userall"; //$NON-NLS-1$
		}
		String path = getLogURL(appName, logType);
		byte[] fb = FileUtil.readFile(path);
		String postfix = "txt"; //$NON-NLS-1$
		if (path != null && path.split("\\.").length >= 2) {
			postfix = path.split("\\.")[1];
		}
		response.setHeader("Content-Disposition", "attachment;filename=" //$NON-NLS-1$ //$NON-NLS-2$
				+ appName + "_user_" + logType + "." + postfix); //$NON-NLS-1$ //$NON-NLS-2$
		response.addHeader("content-type", "application/" + postfix); //$NON-NLS-1$ //$NON-NLS-2$
		return fb;
   }
   
}
