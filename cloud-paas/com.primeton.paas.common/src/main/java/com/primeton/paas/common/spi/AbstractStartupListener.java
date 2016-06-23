/**
 * 
 */
package com.primeton.paas.common.spi;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public abstract class AbstractStartupListener implements IStartupListener {
	
	private boolean isStarted = false;
	
	/**
	 * start. <br>
	 */
	public void start() {
		if (isStarted) {
			return;
		}
		try {
			doStart();
		} finally {
			isStarted = true;
		}
	}
	
	/**
	 * stop. <br>
	 */
	public void stop() {
		if (!isStarted) {
			return;
		}
		try {
			doStop();
		} finally {
			isStarted = false;
		}
	}
	
	/**
	 * Handle start action <br>
	 */
	protected abstract void doStart();
	
	/**
	 * Handle stop action <br>
	 */
	protected abstract void doStop();
	
}
