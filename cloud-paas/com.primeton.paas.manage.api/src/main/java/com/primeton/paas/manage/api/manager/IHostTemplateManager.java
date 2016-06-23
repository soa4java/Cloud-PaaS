/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.manager;

import java.util.List;

import com.primeton.paas.manage.api.model.HostTemplate;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IHostTemplateManager {
	
	/**
	 * 保存套餐模板. <br>
	 * 
	 * @param template 模板
	 * @return true:success
	 */
	boolean save(HostTemplate template);
	
	/**
	 * 删除套餐模板. <br>
	 * 
	 * @param templateId 模板标识
	 * @return true:success
	 */
	boolean remove(String templateId);
	
	/**
	 * 获取套餐模板. <br>
	 * 
	 * @param templateId 模板标识
	 * @return 模板
	 */
	HostTemplate getTemplate(String templateId);
	
	/**
	 * 获取套餐模板. <br>
	 * 
	 * @param template 模板(if null then return all)
	 * @return 套餐模板集合
	 */
	List<HostTemplate> getTemplates(HostTemplate template);
	
}
