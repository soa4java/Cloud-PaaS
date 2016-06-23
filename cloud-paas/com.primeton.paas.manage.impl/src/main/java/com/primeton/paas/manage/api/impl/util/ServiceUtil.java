/**
 * 
 */
package com.primeton.paas.manage.api.impl.util;

import com.primeton.paas.manage.api.factory.ServiceFactory;
import com.primeton.paas.manage.api.model.IService;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ServiceUtil {

	/**
	 * Default. <br>
	 */
	private ServiceUtil() {
		super();
	}

	/**
	 * 
	 * @param service
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IService> T copy(T service) {
		if (service == null) {
			return null;
		}
		IService t = ServiceFactory.newInstance(service.getType());
		t.setCreatedBy(service.getCreatedBy());
		t.setCreatedDate(service.getCreatedDate());
		t.setId(service.getId());
		t.setIp(service.getIp());
		t.setName(service.getName());
		t.setOwner(service.getOwner());
		t.setParentId(service.getParentId());
		t.setPid(service.getPid());
		t.setPort(service.getPort());
		t.setStartDate(service.getStartDate());
		t.setState(service.getState());
		t.getAttributes().putAll(service.getAttributes());
		return (T) t;
	}
	
}
