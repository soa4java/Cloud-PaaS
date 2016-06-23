/**
 * 
 */
package com.primeton.paas.app.api.mail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.api.file.IFile;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class Mail implements IMail {

	private String mailId;
	private String mailServerHost = "mail.primeton.com";
	private String mailServerPort = "25";
	private boolean validate = true;
	private String username;
	private String password;
	private String from;
	private List<String> cc = new ArrayList<String>();
	private List<String> to = new ArrayList<String>();
	private String subject;
	private String contentType = IMail.CONTENTTYPE_TEXT;
	private String content;
	private List<IFile> attachments = new ArrayList<IFile>();
	private Date createDate;
	
	private String appName;

	public Mail() {
		this.mailId = UUID.randomUUID().toString();
		this.createDate = new Date(System.currentTimeMillis());
		this.appName = ServerContext.getInstance().getAppName();
	}

	public String getMailId() {
		return mailId;
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

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
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

	public List<IFile> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<IFile> attachments) {
		this.attachments = attachments;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Mail [mailId=" + mailId + ", mailServerHost=" + mailServerHost
				+ ", mailServerPort=" + mailServerPort + ", validate="
				+ validate + ", username=" + username + ", password="
				+ password + ", from=" + from + ", cc=" + cc + ", to=" + to
				+ ", subject=" + subject + ", contentType=" + contentType
				+ ", content=" + content + ", attachments=" + attachments
				+ ", createDate=" + createDate + "]";
	}

}
