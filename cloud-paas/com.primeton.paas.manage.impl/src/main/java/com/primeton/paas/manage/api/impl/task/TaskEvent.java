/**
 * 
 */
package com.primeton.paas.manage.api.impl.task;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class TaskEvent {

	private String taskStatus;
	private String output;
	private Throwable exception;
	
	/**
	 * Default. <br>
	 */
	public TaskEvent() {
		super();
	}

	/**
	 * @param taskStatus
	 * @param output
	 */
	public TaskEvent(String taskStatus, String output) {
		super();
		this.taskStatus = taskStatus;
		this.output = output;
	}

	/**
	 * @return the taskStatus
	 */
	public String getTaskStatus() {
		return taskStatus;
	}

	/**
	 * @param taskStatus the taskStatus to set
	 */
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * @return the exception
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(Throwable exception) {
		this.exception = exception;
	}
	
	
}
