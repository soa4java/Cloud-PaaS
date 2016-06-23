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

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.console.platform.service.resource.HostManagerUtil;
import com.primeton.paas.manage.api.factory.HostManagerFactory;
import com.primeton.paas.manage.api.factory.HostTemplateManagerFactory;
import com.primeton.paas.manage.api.factory.VariableManagerFactory;
import com.primeton.paas.manage.api.manager.IHostManager;
import com.primeton.paas.manage.api.manager.IHostTemplateManager;
import com.primeton.paas.manage.api.manager.IVariableManager;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.HostTemplate;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.factory.VmPoolConfigFactory;
import com.primeton.paas.manage.spi.model.VmPoolConfig;
import com.primeton.paas.manage.spi.resource.IVmPoolConfig;

/**
 * 
 * @author liming(li-ming@primeton.com)
 *
 */
@Path("/hostPoolMgr")
public class HostPoolMgrResource {
	
	private static IVmPoolConfig vmPoolConfigManager = VmPoolConfigFactory
			.getManager();
	private static IHostTemplateManager hostPackageManager = HostTemplateManagerFactory
			.getManager();
	private static IHostManager hostManager = HostManagerFactory
			.getHostManager();

	private static ILogger logger = LoggerFactory.getLogger(HostPoolMgrResource.class);

