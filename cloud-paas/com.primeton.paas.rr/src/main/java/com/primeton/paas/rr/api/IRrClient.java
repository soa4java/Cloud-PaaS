/**
 * 
 */
package com.primeton.paas.rr.api;

import com.primeton.paas.rr.api.exception.RrRuntimeException;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IRrClient {

	/**
	 * 
	 * @param localPath
	 * @param repoPath
	 * @param comment
	 * @return
	 * @throws RrRuntimeException
	 */
	long checkin(String localPath, String repoPath, String comment) throws RrRuntimeException;

	/**
	 * 
	 * @param repoPath
	 * @param localPath
	 * @param revision
	 * @return
	 * @throws RrRuntimeException
	 */
	long checkout(String repoPath, String localPath, long revision) throws RrRuntimeException;

	/**
	 * 
	 * @param localPathes
	 * @param comment
	 * @return
	 * @throws RrRuntimeException
	 */
	String[] add(String[] localPathes, String comment) throws RrRuntimeException;

	/**
	 * 
	 * @param localPathes
	 * @param comment
	 * @throws RrRuntimeException
	 */
	void delete(String[] localPathes, String comment) throws RrRuntimeException;

	/**
	 * 
	 * @param localPathes
	 * @param comment
	 * @return
	 * @throws RrRuntimeException
	 */
	long commit(String[] localPathes, String comment) throws RrRuntimeException;

	/**
	 * 
	 * @param localPath
	 * @param revision
	 * @return
	 * @throws RrRuntimeException
	 */
	long update(String localPath, long revision) throws RrRuntimeException;

	/**
	 * 
	 * @param repoPath
	 * @param revision
	 * @return
	 * @throws RrRuntimeException
	 */
	IRrEntry[] listEntries(String repoPath, long revision) throws RrRuntimeException;

	/**
	 * 
	 * @param localPath
	 * @throws RrRuntimeException
	 */
	void cleanup(String localPath) throws RrRuntimeException;

	/**
	 * 
	 * @param srcRepoPath
	 * @param dstRepoPath
	 * @param comment
	 * @throws RrRuntimeException
	 */
	void copy(String srcRepoPath, String dstRepoPath, String comment) throws RrRuntimeException;

	/**
	 * 
	 * @param localPath
	 * @param repoPath
	 * @param revision
	 * @throws RrRuntimeException
	 */
	void switchpath(String localPath, String repoPath, long revision) throws RrRuntimeException;

	/**
	 * 
	 * @return
	 */
	String getRepoName();

	/**
	 * 
	 * @return
	 */
	String getRRServeUrl();
	
}
