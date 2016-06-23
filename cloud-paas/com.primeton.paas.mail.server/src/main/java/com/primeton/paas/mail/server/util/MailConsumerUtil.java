/**
 * 
 */
package com.primeton.paas.mail.server.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.utility.api.SystemProperties;
import org.jsoup.Jsoup;

import com.primeton.paas.mail.model.Attachment;
import com.primeton.paas.mail.model.MergeMail;
import com.primeton.paas.mail.model.Result;
import com.primeton.paas.mail.server.config.Constants;
import com.primeton.paas.mail.server.dao.AttachmentDao;
import com.primeton.paas.mail.server.dao.AttachmentDaoImpl;
import com.primeton.paas.mail.server.dao.MailDao;
import com.primeton.paas.mail.server.dao.MailDaoImpl;
import com.primeton.paas.mail.server.model.AttachmentInfo;
import com.primeton.paas.mail.server.model.MailInfo;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MailConsumerUtil {

	private static ILogger logger = LoggerFactory
			.getLogger(MailConsumerUtil.class);

	/**
	 * 
	 * @param mergeMail
	 */
	public static MailInfo insertToDb(MergeMail mergeMail) {
		if (null == mergeMail) {
			return null;
		}
		MailInfo mail = new MailInfo();
		mail.setMailId(mergeMail.getMailId());
		mail.setAppName(mergeMail.getAppName());
		mail.setMailServerHost(mergeMail.getMailServerHost());
		mail.setMailServerPort(mergeMail.getMailServerPort());
		mail.setValidate(mergeMail.isValidate());
		mail.setIfBack(mergeMail.isIfBack());
		mail.setUsername(mergeMail.getUsername());
		String password = mergeMail.getPassword();
		try {
			password = DaoUtil.encoded(mergeMail.getPassword());
		} catch (UnsupportedEncodingException e) {
			if (logger.isWarnEnabled()) {
				logger.warn(e.getMessage());
			}
		}
		mail.setPassword(password);
		mail.setSendFrom(mergeMail.getFrom());
		mail.setSendCc(mergeMail.getCc());
		mail.setSendTo(mergeMail.getTo());
		mail.setSubject(mergeMail.getSubject());
		mail.setContentType(mergeMail.getContentType());
		mail.setContent(mergeMail.getContent());//
		mail.setCreateDate(mergeMail.getCreateDate());
		mail.setStatus(Constants.SENDR_ESULT_NOTSEND);

		List<Attachment> attachments = mergeMail.getAttachments();
		if (attachments != null && !attachments.isEmpty()) {
			List<String> attachmentId = new ArrayList<String>();
			List<AttachmentInfo> attachmentList = new ArrayList<AttachmentInfo>();

			for (Attachment attachment : attachments) {
				AttachmentInfo attach = new AttachmentInfo();
				attach.setAttachId(attachment.getAttachId());
				String path = SystemProperties
						.getProperty(Constants.ATTACHMENT_HOME)
						+ File.separator
						+ mail.getAppName()
						+ File.separator
						+ mail.getSendFrom()
						+ File.separator
						+ mail.getMailId()
						+ File.separator
						+ attach.getAttachId();
				attach.setMailId(mergeMail.getMailId());
				attach.setName(attachment.getName());
				attach.setPath(path);
				AttachmentFileUtil.createAttachmentFile(
						attachment.getContext(), attach);
				attachmentList.add(attach);
				attachmentId.add(attach.getAttachId());
			}

			AttachmentDao aDao = new AttachmentDaoImpl();
			for (AttachmentInfo attachment : attachmentList) {
				aDao.insert(attachment);
			}
			mail.setAttachmentsId(attachmentId);
		}

		MailDao mDao = new MailDaoImpl();
		mDao.insert(mail);
		mail.setPassword(mergeMail.getPassword());
		return mail;
	}

	/**
	 * 
	 * @param mail
	 * @return
	 */
	public static Result send(MailInfo mail) {
		if (null == mail) {
			return null;
		}
		Result r = new Result();
		if ("html".equalsIgnoreCase(mail.getContentType())) { //$NON-NLS-1$
			r = sendHtmlMail(mail);
		} else {
			r = sendTextMail(mail);
		}
		mail.setStatus(r.getState());
		mail.setExceptionCode(r.getExceptionCode());
		mail.setExceptionMessage(r.getException());
		MailDao md = new MailDaoImpl();
		md.update(mail);
		return r;
	}

	/**
	 * 
	 * @param mail
	 * @return
	 */
	private static Result sendTextMail(MailInfo mail) {
		if (null == mail) {
			return null;
		}
		Result rs = new Result();
		long start = System.currentTimeMillis();

		Properties pro = new Properties();
		pro.put("mail.smtp.host", mail.getMailServerHost()); //$NON-NLS-1$
		pro.put("mail.smtp.port", mail.getMailServerPort()); //$NON-NLS-1$
		pro.put("mail.smtp.auth", mail.isValidate() ? Boolean.TRUE.toString() : Boolean.FALSE.toString()); //$NON-NLS-1$
		pro.put("mail.smtp.starttls.enable", "true"); //$NON-NLS-1$
		MyAuthenticator authenticator = null;
		if (mail.isValidate()) {
			authenticator = new MyAuthenticator(mail.getUsername(),
					mail.getPassword());
		}
		Session sendMailSession = Session.getInstance(pro, authenticator);
		try {
			Message mailMessage = new MimeMessage(sendMailSession);
			Address from = new InternetAddress(mail.getSendFrom());
			mailMessage.setFrom(from);

			List<String> toList = mail.getSendTo();
			if (toList != null && !toList.isEmpty()) {
				Address[] tos = null;
				if (toList != null && !toList.isEmpty()) {
					tos = new InternetAddress[toList.size()];
					for (int i = 0; i < toList.size(); i++) {
						tos[i] = new InternetAddress(toList.get(i));
					}
				}
				mailMessage.setRecipients(Message.RecipientType.TO, tos);
			}
			List<String> ccList = mail.getSendCc();
			if (ccList != null && !ccList.isEmpty()) {
				Address[] ccs = null;
				if (ccList != null && !ccList.isEmpty()) {
					ccs = new InternetAddress[ccList.size()];
					for (int i = 0; i < ccList.size(); i++) {
						ccs[i] = new InternetAddress(ccList.get(i));
					}
				}
				mailMessage.setRecipients(Message.RecipientType.CC, ccs);
			}

			mailMessage.setSubject(mail.getSubject());
			mailMessage.setSentDate(new Date());
			String mailContent = mail.getContent();
			try {
				mailContent = new String(mailContent.getBytes(), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				logger.error("SendTextMail changeCharset Error:", e1);
			}
			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();
			// mbp.setContent(mailContent, "text/text;charset=gb2312");
			mbp.setText(mailContent);
			mp.addBodyPart(mbp);

			if (mail.getAttachmentsId() != null
					&& !mail.getAttachmentsId().isEmpty()) {
				AttachmentDao aDao = new AttachmentDaoImpl();
				List<AttachmentInfo> attachmentList = aDao.getAttOfMail(mail
						.getMailId());
				for (AttachmentInfo attachment : attachmentList) {
					mbp = new MimeBodyPart();
					String path = attachment.getPath() + File.separator
							+ attachment.getName();
					FileDataSource fds = new FileDataSource(path);
					mbp.setDataHandler(new DataHandler(fds));
					try {
						mbp.setFileName(MimeUtility.encodeText(fds.getName()));
					} catch (UnsupportedEncodingException e) {
						rs.setException(e.getMessage());
						logger.debug(e);
					}
					mp.addBodyPart(mbp);
				}
			}
			mailMessage.setContent(mp);
			mailMessage.setSentDate(mail.getCreateDate());

			Transport.send(mailMessage);
			rs.setState(Constants.SENDR_ESULT_SUCCESSED);
		} catch (AddressException ae) {
			rs.setState(Constants.SENDR_ESULT_ERROR);
			rs.setException(ae.getMessage());
			rs.setExceptionCode(Constants.CODE_ADDRESS_EXCEPTION);
			ae.printStackTrace();
			logger.debug(ae);
		} catch (AuthenticationFailedException afe) {
			rs.setState(Constants.SENDR_ESULT_ERROR);
			rs.setException(afe.getMessage());
			rs.setExceptionCode(Constants.CODE_AUTHENTICATION_ERROR);
			afe.printStackTrace();
			logger.debug(afe);
		} catch (MessagingException ex) {
			rs.setState(Constants.SENDR_ESULT_ERROR);
			rs.setException(ex.getMessage());
			rs.setExceptionCode(Constants.CODE_MESSAGEING_EXCEPTION);
			ex.printStackTrace();
			logger.debug(ex);
		}
		logger.info("Time Spend " + (System.currentTimeMillis() - start) / 1000
				+ "s");
		return rs;
	}

	/**
	 * 
	 * @param mail
	 * @return
	 */
	private static Result sendHtmlMail(MailInfo mail) {
		if (null == mail) {
			return null;
		}
		Result rs = new Result();
		long start = System.currentTimeMillis();

		Properties pro = new Properties();
		pro.put("mail.smtp.host", mail.getMailServerHost()); //$NON-NLS-1$
		pro.put("mail.smtp.port", mail.getMailServerPort()); //$NON-NLS-1$
		pro.put("mail.smtp.auth", mail.isValidate() ? Boolean.TRUE.toString() : Boolean.FALSE.toString()); //$NON-NLS-1$
		pro.put("mail.smtp.starttls.enable", Boolean.TRUE.toString()); //$NON-NLS-1$
		MyAuthenticator authenticator = null;
		if (mail.isValidate()) {
			authenticator = new MyAuthenticator(mail.getUsername(),
					mail.getPassword());
		}
		Session sendMailSession = Session.getInstance(pro, authenticator);
		try {
			Message mailMessage = new MimeMessage(sendMailSession);
			Address from = new InternetAddress(mail.getSendFrom());
			mailMessage.setFrom(from);

			List<String> toList = mail.getSendTo();
			if (toList != null && !toList.isEmpty()) {
				Address[] tos = null;
				if (toList != null && !toList.isEmpty()) {
					tos = new InternetAddress[toList.size()];
					for (int i = 0; i < toList.size(); i++) {
						tos[i] = new InternetAddress(toList.get(i));
					}
				}
				mailMessage.setRecipients(Message.RecipientType.TO, tos);
			}
			List<String> ccList = mail.getSendCc();
			if (ccList != null && !ccList.isEmpty()) {
				Address[] ccs = null;
				if (ccList != null && !ccList.isEmpty()) {
					ccs = new InternetAddress[ccList.size()];
					for (int i = 0; i < ccList.size(); i++) {
						ccs[i] = new InternetAddress(ccList.get(i));
					}
				}
				mailMessage.setRecipients(Message.RecipientType.CC, ccs);
			}
			mailMessage.setSubject(mail.getSubject());
			mailMessage.setSentDate(new Date());
			String content = newContent(mail);
			List<String> imgList = getImgList(mail);
			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setContent(content, "text/html;charset=gb2312"); //$NON-NLS-1$
			mp.addBodyPart(mbp);

			if (mail.getAttachmentsId() != null
					&& !mail.getAttachmentsId().isEmpty()) {
				AttachmentDao aDao = new AttachmentDaoImpl();
				List<AttachmentInfo> attachmentList = aDao.getAttOfMail(mail
						.getMailId());
				for (AttachmentInfo attachment : attachmentList) {
					mbp = new MimeBodyPart();
					String path = attachment.getPath() + File.separator
							+ attachment.getName();
					FileDataSource fds = new FileDataSource(path);
					mbp.setDataHandler(new DataHandler(fds));
					try {
						mbp.setFileName(MimeUtility.encodeText(fds.getName()));
					} catch (UnsupportedEncodingException e) {
						rs.setException(e.getMessage());
						logger.debug(e);
					}
					if (imgList.contains(attachment.getAttachId())) {
						mbp.setHeader(
								"Content-ID", "<" + attachment.getAttachId() + ">"); //$NON-NLS-1$
					}
					mp.addBodyPart(mbp);
				}
			}
			mailMessage.setContent(mp);
			mailMessage.setSentDate(mail.getCreateDate());
			Transport.send(mailMessage);
			rs.setState(Constants.SENDR_ESULT_SUCCESSED);
		} catch (AddressException ae) {
			rs.setState(Constants.SENDR_ESULT_ERROR);
			rs.setException(ae.getMessage());
			rs.setExceptionCode(Constants.CODE_ADDRESS_EXCEPTION);
			logger.debug(ae);
		} catch (AuthenticationFailedException afe) {
			rs.setState(Constants.SENDR_ESULT_ERROR);
			rs.setException(afe.getMessage());
			rs.setExceptionCode(Constants.CODE_AUTHENTICATION_ERROR);
			logger.debug(afe);
		} catch (MessagingException ex) {
			rs.setState(Constants.SENDR_ESULT_ERROR);
			rs.setException(ex.getMessage());
			rs.setExceptionCode(Constants.CODE_MESSAGEING_EXCEPTION);
			logger.debug(ex);
		}
		logger.info("Time Spend��" + (System.currentTimeMillis() - start)
				/ 1000 + "s");
		return rs;
	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	private static String newContent(MailInfo mail) {
		String contentStr = mail.getContent();
		List<String> attachmentIds = mail.getAttachmentsId();
		org.jsoup.nodes.Document doc = Jsoup.parse(mail.getContent());
		org.jsoup.select.Elements imgs = doc.select("img"); //$NON-NLS-1$
		for (org.jsoup.nodes.Element link : imgs) {
			String attachId = link.getElementsByTag("img").attr("src"); //$NON-NLS-1$ //$NON-NLS-2$
			if (attachmentIds.contains(attachId)) {
				contentStr = contentStr.replace(attachId, "cid:" + attachId);
			}
		}
		return contentStr;
	}

	/**
	 * 
	 * @param mail
	 * @return
	 */
	private static List<String> getImgList(MailInfo mail) {
		List<String> imgList = new ArrayList<String>();
		List<String> attachmentIds = mail.getAttachmentsId();
		org.jsoup.nodes.Document doc = Jsoup.parse(mail.getContent());
		org.jsoup.select.Elements imgs = doc.select("img"); //$NON-NLS-1$
		for (org.jsoup.nodes.Element link : imgs) {
			String attachId = link.getElementsByTag("img").attr("src"); //$NON-NLS-1$ //$NON-NLS-2$
			if (attachmentIds.contains(attachId)) {
				imgList.add(attachId);
			}
		}
		return imgList;
	}

}