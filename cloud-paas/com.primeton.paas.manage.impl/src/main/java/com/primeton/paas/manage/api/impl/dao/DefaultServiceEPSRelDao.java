/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.HashMap;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultServiceEPSRelDao implements ServiceEPSRelDao {

	private static final String NAMESPACE = "serviceEPSRelSqlMap";

	private static BaseDao baseDao = BaseDao.getInstance();
	private static ILogger logger = ManageLoggerFactory
			.getLogger(DefaultServiceEPSRelDao.class);

	private static String getStatementName(String id) {
		return NAMESPACE + "." + id;
	}

	public boolean add(ServiceEPSRel rel) {
		if (rel == null || StringUtil.isEmpty(rel.getClusterId())) {
			return false;
		}
		boolean existed = get(rel.getClusterId()) != null;
		if (existed) {
			logger.warn("ServiceEPSRel {" + rel.getClusterId()
					+ "} is already existed.");
			return false;
		}
		try {
			baseDao.insert(getStatementName("insert"), rel.getMetadata()); //$NON-NLS-1$
			return true;
		} catch (DaoException e) {
		}
		return false;
	}

	public boolean delete(String clusterId) {
		if (StringUtil.isNotEmpty(clusterId)) {
			boolean existed = get(clusterId) != null;
			if (existed) {
				Map<String, Object> args = new HashMap<String, Object>();
				args.put("cluster_id", clusterId); //$NON-NLS-1$
				try {
					baseDao.delete(getStatementName("delete"), args); //$NON-NLS-1$
					return true;
				} catch (DaoException e) {
					logger.error(e);
				}
			}
		}
		return false;
	}

	public boolean update(ServiceEPSRel rel) {
		if (rel == null || StringUtil.isEmpty(rel.getClusterId())) {
			return false;
		}
		boolean existed = get(rel.getClusterId()) != null;
		if (!existed) {
			logger.warn("ServiceEPSRel clusterId:{" + rel.getClusterId()
					+ "} is not found.");
			return false;
		}
		try {
			baseDao.update(getStatementName("update"), rel.getMetadata()); //$NON-NLS-1$
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}

	public ServiceEPSRel get(String clusterId) {
		if (StringUtil.isNotEmpty(clusterId)) {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("cluster_id", clusterId); //$NON-NLS-1$
			try {
				Map<String, Object> rs = baseDao.queryForObject(
						getStatementName("select"), args); //$NON-NLS-1$
				if (rs != null) {
					ServiceEPSRel rel = new ServiceEPSRel();
					rel.getMetadata().putAll(rs);
					return rel;
				}
			} catch (DaoException e) {
				logger.error(e);
			}
		}
		return null;
	}

}
