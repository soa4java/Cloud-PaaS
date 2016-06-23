/**
 * 
 */
package com.primeton.paas.console.coframe.controller;

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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.primeton.paas.console.coframe.util.CapRoleMgrUtil;
import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.manage.api.model.CapFunction;
import com.primeton.paas.manage.api.model.CapRole;
import com.primeton.paas.manage.api.model.PageCond;

/**
 * 角色授权管理
 * 
 * @author liuyi(liu-yi@primeton.com)
 * 
 */
@Path("/authrole")
public class AuthAccreditMgrResource {
	
	@Path("/rolelist")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response roleList(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		CapRole criteria = new CapRole();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setRoleCode(jsObj.getString("roleCode")); //$NON-NLS-1$
				criteria.setRoleName(jsObj.getString("roleName")); //$NON-NLS-1$
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		CapRole[] roles = CapRoleMgrUtil.getRoles(criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", roles); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("getCapRoleByUserId/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCapRoleByUserId(@PathParam("userId") String userId) {
		CapRole[] role = CapRoleMgrUtil.getCapRoleByUserId(userId);
		ResponseBuilder builder = Response.ok(role);
		return builder.build();
	}

	@Path("getInverCapRoleByUserId/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInverCapRoleByUserId(@PathParam("userId") String userId) {
		CapRole[] role = CapRoleMgrUtil.getInverCapRoleByUserId(userId);
		ResponseBuilder builder = Response.ok(role);
		return builder.build();
	}

	@Path("getRoleByRoleCode/{roleCode}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoleByRoleCode(@PathParam("roleCode") String roleCode) {
		CapRole role = CapRoleMgrUtil.getRoleByRoleCode(roleCode);
		ResponseBuilder builder = Response.ok(role);
		return builder.build();
	}

	@Path("saveRoleAndFunctionAuth/{roleId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response saveRoleAndFunctionAuth(
			@FormParam("keyData") String keyData,
			@PathParam("roleId") String roleId) {
		List<CapFunction> list = new ArrayList<CapFunction>();
		if (null != keyData) {
			JSONArray roleJSON = JSONArray.fromObject(keyData);
			Object[] objArray = roleJSON.toArray();
			for (Object o : objArray) {
				JSONObject obj = JSONObject.fromObject(o);
				String strDate=String.valueOf(obj.get("createtime"));  
		        Date newDate= new Date(Long.parseLong(strDate));
		        //移除原有的createtime属性  
		        obj.remove("createtime");   //$NON-NLS-1$
		        //将日期类型的createtime放到obj中  
		        obj.put("createtime", newDate); //$NON-NLS-1$
				CapFunction func = (CapFunction) (JSONObject.toBean(obj,CapFunction.class));
				list.add(func);
			}
		}
		// 保存授权信息
		boolean ifSuccess = CapRoleMgrUtil.saveRoleAndFunctionAuth(list, roleId);
		// boolean ifSuccess = false;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", ifSuccess); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("checkRoleCodeExist/{roleCode}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkRoleCode(@PathParam("roleCode") String roleCode) {
		boolean ifexits = CapRoleMgrUtil.checkRoleCodeIsExist(roleCode);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", ifexits); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("addCapRole")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRole(@FormParam("keyData") String keyData) {
		CapRole role = new CapRole();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				role.setRoleCode(jsObj.getString("roleCode")); //$NON-NLS-1$
				role.setRoleName(jsObj.getString("roleName")); //$NON-NLS-1$
				role.setRoleDesc(jsObj.getString("roleDesc")); //$NON-NLS-1$
			}
		}
		IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
		String currentUser = user.getUserId();
		if (role.getRoleCode() != null) {
			role.setCreateuser(currentUser);
		}
		boolean rs = CapRoleMgrUtil.addCapRole(role);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("updateCapRole")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCapRole(@FormParam("keyData") String keyData) {
		CapRole role = new CapRole();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				role.setRoleCode(jsObj.getString("roleCode")); //$NON-NLS-1$
				role.setRoleName(jsObj.getString("roleName")); //$NON-NLS-1$
				role.setRoleDesc(jsObj.getString("roleDesc")); //$NON-NLS-1$
			}
		}
		boolean rs = CapRoleMgrUtil.updateCapRole(role);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("deleteRoles/{ids}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRoles(@PathParam("ids") String ids) {
		String[] roleIds = ids.split(","); //$NON-NLS-1$
		boolean rs = CapRoleMgrUtil.deleteRoles(roleIds);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
}
