package com.primeton.paas.manage.api.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class CapPartyauth implements Serializable {
	
	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = 272982516072796884L;
	
	private String tenantId;
	private String roleId;
	private String roleType;
	private String partyId;
	private String partyType;
	private String createuser;
	private Date createtime;
	
	/**
	 * 
	 */
	public CapPartyauth() {
		super();
	}
	
	/**
	 * 
	 * @param tenantId
	 * @param roleId
	 * @param roleType
	 * @param partyId
	 * @param partyType
	 * @param createuser
	 * @param createtime
	 */
	public CapPartyauth(String tenantId, String roleId, String roleType, String partyId,
			String partyType, String createuser, Date createtime) {
		super();
		this.tenantId = tenantId;
		this.roleId = roleId;
		this.roleType = roleType;
		this.partyId = partyId;
		this.partyType = partyType;
		this.createuser = createuser;
		this.createtime = createtime;
	}
	
	public String getTenantId() {
		return tenantId;
	}
	
	public String getRoleType() {
		return roleType;
	}
	
	public String getRoleId() {
		return roleId;
	}
	
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getPartyId() {
		return partyId;
	}
	
	public String getPartyType() {
		return partyType;
	}
	
	public String getCreateuser() {
		return createuser;
	}
	
	public Date getCreatetime() {
		return createtime;
	}
	
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	
	public void setPartyType(String partyType) {
		this.partyType = partyType;
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
		return "CapPartyauth [tenantId=" + tenantId + ", roleId=" + roleId
				+ ", roleType=" + roleType + ", partyId=" + partyId
				+ ", partyType=" + partyType + ", createuser=" + createuser
				+ ", createtime=" + createtime + "]";
	}
	
}