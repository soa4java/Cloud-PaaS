/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.console.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * http://ip:port/${appName}/rest/srvmgr/${action}/${params}. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/srvmgr")
public class ServiceManagerResource {
	
	private static IServiceQuery serviceQuery = ServiceManagerFactory
			.getServiceQuery();
	
	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager();
	
	/**
	 * Default. <br>
	 */
	public ServiceManagerResource() {
		super();
	}

	/**
	 * 
	 * @return
	 */
	protected static IServiceQuery getServiceQuery() {
		return serviceQuery = null == serviceQuery ? ServiceManagerFactory
				.getServiceQuery() : serviceQuery;
	}
	
	/**
	 * 
	 * @return
	 */
	protected static IClusterManager getClusterManager() {
		return clusterManager = null == clusterManager ? ClusterManagerFactory
				.getManager() : clusterManager;
	}
	
	/**
	 * 
	 * @param id 集群标识
	 * @return
	 */
	@Path("/startCluster/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response startCluster(@PathParam("id") String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtil.isEmpty(id)) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", "Cluster id is empty."); //$NON-NLS-1$ //$NON-NLS-2$
			return Response.ok(result).build();
		}
		ICluster cluster = getClusterManager().get(id);
		if (null == cluster) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", StringUtil.format("Cluster {0} not found." //$NON-NLS-1$ //$NON-NLS-2$
					, new Object[] { id }));
			return Response.ok(result).build();
		}
		IClusterManager manager = ClusterManagerFactory.getManager(cluster.getType());
		manager = null == manager ? getClusterManager() : manager;
		try {
			manager.start(id);
		} catch (Throwable t) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", t.getMessage()); //$NON-NLS-1$
			return Response.ok(result).build();
		}
		result.put("success", Boolean.TRUE); //$NON-NLS-1$
		result.put("message", StringUtil.format("Start {0} cluster {1} success.",  //$NON-NLS-1$ //$NON-NLS-2$
				new Object[] { cluster.getType(), id }));
		return Response.ok(result).build();
	}
	
	/**
	 * 
	 * @param id 集群标识
	 * @return
	 */
	@Path("/stopCluster/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response stopCluster(@PathParam("id") String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtil.isEmpty(id)) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", "Cluster id is empty."); //$NON-NLS-1$ //$NON-NLS-2$
			return Response.ok(result).build();
		}
		ICluster cluster = getClusterManager().get(id);
		if (null == cluster) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", StringUtil.format("Cluster {0} not found." //$NON-NLS-1$ //$NON-NLS-2$
					, new Object[] { id }));
			return Response.ok(result).build();
		}
		IClusterManager manager = ClusterManagerFactory.getManager(cluster.getType());
		manager = null == manager ? getClusterManager() : manager;
		try {
			manager.stop(id);
		} catch (Throwable t) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", t.getMessage()); //$NON-NLS-1$
			return Response.ok(result).build();
		}
		result.put("success", Boolean.TRUE); //$NON-NLS-1$
		result.put("message", StringUtil.format("Shut down {0} cluster {1} success.",  //$NON-NLS-1$ //$NON-NLS-2$
				new Object[] { cluster.getType(), id }));
		return Response.ok(result).build();
	}
	
	/**
	 * 
	 * @param id 集群标识
	 * @return
	 */
	@Path("/restartCluster/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response restartCluster(@PathParam("id") String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtil.isEmpty(id)) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", "Cluster id is empty."); //$NON-NLS-1$ //$NON-NLS-2$
			return Response.ok(result).build();
		}
		ICluster cluster = getClusterManager().get(id);
		if (null == cluster) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", StringUtil.format("Cluster {0} not found." //$NON-NLS-1$ //$NON-NLS-2$
					, new Object[] { id }));
			return Response.ok(result).build();
		}
		IClusterManager manager = ClusterManagerFactory.getManager(cluster.getType());
		manager = null == manager ? getClusterManager() : manager;
		try {
			manager.restart(id);
		} catch (Throwable t) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", t.getMessage()); //$NON-NLS-1$
			return Response.ok(result).build();
		}
		result.put("success", Boolean.TRUE); //$NON-NLS-1$
		result.put("message", StringUtil.format("Restart {0} cluster {1} success.",  //$NON-NLS-1$ //$NON-NLS-2$
				new Object[] { cluster.getType(), id }));
		return Response.ok(result).build();
	}
	
	/**
	 * 
	 * @param id 服务标识
	 * @return
	 */
	@Path("/startService/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response startService(@PathParam("id") String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtil.isEmpty(id)) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", "Service id is empty."); //$NON-NLS-1$ //$NON-NLS-2$
			return Response.ok(result).build();
		}
		IService service = getServiceQuery().get(id);
		if (null == service) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", StringUtil.format("Service {0} not found." //$NON-NLS-1$ //$NON-NLS-2$
					, new Object[] { id }));
			return Response.ok(result).build();
		}
		IServiceManager manager = ServiceManagerFactory.getManager(service.getType());
		manager = null == manager ? ServiceManagerFactory.getManager() : manager;
		try {
			manager.start(id);
		} catch (Throwable t) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", t.getMessage()); //$NON-NLS-1$
			return Response.ok(result).build();
		}
		result.put("success", Boolean.TRUE); //$NON-NLS-1$
		result.put("message", StringUtil.format("Start {0} service {1} success.",  //$NON-NLS-1$ //$NON-NLS-2$
				new Object[] { service.getType(), id }));
		return Response.ok(result).build();
	}
	
	/**
	 * 
	 * @param id 服务标识
	 * @return
	 */
	@Path("/stopService/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response stopService(@PathParam("id") String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtil.isEmpty(id)) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", "Service id is empty."); //$NON-NLS-1$ //$NON-NLS-2$
			return Response.ok(result).build();
		}
		IService service = getServiceQuery().get(id);
		if (null == service) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", StringUtil.format("Service {0} not found." //$NON-NLS-1$ //$NON-NLS-2$
					, new Object[] { id }));
			return Response.ok(result).build();
		}
		IServiceManager manager = ServiceManagerFactory.getManager(service.getType());
		manager = null == manager ? ServiceManagerFactory.getManager() : manager;
		try {
			manager.stop(id);
		} catch (Throwable t) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", t.getMessage()); //$NON-NLS-1$
			return Response.ok(result).build();
		}
		result.put("success", Boolean.TRUE); //$NON-NLS-1$
		result.put("message", StringUtil.format("Stop {0} service {1} success.",  //$NON-NLS-1$ //$NON-NLS-2$
				new Object[] { service.getType(), id }));
		return Response.ok(result).build();
	}
	
	/**
	 * 
	 * @param id 服务标识
	 * @return
	 */
	@Path("/restartService/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response restartService(@PathParam("id") String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtil.isEmpty(id)) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", "Service id is empty."); //$NON-NLS-1$ //$NON-NLS-2$
			return Response.ok(result).build();
		}
		IService service = getServiceQuery().get(id);
		if (null == service) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", StringUtil.format("Service {0} not found." //$NON-NLS-1$ //$NON-NLS-2$
					, new Object[] { id }));
			return Response.ok(result).build();
		}
		IServiceManager manager = ServiceManagerFactory.getManager(service.getType());
		manager = null == manager ? ServiceManagerFactory.getManager() : manager;
		try {
			manager.restart(id);
		} catch (Throwable t) {
			result.put("success", Boolean.FALSE); //$NON-NLS-1$
			result.put("message", t.getMessage()); //$NON-NLS-1$
			return Response.ok(result).build();
		}
		result.put("success", Boolean.TRUE); //$NON-NLS-1$
		result.put("message", StringUtil.format("Restart {0} service {1} success.",  //$NON-NLS-1$ //$NON-NLS-2$
				new Object[] { service.getType(), id }));
		return Response.ok(result).build();
	}
	
	/**
	 * 
	 * @param id 集群标识
	 * @return
	 */
	@Path("/getCluster/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCluster(@PathParam("id") String id) {
		ICluster result = getClusterManager().get(id);
		return Response.ok(result).build();
	}
	
	/**
	 * 
	 * @param owner
	 * @return
	 */
	@Path("/getClusters/{owner}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getClusters(@PathParam("owner") String owner) {
		ICluster[] result = getClusterManager().getByOwner(owner, null);
		return Response.ok(null == result ? new ICluster[0] : result).build();
	}
	
	/**
	 * 
	 * @param id 服务标识
	 * @return
	 */
	@Path("/getService/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getService(@PathParam("id") String id) {
		IService result = getServiceQuery().get(id);
		return Response.ok(result).build();
	}
	
	/**
	 * 
	 * @param id 集群标识
	 * @return
	 */
	@Path("/getServices/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServices(@PathParam("id") String id) {
		IService[] result = getServiceQuery().getByCluster(id);
		return Response.ok(null == result ? new IService[0] : result).build();
	}
	
}
