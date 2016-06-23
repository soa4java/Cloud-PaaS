/**
 * 
 */
package com.primeton.paas.console.platform.controller;

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

import net.sf.json.JSONObject;

import com.primeton.paas.app.config.model.UserLogModel;
import com.primeton.paas.console.app.service.util.AppLogHelper;
import com.primeton.paas.console.app.service.util.FileUtil;
import com.primeton.paas.console.platform.service.monitor.LogDownLoadUtil;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.WebApp;

/**
 * 
 * @author liu-yi (mailto:liu-yi@primeton.com)
 *
 */
@Path("/log")
public class LogMgrResource {
	
	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
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
			JSONObject  jsObj = JSONObject.fromObject(keyData) ;
			if (null != jsObj) {
				criteria.setName(jsObj.getString("name")); //$NON-NLS-1$
				criteria.setDisplayName(jsObj.getString("displayName")); //$NON-NLS-1$
				criteria.setOwner(jsObj.getString("owner")); //$NON-NLS-1$
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex*pageSize);
		pageCond.setLength(pageSize);
		WebApp[] apps = LogDownLoadUtil.queryInnerApps(criteria, pageCond);
		if (apps == null || apps.length == 0) {
			apps = new WebApp[0];
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", apps); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("/getAllLogs")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getAllLogs(@FormParam("appName") String appName) {
		//获取日志类型
		UserLogModel ulm = AppLogHelper.queryAppLog(appName);
		Map<String,String> userlogs = new HashMap<String, String>();
		if(ulm!=null){
			userlogs = ulm.getUserLogs();
		}
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for (String type : userlogs.keySet()) {
			Map<String,String> rs = new HashMap<String, String>();
			rs.put("logType", type);
			rs.put("appName", appName);
			list.add(rs);
		}
		
		//老应用（只有一种用户日志）日志下载
		if(list.isEmpty()){
			Map<String,String> rs = new HashMap<String, String>();
			rs.put("logType", "user");
			rs.put("appName", appName);
			list.add(rs);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
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
	@Path("/download/{appName}.{logType}")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public byte[] multidownload(@PathParam("appName") String appName,
			@PathParam("logType") String logType,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		String path = LogDownLoadUtil.getAppLogURL(appName, logType);
		if (logType == null || "null".equals(logType)) {
			logType = "userall";
		}
		byte [] fb = FileUtil.readFile(path);
		String postfix = "txt"; 
		if (path != null && path.split("\\.").length >= 2) {
			postfix = path.split("\\.")[1];
		}
		if ("system".equals(logType)) {
			response.setHeader("Content-Disposition", "attachment;filename="
					+ appName + "_" + logType + "." + postfix);
		} else {
			response.setHeader("Content-Disposition", "attachment;filename="
					+ appName + "_user_" + logType + "." + postfix);
		}
		response.addHeader("content-type", "application/" + postfix);
		return fb;
	}
	
	/**
	 * 
	 * @param consoleType
	 * @param logType
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Path("/downloadConsoleLog/{consoleType}.{logType}")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public byte[] downloadConsoleLog(
			@PathParam("consoleType") String consoleType,
			@PathParam("logType") String logType,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		String path = LogDownLoadUtil.getPlatformLogURL(consoleType, logType);
		byte[] fb = FileUtil.readFile(path);
		String postfix = "txt";
		if (path != null && path.split("\\.").length >= 2) {
			postfix = path.split("\\.")[1];
		}
		response.setHeader("Content-Disposition", "attachment;filename="
				+ consoleType + "_" + logType + "." + postfix);
		response.addHeader("content-type", "application/" + postfix);
		return fb;
	}
	
}
