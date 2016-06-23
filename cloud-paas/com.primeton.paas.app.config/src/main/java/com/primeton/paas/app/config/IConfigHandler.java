/**
 * 
 */
package com.primeton.paas.app.config;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 * @param <M> 配置模型
 */
public interface IConfigHandler<M extends IConfigModel> {
	
	/**
	 * 加载配置模型
	 * 
	 * @return 配置模型
	 */
	public M doLoad();
	
	 /**
	  * 删除配置项. <br>
	  * 
	  * @param model 配置模型
	  * @param itemIds 项目集合
	  */
	public void deleteItems(M model,String[] itemIds);

	/**
	 * 更新配置模型
	 * 
	 * @param model 配置模型
	 */
	public void updateModel(M model);
	
} 
