/**
 * 
 */
package com.primeton.paas.app.mail;

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
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.primeton.paas.app.api.file.IFile;
import com.primeton.paas.app.api.file.InnerFileUtil;
import com.primeton.paas.app.api.mail.IMail;
import com.primeton.paas.app.api.mail.MailResult;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;
import com.primeton.paas.app.util.EmailAuthenticator;

/**
 * 邮件发送模拟器:用于APP开发测试. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SimulatorMailUtil {

	private static ILogger logger = SystemLoggerFactory
			.getLogger(SimulatorMailUtil.class);

	

	

	private static String WEB_CONTENT_FILES = "WebContent" + File.separator //$NON-NLS-1$
			+ "files"; //$NON-NLS-1$

	/**
	 * 
	 * @param mail
	 * @return
	 */
	public static MailResult send(IMail mail) {
		if (null == mail) {
			return null;
		}
		MailResult mr = new MailResult();
		if (IMail.CONTENTTYPE_HTML.equals(mail.getContentType())) {
			mr = sendHtmlMail(mail);
		} else {
			mr = sendTextMail(mail);
		}
		return mr;
	}

	/**
	 * 
	 * @param mail
	 * @return
	 */
	public static MailResult sendHtmlMail(IMail mail) {
		MailResult mr = new MailResult();
		Properties pro = new Properties();
		if (mail.getMailServerHost() == null) {
			mail.setMailServerHost("smtp." //$NON-NLS-1$
					+ mail.getFrom().substring(mail.getFrom().indexOf("@") + 1)); //$NON-NLS-1$
		}
		pro.put("mail.smtp.host", mail.getMailServerHost()); //$NON-NLS-1$
		pro.put("mail.smtp.port", mail.getMailServerPort()); //$NON-NLS-1$
		pro.put("mail.smtp.auth", mail.isValidate() ? Boolean.TRUE.toString() : Boolean.FALSE.toString()); 

		EmailAuthenticator authenticator = null;
		if (mail.isValidate()) {
			authenticator = new EmailAuthenticator(mail.getUsername(),
					mail.getPassword());
		}
		Session sendMailSession = Session.getInstance(pro, authenticator);
		// sendMailSession.setDebug(true);
		try {
			Message mailMessage = new MimeMessage(sendMailSession);
			Address from = new InternetAddress(mail.getFrom());
			mailMessage.setFrom(from);

			List<String> toList = mail.getTo();
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
			List<String> ccList = mail.getCc();
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

			List<IFile> imgList = getImgList(mailContent);
			String newContent = changeSrc(mailContent);
			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setContent(newContent, "text/html;charset=gb2312"); //$NON-NLS-1$ // TODO
			mp.addBodyPart(mbp);

			List<IFile> attachments = mail.getAttachments();
			for (IFile imageFile : imgList) {
				attachments.add(imageFile);
			}

			if (attachments != null && !attachments.isEmpty()) {
				for (IFile attachment : attachments) {
					mbp = new MimeBodyPart();
					String path = attachment.getPath();
					path = WEB_CONTENT_FILES
							+ (path.charAt(0) == File.separatorChar ? ""
									: File.separator) + path;
					FileDataSource fds = new FileDataSource(path);
					mbp.setDataHandler(new DataHandler(fds));
					try {
						mbp.setFileName(MimeUtility.encodeText(fds.getName()));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					mp.addBodyPart(mbp);
				}
			}
			mailMessage.setContent(mp);
			mailMessage.setSentDate(new Date());

			Transport.send(mailMessage);
			mr.setState(MailResult.SEND_RESULT_SUCCESSED);
		} catch (AddressException ae) {
			mr.setState(MailResult.SEND_RESULT_ERROR);
			logger.info("AddressException:" + ae.getMessage());
			mr.setException(ae.getMessage());
			mr.setExceptionCode(MailResult.CODE_ADDRESS_ERROR);

		} catch (AuthenticationFailedException afe) {
			mr.setState(MailResult.SEND_RESULT_ERROR);
			logger.info("AuthenticationFailedException:" + afe.getMessage());
			mr.setException(afe.getMessage());
			mr.setExceptionCode(MailResult.CODE_AUTHENTICATION_ERROR);

		} catch (MessagingException ex) {
			mr.setState(MailResult.SEND_RESULT_ERROR);
			logger.info("MessagingException:" + ex.getMessage());
			mr.setException(ex.getMessage());
			mr.setExceptionCode(MailResult.CODE_MESSAGEING_EXCEPTION);
		}
		return mr;
	}

	/**
	 * 
	 * @param mail
	 * @return
	 */
	public static MailResult sendTextMail(IMail mail) {
		MailResult mr = new MailResult();
		Properties pro = new Properties();
		if (mail.getMailServerHost() == null) {
			mail.setMailServerHost("smtp." //$NON-NLS-1$
					+ mail.getFrom().substring(mail.getFrom().indexOf("@") + 1)); //$NON-NLS-1$
		}
		pro.put("mail.smtp.host", mail.getMailServerHost()); //$NON-NLS-1$
		pro.put("mail.smtp.port", mail.getMailServerPort()); //$NON-NLS-1$
		pro.put("mail.smtp.auth", mail.isValidate() ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
		pro.put("mail.smtp.starttls.enable", Boolean.TRUE.toString()); //$NON-NLS-1$ 
		EmailAuthenticator authenticator = null;
		if (mail.isValidate()) {
			authenticator = new EmailAuthenticator(mail.getUsername(),
					mail.getPassword());
		}
		Session sendMailSession = Session.getInstance(pro, authenticator);
		try {
			Message mailMessage = new MimeMessage(sendMailSession);
			Address from = new InternetAddress(mail.getFrom());
			mailMessage.setFrom(from);

			List<String> toList = mail.getTo();
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
			List<String> ccList = mail.getCc();
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

			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setText(mailContent);
			mp.addBodyPart(mbp);

			List<IFile> attachments = mail.getAttachments();
			if (attachments != null && !attachments.isEmpty()) {
				for (IFile attachment : attachments) {
					mbp = new MimeBodyPart();
					String path = attachment.getPath();
					path = WEB_CONTENT_FILES
							+ (path.charAt(0) == File.separatorChar ? ""
									: File.separator) + path;
					FileDataSource fds = new FileDataSource(path);
					mbp.setDataHandler(new DataHandler(fds));
					try {
						mbp.setFileName(MimeUtility.encodeText(fds.getName()));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					mp.addBodyPart(mbp);
				}
			}
			mailMessage.setContent(mp);
			mailMessage.setSentDate(new Date());

			Transport.send(mailMessage);
			mr.setState(MailResult.SEND_RESULT_SUCCESSED);
		} catch (AddressException ae) {
			mr.setState(MailResult.SEND_RESULT_ERROR);
			mr.setException(ae.getMessage());
			mr.setExceptionCode(MailResult.CODE_ADDRESS_ERROR);

		} catch (AuthenticationFailedException afe) {
			mr.setState(MailResult.SEND_RESULT_ERROR);
			mr.setException(afe.getMessage());
			mr.setExceptionCode(MailResult.CODE_AUTHENTICATION_ERROR);

		} catch (MessagingException ex) {
			mr.setState(MailResult.SEND_RESULT_ERROR);
			mr.setException(ex.getMessage());
			mr.setExceptionCode(MailResult.CODE_MESSAGEING_EXCEPTION);
		}
		return mr;
	}

	/**
	 * 
	 * @param htmlStr
	 * @return
	 */
	private static List<IFile> getImgList(String htmlStr) {
		List<IFile> imgList = new ArrayList<IFile>();
		org.jsoup.nodes.Document doc = Jsoup.parse(htmlStr);
		Elements imgs = doc.select("img"); //$NON-NLS-1$
		for (org.jsoup.nodes.Element link : imgs) {
			String path = link.getElementsByTag("img").attr("src"); //$NON-NLS-1$ //$NON-NLS-2$
			IFile file = InnerFileUtil.getFile(File.separator + path);
			String realpath = WEB_CONTENT_FILES
					+ (path.charAt(0) == File.separatorChar ? "" : File.separator) + file.getPath(); //$NON-NLS-1$
			File realfile = new File(realpath);
			if (realfile.exists()) {
				imgList.add(file);
			}
		}
		return imgList;
	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	private static String changeSrc(String content) {
		String newContent = content;
		org.jsoup.nodes.Document doc = Jsoup.parse(newContent);
		Elements imgs = doc.select("img"); //$NON-NLS-1$
		for (org.jsoup.nodes.Element link : imgs) {
			String path = link.getElementsByTag("img").attr("src"); //$NON-NLS-1$ //$NON-NLS-2$
			IFile file = InnerFileUtil.getFile(path);
			String realpath = WEB_CONTENT_FILES
					+ (path.charAt(0) == File.separatorChar ? ""
							: File.separator) + file.getPath();
			File realfile = new File(realpath);
			if (realfile.exists()) {
				newContent = newContent.replace(file.getPath(),
						"cid:" + file.getPath());
			} else {
				logger.info("Pic:[ " + file.getPath()
						+ " ] not found!It may can not show up!");
			}
		}
		return newContent;
	}

}
