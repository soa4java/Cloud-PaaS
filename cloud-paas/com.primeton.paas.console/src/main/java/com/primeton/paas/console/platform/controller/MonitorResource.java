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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.primeton.paas.console.common.Entry;
import com.primeton.paas.console.common.monitor.AppMonitorUtil;
import com.primeton.paas.console.common.monitor.CPUInfo;
import com.primeton.paas.console.common.monitor.CacheMonitorUtil;
import com.primeton.paas.console.common.monitor.HostMonitorUtil;
import com.primeton.paas.console.common.monitor.IOInfo;
import com.primeton.paas.console.common.monitor.LoadAverageInfo;
import com.primeton.paas.console.common.monitor.MemoryInfo;
import com.primeton.paas.console.common.monitor.ServiceMonitorInfo;
import com.primeton.paas.console.common.stretch.ServiceStrategyItemInfo;
import com.primeton.paas.console.common.stretch.StrategyConfigInfo;
import com.primeton.paas.console.common.stretch.StrategyConfigInfo.StretchItem;
import com.primeton.paas.console.common.stretch.StrategyItemInfo;
import com.primeton.paas.console.platform.service.monitor.ServiceMonitorUtil;
import com.primeton.paas.console.platform.service.monitor.TaskMgrUtil;
import com.primeton.paas.console.platform.service.monitor.TelescopicStrategyUtil;
import com.primeton.paas.console.platform.service.monitor.VariablesMgrUtil;
import com.primeton.paas.console.platform.service.srvmgr.ClusterMangerUtil;
import com.primeton.paas.manage.api.app.ServiceWarnStrategy;
import com.primeton.paas.manage.api.app.ServiceWarnStrategyItem;
import com.primeton.paas.manage.api.app.StrategyItem;
import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.cluster.MemcachedCluster;
import com.primeton.paas.manage.api.cluster.MySQLCluster;
import com.primeton.paas.manage.api.cluster.TomcatCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.Task;
import com.primeton.paas.manage.api.model.Variable;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 平台监控
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
@Path("/monitor")
public class MonitorResource {
	
	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager();
	
	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/varlist")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response varlist(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		Variable criteria = new Variable();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setVarKey(jsObj.getString("varKey")); //$NON-NLS-1$
				criteria.setVarValue(jsObj.getString("varValue")); //$NON-NLS-1$
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		Variable[] vars = VariablesMgrUtil.getVariables(criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", vars); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param varKey
	 * @return
	 */
	@Path("checkVarKey/{varKey}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkVarKey(@PathParam("varKey") String varKey) {
		boolean ifexits = VariablesMgrUtil.isExistPasVar(varKey);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", ifexits); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param keyData
	 * @return
	 */
	@Path("addVar")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addVar(@FormParam("keyData") String keyData) {
		Variable var = new Variable();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				var.setVarKey(jsObj.getString("varKey")); //$NON-NLS-1$
				var.setVarValue(jsObj.getString("varValue")); //$NON-NLS-1$
				var.setDescription(jsObj.getString("description")); //$NON-NLS-1$
			}
		}
		boolean rs = VariablesMgrUtil.addPasVar(var);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param keyData
	 * @return
	 */
	@Path("updateVar")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateVar(@FormParam("keyData") String keyData) {
		Variable var = new Variable();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				var.setVarKey(jsObj.getString("varKey")); //$NON-NLS-1$
				var.setVarValue(jsObj.getString("varValue")); //$NON-NLS-1$
				var.setDescription(jsObj.getString("description")); //$NON-NLS-1$
			}
		}
		boolean rs = VariablesMgrUtil.updateVar(var);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param varKeys
	 * @return
	 */
	@Path("deleteVars/{varKeys}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteVars(@PathParam("varKeys") String varKeys) {
		String[] varKeyss = varKeys.split(","); //$NON-NLS-1$
		boolean rs = VariablesMgrUtil.removeVars(varKeyss);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/tasklist")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response tasklist(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		Task criteria = new Task();
		if (null != keyData) {
			JSONObject jsObj = JSONObject.fromObject(keyData);
			if (null != jsObj) {
				criteria.setId(jsObj.getString("id")); //$NON-NLS-1$
				if (!"defaultValue".equals(jsObj.getString("status"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.setStatus(jsObj.getString("status")); //$NON-NLS-1$
				}
				if (!"defaultValue".equals(jsObj.getString("type"))) { //$NON-NLS-1$ //$NON-NLS-2$
					criteria.setType(jsObj.getString("type")); //$NON-NLS-1$
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex * pageSize);
		pageCond.setLength(pageSize);

		Task[] tasks = TaskMgrUtil.queryTasks(criteria, pageCond);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", tasks); //$NON-NLS-1$
		result.put("total", pageCond.getCount()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param taskIds
	 * @return
	 */
	@Path("deleteTasks/{taskIds}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTasks(@PathParam("taskIds") String taskIds) {
		boolean rs = TaskMgrUtil.delTasks(taskIds);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	@Path("abortTask/{taskId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response abortTask(@PathParam("taskId") String taskId) {
		boolean rs = TaskMgrUtil.abortTask(taskId);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", rs); //$NON-NLS-1$
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
	 * @return
	 */
	@Path("/getItemInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response getItemInfo() {
		StrategyItemInfo itemInfo = TelescopicStrategyUtil.getItemInfo();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", itemInfo); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param keyData
	 * @return
	 */
	@Path("/updateGlobalStretchStrategy")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response updateGlobalStretchStrategy(
			@FormParam("keyData") String keyData) {
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
		boolean rs = false;
		if (incStrategy != null && decStrategy != null) {
			StrategyConfigInfo stretchConf = new StrategyConfigInfo();
			stretchConf.setIncStrategy(incStrategy);
			stretchConf.setDecStrategy(decStrategy);
			StrategyConfigInfo stretchConfAfter = TelescopicStrategyUtil
					.updateGlobalStretchStrategy(stretchConf);
			if (stretchConfAfter != null) {
				rs = true;
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
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
			result.put("data", new StretchItem[0]); //$NON-NLS-1$
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
	 * 获取所有平台应用名
	 * 
	 * @return
	 */
	@Path("/getApps")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApps() {
		List<WebApp> apps = TelescopicStrategyUtil.queryApps();
		// 为显示需要
		for (WebApp app : apps) {
			app.setDisplayName(app.getDisplayName() + "(" + app.getName() + ")");
		}
		WebApp[] rs = apps.toArray(new WebApp[apps.size()]);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", rs); //$NON-NLS-1$
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
		boolean rs = false;
		if (incStrategy != null && decStrategy != null) {
			StrategyConfigInfo stretchConf = new StrategyConfigInfo();
			stretchConf.setAppName(appName);
			stretchConf.setStrategyName(appName);
			stretchConf.setIncStrategy(incStrategy);
			stretchConf.setDecStrategy(decStrategy);
			rs = TelescopicStrategyUtil.setAppStretchStrategy(stretchConf);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("setGlobalStretchStrategy/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response setGlobalStretchStrategy(
			@PathParam("appName") String appName) {
		boolean rs = TelescopicStrategyUtil.setGlobalStretchStrategy(appName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
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
	
	/**
	 * CPU监控数据
	 * 
	 * @param appName
	 * @param timeInterval
	 * @return
	 */
	@Path("/getAppCPUInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response getAppCPUInfo(@FormParam("appName") String appName,
			@FormParam("timeInterval") String timeInterval) {
		long timeIntervalx = Long.parseLong(timeInterval);
		CPUInfo info = AppMonitorUtil.getCurrentCPUInfo(appName, timeIntervalx);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("uses", info.getUses()); //$NON-NLS-1$
		result.put("times", info.getTimes()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param appName
	 * @param timeInterval
	 * @return
	 */
	@Path("/getAppMemoryInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response getAppMemoryInfo(@FormParam("appName") String appName,
			@FormParam("timeInterval") String timeInterval) {
		long timeIntervalx = Long.parseLong(timeInterval);
		MemoryInfo info = AppMonitorUtil.getCurrentMemoryInfo(appName,
				timeIntervalx);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("uses", info.getUses()); //$NON-NLS-1$
		result.put("times", info.getTimes()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param appName
	 * @param timeInterval
	 * @return
	 */
	@Path("/getAppLoadAverageInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response getAppLoadAverageInfo(@FormParam("appName") String appName,
			@FormParam("timeInterval") String timeInterval) {
		long timeIntervalx = Long.parseLong(timeInterval);
		LoadAverageInfo info = AppMonitorUtil.getCurrentLoadAverageInfo(
				appName, timeIntervalx);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("loadAverages", info.getLoadAverages()); //$NON-NLS-1$
		result.put("times", info.getTimes()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param appName
	 * @param timeInterval
	 * @return
	 */
	@Path("/getAppIOInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response getAppIOInfo(@FormParam("appName") String appName,
			@FormParam("timeInterval") String timeInterval) {
		long timeIntervalx = Long.parseLong(timeInterval);
		IOInfo info = AppMonitorUtil.getCurrentIOInfo(appName, timeIntervalx);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("inputs", info.getInputs()); //$NON-NLS-1$
		result.put("outputs", info.getOutputs()); //$NON-NLS-1$
		result.put("times", info.getTimes()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("getAppState/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAppState(@PathParam("appName") String appName) {
		int state = AppMonitorUtil.getAppState(appName); 
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("state", state); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @return
	 */
	@Path("getMonitorTimeLengths")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMonitorTimeLengths() {
		Integer[] timeLengths = AppMonitorUtil.getTimeLengths();
		List<Entry> list = new ArrayList<Entry>();
		for (Integer timeLength : timeLengths) {
			Entry cm = new Entry();
			cm.setId(String.valueOf(timeLength));
			cm.setText(timeLength + "分钟"); //$NON-NLS-1$
			list.add(cm);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new Entry[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @return
	 */
	@Path("getMonitorRefreshTimes")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMonitorRefreshTimes() {
		Integer[] refreshTimes = AppMonitorUtil.getRefreshTime();
		List<Entry> list = new ArrayList<Entry>();
		for (Integer refreshTime : refreshTimes) {
			Entry cm = new Entry();
			cm.setId(String.valueOf(refreshTime));
			cm.setText(refreshTime + "s"); //$NON-NLS-1$
			list.add(cm);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list.toArray(new Entry[list.size()])); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 获取所有平台主机名 只用于监控时
	 * @return
	 */
	@Path("/getHosts")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHosts() {
		Host[] hosts = HostMonitorUtil.queryUsedHost();
		// 为显示需要
		for (Host host : hosts) {
			host.setName(host.getIp() + "[" + host.getType() + "]");
		}
		//$NON-NLS-1$
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", hosts); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param hostIp
	 * @return
	 */
	@Path("getHostState/{hostIp}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHostState(@PathParam("hostIp") String hostIp) {
		boolean state = HostMonitorUtil.getHostState(hostIp);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("state", state); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param ip
	 * @param timeInterval
	 * @return
	 */
	@Path("/getHostCPUInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response getHostCPUInfo(@FormParam("ip") String ip,
			@FormParam("timeInterval") String timeInterval) {
		long timeIntervalx = Long.parseLong(timeInterval);
		CPUInfo info = HostMonitorUtil.getCurrentCPUInfo(ip, timeIntervalx);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("uses", info.getUses()); //$NON-NLS-1$
		result.put("times", info.getTimes()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param ip
	 * @param timeInterval
	 * @return
	 */
	@Path("/getHostMemoryInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response getHostMemoryInfo(@FormParam("ip") String ip,
			@FormParam("timeInterval") String timeInterval) {
		long timeIntervalx = Long.parseLong(timeInterval);
		MemoryInfo info = HostMonitorUtil.getCurrentMemoryInfo(ip,
				timeIntervalx);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("uses", info.getUses()); //$NON-NLS-1$
		result.put("times", info.getTimes()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param ip
	 * @param timeInterval
	 * @return
	 */
	@Path("/getHostLoadAverageInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response getHostLoadAverageInfo(@FormParam("ip") String ip,
			@FormParam("timeInterval") String timeInterval) {
		long timeIntervalx = Long.parseLong(timeInterval);
		LoadAverageInfo info = HostMonitorUtil.getCurrentLoadAverageInfo(ip,
				timeIntervalx);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("loadAverages", info.getLoadAverages()); //$NON-NLS-1$
		result.put("times", info.getTimes()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param ip
	 * @param timeInterval
	 * @return
	 */
	@Path("/getHostIOInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response getHostIOInfo(@FormParam("ip") String ip,
			@FormParam("timeInterval") String timeInterval) {
		long timeIntervalx = Long.parseLong(timeInterval);
		IOInfo info = HostMonitorUtil.getCurrentIOInfo(ip, timeIntervalx);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("inputs", info.getInputs()); //$NON-NLS-1$
		result.put("outputs", info.getOutputs()); //$NON-NLS-1$
		result.put("times", info.getTimes()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 缓存监控
	 */
	@Path("/getCacheClusters")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCacheClusters() {
		List<MemcachedCluster> mcList = CacheMonitorUtil.getMemcachedClusters();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", mcList); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getServiceMonitorData/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServiceMonitorData(
			@PathParam("clusterId") String clusterId) {
		int[] sizeAndStatus = ClusterMangerUtil.getClusterSizeAndStatus(clusterId);
		int state = null == sizeAndStatus || sizeAndStatus.length != 2 
				? IService.STATE_NOT_RUNNING
				: sizeAndStatus[1];
		ServiceMonitorInfo data = new ServiceMonitorInfo();
		if (state == IService.STATE_RUNNING) {
			data = ServiceMonitorUtil.getServiceMonitorData(clusterId);
		}
		if (state == 0 || data == null || data.getCpuFree() == null) {
			data.setCpuFree("100"); //$NON-NLS-1$
			data.setCpuUse("0"); //$NON-NLS-1$
			data.setMemFree("100"); //$NON-NLS-1$
			data.setMemUse("0"); //$NON-NLS-1$
			data.setIoi("0"); //$NON-NLS-1$
			data.setIoo("0"); //$NON-NLS-1$
			data.setOneload("0"); //$NON-NLS-1$
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("info", data); //$NON-NLS-1$
		result.put("serviceState", state); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("queryIfAppEnableServiceMonitor/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryIfAppEnableServiceMonitor(
			@PathParam("appName") String appName) {
		boolean flag = ServiceMonitorUtil
				.queryIfAppEnableServiceMonitor(appName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", flag); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("getClusterTypeByApp/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getClusterTypeByApp(@PathParam("appName") String appName) {
		ICluster[] clusters = getClusterByApp(appName);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HaProxyCluster.TYPE, false);
		data.put(JettyCluster.TYPE, false);
		data.put(TomcatCluster.TYPE, false);
		data.put(MySQLCluster.TYPE, false);
		data.put(MemcachedCluster.TYPE, false);

		for (ICluster cluster : clusters) {
			if (HaProxyCluster.TYPE.equals(cluster.getType())) {
				data.put(HaProxyCluster.TYPE, true);
				data.put(HaProxyCluster.TYPE + "ClusterId", cluster.getId());
				continue;
			}
			if (JettyCluster.TYPE.equals(cluster.getType())) {
				data.put(JettyCluster.TYPE, true);
				data.put(JettyCluster.TYPE + "ClusterId", cluster.getId());
				continue;
			}
			if (TomcatCluster.TYPE.equals(cluster.getType())) {
				data.put(TomcatCluster.TYPE, true);
				data.put(TomcatCluster.TYPE + "ClusterId", cluster.getId());
				continue;
			}
			if (MySQLCluster.TYPE.equals(cluster.getType())) {
				data.put(MySQLCluster.TYPE, true);
				data.put(MySQLCluster.TYPE + "ClusterId", cluster.getId());
				continue;
			}
			if (MemcachedCluster.TYPE.equals(cluster.getType())) {
				data.put(MemcachedCluster.TYPE, true);
				data.put(MemcachedCluster.TYPE + "ClusterId", cluster.getId());
				continue;
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", data); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("getServiceStrategyConfigInfo/{clusterId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServiceStrategyConfigInfo(
			@PathParam("clusterId") String clusterId) {
		ServiceWarnStrategy strategy = ServiceMonitorUtil
				.getServiceWarnStrategy(clusterId);
		List<ServiceWarnStrategyItem> items = new ArrayList<ServiceWarnStrategyItem>();
		if (strategy != null) {
			items = strategy.getServiceWarnStrategyItems();
		}

		int cpuThreshold = 0;
		int memThreshold = 0;
		float lbThreshold = 0;
		for (ServiceWarnStrategyItem item : items) {
			String type = item.getItemType();
			String threshold = StringUtil.isEmpty(item.getThreshold()) ? "0" : item.getThreshold(); //$NON-NLS-1$
			if (StrategyItem.TYPE_CPU.equals(type)) {
				cpuThreshold = Integer.parseInt(threshold);
			} else if (StrategyItem.TYPE_MEMORY.equals(type)) {
				memThreshold = Integer.parseInt(threshold);
			} else if (StrategyItem.TYPE_LB.equals(type)) {
				lbThreshold = Float.parseFloat(threshold);
			}
		}
		ServiceStrategyItemInfo ssi = new ServiceStrategyItemInfo();
		if (strategy != null) {
			ssi.setEnableFlag(strategy.isEnable() == true ? "Y" : "N"); //$NON-NLS-1$ //$NON-NLS-2$
			ssi.setAlarmType(strategy.getAlarmType());
			ssi.setAlarmAddress(strategy.getAlarmAddress());
			ssi.setContinuedTime(strategy.getContinuedTime());
			ssi.setIgnoreTime(strategy.getIgnoreTime());
			ssi.setCpuThreshold(String.valueOf(cpuThreshold));
			ssi.setMemThreshold(String.valueOf(memThreshold));
			ssi.setLbThreshold(String.valueOf(lbThreshold));
		} else {// 从未设置过
			ssi.setEnableFlag("N"); //$NON-NLS-1$
			ssi.setContinuedTime(60L);
			ssi.setIgnoreTime(60L);
			ssi.setCpuThreshold(String.valueOf("80")); //$NON-NLS-1$
			ssi.setMemThreshold(String.valueOf("80")); //$NON-NLS-1$
			ssi.setLbThreshold(String.valueOf("2.0")); //$NON-NLS-1$
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", ssi); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @return
	 */
	@Path("/getGlobalServiceStrategyConfigInfo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getGlobalServiceStrategyConfigInfo() {
		ServiceWarnStrategy strategy = ServiceMonitorUtil.getGlobalServiceStrategyConfigInfo();
		List<ServiceWarnStrategyItem> items = new ArrayList<ServiceWarnStrategyItem>();
		if(strategy!=null){
			items = strategy.getServiceWarnStrategyItems();
		}
		
		int cpuThreshold = 0;
		int memThreshold = 0;
		float lbThreshold = 0;
		
		for (ServiceWarnStrategyItem item : items) {
			String type = item.getItemType();
			String threshold = StringUtil.isEmpty(item.getThreshold()) ? "0" : item.getThreshold(); //$NON-NLS-1$
			if (StrategyItem.TYPE_CPU.equals(type)) {
				cpuThreshold = Integer.parseInt(threshold);
			} else if (StrategyItem.TYPE_MEMORY.equals(type)) {
				memThreshold = Integer.parseInt(threshold);
			} else if (StrategyItem.TYPE_LB.equals(type)) {
				lbThreshold = Float.parseFloat(threshold);
			}  
		}
		
		ServiceStrategyItemInfo ssi = new ServiceStrategyItemInfo();
		if(strategy!=null){
			ssi.setEnableFlag(strategy.isEnable()==true?"Y":"N"); //$NON-NLS-1$ //$NON-NLS-2$
			ssi.setAlarmType(strategy.getAlarmType());
			ssi.setAlarmAddress(strategy.getAlarmAddress());
			ssi.setContinuedTime(strategy.getContinuedTime());
			ssi.setIgnoreTime(strategy.getIgnoreTime());
			ssi.setCpuThreshold(String.valueOf(cpuThreshold));
			ssi.setMemThreshold(String.valueOf(memThreshold));
			ssi.setLbThreshold(String.valueOf(lbThreshold));
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", ssi); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param keyData
	 * @return
	 */
	@Path("/updateGlobalServiceWarnStrategy")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  
	public Response updateGlobalServiceWarnStrategy(
			@FormParam("keyData") String keyData) {
		// 保存/修改 服务报警策略信息
		ServiceStrategyItemInfo ssi = new ServiceStrategyItemInfo();
		if (null != keyData) {
			JSONArray stretchItemsJSON = JSONArray.fromObject(keyData);
			Object[] objArray = stretchItemsJSON.toArray();
			if (objArray.length == 1) {
				JSONObject obj = JSONObject.fromObject(objArray[0]);
				ssi = (ServiceStrategyItemInfo) (JSONObject.toBean(obj,
						ServiceStrategyItemInfo.class));
			}
		}
		boolean rs = ServiceMonitorUtil.saveServiceGlobalStrategy(ssi);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 设置服务报警策略为全局策略
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("setServiceGlobalStretchStrategy/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response setServiceGlobalStretchStrategy(
			@PathParam("clusterId") String clusterId) {
		// 设置服务报警为全局策略
		boolean rs = ServiceMonitorUtil
				.setServiceGlobalStretchStrategy(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 判断是否使用全局报警策略
	 * 
	 * @param clusterId
	 * @return true 全局配置 | false 个性化配置
	 */
	@Path("getServiceStrategyType/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServiceStrategyType(
			@PathParam("clusterId") String clusterId) {
		// 判断是否为全局配置
		ServiceWarnStrategy sws = ServiceMonitorUtil
				.getServiceWarnStrategy(clusterId);
		boolean rs = false;
		if (sws != null
				&& ServiceWarnStrategy.GLOBAL_STRATEGY_ID.equals(sws
						.getStrategyId())) {
			rs = true;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param keyData
	 * @param clusterId
	 * @return
	 */
	@Path("/updateServiceWarnStrategy")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateServiceWarnStrategy(
			@FormParam("keyData") String keyData,
			@FormParam("clusterId") String clusterId) {
		// 保存/修改 服务报警策略信息
		ServiceStrategyItemInfo ssi = new ServiceStrategyItemInfo();
		if (null != keyData) {
			JSONArray stretchItemsJSON = JSONArray.fromObject(keyData);
			Object[] objArray = stretchItemsJSON.toArray();
			if (objArray.length == 1) {
				JSONObject obj = JSONObject.fromObject(objArray[0]);
				ssi = (ServiceStrategyItemInfo) (JSONObject.toBean(obj,
						ServiceStrategyItemInfo.class));
			}
		}
		boolean rs = ServiceMonitorUtil.saveServiceWarnStrategy(ssi, clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("enableServiceMonitor/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response enableServiceMonitor(@PathParam("appName") String appName) {
		ServiceMonitorUtil.enableServiceMonitor(appName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("disableServiceMonitor/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response disableServiceMonitor(@PathParam("appName") String appName) {
		ServiceMonitorUtil.disableServiceMonitor(appName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @return
	 */
	@Path("/getPlatformClusters")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlatformClusters() {
		List<ICluster> cList = ServiceMonitorUtil.getPlatformClusters();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cList); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("queryIfClusterEnableServiceMonitor/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryIfClusterEnableServiceMonitor(
			@PathParam("clusterId") String clusterId) {
		boolean flag = ServiceMonitorUtil
				.queryIfClusterEnableServiceMonitor(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", flag); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("enableSingleServiceMonitor/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response enableSingleServiceMonitor(
			@PathParam("clusterId") String clusterId) {
		ServiceMonitorUtil.enableSingleServiceMonitor(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	@Path("disableSingleServiceMonitor/{clusterId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response disableSingleServiceMonitor(
			@PathParam("clusterId") String clusterId) {
		ServiceMonitorUtil.disableSingleServiceMonitor(clusterId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	public static ICluster[] getClusterByApp(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		ICluster[] clusters = clusterManager.getByApp(appName);
		if (clusters != null) {
			for (ICluster cluster : clusters) {
				int size = ClusterMangerUtil.getClusterSize(cluster.getId());
				cluster.getAttributes().put("size", size + "");
			}
		}
		return clusters;
	}

}
