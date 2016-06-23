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
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultAppEPSRelDao implements AppEPSRelDao {
	
	private static final String NAMESPACE = "appEPSRelSqlMap";
	
	private static BaseDao baseDao = BaseDao.getInstance();
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultAppEPSRelDao.class);
	
	private static String getStatementName(String id) {
		return NAMESPACE + "." + id;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.AppRuleRelDao#add(com.primeton.paas.manage.api.impl.monitor.AppRuleRel)
	 */
	public boolean add(AppEPSRel rel) {
		if (rel == null || StringUtil.isEmpty(rel.getAppName())) {
			return false;
		}
		boolean existed = get(rel.getAppName()) != null;
		if (existed) {
			logger.warn("AppEPSRel {" + rel.getAppName() + "} is already existed.");
			return false;
		}
		try {
			baseDao.insert(getStatementName("insert"), rel.getMetadata()); //$NON-NLS-1$
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.AppRuleRelDao#delete(java.lang.String)
	 */
	public boolean delete(String appName) {
		if (StringUtil.isNotEmpty(appName)) {
			boolean existed = get(appName) != null;
			if (existed) {
				Map<String, Object> args = new HashMap<String, Object>();
				args.put("app_name", appName); //$NON-NLS-1$
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

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.AppRuleRelDao#update(com.primeton.paas.manage.api.impl.monitor.AppRuleRel)
	 */
	public boolean update(AppEPSRel rel) {
		if (rel == null || StringUtil.isEmpty(rel.getAppName())) {
			return false;
		}
		boolean existed = get(rel.getAppName()) != null;
		if (!existed) {
			logger.warn("AppEPSRel {" + rel.getAppName() + "} is not found.");
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

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.AppRuleRelDao#get(java.lang.String)
	 */
	public AppEPSRel get(String appName) {
		if (StringUtil.isNotEmpty(appName)) {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("app_name", appName); //$NON-NLS-1$
			try {
				Map<String, Object> rs = baseDao.queryForObject(getStatementName("select"), args); //$NON-NLS-1$
				if (rs != null) {
					AppEPSRel rel = new AppEPSRel();
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
