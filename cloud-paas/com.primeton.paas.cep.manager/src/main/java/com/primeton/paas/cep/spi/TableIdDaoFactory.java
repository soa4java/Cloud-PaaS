/**
 * 
 */
package com.primeton.paas.cep.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class TableIdDaoFactory {
	
	private static TableIdDao DAO = null;
	
	private TableIdDaoFactory() {
		super();
	}
	
	public static TableIdDao getDao() {
		synchronized (TableIdDaoFactory.class) {
			if (DAO == null) {
				ServiceLoader<TableIdDao> loader = ServiceLoader.load(TableIdDao.class);
				if (loader != null) {
					Iterator<TableIdDao> iterator = loader.iterator();
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
