/**
 *
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;

import com.primeton.paas.manage.api.model.HostTemplate;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.model.VmPoolConfig;

/**
 * 
 * @author liming(mailto:li-ming@primeton.com)
 */
public class VmPoolConfigDao {

	private final static String USER_SQL_MAP = "vmPoolConfigSqlMap";

	private static VmPoolConfigDao instance = new VmPoolConfigDao();

	private static BaseDao baseDao = BaseDao.getInstance();;
	
	private static HostTemplateDao templateDao = HostTemplateDao.getDao();

	private VmPoolConfigDao() {
	}

	public static VmPoolConfigDao getInstance() {
		return instance;
	}

	private String getSqlMap(String sqlId) {
		return USER_SQL_MAP + "." + sqlId;
	}

	public void addVmPoolConfig(VmPoolConfig vmPoolConfig) throws DaoException {
		baseDao.insert(getSqlMap("addVmPoolConfig"), vmPoolConfig); //$NON-NLS-1$
	}

	public int getVmPoolConfigCount(Map<String, Object> criteria) throws DaoException {
		if (criteria == null)
			criteria = new HashMap<String, Object>();
		Integer num = (Integer) baseDao.queryForObject(
				getSqlMap("getVmPoolConfigCount"), criteria); //$NON-NLS-1$
		return null == num ? -1 : num;
	}

	public VmPoolConfig getVmPoolConfig(String id)
			throws DaoException {
		VmPoolConfig vmPoolConfig = baseDao.queryForObject(getSqlMap("getVmPoolConfig"), id); //$NON-NLS-1$
		if (vmPoolConfig != null) {
			HostTemplate template = templateDao.getTemplate(vmPoolConfig.getId());
			vmPoolConfig.setHostTemplate(template);
		}
		return vmPoolConfig;
	}

	public List<VmPoolConfig> getVmPoolConfigs(Map<String, Object> criteria)
			throws DaoException {
		List<VmPoolConfig> configs = baseDao.queryForList(getSqlMap("getVmPoolConfigs")
				, null == criteria ? new HashMap<String, Object>() : criteria);
		configs = null == configs ? new ArrayList<VmPoolConfig>() : configs;
		for (VmPoolConfig vmPoolConfig : configs) {
			HostTemplate template = templateDao.getTemplate(vmPoolConfig.getId());
			vmPoolConfig.setHostTemplate(template);
		}
		return configs;
	}

	public List<VmPoolConfig> getVmPoolConfigs(Map<String, Object> criteria, IPageCond pageCond)
			throws DaoException {
		List<VmPoolConfig> vmPoolConfigs = baseDao.queryForList(getSqlMap("getVmPoolConfigs") //$NON-NLS-1$
				, null == criteria ? new HashMap<String, Object>() : criteria
				, null == pageCond ? new PageCond() : pageCond);
		if (vmPoolConfigs != null && vmPoolConfigs.size() > 0) {
			for (VmPoolConfig v : vmPoolConfigs) {
				if (v != null) {
					HostTemplate template = templateDao.getTemplate(v.getId());
					v.setHostTemplate(template);
				}
			}
		}
		return vmPoolConfigs;
	}
	
	public void updateVmPoolConfig(VmPoolConfig vmPoolConfig) throws DaoException {
		if (null == vmPoolConfig) {
			return;
		}
		baseDao.update(getSqlMap("updateVmPoolConfig"), vmPoolConfig); //$NON-NLS-1$
	}
	
	public void delVmPoolConfig(String id) throws DaoException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		baseDao.delete(getSqlMap("delVmPoolConfig"), id); //$NON-NLS-1$
	}

}
