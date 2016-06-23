/**
 * 
 */
package com.primeton.paas.openapi.admin.config;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 * @param <M>
 */
public interface IConfigHandler<M extends IConfigModel> {
	
	/**
	 * 
	 * @return
	 */
	public M doLoad();
	
	 /**
	  * 
	  * @param model
	  * @param itemIds
	  */
	public void deleteItems(M model,String[] itemIds);

	/**
	 * 
	 * @param model
	 */
	public void updateModel(M model);
	
} 
