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
public class SrvManualStretch implements Executable {

	public static final String TYPE = "4";

	// Log
	private static ILogger logger = ManageLoggerFactory
			.getLogger(SrvManualStretch.class);

	public static String SRV_MANUAL_INC_STRETCH = "INCREASE";
	public static String SRV_MANUAL_DEC_STRETCH = "DECREASE";

	private String clusterId; //
	private int num; // instance number
	private String type; // service type
	private String stretchType; // inc & dec

	/**
	 * @param clusterId
	 * @param num
	 * @param type
	 */
	public SrvManualStretch(String clusterId, String stretchType, int num,
			String type) {
		this.clusterId = clusterId;
		this.stretchType = stretchType;
		this.num = num;
		this.type = type;
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
		if (StringUtil.isEmpty(type) || StringUtil.isEmpty(clusterId)) {
			retMsg = "Service manual stretch cancelled, parameter is null.["
					+ stretchType + "] {clusterId=" + clusterId + ",type="
					+ type + ",num=" + num + "}.";

			if (logger.isInfoEnabled()) {
				logger.info(retMsg);
			}
			return retMsg;
		}

		IClusterManager clusterManager = ClusterManagerFactory.getManager(type);
		try {
			if (SRV_MANUAL_INC_STRETCH.equals(stretchType)) {
				clusterManager.increase(clusterId, num);

			} else if (SRV_MANUAL_DEC_STRETCH.equals(stretchType)) {
				clusterManager.decrease(clusterId, num);

			}
		} catch (ServiceException e) {
			retMsg = "Service manual stretch failure.[" + stretchType
					+ "] {clusterId=" + clusterId + ",type=" + type + ",num="
					+ num + "}.error:" + e.getMessage();
			logger.info(retMsg, e);
			throw new TaskException(retMsg, e);
		}

		retMsg = "Successful manual stretch service.[" + stretchType
				+ "] {clusterId=" + clusterId + ",type=" + type + ",num=" + num
				+ "}.";
		return retMsg;
	}
	
}