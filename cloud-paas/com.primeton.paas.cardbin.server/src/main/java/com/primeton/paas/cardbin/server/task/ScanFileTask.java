/**
 * 
 */
package com.primeton.paas.cardbin.server.task;

import java.io.File;
import java.util.Date;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.cardbin.spi.CardBinConstants;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ScanFileTask implements Runnable{

	private static ILogger log = LoggerFactory.getLogger(ScanFileTask.class);
	
	public static int count = 0;
	
	public void run() {
		while (true) {
			System.out.println("In scan data files task:" + new Date());
			
			try {
				doSync();
			} catch (Exception e) {
				log.error(" ========================== Cardbin synchronize failed =========================== " + new Date());
				log.error(e);
			}
			
			try {
				Thread.sleep(1000 * 60 * 2); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return
	 */
	public boolean doSync(){
		String tmpPath = SystemProperties.getProperty(CardBinConstants.TMP_FILE_PATH_KEY, CardBinConstants.DEFAULT_TMP_FILE_PATH);
		String destPath = SystemProperties.getProperty(CardBinConstants.DEST_FILE_PATH_KEY, CardBinConstants.DEFAULT_DEST_FILE_PATH);
		
		File tmpPathFile = new File(tmpPath);
		if (!tmpPathFile.exists()) {
			log.info("Can not find data files in temp path {tmpPath:" + tmpPath+"}.");
			return false;
		}
		
		File destPathFile = new File(destPath);
		if (!destPathFile.exists()) {
			destPathFile.mkdir();
		}
		
		long begin = System.currentTimeMillis();
		log.info("Begin parse data files {directory:" + tmpPath+"}." + new Date());

		CardbinSyncExec syncExec = new CardbinSyncExec();

		if (!syncExec.doParseFiles(tmpPath)) {
			log.warn("Parse Files failed.");
			return false;
		}
		
		syncExec.doAchiveFiles(tmpPath,destPath);
		
		long end = System.currentTimeMillis();
		log.info("End parse data files {directory:" + tmpPath+"}, Time spend "+ (end-begin) / 1000 + " Seconds.");
		return true;
	}
	
}
