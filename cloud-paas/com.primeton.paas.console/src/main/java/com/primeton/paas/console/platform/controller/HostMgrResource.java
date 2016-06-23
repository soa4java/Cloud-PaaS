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

import com.primeton.paas.console.platform.service.resource.HostManagerUtil;
import com.primeton.paas.console.platform.service.resource.StorageManagerUtil;
import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.spi.model.StorageVO;

/**
 * 
 * @author liming(li-ming@primeton.com)
 *
 */
@Path("/hostMgr")
public class HostMgrResource {
	
	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/list")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response list(@FormParam("pageIndex") int pageIndex
							  ,@FormParam("pageSize") int pageSize
							  ,@FormParam("keyData") String keyData) {

		String type = "";
		String ip = "";
		if(null != keyData ){
			JSONObject  jsObj = JSONObject.fromObject(keyData) ;
			if(null != jsObj){
				type = jsObj.getString("type");
				ip = jsObj.getString("ip");
			}
		}
		
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex*pageSize);
		pageCond.setLength(pageSize);
		
		Host[] hosts = HostManagerUtil.queryHosts(type, ip, pageCond);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", hosts);
		result.put("total", pageCond.getCount());
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
		
		
	}
	
	/**
	 * 
	 * @return
	 */
	@Path("getServiceTypes")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServiceTypes() {
		String[] serviceTypes = HostManagerUtil.getServiceTypes();
		List<Map> dataList = new ArrayList<Map>();
		for(String st : serviceTypes){
			Map data = new HashMap();
			data.put("key", st); //$NON-NLS-1$
			data.put("value", st); //$NON-NLS-1$
			dataList.add(data);
		}
		ResponseBuilder builder = Response.ok(dataList);
		return builder.build();
	}
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	@Path("getServiceTypes/{ip}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServiceTypes(@PathParam("ip") String ip) {
		String[] serviceTypes = HostManagerUtil.getTypesByIp(ip);
		List<Map> dataList = new ArrayList<Map>();
		for(String st : serviceTypes){
			Map data = new HashMap();
			data.put("key", st); //$NON-NLS-1$
			data.put("value", st); //$NON-NLS-1$
			dataList.add(data);
		}
		ResponseBuilder builder = Response.ok(dataList);
		return builder.build();
	}
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	@Path("getAdvanceServiceTypes/{ip}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAdvanceServiceTypes(@PathParam("ip") String ip) {
		String[] serviceTypes =  HostManagerUtil.getAdvanceTypesByIP(ip);
		List<Map> dataList = new ArrayList<Map>();
		for(String st : serviceTypes){
			Map data = new HashMap();
			data.put("key", st); //$NON-NLS-1$
			data.put("value", st); //$NON-NLS-1$
			dataList.add(data);
		}
		ResponseBuilder builder = Response.ok(dataList);
		return builder.build();
	}
	
	/**
	 * 
	 * @param hostPackageId
	 * @return
	 */
	@Path("getApplyIpByPid/{hostPackageId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApplyIpByPid(
			@PathParam("hostPackageId") String hostPackageId) {
		String rtn = null;
		if(hostPackageId != null ){
			rtn = HostManagerUtil.getApplyIpByPackageId(hostPackageId);
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param ip
	 * @param types
	 * @return
	 */
	@Path("applyHostService/{ip}&{types}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response applyHostService(@PathParam("ip") String ip,
			@PathParam("types") String types) {
		String rtn = null;
		if(ip != null  && types != null ){
			String[] typeList = types.split(","); //$NON-NLS-1$
			try {
				rtn = HostManagerUtil.applyHostService(ip, typeList);
			} catch (HostException e) {
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", rtn);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param ip
	 * @param types
	 * @return
	 */
	@Path("uninstallService/{ip}&{types}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallService(@PathParam("ip") String ip,
			@PathParam("types") String types) {
		String rtn = null;
		if(ip != null  && types != null ){
			String[] typeList = types.split(",");
			try {
				HostManagerUtil.uninstallService(ip, typeList);
			} catch (HostException e) {
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	@Path("getServices/{ip}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServices(@PathParam("ip") String ip) {
		IService[] services = HostManagerUtil.getServicesByIp(ip, null);
		ResponseBuilder builder = Response.ok(services);
		return builder.build();
	}
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	@Path("getStorages/{ip}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStorages(@PathParam("ip") String ip) {
		List<StorageVO> sList = StorageManagerUtil.getStoragesByIp(ip);
		ResponseBuilder builder = Response.ok(sList);
		return builder.build();
	}

	/**
	 * 
	 * @param ips
	 * @return
	 */
	@Path("/releaseHosts")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response releaseHosts(String ips) {
		boolean rtn = HostManagerUtil.releaseHosts(ips);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}	
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	@Path("getDisks/{ip}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDisks(@PathParam("ip") String ip) {
		List<Disk> diskList = HostManagerUtil.getDisksByIp(ip);
		ResponseBuilder builder = Response.ok(diskList);
		return builder.build();
	}
	
	/**
	 * 
	 * @param ip
	 * @param packageId
	 * @return
	 */
	@Path("/upgradeHost/{ip}&{packageId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response upgradeHost(@PathParam("ip") String ip,
			@PathParam("packageId") String packageId) {
		HostManagerUtil.upgradeHost(ip , packageId);
		boolean rtn = true;
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}	
	
}
