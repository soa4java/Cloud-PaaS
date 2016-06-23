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

import com.primeton.paas.console.coframe.util.CapUserMgrUtil;
import com.primeton.paas.manage.api.model.CapRole;
import com.primeton.paas.manage.api.model.CapUser;
import com.primeton.paas.manage.api.model.PageCond;

/**
 * 用户管理
 * 
 * @author liuyi(liu-yi@primeton.com)
 * 
 */
@Path("/authuser")
public class AuthUserMgrResource {
	
	@Path("/userlist")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response userList(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		CapUser criteria = new CapUser();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setUserId(jsObj.getString("userId")); //$NON-NLS-1$
				criteria.setUserName(jsObj.getString("userName")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.setStatus(jsObj.getInt("status")); //$NON-NLS-1$
				}
				criteria.setPhone(jsObj.getString("phone")); //$NON-NLS-1$
				criteria.setEmail(jsObj.getString("email")); //$NON-NLS-1$
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		CapUser[] users = CapUserMgrUtil.getUsers(criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", users); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("getUserByUserId/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByUserId(@PathParam("userId") String userId) {
		CapUser user = CapUserMgrUtil.getUserByUserId(userId);
		ResponseBuilder builder = Response.ok(user);
		return builder.build();
	}

	@Path("checkUserId/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUserId(@PathParam("userId") String userId) { //$NON-NLS-1$
		boolean ifexits = CapUserMgrUtil.checkUserIdIsExist(userId); 
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", ifexits); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("checkUserPhone")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response checkUserPhone(@FormParam("userId") String userId,
			@FormParam("phone") String phone) {
		boolean ifexits = CapUserMgrUtil.checkUserPhoneIsExist(userId, phone);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", ifexits); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("checkUserEamil")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response checkUserEamil(@FormParam("userId") String userId,
			@FormParam("email") String email) {
		boolean ifexits = CapUserMgrUtil.checkUserEamilIsExist(userId, email);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", ifexits); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("addUser")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(@FormParam("keyData") String keyData) {
		CapUser user = new CapUser();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				user.setUserId(jsObj.getString("userId")); //$NON-NLS-1$
				user.setUserName(jsObj.getString("userName")); //$NON-NLS-1$
				user.setPassword(jsObj.getString("password")); //$NON-NLS-1$
				user.setAddress(jsObj.getString("address")); //$NON-NLS-1$
				user.setNotes(jsObj.getString("notes")); //$NON-NLS-1$
				user.setEmail(jsObj.getString("email")); //$NON-NLS-1$
				user.setPhone(jsObj.getString("phone")); //$NON-NLS-1$
				user.setTel(jsObj.getString("tel")); //$NON-NLS-1$
				user.setStatus(4);

				String gender = jsObj.getString("gender"); //$NON-NLS-1$
				if ("1".equals(gender)) { //$NON-NLS-1$
					gender = "m"; //$NON-NLS-1$
				} else if ("2".equals(gender)) { //$NON-NLS-1$
					gender = "f"; //$NON-NLS-1$
				} else { //$NON-NLS-1$
					gender = "n"; //$NON-NLS-1$
				}
				user.setGender(gender);
			}
		}
		boolean rs = CapUserMgrUtil.addUser(user);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("addAppUser")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAppUser(@FormParam("keyData") String keyData) {
		CapUser user = new CapUser();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				user.setUserId(jsObj.getString("userId")); //$NON-NLS-1$
				user.setUserName(jsObj.getString("userName")); //$NON-NLS-1$
				user.setPassword(jsObj.getString("password")); //$NON-NLS-1$
				user.setAddress(jsObj.getString("address")); //$NON-NLS-1$
				user.setNotes(jsObj.getString("notes")); //$NON-NLS-1$
				user.setEmail(jsObj.getString("email")); //$NON-NLS-1$
				user.setPhone(jsObj.getString("phone")); //$NON-NLS-1$
				user.setTel(jsObj.getString("tel")); //$NON-NLS-1$
				user.setStatus(4);

				String gender = jsObj.getString("gender"); //$NON-NLS-1$
				if ("1".equals(gender)) { //$NON-NLS-1$
					gender = "f"; //$NON-NLS-1$
				} else if ("2".equals(gender)) {
					gender = "m"; //$NON-NLS-1$
				} else {
					gender = "n"; //$NON-NLS-1$
				}
				user.setGender(gender);
			}
		}
		boolean rs = CapUserMgrUtil.addAppUser(user);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("updateUser")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@FormParam("keyData") String keyData) {
		CapUser user = new CapUser();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				user.setUserId(jsObj.getString("userId")); //$NON-NLS-1$
				user.setUserName(jsObj.getString("userName")); //$NON-NLS-1$
				// user.setPassword(jsObj.getString("password")); //$NON-NLS-1$
				user.setAddress(jsObj.getString("address")); //$NON-NLS-1$
				user.setNotes(jsObj.getString("notes")); //$NON-NLS-1$
				user.setEmail(jsObj.getString("email")); //$NON-NLS-1$
				user.setPhone(jsObj.getString("phone")); //$NON-NLS-1$
				user.setTel(jsObj.getString("tel")); //$NON-NLS-1$
				user.setStatus(4);

				String gender = jsObj.getString("gender"); //$NON-NLS-1$
				if ("1".equals(gender)) { //$NON-NLS-1$
					gender = "m"; //$NON-NLS-1$
				} else if ("2".equals(gender)) { //$NON-NLS-1$
					gender = "f"; //$NON-NLS-1$
				} else {
					gender = "n"; //$NON-NLS-1$
				}
				user.setGender(gender);
			}
		}
		boolean rs = CapUserMgrUtil.updateCapUser(user);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("deleteUsers/{ids}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUsers(@PathParam("ids") String ids) {
		String[] userIds = ids.split(",");
		boolean rs = CapUserMgrUtil.deleteUsers(userIds);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	@Path("saveUserAndRoleAuth/{userId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response saveUserAndRoleAuth(@FormParam("keyData") String keyData,
			@PathParam("userId") String userId) { //$NON-NLS-1$
		List<CapRole> list = new ArrayList<CapRole>();
		if (null != keyData) {
			JSONArray roleJSON = JSONArray.fromObject(keyData);
			Object[] objArray = roleJSON.toArray();
			for (Object o : objArray) {
				JSONObject obj = JSONObject.fromObject(o);
				String strDate=String.valueOf(obj.get("createtime"));   //$NON-NLS-1$
		        Date newDate= new Date(Long.parseLong(strDate));
		        //移除原有的createtime属性  
		        obj.remove("createtime");   //$NON-NLS-1$
		        //将日期类型的createtime放到obj中  
		        obj.put("createtime", newDate); //$NON-NLS-1$
				CapRole role = (CapRole) (JSONObject.toBean(obj, CapRole.class));
				list.add(role);
			}
		}
		// 保存授权信息
		boolean ifSuccess = CapUserMgrUtil.saveUserAndRoleAuth(list, userId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", ifSuccess); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
}
