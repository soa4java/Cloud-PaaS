/**
 * 
 */
package com.primeton.paas.mail.server.startup;

import java.io.UnsupportedEncodingException;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.mail.server.model.MailInfo;
import com.primeton.paas.mail.server.util.DaoUtil;
import com.primeton.paas.mail.server.util.MailConsumerUtil;
import com.primeton.paas.mail.server.util.MailWorkerThreadUtil;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class MailWorker extends Thread {
	
	private static ILogger logger = LoggerFactory
			.getLogger(MailCoreThread.class);

	private boolean isRunning = false;
	private boolean flag = false;

	public MailWorker() {
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		logger.info("Start mail worker thread.");
		isRunning = true;
		while (true) {
			if (flag) {
				logger.info("Stop mail worker thread.");
				isRunning = false;
				break;
			}
			MailInfo mail = MailWorkerThreadUtil.getForMailWorker();
			if (mail != null) {
				long start = System.currentTimeMillis();
				try {
					mail.setPassword(DaoUtil.decode(mail.getPassword()));
				} catch (UnsupportedEncodingException e) {
					if (logger.isWarnEnabled()) {
						logger.warn(e.getMessage());
					}
				}
				MailConsumerUtil.send(mail);
				logger.info("SendMail:MailId:" + mail.getMailId() + ",From:"
						+ mail.getSendFrom() + ",To:" + mail.getSendTo()
						+ " Done." + "Time Spend : "
						+ (System.currentTimeMillis() - start) / 1000 + "s");
			} else {
				ThreadUtil.sleep(1000L);
			}

		}
	}

	public void close() {
		this.flag = true;
	}

	public boolean isRunning() {
		return this.isRunning;
	}

}
