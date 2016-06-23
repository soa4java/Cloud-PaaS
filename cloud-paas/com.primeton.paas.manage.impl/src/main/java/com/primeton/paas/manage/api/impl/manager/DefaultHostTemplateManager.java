/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.HostTemplateDao;
import com.primeton.paas.manage.api.manager.IHostTemplateManager;
import com.primeton.paas.manage.api.model.HostTemplate;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultHostTemplateManager implements IHostTemplateManager {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultHostTemplateManager.class);
	
	private static HostTemplateDao templateDao = HostTemplateDao.getDao();

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostTemplateManager#save(com.primeton.paas.manage.api.model.HostTemplate)
	 */
	public boolean save(HostTemplate template) {
		if (null == template) {
			return false;
		}
		try {
			if (null == getTemplateDao().getTemplate(template.getTemplateId())) {
				getTemplateDao().add(template);
			} else {
				getTemplateDao().modify(template);
			}
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostTemplateManager#remove(java.lang.String)
	 */
	public boolean remove(String templateId) {
		if (StringUtil.isEmpty(templateId)) {
			return false;
		}
		try {
			getTemplateDao().deleteTemplate(templateId);
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostTemplateManager#getTemplate(java.lang.String)
	 */
	public HostTemplate getTemplate(String templateId) {
		if (StringUtil.isEmpty(templateId)) {
			return null;
		}
		try {
			return getTemplateDao().getTemplate(templateId);
		} catch (DaoException e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IHostTemplateManager#getTemplates(com.primeton.paas.manage.api.model.HostTemplate)
	 */
	public List<HostTemplate> getTemplates(HostTemplate template) {
		List<HostTemplate> templates = null;
		try {
			templates = getTemplateDao().getTemplates(template);
		} catch (DaoException e) {
			logger.error(e);
		}
		return null == templates ? new ArrayList<HostTemplate>() : templates;
	}

	/**
	 * 
	 * @return
	 */
	protected static HostTemplateDao getTemplateDao() {
		return templateDao = (null == templateDao) ? HostTemplateDao.getDao() : templateDao;
	}
	
	

}
