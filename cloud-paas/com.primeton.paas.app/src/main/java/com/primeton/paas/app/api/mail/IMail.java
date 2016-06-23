/**
 * 
 */
package com.primeton.paas.app.api.mail;

import java.sql.Date;
import java.util.List;

import com.primeton.paas.app.api.file.IFile;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public interface IMail {

	final String CONTENTTYPE_TEXT = "text";
	
	final String CONTENTTYPE_HTML = "html";
	
	/**
	 * 
	 * @param mailServerHost
	 */
	void setMailServerHost(String mailServerHost);
	
	/**
	 * 
	 * @param mailServerPort
	 */
	void setMailServerPort(String mailServerPort);
	
	/**
	 * 
	 * @param validate
	 */
	void setValidate(boolean validate);				 
	
	/**
	 * 
	 * @param username
	 */
	void setUsername(String username);				 
	
	/**
	 * 
	 * @param password
	 */
	void setPassword(String password);				 
	
	/**
	 * 
	 * @param from
	 */
	void setFrom(String from);						 
	
	/**
	 * 
	 * @param cc
	 */
	void setCc(List<String> cc);					
	
	/**
	 * 
	 * @param to
	 */
	void setTo(List<String> to);					
	
	/**
	 * 
	 * @param subject
	 */
	void setSubject(String subject);				
	
	/**
	 * text/html
	 * @param contentType
	 */
	void setContentType(String contentType); 		 
	
	/**
	 * 
	 * @param content
	 */
	void setContent(String content);				 
	
	/**
	 * 
	 * @param attachments
	 */
	void setAttachments(List<IFile> attachments);	 
	
	/**
	 * 
	 * @return
	 */
	String getMailId();
	
	/**
	 * 
	 * @return
	 */
	String getMailServerHost();
	
	/**
	 * 
	 * @return
	 */
	String getMailServerPort();
	
	/**
	 * 
	 * @return
	 */
	boolean isValidate();
	
	/**
	 * 
	 * @return
	 */
	String getUsername();
	
	/**
	 * 
	 * @return
	 */
	String getPassword();
	
	/**
	 * 
	 * @return
	 */
	String getFrom();
	
	/**
	 * 
	 * @return
	 */
	List<String> getCc();
	
	/**
	 * 
	 * @return
	 */
	List<String> getTo();
	
	/**
	 * 
	 * @return
	 */
	String getSubject();
	
	/**
	 * 
	 * @return
	 */
	String getContentType();
	
	/**
	 * 
	 * @return
	 */
	String getContent();
	
	/**
	 * 
	 * @return
	 */
	List<IFile> getAttachments();
	
	/**
	 * 
	 * @return
	 */
	Date getCreateDate();
	
	/**
	 * 
	 * @return
	 */
	String getAppName();
	
}
