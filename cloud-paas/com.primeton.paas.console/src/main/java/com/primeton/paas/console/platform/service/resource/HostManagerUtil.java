/**
 *
 */
package com.primeton.paas.console.platform.service.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.platform.controller.Disk;
import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.exception.StorageException;
import com.primeton.paas.manage.api.factory.HostAssignManagerFactory;
import com.primeton.paas.manage.api.factory.HostManagerFactory;
import com.primeton.paas.manage.api.factory.HostTemplateManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.factory.TaskManagerFactory;
import com.primeton.paas.manage.api.impl.task.DestroyHostTask;
import com.primeton.paas.manage.api.impl.task.SrvInstallTask;
import com.primeton.paas.manage.api.impl.task.UpgradeHostTask;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IHostAssignManager;
import com.primeton.paas.manage.api.manager.IHostManager;
import com.primeton.paas.manage.api.manager.IHostTemplateManager;
import com.primeton.paas.manage.api.manager.IServiceManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.ITaskManager;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.HostTemplate;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.Task;
import com.primeton.paas.manage.api.monitor.IMonitorDataManager;
import com.primeton.paas.manage.api.monitor.MetaData;
import com.primeton.paas.manage.api.monitor.MonitorDataManagerFactory;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.factory.StorageManagerFactory;
import com.primeton.paas.manage.spi.model.Storage;
import com.primeton.paas.manage.spi.resource.IStorageManager;

/**
 * 主机相关管理类（主机的查询、销毁、启动、停止）
 * 
 * @author liming(li-ming@primeton.com)
 */
public class HostManagerUtil {

	private static ILogger logger = LoggerFactory.getLogger(HostManagerUtil.class);

	private static IHostManager hostManager = HostManagerFactory
			.getHostManager();

	private static IServiceQuery srvQueryMgr = ServiceManagerFactory
			.getServiceQuery();

	private static ITaskManager taskManager = TaskManagerFactory.getManager();

	private static IHostAssignManager assignManager = HostAssignManagerFactory
			.getManager();
	
	private static IStorageManager storageManager = StorageManagerFactory
			.getManager();
	
	private static IHostTemplateManager hostTemplateManager = HostTemplateManagerFactory
			.getManager();

	// (1C/2G/0G)
	private static IMonitorDataManager monitorDataManager = MonitorDataManagerFactory
			.getManager();
	
	/**
	 * templateId - HostTemplate
	 * 
	 * @return
	 */
	public static Map<String, HostTemplate> getHostTemplatesMap() {
		List<HostTemplate> templates = hostTemplateManager.getTemplates(null);
		Map<String, HostTemplate> elements = new HashMap<String, HostTemplate>();
		if (null != templates) {
			for (HostTemplate template : templates) {
				elements.put(template.getTemplateId(), template);
			}
		}
		return elements;
	}

	/**
	 * 主机查询
	 * 
	 * @param type
	 * @param ip
	 * @param page
	 * @return
	 */
	public static Host[] queryHosts(String type, String ip, PageCond page) {
		List<Host> hosts = new ArrayList<Host>();
		Host[] allHosts = hostManager.getAll();
		for (Host host : allHosts) {
			if (StringUtil.contain(host.getIp(), ip)) {
				if (StringUtil.isEmpty(type)) {
					hosts.add(host);
				} else {
					List<String> types = host.getTypes();
					for (String t : types) {
						if (type.equals(t)) {
							hosts.add(host);
						}
					}
				}
			}
		}

		Map<String, HostTemplate> templates = getHostTemplatesMap();
		
		// 已经分配主机过滤,增加机型，套餐属性
		List<Host> hostList = new ArrayList<Host>();
		for (Host host : hosts) {
			if (host.isAssigned()) {
				Map<String, String> exts = host.getExts();
				exts.put("packageName", template2String(templates.get(host.getPackageId()))); //$NON-NLS-1$
				hostList.add(host);
			}
		}

		page.setCount(hostList.size());

		int end = page.getBegin() + page.getLength() - 1;
		if (page.getBegin() + page.getLength() > page.getCount()) {
			end = page.getCount() - 1;
		}
		List<Host> retList = new ArrayList<Host>();
		for (int i = page.getBegin(); i <= end; i++) {
			retList.add(hostList.get(i));
		}

		return retList.toArray(new Host[retList.size()]);
	}
	
	/**
	 * 
	 * @param template
	 * @return
	 */
	public static String template2String(HostTemplate template) {
		if (null == template) {
			return "";
		}
		if (template.getTemplateName() != null
				&& (template.getTemplateName().contains("C") || template
						.getTemplateName().contains("c"))
				&& (template.getTemplateName().contains("G") || template
						.getTemplateName().contains("g"))) {
			return template.getTemplateName();
		}
		return template.getTemplateName() + "(" + template.getCpu() + "/"
				+ template.getMemory() + template.getUnit() + ")";
	}

