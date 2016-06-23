/**
 * 
 */
package com.primeton.upcloud.ws.api.test;

import java.util.ArrayList;
import java.util.List;

import com.primeton.upcloud.ws.api.JobResult;
import com.primeton.upcloud.ws.api.ResourceManager;
import com.primeton.upcloud.ws.api.ResourceManagerFactory;
import com.primeton.upcloud.ws.api.StorageNasJobResult;
import com.primeton.upcloud.ws.api.StorageNasVolume;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class TestResourceManagerStorage {

	private static String wsUrl = "http://192.168.100.1/default/ResourceService";
	private static String username = "paas";
	private static String password = "000000";
	private static String nasZoneID = "RegionOne/evs1";
	private static String volumeName = "201310140008";
	private static String groupName = "upaas_group";
	private static int size = 1;
	
	private static ResourceManager manager = ResourceManagerFactory.getManager();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> whiteList = new ArrayList<String>();
		String ip0 = "192.168.100.30";
		String ip1 = "192.168.100.31";
		whiteList.add(ip0);
		whiteList.add(ip1);
		String volumeID = null;
		StorageNasJobResult snjr = null;
		
		String jobID = createStorageNasVolume(whiteList);
		
		while (true) {
			snjr = queryStorageNasVolumeJob(jobID);
			System.out.println(snjr);
			if (snjr != null && StorageNasJobResult.STATUS_SUCCESS.equals(snjr.getJobStatus())
					|| StorageNasJobResult.STATUS_FAIL.equals(snjr.getJobStatus())) {
				break;
			}
		}
		
		if(snjr!=null&&StorageNasJobResult.STATUS_SUCCESS.equals(snjr.getJobStatus())){
			volumeID = snjr.getNasVolume().getVolumeID();
			System.out.println("volumeID = " + volumeID);
		}
		
		StorageNasVolume snv = queryStorageNasVolumeDetail(volumeID);
		System.out.println("Query storageNasVolume :" + snv);
		
		boolean isOK = modifyStorageNasVolumeGroup(volumeID, "modified_upaas_group");
		System.out.println("modify group:-->"+isOK);
		/*
		isOK = modifyStorageNasVolumeSize(volumeID, 2);
		System.out.println("modify size:-->"+isOK);
		
		whiteList.add("192.168.100.32");
		isOK = modifyStorageNasVolumeWhiteList(volumeID, whiteList);
		System.out.println("modify whiteList:-->"+isOK);
		*/
		jobID = destoryStorageNasVolume(volumeID).getId();
		System.out.println(queryStorageNasVolumeJob(jobID).getJobStatus());
	}

	
	public static String createStorageNasVolume(List<String> whiteList){
		JobResult js = manager.createStorageNasVolume(wsUrl, username, password, nasZoneID, volumeName, groupName, size, whiteList);
		System.out.println(js);
		return js.getId();
	}
	
	public static StorageNasJobResult queryStorageNasVolumeJob(String jobID){
		return manager.queryStorageNasVolumeJob(wsUrl, username, password, jobID);
	}
	
	public static StorageNasVolume queryStorageNasVolumeDetail(String volumeID){
		return manager.queryStorageNasVolumeDetail(wsUrl, username, password, volumeID);
	}
	
	public static JobResult destoryStorageNasVolume(String volumeID){
		return manager.destoryStorageNasVolume(wsUrl, username, password, volumeID);
	}
	
	public static boolean modifyStorageNasVolumeGroup(String volumeID,String groupName){
		return manager.modifyStorageNasVolumeGroup(wsUrl, username, password, volumeID, groupName);
	}
	
	public static boolean modifyStorageNasVolumeSize(String volumeID,int size){
		return manager.modifyStorageNasVolumeSize(wsUrl, username, password, volumeID, size);
	}
	
	public static boolean modifyStorageNasVolumeWhiteList(String volumeID,List<String> whiteList){
		return manager.modifyStorageNasVolumeWhiteList(wsUrl, username, password, volumeID, whiteList);
	}
	
}
