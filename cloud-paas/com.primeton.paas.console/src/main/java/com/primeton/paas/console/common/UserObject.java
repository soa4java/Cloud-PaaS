/**
 * 
 */
package com.primeton.paas.console.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author liming(li-ming@primeton.com)
 *
 */
public class UserObject implements IUserObject, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String userName;
	private String userMail;
	private String userRealName;
	private String userOrgId;
	private String userOrgName;
	private String userRemoteIP;
	private String uniqueId;
	private String sessionId;
	private Map<String, Object> attributes;

	public UserObject() {
		attributes = new HashMap<String, Object>();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserOrgId() {
		return userOrgId;
	}

	public void setUserOrgId(String userOrgId) {
		this.userOrgId = userOrgId;
	}

	public String getUserOrgName() {
		return userOrgName;
	}

	public void setUserOrgName(String userOrgName) {
		this.userOrgName = userOrgName;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getUserRemoteIP() {
		return userRemoteIP;
	}

	public void setUserRemoteIP(String userRemoteIP) {
		this.userRemoteIP = userRemoteIP;
	}

	public Object clone() throws CloneNotSupportedException {
		UserObject user = (UserObject) super.clone();
		Map<String, Object> clonedAttributes = new HashMap<String, Object>();
		clonedAttributes.putAll(getAttributes());
		user.setAttributes(clonedAttributes);
		return user;
	}

	public UserObject shallowClone() {
		UserObject cloned = new UserObject();
		cloned.setSessionId(getSessionId());
		cloned.setUniqueId(getUniqueId());
		cloned.setUserId(getUserId());
		cloned.setUserMail(getUserMail());
		cloned.setUserName(getUserName());
		cloned.setUserOrgId(getUserOrgId());
		cloned.setUserOrgName(getUserOrgName());
		cloned.setUserRealName(getUserRealName());
		cloned.setUserRemoteIP(getUserRemoteIP());
		return cloned;
	}

	public boolean contains(String key) {
		return attributes.containsKey(key);
	}

	public Object get(String key) {
		return attributes.get(key);
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void put(String key, Object value) {
		attributes.put(key, value);
	}

	public Object remove(String key) {
		return attributes.remove(key);
	}

	public void setAttributes(Map<String, Object> attributes) {
		if (attributes == null) {
			return;
		} else {
			this.attributes = attributes;
			return;
		}
	}

	public void clear() {
		attributes.clear();
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}