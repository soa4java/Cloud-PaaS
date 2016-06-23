/**
 * 
 */
package com.primeton.paas.manage.api.impl.task;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.StorageException;
import com.primeton.paas.manage.api.exception.TaskException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.manager.Executable;
import com.primeton.paas.manage.spi.factory.StorageManagerFactory;
import com.primeton.paas.manage.spi.resource.IStorageManager;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DestroyStorageTask implements Executable {

	public static final String TYPE = "8";

	private static ILogger logger = ManageLoggerFactory
			.getLogger(DestroyStorageTask.class);

	private static IStorageManager sharedStorageManager = StorageManagerFactory
			.getManager();

	private String[] storages;

	public DestroyStorageTask(String[] storages) {
		this.storages = storages;
	}

	public void clear() {

	}

	public String execute() throws TaskException {
		boolean bn = this.destroyStorage(storages);
		if (bn)
			return "Destroy storage [" + handlePrint(storages) + "] success.";
		return "Destroy storage [" + handlePrint(storages) + "] error.";
	}

	/**
	 * 
	 * @param storages
	 * @return
	 * @throws TaskException
	 */
	public boolean destroyStorage(String[] storages) throws TaskException {
		if (storages == null) {
			return false;
		}
		for (String storage : storages) {
			try {
				sharedStorageManager.destroy(storage);
			} catch (StorageException e) {
				logger.error("Destory storages [" + handlePrint(storages)
						+ "] error : " + e.getMessage() + ".");
				throw new TaskException("Destory storages ["
						+ handlePrint(storages) + "] error : " + e.getMessage()
						+ ".");
			}
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
