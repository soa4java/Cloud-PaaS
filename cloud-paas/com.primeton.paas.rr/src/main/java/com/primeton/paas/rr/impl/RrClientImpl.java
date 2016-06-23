/**
 * 
 */
package com.primeton.paas.rr.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.utility.api.IOUtil;
import org.gocom.cloud.common.utility.api.SystemProperties;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.primeton.paas.rr.api.IRrClient;
import com.primeton.paas.rr.api.IRrEntry;
import com.primeton.paas.rr.api.exception.RrRuntimeException;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RrClientImpl implements IRrClient {
	
	private static final ILogger logger = LoggerFactory.getLogger(RrClientImpl.class);

	private SVNClientManager svnClientManager;
	private ISVNAuthenticationManager authManager;
	private SVNRepository svnRepository;
	private String rrServeUrl;
	private String repoName;

	/**
	 * 
	 * @param rrServeUrl
	 * @param repoName
	 * @param user
	 * @param pwd
	 */
	public RrClientImpl(String rrServeUrl, String repoName, String user, String pwd) {
		if (rrServeUrl == null) {
			throw new NullPointerException("The rr-server-url is null.");
		}
		if (repoName == null) {
			throw new NullPointerException("The repo-name is null.");
		}
		if (user == null) {
			throw new NullPointerException("The user is null.");
		}
		if (pwd == null) {
			throw new NullPointerException("The password is null.");
		}

		DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
		options.addIgnorePattern(".svn"); //$NON-NLS-1$
		options.addIgnorePattern("bin"); //$NON-NLS-1$

		authManager = SVNWCUtil.createDefaultAuthenticationManager(user, pwd);
		svnClientManager = SVNClientManager.newInstance(options, authManager);
		this.rrServeUrl = rrServeUrl;
		this.repoName = repoName;

		SVNURL repoRootUrl = null;
		try {
			repoRootUrl = getRepoUrl("/"); //$NON-NLS-1$
			svnRepository = SVNRepositoryFactory.create(repoRootUrl);
			svnRepository.setAuthenticationManager(authManager);
		} catch (SVNException e) {
			throw new RrRuntimeException("Create svn-repository for url '" + repoRootUrl + "' error: " + e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.psrv.rr.api.IRRClient#getRepoName()
	 */
	public String getRepoName() {
		return repoName;
	}

	/* (non-Javadoc)
	 * @see com.primeton.psrv.rr.api.IRRClient#getRRServeUrl()
	 */
	public String getRRServeUrl() {
		return rrServeUrl;
	}

	/* (non-Javadoc)
	 * @see com.primeton.psrv.rr.api.IRRClient#checkin(java.lang.String, java.lang.String, java.lang.String)
	 */
	public long checkin(String localPath, String repoPath, String comment) throws RrRuntimeException {
		try {
			if (comment == null) {
				comment = "checkin"; //$NON-NLS-1$
			}
			SVNURL checkinUrl = getRepoUrl(repoPath);
			SVNCommitClient commitClient = svnClientManager.getCommitClient();
			if (logger.isDebugEnabled()) {
				logger.debug("Will import '" + getLocalAbsoluteFile(localPath) + "' to repo '" + checkinUrl + "'.");
			}
			return commitClient.doImport(getLocalAbsoluteFile(localPath), checkinUrl, comment, null, true, true, SVNDepth.INFINITY).getNewRevision();
		} catch (SVNException e) {
			throw new RrRuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.psrv.rr.api.IRRClient#checkout(java.lang.String, java.lang.String, long)
	 */
	public long checkout(String repoPath, String localPath, long revision) throws RrRuntimeException {
		try {
			SVNUpdateClient updateClient = svnClientManager.getUpdateClient();
			updateClient.setIgnoreExternals(false);
			SVNURL checkoutUrl = getRepoUrl(repoPath);
			SVNRevision svnRevision = getSvnRevision(revision);
			if (logger.isDebugEnabled()) {
				logger.debug("Will checkout repo '" + checkoutUrl + "' to  '" + getLocalAbsoluteFile(localPath) + "'.");
			}
			return updateClient.doCheckout(checkoutUrl, getLocalAbsoluteFile(localPath), svnRevision, svnRevision, SVNDepth.INFINITY, false);
		} catch (SVNException e) {
			throw new RrRuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.psrv.rr.api.IRRClient#add(java.lang.String[], java.lang.String)
	 */
	public String[] add(String[] localPathes, String comment) throws RrRuntimeException {
		try {
			if (comment == null) {
				comment = "add"; //$NON-NLS-1$
			}
			SVNStatusClient statusClient = svnClientManager.getStatusClient();
			SVNWCClient wcClient = svnClientManager.getWCClient();
			List<File> filesToAdd = new LinkedList<File>();
			for (String localPath : localPathes) {
				File localFile = getLocalAbsoluteFile(localPath);
				boolean loop = true;
				do {
					try {
						SVNStatus status = statusClient.doStatus(localFile, false);
						if (status == null) {
							throw new RrRuntimeException("Please check the path '" + localFile.getAbsolutePath() + "' is exist?");
						}
						SVNStatusType contentsStatus = status.getContentsStatus();
						SVNStatusType nodeStatus = status.getNodeStatus();
						if (nodeStatus == SVNStatusType.STATUS_DELETED) {
							filesToAdd.remove(localFile);
							filesToAdd.add(localFile);
							localFile = localFile.getParentFile();

						} else if (contentsStatus == SVNStatusType.STATUS_UNVERSIONED || contentsStatus == SVNStatusType.STATUS_NONE) {
							filesToAdd.remove(localFile);
							filesToAdd.add(localFile);
							break;

						} else {
							loop = false;
						}
					} catch (SVNException e) {
						if (SVNErrorCode.WC_NOT_WORKING_COPY == e.getErrorMessage().getErrorCode()) {
							filesToAdd.remove(localFile);
							filesToAdd.add(localFile);

							localFile = localFile.getParentFile();
						} else {
							throw e;
						}
					}
				} while (loop);
			}

			Collections.reverse(filesToAdd);
			for (int i = 0; i < filesToAdd.size(); i++) {
				File fileToAdd = filesToAdd.get(i);
				if (logger.isDebugEnabled()) {
					logger.debug("Will add file '" + fileToAdd.getAbsolutePath() + "'.");
				}
				wcClient.doAdd(fileToAdd, false, false, false, i == filesToAdd.size() - 1 ? SVNDepth.INFINITY : SVNDepth.EMPTY, false, false);
			}

			localPathes = new String[filesToAdd.size()];
			for (int i = 0; i < filesToAdd.size(); i++) {
				File fileToAdd = filesToAdd.get(i);
				localPathes[i] = getLocalRelativePath(fileToAdd);
			}
			return localPathes;
		} catch (SVNException e) {
			throw new RrRuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.psrv.rr.api.IRRClient#delete(java.lang.String[], java.lang.String)
	 */
	public void delete(String[] localPathes, String comment) throws RrRuntimeException {
		try {
			if (comment == null) {
				comment = "delete";
			}
			SVNWCClient wcClient = svnClientManager.getWCClient();
			for (String localPath : localPathes) {
				File file = getLocalAbsoluteFile(localPath);
				if (logger.isDebugEnabled()) {
					logger.debug("Will delete file '" + file.getAbsolutePath() + "'.");
				}
				wcClient.doDelete(file, true, true, false);
			}
		} catch (SVNException e) {
			throw new RrRuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.psrv.rr.api.IRRClient#commit(java.lang.String[], java.lang.String)
	 */
	public long commit(String[] localPathes, String comment) throws RrRuntimeException {
		try {
			if (comment == null) {
				comment = "commit"; //$NON-NLS-1$
			}
			SVNCommitClient commitClient = svnClientManager.getCommitClient();
			File[] files = new File[localPathes.length];
			for (int i = 0; i < localPathes.length; i++) {
				String localPath = localPathes[i];
				files[i] = getLocalAbsoluteFile(localPath);
				if (logger.isDebugEnabled()) {
					logger.debug("Will commit file '" + files[i] + "'.");
				}
			}
			return commitClient.doCommit(files, false, comment, null, null, false, false, SVNDepth.INFINITY).getNewRevision();
		} catch (SVNException e) {
			throw new RrRuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.psrv.rr.api.IRRClient#update(java.lang.String, long)
	 */
	public long update(String localPath, long revision) throws RrRuntimeException {
		try {
			SVNUpdateClient updateClient = svnClientManager.getUpdateClient();
			updateClient.setIgnoreExternals(false);
			SVNRevision svnRevision = getSvnRevision(revision);
			if (logger.isDebugEnabled()) {
				logger.debug("Will update file '" + getLocalAbsoluteFile(localPath) + "'.");
			}
			return updateClient.doUpdate(getLocalAbsoluteFile(localPath), svnRevision, SVNDepth.INFINITY, false, false);
		} catch (SVNException e) {
			throw new RrRuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.psrv.rr.api.IRRClient#view(java.lang.String, long)
	 */
	@SuppressWarnings("rawtypes")
	public IRrEntry[] listEntries(String repoPath, long revision) throws RrRuntimeException {
		List<IRrEntry> ret = new ArrayList<IRrEntry>();
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Will list entry form repo '" + getRepoUrl(repoPath) + "'.");
			}
			Collection svnEntries = svnRepository.getDir(repoPath, revision, SVNProperties.wrap(new HashMap()), (Collection) null);
			Iterator iterator = svnEntries.iterator();
			while (iterator.hasNext()) {
				SVNDirEntry svnEntry = (SVNDirEntry) iterator.next();
				RrEntryImpl rrEntry = new RrEntryImpl(svnEntry);
				ret.add(rrEntry);
			}
		} catch (SVNException e) {
			throw new RrRuntimeException(e);
		}
		return ret.toArray(new IRrEntry[ret.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.psrv.rr.api.IRrClient#cleanup(java.lang.String)
	 */
	public void cleanup(String localPath) throws RrRuntimeException {
		SVNWCClient wcClient = svnClientManager.getWCClient();
		try {
			wcClient.doCleanup(getLocalAbsoluteFile(localPath), false);
		} catch (SVNException e) {
			throw new RrRuntimeException(e);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.primeton.psrv.rr.api.IRRClient#copy(java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	public void copy(String srcRepoPath, String dstRepoPath, String comment) throws RrRuntimeException {
		try {
			if (comment == null) {
				comment = "copy"; //$NON-NLS-1$
			}
			SVNCopyClient copyClient = svnClientManager.getCopyClient();
			SVNURL srcUrl = getRepoUrl(srcRepoPath);
			SVNURL dstUrl = getRepoUrl(dstRepoPath);

			if (logger.isInfoEnabled()) {
				logger.info("Will copy from repo-path: '" + srcUrl + "' to repo-path: '" + dstUrl + "'.");
			}

			copyClient
					.doCopy(new SVNCopySource[] { new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, srcUrl) }, dstUrl, false, true, true, comment, SVNProperties
							.wrap(new HashMap()));
		} catch (SVNException e) {
			throw new RrRuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.psrv.rr.api.IRRClient#switchpath(java.lang.String, java.lang.String, long)
	 */
	public void switchpath(String localPath, String repoPath, long revision) throws RrRuntimeException {
		try {
			SVNUpdateClient updateClient = svnClientManager.getUpdateClient();
			updateClient.setIgnoreExternals(false);
			File localFile = getLocalAbsoluteFile(localPath);
			SVNURL repoUrl = getRepoUrl(repoPath);
			SVNRevision svnRevision = getSvnRevision(revision);

			if (logger.isInfoEnabled()) {
				logger.info("The local-path: '" + localFile.getAbsolutePath() + "' will switch to repo-path: '" + repoUrl + "'.");
			}

			updateClient.doSwitch(localFile, repoUrl, SVNRevision.UNDEFINED, svnRevision, SVNDepth.INFINITY, false, false);
		} catch (SVNException e) {
			throw new RrRuntimeException(e);
		}
	}

	public void destory() {
		if (svnClientManager != null) {
			svnClientManager.dispose();
			svnClientManager = null;
		}
		if (svnRepository != null) {
			svnRepository.closeSession();
			svnRepository = null;
		}
	}

	public final static String ENV_FILE_ROOT_DIR = "paas.fileRootDir";
	
	private String rootPath = getRootPath();

	private static String getRootPath() {
		String rootPath = SystemProperties.getProperty(ENV_FILE_ROOT_DIR);
		if (rootPath != null) {
			rootPath = IOUtil.normalizeInUnixStyle(rootPath);
		}
		return rootPath;
	}

	private File getLocalAbsoluteFile(String path) {
		if (path == null) {
			throw new RrRuntimeException("The path is null!");
		}
		path = IOUtil.normalizeInUnixStyle(path);
		if (path.equals("/")) {
			throw new RrRuntimeException("The path is not equals \"/\".");
		} else {
			return new File(this.rootPath, path);
		}
	}

	private String getLocalRelativePath(File file) {
		if (file == null) {
			throw new RrRuntimeException("The path is null!");
		}
		String path = IOUtil.normalizeInUnixStyle(file.getAbsolutePath());
		if (rootPath == null) {
			return path;
		}

		int idx = path.indexOf(rootPath);
		if (idx == 0) {
			return path.substring(rootPath.length());
		}
		return path;
	}

	private SVNRevision getSvnRevision(long revision) {
		SVNRevision svnRevision = revision < 0 ? SVNRevision.HEAD : SVNRevision.create(revision);
		return svnRevision;
	}

	private SVNURL getRepoUrl(String repoPath) throws SVNException {
		SVNURL svnUrl = null;
		if (repoPath == null || repoPath.length() == 0 || repoPath.equals("/")) { //$NON-NLS-1$
			svnUrl = SVNURL.parseURIEncoded(rrServeUrl).appendPath(repoName, false);
		} else {
			svnUrl = SVNURL.parseURIEncoded(rrServeUrl).appendPath(repoName, false).appendPath(repoPath, false);
		}
		return svnUrl;
	}

}
