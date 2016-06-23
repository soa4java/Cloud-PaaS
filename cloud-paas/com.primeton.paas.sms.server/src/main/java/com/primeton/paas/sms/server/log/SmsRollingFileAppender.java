/**
 * 
 */
package com.primeton.paas.sms.server.log;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.spi.LoggingEvent;

import com.primeton.paas.sms.spi.SmsConstants;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public class SmsRollingFileAppender extends org.apache.log4j.RollingFileAppender {
	
	private static File logRootDir = null;
	
	public static File getLogRootDir() {
		return logRootDir;
	}

	public static void setLogRootDir(File logRootDir) {
		SmsRollingFileAppender.logRootDir = logRootDir;
	}

	public SmsRollingFileAppender() {
		super();
		setEncoding(SmsConstants.SYS_ENCODING);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized void doAppend(LoggingEvent event) {		
		if (new File(fileName).exists() == false) {
			try {
				super.setFile(fileName, fileAppend, bufferedIO, bufferSize);
			} catch (IOException ignore) {
			}
		}
		super.doAppend(event);
	}

	public synchronized void setFile(final String fileName, boolean append, boolean bufferedIO, int bufferSize)
			throws IOException {
		if (!new File(fileName).isAbsolute()) {
			File logFile = new File(logRootDir, fileName);
			File parent = logFile.getParentFile();
			if ((parent != null) && (!parent.exists())) {
				if (!parent.mkdirs()) {
					throw new IOException("Logs dir can not create!");
				}
			}
			super.setFile(logFile.getAbsolutePath(), append, bufferedIO, bufferSize);
		}
	}
	
}
