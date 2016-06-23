/**
 * 
 */
package com.primeton.paas.mail.server.dao;

import java.util.List;

import com.primeton.paas.mail.server.model.MailInfo;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface MailDao {
	
	/**
	 * 
	 * @param mail
	 * @return
	 */
	boolean insert(MailInfo mail);
	
	/**
	 * 
	 * @param mail
	 */
	void update(MailInfo mail);
	
	/**
	 * 
	 */
	void deleteAll();
	
	/**
	 * 
	 * @return
	 */
	List<MailInfo> getAll();
	
	/**
	 * 
	 * @return
	 */
	MailInfo getForMailWorker();
	
}
