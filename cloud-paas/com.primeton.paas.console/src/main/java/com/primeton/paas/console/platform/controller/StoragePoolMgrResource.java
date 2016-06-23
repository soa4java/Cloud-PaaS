/**
 *
 */
package com.primeton.paas.console.platform.controller;

import java.util.ArrayList;
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

import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.console.platform.service.resource.StorageManagerUtil;
import com.primeton.paas.console.platform.service.resource.StoragePoolManagerUtil;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.spi.model.Storage;
import com.primeton.paas.manage.spi.model.StoragePoolConfig;

/**
 * 
 * @author liming(li-ming@primeton.com)
 *
 */
@Path("/storagePoolMgr")
public class StoragePoolMgrResource {
	
	@Path("/list")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response list(@FormParam("pageIndex") int pageIndex
							  ,@FormParam("pageSize") int pageSize
							  ,@FormParam("keyData") String keyData) {

		StoragePoolConfig cfg = new StoragePoolConfig();
		if(null != keyData ){
			JSONObject  jsObj = JSONObject.fromObject(keyData) ;
			if(null != jsObj){
				if(null != jsObj.getString("id") && !"".equals(jsObj.getString("id") ))
					cfg.setId(jsObj.getString("id"));
				if(null != jsObj.getString("size") && !"".equals(jsObj.getString("size") ))
					cfg.setStorageSize(jsObj.getInt("size"));
			}
		}
		
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex*pageSize);
		pageCond.setLength(pageSize);
		
		StoragePoolConfig[] cfgs = StoragePoolManagerUtil.queryStoragePools(cfg, pageCond);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", cfgs);
		result.put("total", pageCond.getCount());
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
		
		
	}
	
	
	@Path("listStorage/{size}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response listStorage(@PathParam("size") int size) {
		Storage[] dataList = StoragePoolManagerUtil.queryStoragesBySize(size, new PageCond());
		ResponseBuilder builder = Response.ok(dataList);
		return builder.build();
	}
	
	
	
	@Path("getAppStorageSizes")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAppStorageSizes() {
		String[] strInfo = SystemVariable.getAppStorageSizes();
		List<Map> dataList = new ArrayList<Map>();
		for(String st : strInfo){
			Map data = new HashMap();
			data.put("key", st);
			data.put("value", st);
			dataList.add(data);
		}
		ResponseBuilder builder = Response.ok(dataList);
		return builder.build();
	}
	
	@Path("getFilterAppStorageSizes")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFilterAppStorageSizes() {
		String[] strInfo = StoragePoolManagerUtil.getFilterAppStorageSizes();
		List<Map> dataList = new ArrayList<Map>();
		for(String st : strInfo){
			Map data = new HashMap();
			data.put("key", st);
			data.put("value", st);
			dataList.add(data);
		}
		ResponseBuilder builder = Response.ok(dataList);
		return builder.build();
	}
	
	
	@Path("/apply")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response apply(String ids) {
		boolean rtn = StorageManagerUtil.applyStorages(ids.split(","));
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	
	@Path("getFilterAppStorageSizesNum")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFilterAppStorageSizesNum() {
		int num = StoragePoolManagerUtil.getFilterAppStorageSizesNum();
		ResponseBuilder builder = Response.ok(num);
		return builder.build();
	}
	
	
	/**
	 * 获取常量｛minSize｝<br>
	 * 	
	 * @return
	 */
	@Path("getStoragePoolMinSize")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStoragePoolMinSize() {
		String[] strInfo = SystemVariable.getStoragePoolMinSize();
		List<Map> dataList = new ArrayList<Map>();
		for(String st : strInfo){
			Map data = new HashMap();
			data.put("key", st);
			data.put("value", st);
			dataList.add(data);
		}
		ResponseBuilder builder = Response.ok(dataList);
		return builder.build();
	}
	
	/**
	 * 获取常量｛maxSize｝<br>
	 * 
	 * @return
	 */
	@Path("getStoragePoolMaxSize")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStoragePoolMaxSize() {
		String[] strInfo = SystemVariable.getStoragePoolMaxSize();
		List<Map> dataList = new ArrayList<Map>();
		for(String st : strInfo){
			Map data = new HashMap();
			data.put("key", st);
			data.put("value", st);
			dataList.add(data);
		}
		ResponseBuilder builder = Response.ok(dataList);
		return builder.build();
	}
	
	/**
	 * 获取常量｛increaseSize｝<br>
	 * 
	 * @return
	 */
	@Path("getStoragePoolIncreaseSize")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStoragePoolIncreaseSize() {
		String[] strInfo = SystemVariable.getStoragePoolIncreaseSize();
		List<Map> dataList = new ArrayList<Map>();
		for(String st : strInfo){
			Map data = new HashMap();
			data.put("key", st);
			data.put("value", st);
			dataList.add(data);
		}
		ResponseBuilder builder = Response.ok(dataList);
		return builder.build();
	}
	
	/**
	 * 获取常量｛decreaseSize｝<br>
	 * 
	 * @return
	 */
	@Path("getStoragePoolDecreaseSize")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStoragePoolDecreaseSize() {
		String[] strInfo = SystemVariable.getStoragePoolDecreaseSize();
		List<Map> dataList = new ArrayList<Map>();
		for(String st : strInfo){
			Map data = new HashMap();
			data.put("key", st);
			data.put("value", st);
			dataList.add(data);
		}
		ResponseBuilder builder = Response.ok(dataList);
		return builder.build();
	}
	
	/**
	 * 获取常量｛timeInterval｝<br>
	 * 
	 * @return
	 */
	@Path("getStoragePoolTimeInterval")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStoragePoolTimeInterval() {
		String[] strInfo = SystemVariable.getStoragePoolTimeInterval();
		List<Map> dataList = new ArrayList<Map>();
		for(String st : strInfo){
			Map data = new HashMap();
			data.put("key", st);
			data.put("value", st);
			dataList.add(data);
		}
		ResponseBuilder builder = Response.ok(dataList);
		return builder.build();
	}	
	
	
	@Path("/addStoragePool")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response addStoragePool(StoragePoolConfig cfg) {
		boolean rtn = StoragePoolManagerUtil.addStoragePool(cfg);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	@Path("/updateStoragePool")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response updateStoragePool(StoragePoolConfig cfg) {
		boolean rtn = StoragePoolManagerUtil.updateStoragePool(cfg);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	@Path("/delStoragePools")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response delStoragePools(String storagePools) {
		boolean rtn = StoragePoolManagerUtil.removeStoragePools(storagePools.split(","));
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
}
