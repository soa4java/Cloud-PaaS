/**
 * 
 */
package com.primeton.paas.manage.api.impl.resource;

import com.primeton.paas.common.spi.AbstractStartupListener;
import com.primeton.paas.manage.spi.resource.IStoragePoolStartup;
import com.primeton.paas.manage.spi.resource.StoragePoolFactory;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class StoragePoolStartupListsener extends AbstractStartupListener {
	
	private IStoragePoolStartup manager;
	
	/**
	 * Default. <br>
	 */
	public StoragePoolStartupListsener() {
		super();
		manager = StoragePoolFactory.getStartup();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.common.spi.AbstractStartupListener#doStart()
	 */
	protected void doStart() {
		if (manager != null) {
			manager.start();
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.common.spi.AbstractStartupListener#doStop()
	 */
	protected void doStop() {
		if (manager != null) {
			manager.stop();
		}
	}

}
