/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultTaskDao;
import com.primeton.paas.manage.api.impl.task.TaskEvent;
import com.primeton.paas.manage.api.impl.task.TaskExecutor;
import com.primeton.paas.manage.api.impl.task.TaskListener;
import com.primeton.paas.manage.api.manager.Executable;
import com.primeton.paas.manage.api.manager.ITaskManager;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.Task;
import com.primeton.paas.manage.api.util.ExceptionUtil;
import com.primeton.paas.manage.api.util.RandomUtil;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultTaskManager implements ITaskManager {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultTaskManager.class);
	private Map<String, TaskTimer> taskTimers = new ConcurrentHashMap<String, DefaultTaskManager.TaskTimer>();
	
	private static DefaultTaskDao taskDao = DefaultTaskDao.getInstance();

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.task.ITaskManager#add(com.primeton.paas.manage.api.task.ITaskExecutor, long)
	 */
	public String add(Executable executable, long timeout, String owner) {
		if (executable == null || timeout <= 0) {
			throw new IllegalArgumentException();
		}
		String id = RandomUtil.generateId();
		Task task = new Task();
		task.setId(id);
		task.setType(executable.getType());
		task.setTimeout(timeout);
		task.setStartTime(new Date(System.currentTimeMillis()));
		task.setFinalTime(new Date(task.getStartTime().getTime() + timeout
				* 1000));
		task.setStatus(Task.STATUS_RUNNING);
		task.setOwner(owner);

		try {
			taskDao.addTask(task);
			TaskTimer timer = new TaskTimer(executable, task);
			taskTimers.put(task.getId(), timer);
			timer.start();

			return task.getId();
		} catch (DaoException e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.task.ITaskManager#abort(java.lang.String)
	 */
	public void abort(String id) {
		logger.debug("TaskTimers info '" + taskTimers.size() + "'.");

		if (taskTimers.containsKey(id)) {
			TaskTimer timer = taskTimers.remove(id);
			timer.abort();
		}
		Task task = this.get(id);
		if (null != task) {
			if (task.getStatus().equals(Task.STATUS_RUNNING)) {
				task.setAbortTime(new Date(System.currentTimeMillis()));
				task.setFinishTime(new Date(System.currentTimeMillis()));
				task.setStatus(Task.STATUS_ABORT);
				this.update(task);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.task.ITaskManager#update(com.primeton.paas.manage.api.task.Task)
	 */
	public void update(Task task) {
		if (task == null || StringUtil.isEmpty(task.getId())) {
			return;
		}
		try {
			taskDao.updateTask(task);
		} catch (DaoException e) {
			logger.error(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.task.ITaskManager#remove(java.lang.String)
	 */
	public void remove(String id) {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		if (taskTimers.containsKey(id)) {
			TaskTimer timer = taskTimers.remove(id);
			timer.abort();
		}
		try {
			taskDao.delTask(id);
		} catch (DaoException e) {
			logger.error(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.task.ITaskManager#get(java.lang.String)
	 */
	public Task get(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			return taskDao.getTask(id);
		} catch (DaoException e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.task.ITaskManager#getTasks(java.lang.String, java.lang.String, long, long, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public Task[] getTasks(String id, String type, String status,
			IPageCond pageCond) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("id", id); //$NON-NLS-1$
		criteria.put("type", type); //$NON-NLS-1$
		criteria.put("status", status); //$NON-NLS-1$
		List<Task> taskList = new ArrayList<Task>();
		try {
			if (pageCond == null) {
				taskList = taskDao.getTasks(criteria);
			} else {
				if (pageCond.getCount() <= 0) {
					pageCond.setCount(taskDao.getOrderCount(criteria));
				}
				taskList = taskDao.getTasks(criteria, pageCond);
			}

		} catch (DaoException e) {
			logger.error(e);
		}
		if (taskList != null && taskList.size() > 0) {
			return taskList.toArray(new Task[taskList.size()]);
		}
		return null;
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	private class TaskTimer extends Thread {
		
		private Executable executable;
		private Task task;
		private TaskExecutor executor;
		private TaskListener listener;
		private boolean flag = false;
		
		/**
		 * @param executable
		 * @param task
		 */
		public TaskTimer(Executable executable, Task task) {
			super();
			this.executable = executable;
			this.task = task;
			super.setDaemon(true);
		}
		
		private void init() {
			this.listener = new TaskListener() {
				
				public void handle(TaskEvent event) {
					if (event == null
							|| event.getTaskStatus() == null
							|| (Task.STATUS_EXCEPTION.equals(event.getTaskStatus()) && Task.STATUS_TIMEOUT.equals(task.getStatus()))
							|| (Task.STATUS_EXCEPTION.equals(event.getTaskStatus()) && Task.STATUS_ABORT.equals(task.getStatus()))) {
						return;
					}
					
					task.setStatus(event.getTaskStatus());
					task.setHandleResult(event.getOutput());
					
					if(null != event.getException()){
						task.setExceptionTime(new Date(System.currentTimeMillis()));
						String exceptionMsg = event.getException().getMessage();
						if(null != event.getException().getCause()){
							exceptionMsg +=  "<br/>" +ExceptionUtil.getCauseMessage(event.getException().getCause());
						}
						task.setException(exceptionMsg);
					}
					task.setFinishTime(new Date(System.currentTimeMillis()));
					update(task);
					close();
				}
			};
			
			this.executor = new TaskExecutor(executable, listener);
			this.executor.start();
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			long begin = task.getStartTime().getTime();
			long end = begin;
			init();
			
			while (true) {
				if (this.flag) {
					break;
				}
				end = System.currentTimeMillis();
				if (end > task.getFinalTime().getTime()) {
					task.setStatus(Task.STATUS_TIMEOUT);
					task.setFinishTime(new Date(System.currentTimeMillis()));
					update(task);
					this.executor.abort();
					taskTimers.remove(task.getId());
					this.flag = true;
					break;
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		private void close() {
			this.flag = true;
			taskTimers.remove(task.getId());
		}
		
		@SuppressWarnings("deprecation")
		public void abort() {
			if (this.isAlive()) {
				executor.abort();
				stop();
				taskTimers.remove(task.getId());
				if(task.getStatus().equals(Task.STATUS_RUNNING)){
					task.setAbortTime(new Date(System.currentTimeMillis()));
					task.setFinishTime(new Date(System.currentTimeMillis()));
					task.setStatus(Task.STATUS_ABORT);
					update(task);
				}
			}
		}
		
	}

}