	private static IVariableManager varManager = VariableManagerFactory
			.getManager();
	
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
		VmPoolConfig criteria = new VmPoolConfig();
		if (null != keyData) {
			JSONObject  jsObj = JSONObject.fromObject(keyData) ;
			if (null != jsObj) {
				if (null != jsObj.getString("hostPackage")
						&& !"".equals(jsObj.getString("hostPackage"))) {
					criteria.setId(jsObj.getString("hostPackage"));
				}
			}
		}
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex*pageSize);
		pageCond.setLength(pageSize);
		
		VmPoolConfig[] cfgs = queryHostPools(criteria, pageCond);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", cfgs);
		result.put("total", pageCond.getCount());
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	/**
	 * 
	 * @return
	 */
	@Path("getHostPackages")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHostPackages() {
		ResponseBuilder builder = Response.ok(getHostTemplates());
		return builder.build();
	}
	
	/**
	 * 
	 * @return
	 */
	@Path("getFilterHostPackages")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFilterHostPackages() {
		ResponseBuilder builder = Response.ok(getFilterHostTemplates());
		return builder.build();
	}
	
	/**
	 * 
	 * @return
	 */
	@Path("getFilterHostPackagesNum")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFilterHostPackagesNum() {
		List<HostTemplate> templates = getFilterHostTemplates();
		ResponseBuilder builder = Response.ok(templates == null ? 0 : templates.size());
		return builder.build();
	}
	
	/**
	 * 
	 * @param vmPoolConfigId
	 * @return
	 */
	@Path("/list/{vmPoolConfigId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response list(@PathParam("vmPoolConfigId") String vmPoolConfigId) {
		Host[]  hosts = getHostPackageOfHosts(vmPoolConfigId, new PageCond());
		ResponseBuilder builder = Response.ok(hosts);
		return builder.build();
	}
	
	/**
	 * 
	 * @param hostPools
	 * @return
	 */
	@Path("/delHostPools")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response delHostPools(String hostPools) {
		boolean rtn = removeHostPools(hostPools.split(","));
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}	
	
	/**
	 * 
	 * @param hosts
	 * @return
	 */
	@Path("/delHosts")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response delHosts(String hosts) {
		boolean rtn = HostManagerUtil.delHosts(hosts);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}	
	
	/**
	 * 
	 * @param hosts
	 * @return
	 */
	@Path("/applyHosts")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response applyHosts(String hosts) {
		boolean rtn = HostManagerUtil.applyHosts(hosts.split(","));
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	@Path("/isExistHostIP/{ip}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response isExistHostIP(@PathParam("ip") String ip) {
		boolean rtn = HostManagerUtil.isExistHostIP(ip);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}	
	
	/**
	 * 
	 * @param host
	 * @return
	 */
	@Path("/addHost")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response addHost(Host host) {
		boolean rtn = addHostPackageOfHost(host);
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	/**
	 * 获取常量｛minSize｝<br>
	 * 	
	 * @return
	 */
	@Path("getHostPoolMinSize")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHostPoolMinSize() {
		String[] strInfo = SystemVariable.getHostPoolMinSize();
		List<Map> dataList = new ArrayList<Map>();
		for (String st : strInfo) {
			Map data = new HashMap();
			data.put("key", st); //$NON-NLS-1$
			data.put("value", st); //$NON-NLS-1$
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
	@Path("getHostPoolMaxSize")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHostPoolMaxSize() {
		String[] strInfo = SystemVariable.getHostPoolMaxSize();
		List<Map> dataList = new ArrayList<Map>();
		for (String st : strInfo) {
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
	@Path("getHostPoolIncreaseSize")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHostPoolIncreaseSize() {
		String[] strInfo = SystemVariable.getHostPoolIncreaseSize();
		List<Map> dataList = new ArrayList<Map>();
		for (String st : strInfo) {
			Map data = new HashMap();
			data.put("key", st); //$NON-NLS-1$
			data.put("value", st); //$NON-NLS-1$
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
	@Path("getHostPoolDecreaseSize")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHostPoolDecreaseSize() {
		String[] strInfo = SystemVariable.getHostPoolDecreaseSize();
		List<Map> dataList = new ArrayList<Map>();
		for (String st : strInfo) {
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
	@Path("getHostPoolTimeInterval")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHostPoolTimeInterval() {
		String[] strInfo = SystemVariable.getHostPoolTimeInterval();
		List<Map> dataList = new ArrayList<Map>();
		for (String st : strInfo) {
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
	 * @param cfg
	 * @return
	 */
	@Path("/addHostPool")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response addHostPool(VmPoolConfig cfg) {
		boolean rtn = false;
		if (cfg == null) {
			rtn = false;
			ResponseBuilder builder = Response.ok(rtn);
			return builder.build();
		}
		int vmOperateTimeout = varManager.getIntValue(SystemVariable.VM_OPERATE_TIMEOUT,
				100 * 1000) / 1000;
		cfg.setCreateTimeout(vmOperateTimeout);
		cfg.setDestroyTimeout(vmOperateTimeout);
		try {
			vmPoolConfigManager.add(cfg);
			rtn = true;
		} catch (Exception e) {
			logger.error(e);
		}
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}
	
	/**
	 * 
	 * @param cfg
	 * @return
	 */
	@Path("/updateHostPool")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response updateHostPool(VmPoolConfig cfg) {
		boolean success = false;
		if (cfg == null || StringUtil.isEmpty(cfg.getId())) {
			success = false;
		}
		int vmOperateTimeout = varManager.getIntValue(SystemVariable.VM_OPERATE_TIMEOUT,
				100 * 1000) / 1000;
		cfg.setCreateTimeout(vmOperateTimeout);
		cfg.setDestroyTimeout(vmOperateTimeout);
		try {
			vmPoolConfigManager.update(cfg);
			success = true;
		} catch (Exception e) {
			logger.error(e);
			success = false;
		}
		ResponseBuilder builder = Response.ok(success);
		return builder.build();
	}
	
	/**
	 * 
	 * @param vmPoolConfig
	 * @param page
	 * @return
	 */
	public static VmPoolConfig[] queryHostPools(VmPoolConfig vmPoolConfig,
			IPageCond page) {
		List<VmPoolConfig> vmPoolConfigs = vmPoolConfigManager.getAll(
				vmPoolConfig.getId(), vmPoolConfig.getName(), page);
		if (null != vmPoolConfigs) {
			for (VmPoolConfig v : vmPoolConfigs) {
				Host[] hosts = getHostPackageOfHosts(v.getId(), new PageCond());
				int size = 0;
				if (null != hosts) {
					size = hosts.length;
				}
				v.getAttributes().put("size", size + "");
			}
			return (VmPoolConfig[]) vmPoolConfigs
					.toArray(new VmPoolConfig[vmPoolConfigs.size()]);
		}
		return null;
	}

	/**
	 * 
	 * @param vmPoolConfigId
	 * @return
	 */
	public static VmPoolConfig queryHostPool(String vmPoolConfigId) {
		try {
			return vmPoolConfigManager.get(vmPoolConfigId);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 批量删除主机池
	 * 
	 * @param orders
	 * @return
	 */

	public static boolean removeHostPools(VmPoolConfig[] vmPoolConfigs) {
		if (vmPoolConfigs == null || vmPoolConfigs.length < 1) {
			return true;
		}
		for (VmPoolConfig cur : vmPoolConfigs) {
			String id = cur.getId();
			vmPoolConfigManager.delete(id);
		}
		return true;
	}

	/**
	 * 批量删除主机池
	 * 
	 * @param orders
	 * @return
	 */

	public static boolean removeHostPools(String[] vmPoolConfigs) {
		if (vmPoolConfigs == null || vmPoolConfigs.length < 1) {
			return true;
		}
		for (String id : vmPoolConfigs) {
			vmPoolConfigManager.delete(id);
		}
		return true;
	}

	/**
	 * 获取全部主机套餐<br>
	 * 
	 * @return
	 */

	public static List<HostTemplate> getHostTemplates() {
		return hostPackageManager.getTemplates(null);
	}

	/**
	 * 获取全部未使用主机套餐<br>
	 * 
	 * @return
	 */
	public static List<HostTemplate> getFilterHostTemplates() {
		List<HostTemplate> templates = hostPackageManager.getTemplates(null);
		templates = (null == templates) ? new ArrayList<HostTemplate>()
				: templates;
		List<HostTemplate> leftTemplates = new ArrayList<HostTemplate>();
		for (HostTemplate template : templates) {
			if (vmPoolConfigManager.get(template.getTemplateId()) == null) {
				leftTemplates.add(template);
			}
		}
		return leftTemplates;
	}

	/**
	 * 根据主机套餐Id获取包含下的所有主机<br>
	 * 
	 * @return
	 */
	public static Host[] getHostPackageOfHosts(String vmPoolConfigId,
			IPageCond hostPage) {
		Host[] hosts = hostManager.getByPackage(vmPoolConfigId);
		// 未分配主机过滤
		List<Host> hostList = new ArrayList<Host>();
		for (Host host : hosts) {
			if (!host.isAssigned()) {
				hostList.add(host);
			}
		}
		hostPage.setCount(hostList.size());
		return hostList.toArray(new Host[hostList.size()]);
	}

	/**
	 * 根据主机套餐添加主机<br>
	 * addHostPool
	 * 
	 * @return
	 */
	public static boolean addHostPackageOfHost(Host host) {
		try {
			host.setAssigned(false);
			host.setStandalone(false);
			hostManager.add(host);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}
	
}
