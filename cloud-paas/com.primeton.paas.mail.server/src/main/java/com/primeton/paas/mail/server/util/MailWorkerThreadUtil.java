/**
 * 
 */
package com.primeton.paas.mail.server.util;

import com.primeton.paas.mail.server.config.Constants;
import com.primeton.paas.mail.server.dao.MailDao;
import com.primeton.paas.mail.server.dao.MailDaoImpl;
import com.primeton.paas.mail.server.model.MailInfo;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class MailWorkerThreadUtil {

	/**
	 * 
	 * @return
	 */
	public synchronized static MailInfo getForMailWorker() {
		MailInfo mi = new MailInfo();
		MailDao md = new MailDaoImpl();
		mi = md.getForMailWorker();
		if (mi != null) {
			mi.setStatus(Constants.SENDR_ESULT_SENDING);
			md.update(mi);
			return mi;
		}
		return null;
	}

}
