/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * Redis 哨兵服务. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RedisSentinelService extends AbstractService {

	/**
	 * serialVersionUID. <br>
	 */
	private static final long serialVersionUID = 5736527971993357096L;

	/**
	 * Type of Redis Sentinel Service. <br>
	 */
	public static final String TYPE = "RedisSentinel";
	
	/**
	 * down-after-milliseconds (ms)
	 */
	private static final String DOWN_TIME = "down_time";
	
	/**
	 * failover-timeout (ms)
	 */
	private static final String FAILOVER_TIMEOUT = "failover_timeout";
	
	/**
	 * parallel-syncs
	 */
	private static final String PARALLEL_SYNCS = "parallel_syncs";
	
	/**
	 * Min sentinels agree
	 */
	private static final String MIN_AGREES = "min_agrees";

	/**
	 * Default. <br>
	 */
	public RedisSentinelService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	/**
	 * (ms). <br>
	 * 
	 * @return
	 */
	public long getDownTime() {
		return getValue(DOWN_TIME, 60000L);
	}
	
	/**
	 * (ms). <br>
	 * 
	 * @param downTime
	 */
	public void setDownTime(long downTime) {
		setValue(DOWN_TIME, downTime > 0L ? downTime : 60000L);
	}
	
	/**
	 * (ms). <br>
	 * 
	 * @return
	 */
	public long getFailoverTimeout() {
		return getValue(FAILOVER_TIMEOUT, 180000L);
	}
	
	/**
	 * (ms). <br>
	 * 
	 * @param timeout
	 */
	public void setFailoverTimeout(long timeout) {
		setValue(FAILOVER_TIMEOUT, timeout > 0 ? timeout : 180000L);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getParallelSyncs() {
		return getValue(PARALLEL_SYNCS, 1);
	}
	
	/**
	 * 
	 * @param syncs
	 */
	public void setParallelSyncs(int syncs) {
		setValue(PARALLEL_SYNCS, syncs > 0 ? syncs : 1);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMinAgree() {
		return getValue(MIN_AGREES, 1);
	}
	
	/**
	 * 
	 * @param minAgree
	 */
	public void setMinAgree(int minAgree) {
		setValue(MIN_AGREES, minAgree > 0 ? minAgree : 1);
	}
	
}
