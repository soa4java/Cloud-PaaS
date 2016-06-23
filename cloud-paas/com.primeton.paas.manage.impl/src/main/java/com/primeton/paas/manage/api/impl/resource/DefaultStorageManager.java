/**
 * 
 */
package com.primeton.paas.manage.api.impl.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.mqclient.api.CommandResultMessage;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.StorageException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultStorageDao;
import com.primeton.paas.manage.api.impl.dao.DefaultWhiteListDao;
import com.primeton.paas.manage.api.impl.dao.StorageDao;
import com.primeton.paas.manage.api.impl.dao.WhiteListDao;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.GenKeyUtils;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.model.Storage;
import com.primeton.paas.manage.spi.model.StorageVO;
import com.primeton.paas.manage.spi.model.WhiteList;
import com.primeton.paas.manage.spi.resource.IStorageManager;
import com.primeton.upcloud.ws.api.JobResult;
import com.primeton.upcloud.ws.api.ResourceManager;
import com.primeton.upcloud.ws.api.ResourceManagerFactory;
import com.primeton.upcloud.ws.api.StorageNasJobResult;
import com.primeton.upcloud.ws.api.StorageNasVolume;
import com.primeton.upcloud.ws.api.VmJobResult;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultStorageManager implements IStorageManager {

	private ResourceManager manager = ResourceManagerFactory.getManager();
	private StorageDao storageDao = new DefaultStorageDao();
	private WhiteListDao whiteListDao = new DefaultWhiteListDao();

	private static ILogger logger = ManageLoggerFactory
			.getLogger(DefaultStorageManager.class);

	public static final String SH_MOUNT = "mount.sh"; //$NON-NLS-1$

	private boolean isIaaSEnable = false;

	/**
	 * Default. <br>
	 */
	public DefaultStorageManager() {
		super();
		isIaaSEnable = SystemVariables.isIaasEnableStorage();
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#create(int, java.util.List, long)
	 */
	public Storage create(int size, List<String> whiteLists, long timeout)
			throws StorageException, TimeoutException {
		String storageName = GenKeyUtils.genKey();
		return create(size, whiteLists, storageName, timeout);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#create(int, java.util.List, java.lang.String, long)
	 */
	public Storage create(int size, List<String> whiteLists,
			String storageName, long timeout) throws StorageException,
			TimeoutException {
		if (!isIaaSEnable) {
			logger.warn("Create Storage [size:" + size
					+ "] error, because iaas api was disable.");
			return null;
		}

		if (size <= 0) {
			return null;
		}

		List<String> ips = new ArrayList<String>();
		if (whiteLists != null && !whiteLists.isEmpty()) {
			for (String ip : whiteLists) {
				if (StringUtil.isNotEmpty(ip) && !ips.contains(ip)) {
					ips.add(ip);
				}
			}
		}

		if (StringUtil.isEmpty(storageName)) {
			return null;
		}

		if (null != getByName(storageName)) {
			String message = "Create Storage [storageName:" + storageName
					+ "] already exist.";
			throw new StorageException(message);
		}

		List<String> tmpIps = new ArrayList<String>();
		tmpIps.add("127.0.0.1"); //$NON-NLS-1$
		JobResult rs = manager.createStorageNasVolume(
				WebServiceUtil.getWsUrl(), WebServiceUtil.getUsername(),
				WebServiceUtil.getPassword(), WebServiceUtil.getNasZoneID(),
				storageName, WebServiceUtil.getDefaultGroup(), size, tmpIps);

		if (rs == null || !JobResult.STATUS_OK.equals(rs.getStatus())) {
			String message = (rs != null) ? rs.getMessage()
					: "Create Storage [size:" + size + "] error.";
			throw new StorageException(message);
		}
		String jobID = rs.getId();
		if (logger.isInfoEnabled()) {
			logger.info("Submit Storage vm [size:" + size
					+ "] request to iaas success, return jobID is " + jobID
					+ ".");

		}

		long begin = System.currentTimeMillis();
		long end = begin;
		timeout = timeout > 0 ? timeout : 300000L;
		timeout = timeout * 1;
		StorageNasJobResult vrs = null;
		boolean isTimeout = false;
		while (true) {
			end = System.currentTimeMillis();
			if (end - begin > timeout) {
				isTimeout = true;
				break;
			}
			vrs = manager.queryStorageNasVolumeJob(WebServiceUtil.getWsUrl(),
					WebServiceUtil.getUsername(), WebServiceUtil.getPassword(),
					jobID);
			if (logger.isInfoEnabled()) {
				logger.info("Query create Storage job result is : " + vrs);
			}
			if (vrs != null
					&& (StorageNasJobResult.STATUS_SUCCESS.equals(vrs
							.getJobStatus()) || StorageNasJobResult.STATUS_FAIL
							.equals(vrs.getJobStatus()))) {
				break;
			}

			ThreadUtil.sleep(10000L);
		}

		if (isTimeout) {
			throw new TimeoutException("Create Storage [size:" + size
					+ ", timeout:" + timeout + "] timeout.");
		}

		if (vrs == null
				|| !VmJobResult.STATUS_SUCCESS.equals(vrs.getJobStatus())) {
			throw new StorageException("Create Storage [size:" + size
					+ ", timeout:" + timeout + "] error.");
		}

		StorageNasVolume volume = vrs.getNasVolume();
		Storage storage = new Storage();
		if (volume != null) {
			storage.setId(volume.getVolumeID());
			storage.setAssigned(false);
			storage.setName(volume.getVolumeName());
			;
			storage.setPath(volume.getVolumePath());
			storage.setSize(Integer.parseInt(volume.getVolumeSize()));
		}

		boolean success = storageDao.insert(storage);
		if (!success) {
			throw new StorageException("Save " + storage + " exception.");
		}
		if (!ips.isEmpty()) {
			for (String ip : ips) {
				WhiteList whiteList = new WhiteList();
				whiteList.setIp(ip);
				whiteList.setStatus(WhiteList.STATUS_NOTMOUNT);
				whiteList.setId(storage.getId());
				whiteListDao.insert(whiteList);
			}
		}
		return storage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#apply(int, java.util.List)
	 */
	public Storage apply(int size, List<String> whiteLists)
			throws StorageException {
		if (size <= 0 || null == whiteLists || whiteLists.size() <= 0) {
			return null;
		}
		List<String> ips = new ArrayList<String>();
		if (whiteLists != null && !whiteLists.isEmpty()) {
			for (String ip : whiteLists) {
				if (StringUtil.isNotEmpty(ip) && !ips.contains(ip)) {
					ips.add(ip);
				}
			}
		}

		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("isAssigned", Storage.STATUS_NOTASSIGNED); //$NON-NLS-1$
		criteria.put("size", size); //$NON-NLS-1$
		List<Storage> storageList = storageDao.getAll(criteria);

		Storage sharedStorage = null;
		if (null != storageList && storageList.size() > 0) {
			sharedStorage = storageList.get(0);
			sharedStorage.setIsAssigned(Storage.STATUS_ASSIGNED);
			update(sharedStorage);

			addWhiteLists(sharedStorage.getId(),
					ips.toArray(new String[ips.size()]));
		}

		return sharedStorage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#release(java.lang.String)
	 */
	public void release(String id) throws StorageException {
		removeWhiteLists(id);
		Storage storage = storageDao.get(id);
		storage.setAssigned(false);
		update(storage);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#destroy(java.lang.String)
	 */
	public void destroy(String id) throws StorageException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		Storage storage = get(id);
		if (storage == null) {
			return;
		}

		long begin = System.currentTimeMillis();
		if (logger.isInfoEnabled()) {
			logger.info("Begin destroy " + storage + ".");
		}

		List<String> hosts = getIpsById(id);
		if (hosts != null && !hosts.isEmpty()) {
			for (String ip : hosts) {
				try {
					unmount(ip, id);
				} catch (StorageException e) {
					if (logger.isErrorEnabled()) {
						logger.error(e);
					}
				}
			}
		}

		if (isIaaSEnable) {
			JobResult rs = manager.destoryStorageNasVolume(
					WebServiceUtil.getWsUrl(), WebServiceUtil.getUsername(),
					WebServiceUtil.getPassword(), id);
			if (rs != null && JobResult.STATUS_OK.equals(rs.getStatus())) {
				if (logger.isInfoEnabled()) {
					logger.info("Destroy Storage [" + rs.getStatus()
							+ "] success.");
				}
			} else if (rs != null) {
				if (logger.isInfoEnabled()) {
					logger.info("Destroy Storage [" + rs.getStatus()
							+ "] error.");
				}
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("Destroy Storage error.");
				}
			}
		} else {
			logger.warn("Destroy Storage error, because iaas api was disable.");
		}

		whiteListDao.delete(id, null);

		storageDao.delete(id);

		long end = System.currentTimeMillis();
		if (logger.isInfoEnabled()) {
			logger.info("Finish destroy " + storage + ", time spent "
					+ (end - begin) / 1000L + " seconds.");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#destroy(int, int)
	 */
	public void destroy(int size, int num) throws StorageException {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("isAssigned", Storage.STATUS_NOTASSIGNED); //$NON-NLS-1$
		criteria.put("size", size); //$NON-NLS-1$
		IPageCond page = new PageCond();
		page.setLength(num);
		List<Storage> storageList = storageDao.getAll(criteria, page);

		for (Storage sharedStorage : storageList) {
			storageDao.delete(sharedStorage.getId());
		}

		if (!isIaaSEnable) {
			logger.warn("Destroy Storage error, because iaas api was disable.");
			return;
		}

		for (Storage sharedStorage : storageList) {

			long begin = System.currentTimeMillis();
			if (logger.isInfoEnabled()) {
				logger.info("Begin destroy " + sharedStorage + ".");
			}

			JobResult rs = manager.destoryStorageNasVolume(
					WebServiceUtil.getWsUrl(), WebServiceUtil.getUsername(),
					WebServiceUtil.getPassword(), sharedStorage.getId());
			if (rs != null && JobResult.STATUS_OK.equals(rs.getStatus())) {
				if (logger.isInfoEnabled()) {
					logger.info("Destroy Storage [" + rs.getStatus()
							+ "] success.");
				}
			} else if (rs != null) {
				if (logger.isInfoEnabled()) {
					logger.info("Destroy Storage [" + rs.getStatus()
							+ "] error.");
				}
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("Destroy Storage error.");
				}
			}

			long end = System.currentTimeMillis();
			if (logger.isInfoEnabled()) {
				logger.info("Finish destroy " + sharedStorage + ", time spent "
						+ (end - begin) / 1000L + " seconds.");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#update(com.primeton.paas.manage.spi.model.SharedStorage)
	 */
	public void update(Storage storage) throws StorageException {
		if (storage == null || StringUtil.isEmpty(storage.getId())) {
			return;
		}

		Storage obj = get(storage.getId());
		if (obj == null) {
			throw new StorageException("SharedStorage ["
					+ storage.getId() + "] not found.");
		}

		String wl = "";
		List<WhiteList> wlist = obj.getWhiteLists();
		if (null != wlist) {
			for (WhiteList whiteList : wlist) {
				wl += whiteList.getIp() + "|";
			}
		}

		long begin2 = System.currentTimeMillis();
		if (logger.isInfoEnabled()) {
			logger.info("Begin update SharedStorage [id:" + storage.getId()
					+ ", path:" + storage.getPath() + ", whiteLists:" + wl
					+ "].");
		}

		if (isIaaSEnable) {
			if (obj.getSize() != storage.getSize()) {
				boolean rs = manager.modifyStorageNasVolumeSize(
						WebServiceUtil.getWsUrl(),
						WebServiceUtil.getUsername(),
						WebServiceUtil.getPassword(), storage.getId(),
						storage.getSize());
				if (!rs) {
					String message = "Update Storage [ id:" + storage.getId()
							+ ",size:" + storage.getSize() + "] error.";
					throw new StorageException(message);
				} else {
					logger.info("Update Storage [id:" + storage.getId()
							+ " ,size:" + storage.getSize() + "] success.");
				}
			}
		} else {
			logger.warn("Update Storage error, because iaas api was disable.");
		}

		storageDao.update(storage);
		if (logger.isInfoEnabled()) {
			logger.info("End update SharedStorage [id:" + storage.getId()
					+ ", path:" + storage.getPath() + ", whiteLists:" + wl
					+ "]. Time spent " + (System.currentTimeMillis() - begin2)
					/ 1000L + " seconds.");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#mount(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void mount(String ip, String id, String mountPoint)
			throws StorageException {
		if (StringUtil.isEmpty(ip) || StringUtil.isEmpty(id)
				|| StringUtil.isEmpty(mountPoint)) {
			throw new IllegalArgumentException("ip = " + ip + ", storageId = "
					+ id + ", mountPoint = " + mountPoint);
		}

		if (mountPoint.indexOf(Storage.PATH_PREFIX) != 0) {
			throw new StorageException("ip = " + ip + ", storageId = "
					+ id + ", mountPoint = " + mountPoint
					+ " error,path prefix must be ["
					+ Storage.PATH_PREFIX + "] beginning");
		}

		Storage storage = get(id);
		if (storage == null) {
			throw new StorageException(
					StorageException.NOT_EXIST_EXCEPTION + "[" + id + "]");
		}

		List<String> whiteLists = getWhiteLists(id);
		if (null != whiteLists && whiteLists.size() > 0) {
			if (modifyStorageWhiteList(id, whiteLists)) {
				logger.warn("Storage [" + id
						+ "] has whiteList synchronized success.");
			} else {
				logger.warn("Storage [" + id
						+ "] has whiteList synchronized error.");
			}
		}

		WhiteList whiteList = whiteListDao.getWhiteList(id, ip);
		if (whiteList == null) {
			throw new StorageException("Host [" + ip
					+ "] not in SharedStorage [" + id + "] white list.");
		} else if (whiteList.getStatus() == WhiteList.STATUS_MOUNTED) {
			if (logger.isWarnEnabled()) {
				logger.warn("Host [" + ip
						+ "] has already mount SharedStorage [" + id + "]");
			}
			return;
		}

		WhiteList wList = whiteListDao.getWhiteListByMountPoint(mountPoint, ip);
		if (wList != null && wList.getStatus() == WhiteList.STATUS_MOUNTED) {
			throw new StorageException(
					"Host [" + ip + "] mountPoint [" + mountPoint
							+ "] has in use,please use the other mountPoint, or unloading.");
		}

		Map<String, String> args = new HashMap<String, String>();

		String mountInfos = "";
		List<StorageVO> storageList = getStoragesByIp(ip);
		if (storageList != null && storageList.size() > 0) {
			for (StorageVO s : storageList) {
				if (s.getStatus() == WhiteList.STATUS_MOUNTED
						&& StringUtil.isNotEmpty(s.getMount())) {
					mountInfos += s.getPath() + "," + s.getMount() + ";";
				}
			}
		}
		mountInfos += storage.getPath() + "," + mountPoint;
		args.put("mountInfos", mountInfos);
		String mount_sh = SystemVariables.getBinHome() + "/Common/bin/" //$NON-NLS-1$
				+ SH_MOUNT;
		long begin = System.currentTimeMillis();

		try {
			CommandResultMessage mountResult = SendMessageUtil.sendMessage(ip,
					mount_sh, args, true);
			if (mountResult == null) {
				throw new StorageException(
						"Not receiving a response message to the NodeAgent ["
								+ ip + "].");
			} else if (mountResult.getBody() == null) {
				throw new StorageException(
						"The contents of the message is incomplete, missing message body.");
			} else if (!mountResult.getBody().getSuccess()) {
				logger.error("Error out is :\n"
						+ mountResult.getBody().toString());
				throw new StorageException("Host [" + ip
						+ "] mount storage [" + id + "] error.");
			}
			// SUCCESS
			long end = System.currentTimeMillis();
			logger.info("Host [" + ip + "] mount storage [" + id
					+ "] success. Time spents " + (end - begin) / 1000L
					+ " seconds.");
		} catch (MessageException e) {
			throw new StorageException(e);
		}

		whiteList.setPath(mountPoint);
		whiteList.setStatus(WhiteList.STATUS_MOUNTED);
		whiteListDao.update(whiteList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.primeton.paas.manage.api.manager.ISharedStorageManager#unmount(java
	 * .lang.String, java.lang.String)
	 */
	public void unmount(String ip, String id) throws StorageException {
		if (StringUtil.isEmpty(ip) || StringUtil.isEmpty(id)) {
			return;
		}

		Storage storage = get(id);
		if (storage == null) {
			throw new StorageException(
					StorageException.NOT_EXIST_EXCEPTION + "[" + id + "]");
		}

		WhiteList whiteList = whiteListDao.getWhiteList(id, ip);
		if (whiteList == null) {
			return;
		} else if (whiteList.getStatus() == WhiteList.STATUS_NOTMOUNT) {
			logger.warn("Host [" + ip + "] not mount SharedStorage [" + id
					+ "]");
			return;
		}

		Map<String, String> args = new HashMap<String, String>();

		String mountInfos = "";
		List<StorageVO> storageList = getStoragesByIp(ip);
		if (storageList != null && storageList.size() > 0) {
			for (StorageVO s : storageList) {
				if (!s.getId().equals(storage.getId())
						&& s.getStatus() == WhiteList.STATUS_MOUNTED
						&& StringUtil.isNotEmpty(s.getMount())) {
					mountInfos += s.getPath() + "," + s.getMount() + ";";
				}
			}
		}
		args.put("mountInfos", mountInfos); //$NON-NLS-1$
		args.put("umountInfo", storage.getPath() + "," + whiteList.getPath()); //$NON-NLS-1$
		String mount_sh = SystemVariables.getBinHome() + "/Common/bin/" //$NON-NLS-1$
				+ SH_MOUNT;
		long begin = System.currentTimeMillis();

		try {
			CommandResultMessage mountResult = SendMessageUtil.sendMessage(ip,
					mount_sh, args, true);
			if (mountResult == null) {
				throw new StorageException(
						"Not receiving a response message to the NodeAgent [" + ip + "].");
			} else if (mountResult.getBody() == null) {
				throw new StorageException(
						"The contents of the message is incomplete, missing message body.");
			} else if (!mountResult.getBody().getSuccess()) {
				logger.error("Error out is :\n"
						+ mountResult.getBody().toString());
				throw new StorageException("Host [" + ip
						+ "] unmount storage [" + id + "] error.");
			}
			// SUCCESS
			long end = System.currentTimeMillis();
			logger.info("Host [" + ip + "] unmount storage [" + id
					+ "] success. Time spents " + (end - begin) / 1000L
					+ " seconds.");
		} catch (MessageException e) {
			logger.error("Host [" + ip + "] unmount storage [" + id
					+ "] failed.", e);
		}

		whiteList.setStatus(WhiteList.STATUS_NOTMOUNT);
		whiteListDao.update(whiteList);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#unmount(java.lang.String)
	 */
	public void unmount(String ip) throws StorageException {
		if (StringUtil.isEmpty(ip)) {
			return;
		}
		List<WhiteList> whiteLists = whiteListDao.getWhiteListsByIp(ip);
		if (whiteLists == null || whiteLists.isEmpty()) {
			return;
		}
		for (WhiteList o : whiteLists) {
			try {
				unmount(ip, o.getId());
			} catch (StorageException e) {
				logger.error(e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#addWhiteList(java.lang.String, java.lang.String)
	 */
	public boolean addWhiteList(String id, String ip)
			throws StorageException {
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(ip)) {
			return false;
		}
		boolean exists = enableMount(id, ip);
		if (exists) {
			return true;
		}

		List<String> whiteLists = getWhiteLists(id);
		if (whiteLists == null) {
			whiteLists = new ArrayList<String>();
		}
		whiteLists.add(ip);

		if (modifyStorageWhiteList(id, whiteLists)) {
			WhiteList whiteList = new WhiteList();
			whiteList.setId(id);
			whiteList.setIp(ip);
			whiteList.setStatus(WhiteList.STATUS_NOTMOUNT);
			return whiteListDao.insert(whiteList);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#addWhiteLists(java.lang.String, java.lang.String[])
	 */
	public void addWhiteLists(String id, String[] hosts)
			throws StorageException {
		if (StringUtil.isEmpty(id) || hosts == null || hosts.length == 0) {
			return;
		}
		List<String> whiteLists = getWhiteLists(id);
		for (String ip : hosts) {
			if (StringUtil.isNotEmpty(ip) && !whiteLists.contains(ip)
					&& !enableMount(id, ip)) {
				whiteLists.add(ip);
			}
		}
		if (whiteLists.isEmpty()) {
			return;
		}
		if (modifyStorageWhiteList(id, whiteLists)) {
			for (String ip : whiteLists) {
				WhiteList whiteList = new WhiteList();
				whiteList.setId(id);
				whiteList.setIp(ip);
				whiteList.setStatus(WhiteList.STATUS_NOTMOUNT);
				whiteListDao.insert(whiteList);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#removeWhiteList(java.lang.String, java.lang.String)
	 */
	public boolean removeWhiteList(String id, String ip)
			throws StorageException {
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(ip)) {
			return false;
		}

		List<String> whiteLists = getWhiteLists(id);
		if (whiteLists == null || whiteLists.isEmpty()
				|| !whiteLists.contains(ip)) {
			return true;
		}
		whiteLists.remove(ip);

		if (modifyStorageWhiteList(id, whiteLists)) {
			whiteListDao.delete(id, ip);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#removeWhiteLists(java.lang.String)
	 */
	public boolean removeWhiteLists(String id) throws StorageException {
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		List<String> whiteLists = getWhiteLists(id);
		if (whiteLists == null || whiteLists.isEmpty()) {
			return true;
		}

		if (modifyStorageWhiteList(id, new ArrayList<String>())) {
			whiteListDao.delete(id, null);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#removeWhiteListsByIp(java.lang.String)
	 */
	public boolean removeWhiteListsByIp(String ip)
			throws StorageException {
		List<StorageVO> sList = getStoragesByIp(ip);
		if (null != sList && sList.size() > 0) {
			for (StorageVO s : sList) {
				removeWhiteList(s.getId(), s.getIp());
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#get(java.lang.String)
	 */
	public Storage get(String id) {
		Storage storage = storageDao.get(id);
		if (null != storage) {
			List<WhiteList> whiteLists = whiteListDao
					.getWhiteListsByStorage(storage.getId());
			storage.setWhiteLists(whiteLists);
		}
		return storage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#getAll(com.primeton.paas.manage.api.model.IPageCond)
	 */
	public List<Storage> getAll(IPageCond pageCond) {
		return storageDao.getAll(pageCond);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.ISharedStorageManager#getAll()
	 */
	public List<Storage> getAll() {
		return storageDao.getAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#getByHost(java.lang.String)
	 */
	public List<Storage> getByHost(String ip) {
		List<Storage> storages = new ArrayList<Storage>();
		if (!StringUtil.isNotEmpty(ip)) {
			return storages;
		}

		List<WhiteList> whiteLists = whiteListDao.getWhiteListsByIp(ip);
		if (whiteLists == null || whiteLists.isEmpty()) {
			return storages;
		}
		for (WhiteList whiteList : whiteLists) {
			if (whiteList.getStatus() == WhiteList.STATUS_MOUNTED) {
				Storage e = get(whiteList.getId());
				if (e != null) {
					storages.add(e);
				}
			}
		}
		return storages;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#getBySize(int)
	 */
	public List<Storage> getBySize(int size) {
		if (0 == size) {
			return null;
		}
		List<Storage> sList = this.getAll();
		List<Storage> storageList = new ArrayList<Storage>();
		for (Storage s : sList) {
			if (s.getSize() == size) {
				storageList.add(s);
			}
		}
		return storageList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#getStoragesByIp(java.lang.String)
	 */
	public List<StorageVO> getStoragesByIp(String ip) {
		List<StorageVO> storages = new ArrayList<StorageVO>();
		if (!StringUtil.isNotEmpty(ip)) {
			return storages;
		}
		List<WhiteList> whiteLists = whiteListDao.getWhiteListsByIp(ip);
		if (whiteLists == null || whiteLists.isEmpty()) {
			return storages;
		}
		for (WhiteList whiteList : whiteLists) {
			Storage e = get(whiteList.getId());
			if (e == null) {
				continue;
			}
			StorageVO s = new StorageVO();
			s.setId(e.getId());
			s.setName(e.getName());
			s.setPath(e.getPath());
			s.setAssigned(e.isAssigned());
			s.setSize(e.getSize());
			s.setIp(whiteList.getIp());
			s.setMount(whiteList.getPath());
			s.setStatus(whiteList.getStatus());
			if (e != null) {
				storages.add(s);
			}
		}
		return storages;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#getIpsById(java.lang.String)
	 */
	public List<String> getIpsById(String id) {
		List<String> list = new ArrayList<String>();
		if (StringUtil.isEmpty(id)) {
			return list;
		}
		List<WhiteList> whiteLists = whiteListDao.getWhiteListsByStorage(id);
		if (whiteLists == null || whiteLists.isEmpty()) {
			return list;
		}

		for (WhiteList whiteList : whiteLists) {
			if (whiteList.getStatus() == WhiteList.STATUS_MOUNTED
					&& !list.contains(whiteList.getIp())) {
				list.add(whiteList.getIp());
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#getWhiteLists(java.lang.String)
	 */
	public List<String> getWhiteLists(String id) {
		List<String> whiteLists = new ArrayList<String>();
		if (StringUtil.isEmpty(id)) {
			return whiteLists;
		}
		List<WhiteList> list = whiteListDao.getWhiteListsByStorage(id);
		if (list == null || list.isEmpty()) {
			return whiteLists;
		}
		for (WhiteList whiteList : list) {
			String ip = whiteList.getIp();
			if (!whiteLists.contains(ip)) {
				whiteLists.add(ip);
			}
		}
		return whiteLists;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#getWLists(java.lang.String)
	 */
	public List<WhiteList> getWLists(String id) {
		List<WhiteList> whiteLists = new ArrayList<WhiteList>();
		if (StringUtil.isEmpty(id)) {
			return whiteLists;
		}
		List<WhiteList> list = whiteListDao.getWhiteListsByStorage(id);
		if (list == null || list.isEmpty()) {
			return whiteLists;
		}
		for (WhiteList whiteList : list) {
			String ip = whiteList.getIp();
			if (!whiteLists.contains(ip)) {
				whiteLists.add(whiteList);
			}
		}
		return whiteLists;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#enableMount(java.lang.String, java.lang.String)
	 */
	public boolean enableMount(String id, String ip) {
		return whiteListDao.getWhiteList(id, ip) != null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#getMountPoint(java.lang.String, java.lang.String)
	 */
	public String getMountPoint(String id, String ip) {
		WhiteList whiteList = whiteListDao.getWhiteList(id, ip);
		return whiteList == null ? null : whiteList.getPath();
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#getMountPoints(java.lang.String)
	 */
	public List<String> getMountPoints(String ip) {
		List<String> mountPoints = new ArrayList<String>();
		if (StringUtil.isNotEmpty(ip)) {
			List<WhiteList> whiteLists = whiteListDao.getWhiteListsByIp(ip);
			if (whiteLists != null && !whiteLists.isEmpty()) {
				for (WhiteList whiteList : whiteLists) {
					if (WhiteList.STATUS_MOUNTED == whiteList.getStatus()) {
						mountPoints.add(whiteList.getPath());
					}
				}
				return mountPoints;
			}
		}
		return mountPoints;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#getAll(java.util.Map, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public List<Storage> getAll(Map<String, Object> criteria,
			IPageCond pageCond) {
		return storageDao.getAll(criteria, pageCond);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#add(com.primeton.paas.manage.spi.model.SharedStorage)
	 */
	public void add(Storage sharedStorage) {
		storageDao.insert(sharedStorage);
	}

	/**
	 * 
	 * @param id
	 * @param newWhiteLists
	 * @return
	 * @throws StorageException
	 */
	private boolean modifyStorageWhiteList(String id, List<String> newWhiteLists)
			throws StorageException {
		if (!isIaaSEnable) {
			logger.warn("Can not update storage white list , iaas api has been disabled.");
			return true;
		}

		if (StringUtil.isEmpty(id) || null == newWhiteLists) {
			return false;
		}

		boolean isExecute = true;

		StorageNasVolume storageNasVolume = manager
				.queryStorageNasVolumeDetail(WebServiceUtil.getWsUrl(),
						WebServiceUtil.getUsername(),
						WebServiceUtil.getPassword(), id);
		if (null == storageNasVolume) {
			return false;
		}
		String vWhites = storageNasVolume.getVolumeWhiteList();
		if (StringUtil.isNotEmpty(vWhites)) {
			List<String> vWhiteLists = new ArrayList<String>();
			for (String w : vWhites.split(",")) {
				vWhiteLists.add(w);
			}
			if (vWhiteLists.size() == newWhiteLists.size()) {
				isExecute = false;
				for (String w : newWhiteLists) {
					if (!vWhiteLists.contains(w)) {
						isExecute = true;
						break;
					}
				}
			}
		} else if (newWhiteLists.size() == 0) {
			isExecute = false;
		}

		if (newWhiteLists.size() == 0) {
			newWhiteLists.add("127.0.0.1"); //$NON-NLS-1$
		}

		if (isExecute) {
			boolean rs = manager.modifyStorageNasVolumeWhiteList(
					WebServiceUtil.getWsUrl(), WebServiceUtil.getUsername(),
					WebServiceUtil.getPassword(), id, newWhiteLists);
			if (!rs) {
				String message = "Modify Storage[" + id + "] whiteList["
						+ newWhiteLists + "] error.";
				logger.error(message);
				return false;

			} else {
				logger.info("Modify Storage[" + id + "] whiteList["
						+ newWhiteLists + "] success.");
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#getByName(java.lang.String)
	 */
	public Storage getByName(String name) {
		if (!isIaaSEnable) {
			logger.warn("Get Storage error, because iaas api has been disabled.");
			return null;
		}
		if (StringUtil.isEmpty(name)) {
			return null;
		}
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("name", name); //$NON-NLS-1$
		List<Storage> sList = storageDao.getAll(criteria);
		if (sList != null && sList.size() > 0) {
			Storage storage = sList.get(0);
			StorageNasVolume storageNasVolume = manager
					.queryStorageNasVolumeDetail(WebServiceUtil.getWsUrl(),
							WebServiceUtil.getUsername(),
							WebServiceUtil.getPassword(), storage.getId());
			if (null != storageNasVolume
					&& storageNasVolume.getVolumeID().equals(storage.getId())) {
				return storage;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IStorageManager#modifyStorageSize(java.lang.String, int)
	 */
	public boolean modifyStorageSize(String storageId, int size) {
		logger.info("Submit modify storage [storageId:" + storageId + ", size:"
				+ size + "] request to iaas success.");
		boolean rs = manager.modifyStorageNasVolumeSize(
				WebServiceUtil.getWsUrl(), WebServiceUtil.getUsername(),
				WebServiceUtil.getPassword(), storageId, size);
		if (rs) {
			Storage obj = storageDao.get(storageId);
			obj.setSize(size);
			storageDao.update(obj);
			logger.info("Modify Storage[" + storageId + "] size[" + size
					+ "] success.");
		} else {
			logger.info("Modify Storage[" + storageId + "] size[" + size
					+ "] error.");
		}
		return rs;
	}

}
