/**
 *
 */
package com.primeton.paas.console.platform.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import net.sf.json.JSONObject;

import com.primeton.paas.console.platform.service.resource.StorageManagerUtil;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.spi.model.Storage;
import com.primeton.paas.manage.spi.model.WhiteList;

/**
 * 
 * @author liming(li-ming@primeton.com)
 *
 */
@Path("/storageMgr")
public class StorageMgrResource {
	
	@Path("/list")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response list(@FormParam("pageIndex") int pageIndex
							  ,@FormParam("pageSize") int pageSize
							  ,@FormParam("keyData") String keyData) {

		Storage sharedStorage = new Storage();
		if(null != keyData ){
			JSONObject  jsObj = JSONObject.fromObject(keyData) ;
			if(null != jsObj){
				sharedStorage.setName(jsObj.getString("name"));
				sharedStorage.setId(jsObj.getString("id"));
				sharedStorage.setPath(jsObj.getString("path"));
				if(null != jsObj.get("size") && !"".equals(jsObj.get("size")))
					sharedStorage.setSize(jsObj.getInt("size"));
			}
		}
		
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex*pageSize);
		pageCond.setLength(pageSize);
		
		Storage[] storages = StorageManagerUtil.queryStorages(sharedStorage, pageCond);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", storages);
		result.put("total", pageCond.getCount());
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	
	@Path("get/{id}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") String id) {
		Storage ss = StorageManagerUtil.queryStorage(id);
		ResponseBuilder builder = Response.ok(ss);
		return builder.build();
	}
	
	@Path("getWhiteLists/{id}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWhiteLists(@PathParam("id") String id) {
		List<WhiteList> wLists = StorageManagerUtil.queryStorage(id).getWhiteLists();
		ResponseBuilder builder = Response.ok(wLists);
		return builder.build();
	}
	
	
	@Path("release")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response release(String ids) {
		boolean rtn = StorageManagerUtil.releaseStorages(ids);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	
	@Path("remove")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove(String ids) {
		boolean rtn = StorageManagerUtil.removeStorages(ids);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	@Path("delMountIps/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response delMountIps(@PathParam("id") String id,String ips) {
		boolean rtn = StorageManagerUtil.delMountIps(id , ips.split(","));
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	@Path("addMountIp/{id}&{ip}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMountIp(@PathParam("id") String id, @PathParam("ip") String ip) {
		boolean rtn = StorageManagerUtil.addMountIp(id , ip);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	@Path("updateMountPoint/{id}&{ip}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMountPoint(@PathParam("id") String id, @PathParam("ip") String ip , String mountPoint) {
		boolean rtn = StorageManagerUtil.updateMountPoint(id , ip , mountPoint);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	@Path("/upgradeStorage/{id}&{size}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response upgradeStorage(@PathParam("id") String id, @PathParam("size") int size) {
		boolean rtn = StorageManagerUtil.upgradeStorage(id , size);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}	
	
}
