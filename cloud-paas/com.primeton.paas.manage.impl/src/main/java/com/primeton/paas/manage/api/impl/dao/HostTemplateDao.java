/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.List;
import java.util.UUID;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;

import com.primeton.paas.manage.api.model.HostTemplate;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HostTemplateDao {
	
	private final static String NAMESPACE = "hostTemplateSqlMap";
	
	private static HostTemplateDao DAO = new HostTemplateDao();
	
	private static BaseDao baseDao = BaseDao.getInstance();
	
	/**
	 * 
	 * @return
	 */
	public static HostTemplateDao getDao() {
		return DAO;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	protected String getSqlMap(String id) {
		return NAMESPACE + "." + id;
	}
	
	/**
	 * 
	 * @param templateId
	 * @return
	 * @throws DaoException
	 */
	public HostTemplate getTemplate(String templateId) throws DaoException {
		if (StringUtil.isEmpty(templateId)) {
			return null;
		}
		return getBaseDao().queryForObject(getSqlMap("getTemplate"), templateId); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param template
	 * @throws DaoException
	 */
	public void add(HostTemplate template) throws DaoException {
		if (null == template) {
			return;
		}
		if (StringUtil.isEmpty(template.getTemplateId())) {
			template.setTemplateId(UUID.randomUUID().toString());
		}
		getBaseDao().insert(getSqlMap("addTemplate"), template); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param template
	 * @throws DaoException
	 */
	public void modify(HostTemplate template) throws DaoException {
		if (null == template || StringUtil.isEmpty(template.getTemplateId())) {
			return;
		}
		getBaseDao().update(getSqlMap("modifyTemplate"),template); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param template
	 * @return
	 * @throws DaoException
	 */
	public List<HostTemplate> getTemplates(HostTemplate template) throws DaoException {
		if (null == template) {
			template = new HostTemplate();
		}
		return getBaseDao().queryForList(getSqlMap("getTemplates"), template); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param templateId
	 * @throws DaoException
	 */
	public void deleteTemplate(String templateId) throws DaoException {
		if (StringUtil.isEmpty(templateId)) {
			return;
		}
		getBaseDao().delete(getSqlMap("deleteTemplate"), templateId);
	}
	

	/**
	 * 
	 * @return
	 */
	protected static BaseDao getBaseDao() {
		return baseDao = (null == baseDao) ? BaseDao.getInstance() : baseDao;
	}

}
