/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * PAAS Service Model. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IService extends Serializable {

	int STATE_RUNNING = 1;
	int STATE_NOT_RUNNING = 0;
	
	String HA_MODE_MASTER = "MASTER";
	String HA_MODE_SLAVE = "SLAVE";
	
	String HA_MODE_CLUSTER = "CLUSTER";
	String HA_MODE_BLOCK = "BLOCK";
	
	String HA_MODE_NONE = "none";
	
	String MODE_LOGIC = "LOGIC";
	String MODE_PHYSICAL = "PHYSICAL";
	
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
	String getParentId();
	
	/**
	 * 
	 * @param parentId
	 */
	void setParentId(String parentId);
	
	/**
	 * 
	 * @return
	 */
	String getIp();
	
	/**
	 * 
	 * @param ip
	 */
	void setIp(String ip);
	
	/**
	 * 
	 * @return
	 */
	String getType();
	
	/**
	 * 
	 * @return
	 */
	int getPort();
	
	/**
	 * 
	 * @param port
	 */
	void setPort(int port);
	
	/**
	 * 
	 * @return
	 */
	int getState();
	
	/**
	 * 
	 * @param state
	 */
	void setState(int state);
	
	/**
	 * 
	 * @return
	 */
	int getPid();
	
	/**
	 * 
	 * @param pid
	 */
	void setPid(int pid);
	
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
	String getCreatedBy();
	
	/**
	 * 
	 * @param createdBy
	 */
	void setCreatedBy(String createdBy);
	
	/**
	 * 
	 * @return
	 */
	Date getCreatedDate();
	
	/**
	 * 
	 * @param createdDate
	 */
	void setCreatedDate(Date createdDate);

	/**
	 * 
	 * @return
	 */
	Date getStartDate();
	
	/**
	 * 
	 * @param startDate
	 */
	void setStartDate(Date startDate);
	
	/**
	 * 
	 * @return
	 */
	Map<String, String> getAttributes();
	
	/**
	 * 
	 * @return
	 */
	boolean isStandalone();
	
	/**
	 * 
	 * @return
	 */
	String getPackageId();
	
	/**
	 * 
	 * @return
	 */
	String getHaMode();
	
	/**
	 * PHYSICAL | LOGIC <br>
	 * 
	 * @return PHYSICAL | LOGIC
	 */
	String getMode();
	
}
