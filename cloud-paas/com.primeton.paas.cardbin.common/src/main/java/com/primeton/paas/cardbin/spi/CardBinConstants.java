/**
 * 
 */
package com.primeton.paas.cardbin.spi;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CardBinConstants {
	
	public static final String TYPE = "CardBin";
	
	public static final String SYS_ENCODING = "UTF-8";
	
	public static final String NORMAL_ENTRY_CODE = "00";
	
	public static final String NO_CARD_ENTRY_CODE = "01";
	
    public static final String RESERVE_ENTRY_CODE = "92";
    
    public static final String WITHDRAW_ENTRY_CODE = "92";
    
    public static final String ONSPOT_ENTRY_CODE = "96";
	
    public static final String PRE_AUTH_CONDITION_MODE = "06";
    
    public static final String NORMAL_CONDITION_MODE = "00";
    
    public static final String PIN_MUST_CONDITION_MODE = "02";
    
    public static final String RESERVE_CONDITION_MODE = "67";
    
    public static final String WITHDRAW_CONDITION_MODE = "67";
    
    public static final String SD = "01";
    
    public static final String ISIM = "02";
    
    public static final String NFC_PHONE = "03";
    
    public static final String SIM_PASS = "04";
    
    public static final String NO_IC_CARD = "99";
    
    public static final String SPID_FOR_NO_ICCARD = "00000000";
    
    public static int TOTAL_MAX_CONSUME_AMOUNT = 1000000;

    public static final char BANKCARD_SUPPORT_SERVICE = '1';
    public static final char BANKCARD_NOT_SUPPORT_SERVICE = '0';
    public static final String BANKCARD_DEFAULT_SUPPORT_SERVICE = "0000000000000000";
    public static final String BANKCARD_ALL_SUPPORT_SERVICE = "1111111111111111";
    public static final String BANKCARD_STATS_NORMAL = "00";
    public static final String BANKCARD_STATS_DELETE = "01";

    public static final int WITHDRAW_AMOUNT_MAX = 100000;
    public static final int WITHDRAW_AMOUNT_MIN = 1000;
    
    public static final String INITIAL_CRED_NUM = "000000000000000000";
    
	public static final String FINANCE_CARDBIN_SERVICE_REMOTE_OBJECT_NAME = "financeCardBinServiceObject";
	
	public static final String SERVER_HOME_KEY = "CARDBIN_HOME";
	
	public static final String HOST_KEY = "CARDBIN_HOST";
	public static final String PORT_KEY = "CARDBIN_PORT";
	public static final String DATA_PATH_KEY = "CARDBIN_DATA_PATH";
	
	public static final String REMOTE_HOST_KEY = "REMOTE_HOST";
	public static final String REMOTE_USER_KEY = "REMOTE_USER";
	public static final String REMOTE_PWD_KEY = "REMOTE_PWD";
	public static final String HDFS_FILE_URL_KEY = "HDFS_FILE_URL";
	public static final String REMOTE_FILE_PATH_KEY = "REMOTE_FILE_PATH"; 
	public static final String TMP_FILE_PATH_KEY = "TMP_FILE_PATH"; 
	public static final String DEST_FILE_PATH_KEY = "DEST_FILE_PATH"; 
	public static final String SYNC_DAY_OF_MONTH_KEY = "SYNC_DAY_OF_MONTH"; 
	public static final String SYNC_HOUR_OF_DAY_KEY = "SYNC_HOUR_OF_DAY"; 
	public static final String SYNC_IS_ENABLE_KEY = "SYNC_IS_ENABLE"; 

	
	public static final String DEFAULT_REMOTE_HOST = "127.0.0.1";
	public static final String DEFAULT_REMOTE_USER = "root";
	public static final String DEFAULT_REMOTE_PWD = "UP_vm@2013";
	public static final String DEFAULT_HDFS_FILE_URL = "hdfs://144.240.11.8:8020";
	public static final String DEFAULT_REMOTE_FILE_PATH = "/opt/paas/workspace/cardbin";
	
	public static final String DEFAULT_TMP_FILE_PATH = "D:\\tmp";
	public static final String DEFAULT_DEST_FILE_PATH = "D:\\cardbin";
	
	public static final int DEFAULT_CARDBIN_SYNC_DAY = 10;  
	public static final int DEFAULT_CARDBIN_SYNC_HOUR = 1; 
	
	
	public static final String DEFAULT_HOST_VALUE = "127.0.0.1";
	public static final int DEFAULT_PORT_VALUE = 8010;
	
	public static final String WS_PORT_KEY = "CARDBIN_WS_PORT";
	public static final int DEFAULT_WS_PORT_VALUE = 8011;
	
	public static final String SYSTEM_JDBC_DRIVER_KEY = "JDBC_DRIVER";
	public static final String SYSTEM_JDBC_URL_KEY = "JDBC_URL";
	public static final String SYSTEM_JDBC_USER_KEY = "JDBC_USER";
	public static final String SYSTEM_JDBC_PASSWORD_KEY = "JDBC_PASSWORD";
	public static final String SYSTEM_JDBC_MIN_POOL_SIZE_KEY = "JDBC_MIN_POOL_SIZE";
	public static final String SYSTEM_JDBC_MAX_POOL_SIZE_KEY = "JDBC_MAX_POOL_SIZE";
	
	/**
	 * CardBin 
	 */
	public static final int CARDBIN_COLLUMS = 34; 
	
	public static final int CARBBIN_INDEX = 0; 
	public static final int SYNC_OPER_IN_INDEX = 24;
	public static final int REC_UPDATE_TIMESTAMP_INDEX = 28;
	public static final int REC_CREATE_TIMESTAMP_INDEX = 29;
	public static final int SYNC_STATUS_INDEX = 30;
	public static final int SYNC_BATCH_NO_INDEX = 31;
	public static final int PROC_STATUS_INDEX = 33;
	public static final int SUP_TIME_STAMP_INDEX = 32;
	
	public static final int BUFFER_SIZE = 500;
	
	public static final String CARDBIN_DATA_FILE_PATH = "/opt/upaas/workspace/cardbin"; 
	public static final String CARDBIN_FILE_NAME_PREFIX = "tbl_mcmgm_card_bin_"; 
	public static final String CARDBIN_FILE_NAME_SUFFIX = ".del"; 
	
	public static final int CARDBIN_SYNC_RETRY_TIMES = 3; 
	
}
