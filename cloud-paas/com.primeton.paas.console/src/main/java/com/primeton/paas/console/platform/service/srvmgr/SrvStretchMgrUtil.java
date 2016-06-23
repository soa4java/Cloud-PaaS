/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.manage.api.app.StretchStrategy;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.factory.TaskManagerFactory;
import com.primeton.paas.manage.api.impl.task.SrvManualStretch;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.ITaskManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.Task;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public class SrvStretchMgrUtil {

	private static ITaskManager taskManager = TaskManagerFactory.getManager();

	private static IServiceQuery serviceQuery = ServiceManagerFactory
			.getServiceQuery();

	/**
	 * 手动伸 <br>
	 * 
	 * @param clusterId
	 * @param type
	 * @param num
	 */
	public static String manulIncStretch(String clusterId, String type, int num) {
		IService[] insts = serviceQuery.getByCluster(clusterId);
		if (insts == null || insts.length < 1) {
			return "取消新增实例（伸）：集群内缺少实例！";
		}

		String owner = DataContextManager.current().getMUODataContext()
				.getUserObject().getUserId();
		long timeout = SystemVariable.getServiceStretchTimeout();

		String taskId = taskManager.add(new SrvManualStretch(clusterId,
				StretchStrategy.STRATEGY_INCREASE, num, type), timeout, owner);

		Task task = taskManager.get(taskId);
		task.setHandleResult("Manul increase service task. {clusterId:"
				+ clusterId + ",type:" + type + ",num:" + num + "}.");
		taskManager.update(task);

		return "已提交新增实例请求，长任务标识 [ " + taskId + " ].";
	}

	/**
	 * 手动缩 <br>
	 * 
	 * @param clusterId
	 * @param type
	 * @param num
	 */
	public static String manulDecStretch(String clusterId, String type, int num) {
		IService[] insts = serviceQuery.getByCluster(clusterId);
		if (insts == null || insts.length < 1) {
			return "取消移除实例（缩）：集群内无实例！";
		} else if (insts.length == 1) {
			return "取消移除实例（缩）：集群内只有一个实例，不允许缩！";
		}

		String owner = DataContextManager.current().getMUODataContext()
				.getUserObject().getUserId();

		long timeout = SystemVariable.getServiceStretchTimeout();
		String taskId = taskManager.add(new SrvManualStretch(clusterId,
				StretchStrategy.STRATEGY_DECREASE, num, type), timeout, owner);

		Task task = taskManager.get(taskId);
		task.setHandleResult("Manul descrease service task. {clusterId:"
				+ clusterId + ",type:" + type + ",num:" + num + "}.");
		taskManager.update(task);

		return "已提交移除实例请求，长任务标识 [ " + taskId + " ].";
	}

}
