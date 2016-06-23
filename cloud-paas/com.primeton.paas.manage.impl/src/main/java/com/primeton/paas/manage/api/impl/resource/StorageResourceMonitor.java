/**
 * 
 */
package com.primeton.paas.manage.api.impl.resource;

import java.util.List;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.StorageException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.spi.factory.StorageManagerFactory;
import com.primeton.paas.manage.spi.factory.StoragePoolConfigFactory;
import com.primeton.paas.manage.spi.model.Storage;
import com.primeton.paas.manage.spi.model.StoragePoolConfig;
import com.primeton.paas.manage.spi.model.VmPoolConfig;
import com.primeton.paas.manage.spi.resource.IStorageManager;
import com.primeton.paas.manage.spi.resource.IStoragePoolConfig;

/**
 * 
 * @author liming(mailto:li-ming@primeton.com)
 *
 */
public class StorageResourceMonitor implements Runnable {


	private IStorageManager storageManager;
	private IStoragePoolConfig poolConfigManager;
	
	private boolean flag = true;
	private boolean isRunning = false;
	private String id;
	private StoragePoolConfig poolConfig;
	
	private int currentRetrySize = 0;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(StorageResourceMonitor.class);
	
	/**
	 * 
	 * @param id
	 * @param poolConfig
	 */
	public StorageResourceMonitor(String id, StoragePoolConfig poolConfig) {
		super();
		this.id = id;
		this.poolConfig = poolConfig;
		storageManager =  StorageManagerFactory.getManager();
		poolConfigManager = StoragePoolConfigFactory.getManager();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (!SystemVariables.isIaasEnableStorage()) {
			logger.warn("Can not start storage resource monitor, IAAS-Storage API has been disabled.");
			return;
		}
		if (this.poolConfig == null) {
			return;
		} else if (this.poolConfig.getIsEnable() == StoragePoolConfig.DISABLE) {
			return;
		}
		
		this.isRunning = true;
		if (logger.isInfoEnabled()) {
			logger.info("Start storage resource monitor [" + this.poolConfig.getId() + "] success.");
		}
		
		while (this.flag) {
			List<Storage> sharedStorageList = storageManager.getAll();
			int left = 0;
			if (sharedStorageList != null && sharedStorageList.size() > 0) {
				for (Storage sharedStorage : sharedStorageList) {
					if (sharedStorage.getSize() == poolConfig
							.getStorageSize() && !sharedStorage.isAssigned()) {
						left++;
					}
				}
			}
			
			if (left >= poolConfig.getMinSize() && left <= poolConfig.getMaxSize()) {
				if (logger.isInfoEnabled()) {
					logger.info("StoragePoolConfig [" + this.poolConfig.getId() + "] current size is " + left + ".");
				}
			} else if (left < this.poolConfig.getMinSize()) {
				int size = this.poolConfig.getMinSize() - left;
				size = size > this.poolConfig.getIncreaseSize() ? this.poolConfig.getIncreaseSize() : size;
				Storage sharedStorage = null;
				try {
					sharedStorage = storageManager.create(poolConfig.getStorageSize(), null, this.poolConfig.getCreateTimeout() * 1000L);
				} catch (Throwable t) {
					currentRetrySize ++;
					if (logger.isErrorEnabled()) {
						logger.error("Create sharedStorage failured,id:"+poolConfig.getId()+" , currentRetrySize:"+currentRetrySize+" , error:"+t);
					}
					if(currentRetrySize >= poolConfig.getRetrySize()){
						this.close();
						poolConfig.setIsEnable(VmPoolConfig.DISABLE);
						poolConfigManager.update(poolConfig);
						logger.warn("Send SMS ...");
					}
				}
				if (sharedStorage == null) {
					if (logger.isWarnEnabled()) {
						logger.warn("Create storage [StorageSize:" + poolConfig.getStorageSize() + " failured.");
					}
				}
			} else if (left > this.poolConfig.getMaxSize()) {
				int size = left - this.poolConfig.getMaxSize();
				size = size > this.poolConfig.getDecreaseSize() ? this.poolConfig.getDecreaseSize() : size;
				try {
					storageManager.destroy(poolConfig.getStorageSize(), size);
				} catch (StorageException e) {
					if (logger.isErrorEnabled()) {
						logger.error(e);
					}
				}
			}
			
			ThreadUtil.sleep(this.poolConfig.getTimeInterval() * 1000L);
		}
		this.isRunning = false;
	}
	
	public void close() {
		this.flag = false;
	}
	
	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.poolConfig.toString();
	}
	
}
