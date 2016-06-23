package com.primeton.paas.console.platform.controller;

import java.util.ArrayList;
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

import net.sf.json.JSONObject;

import com.primeton.paas.console.common.Entry;
import com.primeton.paas.console.platform.service.resource.VIPManagerUtil;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.spi.model.VIPSegment;

/**
 * 资源管理
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
@Path("/vipMgr")
public class VIPMgrResource {
	
	@Path("/vipSegmentsList")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response vipSegmentsList(@FormParam("pageIndex") int pageIndex ,@FormParam("pageSize") int pageSize) {
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex*pageSize);
		pageCond.setLength(pageSize);
		
		List<VIPSegment> ipSegments = VIPManagerUtil.getAllVIPSegment(pageCond);
		
		if(ipSegments==null||ipSegments.size()==0){
			ipSegments = new ArrayList<VIPSegment>();
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", ipSegments.toArray(new VIPSegment[ipSegments.size()]));
		result.put("total", pageCond.getCount());
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	
	@Path("/getUsedVIPsBySegmentId")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response getUsedVIPsBySegmentId(@FormParam("pageIndex") int pageIndex ,@FormParam("pageSize") int pageSize,@FormParam("segmentId") String segmentId) {
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex*pageSize);
		pageCond.setLength(pageSize);
		
		List<String> usedIPs = VIPManagerUtil.getUsedVIPsBySegmentId(segmentId,pageCond);
		if(usedIPs==null||usedIPs.size()==0){
			usedIPs = new ArrayList<String>();
		}
		List<Entry> returnList = new ArrayList<Entry>();
		for(String ip:usedIPs){
			Entry cm = new Entry();
			cm.setId(ip);
			cm.setText(ip);
			returnList.add(cm);
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", returnList.toArray(new Entry[returnList.size()]));
		result.put("total", pageCond.getCount());
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	@Path("/addVIPSegment")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response addVIPSegment(@FormParam("keyData") String keyData) {
		boolean ifSuccess = false;
		
		if(null != keyData ){
			VIPSegment segment = new VIPSegment();
			JSONObject  jsObj = JSONObject.fromObject(keyData) ;
			if(null != jsObj){
				String begin = jsObj.getString("begin");
				String end = jsObj.getString("end");
				String netmask = jsObj.getString("netmask");
				segment = VIPManagerUtil.makeVIPSegment(begin, end, netmask);
			}
			ifSuccess = VIPManagerUtil.addSegment(segment);
		}
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", ifSuccess);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	@Path("/updateVIPSegment")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response updateVIPSegment(@FormParam("keyData") String keyData) {
		boolean ifSuccess = false;
		
		if(null != keyData ){
			VIPSegment segment = new VIPSegment();
			JSONObject  jsObj = JSONObject.fromObject(keyData) ;
			if(null != jsObj){
				String segmentId = jsObj.getString("id");
				String begin = jsObj.getString("begin");
				String end = jsObj.getString("end");
				String netmask = jsObj.getString("netmask");
				segment = VIPManagerUtil.makeVIPSegment(segmentId,begin, end, netmask);
			}
			ifSuccess = VIPManagerUtil.updateSegment(segment);
		}
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", ifSuccess);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	@Path("delVIPSegment/{segmentId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response delVIPSegment(@PathParam("segmentId") String segmentId) {
		boolean ifSuccess = false;
		if(segmentId!=null){
			ifSuccess = VIPManagerUtil.delSegment(segmentId);
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", ifSuccess);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	@Path("deleteVIP/{ips}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response deleteVIP(@PathParam("ips") String ips) {
		System.out.println(ips);
		boolean ifSuccess = false;
		if(ips!=null){
			ifSuccess = VIPManagerUtil.deleteVIP(ips);
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", ifSuccess);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	
}
