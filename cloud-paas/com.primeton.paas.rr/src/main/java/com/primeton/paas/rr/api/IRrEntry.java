/**
 * 
 */
package com.primeton.paas.rr.api;

import java.util.Date;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IRrEntry {
	
	/**
	 * 
	 * @return
	 */
	long getRevision();

	/**
	 * 
	 * @return
	 */
	Date getCreatedDate();

	/**
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 
	 * @return
	 */
	String getUrl();
	
	
	/**
	 * 
	 * @return
	 */
	String getRootUrl();

	/**
	 * 
	 * @return
	 */
	boolean isFile();
	
}
