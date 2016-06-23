/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.Task;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface ITaskManager {

	/**
	 * 
	 * @param executable
	 * @param timeout
	 * @param owner
	 * @return
	 */
	public String add(Executable executable, long timeout, String owner);
	
	/**
	 * 
	 * @param id
	 */
	public void abort(String id);
	
	/**
	 * 
	 * @param task
	 */
	public void update(Task task);
	
	/**
	 * 
	 * @param id
	 */
	public void remove(String id);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Task get(String id);
	
	/**
	 * 
	 * @param id
	 * @param type
	 * @param status
	 * @param pageCond
	 * @return
	 */
	public Task[] getTasks(String id, String type, String status,
			IPageCond pageCond);
	
}
