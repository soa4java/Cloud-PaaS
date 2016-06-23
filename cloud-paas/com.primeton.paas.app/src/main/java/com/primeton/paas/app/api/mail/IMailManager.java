/**
 * 
 */
package com.primeton.paas.app.api.mail;


/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IMailManager {

	/**
	 * 发送邮件, 并获取发送结果. <br>
	 * 
	 * @param mail 邮件
	 * @return 发送结果
	 * @throws MailException
	 */
	MailResult syncSend(IMail mail) throws MailException;
	
	/**
	 * 快速发送, 无发送结果返回. <br>
	 * @param mail 邮件
	 * @throws MailException
	 */
	void asyncSend(IMail mail) throws MailException;
	
}
