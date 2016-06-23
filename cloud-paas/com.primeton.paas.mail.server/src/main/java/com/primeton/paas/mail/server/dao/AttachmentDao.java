/**
 * 
 */
package com.primeton.paas.mail.server.dao;

import java.util.List;

import com.primeton.paas.mail.server.model.AttachmentInfo;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public interface AttachmentDao {
	
	/**
	 * 
	 * @param attachment
	 * @return
	 */
	boolean insert(AttachmentInfo attachment);
	
	/**
	 * 
	 */
	void deleteAll();
	
	/**
	 * 
	 * @param attachId
	 * @return
	 */
	AttachmentInfo get(String attachId);
	
	/**
	 * 
	 * @param mailId
	 * @return
	 */
	List<AttachmentInfo> getAttOfMail(String mailId);
	
	/**
	 * 
	 * @return
	 */
	List<AttachmentInfo> getAll();
	
}
