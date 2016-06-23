/**
 * 
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class KeepalivedService extends AbstractService {
	
	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = 6844540484545872562L;
	
	public static final String TYPE = "Keepalived";
	
	private static final String NOTIFICATION_EMAIL = "notificationEmail";
	private static final String NOTIFICATION_EMAIL_FROM = "notificationEmailFrom";
	private static final String SMTP_SERVER = "smtpServer";
	private static final String SMTP_CONNECT_TIMEOUT = "smtpConnectTimeout";
	private static final String ROUTER_ID = "routerId";
	private static final String VRRP_SCRIPT_PATH = "vrrpScriptPath";
	private static final String VRRP_SCRIPT_INTERVAL = "vrrpScriptInterval";
	private static final String VRRP_SCRIPT_WEIGHT = "vrrpScriptWeight";
	private static final String VRRP_STATE = "vrrpState";
	private static final String INTERFACE = "interface";
	private static final String VIRTUAL_ROUTER_ID = "virtualRouterId";
	private static final String MCAST_SRC_IP = "mcastSrcIp";
	private static final String PRIORITY = "priority";
	private static final String ADVERT_INT = "advertInt";
	private static final String AUTH_TYPE = "authType";
	private static final String AUTH_PASS = "authPass";
	private static final String VIRTUAL_IP_ADDRESS = "virtualIpAddress";
	
	/*** master ***/
	public static final String VRRP_STATE_MASTER = "MASTER";
	/*** backup ***/
	public static final String VRRP_STATE_BACKUP = "BACKUP";
	
	/**
	 * 
	 */
	public KeepalivedService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	/**
	 * lizw@primeton.com,liyp@primeton.com ... . <br>
	 * 
	 * @return
	 */
	public String getNotificationEmail() {
		return getValue(NOTIFICATION_EMAIL, "root@localhost");
	}
	
	/**
	 * lizw@primeton.com,liyp@primeton.com ... <br>
	 * 
	 * @param notificationEmail lizw@primeton.com,liyp@primeton.com ...
	 */
	public void setNotificationEmail(String notificationEmail) {
		setValue(NOTIFICATION_EMAIL, notificationEmail);
	}
	
	/**
	 * Sender <br>
	 * 
	 * @return
	 */
	public String getNotificationEmailFrom() {
		return getValue(NOTIFICATION_EMAIL_FROM);
	}
	
	public void setNotificationEmailFrom(String notificationEmailFrom) {
		setValue(NOTIFICATION_EMAIL_FROM, notificationEmailFrom);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSmtpServer() {
		return getValue(SMTP_SERVER, "localhost"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param smtpServer
	 */
	public void setSmtpServer(String smtpServer) {
		setValue(SMTP_SERVER, smtpServer);
	}
	
	/**
	 * 
	 * @return
	 */
	public long getSmtpConnectTimeout() {
		return getValue(SMTP_CONNECT_TIMEOUT, 30);
	}
	
	/**
	 * 
	 * @param smtpConnectTimeout
	 */
	public void setSmtpConnectTimeout(long smtpConnectTimeout) {
		setValue(SMTP_CONNECT_TIMEOUT, smtpConnectTimeout);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRouterId() {
		return getValue(ROUTER_ID, "LVS_DEVEL"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param routerId
	 */
	public void setRouterId(String routerId) {
		setValue(ROUTER_ID, routerId);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getVrrpScriptPath() {
		return getValue(VRRP_SCRIPT_PATH);
	}
	
	/**
	 * 
	 * @param vrrpScriptPath
	 */
	public void setVrrpScriptPath(String vrrpScriptPath) {
		setValue(VRRP_SCRIPT_PATH, vrrpScriptPath);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getVrrpScriptInterval() {
		return getValue(VRRP_SCRIPT_INTERVAL, 5); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param vrrpScriptInterval
	 */
	public void setVrrpScriptInterval(int vrrpScriptInterval) {
		setValue(VRRP_SCRIPT_INTERVAL, vrrpScriptInterval);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getVrrpScriptWeight() {
		return getValue(VRRP_SCRIPT_WEIGHT, 2); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param vrrpScriptWeight
	 */
	public void setVrrpScriptWeight(int vrrpScriptWeight) {
		setValue(VRRP_SCRIPT_WEIGHT, vrrpScriptWeight);
	}
	
	/**
	 * MASTER | BACKUP <br>
	 * 
	 * @return MASTER | BACKUP
	 */
	public String getVrrpState() {
		return getValue(VRRP_STATE, "MASTER"); //$NON-NLS-1$
	}
	
	/**
	 * MASTER | BACKUP <br>
	 * 
	 * @param vrrpState MASTER | BACKUP
	 */
	public void setVrrpState(String vrrpState) {
		setValue(VRRP_STATE, vrrpState);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getInterface() {
		return getValue(INTERFACE, "eth0"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param _interface
	 */
	public void setInterface(String _interface) {
		setValue(INTERFACE, _interface);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getVirtualRouterId() {
		return getValue(VIRTUAL_ROUTER_ID, "51"); //$NON-NLS-1$
	}
	
	/**
	 * virtual_router_id 51 <br>
	 * 
	 * @param virtualRouterId
	 */
	public void setVirtualRouterId(String virtualRouterId) {
		setValue(VIRTUAL_ROUTER_ID, virtualRouterId);
	}
	
	/**
	 * mcast_src_ip 192.168.100.190 <br>
	 * 
	 * @return
	 */
	public String getMcastSrcIp() {
		return getValue(MCAST_SRC_IP);
	}
	
	/**
	 * mcast_src_ip 192.168.100.190 <br>
	 * 
	 * @param mcastSrcIp
	 */
	public void setMcastSrcIp(String mcastSrcIp) {
		setValue(MCAST_SRC_IP, mcastSrcIp);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getPriority() {
		return getValue(PRIORITY, 0); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param priority
	 */
	public void setPriority(int priority) {
		setValue(PRIORITY, priority);
	}
	
	/**
	 * advert_int 1 <br>
	 * 
	 * @return
	 */
	public int getAdvertInt() {
		return getValue(ADVERT_INT, 1);
	}
	
	/**
	 * 
	 * @param advertInt
	 */
	public void setAdvertInt(int advertInt) {
		setValue(ADVERT_INT, advertInt);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAuthType() {
		return getValue(AUTH_TYPE, "PASS"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param authType
	 */
	public void setAuthType(String authType) {
		setValue(AUTH_TYPE, authType);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAuthPass() {
		return getValue(AUTH_PASS, "1111"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param authPass
	 */
	public void setAuthPass(String authPass) {
		setValue(AUTH_PASS, authPass);
	}

	/**
	 * virtual_ipaddress <br>
	 * exp:192.168.100.20/24,192.168.100.21/24... <br>
	 * 
	 * @return
	 */
	public String getVirtualIpAddress() {
		return getValue(VIRTUAL_IP_ADDRESS);
	}
	
	/**
	 * virtual_ipaddress <br>
	 * exp:192.168.100.20/24,192.168.100.21/24... <br>
	 * 
	 */
	public void setVirtualIpAddress(String virtualIpAddress) {
		setValue(VIRTUAL_IP_ADDRESS, virtualIpAddress);
	}
	
}
