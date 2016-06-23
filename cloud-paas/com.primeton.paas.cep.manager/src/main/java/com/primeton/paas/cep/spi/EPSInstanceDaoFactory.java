/**
 * 
 */
package com.primeton.paas.cep.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EPSInstanceDaoFactory {
	
	private static EPSInstanceDao DAO = null;
	
	private EPSInstanceDaoFactory() {
		super();
	}
	
	public static EPSInstanceDao getDao() {
		
		synchronized (EPSInstanceDaoFactory.class) {
			if (DAO == null) {
				ServiceLoader<EPSInstanceDao> loader = ServiceLoader.load(EPSInstanceDao.class);
				if (loader != null) {
					Iterator<EPSInstanceDao> iterator = loader.iterator();
					while (iterator.hasNext()) {
						DAO = iterator.next();
						if (DAO != null) {
							break;
						}
					}
				}
			}
		}
		
		return DAO;
	}

}
