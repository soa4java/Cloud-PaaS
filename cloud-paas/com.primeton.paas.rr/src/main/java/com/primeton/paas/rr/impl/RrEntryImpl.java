/**
 * 
 */
package com.primeton.paas.rr.impl;

import java.util.Date;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;

import com.primeton.paas.rr.api.IRrEntry;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RrEntryImpl implements IRrEntry {

	private SVNDirEntry svnDirEntry;

	public RrEntryImpl(SVNDirEntry svnDirEntry) {
		this.svnDirEntry = svnDirEntry;
	}

	public long getRevision() {
		return svnDirEntry.getRevision();
	}

	public Date getCreatedDate() {
		return svnDirEntry.getDate();
	}

	public String getName() {
		return svnDirEntry.getName();
	}

	public boolean isFile() {
		return svnDirEntry.getKind() == SVNNodeKind.FILE;
	}

	public String getUrl() {
		return svnDirEntry.getURL().toString();
	}

	public String getRootUrl() {
		return svnDirEntry.getRepositoryRoot().toString();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		sb.append("(");
		sb.append("revision:" + getRevision());
		sb.append("; date:" + getCreatedDate());
		sb.append("; url:" + getUrl());
		sb.append("; root:" + getRootUrl());
		sb.append(")");
		return sb.toString();
	}

}
