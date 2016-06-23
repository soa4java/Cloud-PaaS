/**
 * 
 */
package com.primeton.paas.manage.api.impl.resource;

import com.primeton.paas.common.spi.AbstractStartupListener;
import com.primeton.paas.manage.spi.resource.IVmPoolStartup;
import com.primeton.paas.manage.spi.resource.VmPoolFactory;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VmPoolStartupListsener extends AbstractStartupListener {
	
	private IVmPoolStartup manager;
	
	/**
	 * 
	 */
	public VmPoolStartupListsener() {
		super();
		manager = VmPoolFactory.getStartup();
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
