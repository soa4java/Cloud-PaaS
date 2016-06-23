/**
 * 
 */
package com.primeton.upcloud.ws.api.test;

import java.util.ArrayList;
import java.util.List;

import com.primeton.upcloud.ws.api.DBBackUp;
import com.primeton.upcloud.ws.api.DBBackUpJobResult;
import com.primeton.upcloud.ws.api.DBJobResult;
import com.primeton.upcloud.ws.api.DBServer;
import com.primeton.upcloud.ws.api.JobResult;
import com.primeton.upcloud.ws.api.ResourceManager;
import com.primeton.upcloud.ws.api.ResourceManagerFactory;

/**
 * @author liming(mailto:li-ming@primeton.com)
 *
 */
public class TestResourceManagerDBaaS {
	
	private static String wsUrl = "http://172.16.25.51/default/services/DBaaSManageService?wsdl";
	private static String username = "upaas03";
	private static String password = "000000";
	private static String groupName = "upaas_group" ; 
	private static String perfId = "2015011913521746";
	private static String bizZoneId = "2015011913054834"; 
	
	/**storage type  */
	/*
	private static String SIZE_DAT = "size-dat"; 
	private static String SIZE_BIN = "size-bin"; 
	private static String SIZE_REL = "size-rel"; 
	private static String SIZE_RED = "size-red"; 
	*/
	
	/*user*/
	private static String instName = "paasdbtest04" ;    	 
	private static String dbVersion = "5.5";	 
	private static String charset = "utf8";	 
	private static boolean haEnable = false;	 
	private static String haConstruct = "";		 
	private static boolean hasStandyBy = false;			 
	private static String dbUser = "paasadmin";		 
	private static String dbPwd = "000000";					 
	private static String apUser = "paasap";		 
	private static String apPwd = "000000";					 
	private static List<String> whiteList = new ArrayList<String>(); 
	private static boolean backupEnable = false;					 
	private static String backupCycle = "";							 
	private static int backupDays = 1;								 
	
	private static ResourceManager manager = ResourceManagerFactory.getManager();
	
