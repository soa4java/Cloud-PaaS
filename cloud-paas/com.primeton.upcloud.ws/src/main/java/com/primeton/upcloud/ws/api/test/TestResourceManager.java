/**
 * 
 */
package com.primeton.upcloud.ws.api.test;

import com.primeton.upcloud.ws.api.JobResult;
import com.primeton.upcloud.ws.api.ResourceManager;
import com.primeton.upcloud.ws.api.ResourceManagerFactory;
import com.primeton.upcloud.ws.api.VServer;
import com.primeton.upcloud.ws.api.VmJobResult;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class TestResourceManager {
	
	private static String wsUrl = "http://192.168.100.1/default/ResourceService";
	private static String username = "paas";
	private static String password = "000000";
	private static String bizZoneID = "2013091610242115";
	private static String groupName = "upaas_group";
	private static String imageID = "2013091610295010";
	private static String profileID = "2012090100000006";
	private static int number = 1;
	
	private static ResourceManager manager = ResourceManagerFactory.getManager();
	
	public static void main(String[] args) {
		
		String jobID = createVM(number);
		
		String vmID = null;
		VmJobResult rs = null;
		while (true) {
			rs = queryVmJob(jobID);
			System.out.println(rs);
			if (rs != null && VmJobResult.STATUS_SUCCESS.equals(rs.getStatus())
					|| VmJobResult.STATUS_FAIL.equals(rs.getStatus())) {
				break;
			}
			
			sleep(1000L);
		}
		
		if (rs != null && VmJobResult.STATUS_SUCCESS.equals(rs.getStatus())) {
			vmID = rs.getServers().get(0).getId();
			System.out.println("vmID = " + vmID);
		}
		
		VServer server = queryVMDetail(vmID);
		System.out.println("\nQuery vm : " + server + "\n");
		
		boolean isOK = modifyVMGroup(vmID, "lizhongwen");
		System.out.println(isOK);
		
		isOK = modifyVMProfile(vmID, "2012090100000008");
		System.out.println(isOK);
		
		//	sleep(60000L);
		
		System.out.println(destroyVM(vmID));
		
	}
	
	public static String createVM(int number) {
		JobResult rs = manager.createVM(wsUrl, username, password, bizZoneID, groupName, imageID, profileID, number);
		System.out.println(rs);
		return rs.getId();
	}
	
	public static VmJobResult queryVmJob(String jobID) {
		return manager.queryVMJob(wsUrl, username, password, jobID);
	}
	
	public static JobResult destroyVM(String vmID) {
		return manager.destroyVM(wsUrl, username, password, vmID);
	}
	
	public static VServer queryVMDetail(String vmID) {
		return manager.queryVMDetail(wsUrl, username, password, vmID);
	}
	
	public static boolean modifyVMGroup(String vmID, String groupName) {
		return manager.modifyVMGroup(wsUrl, username, password, vmID, groupName);
	}
	
	public static boolean modifyVMProfile(String vmID, String profileID) {
		return manager.modifyVMProfile(wsUrl, username, password, vmID, profileID);
	}
	
	private static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
