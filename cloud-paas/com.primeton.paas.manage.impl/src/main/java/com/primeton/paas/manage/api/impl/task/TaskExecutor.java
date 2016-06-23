/**
 * 
 */
package com.primeton.paas.manage.api.impl.task;

import com.primeton.paas.manage.api.exception.TaskException;
import com.primeton.paas.manage.api.manager.Executable;
import com.primeton.paas.manage.api.model.Task;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class TaskExecutor extends Thread {
	
	private Executable executable;
	private TaskListener listener;
	
	/**
	 * @param executable
	 * @param listener
	 */
	public TaskExecutor(Executable executable, TaskListener listener) {
		super();
		this.executable = executable;
		this.listener = listener;
		super.setDaemon(true);
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		if (executable == null || listener == null) {
			return;
		}
		// init
		executable.init();
		
		// execute
		try {
			String result = executable.execute();
			listener.handle(new TaskEvent(Task.STATUS_FINISH, result));
		} catch (TaskException e) {
			TaskEvent event = new TaskEvent();
			event.setTaskStatus(Task.STATUS_EXCEPTION);
			event.setException(e);
			listener.handle(event);
		} finally {
			executable.clear();
		}
	}
	
	/*
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void abort() {
		this.stop();
		if (executable != null) {
			executable.clear();
		}
	}
	
}
