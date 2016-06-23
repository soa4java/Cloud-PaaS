/**
 *
 * @author liming(li-ming@primeton.com)
 */
package com.primeton.paas.console.platform.controller;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.primeton.paas.console.coframe.util.CapUserMgrUtil;
import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.manage.api.factory.AuthBaseManagerFactory;
import com.primeton.paas.manage.api.impl.util.MD5Util;
import com.primeton.paas.manage.api.manager.IAuthManager;
import com.primeton.paas.manage.api.model.CapUser;

@Path("/myAccount")
public class MyAccountResource {
	
	private static IAuthManager authMgr = AuthBaseManagerFactory.getManager();


	@Path("details")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response details() {
		IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
		CapUser userObject = authMgr.getUserByUserId(user.getUserId());
		ResponseBuilder builder = Response.ok(userObject);
		return builder.build();
	}
	
	
	@Path("/update")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response update(CapUser user) {
		boolean rtn = false;
		if(user != null){
			CapUser userObject = authMgr.getUserByUserId(user.getUserId());
			userObject.setUserName(user.getUserName());
			userObject.setPhone(user.getPhone());
			userObject.setEmail(user.getEmail());
			userObject.setGender(user.getGender());
			userObject.setTel(user.getTel());
			userObject.setAddress(user.getAddress());
			userObject.setNotes(user.getNotes());
			CapUserMgrUtil.updateCapUser(userObject);
			rtn = true;
		}
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	
	@Path("validatePwd/{oldPwd}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response validatePwd(@PathParam("oldPwd") String oldPwd) {
		IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
		boolean rtn = authMgr.validatePasswd(user.getUserId(), oldPwd);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	
	@Path("resetPwd/{newPwd}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response resetPwd(@PathParam("newPwd") String newPwd) {
		IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
		boolean rtn = false;
		if(newPwd != null){
			CapUser userObject = authMgr.getUserByUserId(user.getUserId());
			userObject.setPassword(MD5Util.md5(newPwd));
			CapUserMgrUtil.updateCapUser(userObject);
			rtn = true;
		}
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	
}