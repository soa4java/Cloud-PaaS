/**
 * 
 */
package com.primeton.paas.cep.util;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.cesium.zkclient.api.ZkClient;
import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;

import com.primeton.paas.cep.engine.Constants;
import com.primeton.paas.cep.engine.Engine;
import com.primeton.paas.cep.engine.ServerContext;
import com.primeton.paas.cep.model.EPSInstance;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class PathUtil implements Constants {
	
	public static final String SEPARATOR = "/";
	public static final String ROOT_PATH = SEPARATOR + "Cloud" + SEPARATOR + "PaaS" + SEPARATOR + "CEP";
	public static final String ENGINE_PATH = ROOT_PATH + SEPARATOR + "Engine";
	public static final String EPS_PATH = ROOT_PATH + SEPARATOR + "EPS";
	
	private static ZkClient zkClient = ZkClientFactory.getZkClient();
	
	/**
	 * 
	 * @return
	 */
	public static String getEngineGroupPath() {
		return ENGINE_PATH + SEPARATOR + ServerContext.getContext().getGroupName();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getEPSGroupPath() {
		return EPS_PATH + SEPARATOR + ServerContext.getContext().getGroupName();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getEnginePath() {
		return getEnginePath(ServerContext.getContext().getEngineName());
	}
	
	/**
	 * 
	 * @param engineName
	 * @return
	 */
	public static String getEnginePath(String engineName) {
		return getEngineGroupPath() + SEPARATOR + engineName;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static String getEPSPath(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		return getEPSGroupPath() + SEPARATOR + id;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EPSInstance getEPS(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		String path = getEPSPath(id);
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
		List<EPSInstance> instances = new ArrayList<EPSInstance>();
		String path = getEPSGroupPath();
		if (zkClient.exists(path)) {
			List<String> children = zkClient.getChildren(path);
			if (children != null && !children.isEmpty()) {
				for (String id : children) {
					EPSInstance e = getEPS(id);
					if (e != null) {
						instances.add(e);
					}
				}
			}
		} else {
			zkClient.createPersistent(path, true);
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
	 * @param engineName
	 * @return
	 */
	public static Engine getEngine(String engineName) {
		if (StringUtil.isEmpty(engineName)) {
			return null;
		}
		String groupPath = PathUtil.getEngineGroupPath();
		boolean exists = zkClient.exists(groupPath);
		if (!exists) {
			zkClient.createPersistent(groupPath, true);
		}
		String path = getEnginePath(engineName);
		if (zkClient.exists(path)) {
			return zkClient.readData(path);
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<Engine> getEngines() {
		List<Engine> engines = new ArrayList<Engine>();
		String groupPath = PathUtil.getEngineGroupPath();
		boolean exists = zkClient.exists(groupPath);
		if (!exists) {
			zkClient.createPersistent(groupPath, true);
		}
		List<String> nodes = zkClient.getChildren(groupPath);
		if (nodes != null) {
			for (String engineName : nodes) {
				String path = getEnginePath(engineName);
				Engine engine = zkClient.readData(path);
				if (engine != null) {
					engines.add(engine);
				}
			}
		}
		return engines;
	}
	
	/**
	 * 
	 * @param engine
	 */
	public static void setEngine(Engine engine) {
		if (engine == null || StringUtil.isEmpty(engine.getName())) {
			return;
		}
		String enginePath = getEnginePath(engine.getName());;
		if (!zkClient.exists(getEngineGroupPath())) {
			zkClient.createPersistent(getEngineGroupPath(), true);
		}
		boolean exists = zkClient.exists(enginePath);
		if (!exists) {
			zkClient.createEphemeral(enginePath);
		}
		zkClient.writeData(enginePath, engine);
	}
	
	/**
	 * 
	 * @param engineName
	 */
	public static void deleteEngine(String engineName) {
		String enginepath = getEnginePath(engineName);
		if (zkClient.exists(enginepath)) {
			zkClient.delete(enginepath);
		}
	}

}
