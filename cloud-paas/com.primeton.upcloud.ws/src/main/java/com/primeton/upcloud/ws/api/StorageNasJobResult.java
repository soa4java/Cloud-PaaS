/**
 * 
 */
package com.primeton.upcloud.ws.api;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class StorageNasJobResult {

	public static final String STATUS_SUCCESS = "SUCCESS";

	public static final String STATUS_PENDING = "PENDING";

	public static final String STATUS_FAIL = "FAIL";

	private String jobID;

	private String jobStatus;

	private StorageNasVolume nasVolume;

	private String message;

	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public StorageNasVolume getNasVolume() {
		return nasVolume;
	}

	public void setNasVolume(StorageNasVolume nasVolume) {
		this.nasVolume = nasVolume;
	}

	public String toString() {
		return "JobResult [id=" + jobID + ", status=" + jobStatus
				+ ", message=" + message + "]";
	}

}
