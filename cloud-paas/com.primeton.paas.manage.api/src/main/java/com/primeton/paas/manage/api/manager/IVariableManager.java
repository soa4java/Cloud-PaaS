/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import java.util.Date;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.Variable;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IVariableManager {

	/**
	 * 
	 * @param var
	 */
	void save(Variable var);
	
	/**
	 * 
	 * @param key
	 */
	void remove(String key);
	
	/**
	 * 
	 */
	void clear();
	

	/**
	 * 
	 * @param key
	 * @return
	 */
	Variable get(String key);
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	String getStringValue(String key, String defaultValue);
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	long getLongValue(String key, long defaultValue);
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	int getIntValue(String key, int defaultValue);
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	byte getByteValue(String key, byte defaultValue);
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	boolean getBoolValue(String key, boolean defaultValue);
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	Date getDateValue(String key, Date defaultValue);
	
	/**
	 * 
	 * @param key
	 * @param reg
	 * @param defaultValue
	 * @return
	 */
	Date getDateValue(String key, String reg, Date defaultValue);
	
	/**
	 * 
	 * @return
	 */
	Variable[] getAll();
	
	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 */
	Variable[] get(Variable criteria, IPageCond page);
	
	/**
	 * clean buffer
	 */
	void refresh();
	
	/**
	 * clean buffer
	 * 
	 * @param key
	 */
	void refresh(String key);
	
}
