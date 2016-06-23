/**
 * 
 */
package com.primeton.paas.cep.util;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.zkclient.api.ZkClient;
import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;

import com.primeton.paas.cep.model.EPSInstance;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class PathUtil {
	
	public static final String SEPARATOR = "/";
	public static final String ROOT_PATH = SEPARATOR + "Cloud" + SEPARATOR + "PaaS" + SEPARATOR + "CEP"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	public static final String EPS_PATH = ROOT_PATH + SEPARATOR + "EPS"; //$NON-NLS-1$
	public static final String DEFAULT_GROUP = "default"; //$NON-NLS-1$
	
	private static ZkClient zkClient = ZkClientFactory.getZkClient();
	
	/**
	 * 
	 * @return
	 */
	public static String getRootPath() {
		return ROOT_PATH;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getGroupPath() {
		return EPS_PATH + SEPARATOR + DEFAULT_GROUP;
	}
	
	/**
	 * 
	 * @param epsId
	 * @return
	 */
	public static String getEPSPath(String epsId) {
		if (StringUtil.isEmpty(epsId)) {
			return null;
		}
		return getGroupPath() + SEPARATOR + epsId;
	}
	
	/**
	 * 
	 * @param epsId
	 * @return
	 */
	public static EPSInstance getEPS(String epsId) {
		if (StringUtil.isEmpty(epsId)) {
			return null;
		}
		String path = getEPSPath(epsId);
		boolean exists = zkClient.exists(path);
		if (exists) {
			return zkClient.readData(path);
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<EPSInstance> getEPSs() {
		String groupPath = getGroupPath();
		if (!zkClient.exists(groupPath)) {
			zkClient.createPersistent(groupPath, true);
		}
		List<EPSInstance> instances = new ArrayList<EPSInstance>();
		List<String> children = zkClient.getChildren(groupPath);
		if (children != null && !children.isEmpty()) {
			for (String id : children) {
				EPSInstance e = getEPS(id);
				if (e != null) {
					instances.add(e);
				}
			}
		}
		return instances;
	}
	
	/**
	 * 
	 * @param instance
	 */
	public static void setEPS(EPSInstance instance){
		if (instance == null || StringUtil.isEmpty(instance.getId())) {
			return;
		}
		String path = getEPSPath(instance.getId());
		boolean exists = zkClient.exists(path);
		if (!exists) {
			zkClient.createPersistent(path, true);	// mkdir -p
		}
		zkClient.writeData(path, instance);
	}
	
	/**
	 * 
	 * @param epsId
	 */
	public static void deleteEPS(String epsId) {
		if (StringUtil.isEmpty(epsId)) {
			return;
		}
		String path = getEPSPath(epsId);
		boolean exists = zkClient.exists(path);
		if (exists) {
			zkClient.delete(path);
		}
	}
	
}
