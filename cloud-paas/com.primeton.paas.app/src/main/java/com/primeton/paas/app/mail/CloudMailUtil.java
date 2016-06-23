/**
 * 
 */
package com.primeton.paas.app.mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.gocom.cloud.cesium.mqclient.api.MQClient;
import org.gocom.cloud.cesium.mqclient.api.MQClientFactory;
import org.gocom.cloud.cesium.mqclient.api.MQServer;
import org.gocom.cloud.cesium.mqclient.api.Message;
import org.gocom.cloud.cesium.mqclient.api.MessageFuture;
import org.gocom.cloud.cesium.mqclient.api.MessageProducer;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.api.file.IFile;
import com.primeton.paas.app.api.file.InnerFileUtil;
import com.primeton.paas.app.api.mail.IMail;
import com.primeton.paas.app.api.mail.MailResult;
import com.primeton.paas.app.file.FileUtil;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;
import com.primeton.paas.app.util.EmailUtil;
import com.primeton.paas.mail.model.Attachment;
import com.primeton.paas.mail.model.MergeMail;
import com.primeton.paas.mail.model.MergeMailMessage;
import com.primeton.paas.mail.model.Result;
import com.primeton.paas.mail.model.ResultMessage;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CloudMailUtil {

	private static ILogger logger = SystemLoggerFactory
			.getLogger(CloudMailUtil.class);

	private static ISystemConfig systemConfig;
	private static String targetQueue;
	
	private static final String NAME_MQCLIENT = "mail";

	static {
		init();
	}

	private static void init() {
		if (systemConfig == null) {
			systemConfig = SystemConfigFactory.getSystemConfig();
		}
	}

	/**
	 * 
	 * @param mail
	 * @return
	 */
	public static MailResult syncSend(IMail mail) {
		MailResult mr = new MailResult();
		if (!EmailUtil.validateMailParameter(mail)) {
			logger.error("Mail info error or less!");
			mr.setMailId(mail.getMailId());
			mr.setState(MailResult.SEND_RESULT_ERROR);
			mr.setException("Mail info error or less!");
			return mr;
		}
		MergeMailMessage message = new MergeMailMessage();
		MergeMail mm = mergeMail(mail);
		mm.setIfBack(true);
		message.setBody(mm);
		message.setNeedResponse(true);

		MQClient client = getMQClient();
		String queue = getTargetQueue();

		MessageProducer producer = new MessageProducer(client);
		MessageFuture future = null;
		try {
			future = producer.sendMessageAsyn(queue, message);
		} catch (MessageException e) {
			logger.error("Send to MessageQueue Failed!");
			mr.setException(e.getMessage());
			mr.setState(MailResult.SEND_RESULT_ERROR);
			return mr;
		}
		Message<?>[] resultMessages = future.getResult(60000 * 5);

		ResultMessage rm = null;
		if (resultMessages[0] instanceof ResultMessage) {
			rm = (ResultMessage) resultMessages[0];
		} else {
			mr.setState(MailResult.SEND_RESULT_ERROR);
			return mr;
		}
		Result r = rm.getBody();
		// Result-->MailResult
		mr.setMailId(r.getMailId());
		mr.setState(r.getState());
		mr.setException(r.getException());
		mr.setExceptionCode(r.getExceptionCode());
		return mr;
	}

	/**
	 * 
	 * @param mail
	 */
	public static void asyncSend(IMail mail) {
		if (!EmailUtil.validateMailParameter(mail)) {
			logger.error("Mail info error or less!");
			return;
		}
		MergeMailMessage message = new MergeMailMessage();
		MergeMail mm = mergeMail(mail);
		mm.setIfBack(false);
		message.setBody(mm);
		message.setNeedResponse(false);

		MQClient client = getMQClient();
		String queue = getTargetQueue();
		MessageProducer producer = new MessageProducer(client);
		try {
			producer.sendMessageAsyn(queue, message);
		} catch (MessageException e) {
			logger.info("Send to MessageQueue Failed!");
		}
	}

	/**
	 * 
	 * @param mail
	 * @return
	 */
	private static MergeMail mergeMail(IMail mail) {
		MergeMail email = new MergeMail();
		email.setMailId(mail.getMailId());
		String appName;
		appName = ServerContext.getInstance().getAppName();
		if (appName == null) {
			appName = "Null";
		}
		email.setAppName(appName);
		if (mail.getMailServerHost() == null) {
			mail.setMailServerHost("smtp." //$NON-NLS-1$
					+ mail.getFrom().substring(mail.getFrom().indexOf("@") + 1)); //$NON-NLS-1$
		}
		email.setMailServerHost(mail.getMailServerHost());
		email.setMailServerPort(mail.getMailServerPort());
		email.setUsername(mail.getUsername());
		email.setPassword(mail.getPassword());
		email.setFrom(mail.getFrom());
		email.setTo(mail.getTo());
		email.setCc(mail.getCc());
		email.setSubject(mail.getSubject());
		email.setContentType(mail.getContentType());
		email.setContent(mail.getContent());
		email.setValidate(mail.isValidate());
		email.setCreateDate(new Date(System.currentTimeMillis()));

		List<Attachment> attachments = consumeAttachments(mail.getAttachments());
		String content = mail.getContent();
		if (IMail.CONTENTTYPE_HTML.equals(mail.getContentType())) {
			List<IFile> imgIFiles = getImgList(content);
			if (imgIFiles.size() > 0 && !imgIFiles.isEmpty()) {
				List<Attachment> imgAttachments = consumeAttachments(imgIFiles);
				for (int i = 0; i < imgAttachments.size(); i++) {
					attachments.add(imgAttachments.get(i));
					content = content.replace(imgIFiles.get(i).getPath(),
							imgAttachments.get(i).getAttachId());
				}
			}
		}
		email.setContent(content);
		email.setAttachments(attachments);
		return email;
	}

	/**
	 * 
	 * @param attachFiles
	 * @return
	 */
	private static List<Attachment> consumeAttachments(List<IFile> attachFiles) {
		List<Attachment> attachments = new ArrayList<Attachment>();
		for (IFile iFile : attachFiles) {
			Attachment attachment = new Attachment();
			attachment.setAttachId(UUID.randomUUID().toString());
			attachment.setName(iFile.getName());
			try {
				attachment.setContext(FileUtil
						.read(iFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();//
			}
			attachments.add(attachment);
		}
		return attachments;
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
			IFile file = InnerFileUtil.getFile(path);
			if (file != null && file.getName() != null && file.exists()) {
				imgList.add(file);
			}
		}
		return imgList;
	}

	/**
	 * 
	 * @return
	 */
	private static String getTargetQueue() {
		synchronized (CloudMailUtil.class) {
			if (targetQueue == null) {
				String[] queues = systemConfig.getQueueGroup();
				if (queues != null && queues.length > 0) {
					if (queues.length == 1) {
						targetQueue = queues[0];
					} else {
						int index = (int) (Math.random() * queues.length);
						targetQueue = queues[index];
					}
				} else {
					throw new RuntimeException(
							"Message queue not found for send mail.");
				}
			}
		}
		return targetQueue;
	}

	/**
	 * 
	 * @return
	 */
	private static MQClient getMQClient() {
		MQServer server = systemConfig.getMQServer();
		if (server == null) {
			throw new RuntimeException("MQServer not found for send mail.");
		}
		return MQClientFactory.createMQClient(NAME_MQCLIENT, server);
	}

}
