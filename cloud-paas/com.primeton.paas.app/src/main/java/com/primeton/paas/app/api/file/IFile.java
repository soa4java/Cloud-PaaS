/**
 * 
 */
package com.primeton.paas.app.api.file;

import java.io.IOException;
import java.io.Serializable;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IFile extends Serializable, Comparable<IFile> {

	/**
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 
	 * @return
	 */
	String getPath();

	/**
	 * 
	 * @return
	 */
	IFile getParent();

	/**
	 * 
	 * @return
	 */
	long length();

	/**
	 * 
	 * @return
	 */
	long lastModified();

	/**
	 * 
	 * @return
	 */
	boolean exists();

	/**
	 * 
	 * @return
	 */
	boolean isDirectory();

	/**
	 * 
	 * @return
	 */
	boolean isFile();

	/**
	 * 
	 * @return
	 */
	IFile[] listFiles();

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	boolean createNewFile() throws IOException;

	/**
	 * 
	 * @return
	 */
	boolean mkdirs();

	/**
	 * 
	 * @param dest
	 * @return
	 */
	boolean renameTo(IFile dest);

	/**
	 * 
	 * @param time
	 * @return
	 */
	boolean setLastModified(long time);

	/**
	 * 
	 * @return
	 */
	boolean delete();

}