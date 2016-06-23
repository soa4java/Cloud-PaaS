/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author liyanping(mailto:liyp@primeton.com)
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = 3083617567326056510L;
	
	/** user status */
	public static final int USER_STATUS_UNAUDIT = 1;
	public static final int USER_STATUS_AGREED = 2;
	public static final int USER_STATUS_REJECTED = 3;
	public static final int USER_STATUS_ACTIVED = 4;  
	
	
	private long id ;
	private String userId;
	private String userName;
	private String password;
	
	private String tel;
	private String phone;
	private String email;
	
	private int status;
	private String gender;
	
	private String address;
	private String notes;
	private String handler;
	
	private Date unlocktime; 
	private Date lastlogin;
	private Date createtime;
	
	/**
	 * 
	 */
	public User() {
		super();
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the handler
	 */
	public String getHandler() {
		return handler;
	}

	/**
	 * @param handler the handler to set
	 */
	public void setHandler(String handler) {
		this.handler = handler;
	}

	/**
	 * @return the unlocktime
	 */
	public Date getUnlocktime() {
		return unlocktime;
	}

	/**
	 * @param unlocktime the unlocktime to set
	 */
	public void setUnlocktime(Date unlocktime) {
		this.unlocktime = unlocktime;
	}

	/**
	 * @return the lastlogin
	 */
	public Date getLastlogin() {
		return lastlogin;
	}

	/**
	 * @param lastlogin the lastlogin to set
	 */
	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}

	/**
	 * @return the createtime
	 */
	public Date getCreatetime() {
		return createtime;
	}

	/**
	 * @param createtime the createtime to set
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String toString() {
		return "User [id=" + id + ", userId=" + userId + ", userName="
				+ userName + ", password=" + password + ", tel=" + tel
				+ ", phone=" + phone + ", email=" + email + ", status="
				+ status + ", gender=" + gender + ", address=" + address
				+ ", notes=" + notes + ", handler=" + handler + ", unlocktime="
				+ unlocktime + ", lastlogin=" + lastlogin + ", createtime="
				+ createtime + "]";
	}
	
}
