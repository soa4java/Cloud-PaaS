/**
 * 
 */
package com.primeton.paas.mail.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class MergeMail {
	private String mailId;
	private String appName;
	private String mailServerHost;
	private String mailServerPort;
	private boolean validate;
	private boolean ifBack;
	private String username;
	private String password;
	private String from;
	private List<String> cc = new ArrayList<String>();
	private List<String> to = new ArrayList<String>();
	private String subject;
	private String contentType;
	private String content;
	private Date createDate;
	private List<Attachment> attachments = new ArrayList<Attachment>();

	public MergeMail() {
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public boolean isIfBack() {
		return ifBack;
	}

	public void setIfBack(boolean ifBack) {
		this.ifBack = ifBack;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
}
