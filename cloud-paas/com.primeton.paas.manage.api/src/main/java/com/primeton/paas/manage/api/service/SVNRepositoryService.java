/**
 * 
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class SVNRepositoryService extends AbstractService {

	private static final long serialVersionUID = 6324977929700453253L;
	
	public static final String TYPE = "SVNRepository";
	
	private static final String REPO_NAME= "repoName";//appName
	private static final String REPO_USER = "user";
	private static final String REPO_PASSWORD = "password";

	/**
	 * 
	 */
	public SVNRepositoryService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_LOGIC);
	}
	
	public String getRepoName() {
		return getValue(REPO_NAME);
	}
	
	public void setRepoName(String repoName) {
		setValue(REPO_NAME, repoName);
	}
	
	public String getUser() {
		return getValue(REPO_USER);
	}
	
	public void setUser(String user) {
		setValue(REPO_USER, user);
	}
	
	public String getPassword() {
		return getValue(REPO_PASSWORD);
	}
	
	public void setPassword(String password) {
		setValue(REPO_PASSWORD, password);
	}
	
}
