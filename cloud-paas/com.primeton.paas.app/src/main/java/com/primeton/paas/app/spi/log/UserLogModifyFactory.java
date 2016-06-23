/**
 * 
 */
package com.primeton.paas.app.spi.log;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import com.primeton.paas.app.api.log.IUserLogModify;

/**
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class UserLogModifyFactory {
	
	private static List<IUserLogModify> modifies = new ArrayList<IUserLogModify>();
	
	private UserLogModifyFactory() {
		super();
	}

	/**
	 * 
	 * @return
	 */
	public static List<IUserLogModify> getModifies() {
		if (null == modifies || modifies.isEmpty()) {
			ServiceLoader<IUserLogModify> loader = ServiceLoader
					.load(IUserLogModify.class);
			if (loader != null) {
				for (IUserLogModify modify : loader) {
					modifies.add(modify);
				}
			}
		}
		return modifies;
	}
	
}
