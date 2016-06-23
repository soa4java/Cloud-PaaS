/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.app.config.eos;

import com.primeton.paas.app.config.IConfigModel;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HttpAccessModel implements IConfigModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1707849335260888844L;

	// 最大可上传文件字节数
	private long uploadLimits;
	
	// 上传文件临时缓冲大小
	private long uploadBuffer;
	
	// 不可上传文件类型
	private String forbidUpload;
	
	// 文件上传临时存放目录
	private String uploadTemp;
	
	// 未登录时跳转页面
	private String loginPage;
	
	// 拦截Url
	private String filterUrl;
	
	// 不拦截Url
	private String notFilterUrl;
	
	// 引擎字符编码
	private String engineCharset;
	
	// 门户（Portal）模式
	private boolean portalEnable;

	/**
	 * Default. <br>
	 */
	public HttpAccessModel() {
		super();
	}

	public long getUploadLimits() {
		return uploadLimits;
	}

	public void setUploadLimits(long uploadLimits) {
		this.uploadLimits = uploadLimits;
	}

	public long getUploadBuffer() {
		return uploadBuffer;
	}

	public void setUploadBuffer(long uploadBuffer) {
		this.uploadBuffer = uploadBuffer;
	}

	public String getForbidUpload() {
		return forbidUpload;
	}

	public void setForbidUpload(String forbidUpload) {
		this.forbidUpload = forbidUpload;
	}

	public String getUploadTemp() {
		return uploadTemp;
	}

	public void setUploadTemp(String uploadTemp) {
		this.uploadTemp = uploadTemp;
	}

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public String getFilterUrl() {
		return filterUrl;
	}

	public void setFilterUrl(String filterUrl) {
		this.filterUrl = filterUrl;
	}

	public String getNotFilterUrl() {
		return notFilterUrl;
	}

	public void setNotFilterUrl(String notFilterUrl) {
		this.notFilterUrl = notFilterUrl;
	}

	public String getEngineCharset() {
		return engineCharset;
	}

	public void setEngineCharset(String engineCharset) {
		this.engineCharset = engineCharset;
	}

	public boolean isPortalEnable() {
		return portalEnable;
	}

	public void setPortalEnable(boolean portalEnable) {
		this.portalEnable = portalEnable;
	}

	public String toString() {
		return "HttpAccessModel [uploadLimits=" + uploadLimits
				+ ", uploadBuffer=" + uploadBuffer + ", forbidUpload="
				+ forbidUpload + ", uploadTemp=" + uploadTemp + ", loginPage="
				+ loginPage + ", filterUrl=" + filterUrl + ", notFilterUrl="
				+ notFilterUrl + ", engineCharset=" + engineCharset
				+ ", portalEnable=" + portalEnable + "]";
	}
	
}
