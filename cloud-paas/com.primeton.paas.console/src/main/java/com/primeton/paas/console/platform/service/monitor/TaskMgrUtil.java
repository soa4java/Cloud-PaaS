/**
 * 
 */
package com.primeton.paas.console.platform.service.monitor;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.exception.TaskException;
import com.primeton.paas.manage.api.factory.TaskManagerFactory;
import com.primeton.paas.manage.api.manager.Executable;
import com.primeton.paas.manage.api.manager.ITaskManager;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.Task;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class TaskMgrUtil {
	
	private static ITaskManager taskManager = TaskManagerFactory.getManager();

	private static ILogger logger = LoggerFactory.getLogger(TaskMgrUtil.class);

	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 */
	public static Task[] queryTasks(Task criteria, IPageCond page) {
		return taskManager.getTasks(criteria.getId(), criteria.getType(),
				criteria.getStatus(), page);
	}

	/**
	 * 
	 * @param taskId
	 * @return
	 */
	public static Task queryTask(String taskId) {
		try {
			return taskManager.get(taskId);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 
	 * @param taskId
	 * @return
	 */
	public static boolean abortTask(String taskId) {
		try {
			taskManager.abort(taskId);
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param criteria
	 * @param page
	 * @return
	 */
	public static Task[] addTask(Task criteria, IPageCond page) {
		Executable executable = new Executable(){
			public void clear() {
			}

			public String execute() throws TaskException {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}

			public String getType() {
				return "2";
			}
			
			public void init() {
			}

			};
		IUserObject user = DataContextManager.current().getMUODataContext().getUserObject();
		String currentUser = user.getUserId();
		taskManager.add(executable, 10000 ,currentUser);
		
		return taskManager.getTasks(criteria.getId(), criteria.getType(),criteria.getStatus(), page);
	}
	
	/**
	 * 删除指定id（多个id用','号隔开）的任务<br>
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean delTasks(String ids) {
		try {
			if (null != ids && !"".equals(ids)) {
				for (String id : ids.split(",")) {
					taskManager.remove(id);
				}
				return true;
			}
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return false;
	}
	
}
