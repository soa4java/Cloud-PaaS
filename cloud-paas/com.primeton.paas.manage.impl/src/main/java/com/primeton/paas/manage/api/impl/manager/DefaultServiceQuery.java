/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.gocom.cloud.cesium.common.api.model.PageCond;
import org.gocom.cloud.cesium.manage.service.api.ServiceInstanceManager;
import org.gocom.cloud.cesium.manage.service.api.exception.ServiceInstanceManagerException;
import org.gocom.cloud.cesium.model.api.ServiceInstance;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.CesiumFactory;
import com.primeton.paas.manage.api.impl.util.ObjectUtil;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultServiceQuery implements IServiceQuery {

	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultServiceManager.class);
	
	private static ServiceInstanceManager instanceManager = CesiumFactory.getServiceInstanceManager();
	
	/**
	 * Default. <br>
	 */
	public DefaultServiceQuery() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getTypes()
	 */
	public String[] getTypes() {
		List<String> types = new ArrayList<String>();
		ServiceLoader<IService> loader = ServiceLoader.load(IService.class);
		if (loader != null) {
			for (IService obj : loader) {
				if (obj != null && !StringUtil.isEmpty(obj.getType())) {
					types.add(obj.getType());
				}
			}
		}
		return types.toArray(new String[types.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#get(java.lang.String)
	 */
	public IService get(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			ServiceInstance instance = getInstanceManager().getServiceInst(Long.parseLong(id));
			return ObjectUtil.toPAAS(instance);
		} catch (Throwable t) {
			if(logger.isDebugEnabled()) {
				logger.debug(t);
			}
		} 
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByType(java.lang.String)
	 */
	public IService[] getByType(String type) {
		if (StringUtil.isEmpty(type)) {
			return null;
		}
		List<IService> list = new ArrayList<IService>();
		try {
			List<ServiceInstance> instances = getInstanceManager()
					.getServiceInstsByServiceDefName(type);
			if (instances != null && instances.size() > 0) {
				for (ServiceInstance instance : instances) {
					if (instance != null) {
						IService service = ObjectUtil.toPAAS(instance);
						if (service != null) {
							list.add(service);
						}
					}
				}
			}
		} catch (Throwable t) {
			logger.error(t.getMessage());
		}
		return list.toArray(new IService[list.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByType(java.lang.String, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public IService[] getByType(String type, IPageCond pageCond) {
		if (pageCond == null) {
			return getByType(type);
		}
		if (StringUtil.isEmpty(type)) {
			return null;
		}
		List<IService> list = new ArrayList<IService>();
		try {
			PageCond pc = new PageCond();
			pc.setBegin(pageCond.getBegin());
			pc.setLength(pageCond.getLength());
			List<ServiceInstance> instances = getInstanceManager()
					.getServiceInstsByServiceDefName(type, pc);
			if (instances != null && instances.size() > 0) {
				for (ServiceInstance instance : instances) {
					if (instance != null) {
						IService service = ObjectUtil.toPAAS(instance);
						if (service != null) {
							list.add(service);
						}
					}
				}
			}
			pageCond.setCount(pc.getCount());
		} catch (Throwable t) {
			if (logger.isDebugEnabled()) {
				logger.debug(t.getMessage());
			}
		}
		return list.toArray(new IService[list.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByOwner(java.lang.String, java.lang.String)
	 */
	public IService[] getByOwner(String owner, String type) {
		if (StringUtil.isEmpty(owner)) {
			return getByType(type);
		}
		if (StringUtil.isEmpty(type)) {
			return new IService[0];
		}
		List<IService> list = new ArrayList<IService>();
		try {
			List<ServiceInstance> all = getInstanceManager()
					.getServiceInstsByServiceDefName(type);
			List<ServiceInstance> instances = new ArrayList<ServiceInstance>();
			for (ServiceInstance instance : all) {
				if (instance != null && owner.equals(instance.getOwner())) {
					instances.add(instance);
				}
			}
			if (instances != null && instances.size() > 0) {
				for (ServiceInstance instance : instances) {
					if (instance != null) {
						IService service = ObjectUtil.toPAAS(instance);
						if (service != null) {
							list.add(service);
						}
					}
				}
			}
		} catch (Throwable t) {
			if (logger.isDebugEnabled()) {
				logger.debug(t.getMessage());
			}
		}
		return list.toArray(new IService[list.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByOwner(java.lang.String, java.lang.String, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public IService[] getByOwner(String owner, String type, IPageCond pageCond) {
		if (pageCond == null) {
			return getByOwner(owner, type);
		}

		List<IService> list = new ArrayList<IService>();
		try {
			PageCond pc = new PageCond();
			pc.setBegin(pageCond.getBegin());
			pc.setLength(pageCond.getLength());
			List<ServiceInstance> instances = getInstanceManager().getServiceInsts(
					owner, null, type, pc);
			if (instances != null && instances.size() > 0) {
				for (ServiceInstance instance : instances) {
					if (instance != null) {
						IService service = ObjectUtil.toPAAS(instance);
						if (service != null) {
							list.add(service);
						}
					}
				}
			}
			pageCond.setCount(pc.getCount());
		} catch (Throwable t) {
			if (logger.isDebugEnabled()) {
				logger.debug(t.getMessage());
			}
		}
		return list.toArray(new IService[list.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByCluster(java.lang.String)
	 */
	public IService[] getByCluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return new IService[0];
		}
		List<IService> list = new ArrayList<IService>();
		try {
			List<ServiceInstance> instances = getInstanceManager()
					.getServiceInstsByClusterName(clusterId);
			if (instances != null && instances.size() > 0) {
				for (ServiceInstance instance : instances) {
					if (instance != null) {
						IService service = ObjectUtil.toPAAS(instance);
						if (service != null) {
							list.add(service);
						}
					}
				}
			}
		} catch (Throwable t) {
			if (logger.isDebugEnabled()) {
				logger.debug(t.getMessage());
			}
		}
		return list.toArray(new IService[list.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#get(java.lang.String, java.lang.Class)
	 */
	public <T extends IService> T get(String id, Class<T> clazz) {
		if (StringUtil.isEmpty(id) || clazz == null) {
			return null;
		}
		return clazz.cast(get(id));
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByType(java.lang.String, java.lang.Class)
	 */
	public <T extends IService> List<T> getByType(String type, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (StringUtil.isEmpty(type) || clazz == null) {
			return list;
		}
		IService[] services = getByType(type);
		if (services != null && services.length > 0) {
			for (IService service : services) {
				if (service != null) {
					list.add(clazz.cast(service));
				}
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByType(java.lang.String, com.primeton.paas.manage.api.model.IPageCond, java.lang.Class)
	 */
	public <T extends IService> List<T> getByType(String type,
			IPageCond pageCond, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (StringUtil.isEmpty(type) || clazz == null) {
			return list;
		}
		IService[] services = getByType(type, pageCond);
		if (services != null && services.length > 0) {
			for (IService service : services) {
				if (service != null) {
					list.add(clazz.cast(service));
				}
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByOwner(java.lang.String, java.lang.String, java.lang.Class)
	 */
	public <T extends IService> List<T> getByOwner(String owner, String type,
			Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (StringUtil.isEmpty(type) || clazz == null) {
			return list;
		}
		IService[] services = getByOwner(owner, type);
		if (services != null && services.length > 0) {
			for (IService service : services) {
				if (service != null) {
					list.add(clazz.cast(service));
				}
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByOwner(java.lang.String, java.lang.String, com.primeton.paas.manage.api.model.IPageCond, java.lang.Class)
	 */
	public <T extends IService> List<T> getByOwner(String owner, String type,
			IPageCond pageCond, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (StringUtil.isEmpty(type) || clazz == null) {
			return list;
		}
		IService[] services = getByOwner(owner, type, pageCond);
		if (services != null && services.length > 0) {
			for (IService service : services) {
				if (service != null) {
					list.add(clazz.cast(service));
				}
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByCluster(java.lang.String, java.lang.Class)
	 */
	public <T extends IService> List<T> getByCluster(String clusterId,
			Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (StringUtil.isEmpty(clusterId) || clazz == null) {
			return list;
		}
		IService[] services = getByCluster(clusterId);
		if (services != null && services.length > 0) {
			for (IService service : services) {
				if (service != null) {
					list.add(clazz.cast(service));
				}
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByType(java.lang.String, java.lang.String)
	 */
	public IService[] getByType(String type, String ip) {
		if (StringUtil.isEmpty(ip) || StringUtil.isEmpty(type)) {
			return new IService[0];
		}
		List<IService> list = new ArrayList<IService>();
		try {
			List<ServiceInstance> instances = getInstanceManager()
					.getServiceInstsByIpServiceDefName(ip, type);
			if (instances != null && instances.size() > 0) {
				for (ServiceInstance instance : instances) {
					if (instance != null) {
						list.add(ObjectUtil.toPAAS(instance));
					}
				}
			}
		} catch (Throwable t) {
			if (logger.isErrorEnabled()) {
				logger.error(t);
			}
		}
		return list.toArray(new IService[list.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByType(java.lang.String, java.lang.String, java.lang.Class)
	 */
	public <T extends IService> List<T> getByType(String type, String ip,
			Class<T> clazz) {
		IService[] services = getByType(type, ip);
		List<T> list = new ArrayList<T>();
		for (IService service : services) {
			if (service != null) {
				list.add(clazz.cast(service));
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByIp(java.lang.String)
	 */
	public IService[] getByIp(String ip) {
		if (StringUtil.isEmpty(ip)) {
			return new IService[0];
		}
		List<IService> list = new ArrayList<IService>();
		try {
			List<ServiceInstance> instances = getInstanceManager()
					.getServiceInstsByIp(ip);
			if (instances != null && instances.size() > 0) {
				for (ServiceInstance instance : instances) {
					if (instance != null) {
						list.add(ObjectUtil.toPAAS(instance));
					}
				}
			}
		} catch (ServiceInstanceManagerException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		return list.toArray(new IService[list.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByIp(java.lang.String, java.lang.String)
	 */
	public IService[] getByIp(String ip, String type) {
		if (StringUtil.isEmpty(ip) || StringUtil.isEmpty(type)) {
			return new IService[0];
		}
		List<IService> list = new ArrayList<IService>();
		IService[] services = getByIp(ip);
		if (services != null && services.length > 0) {
			for (IService service : services) {
				if (service != null && type.equals(service.getType())) {
					list.add(service);
				}
			}
		}
		return list.toArray(new IService[list.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByIp(java.lang.String, int)
	 */
	public IService getByIp(String ip, int port) {
		if(StringUtil.isEmpty(ip) || port < 1) {
			return null;
		}
		try {
			return ObjectUtil.toPAAS(getInstanceManager().getServiceInstByIpPort(ip, port));
		} catch (ServiceInstanceManagerException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getByIp(java.lang.String, java.lang.String, java.lang.Class)
	 */
	public <T extends IService> List<T> getByIp(String ip, String type,
			Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (StringUtil.isEmpty(ip) || StringUtil.isEmpty(type) || clazz == null) {
			return list;
		}
		IService[] services = getByIp(ip, type);
		if (services != null && services.length > 0) {
			for (IService service : services) {
				if (service != null) {
					list.add(clazz.cast(service));
				}
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getChildren(java.lang.String)
	 */
	public IService[] getChildren(String id) {
		if (StringUtil.isEmpty(id)) {
			return new IService[0];
		}
		List<IService> list = new ArrayList<IService>();
		try {
			List<ServiceInstance> instances = getInstanceManager()
					.getChildServiceInsts(Long.parseLong(id));
			if (instances != null && instances.size() > 0) {
				for (ServiceInstance instance : instances) {
					if (instance != null) {
						list.add(ObjectUtil.toPAAS(instance));
					}
				}
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return list.toArray(new IService[list.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getPhysicalTypes()
	 */
	public String[] getPhysicalTypes() {
		List<String> types = new ArrayList<String>();
		ServiceLoader<IService> loader = ServiceLoader.load(IService.class);
		if (loader != null) {
			for (IService obj : loader) {
				if (obj != null && !StringUtil.isEmpty(obj.getType())
						&& IService.MODE_PHYSICAL.equals(obj.getMode())) {
					types.add(obj.getType());
				}
			}
		}
		return types.toArray(new String[types.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IServiceQuery#getLogicalTypes()
	 */
	public String[] getLogicalTypes() {
		List<String> types = new ArrayList<String>();
		ServiceLoader<IService> loader = ServiceLoader.load(IService.class);
		if (loader != null) {
			for (IService obj : loader) {
				if (obj != null && !StringUtil.isEmpty(obj.getType())
						&& IService.MODE_LOGIC.equals(obj.getMode())) {
					types.add(obj.getType());
				}
			}
		}
		return types.toArray(new String[types.size()]);
	}

	/**
	 * 
	 * @return
	 */
	protected static ServiceInstanceManager getInstanceManager() {
		return instanceManager = (null == instanceManager) ? 
				CesiumFactory.getServiceInstanceManager() : instanceManager;
	}

}
