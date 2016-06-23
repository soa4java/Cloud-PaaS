/**
 * 
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class HaProxyService extends AbstractService {

	private static final long serialVersionUID = 7157797061856071221L;

	public static final String TYPE = "HaProxy";
	
	private static final String URL_HEALTH_CHECK_INTERVAL = "urlHealthCheckInterval";
	private static final String HEALTH_URL = "healthUrl";
	private static final String MAX_CONNECTION_SIZE = "maxConnectionSize";
	private static final String BALANCE = "balance";
	private static final String SESSION_TIMEOUT = "sessionTimeout";
	private static final String CONN_TIMEOUT = "connTimeout";
	private static final String PROTOCAL = "protocal";
	
	public static final String BALANCE_ROUNDROBIN = "roundrobin";
	public static final String BALANCE_LEASTCONN = "leastconn";
	public static final String BALANCE_SOURCE = "source";
	
	
	public HaProxyService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	public long getConnTimeout(){
		return getValue(CONN_TIMEOUT, 3600L);
	}
	
	public void setConnTimeout(long connTimeout){
		setValue(CONN_TIMEOUT, connTimeout);
	}
	
	public String getProtocal(){
		return getValue(PROTOCAL);
	}
	
	public void setProtocal(String protocal){
		setValue(PROTOCAL, protocal);
	}
	
	public long getUrlHealthCheckInterval() {
		return getValue(URL_HEALTH_CHECK_INTERVAL, 2000L);
	}
	
	public void setUrlHealthCheckInterval(long urlHealthCheckInterval) {
		setValue(URL_HEALTH_CHECK_INTERVAL, urlHealthCheckInterval);
	}
	
	public String getHealthUrl() {
		return getValue(HEALTH_URL, "/");
	}
	
	public void setHealthUrl(String healthUrl) {
		setValue(HEALTH_URL, healthUrl);
	}
	
	public int getMaxConnectionSize() {
		return getValue(MAX_CONNECTION_SIZE, 1000);
	}
	
	public void setMaxConnectionSize(int maxConnectionSize) {
		setValue(MAX_CONNECTION_SIZE, maxConnectionSize);
	}
	
	public String getBalance() {
		return getValue(BALANCE, BALANCE_ROUNDROBIN);
	}
	
	public void setBalance(String balance) {
		setValue(BALANCE, balance);
	}
	
	/**
	 * unit: minute <br>
	 * 
	 * @return
	 */
	public int getSessionTimeout() {
		return getValue(SESSION_TIMEOUT, 30);
	}
	
	public void setSessionTimeout(int sessionTimeout) {
		setValue(SESSION_TIMEOUT, sessionTimeout);
	}
}
