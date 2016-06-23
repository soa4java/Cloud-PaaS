/**
 *
 */
package com.primeton.paas.console.app.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.manage.api.factory.UserManagerFactory;
import com.primeton.paas.manage.api.manager.IUserManager;
import com.primeton.paas.manage.api.model.User;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/myInfo")
public class MyInfoResource {

	private static IUserManager manager = UserManagerFactory.getManager();

	/**
	 * 
	 * @return
	 */
	@Path("details")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response details() {
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		User userObject = manager.get(user.getUserId());
		ResponseBuilder builder = Response.ok(userObject);
		return builder.build();
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	@Path("/update")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(User user) {
		boolean rtn = false;
		if (user != null) {
			User userObject = manager.get(user.getUserId());
			userObject.setUserName(user.getUserName());
			userObject.setPhone(user.getPhone());
			userObject.setEmail(user.getEmail());
			userObject.setGender(user.getGender());
			userObject.setTel(user.getTel());
			userObject.setAddress(user.getAddress());
			userObject.setNotes(user.getNotes());
			manager.update(userObject);
			rtn = true;
		}
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}

	/**
	 * 
	 * @param oldPwd
	 * @return
	 */
	@Path("validatePwd/{oldPwd}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response validatePwd(@PathParam("oldPwd") String oldPwd) { //$NON-NLS-1$
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		boolean rtn = manager.validatePasswd(user.getUserId(), oldPwd);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}

	/**
	 * 
	 * @param newPwd
	 * @return
	 */
	@Path("resetPwd/{newPwd}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response resetPwd(@PathParam("newPwd") String newPwd) { //$NON-NLS-1$
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		boolean rtn = false;
		if (newPwd != null) {
			manager.resetPasswd(user.getUserId(), newPwd);
			rtn = true;
		}
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}

}