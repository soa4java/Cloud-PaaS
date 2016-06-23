/**
 * 
 */
package com.primeton.paas.console.platform.service.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.manage.api.factory.VariableManagerFactory;
import com.primeton.paas.manage.api.manager.IVariableManager;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.RandomUtil;
import com.primeton.paas.manage.spi.factory.StorageManagerFactory;
import com.primeton.paas.manage.spi.factory.StoragePoolConfigFactory;
import com.primeton.paas.manage.spi.model.Storage;
import com.primeton.paas.manage.spi.model.StoragePoolConfig;
import com.primeton.paas.manage.spi.resource.IStorageManager;
import com.primeton.paas.manage.spi.resource.IStoragePoolConfig;

/**
 * @author liming
 *
 */
public class StoragePoolManagerUtil {

	private static IStoragePoolConfig storagePoolConfigManager = StoragePoolConfigFactory
			.getManager();

	private static IStorageManager sharedStorageManager = StorageManagerFactory
			.getManager();

	private static ILogger logger = LoggerFactory.getLogger(StoragePoolManagerUtil.class);

	private static IVariableManager varManager = VariableManagerFactory
			.getManager();

	/**
	 * 
	 * @param poolConfig
	 * @param page
	 * @return
	 */
	public static StoragePoolConfig[] queryStoragePools(
			StoragePoolConfig poolConfig, IPageCond page) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("id", poolConfig.getId()); //$NON-NLS-1$
		criteria.put("storageSize", poolConfig.getStorageSize()); //$NON-NLS-1$
		List<StoragePoolConfig> poolConfigs = storagePoolConfigManager
				.getAll(criteria, page);
		if (null != poolConfigs) {
			for (StoragePoolConfig s : poolConfigs) {
				int size = 0;
				Storage[] sharedStorages = queryStoragesBySize(
						s.getStorageSize(), new PageCond());
				if (null != sharedStorages) {
					size = sharedStorages.length;
				}
				s.getAttributes().put("size", size + ""); //$NON-NLS-1$
			}
			return (StoragePoolConfig[]) poolConfigs
					.toArray(new StoragePoolConfig[poolConfigs
							.size()]);
		}
		return null;
	}

	/**
	 * 
	 * @param configId
	 * @return
	 */
	public static StoragePoolConfig queryStoragePool(
			String configId) {
		try {
			return storagePoolConfigManager
					.get(configId);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 
	 * @param size
	 * @param pageCond
	 * @return
	 */
	public static Storage[] queryStoragesBySize(int size,
			IPageCond pageCond) {
		List<Storage> sList = sharedStorageManager.getBySize(size);
		List<Storage> storageList = new ArrayList<Storage>();
		if (null != sList && sList.size() > 0) {
			for (Storage s : sList) {
				if (!s.isAssigned()) {
					storageList.add(s);
				}
			}
		}
		pageCond.setCount(storageList.size());
		return storageList.toArray(new Storage[storageList.size()]);
	}

	/**
	 * 
	 * @param config
	 * @return
	 */
	public static boolean updateStoragePool(StoragePoolConfig config) {
		try {
			int timeout = varManager.getIntValue(
					SystemVariable.STORAGE_OPERATE_TIMEOUT, 100 * 1000) / 1000;
			config.setCreateTimeout(timeout);
			config.setDestroyTimeout(timeout);
			storagePoolConfigManager.update(config);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param config
	 * @return
	 */
	public static boolean addStoragePool(StoragePoolConfig config) {
		try {
			config.setId(RandomUtil.generateId());
			int timeout = varManager.getIntValue(
					SystemVariable.STORAGE_OPERATE_TIMEOUT, 100 * 1000) / 1000;
			config.setCreateTimeout(timeout);
			config.setDestroyTimeout(timeout);
			storagePoolConfigManager.add(config);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 批量删除主机池
	 * 
	 * @param orders
	 * @return
	 */
	public static boolean removeStoragePools(StoragePoolConfig[] configs) {
		if (configs == null || configs.length < 1) {
			return true;
		}
		for (StoragePoolConfig cur : configs) {
			String id = cur.getId();
			storagePoolConfigManager.delete(id);
		}
		return true;
	}

	/**
	 * 
	 * @param cfgs
	 * @return
	 */
	public static boolean removeStoragePools(String[] cfgs) {
		if (cfgs == null || cfgs.length < 1) {
			return true;
		}
		for (String id : cfgs) {
			storagePoolConfigManager.delete(id);
		}
		return true;
	}

	/**
	 * 未使用的获取应用服务器可供选择的存储大小<br>
	 * 
	 * @return
	 */
	public static String[] getFilterAppStorageSizes() {
		StoragePoolConfig[] configs = queryStoragePools(
				new StoragePoolConfig(), new PageCond());
		String[] appStorageSizes = SystemVariable.getAppStorageSizes();
		String filterAppStorageSizes = "";
		if (null != configs) {
			for (String appStorageSize : appStorageSizes) {
				int i = 0;
				for (StoragePoolConfig sharedStoragePoolConfig : configs) {
					if (appStorageSize.equals(sharedStoragePoolConfig
							.getStorageSize() + "")) {
						i = 1;
						break;
					}
				}
				if (i == 0) {
					filterAppStorageSizes += appStorageSize + ",";
				}
			}
			return filterAppStorageSizes.split(",");
		}
		return appStorageSizes;
	}

	/**
	 * 未使用的获取应用服务器可供选择的存储大小数量<br>
	 * 
	 * @return
	 */
	public static int getFilterAppStorageSizesNum() {
		if (getFilterAppStorageSizes()[0].equals(""))
			return 0;
		return getFilterAppStorageSizes().length;
	}

}