	public static void main(String[] args) {
		/*
		String jobID = createDBInstance();
		System.out.println("jobID"+jobID);
		DBJobResult rs = null;
		if(!jobID.equals("")){
			while (true) {
				rs = queryDBJob(jobID);
				System.out.println(rs);
				if (rs != null && DBJobResult.STATUS_SUCCESS.equals(rs.getStatus())
						|| DBJobResult.STATUS_FAIL.equals(rs.getStatus())) {
					break;
				}
				
				sleep(1000L);
			}
		}
		
		String dbId = "";
		if (rs != null && DBJobResult.STATUS_SUCCESS.equals(rs.getStatus())) {
		    dbId = rs.getServers().get(0).getId();
		    String name = rs.getServers().get(0).getName();
		    String url = rs.getServers().get(0).getUrl();
		    System.out.println("dbID = " + dbId);
			System.out.println("name = " + name);
			System.out.println("url = " + url);
		}
		
		if(!dbId.equals("")){
			DBServer server = queryDBInstance(dbId);
			System.out.println("\nQuery dbID : " + server + "\n");
			
		    String name = server.getName();
		    String url = server.getUrl();
		    System.out.println("dbID = " + dbId);
			System.out.println("name = " + name);
			System.out.println("url = " + url);
		}
		
		String dbId = "2015020316511983";
		JobResult startRS = startDBInstance(dbId);
		//JobResult startRS = stopDBInstance(dbId);
		if(!startRS.getId().equals("")){
			while (true) {
				DBJobResult rs = queryDBJob(startRS.getId());
				System.out.println(rs);
				if (rs != null && DBJobResult.STATUS_SUCCESS.equals(rs.getStatus())
						|| DBJobResult.STATUS_FAIL.equals(rs.getStatus())) {
					break;
				}
				
				sleep(1000L);
			}
		}
		
		String dbId = "2015020316511983";
		List<String> whiteList = new ArrayList<String>();
		whiteList.add("127.0.0.1");
		whiteList.add("126.0.0.1");
		boolean bln  = updateWhiteList(dbId,whiteList);
		if(bln){
			System.out.println("Succes");
			DBServer server = queryDBInstance(dbId);
			System.out.println("\nQuery dbID : " + server + "\n");
		}
		*/
		
		String dbId = "2015020410321840";//test04
		String backupAlias = "backuptest005";
		int backupDays = 2;
		
		DBBackUpJobResult rs = null;
		JobResult backupRs = backupDBInstance(dbId, backupAlias, backupDays);
		if(backupRs!=null&&!backupRs.getId().equals("")){
			while (true) {
				rs = queryDBInstanceBackupJob(backupRs.getId());
				if (rs != null && DBJobResult.STATUS_SUCCESS.equals(rs.getStatus())
						|| DBJobResult.STATUS_FAIL.equals(rs.getStatus())) {
					break;
				}
				
				sleep(1000L);
			}
		}
		if(rs!=null&&DBBackUpJobResult.STATUS_SUCCESS.equals(rs.getStatus())){
			System.out.println(rs.getDbBackup());
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		DBBackUp dbBackUpInfo = queryDBBackup(rs.getDbBackup().getId());
		//	DBBackUp dbBackUpInfo = queryDBBackup(162);
		if(dbBackUpInfo!=null){
			System.out.println(dbBackUpInfo);
		}
//		
//		JobResult restoreRs = restoreDBInstance("2015020410321840", 163);
//		DBJobResult restoreJobRs = null;
//		if(restoreRs!=null&&!restoreRs.getId().equals("")){
//			while (true) {
//				restoreJobRs = queryDBInstanceRestoreJob(restoreRs.getId());
//				if (restoreJobRs != null && DBJobResult.STATUS_SUCCESS.equals(restoreJobRs.getStatus())	|| DBJobResult.STATUS_FAIL.equals(restoreJobRs.getStatus())) {
//					break;
//				}
//				
//				sleep(1000L);
//			}
//		}
//		if(restoreJobRs!=null){
//			System.out.println(restoreJobRs.getServers());
//		}
		
//		JobResult enlargeRs = enlargePerformance("2015020410321840", perfId);//perfId_2C4G
//		System.out.println(enlargeRs);
//		if(enlargeRs!=null&&!enlargeRs.getId().equals("")){
//			while (true) {
//				DBServer rs = queryDBInstance("2015020410321840");
//				if (rs != null) {
//					String newperfId = rs.getAttributes().get("hostPerfId");
//					System.out.println(newperfId);
////				if(newperfId.equals(perfId)){
////					break;
////				}
//				}
//				sleep(1000L);
//			}
//		}
		
//		JobResult enlargeSTRs = enlargeStorage("2015020410321840", SIZE_BIN, 15);
//		if(enlargeSTRs!=null&&!enlargeSTRs.getId().equals("")){
//			while (true) {
//				DBServer rs = queryDBInstance("2015020410321840");
//				if (rs != null) {
//					double dataSizeDouble = Double.parseDouble(rs.getAttributes().get("binLogSize"));
//					int dataSize = (int)dataSizeDouble;
//					System.out.println(dataSize);
//					if(dataSize==15){
//						break;
//					}
//				}
//				sleep(1000L);
//			}
//		}
				
//		List<String> list = new ArrayList<String>();
//		list.add("172.16.26.41");
//		list.add("127.0.0.1");
//		
//		boolean rs = updateWhiteList("2015020410321840", whiteList);
//		System.out.println(rs);
		
//		if(!dbId.equals("")){
//			JobResult rs = destroyDBInstance(dbId);
//			System.out.println("destroyDBInstance:" +rs);
//		}
		
	}
	
	public static String createDBInstance() {
		JobResult rs = manager.createDBInstance(wsUrl, username, password, instName, groupName, perfId, dbVersion, charset, bizZoneId, haEnable, haConstruct, hasStandyBy, dbUser, dbPwd, apUser, apPwd, whiteList, backupEnable, backupCycle, backupDays);
		System.out.println(rs);
		return rs.getId();
	}
	
	public static DBJobResult queryDBJob(String jobID) {
		return manager.queryDBJob(wsUrl, username, password, jobID);
	}
	
	public static JobResult destroyDBInstance(String dbId) {
		return manager.destroyDBInstance(wsUrl, username, password, dbId);
	}
	
	public static DBServer queryDBInstance(String dbId) {
		return manager.queryDBInstance(wsUrl, username, password, dbId);
	}
	
	public static JobResult startDBInstance(String instId) {
		return manager.startDBInstance(wsUrl, username, password, instId);
	}
	
	public static JobResult stopDBInstance(String instId) {
		return manager.stopDBInstance(wsUrl, username, password, instId);
	}
	
	public static JobResult backupDBInstance(String instId,String backupAlias,int backupDays){
		return manager.backupDBInstance(wsUrl, username, password, instId, backupAlias, backupDays);
	}
	
	public static DBBackUpJobResult queryDBInstanceBackupJob(String jobID){
		return manager.queryDBInstanceBackupJob(wsUrl, username, password, jobID);
	}
	
	public static DBBackUp queryDBBackup(int backupUniqueId){
		return manager.queryDBBackup(wsUrl, username, password, backupUniqueId);
	}
	
	public static JobResult restoreDBInstance(String instId,int backupUniqueId){
		return manager.restoreDBInstance(wsUrl, username, password, instId, backupUniqueId);
	}
	
	public static DBJobResult queryDBInstanceRestoreJob(String jobID){
		return manager.queryDBInstanceRestoreJob(wsUrl, username, password, jobID);
	}
	
	public static JobResult enlargePerformance(String instId,String perfId){
		return manager.enlargePerformance(wsUrl, username, password, instId, perfId);
	}
	
	public static JobResult enlargeStorage(String instId,String storageType,int size){
		return manager.enlargeStorage(wsUrl, username, password, instId, storageType, size);
	}
	
	public static boolean updateWhiteList(String instId, List<String> whiteList){
		return manager.updateWhiteList(wsUrl, username, password, instId, whiteList);
	}
	
	private static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
