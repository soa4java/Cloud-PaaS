/**
 * 
 */
package com.primeton.paas.cardbin.server.task;

//import java.net.URI;
import java.util.Calendar;
import java.util.Date;

/*import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;*/
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.cardbin.spi.CardBinConstants;

/**
 * 
 * @author liyp
 */
public class FetchDataFilesTask implements Runnable {

	private static ILogger log = LoggerFactory.getLogger(FetchDataFilesTask.class);
	
	public static String SYNC_STATUS_WAITING = "Waiting";
	
	public static String SYNC_STATUS_SUCCESS = "Success";
	
	public static String SYNC_STATUS_FAILED = "Failed";
	
	public static String syncStatus = SYNC_STATUS_WAITING;
	
	public static int count = 0;
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (true) {
			log.info("In Fetch files task:" + new Date());
			
			Calendar now = Calendar.getInstance();  
			int now_day = now.get(Calendar.DAY_OF_MONTH) ; 
			int now_hour = now.get(Calendar.HOUR_OF_DAY); 
			
			String day = SystemProperties.getProperty(CardBinConstants.SYNC_DAY_OF_MONTH_KEY, ""); 
			int sync_day = day==null || day.isEmpty() ? CardBinConstants.DEFAULT_CARDBIN_SYNC_DAY : Integer.parseInt(day);
			if (sync_day <= 0) {
				sync_day = CardBinConstants.DEFAULT_CARDBIN_SYNC_DAY;
			}
			log.info("CardBin data synchronize day :" + sync_day);
			
			String hour = SystemProperties.getProperty(CardBinConstants.SYNC_HOUR_OF_DAY_KEY, "");//
			int sync_hour = hour==null || hour.isEmpty() ? CardBinConstants.DEFAULT_CARDBIN_SYNC_HOUR : Integer.parseInt(hour);
			log.info("CardBin data synchronize hour :" + sync_hour);
			
			log.info("CardBin data synchronize status :" + syncStatus);
			
			if (now_day == sync_day) {
				if (!syncStatus.equals(SYNC_STATUS_SUCCESS) && count < CardBinConstants.CARDBIN_SYNC_RETRY_TIMES) {
					
					if ((now_hour-sync_hour >= 0 && now_hour-sync_hour < 1) || sync_hour <= 0) {
						try {
							if (doFetchFile()) {
								syncStatus = SYNC_STATUS_SUCCESS;
							} else {
								syncStatus = SYNC_STATUS_FAILED;
							}
						} catch (Exception e) {
							log.error(" ========================== Get Remote Data File Failed =========================== " + new Date());
							log.error(e);
							syncStatus = SYNC_STATUS_FAILED;
						}
						count++ ;
					}
				}
			} else {
				count = 0; 
				if (syncStatus.equals(SYNC_STATUS_SUCCESS)) {
					syncStatus = SYNC_STATUS_WAITING;
				}
			}
			
			try {
				Thread.sleep(1000 * 60 * 2); // 2 m
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 */
	private boolean doFetchFile(){
		long begin = System.currentTimeMillis();
		log.info("Begin get remote cardbin data failes." + new Date());
		
		String hdfsUrl = SystemProperties.getProperty(CardBinConstants.HDFS_FILE_URL_KEY, CardBinConstants.DEFAULT_HDFS_FILE_URL);
		String remotePath = SystemProperties.getProperty(CardBinConstants.REMOTE_FILE_PATH_KEY, CardBinConstants.DEFAULT_REMOTE_FILE_PATH);
		String tmpPath = SystemProperties.getProperty(CardBinConstants.TMP_FILE_PATH_KEY, CardBinConstants.DEFAULT_TMP_FILE_PATH);
		
		log.info("HDFS Url :" + hdfsUrl);
		log.info("remote file Path :" + remotePath);
		log.info("local temp file Path :" + tmpPath);
		
		if (getAllFilesFromHDFS(hdfsUrl,remotePath,tmpPath)) {
			long end = System.currentTimeMillis();
			log.info("End get remote cardbin data files, Time spend "+ (end-begin) / 1000 + " Seconds.");
			return true;
		}
		log.info("failed get remote cardbin data files.");
		return false;
	}
	
	
	/**
	 * 
	 * @param hdfsUrl
	 * @param inputPath
	 * @param outputPath
	 * @return
	 */
	public boolean getAllFilesFromHDFS(String hdfsUrl,String inputPath,String outputPath) {
		// FIXME Comment by ZhongWen.Li
		/*String hdfsPath = inputPath;
		Configuration config = new Configuration();
		//	config.set("fs.default.name", "hdfs://172.17.238.182:8020");
		config.set("fs.default.name", hdfsUrl);
		try{
			FileSystem fs = FileSystem.get(URI.create(hdfsPath), config);
			FileSystem local = FileSystem.getLocal(config);
			FileStatus[] status = fs.listStatus(new Path(hdfsPath));
			Path[] listedPaths = FileUtil.stat2Paths(status);
			if (listedPaths == null || listedPaths.length <1) {
				log.warn("Can not find cardbin data files!!!!!!!!!!");
				return false;
			}
			for (Path p : listedPaths) {
				FileStatus fileStatusTest = fs.getFileStatus(p);
				if (fileStatusTest.isDir() == false) {
					String fileName = p.getName();
					log.info("Cardbin data fileName:" + fileName);
					if (!outputPath.endsWith("/")){
						outputPath = outputPath + "/" + fileName;
					} else {
						outputPath = outputPath + fileName;
					}
					FSDataInputStream in = fs.open(p);
					FSDataOutputStream out = local.create(new Path(outputPath));
					// copy
					IOUtils.copyBytes(in, out, 4096, true);
				}
			}
		} catch(Exception e) {
			log.error(e);
			return false;
		}
		*/
		return true;
	}
	
}
