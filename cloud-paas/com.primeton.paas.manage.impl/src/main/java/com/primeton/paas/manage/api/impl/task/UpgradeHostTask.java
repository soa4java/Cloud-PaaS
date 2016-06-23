/**
 * 
 */
package com.primeton.paas.manage.api.impl.task;

import java.util.concurrent.TimeoutException;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.exception.TaskException;
import com.primeton.paas.manage.api.factory.HostManagerFactory;
import com.primeton.paas.manage.api.factory.HostTemplateManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.manager.Executable;
import com.primeton.paas.manage.api.manager.IHostManager;
import com.primeton.paas.manage.api.manager.IHostTemplateManager;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.HostTemplate;
import com.primeton.paas.manage.spi.exception.VmException;
import com.primeton.paas.manage.spi.factory.VmManagerFactory;
import com.primeton.paas.manage.spi.resource.IVmManager;

/**
 *
 * @author liming(li-ming@primeton.com)
 */
public class UpgradeHostTask implements Executable {

	public static final String TYPE = "10";

	private static ILogger logger = ManageLoggerFactory
			.getLogger(UpgradeHostTask.class);

	private static IVmManager vmManager = VmManagerFactory.getManager();
	private static IHostManager hostManager = HostManagerFactory
			.getHostManager();
	private static IHostTemplateManager hostTemplateManager = HostTemplateManagerFactory
			.getManager();

	private Host host;

	private String packageId;

	private long timeout;

	/**
	 * 
	 * @param host
	 * @param packageId
	 * @param timeout
	 */
	public UpgradeHostTask(Host host, String packageId, long timeout) {
		this.host = host;
		this.packageId = packageId;
		this.timeout = timeout;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.Executable#clear()
	 */
	public void clear() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.Executable#execute()
	 */
	public String execute() throws TaskException {
		HostTemplate hostPackage = hostTemplateManager.getTemplate(packageId);
		boolean success = this.upgradeHost(host.getId(), hostPackage.getProfileId(), timeout);
		if (success) {
			try {
				host.setPackageId(packageId);
				hostManager.update(host);
				return "Upgrade host [" + host.getIp() + "] success.";
			} catch (HostException e) {
				logger.error("Upgrade Host [" + host.getIp()
						+ "] error message : " + e.getMessage() + ".");
				throw new TaskException("Upgrade Host [" + host.getIp()
						+ "] error : " + e.getMessage() + ".");
			}
		}
		return "Upgrade host [" + host.getIp() + "] failure.";
	}

	/**
	 * 
	 * @param vmID
	 * @param profileID
	 * @param timeout
	 * @return
	 * @throws TaskException
	 */
	public boolean upgradeHost(String vmID, String profileID, long timeout)
			throws TaskException {
		if (vmID == null || profileID == null || timeout <= 0) {
			return false;
		}
		try {
			vmManager.upgradeVM(vmID, profileID, timeout);
		} catch (VmException e) {
			logger.error("Upgrade Host [" + host.getIp() + "] error : "
					+ e.getMessage() + ".");
			throw new TaskException("Upgrade Host [" + host.getIp()
					+ "] error : " + e.getMessage() + ".");
		} catch (TimeoutException e) {
			logger.error("Upgrade Host [" + host.getIp() + "] error : "
					+ e.getMessage() + ".");
			throw new TaskException("Upgrade Host [" + host.getIp()
					+ "] error : " + e.getMessage() + ".");
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.primeton.paas.manage.api.manager.Executable#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.Executable#init()
	 */
	public void init() {
	}

}
