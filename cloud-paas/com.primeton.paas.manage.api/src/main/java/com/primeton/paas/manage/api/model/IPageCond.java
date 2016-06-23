/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.io.Serializable;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IPageCond extends Serializable {

	/**
	 * 
	 * @return
	 */
	int getBegin();
	
	/**
	 * 
	 * @param begin
	 */
	void setBegin(int begin);
	
	/**
	 * 
	 * @return
	 */
	int getLength();
	
	/**
	 * 
	 * @param length
	 */
	void setLength(int length);
	
	/**
	 * 
	 * @return
	 */
	int getCount();
	
	/**
	 * 
	 * @param count
	 */
	void setCount(int count);
	
	/**
	 * 
	 * @return
	 */
	int getCurrentPage();
	
	/**
	 * 
	 * @return
	 */
	int getTotalPage();
	
	
}
