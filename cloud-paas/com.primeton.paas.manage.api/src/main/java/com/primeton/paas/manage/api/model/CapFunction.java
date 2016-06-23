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
public class CapFunction implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2767000196038220608L;
	
	private String funcId;
	private String tenantId;
	private String funcName;
	private String funcType;
	private String funcDesc;
	private String funcAction;
	private String isCheck;
	private String isMenu;
	private String createuser;
	private Date createtime;
	
	/**
	 * 
	 */
	public CapFunction() {
		super();
	}
	
	/**
	 * 
	 * @param funcId
	 * @param tenantId
	 * @param funcName
	 * @param funcType
	 * @param funcDesc
	 * @param funcAction
	 * @param isCheck
	 * @param isMenu
	 * @param createuser
	 * @param createtime
	 */
	public CapFunction(String funcId, String tenantId, String funcName,
			String funcType, String funcDesc, String funcAction,
			String isCheck, String isMenu, String createuser, Date createtime) {
		super();
		this.funcId = funcId;
		this.tenantId = tenantId;
		this.funcName = funcName;
		this.funcType = funcType;
		this.funcDesc = funcDesc;
		this.funcAction = funcAction;
		this.isCheck = isCheck;
		this.isMenu = isMenu;
		this.createuser = createuser;
		this.createtime = createtime;
	}
	
	public String getFuncId() {
		return funcId;
	}
	
	public String getTenantId() {
		return tenantId;
	}
	
	public String getFuncName() {
		return funcName;
	}
	
	public String getFuncType() {
		return funcType;
	}
	
	public String getFuncDesc() {
		return funcDesc;
	}
	
	public String getFuncAction() {
		return funcAction;
	}
	
	public String getIsCheck() {
		return isCheck;
	}
	
	public String getIsMenu() {
		return isMenu;
	}
	
	public String getCreateuser() {
		return createuser;
	}
	
	public Date getCreatetime() {
		return createtime;
	}
	
	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}
	
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	
	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}
	
	public void setFuncDesc(String funcDesc) {
		this.funcDesc = funcDesc;
	}
	
	public void setFuncAction(String funcAction) {
		this.funcAction = funcAction;
	}
	
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	
	public void setIsMenu(String isMenu) {
		this.isMenu = isMenu;
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
		return "CapFunction [funcId=" + funcId + ", tenantId=" + tenantId
				+ ", funcName=" + funcName + ", funcType=" + funcType
				+ ", funcDesc=" + funcDesc + ", funcAction=" + funcAction
				+ ", isCheck=" + isCheck + ", isMenu=" + isMenu
				+ ", createuser=" + createuser + ", createtime=" + createtime
				+ "]";
	}
	
}
