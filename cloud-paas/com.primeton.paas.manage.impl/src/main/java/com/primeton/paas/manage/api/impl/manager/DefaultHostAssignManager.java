/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.HostManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IHostAssignManager;
import com.primeton.paas.manage.api.manager.IHostManager;
import com.primeton.paas.manage.api.manager.IServiceManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.KeepalivedService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultHostAssignManager implements IHostAssignManager {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultHostAssignManager.class);
	
	private static IHostManager hostManager = HostManagerFactory.getHostManager();
	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();
	
	/**
	 * Default. <br>
	 */
	public DefaultHostAssignManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostAssignManager#apply(java.lang.String, java.lang.String, boolean, int, long)
	 */
	public String[] apply(String packageId, String type, boolean isStandalone,
			int number, long timeout) throws HostException,
			ServiceException, TimeoutException {
		if (StringUtil.isEmpty(packageId)) {
			throw new ServiceException("Can not assign host, packageId is empty.");
		}
		if (StringUtil.isEmpty(type)) {
			throw new ServiceException("Can not assign host, type is empty.");
		}
		if (number < 1) {
			throw new ServiceException("Can not assign host, number is " + number + ".");
		}
		if (timeout <= 0) {
			timeout = 100000L;
		}
		if (isStandalone) {
			return applyStandalone(packageId, type, false, number, timeout);
		} else {
			return applyShared(packageId, type, number, timeout);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostAssignManager#apply(java.lang.String, java.lang.String, long)
	 */
	public String[] applyMS(String packageId, String type, long timeout)
			throws HostException, ServiceException, TimeoutException {
		return applyStandalone(packageId, type, true, 2, timeout);
	}

	/**
	 * 
	 * @param packageId
	 * @param type
	 * @param isMS
	 * @param number
	 * @param timeout
	 * @return
	 * @throws HostException
	 * @throws ServiceException
	 * @throws TimeoutException
	 */
	private synchronized String[] applyStandalone(String packageId, String type,
			boolean isMS, int number, long timeout) 
			throws HostException, ServiceException, TimeoutException {
		
		int size = isMS ? 2 : number;
		if (size < 1) {
			return new String[0];
		}
		List<Host> hosts = getHostManager().apply(packageId, size);
		List<String> ips = new ArrayList<String>();
		List<ServiceInstallTask> threads = new ArrayList<ServiceInstallTask>();
		for (Host host : hosts) {
			host.setStandalone(true);
			getHostManager().update(host);
			ips.add(host.getIp());
			ServiceInstallTask task = new ServiceInstallTask(host.getIp(), type, getInstallTimeout(type));
			threads.add(task);
			Thread thread = new Thread(task);
			thread.setName("Install_" + type + "_on_" + host.getIp()); //$NON-NLS-1$ //$NON-NLS-2$
			thread.setDaemon(true);
			thread.start();
			if (isMS) {
				ServiceInstallTask ktask = new ServiceInstallTask(host.getIp(), KeepalivedService.TYPE, getInstallTimeout(KeepalivedService.TYPE));
				threads.add(ktask);
				Thread kthread = new Thread(ktask);
				kthread.setName("Install_" + KeepalivedService.TYPE + "_on_" + host.getIp()); //$NON-NLS-1$ //$NON-NLS-2$
				kthread.setDaemon(true);
				kthread.start();
			}
		}
		boolean isFinish = false;
		boolean isOk = false;
		long begin = System.currentTimeMillis();
		long left = timeout;
		Throwable throwable = null;
		while (true) {
			isFinish = true;
			isOk = true;
			for (ServiceInstallTask t : threads) {
				if ((t.getThrowable() != null)) {
					isOk = false;
					throwable = t.getThrowable();
				}
				if (!t.isFinish()) {
					isFinish = false;
					break;
				}
			}
			if (isFinish) {
				break;
			}
			ThreadUtil.sleep(1000L);
			
			left = timeout - (System.currentTimeMillis() - begin);
			if (left < 0) {
				break;
			}
		}
		// 
		if (isFinish && isOk) {
			return ips.toArray(new String[ips.size()]);
		} else { // timeout
			// release host
			getHostManager().release(ips.toArray(new String[ips.size()]));
		}
		//
		if (isFinish && !isOk) {
			throw new ServiceException(throwable);
		} else {
			throw new TimeoutException();
		}
	}
	
	/**
	 * 
	 * @param packageId
	 * @param type
	 * @param isMasterSlave
	 * @param number
	 * @param timeout
	 * @return
	 * @throws HostException
	 * @throws ServiceException
	 * @throws TimeoutException
	 */
	private synchronized String[] applyShared(String packageId, String type, int number, long timeout) 
			throws HostException, ServiceException, TimeoutException {
		Host[] hosts = getHostManager().getByType(type, packageId);
		if (hosts == null) {
			hosts = new Host[0];
		}
		int limit = getMaxInstancesSize(packageId, type);
		List<Host> avaliableHosts = new ArrayList<Host>();
		Map<String, Integer> instanceSizeMap = new HashMap<String, Integer>();
		for (Host host : hosts) {
			if (host.isStandalone()) {
				continue;
			}
			IService[] instances = getServiceQuery().getByIp(host.getIp(), type);
			int size = instances == null ? 0 : instances.length;
			if (size < limit) {
				avaliableHosts.add(host);
				instanceSizeMap.put(host.getIp(), size);
			}
		}
		
		List<String> ips = new ArrayList<String>();
		if (avaliableHosts.size() < number) { 
			int size = number - avaliableHosts.size();
			List<Host> applyHosts = getHostManager().apply(packageId, size);
			
			List<ServiceInstallTask> threads = new ArrayList<ServiceInstallTask>();
			for (Host host : applyHosts) {
				ips.add(host.getIp());
				host.setStandalone(false);
				getHostManager().update(host);
				ServiceInstallTask t = new ServiceInstallTask(host.getIp(), type, getInstallTimeout(type));
				threads.add(t);
				Thread thread = new Thread(t);
				thread.setName("Install_" + type + "_on_" + host.getIp()); //$NON-NLS-1$ //$NON-NLS-2$
				thread.setDaemon(true);
				thread.start();
			}
			boolean isFinish = false;
			boolean isOk = false;
			long begin = System.currentTimeMillis();
			long left = timeout;
			Throwable throwable = null;
			while (true) {
				isFinish = true;
				isOk = true;
				for (ServiceInstallTask t : threads) {
					if ((t.getThrowable() != null)) {
						isOk = false;
						throwable = t.getThrowable();
					}
					if (!t.isFinish()) {
						isFinish = false;
						break;
					}
				}
				if (isFinish) {
					break;
				}
				ThreadUtil.sleep(1000L);
				
				left = timeout - (System.currentTimeMillis() - begin);
				if (left < 0) {
					break;
				}
			}
			// 
			if (isFinish && isOk) {
				for (Host host : avaliableHosts) {
					ips.add(host.getIp());
				}
				return ips.toArray(new String[ips.size()]);
			} else { // timeout
				// release host
				getHostManager().release(ips.toArray(new String[ips.size()]));
			}
			//
			if (isFinish && !isOk) {
				throw new ServiceException(throwable);
			} else {
				throw new TimeoutException();
			}
			
		} else {
			for (int i = 0; i < number; i++) {
				String host = null;
				int size = -1;
				for (String ip : instanceSizeMap.keySet()) {
					if (size == -1) {
						size = instanceSizeMap.get(ip);
						host = ip;
					} else if (size > instanceSizeMap.get(ip)) {
						size = instanceSizeMap.get(ip);
						host = ip;
					}
				}
				ips.add(host);
				instanceSizeMap.remove(host);
			}

			return ips.toArray(new String[ips.size()]);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostAssignManager#getMaxInstancesSize(java.lang.String, java.lang.String)
	 */
	public int getMaxInstancesSize(String packageId, String serviceType) {
		if (StringUtil.isEmpty(packageId) || StringUtil.isEmpty(serviceType)) {
			return 0;
		}
		// max_HaProxy_201311120232_inst
		return SystemVariables.getIntValue("max_" + serviceType + "_" + packageId + "_inst", 10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostAssignManager#getInstallTimeout(java.lang.String)
	 */
	public long getInstallTimeout(String serviceType) {
		if (StringUtil.isEmpty(serviceType)) {
			return 0;
		}
		return SystemVariables.getLongValue("install_" + serviceType + "_timeout", 900000L) ; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * 
	 * @author lizhongwen(mailto:lizw@primeton.com)
	 *
	 */
	private  class ServiceInstallTask implements Runnable {
		
		private String ip;
		private String type;
		private long timeout;
		private boolean isFinish;
		private Throwable throwable;
		
		/**
		 * @param ip
		 * @param type
		 * @param timeout
		 */
		public ServiceInstallTask(String ip, String type, long timeout) {
			super();
			this.ip = ip;
			this.type = type;
			this.timeout = timeout;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			if (StringUtil.isEmpty(ip) || StringUtil.isEmpty(type)) {
				return;
			}
			IServiceManager manager = ServiceManagerFactory.getManager(type);
			try {
				long begin = System.currentTimeMillis();
				logger.info("Begin install service " + type + " on host " + ip + ".");
				manager.install(ip, timeout);
				isFinish = true;
				long end = System.currentTimeMillis();
				logger.info("End install service " + type + " on host " + ip + ". Time spents " + (end-begin)/1000L + " seconds.");
				
				Host host = getHostManager().get(ip);
				if (host != null) {
					host.addType(type);
					try {
						getHostManager().update(host);
					} catch (HostException e) {
						logger.error(e);
					}
				}
				
			} catch (ServiceException e) {
				this.throwable = e;
			}
		}

		/**
		 * @return the isFinish
		 */
		public boolean isFinish() {
			return isFinish;
		}

		/**
		 * @return the throwable
		 */
		public Throwable getThrowable() {
			return throwable;
		}

	}

	/**
	 * 
	 * @return
	 */
	protected static IHostManager getHostManager() {
		return hostManager = (null == hostManager) ? HostManagerFactory.getHostManager() : hostManager;
	}

	/**
	 * 
	 * @return
	 */
	protected static IServiceQuery getServiceQuery() {
		return serviceQuery = (null == serviceQuery) ? ServiceManagerFactory.getServiceQuery() : serviceQuery;
	}
	
}
