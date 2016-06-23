/**
 * 
 */
package com.primeton.paas.cardbin.server.ws.impl;

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

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.cardbin.server.jdbc.ConnectionFactory;
import com.primeton.paas.cardbin.server.ws.ICardbinSync;
import com.primeton.paas.cardbin.spi.CardBinConstants;
import com.primeton.paas.cardbin.spi.CardBinRuntimeException;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@SuppressWarnings("restriction")
@WebService
public class CardbinSync implements ICardbinSync{

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

	private static ILogger logger = LoggerFactory.getLogger(CardbinSync.class);

	/**
	 * 
	 * @param ip
	 * @param port
	 */
	public static void initWsdl(String ip, int port) {
		if (ip == null || ip.isEmpty() || port <= 0) {
			logger.info("IP & wsPort is empty. init wsdl cancelled.");
		}

		String wsdl = "http://" + ip + ":" + port + "/cardbin/synch";
		Endpoint.publish(wsdl, new CardbinSync());
		logger.info("Success deploy wsdl. { " + wsdl + "?wsdl }.");
	}
	
	/* (none Javadoc)
	 * @see com.primeton.paas.cardbin.server.ws.ICardbinSync#doSynch(java.lang.String, java.lang.String)
	 */
	public String doSynch(String paraCode, String batchNo) {
		if (paraCode == null || batchNo == null || paraCode.isEmpty()
				|| paraCode.isEmpty()) {
			throw new RuntimeException("Synch cancelled! parameter is null.");
		}

		long begin = System.currentTimeMillis();
		logger.info("Begin synchonize {paraCode=" + paraCode + ",batchNo="
				+ batchNo + "}.");
		String filePath = getCardBinFileName(batchNo);
		logger.info("get cardbin data filePath ' " + filePath + " '.");

		try {
			String encoding = "GBk";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding); 

				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;

				List<String> buffList = new ArrayList<String>();

				while ((lineTxt = bufferedReader.readLine()) != null) {
					buffList.add(lineTxt);

					if (buffList.size() == CardBinConstants.BUFFER_SIZE) {
						executeAysn(buffList);
						buffList = new ArrayList<String>();
					}
				}

				executeAysn(buffList);
				read.close();
			} else {
				logger.info("Can not find cardbin data filePath { filePath:"
						+ filePath + "}.");
			}

		} catch (Exception e) {
			logger.error(e);
			throw new CardBinRuntimeException(e);
		}

		long end = System.currentTimeMillis();
		logger.info("End synchonize {paraCode=" + paraCode + ",batchNo="
				+ batchNo + "}. Time Spend " + (end - begin) / 1000
				+ " Seconds.");

		return "End synchonize {paraCode=" + paraCode + ",batchNo=" + batchNo
				+ "}.";
	}
	
	/**
	 * 
	 * @param dataList
	 * @throws SQLException
	 */
	public static void executeAysn(List<String> dataList) throws SQLException {
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
			if ("D".equals(operIn)) { //$NON-NLS-1$
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

	/**
	 * 
	 * @param batchNo
	 * @return
	 */
	public static String getCardBinFileName(String batchNo) {
		String dataPath = SystemProperties.getProperty(
				CardBinConstants.DATA_PATH_KEY,
				CardBinConstants.CARDBIN_DATA_FILE_PATH);
		if (dataPath == null || dataPath.trim().isEmpty()) {
			throw new RuntimeException(
					"Synch cancelled! cardbin data file dataPath is empty.");
		}
		return dataPath + File.separator
				+ CardBinConstants.CARDBIN_FILE_NAME_PREFIX + batchNo
				+ CardBinConstants.CARDBIN_FILE_NAME_SUFFIX;
	}
	
}
