/**
 * 
 */
package com.primeton.paas.manage.api.impl.task;

import java.util.concurrent.TimeoutException;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.TaskException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.manager.Executable;
import com.primeton.paas.manage.spi.exception.VmException;
import com.primeton.paas.manage.spi.factory.VmManagerFactory;
import com.primeton.paas.manage.spi.resource.IVmManager;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DestroyHostTask implements Executable {

	public static final String TYPE = "7";

	private static ILogger logger = ManageLoggerFactory
			.getLogger(DestroyHostTask.class);

	private static IVmManager vmManager = VmManagerFactory.getManager();

	private String[] hosts;

	private String[] ips;

	private long timeout;

	/**
	 * 
	 * @param hosts
	 * @param ips
	 * @param timeout
	 */
	public DestroyHostTask(String[] hosts, String[] ips, long timeout) {
		this.hosts = hosts;
		this.timeout = timeout;
		this.ips = ips;
	}

	public void clear() {

	}

	public String execute() throws TaskException {
		boolean bn = this.destroyHost(hosts, timeout);
		if (bn)
			return "Destroy host[" + handlePrint(ips) + "] success.";
		return "Destroy host[" + handlePrint(ips) + "] error.";
	}

	/**
	 * 
	 * @param hosts
	 * @param timeout
	 * @return
	 * @throws TaskException
	 */
	public boolean destroyHost(String[] hosts, long timeout)
			throws TaskException {
		if (hosts == null || timeout <= 0) {
			return false;
		}
		try {
			vmManager.destroy(hosts, timeout);
		} catch (VmException e) {
			logger.error("Destory Hosts [" + handlePrint(hosts) + "] error : "
					+ e.getMessage() + ".");
			throw new TaskException("Destory Hosts [" + handlePrint(hosts)
					+ "] error : " + e.getMessage() + ".");
		} catch (TimeoutException e) {
			logger.error("Destory Hosts [" + handlePrint(hosts) + "] error : "
					+ e.getMessage() + ".");
			throw new TaskException("Destory Hosts [" + handlePrint(hosts)
					+ "] error : " + e.getMessage() + ".");
		}
		return true;
	}

	public String getType() {
		return TYPE;
	}

	public void init() {
	}

	private String handlePrint(String[] args) {
		String str = "";
		if (args != null) {
			int size = 0;
			for (String arg : args) {
				str += arg;
				if (size < args.length - 1) {
					str += ",";
				}
				size++;
			}
		}
		return str;
	}
	
}
