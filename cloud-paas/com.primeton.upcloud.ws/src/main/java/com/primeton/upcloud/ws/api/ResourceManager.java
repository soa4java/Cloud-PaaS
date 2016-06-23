/**
 * 
 */
package com.primeton.upcloud.ws.api;

import java.util.List;


/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface ResourceManager {

	/**
	 * 
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param bizZoneID
	 * @param groupName
	 * @param imageID
	 * @param profileID
	 * @param number
	 * @return
	 */
	JobResult createVM(String wsUrl, String username, String password, String bizZoneID, String groupName, String imageID, String profileID, int number);
	
	/**
	 * 
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param jobID
	 * @return
	 */
	VmJobResult queryVMJob(String wsUrl, String username, String password, String jobID);
	
	/**
	 * 
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param vmID
	 * @return
	 */
	JobResult destroyVM(String wsUrl, String username, String password, String vmID);
	
	/**
	 * 
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param vmID
	 * @return
	 */
	VServer queryVMDetail(String wsUrl, String username, String password, String vmID);
	
	/**
	 * 
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param vmID
	 * @param groupName
	 * @return
	 */
	boolean modifyVMGroup(String wsUrl, String username, String password, String vmID, String groupName);
	
	/**
	 * 
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param vmID
	 * @param profileID
	 * @return
	 */
	boolean modifyVMProfile(String wsUrl, String username, String password, String vmID, String profileID);
	
	/**
	 * 
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param vmID
	 * @param profileID
	 * @return
	 */
	JobResult upgradeVM(String wsUrl, String username, String password,
			String vmID, String profileID);
	
	/**
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param nasZoneID
	 * @param volumeName
	 * @param groupName
	 * @param size
	 * @param whiteList
	 * @return
	 */
	JobResult createStorageNasVolume(String wsUrl,String username,String password,String nasZoneID,String volumeName,String groupName,int size,List<String> whiteList);
	
	/**
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param volumeID
	 * @return
	 */
	JobResult destoryStorageNasVolume(String wsUrl,String username,String password,String volumeID);
	
	/**
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param jobID
	 * @return
	 */
	StorageNasJobResult queryStorageNasVolumeJob(String wsUrl,String username,String password,String jobID);
	
	/**
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param volumeID
	 * @return
	 */
	StorageNasVolume queryStorageNasVolumeDetail(String wsUrl,String username,String password,String volumeID);
	
	/**
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param volumeID
	 * @param groupName
	 * @return
	 */
	boolean modifyStorageNasVolumeGroup(String wsUrl,String username,String password,String volumeID,String groupName);
	
	/**
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param volumeID
	 * @param size
	 * @return
	 */
	boolean modifyStorageNasVolumeSize(String wsUrl,String username,String password,String volumeID,int size);
	
	/**
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param volumeID
	 * @param whiteList
	 * @return
	 */
	boolean modifyStorageNasVolumeWhiteList(String wsUrl,String username,String password,String volumeID,List<String> whiteList);
	
	
	/**
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param instName
	 * @param groupName
	 * @param perfId
	 * @param dbVersion
	 * @param charset
	 * @param bizZoneId
	 * @param haEnable
	 * @param haConstruct
	 * @param hasStandyBy
	 * @param dbUser
	 * @param dbPwd
	 * @param apUser
	 * @param apPwd
	 * @param whiteList
	 * @param backupEnable
	 * @param backupCycle
	 * @param backupDays
	 * @return
	 */
	public JobResult  createDBInstance(String wsUrl, String username,String password,String instName,String groupName,String perfId,String dbVersion,String charset,String bizZoneId,boolean haEnable,String haConstruct,boolean hasStandyBy,String dbUser,String dbPwd,String apUser,String apPwd,List<String> whiteList,boolean backupEnable,String backupCycle,int backupDays);
	
	/**
	 * @param wsUrl
	 * @param username
	 * @param password
	 * @param jobID
	 * @return
	 */
	public DBJobResult queryDBJob(String wsUrl, String username,String password, String jobID);
	
	/**
	 * @param wsUrl
	 * @param userName
	 * @param password
	 * @param instId
	 * @return
	 */
	public DBServer queryDBInstance(String wsUrl,String userName, String password, String instId) ;
	
	/**
	 * @param wsUrl
	 * @param userName
	 * @param password
	 * @param instId
	 * @return
	 */
	public JobResult destroyDBInstance(String wsUrl, String userName, String password, String instId);
	
	/**
	 * @param wsUrl
	 * @param userName
	 * @param password
	 * @param instId
	 * @return
	 */
	public JobResult startDBInstance(String wsUrl, String userName, String password, String instId) ;
	
	/**
	 * @param wsUrl
	 * @param userName
	 * @param password
	 * @param instId
	 * @return
	 */
	public JobResult stopDBInstance(String wsUrl, String userName, String password, String instId) ;
	
	/**
	 * @param wsUrl
	 * @param userName
	 * @param password
	 * @param instId  
	 * @param backupAlias  
	 * @param backupDays	 
	 * @return				
	 */
	public JobResult backupDBInstance(String wsUrl,String userName,String password,String instId,String backupAlias,int backupDays);
	
	/**
	 * @param wsUrl
	 * @param userName
	 * @param password
	 * @param jobID
	 * @return
	 */
	public DBBackUpJobResult queryDBInstanceBackupJob(String wsUrl,String userName,String password,String jobID);
	
	/**
	 * 
	 * @param wsUrl
	 * @param userName
	 * @param password
	 * @param uniqueId
	 * @return
	 */
	public DBBackUp queryDBBackup(String wsUrl,String userName,String password,int backupUniqueId);
	
	/**
	 * 
	 * @param wsUrl
	 * @param userName
	 * @param password
	 * @param instId
	 * @param uniqueId
	 * @return
	 */
	public JobResult restoreDBInstance(String wsUrl,String userName,String password,String instId,int backupUniqueId);
	
	/**
	 * 
	 * @param wsUrl
	 * @param userName
	 * @param password
	 * @param jobID
	 * @return
	 */
	public DBJobResult queryDBInstanceRestoreJob(String wsUrl,String userName,String password,String jobID);
	
	/**
	 * 
	 * @param wsUrl
	 * @param userName
	 * @param password
	 * @param instId
	 * @param perfId
	 * @return
	 */
	public JobResult enlargePerformance(String wsUrl,String userName,String password,String instId,String perfId);
	
	/**
	 * 
	 * @param wsUrl
	 * @param userName
	 * @param password
	 * @param instId
	 * @param storageType
	 * @param size
	 * @return
	 */
	public JobResult enlargeStorage(String wsUrl,String userName,String password,String instId,String storageType,int size);
	
	/**
	 * @param wsUrl
	 * @param userName
	 * @param password
	 * @param instId
	 * @param whiteList
	 * @return
	 */
	public boolean updateWhiteList(String wsUrl, String userName, String password, String instId, List<String> whiteList);
	
}
