/**
 * 
 */
package com.primeton.paas.mail.server.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class MailInfo {

	private String mailId;
	private String appName;
	private String mailServerHost;
	private String mailServerPort;
	private boolean validate;
	private boolean ifBack;
	private String username;
	private String password;
	private String sendFrom;
	private List<String> sendCc = new ArrayList<String>();
	private List<String> sendTo = new ArrayList<String>();
	private String subject;
	private String contentType;
	private String content;
	private List<String> attachmentsId = new ArrayList<String>();
	private Date createDate;

	private String status;
	private String exceptionMessage;
	private String exceptionCode;

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public boolean isIfBack() {
		return ifBack;
	}

	public void setIfBack(boolean ifBack) {
		this.ifBack = ifBack;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubject() {
		return subject;
	}

	public String getSendFrom() {
		return sendFrom;
	}

	public void setSendFrom(String sendFrom) {
		this.sendFrom = sendFrom;
	}

	public List<String> getSendCc() {
		return sendCc;
	}

	public void setSendCc(List<String> sendCc) {
		this.sendCc = sendCc;
	}

	public List<String> getSendTo() {
		return sendTo;
	}

	public void setSendTo(List<String> sendTo) {
		this.sendTo = sendTo;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getAttachmentsId() {
		return attachmentsId;
	}

	public void setAttachmentsId(List<String> attachmentsId) {
		this.attachmentsId = attachmentsId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "MailInfo [mailId=" + mailId + ", appName=" + appName
				+ ", mailServerHost=" + mailServerHost + ", mailServerPort="
				+ mailServerPort + ", validate=" + validate + ", ifBack="
				+ ifBack + ", username=" + username + ", password=" + password
				+ ", sendFrom=" + sendFrom + ", sendCc=" + sendCc + ", sendTo="
				+ sendTo + ", subject=" + subject + ", contentType="
				+ contentType + ", content=" + content + ", attachmentsId="
				+ attachmentsId + ", createDate=" + createDate + ", status="
				+ status + ", exceptionMessage=" + exceptionMessage
				+ ", exceptionCode=" + exceptionCode + "]";
	}

}
