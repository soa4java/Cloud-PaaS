/**
 * 
 */
package com.primeton.paas.mail.server.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.mail.server.model.AttachmentInfo;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class AttachmentFileUtil {
	
	private static ILogger logger = LoggerFactory
			.getLogger(AttachmentFileUtil.class);

	/**
	 * 
	 * @param b
	 * @param attach
	 * @return
	 */
	public static File createAttachmentFile(byte[] b, AttachmentInfo attach) {
		File file = new File(attach.getPath() + File.separator
				+ attach.getName());
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		BufferedOutputStream stream = null;
		try {
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return file;
	}
	
}
