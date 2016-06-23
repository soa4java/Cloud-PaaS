/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class Host implements Serializable {
	
	/***serialVersionUID**/
	private static final long serialVersionUID = 4070660921574941330L;
	
	private String ip;
	private String name;
	private String type;
	private Map<String, String> exts = new HashMap<String, String>();
	
	public static final String ASSIGNED = "assigned";
	public static final String ID = "id";
	public static final String PACKAGE_ID = "packageId";
	public static final String STANDALONE = "standalone";
	
	/**
	 * 
	 */
	public Host() {
		super();
	}

	/**
	 * @param ip
	 */
	public Host(String ip) {
		super();
		this.ip = ip;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
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
	 * Exp:[Jetty],[MySQL],... <br>
	 * 
	 * @return
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * @return the types
	 */
	public List<String> getTypes() {
		List<String> list = new ArrayList<String>();
		if (StringUtil.isNotEmpty(this.type)) {
			String[] types = this.type.split(",");
			for (String type : types) {
				type = type.substring(1, type.length()-1);
				list.add(type);
			}
		}
		return list;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(List<String> types) {
		if(types == null || types.isEmpty()) {
			this.type = "";
			return;
		}
		String type = "";
		for (int i = 0; i < types.size(); i++) {
			type += "["+types.get(i)+"]";
			if (i < types.size() - 1) {
				type += ",";
			}
			
		}
		this.type = type;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public boolean addType(String type) {
		if(StringUtil.isEmpty(type)) {
			return false;
		}
		List<String> types = getTypes();
		if (! types.contains(type)) {
			types.add(type);
			setTypes(types);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public boolean removeType(String type) {
		if(StringUtil.isEmpty(type)) {
			return false;
		}
		List<String> types = getTypes();
		if (types.contains(type)) {
			types.remove(type);
			setTypes(types);
			return true;
		}
		return false;
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
	}
	
	/**
	 * 
	 * @return
	 */
	public String getId() {
		String id = exts.get(ID);
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		exts.put(ID, id);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPackageId() {
		return exts.get(PACKAGE_ID);
	}
	
	/**
	 * 
	 * @param packageId
	 */
	public void setPackageId(String packageId) {
		if(StringUtil.isNotEmpty(packageId)) {
			exts.put(PACKAGE_ID, packageId);
		}
	}
	
	public boolean isStandalone() {
		String obj = exts.get(STANDALONE);
		return obj == null ? false : Boolean.parseBoolean(obj);
	}
	
	public void setStandalone(boolean isStandalone) {
		exts.put(STANDALONE, String.valueOf(isStandalone));
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> getExts() {
		return this.exts;
	}
	
	private boolean controlable = false; // VO, not persistence
	
	public boolean isControlable() {
		return controlable;
	}

	public void setControlable(boolean controlable) {
		this.controlable = controlable;
	}

}
