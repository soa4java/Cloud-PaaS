/**
 * 
 */
package com.primeton.paas.console.coframe.controller;

import java.util.HashMap;
import java.util.Map;

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

import com.primeton.paas.console.coframe.util.CapMenuMgrUtil;
import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.manage.api.impl.util.IAuthConstants;
import com.primeton.paas.manage.api.model.CapMenu;
import com.primeton.paas.manage.api.model.PageCond;

/**
 * 菜单管理
 * 
 * @author liuyi(liu-yi@primeton.com)
 * 
 */
@Path("/authmenu")
public class AuthMenuMgrResource {
	
	@Path("/getMenuTreeForDisplay")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMenuTreeForDisplayByUserId() {
		IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
		String currentUser = user.getUserId();
		CapMenu[] menus = CapMenuMgrUtil.getMenuByUserId(currentUser);
		if (menus.length <= 0) {
			menus = new CapMenu[0];
		}
		JSONArray menuJSON = JSONArray.fromObject(menus);
		ResponseBuilder builder = Response.ok(menuJSON);
		return builder.build();
	}

	@Path("/getMenuTreeForEdit")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMenuTreeForDisplay() {
		CapMenu[] menus = CapMenuMgrUtil.getAllMenu();
		JSONArray menuJSON = JSONArray.fromObject(menus);
		ResponseBuilder builder = Response.ok(menuJSON);
		return builder.build();
	}

	@Path("getFirstLevelMenu")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteFunctions(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize) {
		PageCond page = new PageCond();
		page.setBegin(pageIndex*pageSize);
		page.setLength(pageSize);
		CapMenu[] menus = CapMenuMgrUtil.getFirstLevelMenu(page);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", menus); //$NON-NLS-1$
		result.put("total", page.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("addFirstLevelCapMenu")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFirstLevelCapMenu(@FormParam("keyData") String keyData) {
		CapMenu menu = new CapMenu();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				menu.setMenuCode(jsObj.getString("menuCode")); //$NON-NLS-1$
				menu.setMenuName(jsObj.getString("menuName")); //$NON-NLS-1$
			}
		}
		boolean rs = CapMenuMgrUtil.addFirstLevelCapMenu(menu);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("addCapMenu")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCapMenu(@FormParam("keyData") String keyData) {
		CapMenu menu = new CapMenu();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				// jsObj.getString("")
				menu.setMenuSeq(jsObj.getString("parentMenuSeq")); //$NON-NLS-1$ // 还需增加本级菜单Id
				menu.setParentMenuId(jsObj.getString("parentMenuId")); //$NON-NLS-1$
				menu.setMenuCode(jsObj.getString("menuCode")); //$NON-NLS-1$
				menu.setMenuName(jsObj.getString("menuName")); //$NON-NLS-1$
				menu.setImagepath(jsObj.getString("imagepath")); //$NON-NLS-1$
				menu.setLinkAction(jsObj.getString("linkAction")); //$NON-NLS-1$
				menu.setLinkRes(jsObj.getString("linkRes")); //$NON-NLS-1$
				// menu.setLinkType(jsObj.getString("linkType"));//功能类型
				menu.setLinkType(IAuthConstants.FUNCTION_TO_RESOURCE_TYPE);// 资源类型，默认只有功能资源
			}
		}
		boolean rs = CapMenuMgrUtil.addCapMenu(menu);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("updateCapMenu")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCapMenu(@FormParam("keyData") String keyData) {
		CapMenu menu = new CapMenu();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				menu.setMenuId(jsObj.getString("menuId")); //$NON-NLS-1$
				menu.setMenuCode(jsObj.getString("menuCode")); //$NON-NLS-1$
				menu.setMenuName(jsObj.getString("menuName")); //$NON-NLS-1$
				menu.setImagepath(jsObj.getString("imagepath")); //$NON-NLS-1$
				menu.setLinkRes(jsObj.getString("linkRes")); //$NON-NLS-1$
				menu.setLinkAction(jsObj.getString("linkAction")); //$NON-NLS-1$
				// menu.setLinkType(jsObj.getString("linkType"));//功能类型
				menu.setLinkType(IAuthConstants.FUNCTION_TO_RESOURCE_TYPE);// 资源类型，默认只有功能资源
			}
		}
		boolean rs = CapMenuMgrUtil.updateCapMenu(menu);
		// boolean rs = false;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("deleteCapMenu/{menuId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCapMenu(@PathParam("menuId") String menuId) {
		boolean deleteOk = CapMenuMgrUtil.deleteCapMenu(menuId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", deleteOk); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

}
