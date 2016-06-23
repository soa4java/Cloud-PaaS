/**
 * 
 */
package com.primeton.paas.console.common;

import java.util.HashMap;

/**
 *
 * @author liming(li-ming@primeton.com)
 */
@SuppressWarnings({ "rawtypes" })
public class DataContextManager extends HashMap {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3969431266797694133L;
	
	protected static ThreadLocal currentBus = new ThreadLocal();
	private IMUODataContext muoDataContext;

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DataContextManager current() {
		DataContextManager obj = (DataContextManager) currentBus.get();
		if (null == obj) {
			obj = new DataContextManager();
			currentBus.set(obj);
		}
		return obj;
	}

	/**
	 * 
	 * @return
	 */
	public IMUODataContext getMUODataContext() {
		return muoDataContext;
	}

	/**
	 * 
	 * @param muoDataContext
	 * @return
	 */
	public IMUODataContext setMUODataContext(IMUODataContext muoDataContext) {
		IMUODataContext old = this.muoDataContext;
		this.muoDataContext = muoDataContext;
		return old;
	}

}
