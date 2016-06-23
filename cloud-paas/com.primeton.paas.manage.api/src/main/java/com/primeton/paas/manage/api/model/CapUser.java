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
public class CapUser implements Serializable {
	
	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = 2725996498580067739L;
	
	/** user status */
	public static final int USER_STATUS_UNAUDIT = 1;
	public static final int USER_STATUS_AGREED = 2;
	public static final int USER_STATUS_REJECTED = 3;
	public static final int USER_STATUS_ACTIVED = 4;
	
	private String operatorId;
	private String tenantId;
	private String userId;
	private String password;
	private String userName;
	
	
	private String tel;
	private String phone;
	private String email;
	private String address;
	
	private int status;
	private String gender;
	
	private Date lastlogin;
	private String createuser;
	private Date createtime;
	private String notes; 
	
	public CapUser() {
		super();
	}
	
	/**
	 * 
	 * @param operatorId
	 * @param tenantId
	 * @param userId
	 * @param password
	 * @param userName
	 * @param tel
	 * @param phone
	 * @param email
	 * @param address
	 * @param status
	 * @param gender
	 * @param lastlogin
	 * @param createuser
	 * @param createtime
	 * @param notes
	 */
	public CapUser(String operatorId, String tenantId, String userId,
			String password, String userName, String tel, String phone,
			String email, String address, int status, String gender,
			Date lastlogin, String createuser, Date createtime, String notes) {
		super();
		this.operatorId = operatorId;
		this.tenantId = tenantId;
		this.userId = userId;
		this.password = password;
		this.userName = userName;
		this.tel = tel;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.status = status;
		this.gender = gender;
		this.lastlogin = lastlogin;
		this.createuser = createuser;
		this.createtime = createtime;
		this.notes = notes;
	}
	
	public String getOperatorId() {
		return operatorId;
	}
	
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	
	public String getTenantId() {
		return tenantId;
	}
	
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getTel() {
		return tel;
	}
	
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public Date getLastlogin() {
		return lastlogin;
	}
	
	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}
	
	public String getCreateuser() {
		return createuser;
	}
	
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	
	public Date getCreatetime() {
		return createtime;
	}
	
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "CapUser [operatorId=" + operatorId + ", tenantId=" + tenantId
				+ ", userId=" + userId + ", password=" + password
				+ ", userName=" + userName + ", tel=" + tel + ", phone="
				+ phone + ", email=" + email + ", address=" + address
				+ ", status=" + status + ", gender=" + gender + ", lastlogin="
				+ lastlogin + ", createuser=" + createuser + ", createtime="
				+ createtime + ", notes=" + notes + "]";
	}
	
}
