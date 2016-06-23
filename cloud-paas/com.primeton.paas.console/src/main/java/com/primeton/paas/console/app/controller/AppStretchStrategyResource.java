/**
 *
 */
package com.primeton.paas.console.app.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.app.service.util.AppUtil;
import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.common.stretch.StrategyBasicConfigUtil;
import com.primeton.paas.console.common.stretch.StrategyConfigInfo;
import com.primeton.paas.console.common.stretch.StrategyConfigInfo.StretchItem;
import com.primeton.paas.console.common.stretch.StrategyConfigUtil;
import com.primeton.paas.console.common.stretch.StrategyItemInfo;
import com.primeton.paas.console.platform.service.monitor.TelescopicStrategyUtil;
import com.primeton.paas.manage.api.app.StretchStrategy;
import com.primeton.paas.manage.api.factory.OrderManagerFactory;
import com.primeton.paas.manage.api.manager.IOrderManager;
import com.primeton.paas.manage.api.model.Order;
import com.primeton.paas.manage.api.model.OrderItem;
import com.primeton.paas.manage.api.model.OrderItemAttr;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 应用伸缩策略. <br>
 * 
 * @author YanPing.Li (mailto:liyp@primeton.com)
 *
 */
@Path("/appSS")
public class AppStretchStrategyResource {

	private static ILogger logger = LoggerFactory.getLogger(AppStretchStrategyResource.class);
	
	private static IOrderManager orderManager = OrderManagerFactory.getManager(); 
	
	/**
	 * 获取配置项下拉框 可选项列表 --array 形式 <br>
	 * 
	 * @return  
	 */
	public static StrategyItemInfo getItemInfo() {
		return StrategyBasicConfigUtil.getItemOpts();
	}
	
	/**
	 * 
	 * @param owner
	 * @return
	 */
	public static List<WebApp> getApps(String owner) {
		List<WebApp> appList = new ArrayList<WebApp>();
		WebApp[] apps = AppUtil.getWebapps(owner);
		if (apps != null && apps.length >0) {
			for (WebApp app :apps) {
				int state = app.getState();
				if (state == WebApp.STATE_OPEND ) {
					appList.add(app);
				}
			}
		}
		return appList;
	}
	
	/**
	 * 获取指定应用当前的伸缩策略详情 <br>
	 * 
	 * @param appName
	 * @return
	 */
	public static StrategyConfigInfo getAppStretchStrategy(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		return StrategyConfigUtil.getAppStretchStrategy(appName);
	}
	
	/**
	 * 获取当前全局伸缩策略详情 <br>
	 * 
	 * @return
	 */
	public static StrategyConfigInfo getGlobalStretchStrategy() {
		return StrategyConfigUtil.getGlobalStretchStrategy();
	}
	
	/**
	 * 为应用设置个性化伸缩策略配置 <br>
	 * 
	 * @param stretchConf
	 * @return
	 */
	public static Order setAppStretchStrategy(StrategyConfigInfo stretchConf) {
		if (stretchConf == null) {
			return null;
		}
		String appName = stretchConf.getAppName();
		String strategyName = appName ; //个性化配置:应用名称作为 伸缩策略名
		if(StringUtil.isEmpty(appName)){
			return null;
		}
		Order order = new Order();
		IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
		//submitTime & beginTime & endTime
		order.setSubmitTime(new Date());
		order.setBeginTime(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR,3);
		order.setEndTime(cal.getTime());
		
		order.setOwner(user.getUserId());
		order.setOrderStatus(Order.ORDER_STATUS_SUBMITED);
		order.setOrderType(Order.ORDER_TYPE_STRETCH_STRATEGY);
		
		List<OrderItem> itemList = new ArrayList<OrderItem>();
		
		/** 订单项：伸缩配置类型（全局伸缩配置 | 个性化伸缩配置） */
		OrderItem typeItem = new OrderItem();
		typeItem.setItemStatus(OrderItem.ITEM_STATUS_INIT);
		typeItem.setItemType(OrderItem.ITEM_TYPE_STRETCH_STRATEGY_NAME);
		
		List<OrderItemAttr> typeAttrs = new ArrayList<OrderItemAttr>();
		OrderItemAttr typeAttr = new OrderItemAttr();
		
		// appName name
		typeAttr.setAttrName(OrderItemAttr.ATTR_APP_NAME);
		typeAttr.setAttrValue(appName);
		typeAttrs.add(typeAttr);
		
		// strategy name
		typeAttr = new OrderItemAttr();
		typeAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_STRATEGY_NAME);
		typeAttr.setAttrValue(strategyName);
		typeAttr.setDescription("");
		typeAttrs.add(typeAttr);
		
