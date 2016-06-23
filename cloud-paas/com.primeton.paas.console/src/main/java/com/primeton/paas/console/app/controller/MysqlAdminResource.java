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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.service.MySQLService;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/mysqlAdmin")
public class MysqlAdminResource {
	
	private static IServiceQuery srvQueryMgr = ServiceManagerFactory.getServiceQuery();
	
	/**
	 * 获取登陆phpMyAdmin的URL <br>
	 * @param host  主机
	 * @param username  数据库用户名
	 * @param password  登陆密码
	 * @return
	 */
	private static String getMySqlURL(String host) {
		String url = SystemVariable.getPhpMyAdminURL(); // http://192.168.100.219:8088/phpmyadmin/index.php
		if (url == null || url.trim().length() == 0) {
			return null;
		}
		if (host == null)
			return url;
		String[] array = host.split("\\."); //$NON-NLS-1$
		if (array.length != 4)
			return url;
		// 192.168.100.30 => index = 192 * 1000000000 + 168 * 1000000 + 100 * 1000 + 30 => index = 192168100030
		long index = Long.parseLong(array[0]) * 1000000000L + Long.parseLong(array[1]) * 1000000L + Long.parseLong(array[2]) * 1000L + Long.parseLong(array[3]);
		// url += "?db=" + ServiceConstants.PAAS_DB_SERVICE_DB_NAME;
		url += "?server=" + index; //$NON-NLS-1$
		// url += "&pma_username=" + username;
		// url += "&pma_password=" + password;
		// http://192.168.100.1/phpmyadmin/index.php?db=paas_db&server=192168100001
		return url;
	}
	
	/**
	 * 获得已经开通的数据库服务 <br>
	 * 
	 * @param userID PaaS平台租户ID
	 * @return
	 */
	public static List<MySQLService> getMySqlServices(String userID, IPageCond pageCond) {
		List<MySQLService> instances = new ArrayList<MySQLService>();
		if (userID == null || userID.trim().length() == 0) {
			return instances;
		}
		instances = srvQueryMgr.getByOwner(userID, MySQLService.TYPE, pageCond, MySQLService.class);
		if (instances == null || instances.isEmpty()) {
			return new ArrayList<MySQLService>();
		}
		for (MySQLService instance : instances) {
			String ip = instance.getIp();
			String url = getMySqlURL(ip);
			instance.getAttributes().put("phpMyAdminURL", url); //$NON-NLS-1$
		}
		return instances;
	}

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
	public Response list(@FormParam("pageIndex") int pageIndex
							  ,@FormParam("pageSize") int pageSize
							  ,@FormParam("keyData") String keyData) {
		
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex*pageSize);
		pageCond.setLength(pageSize);
		
 		IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
 		String currentUser = user.getUserId();
		
		List<MySQLService> mySQLServiceList = getMySqlServices(currentUser, null);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", mySQLServiceList); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
}
