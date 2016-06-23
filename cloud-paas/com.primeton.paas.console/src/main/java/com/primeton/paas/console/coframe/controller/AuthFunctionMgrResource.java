/**
 * 
 */
package com.primeton.paas.console.coframe.controller;

import java.util.HashMap;
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

import com.primeton.paas.console.coframe.util.CapFunctionMgrUtil;
import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.manage.api.impl.util.IAuthConstants;
import com.primeton.paas.manage.api.model.CapFunction;
import com.primeton.paas.manage.api.model.PageCond;

/**
 * 功能管理
 * 
 * @author liuyi(liu-yi@primeton.com)
 * 
 */
@Path("/authfunction")
public class AuthFunctionMgrResource {
	
	@Path("/funclist")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response funcList(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		CapFunction criteria = new CapFunction();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setFuncId(jsObj.getString("funcId")); //$NON-NLS-1$
				criteria.setFuncName(jsObj.getString("funcName")); //$NON-NLS-1$
				String funcType = jsObj.getString("funcType"); //$NON-NLS-1$
				if (!"defaultValue".equals(funcType)) { //$NON-NLS-1$
					criteria.setFuncType(funcType);
				}
				criteria.setFuncAction(jsObj.getString("funcAction")); //$NON-NLS-1$
				String isCheck = jsObj.getString("isCheck"); //$NON-NLS-1$
				if (!"defaultValue".equals(isCheck)) { //$NON-NLS-1$
					criteria.setIsCheck(isCheck);
				}
				String isMenu = jsObj.getString("isMenu"); //$NON-NLS-1$
				if (!"defaultValue".equals(isMenu)) { //$NON-NLS-1$
					criteria.setIsMenu(isMenu);
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		CapFunction[] functions = CapFunctionMgrUtil.getFunctions(criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", functions); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("/funclistForSelect")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response funcListForSelect(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		CapFunction criteria = new CapFunction();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setFuncId(jsObj.getString("funcId")); //$NON-NLS-1$
				criteria.setFuncName(jsObj.getString("funcName")); //$NON-NLS-1$
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		CapFunction[] functions = CapFunctionMgrUtil.getFunctions(criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", functions); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("checkFuncIdIsExist/{funcId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkFuncId(@PathParam("funcId") String funcId) {
		boolean ifexits = CapFunctionMgrUtil.checkFuncIdIsExist(funcId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", ifexits); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("getFunctionByFuncId/{funcId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFunctionByFuncId(@PathParam("funcId") String funcId) {
		CapFunction function = CapFunctionMgrUtil.getFunctionByFuncId(funcId);
		ResponseBuilder builder = Response.ok(function);
		return builder.build();
	}

	@Path("getCapFunctionByRoleId/{roleId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCapFunctionByRoleId(@PathParam("roleId") String roleId) {
		CapFunction[] funcs = CapFunctionMgrUtil.getCapFunctionByRoleId(roleId);
		ResponseBuilder builder = Response.ok(funcs);
		return builder.build();
	}

	@Path("getInverCapFunctionByRoleId/{roleId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInverCapFunctionByRoleId(
			@PathParam("roleId") String roleId) {
		CapFunction[] funcs = CapFunctionMgrUtil.getInverCapFunctionByRoleId(roleId);
		ResponseBuilder builder = Response.ok(funcs);
		return builder.build();
	}

	@Path("deleteFunctions/{ids}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteFunctions(@PathParam("ids") String ids) {
		String[] funcIds = ids.split(","); //$NON-NLS-1$
		boolean deleteOk = CapFunctionMgrUtil.deleteFunctions(funcIds);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", deleteOk); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("addCapFunction")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCapFunction(@FormParam("keyData") String keyData) {
		CapFunction func = new CapFunction();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				func.setFuncId(jsObj.getString("funcId")); //$NON-NLS-1$
				func.setFuncName(jsObj.getString("funcName")); //$NON-NLS-1$
				func.setFuncType(jsObj.getString("funcType")); //$NON-NLS-1$
				func.setIsCheck(jsObj.getString("isCheck")); //$NON-NLS-1$
				func.setIsMenu(jsObj.getString("isMenu")); //$NON-NLS-1$
				func.setFuncAction(jsObj.getString("funcAction")); //$NON-NLS-1$
				func.setFuncDesc(jsObj.getString("funcDesc")); //$NON-NLS-1$
				
				IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
				String currentUser = user.getUserId();
				
				func.setCreateuser(currentUser);
				func.setTenantId(IAuthConstants.DEFAULT_TENANT_ID);
			}
		}
		boolean rs = CapFunctionMgrUtil.addCapFunction(func);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("updateCapFunction")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCapFunction(@FormParam("keyData") String keyData) {
		CapFunction func = new CapFunction();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				func.setFuncId(jsObj.getString("funcId")); //$NON-NLS-1$
				func.setFuncName(jsObj.getString("funcName")); //$NON-NLS-1$
				func.setFuncType(jsObj.getString("funcType")); //$NON-NLS-1$
				func.setIsCheck(jsObj.getString("isCheck")); //$NON-NLS-1$
				func.setIsMenu(jsObj.getString("isMenu")); //$NON-NLS-1$
				func.setFuncAction(jsObj.getString("funcAction")); //$NON-NLS-1$
				func.setFuncDesc(jsObj.getString("funcDesc")); //$NON-NLS-1$
			}
		}
		boolean rs = CapFunctionMgrUtil.updateCapFunction(func);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

}