		typeItem.setAttrList(typeAttrs);
		itemList.add(typeItem);
		
		/** 订单项：“伸” 策略*/
		StrategyConfigInfo.StretchItem incConf = stretchConf.getIncStrategy();
		if (incConf == null) {
			//
		} else {
			OrderItem incItem = new OrderItem();
			incItem.setItemStatus(OrderItem.ITEM_STATUS_INIT);
			incItem.setItemType(OrderItem.ITEM_TYPE_STRETCH_INC_STRATEGY);
			
			List<OrderItemAttr> incAttrs = new ArrayList<OrderItemAttr>();
			OrderItemAttr incAttr = null;
			
			//strategy type : increase 
			incAttr = new OrderItemAttr();
			incAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_STRATEGY_TYPE);
			incAttr.setAttrValue(StretchStrategy.STRATEGY_INCREASE); 
			incAttr.setDescription("伸");
			incAttrs.add(incAttr);
			
			//isEnable
			incAttr = new OrderItemAttr();
			incAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_ISENABLE);
			incAttr.setAttrValue("Y".equals(incConf.getEnableFlag())? "true":"false" ); 
			incAttrs.add(incAttr);
			
			//strategy name : appName
			incAttr = new OrderItemAttr();
			incAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_STRATEGY_NAME);
			incAttr.setAttrValue(strategyName); 
			incAttrs.add(incAttr);
			
			//stretch size  
			incAttr = new OrderItemAttr();
			incAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_SIZE);
			incAttr.setAttrValue(incConf.getStretchSize()+""); 
			incAttr.setDescription("台");
			incAttrs.add(incAttr);
			
			//continued time 
			incAttr = new OrderItemAttr();
			incAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_STRATEGY_CONTINUED_TIME);
			incAttr.setAttrValue(incConf.getContinuedTime()+""); 
			incAttr.setDescription("分钟");
			incAttrs.add(incAttr);
			
			//ignore time 
			incAttr = new OrderItemAttr();
			incAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_IGNORE_TIME);
			incAttr.setAttrValue(incConf.getIgnoreTime()+""); 
			incAttr.setDescription("分钟");
			incAttrs.add(incAttr);
			
			//cpu threshold
			incAttr = new OrderItemAttr();
			incAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_CPU_THRESHOLD);
			incAttr.setAttrValue(incConf.getCpuThreshold() +""); 
			incAttr.setDescription("%");
			incAttrs.add(incAttr);
			
			//memory threshold
			incAttr = new OrderItemAttr();
			incAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_MEMORY_THRESHOLD);
			incAttr.setAttrValue(incConf.getMemThreshold() +""); 
			incAttr.setDescription("%");
			incAttrs.add(incAttr);
			
			//lb threshold
			incAttr = new OrderItemAttr();
			incAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_LB_THRESHOLD);
			incAttr.setAttrValue(incConf.getLbThreshold()+""); 
			incAttr.setDescription("");
			incAttrs.add(incAttr);
			
			incItem.setAttrList(incAttrs);
			itemList.add(incItem);
		}
		
		
		/** 订单项：“缩” 策略*/
		StrategyConfigInfo.StretchItem decConf = stretchConf.getDecStrategy();
		if (decConf == null) {
			//
		} else {
			OrderItem decItem = new OrderItem();
			decItem.setItemStatus(OrderItem.ITEM_STATUS_INIT);
			decItem.setItemType(OrderItem.ITEM_TYPE_STRETCH_DEC_STRATEGY);
			
			List<OrderItemAttr> decAttrs = new ArrayList<OrderItemAttr>();
			OrderItemAttr decAttr = null;
			
			//strategy type : decrease 
			decAttr = new OrderItemAttr();
			decAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_STRATEGY_TYPE);
			decAttr.setAttrValue(StretchStrategy.STRATEGY_DECREASE); 
			typeAttr.setDescription("缩");
			decAttrs.add(decAttr);
			
			//isEnable
			decAttr = new OrderItemAttr();
			decAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_ISENABLE);
			decAttr.setAttrValue("Y".equals(decConf.getEnableFlag())? "true":"false" ); 
			decAttrs.add(decAttr);
			
			//strategy name : appName
			decAttr = new OrderItemAttr();
			decAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_STRATEGY_NAME);
			decAttr.setAttrValue(strategyName); 
			decAttrs.add(decAttr);
			
			//stretch size  
			decAttr = new OrderItemAttr();
			decAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_SIZE);
			decAttr.setAttrValue(decConf.getStretchSize()+""); 
			decAttr.setDescription("台");
			decAttrs.add(decAttr);
			
			//continued time 
			decAttr = new OrderItemAttr();
			decAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_STRATEGY_CONTINUED_TIME);
			decAttr.setAttrValue(decConf.getContinuedTime()+""); 
			decAttr.setDescription("分钟");
			decAttrs.add(decAttr);
			
			//ignore time 
			decAttr = new OrderItemAttr();
			decAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_IGNORE_TIME);
			decAttr.setAttrValue(decConf.getIgnoreTime()+"");
			decAttr.setDescription("分钟");
			decAttrs.add(decAttr);
			
			//cpu threshold
			decAttr = new OrderItemAttr();
			decAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_CPU_THRESHOLD);
			decAttr.setAttrValue(decConf.getCpuThreshold() +""); 
			decAttr.setDescription("%");
			decAttrs.add(decAttr);
			
			//memory threshold
			decAttr = new OrderItemAttr();
			decAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_MEMORY_THRESHOLD);
			decAttr.setAttrValue(decConf.getMemThreshold() +""); 
			decAttr.setDescription("%");
			decAttrs.add(decAttr);
			
			//lb threshold
			decAttr = new OrderItemAttr();
			decAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_LB_THRESHOLD);
			decAttr.setAttrValue(decConf.getLbThreshold()+""); 
			decAttr.setDescription("");
			decAttrs.add(decAttr);
			
			decItem.setAttrList(decAttrs);
			itemList.add(decItem);
		}
		//全局伸缩策略订单
		order.setItemList(itemList);
		try {
			order = orderManager.add(order);
		} catch (Exception e) {
			logger.error(e);
		}
		return order;
	}
	
	/**
	 * 为应用设置 全局伸缩策略 <br>
	 * 
	 * @param appName  应用名称
	 * @param isIncEnable  是否启用全局 伸
	 * @param isDecEnable  是否启用全局 缩
	 * @return
	 */
	public static Order doSetGlobalStretchStrategy(String appName/*,boolean isIncEnable, boolean isDecEnable*/) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		Order order = new Order();
		
		IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
		String currentUser = user.getUserId();

		
		//submitTime & beginTime & endTime
		order.setSubmitTime(new Date());
		order.setBeginTime(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR,3);
		order.setEndTime(cal.getTime());
		
		order.setOwner(currentUser);
		order.setOrderStatus(Order.ORDER_STATUS_SUBMITED);
		order.setOrderType(Order.ORDER_TYPE_STRETCH_STRATEGY);
		

		List<OrderItem> itemList = new ArrayList<OrderItem>();
		
		/** 伸缩配置类型： 全局伸缩配置 */
		OrderItem typeItem = new OrderItem();
		typeItem.setItemStatus(OrderItem.ITEM_STATUS_INIT);
		typeItem.setItemType(OrderItem.ITEM_TYPE_STRETCH_STRATEGY_NAME);
		
		//appName name 
		List<OrderItemAttr> typeAttrs = new ArrayList<OrderItemAttr>();
		OrderItemAttr typeAttr = new OrderItemAttr();
		typeAttr.setAttrName(OrderItemAttr.ATTR_APP_NAME);
		typeAttr.setAttrValue(appName);
		typeAttrs.add(typeAttr);
		
		//strategy name 
		typeAttr = new OrderItemAttr();
		typeAttr.setAttrName(OrderItemAttr.ATTR_STRETCH_STRATEGY_NAME);
		typeAttr.setAttrValue(StretchStrategy.GLOBAL_STRATEGY);
		typeAttrs.add(typeAttr);
		
		typeItem.setAttrList(typeAttrs);
		itemList.add(typeItem);
		
		//全局伸缩策略订单
		order.setItemList(itemList);
		
		try {
			order = orderManager.add(order);
		} catch (Exception e) {
			logger.error(e);
		}
		return order;
	}

	/**
	 * 
	 * @return
	 */
	@Path("/listApp")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listApp() {
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String currentUser = user.getUserId();
		List<WebApp> appList = getApps(currentUser);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", appList); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("/get/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("appName") String appName) {
		StrategyConfigInfo sCfg = getAppStretchStrategy(appName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", sCfg); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 获取所有平台应用名
	 * 
	 * @return
	 */
	@Path("/getApps")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApps() {
		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String currentUser = user.getUserId();

		List<WebApp> appList = getApps(currentUser);
		// 为显示需要
		for (WebApp app : appList) {
			app.setDisplayName(app.getDisplayName() + "(" + app.getName() + ")");
		}
		WebApp[] rs = appList.toArray(new WebApp[appList.size()]);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @return
	 */
	@Path("/getGlobalStrategyConfigInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getGlobalStrategyConfigInfo() {
		StrategyConfigInfo sii = TelescopicStrategyUtil
				.getGlobalStretchStrategy();
		StrategyConfigInfo.StretchItem itemInc = new StrategyConfigInfo.StretchItem();
		StrategyConfigInfo.StretchItem itemDec = new StrategyConfigInfo.StretchItem();
		itemInc = sii.getIncStrategy();
		itemDec = sii.getDecStrategy();
		List<StretchItem> items = new ArrayList<StretchItem>();
		items.add(itemInc);
		items.add(itemDec);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", items.toArray(new StretchItem[items.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("/getAppStrategyConfigInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getAppStrategyConfigInfo(
			@FormParam("appName") String appName) {
		StrategyConfigInfo appConfigInfo = TelescopicStrategyUtil
				.getAppStretchStrategy(appName);
		if (appName == null || appConfigInfo == null) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("data", new StretchItem[0]);
			ResponseBuilder builder = Response.ok(result);
			return builder.build();
		}
		StrategyConfigInfo.StretchItem itemInc = new StrategyConfigInfo.StretchItem();
		StrategyConfigInfo.StretchItem itemDec = new StrategyConfigInfo.StretchItem();
		itemInc = appConfigInfo.getIncStrategy();
		itemDec = appConfigInfo.getDecStrategy();
		List<StretchItem> items = new ArrayList<StretchItem>();
		items.add(itemInc);
		items.add(itemDec);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", items.toArray(new StretchItem[items.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param keyData
	 * @param appName
	 * @return
	 */
	@Path("/updateAppStretchStrategy/{appName}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateAppStretchStrategy(
			@FormParam("keyData") String keyData,
			@PathParam("appName") String appName) {
		StretchItem incStrategy = null;
		StretchItem decStrategy = null;
		if (null != keyData) {
			JSONArray stretchItemsJSON = JSONArray.fromObject(keyData);
			Object[] objArray = stretchItemsJSON.toArray();
			if (objArray.length == 2) {
				for (Object o : objArray) {
					JSONObject obj = JSONObject.fromObject(o);
					StretchItem item = (StretchItem) (JSONObject.toBean(obj,
							StretchItem.class));
					if ("INCREASE".equals(item.getStrategyType())) { //$NON-NLS-1$
						incStrategy = item;
					} else if ("DECREASE".equals(item.getStrategyType())) { //$NON-NLS-1$
						decStrategy = item;
					}
				}
			}
		}

		Order order = null;
		if (incStrategy != null && decStrategy != null) {
			StrategyConfigInfo stretchConf = new StrategyConfigInfo();
			stretchConf.setAppName(appName);
			stretchConf.setStrategyName(appName);
			stretchConf.setIncStrategy(incStrategy);
			stretchConf.setDecStrategy(decStrategy);
			order = setAppStretchStrategy(stretchConf);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", order); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("setAppAsGlobalStretchStrategy/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response setGlobalStretchStrategy(
			@PathParam("appName") String appName) {
		Order order = doSetGlobalStretchStrategy(appName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", order); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("getAppStrategyType/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAppStrategyType(@PathParam("appName") String appName) {
		StrategyConfigInfo appConfigInfo = TelescopicStrategyUtil
				.getAppStretchStrategy(appName);
		String type = appConfigInfo.getStrategyName();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("type", type); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

}
