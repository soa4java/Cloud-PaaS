/**
 * 
 */
package com.primeton.paas.console.platform.controller;

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

import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.console.platform.service.audit.UserAuditMgrUtil;
import com.primeton.paas.manage.api.factory.UserManagerFactory;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.User;

/**
 * 服务审批 （用户审批）
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
@Path("/audit")
public class UserAuditMgrResource {

	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/userlist")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response userList(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		User criteria = new User();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setUserId(jsObj.getString("userId"));
				criteria.setUserName(jsObj.getString("userName"));
				if (!"defaultValue".equals(jsObj.getString("status"))) {
					criteria.setStatus(jsObj.getInt("status"));
				}
				criteria.setPhone(jsObj.getString("phone"));
				criteria.setEmail(jsObj.getString("email"));
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		User[] users = UserAuditMgrUtil.getUsers(criteria, pageCond);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", users);
		result.put("total", pageCond.getCount());
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param keyData
	 * @return
	 */
	@Path("registerAppUser")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(@FormParam("keyData") String keyData) {
		User user = new User();
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

				String gender = jsObj.getString("gender"); //$NON-NLS-1$
				if ("1".equals(gender)) {
					gender = "m";
				} else if ("2".equals(gender)) {
					gender = "f";
				} else {
					gender = "n";
				}
				user.setGender(gender);
			}
		}
		boolean rs = UserAuditMgrUtil.register(user);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	@Path("checkUserId/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUserId(@PathParam("userId") String userId) {
		boolean ifexits = UserAuditMgrUtil.isExistUser(userId);// false 不存在 ，可用
																// true 存在，不可用
		// boolean ifexits = false;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", ifexits);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param phone
	 * @return
	 */
	@Path("checkUserPhone")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUserPhone(@FormParam("phone") String phone) {
		boolean ifexits = UserAuditMgrUtil.isExistPhone(phone);// false 不存在 ，可用
																// true 存在，不可用
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", ifexits);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param email
	 * @return
	 */
	@Path("checkUserEamil")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUserEmail(@FormParam("email") String email) {
		boolean ifexits = UserAuditMgrUtil.isExistEmail(email);// false 不存在 ，可用
																// true 存在，不可用
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", ifexits);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param ids
	 * @return
	 */
	@Path("deleteUsers/{ids}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUsers(@PathParam("ids") String ids) {
		String[] userIds = ids.split(",");
		boolean rs = UserAuditMgrUtil.removeUsers(userIds);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param ids
	 * @return
	 */
	@Path("agreeCapUsersReg/{ids}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response agreeCapUsersReg(@PathParam("ids") String ids) {
		String[] userIds = ids.split(",");

		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String handler = user.getUserId();

		boolean rs = UserAuditMgrUtil.agreeUsersReg(userIds, handler);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param ids
	 * @param notes
	 * @return
	 */
	@Path("rejectCapUsersReg")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response rejectCapUsersReg(@FormParam("ids") String ids,
			@FormParam("notes") String notes) {
		boolean rs = false;
		Map<String, Object> result = new HashMap<String, Object>();
		if (ids == null) {
			result.put("result", rs);
			ResponseBuilder builder = Response.ok(result);
			return builder.build();
		}
		String[] userIds = ids.split(",");

		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String handler = user.getUserId();

		rs = UserAuditMgrUtil.rejectUsersReg(userIds, notes, handler);
		result.put("result", rs);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param userId
	 * @param password
	 * @return
	 */
	@Path("resetPassword")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response resetPassword(@FormParam("userId") String userId,
			@FormParam("password") String password) {
		boolean rs = true;
		UserAuditMgrUtil.resetPasswd(userId, password);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param userId
	 * @param phone
	 * @return
	 */
	@Path("resetForgottenPwd")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response resetForgottenPassword(@FormParam("userId") String userId,
			@FormParam("phone") String phone) {
		String newPassword = UserManagerFactory.getManager()
				.resetForgottenPassword(userId, phone);
		boolean rs = newPassword != null;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

}
