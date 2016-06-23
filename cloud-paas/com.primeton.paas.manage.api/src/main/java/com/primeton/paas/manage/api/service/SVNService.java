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
public class SVNService extends AbstractService {

	private static final long serialVersionUID = 968297622695984901L;
	
	public static final String TYPE = "SVN";
	
	private static final String REPO_ROOT = "repoRoot"; // repos/

	public SVNService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	public String getRepoRoot() {
		return getValue(REPO_ROOT);
	}
	
	public void setRepoRoot(String repoRoot) {
		setValue(REPO_ROOT, repoRoot);
	}
	
}
