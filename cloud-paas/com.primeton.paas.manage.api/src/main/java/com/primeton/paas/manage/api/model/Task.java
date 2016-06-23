/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.util.Date;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class Task {
	
	public static final String STATUS_RUNNING = "1";
	
	public static final String STATUS_FINISH = "2";
	
	public static final String STATUS_EXCEPTION = "3";
	
	public static final String STATUS_ABORT = "4";
	
	public static final String STATUS_TIMEOUT = "5";
	
	private String id;				
	private String type;			
	private String status;		
	private long timeout;			
	private Date startTime;			
	private Date finishTime;		
	private Date finalTime;			
	private Date exceptionTime;		
	private Date abortTime;			
	private String handleResult;	
	private String exception;		
	private String owner;       
	
	/**
	 * Default. <br>
	 */
	public Task() {
		super();
	}

	public Date getAbortTime() {
		return abortTime;
	}

	public void setAbortTime(Date abortTime) {
		this.abortTime = abortTime;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public Date getExceptionTime() {
		return exceptionTime;
	}

	public void setExceptionTime(Date exceptionTime) {
		this.exceptionTime = exceptionTime;
	}

	public Date getFinalTime() {
		return finalTime;
	}

	public void setFinalTime(Date finalTime) {
		this.finalTime = finalTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String toString() {
		return "Task [id=" + id + ", type=" + type + ", status=" + status
				+ ", timeout=" + timeout + ", startTime=" + startTime
				+ ", finishTime=" + finishTime + ", finalTime=" + finalTime
				+ ", exceptionTime=" + exceptionTime + ", abortTime="
				+ abortTime + ", handleResult=" + handleResult + ", exception="
				+ exception + ", owner=" + owner + "]";
	}

}
