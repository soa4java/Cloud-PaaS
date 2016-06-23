/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface  ICluster extends Serializable {

	/**
	 * 
	 * @return
	 */
	String getId();
	
	/**
	 * 
	 * @param id
	 */
	void setId(String id);
	
	/**
	 * 
	 * @return
	 */
	String getName();
	
	/**
	 * 
	 * @param name
	 */
	void setName(String name);
	
	/**
	 * 
	 * @return
	 */
	String getType();
	
	/**
	 * 
	 * @return
	 */
	int getMaxSize();
	
	/**
	 * 
	 * @param maxSize
	 */
	void setMaxSize(int maxSize);
	
	/**
	 * 
	 * @return
	 */
	int getMinSize();
	
	/**
	 * 
	 * @param minSize
	 */
	void setMinSize(int minSize);
	
	/**
	 * 
	 * @return
	 */
	String getOwner();
	
	/**
	 * 
	 * @param owner
	 */
	void setOwner(String owner);
	
	/**
	 * 
	 * @return
	 */
	String getDesc();
	
	/**
	 * 
	 * @param desc
	 */
	void setDesc(String desc);
	
	/**
	 * 
	 * @return
	 */
	Map<String, String> getAttributes();
	
}
