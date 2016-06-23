/**
 * 
 */
package com.primeton.paas.manage.api.impl.task;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.exception.TaskException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.manager.Executable;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SrvAutoStretchTask implements Executable {
	
	public static final String TYPE = "3";

	//	Log
	private static ILogger logger = ManageLoggerFactory.getLogger(SrvAutoStretchTask.class);
	
	public static String SRV_MANUAL_INC_STRETCH  = "INCREASE";
	public static String SRV_MANUAL_DEC_STRETCH  = "DECREASE";
	
	private String clusterId; //
	private int num;	 //instance number
	private String type; //service type
	private long timeout;
	private String stretchType; //inc & dec
	
	/**
	 * @param clusterId
	 * @param num
	 * @param type
	 * @param timeout
	 */
	public SrvAutoStretchTask(String clusterId,String stretchType,int num,String type, long timeout){
		this.clusterId = clusterId;
		this.stretchType = stretchType;
		this.num = num;
		this.type = type;
		this.timeout = timeout;
	}
	
	public void init() {
	}
	
	public void clear() {
	}
	
	public String getType() {
		return TYPE;
	}

	public String execute() throws TaskException {
		String retMsg = null;
		logger.info("Service auto stretch start.[" + stretchType +"] {clusterId="+clusterId +",type="+type + ",num="+num + ",timeout="+timeout +"}.");
		if (StringUtil.isEmpty(type) || StringUtil.isEmpty(clusterId))   {
			retMsg = "Service auto stretch cancelled, parameter is null.[" + stretchType +"] {clusterId="+clusterId +",type="+type + ",num="+num + ",timeout="+timeout +"}.";
			logger.error(retMsg);
			throw new TaskException(retMsg);
		}
		
		IClusterManager clusterManager = ClusterManagerFactory.getManager(type);
		try {
			if (SRV_MANUAL_INC_STRETCH.equals(stretchType)) {
				clusterManager.increase(clusterId, num);
				
			} else if (SRV_MANUAL_DEC_STRETCH.equals(stretchType)) {
				clusterManager.decrease(clusterId, num);
				
			}
		} catch (ServiceException e) {
			retMsg = "Service auto stretch failure.[" + stretchType +"] {clusterId="+clusterId +",type="+type + ",num="+num + ",timeout="+timeout +"}.error:" + e.getMessage();
			logger.error(retMsg,e);
			throw new TaskException(retMsg,e);
		}
		
		retMsg = "Successful auto stretch service.[" + stretchType +"] {clusterId="+clusterId +",type="+type + ",num="+num + ",timeout="+timeout +"}.";
		logger.info(retMsg);
		return retMsg;
	}
	
}
