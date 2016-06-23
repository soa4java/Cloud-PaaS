/**
 * 
 */
package com.primeton.paas.manage.api.impl.util;

import java.util.Map;

import org.gocom.cloud.cesium.manage.service.api.exception.ServiceInstanceManagerException;
import org.gocom.cloud.cesium.model.api.Application;
import org.gocom.cloud.cesium.model.api.Cluster;
import org.gocom.cloud.cesium.model.api.ServiceInstance;

import com.primeton.paas.manage.api.factory.ClusterFactory;
import com.primeton.paas.manage.api.factory.RuntimeManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceFactory;
import com.primeton.paas.manage.api.manager.IRuntimeManager;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ObjectUtil {

	/**
	 * Default. <br>
	 */
	private ObjectUtil() {
		super();
	}

	/**
	 * 
	 * @param service
	 * @return
	 */
	public static ServiceInstance toCesium(IService service) {
		if (service == null) {
			return null;
		}
		ServiceInstance instance = new ServiceInstance();
		if (!StringUtil.isEmpty(service.getId())) {
			instance.setId(Long.parseLong(service.getId()));
		}
		instance.setIp(service.getIp());
		instance.setName(service.getName());
		instance.setOwner(service.getOwner());
		instance.setParentId(StringUtil.isEmpty(service.getParentId()) ? -1
				: Long.parseLong(service.getParentId()));
		instance.setPid(service.getPid());
		instance.setPort(service.getPort());
		instance.setServiceDefName(service.getType());
		instance.setStartTime(service.getStartDate());
		instance.setState(service.getState());
		instance.setAttributes(service.getAttributes());

		return instance;
	}

	/**
	 * 
	 * @param instance
	 * @return
	 */
	public static IService toPAAS(ServiceInstance instance) {
		if (instance == null) {
			return null;
		}
		String type = instance.getServiceDefName();
		IService service = ServiceFactory.newInstance(type);
		if (service == null) {
			return null;
		}
		service.setId(String.valueOf(instance.getId()));
		service.setIp(instance.getIp());
		service.setName(instance.getName());
		service.setOwner(instance.getOwner());
		service.setParentId(String.valueOf(instance.getParentId()));
		service.setPid(instance.getPid());
		service.setPort(instance.getPort());
		service.setStartDate(instance.getStartTime());
		service.setState(instance.getState());
		service.getAttributes().putAll(instance.getAttributes());
		return service;
	}

	/**
	 * 
	 * @param cluster
	 * @return
	 */
	public static Cluster toCesium(ICluster cluster) {
		if (cluster == null) {
			return null;
		}
		Cluster c = new Cluster();
		c.setName(cluster.getId());
		c.setType(cluster.getType());
		c.setDisplayName(cluster.getName());
		c.setOwner(cluster.getOwner());
		c.setDesc(cluster.getDesc());
		c.setAttributes(cluster.getAttributes());
		return c;
	}

	/**
	 * 
	 * @param cluster
	 * @return
	 */
	public static ICluster toPAAS(Cluster cluster) {
		if (cluster == null || StringUtil.isEmpty(cluster.getType())) {
			return null;
		}
		ICluster target = ClusterFactory.newInstance(cluster.getType());
		if (target == null) {
			return null;
		}
		target.setId(cluster.getName());
		target.setDesc(cluster.getDesc());
		target.setName(cluster.getDisplayName());
		target.setOwner(cluster.getOwner());
		target.getAttributes().putAll(cluster.getAttributes());
		return target;
	}

	/**
	 * 
	 * @param application
	 * @return
	 */
	public static WebApp toPAAS(Application application) {
		if (application == null) {
			return null;
		}
		WebApp webApp = new WebApp();
		webApp.setName(application.getAppName());
		webApp.setDesc(application.getDesc());
		webApp.setDisplayName(application.getDisplayName());
		webApp.setOwner(application.getOwner());
		webApp.setAttributes(application.getAttributes());
		return webApp;
	}

	/**
	 * 
	 * @param webApp
	 * @return
	 */
	public static Application toCesium(WebApp webApp) {
		if (webApp == null) {
			return null;
		}
		Application application = new Application();
		application.setAppName(webApp.getName());
		application.setAttributes(webApp.getAttributes());
		application.setDesc(webApp.getDesc());
		application.setDisplayName(webApp.getDisplayName());
		application.setOwner(webApp.getOwner());
		return application;
	}

	/**
	 * 
	 * @param host
	 * @return
	 */
	public static org.gocom.cloud.cesium.model.api.Host toCesium(Host host) {
		if (host == null) {
			return null;
		}
		org.gocom.cloud.cesium.model.api.Host obj = new org.gocom.cloud.cesium.model.api.Host();
		obj.setIp(host.getIp());
		obj.setName(host.getName());
		obj.setTypes(host.getTypes());
		obj.setAttributes(host.getExts());
		return obj;
	}

	/**
	 * 
	 * @param host
	 * @return
	 * @throws ServiceInstanceManagerException
	 */
	public static Host toPAAS(org.gocom.cloud.cesium.model.api.Host host) {
		if (host == null) {
			return null;
		}
		Host obj = new Host();
		obj.setIp(host.getIp());
		obj.setName(host.getName());
		obj.setTypes(host.getTypes());
		Map<String, String> exts = host.getAttributes();
		if (exts != null && !exts.isEmpty()) {
			obj.getExts().putAll(exts);
		}

		// EXT
		IRuntimeManager runtimeManager = RuntimeManagerFactory.getManager();
		if (runtimeManager != null) {
			obj.setControlable(runtimeManager.agentIsOnline(obj.getIp()));
		}
		return obj;
	}

}
