/**
 * 
 */
package com.primeton.paas.cep.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.primeton.paas.cep.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EPSInstance {
	
	public static final String ENABLE = "ENABLE";
	public static final String DISABLE = "DISABLE";
	
	private String id;							// db && zk
	private String enable = DISABLE;			// db && zk
	private String eps = "";					// db && zk
	private String eventName = "";				// db && zk
	private String listeners = "";				// db && zk
	private long createTime;					// db && zk
	private long startTime;						// zk
	
	/**
	 * 
	 */
	public EPSInstance() {
		super();
	}
	
	/**
	 * 
	 * @param eps PreparedEPS
	 */
	public EPSInstance(EPS eps) {
		super();
		this.eps = (eps == null) ? "" : eps.exportStatement();
	}
	
	/**
	 * 
	 * @param id
	 * @param enable
	 * @param eps
	 * @param eventName
	 * @param listeners
	 */
	public EPSInstance(String id, String enable, String eps, String eventName,
			String listeners) {
		super();
		this.id = id;
		this.enable = enable;
		this.eps = eps;
		this.eventName = eventName;
		this.listeners = listeners;
	}
	
	

	/**
	 * 
	 * @param enable
	 * @param eps
	 * @param eventName
	 * @param listeners
	 */
	public EPSInstance(String enable, String eps, String eventName,
			String listeners) {
		super();
		this.enable = enable;
		this.eps = eps;
		this.eventName = eventName;
		this.listeners = listeners;
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
	 * @return the enable
	 */
	public String getEnable() {
		return enable;
	}

	/**
	 * @param enable the enable to set
	 */
	public void setEnable(String enable) {
		this.enable = enable;
	}

	/**
	 * @return the eps
	 */
	public String getEps() {
		return eps;
	}

	/**
	 * @param eps the eps to set
	 */
	public void setEps(String eps) {
		this.eps = eps;
	}

	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return the listeners
	 */
	public String getListeners() {
		return listeners;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> listListeners() {
		List<String> listeners = new ArrayList<String>();
		if (StringUtil.isNotEmpty(this.listeners)) {
			String[] array = this.listeners.split(",");
			if (array != null && array.length > 0) {
				for (String className : array) {
					if (StringUtil.isNotEmpty(className) && !listeners.contains(className)) {
						listeners.add(className);
					}
				}
			}
		}
		return listeners;
	}
	
	/**
	 * 
	 * @param className
	 */
	public void addListener(String className) {
		if (StringUtil.isNotEmpty(className)) {
			List<String> listeners = listListeners();
			if (!listeners.contains(className)) {
				listeners.add(className);
				StringBuffer stringBuffer = new StringBuffer();
				for (String name : listeners) {
					stringBuffer.append(name).append(",");
				}
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
				this.listeners = stringBuffer.toString();
			}
		}
	}
	
	/**
	 * 
	 * @param classNames
	 */
	public <T extends Collection<String>> void addListeners(T classNames) {
		if (classNames == null || classNames.isEmpty()) {
			return;
		}
		List<String> listeners = listListeners();
		for (String className : classNames) {
			if (!listeners.contains(className)) {
				listeners.add(className);
			}
		}
		StringBuffer stringBuffer = new StringBuffer();
		for (String name : listeners) {
			stringBuffer.append(name).append(",");
		}
		if (stringBuffer.length() > 1) {
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		}
		this.listeners = stringBuffer.toString();
	}
	
	/**
	 * @param listeners the listeners to set
	 */
	public void setListeners(String listeners) {
		this.listeners = listeners;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer(super.toString()).append(":")
			.append("[ id:" + id)
			/*
			.append(", status:" + status)
			*/
			.append(", enable:" + enable)
			.append(", eventName:" + eventName)
			.append(", eps:" + eps)
			.append(", listeners:" + listeners)
			.append(", createTime:" + createTime)
			.append(", startTime:" + startTime)
			.append(" ]").toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (EPSInstance.class.getName().equals(obj.getClass().getName())) {
			EPSInstance instance = (EPSInstance)obj;
			return equals(id, instance.getId())
				&& equals(enable, instance.getEnable())
				&& equals(eventName, instance.getEventName())
				&& equals(eps, instance.getEps())
				&& equals(listeners, instance.getListeners());
		}
		return false;
	}
	
	/**
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	private static boolean equals(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}
		return str1 != null && str1.equals(str2);
	}
	
}
