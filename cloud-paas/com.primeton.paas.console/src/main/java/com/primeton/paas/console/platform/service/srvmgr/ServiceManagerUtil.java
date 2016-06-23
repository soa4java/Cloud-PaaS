/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IHaProxyServiceManager;
import com.primeton.paas.manage.api.manager.IKeepalivedServiceManager;
import com.primeton.paas.manage.api.manager.INginxServiceManager;
import com.primeton.paas.manage.api.manager.IServiceManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.HaProxyService;
import com.primeton.paas.manage.api.service.KeepalivedService;
import com.primeton.paas.manage.api.service.NginxService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 服务、集群、主机等控制的方法 供异步方法调用
 * 
 * @author liyanping(liyp@primeton.com)
 * 
 */
public class ServiceManagerUtil {

	private static ILogger logger = LoggerFactory.getLogger(ServiceManagerUtil.class);

	private static IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();

	private static IHaProxyServiceManager haproxySrvManager = ServiceManagerFactory
			.getManager(HaProxyService.TYPE);
	private static INginxServiceManager nginxSrvManager = ServiceManagerFactory
			.getManager(NginxService.TYPE);
	private static IKeepalivedServiceManager keepalivedManager = ServiceManagerFactory
			.getManager(KeepalivedService.TYPE);

	/**
	 * @param serviceId
	 * @param type
	 * @return
	 */

	public static boolean startService(String serviceId, String type) {
		if (NginxService.TYPE.equals(type)) {
			return startNginxService(serviceId);
		}
		if (StringUtil.isEmpty(type)) {
			IService service = serviceQuery.get(serviceId);
			if (null == service) {
				return false;
			}
			type = service.getType();
		}
		IServiceManager manager = ServiceManagerFactory.getManager(type);
		manager = null == manager ? ServiceManagerFactory.getManager() : manager;
		try {
			manager.start(serviceId);
		} catch (ServiceException e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * @param serviceId
	 * @param type
	 * @return
	 */
	public static boolean stopService(String serviceId, String type) {
		if (StringUtil.isEmpty(serviceId)) {
			return false;
		}
		if (StringUtil.isEmpty(type)) {
			IService service = serviceQuery.get(serviceId);
			if (null == service) {
				return false;
			}
			type = service.getType();
		}
		IServiceManager manager = ServiceManagerFactory.getManager(type);
		manager = null == manager ? ServiceManagerFactory.getManager() : manager;
		try {
			manager.stop(serviceId);
		} catch (ServiceException e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param serviceId
	 * @param type
	 * @return
	 */

	public static boolean removeService(String serviceId, String type) {
		if (StringUtil.isEmpty(serviceId)) {
			return false;
		}
		if (StringUtil.isEmpty(type)) {
			IService service = serviceQuery.get(serviceId);
			if (null == service) {
				return false;
			}
			type = service.getType();
		}
		IServiceManager manager = ServiceManagerFactory.getManager(type);
		manager = null == manager ? ServiceManagerFactory.getManager() : manager;
		try {
			manager.destroy(serviceId);
		} catch (ServiceException e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * @param serviceId
	 * @param type
	 * @return
	 */
	public static boolean restartService(String serviceId, String type) {
		if (StringUtil.isEmpty(serviceId)) {
			return false;
		}
		if (StringUtil.isEmpty(type)) {
			IService service = serviceQuery.get(serviceId);
			if (null == service) {
				return false;
			}
			type = service.getType();
		}
		IServiceManager manager = ServiceManagerFactory.getManager(type);
		manager = null == manager ? ServiceManagerFactory.getManager() : manager;
		try {
			manager.restart(serviceId);
		} catch (ServiceException e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 查询关联keepalived实例
	 * 
	 * @param serviceId
	 * @return
	 */
	public static List<String> getRelKeepalivedInst(String serviceId) {
		IService[] insts = serviceQuery.getByType(KeepalivedService.TYPE);
		List<String> retList = new ArrayList<String>();
		if (insts != null) {
			for (IService inst : insts) {
				if (inst != null && serviceId.equals(inst.getParentId())) {
					retList.add(inst.getId());
				}
			}
		}
		return retList;
	}

	/**
	 * @param serviceId
	 */
	public static boolean startHaproxyService(String serviceId) {
		try {
			haproxySrvManager.start(serviceId);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		// 查询关联的Keepalived服务实例：keepalived实例的pId为当前serviceId
		List<String> idList = getRelKeepalivedInst(serviceId);
		if (idList != null && !idList.isEmpty()) {
			try {
				for (String id : idList) {
					keepalivedManager.start(id);
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return true;
	}

	/**
	 * 
	 * @param serviceId
	 */
	public static boolean startNginxService(String serviceId) {
		try {
			nginxSrvManager.start(serviceId);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		// 查询关联的Keepalived服务实例：keepalived实例的pId为当前serviceId
		List<String> idList = getRelKeepalivedInst(serviceId);
		if (idList != null && !idList.isEmpty()) {
			try {
				for (String id : idList) {
					keepalivedManager.start(id);
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return true;
	}

}
