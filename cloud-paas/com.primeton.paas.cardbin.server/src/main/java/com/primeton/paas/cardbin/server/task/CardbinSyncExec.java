/**
 * 
 */
package com.primeton.paas.cardbin.server.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.cardbin.server.jdbc.ConnectionFactory;
import com.primeton.paas.cardbin.server.util.Exec;
import com.primeton.paas.cardbin.spi.CardBinConstants;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CardbinSyncExec {

	public static String insert_sql = "INSERT INTO PAS_CARDBIN (CARD_BIN,CARD_CN_NM,CARD_EN_NM, ISS_INS_ID_CD, ISS_INS_CN_NM ," +
	" ISS_INS_EN_NM, CARD_BIN_LEN, CARD_ATTR, CARD_CATA ,  CARD_CLASS ,  CARD_BRAND ,  CARD_PROD ,  CARD_LVL , CARD_MEDIA," +
	" IC_APP_TP, PAN_LEN , PAN_SAMPLE , PAY_CURR_CD1 , PAY_CURR_CD2, PAY_CURR_CD3 , CARD_BIN_PRIV_BMP, PUBLISH_DT,CARD_VFY_ALGO ," +
	" FRN_TRANS_IN , OPER_IN ,EVENT_ID ,REC_ID , REC_UPD_USR_ID , REC_UPD_TS ,REC_CRT_TS,SYNC_ST, SYNC_BAT_NO , SUP_TS , PROC_ST )" +
	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static String replace_sql = "REPLACE INTO PAS_CARDBIN (CARD_BIN,CARD_CN_NM,CARD_EN_NM, ISS_INS_ID_CD, ISS_INS_CN_NM ," +
	" ISS_INS_EN_NM, CARD_BIN_LEN, CARD_ATTR, CARD_CATA ,  CARD_CLASS ,  CARD_BRAND ,  CARD_PROD ,  CARD_LVL , CARD_MEDIA," +
	" IC_APP_TP, PAN_LEN , PAN_SAMPLE , PAY_CURR_CD1 , PAY_CURR_CD2, PAY_CURR_CD3 , CARD_BIN_PRIV_BMP, PUBLISH_DT,CARD_VFY_ALGO ," +
	" FRN_TRANS_IN , OPER_IN ,EVENT_ID ,REC_ID , REC_UPD_USR_ID , REC_UPD_TS ,REC_CRT_TS,SYNC_ST, SYNC_BAT_NO, SUP_TS , PROC_ST )" +
	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static String delete_sql = "DELETE FROM PAS_CARDBIN WHERE CARD_BIN = ?";

	private static ILogger logger = LoggerFactory.getLogger(CardbinSyncExec.class);
	
	private static final String SCRIPT_SCP = "cardbinscp.sh"; 
	
	/**
	 * 
	 */
	public boolean doSync(){
		logger.info("call at " + new Date());
		
		String remotePath = SystemProperties.getProperty(CardBinConstants.REMOTE_FILE_PATH_KEY, CardBinConstants.DEFAULT_REMOTE_FILE_PATH);
		String tmpPath = SystemProperties.getProperty(CardBinConstants.TMP_FILE_PATH_KEY, CardBinConstants.DEFAULT_TMP_FILE_PATH);
		String destPath = SystemProperties.getProperty(CardBinConstants.DEST_FILE_PATH_KEY, CardBinConstants.DEFAULT_DEST_FILE_PATH);
		
		File destPathFile = new File(destPath);
		if (!destPathFile.exists()) {
			destPathFile.mkdir();
		}

		if (!doCopyFile(remotePath,tmpPath)){
			logger.warn("Copy file failed.");
			return false;
		}
		
		if(!doCompareFiles(tmpPath, destPath)){
			logger.warn("Files not updated, synchronize cancelled.");
			
			doAchiveFiles(tmpPath,destPath);
			return true;
		}
		
		if (!doParseFiles(tmpPath)) {
			logger.warn("Parse Files failed.");
			return false;
		}
		
		doAchiveFiles(tmpPath,destPath);
		return true;
	}

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public boolean doParseFiles(String filePath) {
		File tmpPathFile = new File(filePath);
		File[] files = tmpPathFile.listFiles();
		
		if (files == null || files.length <1) {
			logger.info("Parse files cancelled, file director is empty.{filePath:"+ filePath+"}.");
			return true;
		}

		for (File _file : files) {
			if (_file.isDirectory()) {
				return doParseFiles(_file.getAbsolutePath());
			}
			if (_file.isFile() && _file.exists()) {
				
				String fileName = _file.getName();
				if (fileName.startsWith(".")) {
					logger.info("file {[" + fileName
							+ "]} is not cardbin data file");
					continue;
				}
				long begin = System.currentTimeMillis();
				logger.info("Begin process cardbin data filePath ' " + _file + " '.");
				
				try {
					String encoding = "utf-8";
					
					InputStreamReader read = new InputStreamReader(new FileInputStream(_file),encoding);//���ǵ������ʽ
					
					BufferedReader bufferedReader = new BufferedReader(read);
					String lineTxt = null;
					
					List<String> buffList = new ArrayList<String>();
					
					while((lineTxt = bufferedReader.readLine()) != null){
						buffList.add(lineTxt);
						
						if (buffList.size() == CardBinConstants.BUFFER_SIZE) {
							executeAysn(buffList);
							buffList = new ArrayList<String>();
						}
					}
					
					executeAysn(buffList);
					read.close();
				} catch (Exception e) {
					logger.error(e);
					return false;
				}
				long end = System.currentTimeMillis();
				logger.info("End process cardbin data file '" +_file + "' ,Time Spend " + (end - begin)/1000 + " Seconds.");

			} else{
				logger.info("Can not find cardbin data filePath { filePath:" + _file + "}." );
			}
		}
		
		logger.info("End synchnize.");
		return true;
	}


	/**
	 * 
	 * @param tmpPath
	 * @param destPath
	 */
	public void doAchiveFiles(String tmpPath, String destPath) {
		String cmd_cp = "cp -r " + tmpPath + " " + destPath ;
		long begin = System.currentTimeMillis();
		logger.info("Begin mv files from " + tmpPath + " to " + destPath + ".");
		
		
		int i = 3;
		Exec exec = new Exec();
		
		while (i > 0) {
			try {
				exec.execute(cmd_cp, 1000 * 2);
				
				break;
			} catch (Exception e) {
				logger.error(e);
			}
			
			logger.warn("Copy files from " + tmpPath + " to " + destPath +" failed. Retry!");
			i--;
		}
		
		String cmd_rm = "rm -rf " + tmpPath ;
		i=3;
		while (i > 0) {
			try {
				exec.execute(cmd_rm, 1000 * 2);
				
				break;
			} catch (Exception e) {
				logger.error(e);
			}
			
			logger.warn("Remove files " + tmpPath + "failed. Retry!");
			i--;
		}
		
		long end = System.currentTimeMillis();
		logger.info("End mv files from " + tmpPath + " to " + destPath + ", Time spends " + (end-begin)/ 10000 + " Seconds.");
	}

	/**
	 * 
	 * @param srcPath
	 * @param dstPath
	 * @return
	 */
	public  boolean doCompareFiles(String srcPath, String dstPath) {
		if (srcPath == null || srcPath.isEmpty() || dstPath == null || dstPath.isEmpty()) {
			return true; 	
		}
		
		long begin = System.currentTimeMillis();
		logger.info("Begin compare files if changed!");
		
		File srcPathFile = new File(srcPath);
		File[] srcFiles = srcPathFile.listFiles();
		
		if (!srcPathFile.exists() || srcFiles== null) {
			return true;
		}
		

		for (File _srcFile : srcFiles) {
			if (_srcFile.isDirectory()) {
				return doCompareFiles(_srcFile.getAbsolutePath(), dstPath);
			}
			if (_srcFile.isFile() && _srcFile.exists()) {
				String _srcName = _srcFile.getName();
				File _dstFile = getFileByName(_srcName, dstPath);
				if (_dstFile == null) {
					return true;
				}
				long _srcSize = _srcFile.length(); // bytes
				long _dstSize = _dstFile.length();
				
				if (_srcSize != _dstSize) {
					return true;
				}
			}
		}
		long end = System.currentTimeMillis();
		logger.info("End compare files ,Time Spends "+ (end - begin)/1000 + " seconds.");
		
		return false;
	}
	
	private File getFileByName(String fileName, String filePath){
		File dstPathFile = new File(filePath);
		
		if (!dstPathFile.exists()) {
			return null;
		}
		
		if (dstPathFile.isFile() && fileName.equals(dstPathFile.getName())) {
			return dstPathFile;
		}
		
		File[] dstFiles = dstPathFile.listFiles();
		if (dstFiles == null || dstFiles.length < 1) {
			return null;
		}
		
		for (File _file : dstFiles){
			if (!_file.exists()) {
				continue ;
			}
			if (_file.isDirectory()) {
				return getFileByName(fileName, _file.getAbsolutePath());
			}
			String _name = _file.getName();
			if (fileName.equals(_name)) {
				return _file;
			}
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param remotePath
	 * @param tmpPath
	 * @return
	 */
	public boolean doCopyFile(String remotePath, String tmpPath) {
		String remoteIp = SystemProperties.getProperty(CardBinConstants.REMOTE_HOST_KEY, CardBinConstants.DEFAULT_REMOTE_HOST);
		String remoteUser = SystemProperties.getProperty(CardBinConstants.REMOTE_USER_KEY, CardBinConstants.DEFAULT_REMOTE_USER);
		String remotePwd = SystemProperties.getProperty(CardBinConstants.REMOTE_PWD_KEY, CardBinConstants.DEFAULT_REMOTE_PWD);

		logger.info("Copy remote file: " + remotePath);
		// ./cardbinscp.sh 172.16.26.42 root UP_vm@2013 /opt/paas/workspace/cardbin /opt/paas/workspace/cardbin

		String cmd = "/opt/upaas/bin/CardBin/bin/" + SCRIPT_SCP + " "
					+ remoteIp + " " 
					+ remoteUser  + " " 
					+ remotePwd + " "
					+ remotePath  + "  "
					+ tmpPath ;
		
		logger.info("Will Execute : " + cmd);
		
		File tmpPathFile = new File(tmpPath);
		Exec exec = new Exec();
		
		int i = 3;
		while (i > 0) {
			try {
				exec.execute(cmd, 1000 * 10);
				
				if(tmpPathFile.exists()  && tmpPathFile.isDirectory()  && tmpPathFile.listFiles() != null) {
					logger.info("Copy successed.");
					return true;
				}
			} catch (Exception e) {
				logger.error(e);
			}
			
			logger.warn("Copy failed, retry!");
			i--;
		}
		
		if (!tmpPathFile.exists() || !tmpPathFile.isDirectory() || tmpPathFile.listFiles()== null  || tmpPathFile.listFiles().length < 1) {
			logger.info("Copy failed from '" + remoteIp + "' failed .{remotePath:"+remotePath + ", localPath:"+tmpPath+"}.");
			return false;
		}
		
		return false;
	}

	
	/**
	 * 
	 * @param dataList
	 * @throws SQLException
	 */
	public void executeAysn(List<String> dataList) throws SQLException{
		if (dataList == null || dataList.isEmpty()) {
			return;
		}
		
		List<String> updateList = new ArrayList<String>();
		List<String> deleteList = new ArrayList<String>();
		
		for (String _rowLine : dataList) {
			String[] collums = _rowLine.split(",");
			if (collums == null || collums.length != CardBinConstants.CARDBIN_COLLUMS) {
				continue;
			}
			String operIn = collums[CardBinConstants.SYNC_OPER_IN_INDEX];
			operIn = operIn.replace("\"", "").trim();
			if ("D".equals(operIn)) {
				deleteList.add(_rowLine);
			} else {
				updateList.add(_rowLine);
			}
		}
		Connection con = null;
		PreparedStatement stmt = null;
		
		try {
			con = ConnectionFactory.getConnection();
			con.setAutoCommit(false);
			
			if (deleteList != null && !deleteList.isEmpty()) {
				logger.info("delete rows:" + deleteList.size());
				
				stmt = con.prepareStatement(delete_sql);
				for (String _data : deleteList) {
					String[] cols = _data.split(",");
					if (cols == null || cols.length < 1) {
						continue;
					}
					String cardBin = cols[CardBinConstants.CARBBIN_INDEX];
					cardBin = cardBin.replace("\"", "").trim();
					stmt.setObject(1, cardBin);
					stmt.addBatch();
				}
				stmt.executeBatch();
			}
			
			if (updateList != null && !updateList.isEmpty()) {
				stmt = con.prepareStatement(replace_sql);
				logger.info("update rows:" + updateList.size());
				for (String _data : updateList) {
					String[] cols = _data.split(",");
					if (cols == null || cols.length != CardBinConstants.CARDBIN_COLLUMS) {
						continue;
					}
					for (int i = 0,len2 = cols.length; i < len2; i++) {
						String _col = cols[i];
						_col = _col.replace("\"", "").trim();
						
						if (i == CardBinConstants.SUP_TIME_STAMP_INDEX) {
							stmt.setObject(i+1, new Date());
						} else {
							stmt.setObject(i+1, _col);
						}
					}
					stmt.addBatch();
				}
				
				stmt.executeBatch();
			}
			con.commit();
		} catch (SQLException e) {
			logger.error(e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				logger.error(e1);
			}
			
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}

}
