/**
 * 
 */
package com.primeton.paas.manage.api.impl.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.VmPoolConfigDao;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.model.VmPoolConfig;
import com.primeton.paas.manage.spi.resource.IVmPoolConfig;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VmPoolConfigImpl implements IVmPoolConfig {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(VmPoolConfigImpl.class);
	private static VmPoolConfigDao vmPoolConfigDao = VmPoolConfigDao.getInstance();

	public VmPoolConfigImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmPoolConfigManager#add(com.primeton.paas.manage.spi.resource.VmPoolConfig)
	 */
	public boolean add(VmPoolConfig cfg) {
		if (null == cfg) {
			return false;
		}
		try {
			vmPoolConfigDao.addVmPoolConfig(cfg);
			if (VmPoolConfig.ENABLE == cfg.getIsEnable()) {
				HostResourceMonitor monitor = new HostResourceMonitor(
						cfg.getId(), cfg);
				HostResourceMonitorManager.register(monitor);
			}
			return true;
		} catch (Throwable t) {
			logger.error(t);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmPoolConfigManager#update(com.primeton.paas.manage.spi.resource.VmPoolConfig)
	 */
	public boolean update(VmPoolConfig cfg) {
		if (cfg == null || StringUtil.isEmpty(cfg.getId())) {
			return false;
		}
		try {
			vmPoolConfigDao.updateVmPoolConfig(cfg);
			HostResourceMonitorManager.unregister(cfg.getId());
			if (VmPoolConfig.ENABLE == cfg.getIsEnable()) {
				HostResourceMonitor monitor = new HostResourceMonitor(
						cfg.getId(), cfg);
				HostResourceMonitorManager.register(monitor);
			}
			return true;
		} catch (Throwable t) {
			logger.error(t);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmPoolConfigManager#delete(java.lang.String)
	 */
	public void delete(String id) {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		VmPoolConfig vmPoolConfig = get(id);
		if (vmPoolConfig == null)
			return;
		try {
			vmPoolConfigDao.delVmPoolConfig(id);
			HostResourceMonitorManager.unregister(id);
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmPoolConfigManager#get(java.lang.String)
	 */
	public VmPoolConfig get(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			return vmPoolConfigDao.getVmPoolConfig(id);
		} catch (Throwable t) {
			logger.error(t);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmPoolConfigManager#getAll(com.primeton.paas.manage.api.model.IPageCond)
	 */
	public List<VmPoolConfig> getAll(IPageCond pageCond) {
		List<VmPoolConfig> configs = new ArrayList<VmPoolConfig>();
		try {
			if (pageCond == null) {
				configs = vmPoolConfigDao
						.getVmPoolConfigs(new HashMap<String, Object>());
			} else {
				if (pageCond.getCount() <= 0) {
					pageCond.setCount(vmPoolConfigDao
							.getVmPoolConfigCount(new HashMap<String, Object>()));
				}
				configs = vmPoolConfigDao.getVmPoolConfigs(
						new HashMap<String, Object>(), pageCond);
			}

		} catch (Throwable t) {
			logger.error(t);
		}
		return configs;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmPoolConfigManager#getAll(com.primeton.paas.manage.api.model.IPageCond)
	 */
	public List<VmPoolConfig> getAll(String id, String name, IPageCond pageCond) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("id", id); //$NON-NLS-1$
		criteria.put("name", name); //$NON-NLS-1$
		List<VmPoolConfig> configs = new ArrayList<VmPoolConfig>();
		try {
			if (pageCond == null) {
				configs = vmPoolConfigDao.getVmPoolConfigs(criteria);
			} else {
				if (pageCond.getCount() <= 0) {
					pageCond.setCount(vmPoolConfigDao
							.getVmPoolConfigCount(criteria));
				}
				configs = vmPoolConfigDao.getVmPoolConfigs(criteria, pageCond);
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return null == configs ? new ArrayList<VmPoolConfig>() : configs;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmPoolConfigManager#getAllEnabled()
	 */
	public List<VmPoolConfig> getAllEnabled() {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("isEnable", VmPoolConfig.ENABLE); //$NON-NLS-1$
		List<VmPoolConfig> list = null;
		try {
			list = vmPoolConfigDao.getVmPoolConfigs(criteria);
		} catch (Throwable t) {
			logger.error(t);
		}
		return list == null ? new ArrayList<VmPoolConfig>() : list;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmPoolConfigManager#getAllDisabled()
	 */
	public List<VmPoolConfig> getAllDisabled() {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("isEnable", VmPoolConfig.DISABLE); //$NON-NLS-1$
		List<VmPoolConfig> list = null;
		try {
			list = vmPoolConfigDao.getVmPoolConfigs(criteria);
		} catch (Throwable t) {
			logger.error(t);
		}
		return list == null ? new ArrayList<VmPoolConfig>() : list;
	}

}
