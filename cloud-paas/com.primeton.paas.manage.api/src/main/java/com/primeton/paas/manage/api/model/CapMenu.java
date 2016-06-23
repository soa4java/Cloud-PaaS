package com.primeton.paas.manage.api.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class CapMenu implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2323752202753762711L;
	
	private String menuId;
	private String tenantId;
	private String menuCode;
	private String menuName;
	private String linkType;
	private String linkRes;
	private String linkAction;
	private int menuLevel;
	private String menuSeq;
	private String isleaf;
	private String parentMenuId;
	private String imagepath;
	private String expandpath;
	private String openmode;
	private String createuser;
	private Date createtime;
	
	public CapMenu() {
		super();
	}

	/**
	 * 
	 * @param menuId
	 * @param tenantId
	 * @param menuCode
	 * @param menuName
	 * @param linkType
	 * @param linkRes
	 * @param linkAction
	 * @param menuLevel
	 * @param menuSeq
	 * @param isleaf
	 * @param parentMenuId
	 * @param imagepath
	 * @param expandpath
	 * @param openmode
	 * @param createuser
	 * @param createtime
	 */
	public CapMenu(String menuId, String tenantId, String menuCode,
			String menuName, String linkType, String linkRes,
			String linkAction, int menuLevel, String menuSeq, String isleaf,String parentMenuId,
			String imagepath, String expandpath, String openmode,
			String createuser, Date createtime) {
		super();
		this.menuId = menuId;
		this.tenantId = tenantId;
		this.menuCode = menuCode;
		this.menuName = menuName;
		this.linkType = linkType;
		this.linkRes = linkRes;
		this.linkAction = linkAction;
		this.menuLevel = menuLevel;
		this.menuSeq = menuSeq;
		this.isleaf = isleaf;
		this.parentMenuId = parentMenuId;
		this.imagepath = imagepath;
		this.expandpath = expandpath;
		this.openmode = openmode;
		this.createuser = createuser;
		this.createtime = createtime;
	}

	public String getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public String getMenuId() {
		return menuId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public String getLinkType() {
		return linkType;
	}

	public String getLinkRes() {
		return linkRes;
	}

	public String getLinkAction() {
		return linkAction;
	}

	public int getMenuLevel() {
		return menuLevel;
	}

	public String getMenuSeq() {
		return menuSeq;
	}

	public String getIsleaf() {
		return isleaf;
	}

	public String getImagepath() {
		return imagepath;
	}

	public String getExpandpath() {
		return expandpath;
	}

	public String getOpenmode() {
		return openmode;
	}

	public String getCreateuser() {
		return createuser;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public void setLinkRes(String linkRes) {
		this.linkRes = linkRes;
	}

	public void setLinkAction(String linkAction) {
		this.linkAction = linkAction;
	}

	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}

	public void setMenuSeq(String menuSeq) {
		this.menuSeq = menuSeq;
	}

	public void setIsleaf(String isleaf) {
		this.isleaf = isleaf;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}

	public void setExpandpath(String expandpath) {
		this.expandpath = expandpath;
	}

	public void setOpenmode(String openmode) {
		this.openmode = openmode;
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
		return "CapMenu [menuId=" + menuId + ", tenantId=" + tenantId
				+ ", menuCode=" + menuCode + ", menuName=" + menuName
				+ ", linkType=" + linkType + ", linkRes=" + linkRes
				+ ", linkAction=" + linkAction + ", menuLevel=" + menuLevel
				+ ", menuSeq=" + menuSeq + ", isleaf=" + isleaf
				+ ", parentMenuId=" + parentMenuId + ", imagepath=" + imagepath
				+ ", expandpath=" + expandpath + ", openmode=" + openmode
				+ ", createuser=" + createuser + ", createtime=" + createtime
				+ "]";
	}
	
}