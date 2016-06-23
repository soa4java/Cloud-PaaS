/**
 * 
 */
package com.primeton.paas.manage.spi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class Storage {

	private String id;
	private String name;
	private String path;
	private int size; // GB
	private int isAssigned;
	
	private List<WhiteList> whiteLists;
	
	// EXTS
	private static final String ASSIGNED = "assigned";
	
	private int mountStatus = 0;
	
	public static final int STATUS_ASSIGNED = 1;
	public static final int STATUS_NOTASSIGNED = 0;
	
	public static final String PATH_PREFIX = "/storage/";
	
	private Map<String, String> exts = new HashMap<String, String>();
	
	/**
	 * Default constructor <br>
	 */
	public Storage() {
		super();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isAssigned() {
		String obj = exts.get(ASSIGNED);
		return obj == null ? false : Boolean.parseBoolean(obj);
	}
	
	/**
	 * 
	 * @param isAssigned
	 */
	public void setAssigned(boolean isAssigned) {
		exts.put(ASSIGNED, String.valueOf(isAssigned));
		if(isAssigned){
			this.setIsAssigned(STATUS_ASSIGNED);
		}else{
			this.setIsAssigned(STATUS_NOTASSIGNED);
		}
	}
	

	public int getIsAssigned() {
		return isAssigned;
	}

	public void setIsAssigned(int isAssigned) {
		this.isAssigned = isAssigned;
		if(null == exts.get(ASSIGNED)){
			if(isAssigned == STATUS_ASSIGNED){
				setAssigned(true);
			}else{
				setAssigned(false);
			}
		}else if(!isAssigned() && isAssigned==STATUS_ASSIGNED){
			this.setAssigned(true);
		}else if(isAssigned() && isAssigned!=STATUS_ASSIGNED){
			this.setAssigned(false);
		}
	}

	public List<WhiteList> getWhiteLists() {
		return whiteLists;
	}

	public void setWhiteLists(List<WhiteList> whiteLists) {
		this.whiteLists = whiteLists;
	}
	
	
	public int getMountStatus() {
		return mountStatus;
	}

	public void setMountStatus(int mountStatus) {
		this.mountStatus = mountStatus;
	}

	public String toString() {
		return "id=" + this.id + ",name=" + this.name + ",path=" + this.path + ",size=" + this.size + ",isAssigned=" + this.isAssigned;
	}
	
}
