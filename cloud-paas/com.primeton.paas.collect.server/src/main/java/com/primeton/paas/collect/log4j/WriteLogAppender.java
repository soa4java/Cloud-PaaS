/**
 * 
 */
package com.primeton.paas.collect.log4j;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.collect.common.JsonSerializeUtil;
import com.primeton.paas.collect.common.LogMetaData;
import com.primeton.paas.collect.common.StringUtil;
import com.primeton.paas.collect.server.ServerContext;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class WriteLogAppender extends RollingFileAppender {
	
	private static ILogger logger = LoggerFactory.getLogger(WriteLogAppender.class);
	
	private ServerContext context = ServerContext.getContext();

	/* (non-Javadoc)
	 * @see org.apache.log4j.AppenderSkeleton#doAppend(org.apache.log4j.spi.LoggingEvent)
	 */
	public synchronized void doAppend(LoggingEvent event) {
		String json = event.getMessage().toString(); // LogMetaData
		if (StringUtil.isEmpty(json)) {
			return;
		}
		
		try {
			LogMetaData metaData = JsonSerializeUtil.deserialize(json, LogMetaData.class);
			if (metaData != null) {
				// /storage/app/erp/10000.user.log
				String filePath = context.getLogRoot() + File.separator
						+ metaData.getAppName() + File.separator
						+ metaData.getInstance() + "." + metaData.getType() + ".log"; //$NON-NLS-1$
				if (logger.isDebugEnabled()) {
					logger.debug("Write log {###\n" + metaData.getContent() + "###} to file [" + filePath + "].");
				}
				try {
					super.setFile(filePath, true, true, context.getAppenderBuffer()); // 1000KB Buffer
					String content = metaData.getContent();
					LoggingEvent e = new LoggingEvent(event.getFQNOfLoggerClass(), event.getLogger(), event.getTimeStamp(), event.getLevel(), content, event.getThreadName(), event.getThrowableInformation(), event.getNDC(), event.getLocationInformation(), event.getProperties());
					super.doAppend(e);
				} catch (IOException e) {
					if(logger.isErrorEnabled()) {
						logger.error(e);
					}
				}
			}
		} catch (Throwable t) {
			logger.error(t);
			logger.error(json);
		}
	}

}
