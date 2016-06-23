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
import com.primeton.paas.manage.api.exception.StorageException;
import com.primeton.paas.manage.api.factory.TaskManagerFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultWhiteListDao;
import com.primeton.paas.manage.api.impl.dao.WhiteListDao;
import com.primeton.paas.manage.api.impl.task.DestroyStorageTask;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.ITaskManager;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.factory.StorageManagerFactory;
import com.primeton.paas.manage.spi.model.Storage;
import com.primeton.paas.manage.spi.model.StorageVO;
import com.primeton.paas.manage.spi.model.WhiteList;
import com.primeton.paas.manage.spi.resource.IStorageManager;

/**
 * @author liming
 *
 */
public class StorageManagerUtil {

	private static ILogger logger = LoggerFactory.getLogger(StorageManagerUtil.class);
	
	private static IStorageManager storageManager = StorageManagerFactory
			.getManager();
	private static ITaskManager taskManager = TaskManagerFactory.getManager();

	private static WhiteListDao whiteListDao = new DefaultWhiteListDao();

	/**
	 * 
	 * @param storage
	 * @param page
	 * @return
	 */
	public static Storage[] queryStorages(Storage storage,
			IPageCond page) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (null  != storage) {
			criteria.put("id", storage.getId()); //$NON-NLS-1$
			criteria.put("name", storage.getName()); //$NON-NLS-1$
			criteria.put("path", storage.getPath()); //$NON-NLS-1$
			criteria.put("size", storage.getSize()); //$NON-NLS-1$
			criteria.put("isAssigned", Storage.STATUS_ASSIGNED);
		}
		List<Storage> storages = storageManager.getAll(criteria, page);
		if (null != storages) {
			List<Storage> storagesTemp = new ArrayList<Storage>();
			List<WhiteList> wList = whiteListDao.getAll();
			for (Storage s : storages) {
				for (WhiteList w : wList) {
					if (w.getId().equals(s.getId())
							&& w.getStatus() == WhiteList.STATUS_MOUNTED) {
						s.setMountStatus(WhiteList.STATUS_MOUNTED);
						break;
					}
				}
				storagesTemp.add(s);
			}
			return storagesTemp.toArray(new Storage[storagesTemp.size()]);
		}
		return null;
	}

	/**
	 * 
	 * @param storageId
	 * @return
	 */
	public static Storage queryStorage(String storageId) {
		try {
			return storageManager.get(storageId);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 
	 * @param storage
	 * @return
	 */
	public static boolean updateStorage(Storage storage) {
		try {
			storageManager.update(storage);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param id
	 * @param ip
	 * @param mountPoint
	 * @return
	 */
	public static boolean updateMountPoint(String id, String ip,
			String mountPoint) {
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(ip)) {
			return false;
		}
		try {
			storageManager.unmount(ip, id);
			if (StringUtil.isNotEmpty(mountPoint)) {
				storageManager.mount(ip, id, mountPoint);
			}
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param id
	 * @param ip
	 * @return
	 */
	public static boolean addMountIp(String id, String ip) {
		if (id == null || ip == null) {
			return false;
		}
		try {
			return storageManager.addWhiteList(id, ip);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	/**
	 * 
	 * @param id
	 * @param storages
	 * @return
	 */
	public static boolean delMountIps(String id, StorageVO[] storages) {
		if (id == null || storages == null) {
			return false;
		}
		String ip = "";
		try {
			for (StorageVO s : storages) {
				ip = s.getIp();
				if (s.getStatus() == Storage.STATUS_ASSIGNED) {
					storageManager.unmount(ip, id);
				}
				storageManager.removeWhiteList(id, ip);
			}
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param id
	 * @param ips
	 * @return
	 */
	public static boolean delMountIps(String id, String[] ips) {
		if (id == null || ips == null) {
			return false;
		}
		String ip = "";
		try {
			for (String v : ips) {
				ip = v;
				storageManager.unmount(ip, id);
				storageManager.removeWhiteList(id, ip);
			}
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param storageIds
	 * @return
	 */
	public static boolean removeStorages(String storageIds) {
		if (StringUtil.isEmpty(storageIds)) {
			return true;
		}
		List<Storage> storageList = new ArrayList<Storage>();
		String[] ids = storageIds.split(",");
		for (String id : ids) {
			Storage s = storageManager.get(id);
			if (null != s) {
				storageList.add(s);
			}
		}
		if (storageList.size() > 0) {
			removeStorages(storageList.toArray(new Storage[storageList
					.size()]));
		}
		return true;
	}

	/**
	 * 
	 * @param storageIds
	 * @return
	 */
	public static boolean releaseStorages(String storageIds) {
		if (StringUtil.isEmpty(storageIds)) {
			return true;
		}
		List<Storage> storageList = new ArrayList<Storage>();
		String[] ids = storageIds.split(",");
		for (String id : ids) {
			Storage s = storageManager.get(id);
			if (s.getWhiteLists().size() > 0) {
				return false;
			}
			if (null != s) {
				storageList.add(s);
			}
		}
		if (storageList.size() > 0) {
			String sId = "";
			for (Storage s : storageList) {
				try {
					sId = s.getId();
					storageManager.release(sId);
				} catch (StorageException e) {
					logger.error(e);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @param storages
	 * @return
	 */
	public static boolean removeStorages(Storage[] storages) {
		if (storages == null || storages.length < 1) {
			return true;
		}
		List<String> curs = new ArrayList<String>();
		for (Storage cur : storages) {
			String id = cur.getId();
			curs.add(id);
		}
		if (curs.size() > 0) {
			String[] vms = curs.toArray(new String[curs.size()]);
			try {
				// 销毁存储
				String owner = DataContextManager.current().getMUODataContext()
						.getUserObject().getUserId();
				long recoverTimeout = SystemVariables
						.getStorageDestroyTimeout() * vms.length;
				taskManager.add(new DestroyStorageTask(vms),
						recoverTimeout, owner);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return true;
	}

	/**
	 * 分配存储
	 * 
	 * @param storages
	 * @return
	 */
	public static boolean applyStorages(Storage[] storages) {
		if (storages == null || storages.length < 1) {
			return false;
		}
		for (Storage s : storages) {
			try {
				Storage storage = storageManager.get(s.getId());
				storage.setAssigned(true);
				storageManager.update(storage);
			} catch (StorageException e) {
				logger.error(e);
				return false;
			}
		}
		return true;
	}

	/**
	 * 分配存储
	 * 
	 * @param sharedStorages
	 * @return
	 */
	public static boolean applyStorages(String[] ids) {
		if (ids == null || ids.length < 1) {
			return false;
		}
		String id = "";
		for (String v : ids) {
			try {
				id = v;
				Storage storage = storageManager.get(id);
				storage.setAssigned(true);
				storageManager.update(storage);
			} catch (StorageException e) {
				logger.error(e);
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据IP获取存储列表
	 * 
	 * @param orders
	 * @return
	 */
	public static List<StorageVO> getStoragesByIp(String ip) {
		try {
			return storageManager.getStoragesByIp(ip);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 
	 * @param storageId
	 * @param size
	 * @return
	 */
	public static boolean upgradeStorage(String storageId, int size) {
		if (StringUtil.isEmpty(storageId) || size <= 0) {
			return false;
		}
		try {
			return storageManager.modifyStorageSize(storageId, size);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

}