	/**
	 * 添加一台主机
	 * 
	 * @param host
	 * @return
	 * @throws HostException
	 */
	public static void addHost(Host host) throws HostException {
		if (null == host || StringUtil.isEmpty(host.getIp())) {
			return;
		}
		hostManager.add(host);
	}

	/**
	 * 申请一台主机
	 * 
	 * @param host
	 * @return
	 * @throws HostException
	 */
	public static void applyHost(String hostPackageId, String[] types)
			throws HostException {
		String owner = DataContextManager.current().getMUODataContext()
				.getUserObject().getUserId();

		List<Host> hostList = hostManager.apply(hostPackageId, 1);

		if (null != hostList && null != hostList.get(0)) {
			Host host = hostList.get(0);
			if (null != types) {
				for (String type : types) {
					String taskId = taskManager.add(
							new SrvInstallTask(host.getId(), type), new Long(
									assignManager.getInstallTimeout(type)),
							owner);

					Task task = taskManager.get(taskId);
					if (task != null) {
						task.setHandleResult("Service install task. {hostPackageId:"
								+ hostPackageId + ",type:" + type + "}.");
						taskManager.update(task);
					}
				}
			}

		}
	}

	/**
	 * 检测主机池是否有多余主机
	 * 
	 * @param host
	 * @return
	 * @throws HostException
	 */
	public static boolean isExistHostByPackageId(String hostPackageId) {
		if (StringUtil.isEmpty(hostPackageId)) {
			return false;
		}
		try {
			hostManager.apply(hostPackageId, 1);
		} catch (HostException e) {
			return false;
		}
		return true;
	}

