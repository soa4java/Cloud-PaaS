package com.primeton.paas.manage.api.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class CapResauth implements Serializable {
	
	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = -8517990247766241430L;
	private String tenantId;
	private String partyId;
	private String partyType;
	private String resId;
	private String resType;
	private String resState;
	private String partyScope;
	private String createuser;
	private Date createtime;
	
	/**
	 * 
	 */
	public CapResauth() {
		super();
	}
	
	/**
	 * 
	 * @param tenantId
	 * @param partyId
	 * @param partyType
	 * @param resId
	 * @param resType
	 * @param resState
	 * @param partyScope
	 * @param createuser
	 * @param createtime
	 */
	public CapResauth(String tenantId, String partyId, String partyType,
			String resId, String resType, String resState, String partyScope,
			String createuser, Date createtime) {
		super();
		this.tenantId = tenantId;
		this.partyId = partyId;
		this.partyType = partyType;
		this.resId = resId;
		this.resType = resType;
		this.resState = resState;
		this.partyScope = partyScope;
		this.createuser = createuser;
		this.createtime = createtime;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getPartyId() {
		return partyId;
	}

	public String getPartyType() {
		return partyType;
	}

	public String getResId() {
		return resId;
	}

	public String getResType() {
		return resType;
	}

	public String getResState() {
		return resState;
	}

	public String getPartyScope() {
		return partyScope;
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

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public void setResState(String resState) {
		this.resState = resState;
	}

	public void setPartyScope(String partyScope) {
		this.partyScope = partyScope;
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
		return "CapResauth [tenantId=" + tenantId + ", partyId=" + partyId
				+ ", partyType=" + partyType + ", resId=" + resId
				+ ", resType=" + resType + ", resState=" + resState
				+ ", partyScope=" + partyScope + ", createuser=" + createuser
				+ ", createtime=" + createtime + "]";
	}
	
	
}