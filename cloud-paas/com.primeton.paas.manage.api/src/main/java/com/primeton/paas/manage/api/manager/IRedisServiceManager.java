/**
 *
 */
package com.primeton.paas.manage.api.manager;

import java.util.List;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.service.RedisService;

/**
 * Redis 服务管理器. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IRedisServiceManager extends IServiceManager {
	
	/**
	 * New service instance. <br>
	 * 
	 * @param service instance
	 * @param clusterId cluster
	 * @param number 1 (master) + N (slave)
	 * @return redis instances
	 * @throws ServiceException
	 */
	List<RedisService> add(RedisService service, String clusterId, int number)
			throws ServiceException;
	
}
