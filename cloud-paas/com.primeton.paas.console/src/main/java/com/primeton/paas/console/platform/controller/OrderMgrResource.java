/**
 *
 */
package com.primeton.paas.console.platform.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.console.platform.service.audit.OrderManagerUtil;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.PageCond;

/**
 * 
 * @author liming(li-ming@primeton.com)
 *
 */
@Path("/orderMgr")
public class OrderMgrResource {
	
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
		Order criteria = new Order();
		Date submitTimeBegin = null;
		Date submitTimeEnd = null;
		Date handleTimeBegin = null;
		Date handleTimeEnd = null;
		
		if(null != keyData ){
			JSONObject  jsObj = JSONObject.fromObject(keyData) ;
			if(null != jsObj){
				
				criteria.setOrderId(jsObj.getString("orderId"));
				criteria.setOwner(jsObj.getString("owner"));
				if(!"defaultValue".equals(jsObj.getString("orderType"))){
					criteria.setOrderType(jsObj.getString("orderType"));
				}
				if(!"defaultValue".equals(jsObj.getString("orderStatus"))){
					criteria.setOrderStatus(jsObj.getInt("orderStatus"));
				}

				SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String submitTimeBeginStr = (String)jsObj.get("submitTimeBegin");
				String submitTimeEndStr = (String)jsObj.get("submitTimeEnd");
				if(submitTimeBeginStr != null && !"".equals(submitTimeBeginStr)){
					try {
						submitTimeBegin = sim.parse(submitTimeBeginStr);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if(submitTimeEndStr != null && !"".equals(submitTimeEndStr)){
					try {
						submitTimeEnd = sim.parse(submitTimeEndStr);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				
				
				String handleTimeBeginStr = (String)jsObj.get("handleTimeBegin");
				String handleTimeEndStr = (String)jsObj.get("handleTimeEnd");
				if(handleTimeBeginStr != null && !"".equals(handleTimeBeginStr)){
					try {
						handleTimeBegin = sim.parse(handleTimeBeginStr);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if(handleTimeEndStr != null && !"".equals(handleTimeEndStr)){
					try {
						handleTimeEnd = sim.parse(handleTimeEndStr);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex*pageSize);
		pageCond.setLength(pageSize);
		
		Order[] orders = OrderManagerUtil.getOrders(criteria, submitTimeBegin, submitTimeEnd, handleTimeBegin, handleTimeEnd, pageCond);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", orders);
		result.put("total", pageCond.getCount());
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
		
		
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Path("details/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response details(@PathParam("id") String id) {
		Order order = OrderManagerUtil.getOrderWithItems(id);
		ResponseBuilder builder = Response.ok(order);
		return builder.build();
	}
	
	/**
	 * 
	 * @param orderId
	 * @return
	 */
	@Path("approveApply/{orderId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response approveApply(@PathParam("orderId") String  orderId) {
        boolean rtn = false;
        if(orderId != null ){
    		IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
    		String currentUser = user.getUserId();
        	rtn = OrderManagerUtil.approveOrder(orderId, currentUser);
        }
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", rtn);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param orderId
	 * @param itemId
	 * @return
	 */
	@Path("redoItemApply/{orderId}&{itemId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response redoItemApply(@PathParam("orderId") String  orderId , @PathParam("itemId") String  itemId) {
		boolean rtn = false;
		if(orderId != null ){
			rtn = OrderManagerUtil.redoItem(orderId, itemId);
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", rtn);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param orderId
	 * @param notes
	 * @return
	 */
	@Path("rejectApply/{orderId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response rejectApply(@PathParam("orderId") String  orderId , String  notes) {
		boolean rtn = false;
		if(orderId != null ){
    		IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
    		String currentUser = user.getUserId();
			rtn = OrderManagerUtil.rejectOrder(orderId, notes, currentUser);
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", rtn);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param orderId
	 * @return
	 */
	@Path("delete/{orderId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("orderId") String orderId) {
		boolean rtn = false;
		if(orderId != null ){
			rtn = OrderManagerUtil.removeOrders(orderId.split(","));
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", rtn);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

}
