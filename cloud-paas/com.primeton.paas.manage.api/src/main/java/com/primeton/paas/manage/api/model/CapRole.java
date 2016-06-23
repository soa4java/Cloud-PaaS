/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class CapRole implements Serializable {
	
	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = -2016702102771551791L;
	private String roleId;
	private String tenantId;
	private String roleCode;
	private String roleName;
	private String roleDesc;
	private String createuser;
	private Date createtime;
	
	/**
	 * 
	 */
	public CapRole() {
		super();
	}
	
	/**
	 * 
	 * @param roleId
	 * @param tenantId
	 * @param roleCode
	 * @param roleName
	 * @param roleDesc
	 * @param createuser
	 * @param createtime
	 */
	public CapRole(String roleId, String tenantId, String roleCode,
			String roleName, String roleDesc, String createuser, Date createtime) {
		super();
		this.roleId = roleId;
		this.tenantId = tenantId;
		this.roleCode = roleCode;
		this.roleName = roleName;
		this.roleDesc = roleDesc;
		this.createuser = createuser;
		this.createtime = createtime;
	}
	
	public String getRoleId() {
		return roleId;
	}
	
	public String getTenantId() {
		return tenantId;
	}
	
	public String getRoleCode() {
		return roleCode;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public String getRoleDesc() {
		return roleDesc;
	}
	
	public String getCreateuser() {
		return createuser;
	}
	
	public Date getCreatetime() {
		return createtime;
	}
	
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "CapRole [roleId=" + roleId + ", tenantId=" + tenantId
				+ ", roleCode=" + roleCode + ", roleName=" + roleName
				+ ", roleDesc=" + roleDesc + ", createuser=" + createuser
				+ ", createtime=" + createtime + "]";
	}
	
}