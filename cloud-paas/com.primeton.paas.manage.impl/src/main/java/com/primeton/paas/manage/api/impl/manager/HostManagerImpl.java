/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.common.api.model.PageCond;
import org.gocom.cloud.cesium.manage.resource.api.ResourceManager;
import org.gocom.cloud.cesium.manage.resource.api.factory.ResourceManagerFactory;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.util.ObjectUtil;
import com.primeton.paas.manage.api.manager.IHostManager;
import com.primeton.paas.manage.api.manager.IServiceManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 主机管理器实现. <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class HostManagerImpl implements IHostManager {
	
	private static ResourceManager resourceManager = ResourceManagerFactory.createReourceManager();
	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();
	
	private static ILogger logger = ManageLoggerFactory.getLogger(HostManagerImpl.class);
	
	public HostManagerImpl() {
		super();
	}


	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IHostManager#add(com.primeton.paas.manage.api.model.Host)
	 */
	public void add(Host host) throws HostException {
		if (host == null || StringUtil.isEmpty(host.getIp())) {
			return;
		}
		if (get(host.getIp()) != null) {
			throw new HostException("Host [" + host.getIp() + "] already exists, can not add again.");
		}
		try {
			getResourceManager().saveHost(ObjectUtil.toCesium(host));
		} catch (Throwable t) {
			throw new HostException(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IHostManager#get(java.lang.String)
	 */
	public Host get(String ip) {
		if (StringUtil.isEmpty(ip)) {
			return null;
		}
		try {
			return ObjectUtil.toPAAS(getResourceManager().getHostByIp(ip));
		} catch (Throwable t) {
			logger.error(t.getMessage());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IHostManager#getByType(java.lang.String)
	 */
	public Host[] getByType(String type) {
		if (StringUtil.isEmpty(type)) {
			return new Host[0];
		}
		List<Host> list = new ArrayList<Host>();
		try {
			List<org.gocom.cloud.cesium.model.api.Host> hosts = resourceManager
					.getHostsByServiceDefName(type);
			if (hosts != null && hosts.size() > 0) {
				for (org.gocom.cloud.cesium.model.api.Host o : hosts) {
					if (o != null) {
						list.add(ObjectUtil.toPAAS(o));
					}
				}
			}
		} catch (Throwable t) {
			if (logger.isErrorEnabled()) {
				logger.error(t.getMessage());
			}
		}
		return list.toArray(new Host[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IHostManager#getByType(java.lang.String, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public Host[] getByType(String type, IPageCond pageCond) {
		Host[] hosts = getByType(type);
		if (pageCond == null) {
			return hosts;
		}
		if (hosts == null || hosts.length == 0) {
			return new Host[0];
		}
		List<Host> list = new ArrayList<Host>();
		pageCond.setCount(hosts.length);
		if (pageCond.getBegin() >= pageCond.getCount()) {
			pageCond.setBegin(0);
		}
		long end = pageCond.getBegin() + pageCond.getLength() - 1;
		if (hosts.length < end) {
			end = hosts.length - 1;
		}
		for (int i = pageCond.getBegin(); i <= end; i++) {
			list.add(hosts[i]);
		}
		return list.toArray(new Host[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IHostManager#getAll()
	 */
	public Host[] getAll() {
		List<Host> list = new ArrayList<Host>();
		try {
			List<org.gocom.cloud.cesium.model.api.Host> hosts = resourceManager
					.getAllHosts(null);
			if (hosts != null) {
				for (org.gocom.cloud.cesium.model.api.Host host : hosts) {
					if (host != null) {
						list.add(ObjectUtil.toPAAS(host));
					}
				}
			}
		} catch (Throwable t) {
			logger.error(t.getMessage());
		}
		return list.toArray(new Host[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IHostManager#getAll(com.primeton.paas.manage.api.model.IPageCond)
	 */
	public Host[] getAll(IPageCond pageCond) {
		if (pageCond == null) {
			return getAll();
		}
		List<Host> list = new ArrayList<Host>();
		try {
			PageCond pc = new PageCond();
			pc.setBegin(pageCond.getBegin());
			pc.setLength(pageCond.getLength());
			List<org.gocom.cloud.cesium.model.api.Host> hosts = resourceManager
					.getAllHosts(pc);
			if (hosts != null && hosts.size() > 0) {
				for (org.gocom.cloud.cesium.model.api.Host e : hosts) {
					if (e != null) {
						list.add(ObjectUtil.toPAAS(e));
					}
				}
				pageCond.setCount(pc.getCount());
			} else {
				pageCond.setCount(0);
			}
		} catch (Throwable t) {
			logger.error(t.getMessage());
		}
		return list.toArray(new Host[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IHostManager#update(com.primeton.paas.manage.api.model.Host)
	 */
	public void update(Host host) throws HostException {
		if (host == null || StringUtil.isEmpty(host.getIp())) {
			return;
		}
		Host h = get(host.getIp());
		if (h == null) {
			throw new HostException("Host [" + host.getIp() + "] not exists, can not update.");
		}
		try {
			getResourceManager().saveHost(ObjectUtil.toCesium(host));
		} catch (Throwable t) {
			throw new HostException(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostManager#delete(java.lang.String)
	 */
	public void delete(String ip) throws HostException {
		if (StringUtil.isEmpty(ip)) {
			return;
		}
		Host host = get(ip);
		if (host == null) {
			return;
		}
		try {
			IService[] services = getServiceQuery().getByIp(ip);
			if (services.length > 0) {
				throw new HostException("Can not destroy host, exists service on host [" + ip + "]");
			}
			int[] ports = getResourceManager().getUsedPorts(ip, null);
			if (ports != null && ports.length > 0) {
				getResourceManager().releaseUsedPorts(ip, ports);
			}
			getResourceManager().deleteHost(ip);
		} catch (Throwable t) {
			if (logger.isErrorEnabled()) {
				logger.error(t.getMessage());
			}
			throw new HostException(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostManager#getByType(java.lang.String, java.lang.String)
	 */
	public Host[] getByType(String type, String packageId) {
		if (StringUtil.isEmpty(type) || StringUtil.isEmpty(packageId)) {
			return new Host[0];
		}
		Host[] hosts = getByType(type);
		if (hosts == null || hosts.length == 0) {
			return new Host[0];
		}
		List<Host> list = new ArrayList<Host>();
		for (Host host : hosts) {
			if (packageId.equals(host.getPackageId())) {
				list.add(host);
			}
		}
		return list.toArray(new Host[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostManager#apply(java.lang.String, int)
	 */
	public synchronized List<Host> apply(String id, int number)
			throws HostException {
		List<Host> list = new ArrayList<Host>();
		if (StringUtil.isEmpty(id) || number <= 0) {
			return list;
		}
		Host[] hosts = getByPackage(id);
		if (hosts != null && hosts.length > 0) {
			for (Host host : hosts) {
				if (!host.isAssigned() && host.isControlable()) {
					list.add(host);
					if (list.size() == number) {
						break;
					}
				}
			}
			if (list.size() == number) {
				for (Host host : list) {
					host.setAssigned(true);
					update(host);
				}
				return list;
			}
		}
		throw new HostException("There is not enough host for package [" + id + "] number [" + number + "].");
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostManager#release(java.lang.String[])
	 */
	public void release(String[] ips) throws HostException {
		if (ips == null || ips.length == 0) {
			return;
		}
		for (String ip : ips) {
			release(ip);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostManager#release(java.lang.String)
	 */
	public void release(String ip) throws HostException {
		if (StringUtil.isEmpty(ip)) {
			return;
		}
		Host host = get(ip);
		if (host != null) {
			IService[] services = getServiceQuery().getByIp(ip);
			if (services.length > 0) {
				throw new HostException("Can not destroy or release, exists service on host [" + ip + "]");
			}
			for (String s : host.getTypes()) {
				IServiceManager manager = ServiceManagerFactory.getManager(s);
				try {
					manager.uninstall(ip, -1);
				} catch (ServiceException e) {
					logger.error(e.getMessage());
				}
			}
			try {
				int[] ports = getResourceManager().getUsedPorts(ip, null);
				if (ports != null && ports.length > 0) {
					getResourceManager().releaseUsedPorts(ip, ports);
				}
			} catch (Throwable t) {
				if (logger.isErrorEnabled()) {
					logger.error(t.getMessage());
				}
			}
			host.setAssigned(false);
			host.setTypes(null);
			host.setStandalone(false);
			update(host);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostManager#getByPackage(java.lang.String)
	 */
	public Host[] getByPackage(String packageId) {
		if (StringUtil.isEmpty(packageId)) {
			return null;
		}
		Host[] hosts = this.getAll();
		List<Host> hostsList = new ArrayList<Host>();
		for (Host host : hosts) {
			if (packageId.equals(host.getPackageId())) {
				hostsList.add(host);
			}
		}
		return hostsList.toArray(new Host[hostsList.size()]);
	}

	/**
	 * 
	 * @return
	 */
	protected static ResourceManager getResourceManager() {
		return resourceManager = (null == resourceManager) 
				? ResourceManagerFactory.createReourceManager() : resourceManager;
	}

	/**
	 * 
	 * @return
	 */
	protected static IServiceQuery getServiceQuery() {
		return serviceQuery = (null == serviceQuery) 
				? ServiceManagerFactory.getServiceQuery() : serviceQuery;
	}
	
}
