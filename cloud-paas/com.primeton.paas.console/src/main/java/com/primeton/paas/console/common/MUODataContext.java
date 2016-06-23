/**
 * 
 */
package com.primeton.paas.console.common;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MUODataContext implements IMUODataContext {
	
	private  IUserObject userObject;

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.console.common.datacontext.IMUODataContext#getUserObject()
	 */
	public IUserObject getUserObject() {
		return userObject;
	}
	
	/**
	 * 
	 * @param userObject
	 */
	public MUODataContext(IUserObject userObject) {
		this.userObject = userObject;
	}

}

  