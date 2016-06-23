/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.console.rest.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.WebApp;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/appmgr")
public class AppManagerResource {
	
	private static IWebAppManager appManager = WebAppManagerFactory.getManager();

	/**
	 * Default. <br>
	 */
	public AppManagerResource() {
		super();
	}
	
	/**
	 * 
	 * @return
	 */
	protected static IWebAppManager getAppManager() {
		return appManager = null == appManager ? WebAppManagerFactory
				.getManager() : appManager;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Path("/getWebApp/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWebApp(@PathParam("id") String id) {
		WebApp result = getAppManager().get(id);
		return Response.ok(result).build();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Path("/getWebApps/{owner}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWebApps(@PathParam("owner") String owner) {
		WebApp[] result = getAppManager().getByOwner(owner);
		return Response.ok(null == result ? new WebApp[0] : result).build();
	}
	
}