	/**
	 * 获取申请主机的IP
	 * 
	 * @param host
	 * @return
	 * @throws HostException
	 */
	public static String getApplyIpByPackageId(String hostPackageId) {
		if (StringUtil.isEmpty(hostPackageId)) {
			return null;
		}
		try {
			List<Host> hostList = hostManager.apply(hostPackageId, 1);
			Host host = hostList.get(0);
			if (null != host) {
				return host.getIp();
			}
		} catch (HostException e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 申请主机
	 * 
	 * @param host
	 * @return
	 * @throws HostException
	 */
	public static boolean applyHosts(Host[] hosts) {
		if (hosts == null || hosts.length < 1) {
			return false;
		}
		for (Host h : hosts) {
			try {
				Host host = hostManager.get(h.getIp());
				host.setAssigned(true);
				hostManager.update(host);
			} catch (HostException e) {
				logger.error(e);
				return false;
			}
		}
		return true;
	}

	/**
	 * 申请主机
	 * 
	 * @param host
	 * @return
	 * @throws HostException
	 */
	public static boolean applyHosts(String[] ips) {
		if (ips == null || ips.equals("")) {
			return false;
		}
		for (String ip : ips) {
			try {
				Host host = hostManager.get(ip);
				host.setAssigned(true);
				hostManager.update(host);
			} catch (HostException e) {
				logger.error(e);
				return false;
			}
		}
		return true;
	}

	/**
	 * 申请主机服务
	 * 
	 * @param host
	 * @return
	 * @throws HostException
	 */
	public static String applyHostService(String ip, String[] types)
			throws HostException {
		String owner = DataContextManager.current().getMUODataContext()
				.getUserObject().getUserId();

		Host host = hostManager.get(ip);
		String taskIds = "";
		if (null != host && null != types) {
			int i = 0;
			for (String type : types) {
				String taskId = taskManager.add(
						new SrvInstallTask(ip, type), new Long(
								assignManager.getInstallTimeout(type)),
								owner);
				
				Task task = taskManager.get(taskId);
				if (task != null) {
					task.setHandleResult("Service install task. {ip:" + ip
							+ ",type:" + type + "}.");
					taskManager.update(task);
					
					taskIds += taskId;
					if (i < types.length - 1) {
						taskIds += ",";
					}
					i++;
				}
			}
		}
		return taskIds;
	}

	/**
	 * 卸载主机服务
	 * 
	 * @param host
	 * @return
	 * @throws HostException
	 */
	public static void uninstallService(String ip, String[] types)
			throws HostException {
		Host host = hostManager.get(ip);
		if (null != host) {
			if (null != types) {
				for (String type : types) {
					// 检测主机上是否存在服务实例
					IService[] services = srvQueryMgr.getByIp(ip, type);
					if (services.length > 0) {
						throw new HostException("Can not destroy or release, exists service on host[" + ip + "]");
					}

					// 卸载主机上已安装服务
					IServiceManager manager = ServiceManagerFactory.getManager(type);
					// if not found use default
					manager = null == manager ? ServiceManagerFactory.getManager() : manager;
					try {
						manager.uninstall(ip, -1);
					} catch (ServiceException e) {
						throw new HostException(e);
					}
				}

				host = hostManager.get(ip);
				if (StringUtil.isEmpty(host.getType()) && host.isStandalone()) {
					host.setStandalone(false);
					hostManager.update(host);
				}

			}
		}
	}

	/**
	 * 删除指定IP的主机<br>
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean delHost(String ip) {
		if (StringUtil.isEmpty(ip)) {
			return false;
		}
		Host host = hostManager.get(ip);
		if (null != host) {
			try {
				delHosts(new Host[] { host });
			} catch (HostException e) {
				logger.error("Delete host {0} error.", new Object[] { ip }, e);
				return false;
			} catch (StorageException e) {
				logger.error("Host {0} unmount storage error.", new Object[] { ip }, e);
				return false;
			}
		}
		return true;
	}

	/**
	 * 删除指定IP（多个ip用','号隔开）的主机<br>
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean delHosts(String ips) {
		if (StringUtil.isNotEmpty(ips)) {
			List<Host> hostList = new ArrayList<Host>();
			for (String ip : ips.split(",")) {
				Host host = hostManager.get(ip);
				if (null != host) {
					hostList.add(host);
				}
			}
			try {
				delHosts(hostList.toArray(new Host[hostList.size()]));
			} catch (HostException e) {
				logger.error("Delete hosts {0} error.", new Object[] { ips }, e);
				return false;
			} catch (StorageException e) {
				logger.error("Host {0} unmount storage error.", new Object[] { ips }, e);
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * 释放指定IP（多个ip用','号隔开）的主机<br>
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean releaseHosts(String ips) {
		try {
			if (null != ips && !"".equals(ips)) {
				// 释放host
				hostManager.release(ips.split(","));
				// 卸载存储
				for (String ip : ips.split(",")) {
					try {
						storageManager.unmount(ip);
						storageManager.removeWhiteListsByIp(ip);
						List<Storage> sslist = storageManager
								.getByHost(ip);
						for (Storage ss : sslist) {
							List<String> wl = storageManager
									.getWhiteLists(ss.getId());
							if (wl == null || wl.size() == 0) {
								storageManager.release(ss.getId());
							}
						}
					} catch (StorageException e) {
						logger.error("Host {0} unmount storage error.", new Object[] { ip }, e);
						return false;
					}
				}

				return true;
			}
		} catch (HostException e) {
			logger.error(e);
			return false;
		}
		return false;
	}

	/**
	 * 删除多台主机<br>
	 * 
	 * @param hosts
	 * @throws HostException
	 * @throws StorageException
	 */

	public static void delHosts(Host[] hosts) throws HostException,
			StorageException {
		if (hosts == null || hosts.length < 1)
			return;

		List<String> vmIDs = new ArrayList<String>();
		List<String> ips = new ArrayList<String>();
		for (int i = 0; i < hosts.length; i++) {
			Host host = hostManager.get(hosts[i].getIp());
			if (host == null) {
				continue;
			}
			String id = host.getId();
			String ip = host.getIp();
			if (StringUtil.isNotEmpty(id)) {
				vmIDs.add(id);
				ips.add(ip);
			}

			// 删除host
			hostManager.delete(ip);

			// 卸载存储
			storageManager.unmount(ip);
			storageManager.removeWhiteListsByIp(ip);
			List<Storage> sslist = storageManager.getByHost(ip);
			for (Storage ss : sslist) {
				List<String> wl = storageManager
						.getWhiteLists(ss.getId());
				if (wl == null || wl.size() == 0) {
					storageManager.release(ss.getId());
				}
			}
		}

		// 销毁机器
		if (vmIDs.size() > 0) {
			String[] vms = vmIDs.toArray(new String[vmIDs.size()]);
			String[] ipsTemp = ips.toArray(new String[ips.size()]);
			try {
				// 销毁主机
				String owner = DataContextManager.current().getMUODataContext()
						.getUserObject().getUserId();

				long recoverTimeout = SystemVariables.getVmDestroyTimeout()
						* vmIDs.size();
				String taskId = taskManager.add(new DestroyHostTask(vms,
						ipsTemp, recoverTimeout), recoverTimeout, owner);
				// Task task = taskManager.get(taskId);
				// taskManager.update(task);
				logger.info("Destroy hosts {0}, submit task {1}.", new Object[] { ips, taskId});
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	/**
	 * 配置升级主机<br>
	 * 
	 * @param hosts
	 * @throws HostException
	 * @throws StorageException
	 */

	public static void upgradeHost(String ip, String packageId) {
		if (ip == null)
			return;

		List<String> vmIDs = new ArrayList<String>();
		List<String> ips = new ArrayList<String>();
		Host host = hostManager.get(ip);
		String id = host.getId();
		if (StringUtil.isNotEmpty(id)) {
			vmIDs.add(id);
			ips.add(ip);
		}

		// 升级机器
		if (vmIDs.size() > 0) {
			// String[] vms = vmIDs.toArray(new String[vmIDs.size()]);
			// String[] ipsTemp = ips.toArray(new String[ips.size()]);
			try {
				String owner = DataContextManager.current().getMUODataContext()
						.getUserObject().getUserId();
				long recoverTimeout = SystemVariables.getVmDestroyTimeout()
						* vmIDs.size();
				String taskId = taskManager.add(new UpgradeHostTask(host,
						packageId, recoverTimeout), recoverTimeout, owner);
				logger.info("Upgrade host {0} to template {1}, submit a task {2}.", 
						new Object[] { ip, packageId, taskId});
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	/**
	 * 获取主机信息<br>
	 * 
	 * @param ip
	 *            主机IP
	 * @return
	 */
	public static Host getHost(String ip) {
		return (StringUtil.isEmpty(ip)) ? null : hostManager.get(ip);
	}

	/**
	 * 判断主机ip是否已经使用<br>
	 * 
	 * @param ip
	 *            主机IP
	 * @return
	 */
	public static boolean isExistHostIP(String ip) {
		return (StringUtil.isEmpty(ip)) ? false : null != hostManager.get(ip);
	}

	/**
	 * 获取全部服务类型<br>
	 * 
	 * @return
	 */

	public static String[] getServiceTypes() {
		return srvQueryMgr.getPhysicalTypes();
	}

	/**
	 * 获取可安装的服务类型<br>
	 * 
	 * @return
	 */

	public static String[] getAdvanceTypesByIP(String ip) {
		String[] types = getTypesByIp(ip);
		String[] advanceTypes = srvQueryMgr.getPhysicalTypes();
		List<String> list = new ArrayList<String>();
		for (String type : advanceTypes) {
			list.add(type);
		}
		for (String type : types) {
			list.remove(type);
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 获取全部服务类型<br>
	 * 
	 * @return
	 */

	public static IService[] getServices(String ip) {
		return srvQueryMgr.getByIp(ip);
	}

	/**
	 * 获取主机IP所安装服务<br>
	 * 
	 * @return
	 */
	public static String[] getTypesByIp(String ip) {
		List<String> typeList = hostManager.get(ip).getTypes();
		return typeList.toArray(new String[typeList.size()]);
	}

	/**
	 * 根据IP及类型 查询服务列表
	 * 
	 * @param ip
	 * @param type
	 * @return
	 */
	public static IService[] getServicesByIp(String ip, String type) {
		if (StringUtil.isEmpty(ip)) {
			return null;
		}
		return srvQueryMgr.getByIp(ip);
	}

	/**
	 * 根据IP 查询磁盘容量
	 * 
	 * @param ip
	 * @param type
	 * @return
	 */
	public static List<Disk> getDisksByIp(String ip) {
		List<Disk> diskList = new ArrayList<Disk>();
		MetaData metaData = monitorDataManager.getLatestDataByIp(ip);
		if (metaData != null && metaData.getMetaData() != null
				&& metaData.getMetaData().get(MetaData.STO_FILESYSTEM) != null) {
			String[] filesystems = ((String) metaData.getMetaData().get(
					MetaData.STO_FILESYSTEM)).split(";");
			if (filesystems.length > 0) {
				String[] sizes = ((String) metaData.getMetaData().get(
						MetaData.STO_SIZE)).split(";");
				String[] useds = ((String) metaData.getMetaData().get(
						MetaData.STO_USED)).split(";");
				String[] avails = ((String) metaData.getMetaData().get(
						MetaData.STO_AVAIL)).split(";");
				String[] uses = ((String) metaData.getMetaData().get(
						MetaData.STO_USE)).split(";");
				String[] mounteds = ((String) metaData.getMetaData().get(
						MetaData.STO_MOUNTED)).split(";");
				for (int i = 0; i < filesystems.length; i++) {
					Disk disk = new Disk();
					disk.setFilesystem(filesystems[i]);
					disk.setSize(sizes[i]);
					disk.setUsed(useds[i]);
					disk.setAvail(avails[i]);
					disk.setUse(uses[i]);
					disk.setMounted(mounteds[i]);
					diskList.add(disk);
				}
			}
		}
		return diskList;
	}

}
