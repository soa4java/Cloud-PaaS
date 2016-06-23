/**
 * 
 */
package com.primeton.paas.manage.api.service;

import java.util.Date;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class WarService extends AbstractService {

	private static final long serialVersionUID = 1203838996005727136L;
	
	public static final String TYPE = "War";
	
	// "/repos/sample/war"
	private static final String REVISION = "revision";
	private static final String REPO_FILE = "repoFile"; 
	private static final String WAR_VERSION = "warVersion"; 
	private static final String SUBMIT_TIME = "submitTime";
	private static final String SUBMIT_BY = "submitBy";
	private static final String DEPLOYED = "isDeployVersion";
	
	private static final String DESC = "description";
	
	
	public WarService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_LOGIC);
	}
	
	public String getRevision(){
		return getValue(REVISION);
	}
	
	public void setRevision(long revision){
		setValue(REVISION, revision);
	}
	
	public String getRepoFile() {
		return getValue(REPO_FILE);
	}
	
	public void setRepoFile(String repoFile){
		setValue(REPO_FILE, repoFile);
	}
	
	public String getWarVersion() {
		return getValue(WAR_VERSION);
	}
	
	public void setWarVersion(String WarVersion) {
		setValue(WAR_VERSION, WarVersion);
	}
	
	public Date getSubmitTime() {
		return new Date(getValue(SUBMIT_TIME, System.currentTimeMillis()));
	}
	
	public void setSubmitTime(Date submitTime) {
		if (submitTime != null) {
			setValue(SUBMIT_TIME, submitTime.getTime());
		}
	}
	
	public String getSubmitBy() {
		return getValue(SUBMIT_BY);
	}
	
	public void setSubmitBy(String submitBy) {
		setValue(SUBMIT_BY, submitBy);
	}
	
	public boolean isDeployVersion() {
		return getValue(DEPLOYED, false);
	}
	
	public void setDeployVersion(boolean isDeployVersion) {
		setValue(DEPLOYED, isDeployVersion);
	}
	
	public String getDesc(){
		return getValue(DESC);
	}
	
	public void setDesc(String description){
		setValue(DESC, description);
	}
	
}
