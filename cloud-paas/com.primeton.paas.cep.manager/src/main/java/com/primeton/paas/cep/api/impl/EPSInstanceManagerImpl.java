/**
 * 
 */
package com.primeton.paas.cep.api.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.cep.api.EPSException;
import com.primeton.paas.cep.api.EPSInitException;
import com.primeton.paas.cep.api.EPSInstanceManager;
import com.primeton.paas.cep.model.EPSInstance;
import com.primeton.paas.cep.spi.EPSInstanceDao;
import com.primeton.paas.cep.spi.EPSInstanceDaoFactory;
import com.primeton.paas.cep.spi.TableIdDao;
import com.primeton.paas.cep.spi.TableIdDaoFactory;
import com.primeton.paas.cep.util.PathUtil;
import com.primeton.paas.cep.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EPSInstanceManagerImpl implements EPSInstanceManager, Runnable {
	
	private static final String TABLE = "cep_eps_instance";
	
	private boolean init;
	private EPSInstanceDao epsDao;
	private TableIdDao tableIdDao;
	
	private static ILogger logger = LoggerFactory.getLogger(EPSInstanceManagerImpl.class); 
	
	/**
	 * 
	 */
	public EPSInstanceManagerImpl() {
		super();
		epsDao = EPSInstanceDaoFactory.getDao();
		tableIdDao = TableIdDaoFactory.getDao();
		
		Thread thread = new Thread(this);
		thread.setName("EPS-sync-Thread");
		thread.setDaemon(true);
		thread.start();
	}

	private String getNewId() {
		try {
			return tableIdDao.getNewId(TABLE) + "";
		} catch (SQLException e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#init()
	 */
	public synchronized void init() throws EPSInitException {
		if (init) {
			logger.warn("On database and zookeeper EPS instances have been synchronized.");
			return;
		}
		long begin = System.currentTimeMillis();
		logger.info("Begin synchronize EPS instances [database with zookeeper].");
		List<EPSInstance> zk = PathUtil.getEPSs();
		for (EPSInstance instance : zk) {
			PathUtil.deleteEPS(instance.getId());
		}
		List<EPSInstance> instances = getAll();
		for (EPSInstance instance : instances) {
			PathUtil.setEPS(instance);
		}
		long end = System.currentTimeMillis();
		logger.info("End synchronize EPS instances [database with zookeeper]. Time spent " + (end-begin)/1000L + " seconds.");
		init = true;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#add(com.primeton.paas.cep.model.EPSInstance)
	 */
	public EPSInstance register(EPSInstance instance) throws EPSException {
		if (instance == null) {
			return null;
		}
		if (StringUtil.isEmpty(instance.getId())) {
			instance.setId(getNewId());
		}
		instance.setCreateTime(System.currentTimeMillis());
		try {
			epsDao.add(instance);
			PathUtil.setEPS(instance);
		} catch (Exception e) {
			throw new EPSException(e);
		}
		logger.info("Register " + instance + " success.");
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#update(com.primeton.paas.cep.model.EPSInstance)
	 */
	public void update(EPSInstance instance) throws EPSException {
		if (instance == null || StringUtil.isEmpty(instance.getId())) {
			return;
		}
		if (null == get(instance.getId())) {
			throw new EPSException("EPSInstance [" + instance.getId() + "] not found.");
		}
		try {
			PathUtil.setEPS(instance);
			epsDao.update(instance);
		} catch (Exception e) {
			throw new EPSException(e);
		}
		logger.info("Update " + instance + " success.");
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#enable(java.lang.String)
	 */
	public void enable(String id) throws EPSException {
		if (StringUtil.isNotEmpty(id)) {
			EPSInstance instance = get(id);
			if (instance == null) {
				throw new EPSException("EPSInstance [" + id + "] not found.");
			}
			instance.setEnable(EPSInstance.ENABLE);
			update(instance);
			logger.info("Enable EPSInstance [" + id + "]");
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#disable(java.lang.String)
	 */
	public void disable(String id) throws EPSException {
		if (StringUtil.isNotEmpty(id)) {
			EPSInstance instance = get(id);
			if (instance == null) {
				throw new EPSException("EPSInstance [" + id + "] not found.");
			}
			instance.setEnable(EPSInstance.DISABLE);
			update(instance);
			logger.info("Disable EPSInstance [" + id + "]");
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#destroy(java.lang.String)
	 */
	public void unregister(String id) throws EPSException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		if (null == get(id)) {
			logger.warn("EPSInstance [" + id + "] not found.");
			return;
		}
		try {
			PathUtil.deleteEPS(id);
			epsDao.delete(id);
		} catch (Exception e) {
			throw new EPSException(e);
		}
		logger.info("Unregister EPSInstance [" + id + "] success.");
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#get(java.lang.String)
	 */
	public EPSInstance get(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			EPSInstance instance = epsDao.get(id);
			EPSInstance zk = PathUtil.getEPS(id);
			if (zk != null) {
				instance.setStartTime(zk.getStartTime());
			} else {
				PathUtil.setEPS(instance);
			}
			return instance;
		} catch (SQLException e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EPSInstanceManager#getAll()
	 */
	public List<EPSInstance> getAll() {
		try {
			List<EPSInstance> instances = epsDao.getAll();
			if (instances != null) {
				for (EPSInstance instance : instances) {
					EPSInstance zk = PathUtil.getEPS(instance.getId());
					if (zk != null) {
						instance.setStartTime(zk.getStartTime());
					} else {
						logger.warn("EPSInstance node [" + PathUtil.getEPSPath(instance.getId()) + "] not found.");
						PathUtil.setEPS(instance);
					}
				}
				return instances;
			}
		} catch (SQLException e) {
			logger.error(e);
		}
		return new ArrayList<EPSInstance>();
	}
	
	/**
	 * DB & zookeeper sync <br>
	 */
	private void syncEPS() {
		long begin = System.currentTimeMillis();
		logger.info("Start synchronize zookeeper and database EPS instances.");
		List<EPSInstance> dbEPSs = getAll();
		List<EPSInstance> zkEPSs = PathUtil.getEPSs();
		
		logger.info("Begin scan zookeeper EPS instances.");
		for (EPSInstance instance : zkEPSs) {
			if (instance == null 
					|| StringUtil.isEmpty(instance.getId())) {
				continue;
			} else {
				EPSInstance eps = null; // DB
				for (EPSInstance e : dbEPSs) {
					if (e != null && e.getId() != null 
							&& e.getId().equals(instance.getId())) {
						eps = e;
						break;
					}
				}
				if (eps == null) {
					PathUtil.deleteEPS(instance.getId());
				} else {
					if (!eps.equals(instance)) {
						PathUtil.setEPS(eps);
					}
				}
			}
		}
		logger.info("End scan zookeeper EPS instances.");
		
		logger.info("Begin scan database EPS instances.");
		zkEPSs = PathUtil.getEPSs();
		List<String> epsIds = new ArrayList<String>();
		for (EPSInstance instance : zkEPSs) {
			if (instance != null && StringUtil.isNotEmpty(instance.getId())) {
				epsIds.add(instance.getId());
			}
		}
		for (EPSInstance instance : dbEPSs) {
			if (instance != null) {
				if (!epsIds.contains(instance.getId())) {
					PathUtil.setEPS(instance);
				}
			}
		}
		logger.info("End scan database EPS instances.");
		
		long end = System.currentTimeMillis();
		logger.info("End synchronize zookeeper and database EPS instances, time spent " + (end-begin)/1000L + " seconds.");
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.info("Start EPS instance synchronize thread success.");
		
		ThreadUtil.sleep(60000L);
		while (true) {
			try {
				syncEPS();
				
				Thread.sleep(300000L); // 300s
			} catch (Throwable t) {
				logger.warn(t.getMessage());
			}
		}
	}

}
